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
package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.WifiGuestDeviceConnected;

public class TestWifiGuestDeviceConnectedFactory extends EventLogEntryFactoryTestBase<WifiGuestDeviceConnected> {

    @Test
    public void testNoMatch1() {
        assertMatchFailed("WLAN-Gerät angemeldet. Geschwindigkeit 54 Mbit/s. MAC-Adresse: " + MAC_ADDRESS + ", Name: "
                + HOSTNAME + ".");
    }

    @Test
    public void testNoMatch2() {
        assertMatchFailed("WLAN-Gerät hat sich neu angemeldet. Geschwindigkeit 65 Mbit/s. MAC-Adresse: " + MAC_ADDRESS
                + ", Name: " + HOSTNAME + ".");
    }

    @Test
    public void testNoMatch3() {
        assertMatchFailed("Neues WLAN-Gerät erstmalig angemeldet. Geschwindigkeit 130 Mbit/s. MAC-Adresse: "
                + MAC_ADDRESS + ", Name: " + HOSTNAME + ".");
    }

    @Test
    public void testMatch1() {
        assertEntry(
                "WLAN-Gerät über Gastzugang angemeldet. Geschwindigkeit 130 Mbit/s. MAC-Adresse: " + MAC_ADDRESS + ".",
                "130 Mbit/s", MAC_ADDRESS);
    }

    private void assertEntry(final String message, final String expectedSpeed, final String expectedMac) {
        assertMatched(message, new WifiGuestDeviceConnected(expectedSpeed, expectedMac));
    }

    @Override
    protected AbstractEventLogEntryFactory<WifiGuestDeviceConnected> createFactory() {
        return new WifiGuestDeviceConnectedFactory();
    }
}
