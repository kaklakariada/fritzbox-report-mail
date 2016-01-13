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
package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.IPv4InternetConnectionEstablished;

public class TestIPv4InternetConnectionEstablishedFactory
        extends EventLogEntryFactoryTestBase<IPv4InternetConnectionEstablished> {

    @Test
    public void testFailureInvalidIpAddress() {
        assertMatchFailed("Internetverbindung wurde erfolgreich hergestellt. IP-Adresse: " + IP_ADDRESS_INVALID
                + ", DNS-Server: " + IP_ADDRESS2 + " und " + IP_ADDRESS3 + ", Gateway: " + IP_ADDRESS4
                + ", Breitband-PoP: ABCD21-se800-B226E1910E02MB");
    }

    @Test
    public void testFailureTrailingWhitespace() {
        assertMatchFailed("Internetverbindung wurde erfolgreich hergestellt. IP-Adresse: " + IP_ADDRESS1
                + ", DNS-Server: " + IP_ADDRESS2 + " und " + IP_ADDRESS3 + ", Gateway: " + IP_ADDRESS4
                + ", Breitband-PoP: ABCD21-se800-B226E1910E02MB  ");
    }

    @Test
    public void testSuccess() {
        assertMatched(
                "Internetverbindung wurde erfolgreich hergestellt. IP-Adresse: " + IP_ADDRESS1 + ", DNS-Server: "
                        + IP_ADDRESS2 + " und " + IP_ADDRESS3 + ", Gateway: " + IP_ADDRESS4
                        + ", Breitband-PoP: ABCD21-se800-B226E1910E02MB",
                new IPv4InternetConnectionEstablished(IP_ADDRESS1, IP_ADDRESS2, IP_ADDRESS3, IP_ADDRESS4,
                        "ABCD21-se800-B226E1910E02MB"));
    }

    @Override
    protected AbstractEventLogEntryFactory<IPv4InternetConnectionEstablished> createFactory() {
        return new IPv4InternetConnectionEstablishedFactory();
    }
}
