package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import java.time.LocalDateTime;
import java.util.List;

import com.github.kaklakariada.fritzbox.report.model.event.WifiGuestDeviceDisconnectedHard;

public class WifiGuestDeviceDisconnectedHardFactory extends EventLogEntryFactory<WifiGuestDeviceDisconnectedHard> {

    protected WifiGuestDeviceDisconnectedHardFactory() {
        // WLAN-Gerät am Gastzugang wird abgemeldet: WLAN-Gerät antwortet nicht.
        // MAC-Adresse: 8C:2D:AA:0D:D6:F0. (#0103).
        super("WLAN-Gerät am Gastzugang wird abgemeldet: WLAN-Gerät antwortet nicht. MAC-Adresse: " + MAC_ADDRESS_REGEXP
                + ". \\(#(\\d+)\\).", 2);
    }

    @Override
    protected WifiGuestDeviceDisconnectedHard createEventLogEntry(final LocalDateTime timestamp, final String message,
            final List<String> groups) {
        return new WifiGuestDeviceDisconnectedHard(groups.get(0), groups.get(1));
    }
}