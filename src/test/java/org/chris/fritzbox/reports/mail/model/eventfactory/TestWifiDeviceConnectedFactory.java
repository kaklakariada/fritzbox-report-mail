package org.chris.fritzbox.reports.mail.model.eventfactory;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.chris.fritzbox.reports.mail.model.event.WifiDeviceConnected;
import org.junit.Before;
import org.junit.Test;

public class TestWifiDeviceConnectedFactory {

    private static final LocalDateTime TIMESTAMP = LocalDateTime.now();
    private WifiDeviceConnectedFactory factory;

    @Before
    public void setUp() {
        factory = new WifiDeviceConnectedFactory();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch() {
        factory.createEventLogEntryInternal(TIMESTAMP, "no match");
    }

    @Test
    public void testMatch1() {
        assertEntry("WLAN-Gerät angemeldet. Geschwindigkeit 54 Mbit/s. MAC-Adresse: 00:24:D2:37:75:F1, Name: iradio.",
                "54 Mbit/s", "00:24:D2:37:75:F1", "iradio");
    }

    @Test
    public void testMatch2() {
        assertEntry(
                "WLAN-Gerät hat sich neu angemeldet. Geschwindigkeit 65 Mbit/s. MAC-Adresse: 70:73:CB:49:92:7C, Name: ipod.",
                "65 Mbit/s", "70:73:CB:49:92:7C", "ipod");
    }

    @Test
    public void testMatch3() {
        assertEntry(
                "Neues WLAN-Gerät erstmalig angemeldet. Geschwindigkeit 130 Mbit/s. MAC-Adresse: 58:B0:35:73:20:F5, Name: chpimbp.",
                "130 Mbit/s", "58:B0:35:73:20:F5", "chpimbp");
    }

    private void assertEntry(final String message, final String expectedSpeed, final String expectedMac,
            final String expectedName) {
        final WifiDeviceConnected entry = factory.createEventLogEntryInternal(TIMESTAMP, message);
        assertEquals(expectedSpeed, entry.getSpeed());
        assertEquals(expectedMac, entry.getMacAddress());
        assertEquals(expectedName, entry.getName());
    }
}
