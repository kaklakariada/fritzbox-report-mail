package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceConnected;

public class TestWifiDeviceConnectedFactory extends EventLogEntryFactoryTestBase<WifiDeviceConnected> {

    @Test
    public void testMatch1() {
        assertEntry("WLAN-Gerät angemeldet. Geschwindigkeit 54 Mbit/s. MAC-Adresse: " + MAC_ADDRESS + ", Name: "
                + HOSTNAME + ".", "54 Mbit/s", MAC_ADDRESS, HOSTNAME);
    }

    @Test
    public void testMatch2() {
        assertEntry("WLAN-Gerät hat sich neu angemeldet. Geschwindigkeit 65 Mbit/s. MAC-Adresse: " + MAC_ADDRESS
                + ", Name: " + HOSTNAME + ".", "65 Mbit/s", MAC_ADDRESS, HOSTNAME);
    }

    @Test
    public void testMatch3() {
        assertEntry("Neues WLAN-Gerät erstmalig angemeldet. Geschwindigkeit 130 Mbit/s. MAC-Adresse: " + MAC_ADDRESS
                + ", Name: " + HOSTNAME + ".", "130 Mbit/s", MAC_ADDRESS, HOSTNAME);
    }

    private void assertEntry(final String message, final String expectedSpeed, final String expectedMac,
            final String expectedName) {
        assertMatched(message, new WifiDeviceConnected(expectedSpeed, expectedMac, expectedName));
    }

    @Override
    protected AbstractEventLogEntryFactory<WifiDeviceConnected> createFactory() {
        return new WifiDeviceConnectedFactory();
    }
}
