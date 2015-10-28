package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.kaklakariada.fritzbox.reports.model.Event;

public abstract class EventLogEntryFactory<T extends Event> {

    protected final static String MAC_ADDRESS_REGEXP = "((?:[0-9A-Fa-f]{2}[:-]){5}[0-9A-Fa-f]{2})";
    protected final static String EVERYTHING_UNTIL_PERIOD_REGEXP = "([^.]+?)";

    private final static List<EventLogEntryFactory<?>> factories = Arrays.asList(new WifiDeviceConnectedFactory(),
            new WifiDeviceDisconnectedFactory(), new WifiDeviceDisconnectedHardFactory(),
            new WifiGuestDeviceConnectedFactory(), new WifiGuestDeviceDisconnectedFactory(),
            new WifiDeviceDisconnectedHardFactory(), new WifiDeviceAuthorizationFailedFactory());

    private final Pattern pattern;

    private final int expectedGroupCount;

    protected EventLogEntryFactory(final String regex, final int expectedGroupCount) {
        this.expectedGroupCount = expectedGroupCount;
        this.pattern = Pattern.compile(regex);
    }

    public static Event createEventLogEntry(final LocalDateTime timestamp, final String message) {
        for (final EventLogEntryFactory<?> factory : factories) {
            if (factory.matches(message)) {
                return factory.createEventLogEntryInternal(timestamp, message);
            }
        }
        return null;
    }

    private boolean matches(final String message) {
        return createMatcher(message).matches();
    }

    private Matcher createMatcher(final String message) {
        return pattern.matcher(message);
    }

    T createEventLogEntryInternal(final LocalDateTime timestamp, final String message) {
        final Matcher matcher = createMatcher(message);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("String '" + message + "' does not match '" + pattern + "'");
        }
        return createEventLogEntry(timestamp, message, matcher.toMatchResult());
    }

    protected T createEventLogEntry(final LocalDateTime timestamp, final String message, final MatchResult matcher) {
        final int groupCount = matcher.groupCount();
        if (groupCount != expectedGroupCount) {
            throw new AssertionError("Expected " + expectedGroupCount + " but got " + groupCount
                    + " groups for regexp '" + pattern + "' and input string '" + message + "'");
        }
        final List<String> groups = new ArrayList<>(groupCount);
        for (int i = 1; i <= groupCount; i++) {
            groups.add(matcher.group(i));
        }
        return createEventLogEntry(timestamp, message, groups);
    }

    protected abstract T createEventLogEntry(LocalDateTime timestamp, String message, List<String> groups);
}
