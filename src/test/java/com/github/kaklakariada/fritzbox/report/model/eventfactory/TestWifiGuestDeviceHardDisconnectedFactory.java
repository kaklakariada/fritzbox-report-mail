package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.WifiGuestDeviceDisconnectedHard;

public class TestWifiGuestDeviceHardDisconnectedFactory
        extends EventLogEntryFactoryTestBase<WifiGuestDeviceDisconnectedHard> {

    @Test
    public void testNoMatch1() {
        assertMatchFailed("WLAN-Gerät wird abgemeldet: WLAN-Gerät antwortet nicht. MAC-Adresse: " + MAC_ADDRESS
                + ", Name: " + HOSTNAME + ". (#0103).");
    }

    @Test
    public void testMatch1() {
        assertEntry("WLAN-Gerät am Gastzugang wird abgemeldet: WLAN-Gerät antwortet nicht. MAC-Adresse: " + MAC_ADDRESS
                + ". (#0103).", MAC_ADDRESS, "0103");
    }

    private void assertEntry(final String message, final String expectedMac, final String expectedId) {
        assertMatched(message, new WifiGuestDeviceDisconnectedHard(expectedMac, expectedId));
    }

    @Override
    protected EventLogEntryFactory<WifiGuestDeviceDisconnectedHard> createFactory() {
        return new WifiGuestDeviceDisconnectedHardFactory();
    }
}
