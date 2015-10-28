package org.chris.fritzbox.reports.mail.model.eventfactory;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.chris.fritzbox.reports.mail.model.event.WifiDeviceAuthorizationFailed;
import org.junit.Before;
import org.junit.Test;

public class TestWifiDeviceAuthorizationFailedFactory {

    private static final LocalDateTime TIMESTAMP = LocalDateTime.now();
    private WifiDeviceAuthorizationFailedFactory factory;

    @Before
    public void setUp() {
        factory = new WifiDeviceAuthorizationFailedFactory();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch() {
        factory.createEventLogEntryInternal(TIMESTAMP, "no match");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch1() {
        assertEntry("WLAN-Gerät angemeldet. Geschwindigkeit 54 Mbit/s. MAC-Adresse: 00:24:D2:37:75:F1, Name: iradio.",
                "00:24:D2:37:75:F1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch2() {
        assertEntry(
                "WLAN-Gerät hat sich neu angemeldet. Geschwindigkeit 65 Mbit/s. MAC-Adresse: 70:73:CB:49:92:7C, Name: ipod.",
                "70:73:CB:49:92:7C");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch3() {
        assertEntry(
                "Neues WLAN-Gerät erstmalig angemeldet. Geschwindigkeit 130 Mbit/s. MAC-Adresse: 58:B0:35:73:20:F5, Name: chpimbp.",
                "58:B0:35:73:20:F5");
    }

    @Test
    public void testMatch() {
        assertEntry("WLAN-Anmeldung ist gescheitert: Autorisierung fehlgeschlagen. MAC-Adresse: F0:B4:79:15:50:F5.",
                "F0:B4:79:15:50:F5");
    }

    private void assertEntry(final String message, final String expectedMac) {
        final WifiDeviceAuthorizationFailed entry = factory.createEventLogEntryInternal(TIMESTAMP, message);
        assertEquals(expectedMac, entry.getMacAddress());
    }
}
