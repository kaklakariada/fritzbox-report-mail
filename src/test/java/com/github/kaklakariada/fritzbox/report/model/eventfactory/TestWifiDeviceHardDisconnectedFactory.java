package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnectedHard;

public class TestWifiDeviceHardDisconnectedFactory extends EventLogEntryFactoryTestBase<WifiDeviceDisconnectedHard> {

    @Test
    public void testMatch1() {
        assertEntry("WLAN-Gerät wird abgemeldet: WLAN-Gerät antwortet nicht. MAC-Adresse: " + MAC_ADDRESS + ", Name: "
                + HOSTNAME + ". (#0103).", MAC_ADDRESS, HOSTNAME, "0103");
    }

    private void assertEntry(final String message, final String expectedMac, final String expectedName,
            final String expectedId) {
        assertMatched(message, new WifiDeviceDisconnectedHard(expectedMac, expectedName, expectedId));
    }

    @Override
    protected AbstractEventLogEntryFactory<WifiDeviceDisconnectedHard> createFactory() {
        return new WifiDeviceDisconnectedHardFactory();
    }
}
