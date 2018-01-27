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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.kaklakariada.fritzbox.report.model.Event;

public abstract class AbstractEventLogEntryFactory<T extends Event> {

    protected final static String MAC_ADDRESS_REGEXP = "((?:[0-9A-Fa-f]{2}[:-]){5}[0-9A-Fa-f]{2})";
    protected final static String IPV4_ADDRESS_REGEXP = "((?:25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\."
            + "(?:25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\."
            + "(?:25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\."
            + "(?:25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9]))";
    protected final static String NON_WHITESPACE_REGEXP = "(\\S+)";
    protected final static String EVERYTHING_UNTIL_PERIOD_REGEXP = "([^.]+?)";

    private final Pattern pattern;
    private final int expectedGroupCount;

    protected AbstractEventLogEntryFactory(final String regex, final int expectedGroupCount) {
        this.expectedGroupCount = expectedGroupCount;
        this.pattern = Pattern.compile(regex);
    }

    boolean matches(final String message) {
        return createMatcher(message).matches();
    }

    private Matcher createMatcher(final String message) {
        return pattern.matcher(message);
    }

    T createEventLogEntryInternal(final String message) {
        final Matcher matcher = createMatcher(message);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("String '" + message + "' does not match '" + pattern + "'");
        }
        return createEventLogEntry(message, matcher.toMatchResult());
    }

    protected T createEventLogEntry(final String message, final MatchResult matcher) {
        final int groupCount = matcher.groupCount();
        if (groupCount != expectedGroupCount) {
            throw new AssertionError("Expected " + expectedGroupCount + " but got " + groupCount
                    + " groups for regexp '" + pattern + "' and input string '" + message + "'");
        }
        final List<String> groups = new ArrayList<>(groupCount);
        for (int i = 1; i <= groupCount; i++) {
            groups.add(matcher.group(i));
        }
        return createEventLogEntry(message, groups);
    }

    protected abstract T createEventLogEntry(String message, List<String> groups);
}
