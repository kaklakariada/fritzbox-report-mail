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

public class WifiDeviceConnected extends WifiDeviceEvent {

    private static final long serialVersionUID = 1L;
    private final String speed;

    public WifiDeviceConnected(final String speed, final WifiType wifiType, final String ipAddress,
            final String macAddress, final String deviceName) {
        super(wifiType, ipAddress, macAddress, deviceName);
        this.speed = speed;
    }

    public String getSpeed() {
        return speed;
    }

    @Override
    public String getDescription() {
        return "wifi connected: name=" + name + ", wifiType " + wifiType + ", speed=" + speed + ", macAddress="
                + macAddress + ", ipAddress=" + ipAddress;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(speed);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WifiDeviceConnected other = (WifiDeviceConnected) obj;
        return Objects.equals(speed, other.speed);
    }

    @Override
    public boolean isConnectEvent() {
        return true;
    }

    @Override
    public boolean isDisconnectEvent() {
        return false;
    }
}
