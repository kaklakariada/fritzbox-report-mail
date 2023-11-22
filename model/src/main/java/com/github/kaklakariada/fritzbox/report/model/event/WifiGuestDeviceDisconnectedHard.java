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

public class WifiGuestDeviceDisconnectedHard extends WifiGuestDeviceDisconnected {

    private static final long serialVersionUID = 1L;

    private final String code;

    public WifiGuestDeviceDisconnectedHard(final String macAddress, final String code) {
        super(macAddress);
        this.code = code;
    }

    @Override
    public String getDescription() {
        return "wifi guest disconnected: macAddress=" + getMacAddress() + ", code=" + code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(code);
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
        final WifiGuestDeviceDisconnectedHard other = (WifiGuestDeviceDisconnectedHard) obj;
        return Objects.equals(code, other.code);
    }
}
