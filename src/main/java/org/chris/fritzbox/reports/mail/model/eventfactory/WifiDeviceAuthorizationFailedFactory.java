package org.chris.fritzbox.reports.mail.model.eventfactory;

import java.time.LocalDateTime;
import java.util.List;

import org.chris.fritzbox.reports.mail.model.event.WifiDeviceAuthorizationFailed;

public class WifiDeviceAuthorizationFailedFactory extends EventLogEntryFactory<WifiDeviceAuthorizationFailed> {

    WifiDeviceAuthorizationFailedFactory() {
        // WLAN-Anmeldung ist gescheitert: Autorisierung fehlgeschlagen.
        // MAC-Adresse: F0:B4:79:15:50:F5.
        super("WLAN-Anmeldung ist gescheitert: Autorisierung fehlgeschlagen. MAC-Adresse: " + MAC_ADDRESS_REGEXP + ".",
                1);
    }

    @Override
    protected WifiDeviceAuthorizationFailed createEventLogEntry(final LocalDateTime timestamp, final String message,
            final List<String> groups) {
        return new WifiDeviceAuthorizationFailed(groups.get(0));
    }
}
