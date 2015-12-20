package com.github.kaklakariada.fritzbox.report.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
                .collect(Collectors.groupingBy(vol -> vol.getDay().withDayOfMonth(1),
                        Collectors.reducing((v1, v2) -> v1.plusSameMonth(v2)))) //
                .values().stream() //
                .map(Optional::get) //
                .sorted(Comparator.comparing(AggregatedVolume::getDay));
    }

    public static class AggregatedVolume {
        private final LocalDate day;
        private final DataVolume totalVolume;
        private final DataVolume sentVolume;
        private final DataVolume reveivedVolume;
        private final int numberOfConnections;
        private final Duration onlineTime;

        public AggregatedVolume(final LocalDate day, final DataConnections conn) {
            this(day, conn.getTotalVolume(), conn.getSentVolume(), conn.getReveivedVolume(),
                    conn.getNumberOfConnections(), conn.getOnlineTime());
        }

        public AggregatedVolume(final LocalDate day, final DataVolume totalVolume, final DataVolume sentVolume,
                final DataVolume reveivedVolume, final int numberOfConnections, final Duration onlineTime) {
            this.day = day;
            this.totalVolume = totalVolume;
            this.sentVolume = sentVolume;
            this.reveivedVolume = reveivedVolume;
            this.numberOfConnections = numberOfConnections;
            this.onlineTime = onlineTime;
        }

        public AggregatedVolume plusSameMonth(final AggregatedVolume other) {
            return plus(other, this.day.withDayOfMonth(1));
        }

        public AggregatedVolume plusSameYear(final AggregatedVolume other) {
            return plus(other, this.day.withMonth(1).withDayOfMonth(1));
        }

        private AggregatedVolume plus(final AggregatedVolume other, final LocalDate newDay) {
            return new AggregatedVolume(newDay, //
                    this.totalVolume.plus(other.totalVolume), //
                    this.sentVolume.plus(other.sentVolume), //
                    this.reveivedVolume.plus(other.reveivedVolume), //
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

        public DataVolume getReveivedVolume() {
            return reveivedVolume;
        }

        public int getNumberOfConnections() {
            return numberOfConnections;
        }

        public Duration getOnlineTime() {
            return onlineTime;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((day == null) ? 0 : day.hashCode());
            result = prime * result + numberOfConnections;
            result = prime * result + ((onlineTime == null) ? 0 : onlineTime.hashCode());
            result = prime * result + ((reveivedVolume == null) ? 0 : reveivedVolume.hashCode());
            result = prime * result + ((sentVolume == null) ? 0 : sentVolume.hashCode());
            result = prime * result + ((totalVolume == null) ? 0 : totalVolume.hashCode());
            return result;
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
            if (day == null) {
                if (other.day != null) {
                    return false;
                }
            } else if (!day.equals(other.day)) {
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
            if (totalVolume == null) {
                if (other.totalVolume != null) {
                    return false;
                }
            } else if (!totalVolume.equals(other.totalVolume)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "AggregatedVolume [day=" + day + ", totalVolume=" + totalVolume + ", sentVolume=" + sentVolume
                    + ", reveivedVolume=" + reveivedVolume + ", numberOfConnections=" + numberOfConnections
                    + ", onlineTime=" + onlineTime + "]";
        }
    }
}
