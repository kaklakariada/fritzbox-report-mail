package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import java.util.List;

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnected;

public class WifiDeviceDisconnectedFactory extends AbstractEventLogEntryFactory<WifiDeviceDisconnected> {

    protected WifiDeviceDisconnectedFactory() {
        super("WLAN-Gerät hat sich abgemeldet. MAC-Adresse: " + MAC_ADDRESS_REGEXP + ", Name: "
                + EVERYTHING_UNTIL_PERIOD_REGEXP + ".", 2);
    }

    @Override
    protected WifiDeviceDisconnected createEventLogEntry(final String message, final List<String> groups) {
        return new WifiDeviceDisconnected(groups.get(0), groups.get(1));
    }
}
