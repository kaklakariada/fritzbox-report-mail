/**
 * A Java API for parsing and processing status report mails of a FritzBox
 * Copyright (C) 2018 Christoph Pirkl <christoph at users.sourceforge.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General  License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General  License for more details.
 *
 * You should have received a copy of the GNU General  License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import org.junit.jupiter.api.Test;

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceConnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiType;

class TestWifiDeviceConnectedFactory extends EventLogEntryFactoryTestBase<WifiDeviceConnected> {

    @Test
    void testMatch1() {
        assertEntry("WLAN-Gerät angemeldet. Geschwindigkeit 54 Mbit/s. MAC-Adresse: " + MAC_ADDRESS + ", Name: "
                + HOSTNAME + ".", "54 Mbit/s", MAC_ADDRESS, HOSTNAME);
    }

    @Test
    void testMatch2() {
        assertEntry("WLAN-Gerät hat sich neu angemeldet. Geschwindigkeit 65 Mbit/s. MAC-Adresse: " + MAC_ADDRESS
                + ", Name: " + HOSTNAME + ".", "65 Mbit/s", MAC_ADDRESS, HOSTNAME);
    }

    @Test
    void testMatch3() {
        assertEntry("Neues WLAN-Gerät erstmalig angemeldet. Geschwindigkeit 130 Mbit/s. MAC-Adresse: " + MAC_ADDRESS
                + ", Name: " + HOSTNAME + ".", "130 Mbit/s", MAC_ADDRESS, HOSTNAME);
    }

    @Test
    void testMatch4() {
        assertEntry("WLAN-Gerät angemeldet (5 GHz), 866 Mbit/s, " + HOSTNAME + ", IP " + IP_ADDRESS1 + ", MAC "
                + MAC_ADDRESS + ".", "866 Mbit/s", WifiType._5_GHZ, IP_ADDRESS1, MAC_ADDRESS, HOSTNAME);
    }

    @Test
    void testMatch5() {
        assertEntry("WLAN-Gerät angemeldet (2,4 GHz), 866 Mbit/s, " + HOSTNAME + ", IP " + IP_ADDRESS1 + ", MAC "
                + MAC_ADDRESS + ".", "866 Mbit/s", WifiType._2_4_GHZ, IP_ADDRESS1, MAC_ADDRESS, HOSTNAME);
    }

    @Test
    void testMatch6() {
        assertEntry("WLAN-Gerät angemeldet (2,4 GHz). Geschwindigkeit 72 Mbit/s. MAC-Adresse: " + MAC_ADDRESS
                + ", Name: " + HOSTNAME + ".", "72 Mbit/s", WifiType._2_4_GHZ, null, MAC_ADDRESS, HOSTNAME);
    }

    @Test
    void testMatch7() {
        assertEntry(
                "WLAN-Gerät angemeldet (5 GHz), 300 Mbit/s, " + HOSTNAME + ", IP " + IP_ADDRESS1 + ", MAC "
                        + MAC_ADDRESS + ", Name: " + HOSTNAME + ".",
                "300 Mbit/s", WifiType._5_GHZ, IP_ADDRESS1, MAC_ADDRESS, HOSTNAME);
    }

    private void assertEntry(final String message, final String expectedSpeed, final String expectedMac,
            final String expectedName) {
        assertEntry(message, expectedSpeed, null, null, expectedMac, expectedName);
    }

    private void assertEntry(final String message, final String expectedSpeed, WifiType expectedType,
            String expectedIpAddress,
            final String expectedMac, final String expectedName) {
        assertMatched(message,
                new WifiDeviceConnected(expectedSpeed, expectedType, expectedIpAddress, expectedMac, expectedName));
    }

    @Override
    protected AbstractEventLogEntryFactory<WifiDeviceConnected> createFactory() {
        return new WifiDeviceConnectedFactory();
    }
}
