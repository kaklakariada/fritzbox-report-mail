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

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnectedHard;
import com.github.kaklakariada.fritzbox.report.model.event.WifiType;

class TestWifiDeviceHardDisconnectedFactory extends EventLogEntryFactoryTestBase<WifiDeviceDisconnectedHard> {

    @Test
    void testMatch1() {
        assertEntry("WLAN-Gerät wird abgemeldet: WLAN-Gerät antwortet nicht. MAC-Adresse: " + MAC_ADDRESS + ", Name: "
                + HOSTNAME + ". (#0103).", null, null, MAC_ADDRESS, HOSTNAME, "0103");
    }

    @Test
    void testMatch2() {
        assertEntry("WLAN-Gerät wird abgemeldet (2,4 GHz): WLAN-Gerät antwortet nicht. MAC-Adresse: " + MAC_ADDRESS
                + ", Name: " + HOSTNAME + ". (#0302).", WifiType.TWO_POINT_FOUR_GHZ, null, MAC_ADDRESS, HOSTNAME,
                "0302");
    }

    @Test
    void testMatch3() {
        assertEntry(
                "WLAN-Gerät wird abgemeldet (2,4 GHz): WLAN-Gerät antwortet nicht, " + HOSTNAME + ", IP " + IP_ADDRESS1
                        + ", MAC " + MAC_ADDRESS + ". (#0104).",
                WifiType.TWO_POINT_FOUR_GHZ, IP_ADDRESS1, MAC_ADDRESS, HOSTNAME, "0104");
    }

    @Test
    void testMatch4() {
        assertEntry(
                "WLAN-Gerät wird abgemeldet (2,4 GHz): WLAN-Gerät antwortet nicht, " + HOSTNAME + ", IP " + IP_ADDRESS1
                        + ", MAC " + MAC_ADDRESS + ", Name: " + HOSTNAME + ". (#0302).",
                WifiType.TWO_POINT_FOUR_GHZ, IP_ADDRESS1, MAC_ADDRESS, HOSTNAME, "0302");
    }

    private void assertEntry(String message, WifiType type, String ipAddress, final String expectedMac,
            final String expectedName,
            final String expectedId) {
        assertMatched(message, new WifiDeviceDisconnectedHard(type, ipAddress, expectedMac, expectedName, expectedId));
    }

    @Override
    protected AbstractEventLogEntryFactory<WifiDeviceDisconnectedHard> createFactory() {
        return new WifiDeviceDisconnectedHardFactory();
    }
}
