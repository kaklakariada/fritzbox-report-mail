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
package com.github.kaklakariada.fritzbox.report.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class EventLogEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private final LocalDate date;
    private final LocalDateTime timestamp;
    private final String message;
    private final Event event;

    public EventLogEntry(LocalDate date, final LocalDateTime timestamp, final String message, final Event event) {
        this.date = Objects.requireNonNull(date, "date");
        this.timestamp = timestamp;
        this.message = message;
        this.event = event;
    }

    public LocalDate getDate() {
        return date;
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
        return Objects.hash(date, event, message, timestamp);
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
        return Objects.equals(date, other.date) && Objects.equals(event, other.event)
                && Objects.equals(message, other.message) && Objects.equals(timestamp, other.timestamp);
    }
}
