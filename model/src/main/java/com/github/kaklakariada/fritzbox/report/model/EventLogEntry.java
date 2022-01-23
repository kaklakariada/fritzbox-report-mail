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
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class EventLogEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int reportId;
    private final int logEntryId;
    private final LocalDateTime timestamp;
    private final String message;
    private final Event event;

    public EventLogEntry(int reportId, int logEntryId, final LocalDateTime timestamp, final String message,
            final Event event) {
        this.reportId = reportId;
        this.logEntryId = logEntryId;
        this.timestamp = timestamp;
        this.message = message;
        this.event = event;
    }

    public int getReportId() {
        return reportId;
    }

    public int getLogEntryId() {
        return logEntryId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Optional<Event> getEvent() {
        return Optional.ofNullable(event);
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
        return Objects.hash(event, logEntryId, message, reportId, timestamp);
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
        return Objects.equals(event, other.event) && logEntryId == other.logEntryId
                && Objects.equals(message, other.message) && reportId == other.reportId
                && Objects.equals(timestamp, other.timestamp);
    }
}
