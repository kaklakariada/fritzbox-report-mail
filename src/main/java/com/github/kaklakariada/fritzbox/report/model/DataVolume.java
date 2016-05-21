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

import java.io.Serializable;
import java.util.Comparator;

public class DataVolume implements Serializable, Comparable<DataVolume> {

    private static final long serialVersionUID = 1L;
    private static final Comparator<DataVolume> COMPARATOR = Comparator.comparingInt(DataVolume::getVolumeKb);

    private final int volume;
    private final Unit unit;

    public enum Unit {
        MB("MB"), KB("kB");
        private final String name;

        private Unit(final String name) {
            this.name = name;
        }

        public static Unit forName(final String name) {
            for (final Unit unit : values()) {
                if (unit.name.equals(name)) {
                    return unit;
                }
            }
            throw new IllegalArgumentException("Unknown unit " + name);
        }
    }

    private DataVolume(final int volume, final Unit unit) {
        this.volume = volume;
        this.unit = unit;
    }

    public static DataVolume of(final int volume, Unit unit) {
        return new DataVolume(volume, unit);
    }

    public static DataVolume parse(final String value) {
        final String trimmedValue = value.trim();
        final String suffix = trimmedValue.substring(trimmedValue.length() - 2);
        final String numberString = trimmedValue.subSequence(0, trimmedValue.length() - 2).toString();
        final int volume = Integer.valueOf(numberString);
        final Unit unit = Unit.forName(suffix);
        return new DataVolume(volume, unit);
    }

    public int getVolume() {
        return volume;
    }

    public Unit getUnit() {
        return unit;
    }

    public int getVolumeKb() {
        if (unit == Unit.KB) {
            return volume;
        }
        return volume * 1000;
    }

    @Override
    public String toString() {
        return volume + unit.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        result = prime * result + volume;
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
        final DataVolume other = (DataVolume) obj;
        if (unit != other.unit) {
            return false;
        }
        if (volume != other.volume) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(DataVolume o) {
        return COMPARATOR.compare(this, o);
    }

    public DataVolume plus(final DataVolume other) {
        if (other.volume == 0) {
            return this;
        }
        if (this.volume == 0) {
            return other;
        }
        if (this.unit == other.unit) {
            return new DataVolume(this.volume + other.volume, this.unit);
        }
        if (this.unit == Unit.KB) {
            return new DataVolume(this.volume / 1000 + 1 + other.volume, Unit.MB);
        }
        return new DataVolume(other.volume / 1000 + 1 + this.volume, Unit.MB);
    }
}
