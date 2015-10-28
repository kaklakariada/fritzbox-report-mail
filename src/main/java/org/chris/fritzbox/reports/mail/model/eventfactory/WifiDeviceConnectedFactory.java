package org.chris.fritzbox.reports.mail.model.eventfactory;

import java.time.LocalDateTime;
import java.util.List;

import org.chris.fritzbox.reports.mail.model.event.WifiDeviceConnected;

public class WifiDeviceConnectedFactory extends EventLogEntryFactory<WifiDeviceConnected> {

    WifiDeviceConnectedFactory() {
        // WLAN-Gerät angemeldet. Geschwindigkeit 54 Mbit/s. MAC-Adresse:
        // 00:24:D2:37:75:F1, Name: iradio.
        // "Neues WLAN-Gerät erstmalig angemeldet. Geschwindigkeit 130 Mbit/s. MAC-Adresse: 58:B0:35:73:20:F5, Name:
        // chpimbp."
        super("(?:Neues )?WLAN-Gerät (?:erstmalig )?(?:hat sich neu )?angemeldet. Geschwindigkeit "
                + EVERYTHING_UNTIL_PERIOD_REGEXP + ". MAC-Adresse: " + MAC_ADDRESS_REGEXP + ", Name: "
                + EVERYTHING_UNTIL_PERIOD_REGEXP + ".", 3);
    }

    @Override
    protected WifiDeviceConnected createEventLogEntry(final LocalDateTime timestamp, final String message,
            final List<String> groups) {
        return new WifiDeviceConnected(groups.get(0), groups.get(1), groups.get(2));
    }
}
