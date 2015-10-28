package com.github.kaklakariada.fritzbox.report;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.mboxiterator.CharBufferWrapper;
import org.apache.james.mime4j.mboxiterator.FromLinePatterns;
import org.apache.james.mime4j.mboxiterator.MboxIterator;
import org.apache.james.mime4j.message.DefaultMessageBuilder;

public class ThunderbirdMboxReader {

    private static final Charset CHARSET_ENCODING = Charset.forName("UTF8");
    private final MessageBuilder messageBuilder = new DefaultMessageBuilder();

    public Stream<Message> readMbox(final Path mboxFile) {
        return readCharBufferStream(mboxFile).map(this::convert);
    }

    private Stream<CharBufferWrapper> readCharBufferStream(final Path mboxFile) {
        // parallel processing not supported by MboxIterator
        final boolean parallel = false;
        return StreamSupport.stream(createIterator(mboxFile).spliterator(), parallel);
    }

    private MboxIterator createIterator(final Path mboxFile) {
        try {
            return MboxIterator.fromFile(mboxFile.toFile()) //
                    .charset(CHARSET_ENCODING) //
                    .fromLine(FromLinePatterns.DEFAULT2) //
                    .build();
        } catch (final IOException e) {
            throw new RuntimeException("Error reading from " + mboxFile, e);
        }
    }

    private Message convert(final CharBufferWrapper message) {
        try {
            return messageBuilder.parseMessage(message.asInputStream(CHARSET_ENCODING));
        } catch (MimeException | IOException e) {
            throw new RuntimeException("Error parsing message " + message, e);
        }
    }
}
