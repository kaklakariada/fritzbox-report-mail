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

public class WifiDeviceDisconnectedHard extends WifiDeviceDisconnected {

    private static final long serialVersionUID = 1L;

    private final String code;

    public WifiDeviceDisconnectedHard(final WifiType type, final String ipAddress, final String macAddress,
            final String name,
            final String code) {
        super(type, ipAddress, macAddress, name);
        this.code = code;
    }

    @Override
    public String getDescription() {
        return "wifi disconnected: name=" + getName() + ", wifiType=" + getWifiType() + ", macAddress="
                + getMacAddress()
                + ", code=" + code + ", ipAddress=" + getIpAddress();
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((code == null) ? 0 : code.hashCode());
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
        final WifiDeviceDisconnectedHard other = (WifiDeviceDisconnectedHard) obj;
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        return true;
    }
}
