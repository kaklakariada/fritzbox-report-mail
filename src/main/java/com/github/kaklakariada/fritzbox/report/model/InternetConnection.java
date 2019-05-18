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
package com.github.kaklakariada.fritzbox.report.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class InternetConnection implements Serializable {
    private static final long serialVersionUID = 1L;

    private final LocalDateTime timestamp;
    private final String ipAddress;
    private final String dnsServer1;
    private final String dnsServer2;
    private final String gateway;
    private final String pop;

    public InternetConnection(final LocalDateTime timestamp, final String ipAddress, final String dnsServer1,
            final String dnsServer2, final String gateway, final String pop) {
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
        this.dnsServer1 = dnsServer1;
        this.dnsServer2 = dnsServer2;
        this.gateway = gateway;
        this.pop = pop;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getDnsServer1() {
        return dnsServer1;
    }

    public String getDnsServer2() {
        return dnsServer2;
    }

    public String getGateway() {
        return gateway;
    }

    public String getPop() {
        return pop;
    }

    @Override
    public String toString() {
        return "InternetConnection [" + timestamp + ", ip: " + ipAddress + ", dnsServer1=" + dnsServer1
                + ", dnsServer2=" + dnsServer2 + ", gateway=" + gateway + ", pop=" + pop + "]";
    }
}
