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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + numberOfConnections;
        result = prime * result + ((onlineTime == null) ? 0 : onlineTime.hashCode());
        result = prime * result + ((reveivedVolume == null) ? 0 : reveivedVolume.hashCode());
        result = prime * result + ((sentVolume == null) ? 0 : sentVolume.hashCode());
        result = prime * result + ((timePeriod == null) ? 0 : timePeriod.hashCode());
        result = prime * result + ((totalVolume == null) ? 0 : totalVolume.hashCode());
        return result;
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
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        if (numberOfConnections != other.numberOfConnections) {
            return false;
        }
        if (onlineTime == null) {
            if (other.onlineTime != null) {
                return false;
            }
        } else if (!onlineTime.equals(other.onlineTime)) {
            return false;
        }
        if (reveivedVolume == null) {
            if (other.reveivedVolume != null) {
                return false;
            }
        } else if (!reveivedVolume.equals(other.reveivedVolume)) {
            return false;
        }
        if (sentVolume == null) {
            if (other.sentVolume != null) {
                return false;
            }
        } else if (!sentVolume.equals(other.sentVolume)) {
            return false;
        }
        if (timePeriod != other.timePeriod) {
            return false;
        }
        if (totalVolume == null) {
            if (other.totalVolume != null) {
                return false;
            }
        } else if (!totalVolume.equals(other.totalVolume)) {
            return false;
        }
        return true;
    }
}
