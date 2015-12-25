package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceAuthorizationFailed;

public class TestWifiDeviceAuthorizationFailedFactory
        extends EventLogEntryFactoryTestBase<WifiDeviceAuthorizationFailed> {

    public void testNoMatch1() {
        assertMatchFailed(
                "WLAN-Gerät angemeldet. Geschwindigkeit 54 Mbit/s. MAC-Adresse: 00:24:D2:37:75:F1, Name: iradio.");
    }

    public void testNoMatch2() {
        assertMatchFailed(
                "WLAN-Gerät hat sich neu angemeldet. Geschwindigkeit 65 Mbit/s. MAC-Adresse: 70:73:CB:49:92:7C, Name: ipod.");
    }

    public void testNoMatch3() {
        assertMatchFailed(
                "Neues WLAN-Gerät erstmalig angemeldet. Geschwindigkeit 130 Mbit/s. MAC-Adresse: 58:B0:35:73:20:F5, Name: chpimbp.");
    }

    @Test
    public void testMatch() {
        assertEntry("WLAN-Anmeldung ist gescheitert: Autorisierung fehlgeschlagen. MAC-Adresse: F0:B4:79:15:50:F5.",
                "F0:B4:79:15:50:F5");
    }

    private void assertEntry(final String message, final String expectedMac) {
        assertMatched(message, new WifiDeviceAuthorizationFailed(expectedMac));
    }

    @Override
    protected WifiDeviceAuthorizationFailedFactory createFactory() {
        return new WifiDeviceAuthorizationFailedFactory();
    }
}
