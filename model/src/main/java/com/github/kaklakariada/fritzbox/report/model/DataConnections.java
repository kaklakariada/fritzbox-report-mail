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

import static java.util.Arrays.asList;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class DataConnections implements Serializable {

    private static final long serialVersionUID = 1L;
    private final TimePeriod timePeriod;
    private final DataVolume totalVolume;
    private final DataVolume sentVolume;
    private final DataVolume receivedVolume;
    private final int numberOfConnections;
    private final Duration onlineTime;
    private final LocalDate date;
    private final int reportId;

    public DataConnections(int reportId, final LocalDate date, final TimePeriod timePeriod, final Duration onlineTime,
            final DataVolume totalVolume, final DataVolume sentVolume, final DataVolume receivedVolume,
            final int numberOfConnections) {
        this.reportId = reportId;
        this.date = Objects.requireNonNull(date);
        this.timePeriod = timePeriod;
        this.onlineTime = onlineTime;
        this.totalVolume = totalVolume;
        this.sentVolume = sentVolume;
        this.receivedVolume = receivedVolume;
        this.numberOfConnections = numberOfConnections;
    }

    public enum TimePeriod {
        TODAY("Heute"), //
        YESTERDAY("Gestern"), //
        THIS_WEEK("Aktuelle Woche"), //
        LAST_WEEK("Letzte Woche"), //
        THIS_MONTH("Aktueller Monat"), //
        LAST_MONTH("Vormonat", "Letzter Monat");

        private final List<String> names;

        private TimePeriod(final String... names) {
            this.names = asList(names);
        }

        public static TimePeriod forName(final String name) {
            final String trimmedName = name.trim();
            for (final TimePeriod period : values()) {
                if (period.names.contains(trimmedName)) {
                    return period;
                }
            }
            throw new IllegalArgumentException("Unknown time period: '" + name + "'");
        }
    }

    public int getReportId() {
        return reportId;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public DataVolume getTotalVolume() {
        return totalVolume;
    }

    public DataVolume getSentVolume() {
        return sentVolume;
    }

    public DataVolume getReceivedVolume() {
        return receivedVolume;
    }

    public int getNumberOfConnections() {
        return numberOfConnections;
    }

    public Duration getOnlineTime() {
        return onlineTime;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "DataConnections [timePeriod=" + timePeriod + ", onlineTime=" + onlineTime + ", totalVolume="
                + totalVolume + ", sentVolume=" + sentVolume + ", receivedVolume=" + receivedVolume
                + ", numberOfConnections=" + numberOfConnections + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, numberOfConnections, onlineTime, reportId, receivedVolume, sentVolume, timePeriod,
                totalVolume);
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
        final DataConnections other = (DataConnections) obj;
        return Objects.equals(date, other.date) && numberOfConnections == other.numberOfConnections
                && Objects.equals(onlineTime, other.onlineTime) && reportId == other.reportId
                && Objects.equals(receivedVolume, other.receivedVolume) && Objects.equals(sentVolume, other.sentVolume)
                && timePeriod == other.timePeriod && Objects.equals(totalVolume, other.totalVolume);
    }
}
