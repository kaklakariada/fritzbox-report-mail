package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import java.time.LocalDateTime;
import java.util.List;

import com.github.kaklakariada.fritzbox.report.model.event.WifiGuestDeviceConnected;

public class WifiGuestDeviceConnectedFactory extends EventLogEntryFactory<WifiGuestDeviceConnected> {

    WifiGuestDeviceConnectedFactory() {
        super("WLAN-Gerät über Gastzugang angemeldet. Geschwindigkeit " + EVERYTHING_UNTIL_PERIOD_REGEXP
                + ". MAC-Adresse: " + MAC_ADDRESS_REGEXP + ".", 2);
    }

    @Override
    protected WifiGuestDeviceConnected createEventLogEntry(final LocalDateTime timestamp, final String message,
            final List<String> groups) {
        return new WifiGuestDeviceConnected(groups.get(0), groups.get(1));
    }
}
