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

import java.io.Serializable;
import java.time.*;
import java.util.List;
import java.util.Objects;

import org.apache.james.mime4j.dom.Message;

import com.github.kaklakariada.fritzbox.report.convert.EmailBody.Type;
import com.github.kaklakariada.fritzbox.report.model.EmailMetadata;

public class EmailContent implements Serializable {
    private static final long serialVersionUID = 1L;
    // non-transient instance field of a serializable class declared with a
    // non-serializable type
    @SuppressWarnings("serial")
    private final List<EmailBody> parts;
    private final Instant headerDate;
    private final String subject;
    private final String messageId;

    public EmailContent(final Message message, final List<EmailBody> parts) {
        Objects.requireNonNull(message, "message");
        this.headerDate = message.getDate().toInstant();
        this.messageId = Objects.requireNonNull(message.getMessageId(), "messageId");
        this.subject = Objects.requireNonNull(message.getSubject(), "subject");
        this.parts = Objects.requireNonNull(parts, "parts");
    }

    public List<EmailBody> getParts() {
        return parts;
    }

    public String getSubject() {
        return subject;
    }

    public EmailBody getPart(final Type type) {
        EmailBody found = null;
        for (final EmailBody part : parts) {
            if (part.getType() == type) {
                if (found != null) {
                    throw new IllegalStateException(
                            "Found more than two parts of type " + type + ": " + found + " and " + part);
                }
                found = part;
            }
        }
        return found;
    }

    public LocalDate getDate() {
        return LocalDate.ofInstant(getHeaderDate(), ZoneId.systemDefault());
    }

    private Instant getHeaderDate() {
        return headerDate;
    }

    public EmailMetadata getMetadata() {
        return new EmailMetadata(messageId, headerDate, subject);
    }

    @Override
    public String toString() {
        return "EmailContent [subject=" + subject + ", date=" + headerDate + ", id=" + messageId + ", part count="
                + parts.size() + "]";
    }
}
