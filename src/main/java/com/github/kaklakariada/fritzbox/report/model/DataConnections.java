package com.github.kaklakariada.fritzbox.report.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;

public class DataConnections implements Serializable {

    private static final long serialVersionUID = 1L;
    private final TimePeriod timePeriod;
    private final DataVolume totalVolume;
    private final DataVolume sentVolume;
    private final DataVolume reveivedVolume;
    private final int numberOfConnections;
    private final Duration onlineTime;
    private final LocalDate date;

    public DataConnections(final LocalDate date, final TimePeriod timePeriod, final Duration onlineTime,
            final DataVolume totalVolume, final DataVolume sentVolume, final DataVolume reveivedVolume,
            final int numberOfConnections) {
        this.date = date;
        this.timePeriod = timePeriod;
        this.onlineTime = onlineTime;
        this.totalVolume = totalVolume;
        this.sentVolume = sentVolume;
        this.reveivedVolume = reveivedVolume;
        this.numberOfConnections = numberOfConnections;
    }

    public enum TimePeriod {

        TODAY("Heute"), //
        YESTERDAY("Gestern"), //
        THIS_WEEK("Aktuelle Woche"), //
        LAST_WEEK("Letzte Woche"), //
        THIS_MONTH("Aktueller Monat"), //
        LAST_MONTH("Letzter Monat");

        private final String name;

        private TimePeriod(final String name) {
            this.name = name;
        }

        public static TimePeriod forName(final String name) {
            for (final TimePeriod period : values()) {
                if (period.name.equals(name.trim())) {
                    return period;
                }
            }
            throw new IllegalArgumentException("Unknown time period: '" + name + "'");
        }
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

    public DataVolume getReveivedVolume() {
        return reveivedVolume;
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
                + totalVolume + ", sentVolume=" + sentVolume + ", reveivedVolume=" + reveivedVolume
                + ", numberOfConnections=" + numberOfConnections + "]";
    }
}
