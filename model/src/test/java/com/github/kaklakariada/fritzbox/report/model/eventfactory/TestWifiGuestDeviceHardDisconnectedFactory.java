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

import com.github.kaklakariada.fritzbox.report.model.event.WifiGuestDeviceDisconnectedHard;

class TestWifiGuestDeviceHardDisconnectedFactory
        extends EventLogEntryFactoryTestBase<WifiGuestDeviceDisconnectedHard> {

    @Test
    void testNoMatch1() {
        assertMatchFailed("WLAN-Gerät wird abgemeldet: WLAN-Gerät antwortet nicht. MAC-Adresse: " + MAC_ADDRESS
                + ", Name: " + HOSTNAME + ". (#0103).");
    }

    @Test
    void testMatch1() {
        assertEntry("WLAN-Gerät am Gastzugang wird abgemeldet: WLAN-Gerät antwortet nicht. MAC-Adresse: " + MAC_ADDRESS
                + ". (#0103).", MAC_ADDRESS, "0103");
    }

    private void assertEntry(final String message, final String expectedMac, final String expectedId) {
        assertMatched(message, new WifiGuestDeviceDisconnectedHard(expectedMac, expectedId));
    }

    @Override
    protected AbstractEventLogEntryFactory<WifiGuestDeviceDisconnectedHard> createFactory() {
        return new WifiGuestDeviceDisconnectedHardFactory();
    }
}
