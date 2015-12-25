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
