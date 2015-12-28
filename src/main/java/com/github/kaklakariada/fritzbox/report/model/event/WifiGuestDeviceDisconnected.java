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
package com.github.kaklakariada.fritzbox.report.model.event;

import com.github.kaklakariada.fritzbox.report.model.Event;

public class WifiGuestDeviceDisconnected extends Event {

    private static final long serialVersionUID = 1L;

    private final String macAddress;

    public WifiGuestDeviceDisconnected(final String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    @Override
    public String getDescription() {
        return "wifi guest disconnected: mac=" + macAddress;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((macAddress == null) ? 0 : macAddress.hashCode());
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
        final WifiGuestDeviceDisconnected other = (WifiGuestDeviceDisconnected) obj;
        if (macAddress == null) {
            if (other.macAddress != null) {
                return false;
            }
        } else if (!macAddress.equals(other.macAddress)) {
            return false;
        }
        return true;
    }
}
