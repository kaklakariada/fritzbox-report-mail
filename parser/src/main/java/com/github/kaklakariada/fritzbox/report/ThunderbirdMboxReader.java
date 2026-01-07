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
package com.github.kaklakariada.fritzbox.report;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.mboxiterator.CharBufferWrapper;
import org.apache.james.mime4j.mboxiterator.MboxIterator;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.apache.james.mime4j.stream.MimeConfig;

public class ThunderbirdMboxReader {
    private static final Logger LOG = Logger.getLogger(ThunderbirdMboxReader.class.getName());

    /**
     * Matches other type of From_ line (without @): From MAILER-DAEMON Wed Oct 05
     * 21:54:09 2011 Thunderbird mbox content: From - Wed Apr 02 06:51:08 2014
     */
    private static final String DEFAULT2 = "^From (\\S+.*\\d{4})?$";

    private static final Charset CHARSET_ENCODING = StandardCharsets.UTF_8;
    private final MessageBuilder messageBuilder = createMessageBuilder();

    private DefaultMessageBuilder createMessageBuilder() {
        final DefaultMessageBuilder builder = new DefaultMessageBuilder();
        final MimeConfig config = new MimeConfig.Builder()
                .setMaxContentLen(20 * 100 * 1024 * 1024L)
                .setMaxHeaderCount(-1)
                .setMaxHeaderLen(-1)
                .setMaxLineLen(-1)
                .setStrictParsing(false)
                .setMalformedHeaderStartsBody(true)
                .build();
        builder.setMimeEntityConfig(config);
        return builder;
    }

    public Stream<RawMessage> readMbox(final Path mboxFile) {
        LOG.fine(() -> "Reading raw mails from mbox " + mboxFile + "...");
        return readCharBufferStream(mboxFile).map(this::convert);
    }

    private Stream<CharBufferWrapper> readCharBufferStream(final Path mboxFile) {
        // parallel processing not supported by MboxIterator
        final boolean parallel = false;
        return StreamSupport.stream(createIterator(mboxFile).spliterator(), parallel);
    }

    private MboxIterator createIterator(final Path mboxFile) {
        try {
            return MboxIterator.fromFile(mboxFile.toFile())
                    .charset(CHARSET_ENCODING)
                    .fromLine(DEFAULT2)
                    .build();
        } catch (final IOException exception) {
            throw new UncheckedIOException("error reading mbox " + mboxFile, exception);
        }
    }

    private RawMessage convert(final CharBufferWrapper message) {
        return new RawMessage(parseMessage(message));
    }

    private Message parseMessage(final CharBufferWrapper message) {
        try (InputStream input = message.asInputStream(CHARSET_ENCODING)) {
            return messageBuilder.parseMessage(input);
        } catch (final IOException e) {
            throw new UncheckedIOException("Error parsing message " + message, e);
        } catch (final MimeException e) {
            throw new IllegalStateException("Error parsing message " + message, e);
        }
    }
}
