/**
 * A Java API for parsing and processing status report mails of a FritzBox
 * Copyright (C) 2018 Christoph Pirkl <christoph at users.sourceforge.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.kaklakariada.fritzbox.report.model.Event;

public class EventLogEntryFactory {

    private final List<AbstractEventLogEntryFactory<?>> factories;

    private EventLogEntryFactory(final List<AbstractEventLogEntryFactory<?>> factories) {
        this.factories = new ArrayList<>(factories);
    }

    public EventLogEntryFactory() {
        this(asList(new WifiDeviceConnectedFactory(), new WifiDeviceDisconnectedFactory(),
                new WifiDeviceDisconnectedHardFactory(), new WifiGuestDeviceConnectedFactory(),
                new WifiGuestDeviceDisconnectedFactory(), new WifiGuestDeviceDisconnectedHardFactory(),
                new WifiDeviceDisconnectedHardFactory(), new WifiDeviceAuthorizationFailedFactory(),
                new IPv4InternetConnectionEstablishedFactory(), new IPv6InternetConnectionEstablishedFactory(),
                new IPv6PrefixAssignedFactory(), new DslSyncFailedFactory(), new DslSyncSuccessfulFactory(),
                new InternetDisconnectedFactory()));
    }

    public Event createEventLogEntry(final String message) {
        final String rawMessage = removeRepeatedSuffix(message);
        for (final AbstractEventLogEntryFactory<?> factory : factories) {
            if (factory.matches(rawMessage)) {
                return factory.createEventLogEntryInternal(rawMessage);
            }
        }
        return null;
    }

    @SuppressWarnings("java:S5852") // Input size is small, no risk of catastrophic backtracking.
    private static final Pattern REPEATED_SUFFIX = Pattern
            .compile("^(.*?)\\s*\\[\\d+ Meldungen seit [\\d\\.\\s:]{8,20}\\]$");

    private String removeRepeatedSuffix(final String message) {
        final Matcher matcher = REPEATED_SUFFIX.matcher(message);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return message;
    }
}
