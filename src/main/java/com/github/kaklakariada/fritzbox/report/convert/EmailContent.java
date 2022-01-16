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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.apache.james.mime4j.dom.Message;

import com.github.kaklakariada.fritzbox.report.convert.EmailBody.Type;

public class EmailContent {
    private final List<EmailBody> parts;
    private final Message message;

    public EmailContent(Message message, List<EmailBody> parts) {
        this.message = message;
        this.parts = parts;
    }

    public List<EmailBody> getParts() {
        return parts;
    }

    public EmailBody getPart(Type type) {
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
        return LocalDate.ofInstant(message.getDate().toInstant(), ZoneId.systemDefault());
    }

    @Override
    public String toString() {
        return "EmailContent [part count=" + parts.size() + ", parts=" + parts + "]";
    }
}
