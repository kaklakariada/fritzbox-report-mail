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
package com.github.kaklakariada.fritzbox.report.convert;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.james.mime4j.dom.*;

import com.github.kaklakariada.fritzbox.report.RawMessage;

import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;

public class MessageHtmlTextBodyConverter implements Function<RawMessage, EmailContent> {

    @Override
    public EmailContent apply(final RawMessage msg) {
        try {
            return new EmailConverter(msg).processRootBody();
        } catch (final RuntimeException e) {
            throw new IllegalStateException("Failed to parse message " + msg, e);
        }
    }

    private static class EmailConverter {

        private final RawMessage message;

        EmailConverter(final RawMessage message) {
            this.message = message;
        }

        private EmailContent processRootBody() {
            final List<EmailBody> parts = processBody(message.message().getBody());
            return new EmailContent(message, parts);
        }

        private List<EmailBody> processBody(final Body body) {
            if (body instanceof final SingleBody singleBody) {
                try {
                    final String charset = body.getParent().getCharset();
                    try (InputStream inputStream = singleBody.getInputStream()) {
                        final String string = new String(inputStream.readAllBytes(),
                                Charset.forName(charset));
                        return List.of(new EmailBody(string));
                    }
                } catch (final IOException e) {
                    throw new UncheckedIOException("Failed to read email body " + body, e);
                }
            } else if (body instanceof final Multipart multipartBody) {
                return multipartBody.getBodyParts().stream()
                        .map(Entity::getBody)
                        .map(this::processBody)
                        .flatMap(List::stream)
                        .toList();
            } else {
                throw new IllegalStateException("Unexpected body type " + body.getClass().getName());
            }
        }

        private List<EmailBody> getContent(final BodyPart content) throws MessagingException, IOException {
            if (content.getContent() instanceof final String stringContent) {
                return List.of(new EmailBody(stringContent));
            } else if (content.getContent() instanceof final MimeMultipart multipartContent) {
                return getHtmlBodyParts(multipartContent);
            }
            throw new IllegalStateException("Unknown content type " + content.getContent());
        }

        private List<EmailBody> getHtmlBodyParts(final MimeMultipart content) throws MessagingException, IOException {
            final ArrayList<EmailBody> bodies = new ArrayList<>();
            for (int i = 0; i < content.getCount(); i++) {
                final MimeBodyPart part = (MimeBodyPart) content.getBodyPart(i);
                bodies.addAll(getContent(part));
            }
            return bodies;
        }
    }
}
