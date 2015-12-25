package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import java.time.LocalDateTime;
import java.util.List;

import com.github.kaklakariada.fritzbox.report.model.event.WifiGuestDeviceDisconnected;

public class WifiGuestDeviceDisconnectedFactory extends EventLogEntryFactory<WifiGuestDeviceDisconnected> {

    protected WifiGuestDeviceDisconnectedFactory() {
        super("WLAN-Gerät hat sich vom Gastzugang abgemeldet. MAC-Adresse: " + MAC_ADDRESS_REGEXP + ".", 1);
    }

    @Override
    protected WifiGuestDeviceDisconnected createEventLogEntry(final LocalDateTime timestamp, final String message,
            final List<String> groups) {
        return new WifiGuestDeviceDisconnected(groups.get(0));
    }
}
