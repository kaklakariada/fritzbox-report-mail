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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.report.model.DataConnections.TimePeriod;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceConnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceEvent;

public class FritzBoxReportMail implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(FritzBoxReportMail.class);

    private static final long serialVersionUID = 1L;
    private final int reportId;
    private final Map<TimePeriod, DataConnections> dataConnections;
    private final List<EventLogEntry> eventLog;
    private final List<InternetConnection> connections;
    private final EmailMetadata emailMetadata;
    private final FritzBoxInfo fritzBoxInfo;
    private final LocalDate date;

    public FritzBoxReportMail(int reportId, LocalDate date, EmailMetadata emailMetadata, FritzBoxInfo fritzBoxInfo,
            final Map<TimePeriod, DataConnections> dataConnections,
            final List<EventLogEntry> eventLog, final List<InternetConnection> connections) {
        this.reportId = reportId;
        this.date = date;
        this.emailMetadata = emailMetadata;
        this.fritzBoxInfo = fritzBoxInfo;
        this.dataConnections = dataConnections;
        this.eventLog = eventLog;
        this.connections = connections;
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

    public List<WifiConnection> getWifiConnections() {
        final List<EventLogEntry> wifiEvents = eventLog.stream().filter(e -> e.getEvent().isPresent())
                .filter(e -> (e.getEvent().get() instanceof WifiDeviceEvent))
                .sorted(Comparator.comparing(EventLogEntry::getTimestamp))
                .toList();
        final List<WifiConnection> wifiConnections = new ArrayList<>();

        final Map<String, EventLogEntry> connectionStartEvents = new HashMap<>();

        for (final EventLogEntry logEntry : wifiEvents) {
            final WifiDeviceEvent event = (WifiDeviceEvent) logEntry.getEvent().get();
            final String macAddress = event.getMacAddress();
            if (event.isConnectEvent()) {
                if (connectionStartEvents.containsKey(macAddress)) {
                    final EventLogEntry existingEvent = connectionStartEvents.get(macAddress);
                    LOG.trace("Two consecutive connection start events:\n - {}\n - {}",
                            existingEvent, logEntry);
                    wifiConnections.add(createWifiConnectionEvent(logEntry, null));
                }
                connectionStartEvents.put(macAddress, logEntry);
            } else {
                if (connectionStartEvents.containsKey(macAddress)) {
                    wifiConnections.add(createWifiConnectionEvent(connectionStartEvents.get(macAddress), logEntry));
                    connectionStartEvents.remove(macAddress);
                } else {
                    LOG.trace("Connection end event without start event: {}", logEntry);
                    wifiConnections.add(createWifiConnectionEvent(null, logEntry));
                }
            }
        }
        return wifiConnections;
    }

    private WifiConnection createWifiConnectionEvent(EventLogEntry startEvent, EventLogEntry endEvent) {
        if (startEvent != null) {
            final WifiDeviceConnected connectedEvent = startEvent.getEvent().map(WifiDeviceConnected.class::cast)
                    .orElseThrow();
            return new WifiConnection(connectedEvent.getName(), connectedEvent.getMacAddress(),
                    connectedEvent.getSpeed(), connectedEvent.getWifiType(), startEvent.getTimestamp(),
                    endEvent != null ? endEvent.getTimestamp() : null);
        }
        if (endEvent != null) {
            final WifiDeviceDisconnected disconnectedEvent = endEvent.getEvent()
                    .map(WifiDeviceDisconnected.class::cast)
                    .orElseThrow();
            return new WifiConnection(disconnectedEvent.getName(), disconnectedEvent.getMacAddress(), null,
                    disconnectedEvent.getWifiType(), null,
                    endEvent.getTimestamp());
        }
        throw new IllegalArgumentException("Neither start nor end event given");
    }
}
