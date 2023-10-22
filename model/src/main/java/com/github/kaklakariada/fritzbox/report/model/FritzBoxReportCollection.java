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

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import com.github.kaklakariada.fritzbox.report.model.DataConnections.TimePeriod;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceEvent;

public class FritzBoxReportCollection implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<LocalDate, FritzBoxReportMail> reports;

    public FritzBoxReportCollection(final Map<LocalDate, FritzBoxReportMail> reports) {
        this.reports = reports;
    }

    public Stream<FritzBoxReportMail> getReports() {
        return reports.values().stream();
    }

    public int getReportCount() {
        return reports.size();
    }

    public Stream<AggregatedVolume> getDataVolumeByDay() {
        return reports.values().stream()
                .map(FritzBoxReportMail::getDataConnections)
                .map(connections -> connections.get(TimePeriod.YESTERDAY))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(DataConnections::getDate))
                .map(conn -> new AggregatedVolume(conn.getReportId(), conn.getDate(), conn));
    }

    public Stream<AggregatedVolume> getDataVolumeByDayAndMonth() {
        return getDataVolumeByDay()
                .collect(groupingBy(vol -> vol.getDay().withDayOfMonth(1),
                        reducing(AggregatedVolume::plusSameMonth)))
                .values().stream()
                .map(Optional::get)
                .sorted(comparing(AggregatedVolume::getDay));
    }

    public Stream<EventLogEntry> getLogEntries() {
        return this.reports.values().stream()
                .sorted(comparing(FritzBoxReportMail::getDate)) //
                .flatMap(r -> r.getEventLog().stream()) //
                .sorted(comparing(EventLogEntry::getTimestamp));
    }

    public Stream<EventLogEntry> getWifiLogEntries() {
        return this.getLogEntries().filter(e -> e.getEvent().isPresent())
                .filter(e -> (e.getEvent().get() instanceof WifiDeviceEvent));
    }
}
