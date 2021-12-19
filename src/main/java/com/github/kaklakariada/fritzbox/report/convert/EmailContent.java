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
