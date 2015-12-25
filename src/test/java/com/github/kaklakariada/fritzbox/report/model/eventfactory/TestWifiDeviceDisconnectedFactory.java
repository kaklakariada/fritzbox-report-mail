package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnected;

public class TestWifiDeviceDisconnectedFactory extends EventLogEntryFactoryTestBase<WifiDeviceDisconnected> {

    @Test
    public void testMatch1() {
        assertEntry("WLAN-Gerät hat sich abgemeldet. MAC-Adresse: " + MAC_ADDRESS + ", Name: " + HOSTNAME + ".",
                MAC_ADDRESS, HOSTNAME);
    }

    @Test
    public void testMatch2() {
        assertEntry("WLAN-Gerät hat sich abgemeldet. MAC-Adresse: " + MAC_ADDRESS + ", Name: " + HOSTNAME + ".",
                MAC_ADDRESS, HOSTNAME);
    }

    private void assertEntry(final String message, final String expectedMac, final String expectedName) {
        assertMatched(message, new WifiDeviceDisconnected(expectedMac, expectedName));
    }

    @Override
    protected AbstractEventLogEntryFactory<WifiDeviceDisconnected> createFactory() {
        return new WifiDeviceDisconnectedFactory();
    }
}
