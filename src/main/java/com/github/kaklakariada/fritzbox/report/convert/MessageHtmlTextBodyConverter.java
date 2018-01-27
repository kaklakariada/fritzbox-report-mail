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

import java.util.List;
import java.util.function.Function;

import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.dom.TextBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHtmlTextBodyConverter implements Function<Message, TextBody> {
    final static Logger logger = LoggerFactory.getLogger(MessageHtmlTextBodyConverter.class);

    @Override
    public TextBody apply(final Message msg) {
        final Multipart body = (Multipart) msg.getBody();
        final List<Entity> bodyParts = body.getBodyParts();
        if (bodyParts.size() != 2) {
            throw new AssertionError("Expected 2 but got " + bodyParts.size());
        }
        final Entity part0 = bodyParts.get(0);
        final Entity part1 = bodyParts.get(1);
        logger.trace("part0: {}, part1: {}", part0.getMimeType(), part1.getMimeType());

        switch (part0.getMimeType()) {
        case "multipart/related":
            final Multipart part0body = (Multipart) part0.getBody();
            if (part0body.getBodyParts().size() != 2) {
                throw new AssertionError("Expected 2 parts but got " + part0body.getBodyParts());
            }
            final String mimeType = part0body.getBodyParts().get(0).getMimeType();
            if (!mimeType.equals("text/html")) {
                throw new AssertionError("Expected mime type text/html but got " + mimeType);
            }
            return convert(part0body.getBodyParts().get(0));
        case "text/html":
            return convert(part0);
        case "text/plain":
            switch (part1.getMimeType()) {
            case "text/html":
                return convert(part1);
            case "multipart/related":
                final Multipart part1body = (Multipart) part1.getBody();
                if (part1body.getBodyParts().size() != 2) {
                    throw new AssertionError("Expected 2 parts but got " + part1body.getBodyParts());
                }

                final String mimeType2 = part1body.getBodyParts().get(0).getMimeType();
                if (!mimeType2.equals("text/html")) {
                    throw new AssertionError("Expected mime type text/html but got " + mimeType2);
                }
                return convert(part1body.getBodyParts().get(0));
            default:
                throw new AssertionError("Unexpected mime type of part 1: " + part1.getMimeType());
            }
        default:
            throw new AssertionError("Unexpected mime type of part 0: " + part0.getMimeType());
        }
    }

    private TextBody convert(final Entity entity) {
        if (!entity.getMimeType().equals("text/html")) {
            throw new AssertionError("Expected mime type text/html but got " + entity.getMimeType());
        }
        return (TextBody) entity.getBody();
    }
}
