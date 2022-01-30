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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Wraps a CharBuffer and exposes some convenience methods to easy parse with
 * Mime4j.
 */
public class CharBufferWrapper {

    private final CharBuffer messageBuffer;

    public CharBufferWrapper(CharBuffer messageBuffer) {
        if (messageBuffer == null) {
            throw new IllegalStateException("The buffer is null");
        }
        this.messageBuffer = messageBuffer;
    }

    public InputStream asInputStream(Charset encoding) {
        return new ByteBufferInputStream(encoding.encode(messageBuffer));
    }

    public char[] asCharArray() {
        return messageBuffer.array();
    }

    @Override
    public String toString() {
        return messageBuffer.toString();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharBufferWrapper)) {
            return false;
        }

        final CharBufferWrapper that = (CharBufferWrapper) o;
        if (this.messageBuffer == null) {
            return that.messageBuffer == null;
        }

        return messageBuffer.equals(that.messageBuffer);
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(messageBuffer);
    }

    /**
     * Provide an InputStream view over a ByteBuffer.
     */
    private static class ByteBufferInputStream extends InputStream {

        private final ByteBuffer buf;

        private ByteBufferInputStream(ByteBuffer buf) {
            this.buf = buf;
        }

        @Override
        public int read() throws IOException {
            if (!buf.hasRemaining()) {
                return -1;
            }
            return buf.get() & 0xFF;
        }

        @Override
        public int read(byte[] bytes, int off, int len) throws IOException {
            if (!buf.hasRemaining()) {
                return -1;
            }
            buf.get(bytes, off, Math.min(len, buf.remaining()));
            return len;
        }

    }
}
