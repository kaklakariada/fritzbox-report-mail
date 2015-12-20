package com.github.kaklakariada.fritzbox.report.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.github.kaklakariada.fritzbox.report.model.DataConnections.TimePeriod;

public class FritzBoxReportMail implements Serializable {

    private static final long serialVersionUID = 1L;
    private final LocalDate date;
    private final Map<TimePeriod, DataConnections> dataConnections;
    private final List<EventLogEntry> eventLog;
    private final List<InternetConnection> connections;

    public FritzBoxReportMail(final LocalDate date, final Map<TimePeriod, DataConnections> dataConnections,
            final List<EventLogEntry> eventLog, final List<InternetConnection> connections) {
        this.date = date;
        this.dataConnections = dataConnections;
        this.eventLog = eventLog;
        this.connections = connections;
    }

    public LocalDate getDate() {
        return date;
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
