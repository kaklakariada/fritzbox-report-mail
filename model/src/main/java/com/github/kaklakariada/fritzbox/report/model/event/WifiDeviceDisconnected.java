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

public class WifiDeviceDisconnected extends WifiDeviceEvent {

    private static final long serialVersionUID = 1L;

    public WifiDeviceDisconnected(final WifiType wifiType, final String ipAddress, final String macAddress,
            final String deviceName) {
        super(wifiType, ipAddress, macAddress, deviceName);
    }

    @Override
    public String getDescription() {
        return "wifi disconnected: name=" + name + ", wifiType=" + wifiType + ", macAddress=" + macAddress
                + ", ipAddress=" + ipAddress;
    }

    @Override
    public boolean isConnectEvent() {
        return false;
    }

    @Override
    public boolean isDisconnectEvent() {
        return true;
    }
}
