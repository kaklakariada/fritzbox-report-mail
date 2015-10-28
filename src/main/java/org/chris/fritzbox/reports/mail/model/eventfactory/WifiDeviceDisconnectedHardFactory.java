package org.chris.fritzbox.reports.mail.model.eventfactory;

import java.time.LocalDateTime;
import java.util.List;

import org.chris.fritzbox.reports.mail.model.event.WifiDeviceDisconnectedHard;

public class WifiDeviceDisconnectedHardFactory extends EventLogEntryFactory<WifiDeviceDisconnectedHard> {

    protected WifiDeviceDisconnectedHardFactory() {
        super("WLAN-Gerät wird abgemeldet: WLAN-Gerät antwortet nicht. MAC-Adresse: " + MAC_ADDRESS_REGEXP + ", Name: "
                + EVERYTHING_UNTIL_PERIOD_REGEXP + ". \\(#(\\d+)\\).", 3);
    }

    @Override
    protected WifiDeviceDisconnectedHard createEventLogEntry(final LocalDateTime timestamp, final String message,
            final List<String> groups) {
        return new WifiDeviceDisconnectedHard(groups.get(0), groups.get(1), groups.get(2));
    }
}
