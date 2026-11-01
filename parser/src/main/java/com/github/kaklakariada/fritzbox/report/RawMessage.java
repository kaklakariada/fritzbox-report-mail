package com.github.kaklakariada.fritzbox.report;

import java.time.Instant;

import org.apache.james.mime4j.dom.Message;

public record RawMessage(Message message) {

    public Instant getDate() {
        return message.getDate() != null ? message.getDate().toInstant() : null;
    }

    public String getMessageId() {
        return message.getMessageId();
    }

    public String getSubject() {
        return message.getSubject();
    }
}
