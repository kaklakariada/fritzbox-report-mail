package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.github.kaklakariada.fritzbox.report.model.Event;

public class EventLogEntryFactory {

    private final List<AbstractEventLogEntryFactory<?>> factories;

    private EventLogEntryFactory(List<AbstractEventLogEntryFactory<?>> factories) {
        this.factories = new ArrayList<>(factories);
    }

    public EventLogEntryFactory() {
        this(asList(new WifiDeviceConnectedFactory(), //
                new WifiDeviceDisconnectedFactory(), //
                new WifiDeviceDisconnectedHardFactory(), //
                new WifiGuestDeviceConnectedFactory(), //
                new WifiGuestDeviceDisconnectedFactory(), //
                new WifiDeviceDisconnectedHardFactory(), //
                new WifiDeviceAuthorizationFailedFactory()));
    }

    public Event createEventLogEntry(final String message) {
        for (final AbstractEventLogEntryFactory<?> factory : factories) {
            if (factory.matches(message)) {
                return factory.createEventLogEntryInternal(message);
            }
        }
        return null;
    }
}
