package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.WifiGuestDeviceDisconnected;

public class TestWifiGuestDeviceDisconnectedFactory extends EventLogEntryFactoryTestBase<WifiGuestDeviceDisconnected> {

    @Test
    public void testNoMatch1() {
        assertMatchFailed("WLAN-Gerät hat sich abgemeldet. MAC-Adresse: " + MAC_ADDRESS + ", Name: " + HOSTNAME + ".");
    }

    @Test
    public void testNoMatch2() {
        assertMatchFailed("WLAN-Gerät hat sich abgemeldet. MAC-Adresse: " + MAC_ADDRESS + ", Name: " + HOSTNAME + ".");
    }

    @Test
    public void testMatch() {
        assertEntry("WLAN-Gerät hat sich vom Gastzugang abgemeldet. MAC-Adresse: " + MAC_ADDRESS + ".", MAC_ADDRESS);
    }

    private void assertEntry(final String message, final String expectedMac) {
        assertMatched(message, new WifiGuestDeviceDisconnected(expectedMac));
    }

    @Override
    protected EventLogEntryFactory<WifiGuestDeviceDisconnected> createFactory() {
        return new WifiGuestDeviceDisconnectedFactory();
    }
}
