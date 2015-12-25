package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceAuthorizationFailed;

public class TestWifiDeviceAuthorizationFailedFactory
        extends EventLogEntryFactoryTestBase<WifiDeviceAuthorizationFailed> {

    @Test
    public void testNoMatch1() {
        assertMatchFailed("WLAN-Gerät angemeldet. Geschwindigkeit 54 Mbit/s. MAC-Adresse: " + MAC_ADDRESS + ", Name: "
                + HOSTNAME + ".");
    }

    @Test
    public void testNoMatch2() {
        assertMatchFailed("WLAN-Gerät hat sich neu angemeldet. Geschwindigkeit 65 Mbit/s. MAC-Adresse: " + MAC_ADDRESS
                + ", Name: " + HOSTNAME + ".");
    }

    @Test
    public void testNoMatch3() {
        assertMatchFailed("Neues WLAN-Gerät erstmalig angemeldet. Geschwindigkeit 130 Mbit/s. MAC-Adresse: "
                + MAC_ADDRESS + ", Name: " + HOSTNAME + ".");
    }

    @Test
    public void testMatch() {
        assertEntry("WLAN-Anmeldung ist gescheitert: Autorisierung fehlgeschlagen. MAC-Adresse: " + MAC_ADDRESS + ".",
                MAC_ADDRESS);
    }

    private void assertEntry(final String message, final String expectedMac) {
        assertMatched(message, new WifiDeviceAuthorizationFailed(expectedMac));
    }

    @Override
    protected EventLogEntryFactory<WifiDeviceAuthorizationFailed> createFactory() {
        return new WifiDeviceAuthorizationFailedFactory();
    }
}
