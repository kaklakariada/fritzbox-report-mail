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

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

public class AggregatedVolume {
    private final LocalDate day;
    private final DataVolume totalVolume;
    private final DataVolume sentVolume;
    private final DataVolume receivedVolume;
    private final int numberOfConnections;
    private final Duration onlineTime;
    private final int reportId;

    public AggregatedVolume(final int reportId, final LocalDate day, final DataConnections conn) {
        this(reportId, day, conn.getTotalVolume(), conn.getSentVolume(), conn.getReceivedVolume(),
                conn.getNumberOfConnections(),
                conn.getOnlineTime());
    }

    private AggregatedVolume(final int reportId, final LocalDate day, final DataVolume totalVolume,
            final DataVolume sentVolume,
            final DataVolume receivedVolume, final int numberOfConnections, final Duration onlineTime) {
        this.reportId = reportId;
        this.day = day;
        this.totalVolume = totalVolume;
        this.sentVolume = sentVolume;
        this.receivedVolume = receivedVolume;
        this.numberOfConnections = numberOfConnections;
        this.onlineTime = onlineTime;
    }

    public int getReportId() {
        return reportId;
    }

    public AggregatedVolume plusSameMonth(final AggregatedVolume other) {
        return plus(other, this.day.withDayOfMonth(1));
    }

    public AggregatedVolume plusSameYear(final AggregatedVolume other) {
        return plus(other, this.day.withMonth(1).withDayOfMonth(1));
    }

    private AggregatedVolume plus(final AggregatedVolume other, final LocalDate newDay) {
        return new AggregatedVolume(-1, newDay, //
                this.totalVolume.plus(other.totalVolume), //
                this.sentVolume.plus(other.sentVolume), //
                this.receivedVolume.plus(other.receivedVolume), //
                this.numberOfConnections + other.numberOfConnections, //
                this.onlineTime.plus(other.onlineTime));
    }

    public LocalDate getDay() {
        return day;
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

    @Override
    public String toString() {
        return "AggregatedVolume [day=" + day + ", totalVolume=" + totalVolume + ", sentVolume=" + sentVolume
                + ", receivedVolume=" + receivedVolume + ", numberOfConnections=" + numberOfConnections
                + ", onlineTime=" + onlineTime + ", reportId=" + reportId + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, numberOfConnections, onlineTime, reportId, receivedVolume, sentVolume, totalVolume);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AggregatedVolume other = (AggregatedVolume) obj;
        return Objects.equals(day, other.day) && numberOfConnections == other.numberOfConnections
                && Objects.equals(onlineTime, other.onlineTime) && reportId == other.reportId
                && Objects.equals(receivedVolume, other.receivedVolume) && Objects.equals(sentVolume, other.sentVolume)
                && Objects.equals(totalVolume, other.totalVolume);
    }

}