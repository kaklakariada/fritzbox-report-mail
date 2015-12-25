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
    protected EventLogEntryFactory<WifiGuestDeviceConnected> createFactory() {
        return new WifiGuestDeviceConnectedFactory();
    }
}
