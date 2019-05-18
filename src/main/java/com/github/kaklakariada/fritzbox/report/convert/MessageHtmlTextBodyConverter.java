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

import java.io.IOException;
import java.util.Properties;
import java.util.function.Function;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.TextBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHtmlTextBodyConverter implements Function<Message, EmailBody> {
    private static final String TEXT_PLAIN = "text/plain";
    private static final String TEXT_HTML = "text/html";
    private static final String MULTIPART_RELATED = "multipart/related";
    private static final Logger logger = LoggerFactory.getLogger(MessageHtmlTextBodyConverter.class);

    @Override
    public EmailBody apply(final Message msg) {

        final TextBody textBody = (TextBody) msg.getBody();

        final Session session = Session.getDefaultInstance(new Properties());
        try {
            final MimeMessage mimeMessage = new MimeMessage(session, textBody.getInputStream());
            final MimeMultipart content = (MimeMultipart) mimeMessage.getContent();
            if (content.getCount() != 2) {
                throw new AssertionError("Expected 2 but got " + content.getCount());
            }

            return getContent(getHtmlBodyPart(content));
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private EmailBody getContent(final BodyPart content) throws MessagingException, IOException {
        if (content.getContent() instanceof String) {
            return new EmailBody(content.getContentType(), (String) content.getContent());
        } else if (content.getContent() instanceof MimeMultipart) {
            final MimeMultipart content2 = (MimeMultipart) content.getContent();
            return getContent(getHtmlBodyPart(content2));
        } else {
            throw new IllegalStateException("Unknown content type " + content.getContent());
        }
    }

    private MimeBodyPart getHtmlBodyPart(MimeMultipart content) throws MessagingException {
        for (int i = 0; i < content.getCount(); i++) {
            final MimeBodyPart part = (MimeBodyPart) content.getBodyPart(i);
            if (part.getContentType().equals("text/html; charset=\"utf-8\"")) {
                return part;
            }
            if (part.getContentType().equals("text/html; charset=\"iso-8859-1\"")) {
                return part;
            }
            if (part.getContentType().startsWith("multipart/related;")) {
                return part;
            }
        }
        throw new IllegalStateException("No html body part found for " + content);
    }
}
