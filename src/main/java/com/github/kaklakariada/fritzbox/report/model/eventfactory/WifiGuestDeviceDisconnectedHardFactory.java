package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import java.util.List;

import com.github.kaklakariada.fritzbox.report.model.event.WifiGuestDeviceDisconnectedHard;

public class WifiGuestDeviceDisconnectedHardFactory extends EventLogEntryFactory<WifiGuestDeviceDisconnectedHard> {

    protected WifiGuestDeviceDisconnectedHardFactory() {
        super("WLAN-Gerät am Gastzugang wird abgemeldet: WLAN-Gerät antwortet nicht. MAC-Adresse: " + MAC_ADDRESS_REGEXP
                + ". \\(#(\\d+)\\).", 2);
    }

    @Override
    protected WifiGuestDeviceDisconnectedHard createEventLogEntry(final String message, final List<String> groups) {
        return new WifiGuestDeviceDisconnectedHard(groups.get(0), groups.get(1));
    }
}
