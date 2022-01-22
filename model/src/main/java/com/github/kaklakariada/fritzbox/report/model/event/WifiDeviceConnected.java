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
package com.github.kaklakariada.fritzbox.report.model.event;

import java.util.Objects;

import com.github.kaklakariada.fritzbox.report.model.Event;

public class WifiDeviceConnected extends Event {

    private static final long serialVersionUID = 1L;

    private final String speed;
    private final String macAddress;
    private final String name;

    private final WifiType wifiType;

    public WifiDeviceConnected(final String speed, WifiType wifiType, String ipAddress, final String macAddress,
            final String name) {
        this.speed = speed;
        this.wifiType = wifiType;
        this.macAddress = macAddress;
        this.name = name;
    }

    @Override
    public String getDescription() {
        return "wifi connected: name=" + name + ", type " + wifiType + ", speed=" + speed + ", mac=" + macAddress;
    }

    public String getSpeed() {
        return speed;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(macAddress, name, speed, wifiType);
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
        final WifiDeviceConnected other = (WifiDeviceConnected) obj;
        return Objects.equals(macAddress, other.macAddress) && Objects.equals(name, other.name)
                && Objects.equals(speed, other.speed) && wifiType == other.wifiType;
    }
}
