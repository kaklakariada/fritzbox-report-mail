package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import java.util.List;

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceConnected;

public class WifiDeviceConnectedFactory extends EventLogEntryFactory<WifiDeviceConnected> {

    WifiDeviceConnectedFactory() {
        super("(?:Neues )?WLAN-Gerät (?:erstmalig )?(?:hat sich neu )?angemeldet. Geschwindigkeit "
                + EVERYTHING_UNTIL_PERIOD_REGEXP + ". MAC-Adresse: " + MAC_ADDRESS_REGEXP + ", Name: "
                + EVERYTHING_UNTIL_PERIOD_REGEXP + ".", 3);
    }

    @Override
    protected WifiDeviceConnected createEventLogEntry(final String message, final List<String> groups) {
        return new WifiDeviceConnected(groups.get(0), groups.get(1), groups.get(2));
    }
}
