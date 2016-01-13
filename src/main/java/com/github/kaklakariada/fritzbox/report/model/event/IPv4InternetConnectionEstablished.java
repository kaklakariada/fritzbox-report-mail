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

public class IPv4InternetConnectionEstablished extends Event {

    private static final long serialVersionUID = 1L;

    private final String ipAddress;
    private final String dns1;
    private final String dns2;
    private final String gateway;
    private final String broadbandPop;

    public IPv4InternetConnectionEstablished(String ipAddress, String dns1, String dns2, String gateway,
            String broadbandPop) {
        this.ipAddress = ipAddress;
        this.dns1 = dns1;
        this.dns2 = dns2;
        this.gateway = gateway;
        this.broadbandPop = broadbandPop;
    }

    @Override
    public String getDescription() {
        return "IPv4 connection established, ip=" + ipAddress + ", dns1=" + dns1 + ", dns2=" + dns2 + ", gateway="
                + gateway + ", pop=" + broadbandPop;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((broadbandPop == null) ? 0 : broadbandPop.hashCode());
        result = prime * result + ((dns1 == null) ? 0 : dns1.hashCode());
        result = prime * result + ((dns2 == null) ? 0 : dns2.hashCode());
        result = prime * result + ((gateway == null) ? 0 : gateway.hashCode());
        result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
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
        final IPv4InternetConnectionEstablished other = (IPv4InternetConnectionEstablished) obj;
        if (broadbandPop == null) {
            if (other.broadbandPop != null) {
                return false;
            }
        } else if (!broadbandPop.equals(other.broadbandPop)) {
            return false;
        }
        if (dns1 == null) {
            if (other.dns1 != null) {
                return false;
            }
        } else if (!dns1.equals(other.dns1)) {
            return false;
        }
        if (dns2 == null) {
            if (other.dns2 != null) {
                return false;
            }
        } else if (!dns2.equals(other.dns2)) {
            return false;
        }
        if (gateway == null) {
            if (other.gateway != null) {
                return false;
            }
        } else if (!gateway.equals(other.gateway)) {
            return false;
        }
        if (ipAddress == null) {
            if (other.ipAddress != null) {
                return false;
            }
        } else if (!ipAddress.equals(other.ipAddress)) {
            return false;
        }
        return true;
    }
}
