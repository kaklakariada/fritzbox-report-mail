package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnectedHard;
import com.github.kaklakariada.fritzbox.report.model.eventfactory.WifiDeviceDisconnectedHardFactory;

public class TestWifiDeviceHardDisconnectedFactory {

    private static final LocalDateTime TIMESTAMP = LocalDateTime.now();
    private WifiDeviceDisconnectedHardFactory factory;

    @Before
    public void setUp() {
        factory = new WifiDeviceDisconnectedHardFactory();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch() {
        factory.createEventLogEntryInternal(TIMESTAMP, "no match");
    }

    @Test
    public void testMatch1() {
        assertEntry(
                "WLAN-Gerät wird abgemeldet: WLAN-Gerät antwortet nicht. MAC-Adresse: 00:24:D2:37:75:F1, Name: iradio. (#0103).",
                "00:24:D2:37:75:F1", "iradio", "0103");
    }

    private void assertEntry(final String message, final String expectedMac, final String expectedName,
            final String expectedId) {
        final WifiDeviceDisconnectedHard entry = factory.createEventLogEntryInternal(TIMESTAMP, message);
        assertEquals(expectedMac, entry.getMacAddress());
        assertEquals(expectedName, entry.getName());
        assertEquals(expectedId, entry.getCode());
    }
}
