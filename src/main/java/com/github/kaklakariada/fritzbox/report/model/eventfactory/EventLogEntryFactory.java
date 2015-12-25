package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kaklakariada.fritzbox.report.model.Event;

public class EventLogEntryFactory {

    private final static Logger LOG = LoggerFactory.getLogger(EventLogEntryFactory.class);

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
                new WifiGuestDeviceDisconnectedHardFactory(), //
                new WifiDeviceDisconnectedHardFactory(), //
                new WifiDeviceAuthorizationFailedFactory()));
    }

    public Event createEventLogEntry(final String message) {
        for (final AbstractEventLogEntryFactory<?> factory : factories) {
            if (factory.matches(message)) {
                LOG.trace("Factory {} can handle message '{}'.", factory.getClass().getSimpleName(), message);
                return factory.createEventLogEntryInternal(message);
            }
        }
        LOG.trace("None of the {} factories can handle message '{}'.", factories.size(), message);
        return null;
    }
}
