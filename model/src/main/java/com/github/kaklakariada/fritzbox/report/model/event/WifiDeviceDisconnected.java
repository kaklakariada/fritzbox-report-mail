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

public class WifiDeviceDisconnected extends Event {

    private static final long serialVersionUID = 1L;

    private final WifiType wifiType;
    private final String ipAddress;
    private final String macAddress;
    private final String name;

    public WifiDeviceDisconnected(WifiType wifiType, String ipAddress, final String macAddress, final String name) {
        this.wifiType = wifiType;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getName() {
        return name;
    }

    public WifiType getWifiType() {
        return wifiType;
    }

    @Override
    public String getDescription() {
        return "wifi disconnected: name=" + name + ", type=" + wifiType + ", mac=" + macAddress + ", ip=" + ipAddress;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, macAddress, name, wifiType);
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
        final WifiDeviceDisconnected other = (WifiDeviceDisconnected) obj;
        return Objects.equals(ipAddress, other.ipAddress) && Objects.equals(macAddress, other.macAddress)
                && Objects.equals(name, other.name) && wifiType == other.wifiType;
    }
}
