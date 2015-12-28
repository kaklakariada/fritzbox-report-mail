/**
 * A Java API for parsing and processing status report mails of a FritzBox
 * Copyright (C) 2015 Christoph Pirkl <christoph at users.sourceforge.net>
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.github.kaklakariada.fritzbox.report.model.DataConnections.TimePeriod;

public class FritzBoxReportCollection implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<FritzBoxReportMail> reports;

    public FritzBoxReportCollection(final List<FritzBoxReportMail> reports) {
        this.reports = reports;
    }

    public int getReportCount() {
        return reports.size();
    }

    public Stream<AggregatedVolume> getDataVolumeByDay() {
        return reports.stream() //
                .map(FritzBoxReportMail::getDataConnections) //
                .map(connections -> connections.get(TimePeriod.YESTERDAY)) //
                .sorted(Comparator.comparing(DataConnections::getDate))
                .map(conn -> new AggregatedVolume(conn.getDate(), conn));
    }

    public Stream<AggregatedVolume> getDataVolumeByDayAndMonth() {
        return getDataVolumeByDay() //
                .collect(groupingBy(vol -> vol.getDay().withDayOfMonth(1), //
                        reducing((v1, v2) -> v1.plusSameMonth(v2)))) //
                .values().stream() //
                .map(Optional::get) //
                .sorted(comparing(AggregatedVolume::getDay));
    }

    public Stream<EventLogEntry> getLogEntries() {
        return this.reports.stream() //
                .sorted(comparing(FritzBoxReportMail::getDate)) //
                .flatMap(r -> r.getEventLog().stream()) //
                .sorted(comparing(EventLogEntry::getTimestamp));
    }
}
