package org.chris.fritzbox.reports.mail.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.chris.fritzbox.reports.mail.model.DataConnections.TimePeriod;

public class FritzBoxReportMail implements Serializable {

    private static final long serialVersionUID = 1L;
    private final LocalDateTime date;
    private final Map<TimePeriod, DataConnections> dataConnections;
    private final List<EventLogEntry> eventLog;
    private final List<InternetConnection> connections;

    public FritzBoxReportMail(final LocalDateTime date, final Map<TimePeriod, DataConnections> dataConnections,
            final List<EventLogEntry> eventLog, final List<InternetConnection> connections) {
        this.date = date;
        this.dataConnections = dataConnections;
        this.eventLog = eventLog;
        this.connections = connections;
    }

    public LocalDateTime getDate() {
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
