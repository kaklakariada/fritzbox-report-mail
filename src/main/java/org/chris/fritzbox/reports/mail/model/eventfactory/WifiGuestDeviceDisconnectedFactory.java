package org.chris.fritzbox.reports.mail.model.eventfactory;

import java.time.LocalDateTime;
import java.util.List;

import org.chris.fritzbox.reports.mail.model.event.WifiGuestDeviceDisconnected;

public class WifiGuestDeviceDisconnectedFactory extends EventLogEntryFactory<WifiGuestDeviceDisconnected> {

    protected WifiGuestDeviceDisconnectedFactory() {
        // WLAN-Gerät hat sich vom Gastzugang abgemeldet. MAC-Adresse:
        // F0:B4:79:15:50:F5.
        super("WLAN-Gerät hat sich vom Gastzugang abgemeldet. MAC-Adresse: " + MAC_ADDRESS_REGEXP + ".", 1);
    }

    @Override
    protected WifiGuestDeviceDisconnected createEventLogEntry(final LocalDateTime timestamp, final String message,
            final List<String> groups) {
        return new WifiGuestDeviceDisconnected(groups.get(0));
    }
}
