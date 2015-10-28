package org.chris.fritzbox.reports.mail.model.eventfactory;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.chris.fritzbox.reports.mail.model.event.WifiDeviceDisconnected;
import org.junit.Before;
import org.junit.Test;

public class TestWifiDeviceDisconnectedFactory {

    private static final LocalDateTime TIMESTAMP = LocalDateTime.now();
    private WifiDeviceDisconnectedFactory factory;

    @Before
    public void setUp() {
        factory = new WifiDeviceDisconnectedFactory();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch() {
        factory.createEventLogEntryInternal(TIMESTAMP, "no match");
    }

    @Test
    public void testMatch1() {
        assertEntry("WLAN-Gerät hat sich abgemeldet. MAC-Adresse: 70:73:CB:49:92:7C, Name: ipod.", "70:73:CB:49:92:7C",
                "ipod");
    }

    @Test
    public void testMatch2() {
        assertEntry("WLAN-Gerät hat sich abgemeldet. MAC-Adresse: 70:73:CB:49:92:7C, Name: ipod.", "70:73:CB:49:92:7C",
                "ipod");
    }

    private void assertEntry(final String message, final String expectedMac, final String expectedName) {
        final WifiDeviceDisconnected entry = factory.createEventLogEntryInternal(TIMESTAMP, message);
        assertEquals(expectedMac, entry.getMacAddress());
        assertEquals(expectedName, entry.getName());
    }
}
