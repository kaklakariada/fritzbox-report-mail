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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((event == null) ? 0 : event.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventLogEntry other = (EventLogEntry) obj;
        if (event == null) {
            if (other.event != null) {
                return false;
            }
        } else if (!event.equals(other.event)) {
            return false;
        }
        if (message == null) {
            if (other.message != null) {
                return false;
            }
        } else if (!message.equals(other.message)) {
            return false;
        }
        if (timestamp == null) {
            if (other.timestamp != null) {
                return false;
            }
        } else if (!timestamp.equals(other.timestamp)) {
            return false;
        }
        return true;
    }
}
