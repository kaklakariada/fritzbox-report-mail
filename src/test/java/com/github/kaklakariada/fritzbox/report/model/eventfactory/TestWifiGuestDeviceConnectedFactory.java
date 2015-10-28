package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.WifiGuestDeviceConnected;
import com.github.kaklakariada.fritzbox.report.model.eventfactory.WifiGuestDeviceConnectedFactory;

public class TestWifiGuestDeviceConnectedFactory {

    private static final LocalDateTime TIMESTAMP = LocalDateTime.now();
    private WifiGuestDeviceConnectedFactory factory;

    @Before
    public void setUp() {
        factory = new WifiGuestDeviceConnectedFactory();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch() {
        factory.createEventLogEntryInternal(TIMESTAMP, "no match");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch1() {
        assertEntry("WLAN-Gerät angemeldet. Geschwindigkeit 54 Mbit/s. MAC-Adresse: 00:24:D2:37:75:F1, Name: iradio.",
                "54 Mbit/s", "00:24:D2:37:75:F1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch2() {
        assertEntry(
                "WLAN-Gerät hat sich neu angemeldet. Geschwindigkeit 65 Mbit/s. MAC-Adresse: 70:73:CB:49:92:7C, Name: ipod.",
                "65 Mbit/s", "70:73:CB:49:92:7C");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch3() {
        assertEntry(
                "Neues WLAN-Gerät erstmalig angemeldet. Geschwindigkeit 130 Mbit/s. MAC-Adresse: 58:B0:35:73:20:F5, Name: chpimbp.",
                "130 Mbit/s", "58:B0:35:73:20:F5");
    }

    @Test
    public void testMatch1() {
        assertEntry(
                "WLAN-Gerät über Gastzugang angemeldet. Geschwindigkeit 130 Mbit/s. MAC-Adresse: F0:B4:79:15:50:F5.",
                "130 Mbit/s", "F0:B4:79:15:50:F5");
    }

    private void assertEntry(final String message, final String expectedSpeed, final String expectedMac) {
        final WifiGuestDeviceConnected entry = factory.createEventLogEntryInternal(TIMESTAMP, message);
        assertEquals(expectedSpeed, entry.getSpeed());
        assertEquals(expectedMac, entry.getMacAddress());
    }
}
