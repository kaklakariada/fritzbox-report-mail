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

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiType;

class TestWifiDeviceDisconnectedFactory extends EventLogEntryFactoryTestBase<WifiDeviceDisconnected> {

    @Test
    void testMatch1() {
        assertEntry("WLAN-Gerät hat sich abgemeldet. MAC-Adresse: " + MAC_ADDRESS + ", Name: " + HOSTNAME + ".",
                null, MAC_ADDRESS, HOSTNAME);
    }

    @Test
    void testMatch2() {
        assertEntry("WLAN-Gerät hat sich abgemeldet. MAC-Adresse: " + MAC_ADDRESS + ", Name: " + HOSTNAME + ".",
                null, MAC_ADDRESS, HOSTNAME);
    }

    @Test
    void testMatch3() {
        assertEntry("WLAN-Gerät hat sich abgemeldet (5 GHz). MAC-Adresse: " + MAC_ADDRESS + ", Name: " + HOSTNAME + ".",
                WifiType._5_GHZ, MAC_ADDRESS, HOSTNAME);
    }

    private void assertEntry(String message, WifiType wifiType, final String expectedMac, final String expectedName) {
        assertMatched(message, new WifiDeviceDisconnected(wifiType, expectedMac, expectedName));
    }

    @Override
    protected AbstractEventLogEntryFactory<WifiDeviceDisconnected> createFactory() {
        return new WifiDeviceDisconnectedFactory();
    }
}
