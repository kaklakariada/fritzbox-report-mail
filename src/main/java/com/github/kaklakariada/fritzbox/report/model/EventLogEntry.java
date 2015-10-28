package com.github.kaklakariada.fritzbox.report.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class EventLogEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private final LocalDateTime timestamp;
    private final String message;
    private final Event event;

    public EventLogEntry(final LocalDateTime timestamp, final String message, final Event event) {
        this.timestamp = timestamp;
        this.message = message;
        this.event = event;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "EventLogEntry [" + timestamp + ": " + getDescription() + "]";
    }

    private String getDescription() {
        return event != null ? event.getDescription() : getMessage();
    }
}
