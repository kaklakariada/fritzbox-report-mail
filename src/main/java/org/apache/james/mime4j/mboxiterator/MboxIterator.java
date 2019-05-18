/**
 * A Java API for parsing and processing status report mails of a FritzBox
 * Copyright (C) 2018 Christoph Pirkl <christoph at users.sourceforge.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.apache.james.mime4j.mboxiterator;

import java.io.CharConversionException;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Class that provides an iterator over email messages inside an mbox file. An mbox file is a sequence of email messages
 * separated by From_ lines.
 * </p>
 * <p>
 * Description ot the file format:
 * </p>
 * <ul>
 * <li>http://tools.ietf.org/html/rfc4155</li>
 * <li>http://qmail.org/man/man5/mbox.html</li>
 * </ul>
 */
public class MboxIterator implements Iterable<CharBufferWrapper>, Closeable {

    private final FileInputStream theFile;
    private final CharBuffer mboxCharBuffer;
    private Matcher fromLineMatcher;
    private boolean fromLineFound;
    private final MappedByteBuffer byteBuffer;
    private final CharsetDecoder DECODER;
    /**
     * Flag to signal end of input to {@link java.nio.charset.CharsetDecoder#decode(java.nio.ByteBuffer)} .
     */
    private boolean endOfInputFlag = false;
    private final int maxMessageSize;
    private final Pattern MESSAGE_START;
    private int findStart = -1;
    private int findEnd = -1;
    private final File mbox;

    private MboxIterator(final File mbox,
            final Charset charset,
            final String regexpPattern,
            final int regexpFlags,
            final int MAX_MESSAGE_SIZE)
            throws FileNotFoundException, IOException, CharConversionException {
        // TODO: do better exception handling - try to process some of them maybe?
        this.maxMessageSize = MAX_MESSAGE_SIZE;
        this.MESSAGE_START = Pattern.compile(regexpPattern, regexpFlags);
        this.DECODER = charset.newDecoder();
        this.mboxCharBuffer = CharBuffer.allocate(MAX_MESSAGE_SIZE);
        this.mbox = mbox;
        this.theFile = new FileInputStream(mbox);
        this.byteBuffer = theFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, theFile.getChannel().size());
        initMboxIterator();
    }

    /**
     * initialize the Mailbox iterator
     *
     * @throws IOException
     * @throws CharConversionException
     */
    protected void initMboxIterator() throws IOException {
        decodeNextCharBuffer();
        fromLineMatcher = MESSAGE_START.matcher(mboxCharBuffer);
        fromLineFound = fromLineMatcher.find();
        if (fromLineFound) {
            saveFindPositions(fromLineMatcher);
        } else if (fromLineMatcher.hitEnd()) {
            String path = "";
            if (mbox != null) {
                path = mbox.getPath();
            }
            throw new IllegalArgumentException("File " + path + " does not contain From_ lines that match the pattern '"
                    + MESSAGE_START.pattern() + "'! Maybe not be a valid Mbox or wrong matcher.");
        }
    }

    private void decodeNextCharBuffer() throws CharConversionException {
        final CoderResult coderResult = DECODER.decode(byteBuffer, mboxCharBuffer, endOfInputFlag);
        updateEndOfInputFlag();
        mboxCharBuffer.flip();
        if (coderResult.isError()) {
            if (coderResult.isMalformed()) {
                throw new CharConversionException("Malformed input!");
            } else if (coderResult.isUnmappable()) {
                throw new CharConversionException("Unmappable character!");
            }
        }
    }

    private void updateEndOfInputFlag() {
        if (byteBuffer.remaining() <= maxMessageSize) {
            endOfInputFlag = true;
        }
    }

    private void saveFindPositions(Matcher lineMatcher) {
        findStart = lineMatcher.start();
        findEnd = lineMatcher.end();
    }

    @Override
    public Iterator<CharBufferWrapper> iterator() {
        return new MessageIterator();
    }

    @Override
    public void close() throws IOException {
        theFile.close();
    }

    private class MessageIterator implements Iterator<CharBufferWrapper> {

        @Override
        public boolean hasNext() {
            if (!fromLineFound) {
                try {
                    close();
                } catch (final IOException e) {
                    throw new RuntimeException("Exception closing file!");
                }
            }
            return fromLineFound;
        }

        /**
         * Returns a CharBuffer instance that contains a message between position and limit. The array that backs this
         * instance is the whole block of decoded messages.
         *
         * @return CharBuffer instance
         */
        @Override
        public CharBufferWrapper next() {
            final CharBuffer message;
            fromLineFound = fromLineMatcher.find();
            if (fromLineFound) {
                message = mboxCharBuffer.slice();
                message.position(findEnd + 1);
                saveFindPositions(fromLineMatcher);
                message.limit(fromLineMatcher.start());
            } else {
                /*
                 * We didn't find other From_ lines this means either: - we reached end of mbox and no more messages -
                 * we reached end of CharBuffer and need to decode another batch.
                 */
                if (byteBuffer.hasRemaining()) {
                    // decode another batch, but remember to copy the remaining chars first
                    final CharBuffer oldData = mboxCharBuffer.duplicate();
                    mboxCharBuffer.clear();
                    oldData.position(findStart);
                    while (oldData.hasRemaining()) {
                        mboxCharBuffer.put(oldData.get());
                    }
                    try {
                        decodeNextCharBuffer();
                    } catch (final CharConversionException ex) {
                        throw new RuntimeException(ex);
                    }
                    fromLineMatcher = MESSAGE_START.matcher(mboxCharBuffer);
                    fromLineFound = fromLineMatcher.find();
                    if (fromLineFound) {
                        saveFindPositions(fromLineMatcher);
                    }
                    message = mboxCharBuffer.slice();
                    message.position(fromLineMatcher.end() + 1);
                    fromLineFound = fromLineMatcher.find();
                    if (fromLineFound) {
                        saveFindPositions(fromLineMatcher);
                        message.limit(fromLineMatcher.start());
                    }
                } else {
                    message = mboxCharBuffer.slice();
                    message.position(findEnd + 1);
                    message.limit(message.capacity());
                }
            }
            return new CharBufferWrapper(message);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static Builder fromFile(File filePath) {
        return new Builder(filePath);
    }

    public static Builder fromFile(String file) {
        return new Builder(file);
    }

    public static class Builder {

        private final File file;
        private Charset charset = Charset.forName("UTF-8");
        private String regexpPattern = FromLinePatterns.DEFAULT;
        private int flags = Pattern.MULTILINE;
        /**
         * Default max message size in chars: ~ 10MB chars. If the mbox file contains larger messages they will not be
         * decoded correctly.
         */
        private int maxMessageSize = 10 * 1024 * 1024;

        private Builder(String filePath) {
            this(new File(filePath));
        }

        private Builder(File file) {
            this.file = file;
        }

        public Builder charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public Builder fromLine(String fromLine) {
            this.regexpPattern = fromLine;
            return this;
        }

        public Builder flags(int flags) {
            this.flags = flags;
            return this;
        }

        public Builder maxMessageSize(int maxMessageSize) {
            this.maxMessageSize = maxMessageSize;
            return this;
        }

        public MboxIterator build() throws FileNotFoundException, IOException {
            return new MboxIterator(file, charset, regexpPattern, flags, maxMessageSize);
        }
    }

    /**
     * Utility method to log important details about buffers.
     *
     * @param buffer
     */
    public static String bufferDetailsToString(final Buffer buffer) {
        final StringBuilder sb = new StringBuilder("Buffer details: ");
        sb.append("\ncapacity:\t").append(buffer.capacity())
                .append("\nlimit:\t").append(buffer.limit())
                .append("\nremaining:\t").append(buffer.remaining())
                .append("\nposition:\t").append(buffer.position())
                .append("\nbuffer:\t").append(buffer.isReadOnly())
                .append("\nclass:\t").append(buffer.getClass());
        return sb.toString();
    }
}