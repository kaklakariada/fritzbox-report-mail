package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import java.util.List;

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceAuthorizationFailed;

public class WifiDeviceAuthorizationFailedFactory extends EventLogEntryFactory<WifiDeviceAuthorizationFailed> {

    WifiDeviceAuthorizationFailedFactory() {
        super("WLAN-Anmeldung ist gescheitert: Autorisierung fehlgeschlagen. MAC-Adresse: " + MAC_ADDRESS_REGEXP + ".",
                1);
    }

    @Override
    protected WifiDeviceAuthorizationFailed createEventLogEntry(final String message, final List<String> groups) {
        return new WifiDeviceAuthorizationFailed(groups.get(0));
    }
}
