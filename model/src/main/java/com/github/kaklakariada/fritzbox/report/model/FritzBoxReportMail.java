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
import java.util.*;

import com.github.kaklakariada.fritzbox.report.model.DataConnections.TimePeriod;

public class FritzBoxReportMail implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int reportId;
    private final Map<TimePeriod, DataConnections> dataConnections;
    private final List<EventLogEntry> eventLog;
    private final List<InternetConnection> connections;
    private final EmailMetadata emailMetadata;
    private final FritzBoxInfo fritzBoxInfo;
    private final LocalDate date;

    public FritzBoxReportMail(final int reportId, final LocalDate date, final EmailMetadata emailMetadata,
            final FritzBoxInfo fritzBoxInfo,
            final Map<TimePeriod, DataConnections> dataConnections,
            final List<EventLogEntry> eventLog, final List<InternetConnection> connections) {
        this.reportId = Objects.requireNonNull(reportId, "reportId");
        this.date = Objects.requireNonNull(date, "date");
        this.emailMetadata = Objects.requireNonNull(emailMetadata, "emailMetadata");
        this.fritzBoxInfo = Objects.requireNonNull(fritzBoxInfo, "fritzBoxInfo");
        this.dataConnections = Objects.requireNonNull(dataConnections, "dataConnections");
        this.eventLog = Objects.requireNonNull(eventLog, "eventLog");
        this.connections = Objects.requireNonNull(connections, "connections");
    }

    public int getReportId() {
        return reportId;
    }

    public LocalDate getDate() {
        return date;
    }

    public EmailMetadata getEmailMetadata() {
        return emailMetadata;
    }

    public FritzBoxInfo getFritzBoxInfo() {
        return fritzBoxInfo;
    }

    public Map<TimePeriod, DataConnections> getDataConnections() {
        return dataConnections;
    }

    public List<EventLogEntry> getEventLog() {
        return eventLog;
    }

    public List<InternetConnection> getConnections() {
        return connections;
    }
}
