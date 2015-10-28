package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.WifiGuestDeviceDisconnected;
import com.github.kaklakariada.fritzbox.report.model.eventfactory.WifiGuestDeviceDisconnectedFactory;

public class TestWifiGuestDeviceDisconnectedFactory {

    private static final LocalDateTime TIMESTAMP = LocalDateTime.now();
    private WifiGuestDeviceDisconnectedFactory factory;

    @Before
    public void setUp() {
        factory = new WifiGuestDeviceDisconnectedFactory();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch() {
        factory.createEventLogEntryInternal(TIMESTAMP, "no match");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch1() {
        assertEntry("WLAN-Gerät hat sich abgemeldet. MAC-Adresse: 70:73:CB:49:92:7C, Name: ipod.", "70:73:CB:49:92:7C");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoMatch2() {
        assertEntry("WLAN-Gerät hat sich abgemeldet. MAC-Adresse: 70:73:CB:49:92:7C, Name: ipod.", "70:73:CB:49:92:7C");
    }

    @Test
    public void testMatch() {
        assertEntry("WLAN-Gerät hat sich vom Gastzugang abgemeldet. MAC-Adresse: F0:B4:79:15:50:F5.",
                "F0:B4:79:15:50:F5");
    }

    private void assertEntry(final String message, final String expectedMac) {
        final WifiGuestDeviceDisconnected entry = factory.createEventLogEntryInternal(TIMESTAMP, message);
        assertEquals(expectedMac, entry.getMacAddress());
    }
}
