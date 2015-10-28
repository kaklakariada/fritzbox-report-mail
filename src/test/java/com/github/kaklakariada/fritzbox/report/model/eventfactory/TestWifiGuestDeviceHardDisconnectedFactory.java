package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.WifiGuestDeviceDisconnectedHard;
import com.github.kaklakariada.fritzbox.report.model.eventfactory.WifiGuestDeviceDisconnectedHardFactory;

public class TestWifiGuestDeviceHardDisconnectedFactory {

    private static final LocalDateTime TIMESTAMP = LocalDateTime.now();
    private WifiGuestDeviceDisconnectedHardFactory factory;

    @Before
    public void setUp() {
        factory = new WifiGuestDeviceDisconnectedHardFactory();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch() {
        factory.createEventLogEntryInternal(TIMESTAMP, "no match");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch1() {
        assertEntry(
                "WLAN-Gerät wird abgemeldet: WLAN-Gerät antwortet nicht. MAC-Adresse: 00:24:D2:37:75:F1, Name: iradio. (#0103).",
                "00:24:D2:37:75:F1", "0103");
    }

    @Test
    public void testMatch1() {
        assertEntry(
                "WLAN-Gerät am Gastzugang wird abgemeldet: WLAN-Gerät antwortet nicht. MAC-Adresse: 8C:2D:AA:0D:D6:F0. (#0103).",
                "8C:2D:AA:0D:D6:F0", "0103");
    }

    private void assertEntry(final String message, final String expectedMac, final String expectedId) {
        final WifiGuestDeviceDisconnectedHard entry = factory.createEventLogEntryInternal(TIMESTAMP, message);
        assertEquals(expectedMac, entry.getMacAddress());
        assertEquals(expectedId, entry.getCode());
    }
}
