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

import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.Optional;

import com.github.kaklakariada.fritzbox.report.model.Event;
import com.github.kaklakariada.fritzbox.report.model.regex.MatchedRegex;
import com.github.kaklakariada.fritzbox.report.model.regex.Regex;
import com.github.kaklakariada.fritzbox.report.model.regex.RegexMapper;

public abstract class AbstractEventLogEntryFactory<T extends Event> {

    protected static final String MAC_ADDRESS_REGEXP = "((?:[0-9A-Fa-f]{2}[:-]){5}[0-9A-Fa-f]{2})";
    protected static final String IPV4_ADDRESS_REGEXP = "((?:25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\."
            + "(?:25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\."
            + "(?:25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\."
            + "(?:25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9]))";
    protected static final String NON_WHITESPACE_REGEXP = "(\\S+)";
    protected static final String EVERYTHING_UNTIL_PERIOD_REGEXP = "([^.]+?)";
    protected static final String EVERYTHING_UNTIL_COMMA_REGEXP = "([^,]+?)";
    protected static final String WIFI_TYPE_REGEXP = "\\(([^)]+)\\)";

    private final List<Regex> regex;

    protected AbstractEventLogEntryFactory(final String regex, final int expectedGroupCount) {
        this(Regex.create(regex, expectedGroupCount, null));
    }

    protected AbstractEventLogEntryFactory(Regex... regexps) {
        regex = List.of(regexps);
    }

    boolean matches(final String message) {
        return firstMatchingMatcher(message).isPresent();
    }

    private Optional<MatchedRegex> firstMatchingMatcher(final String message) {
        return regex.stream().map(regex -> regex.matches(message))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    T createEventLogEntryInternal(final String message) {
        final MatchedRegex matchedRegex = firstMatchingMatcher(message).orElseThrow(
                () -> new IllegalStateException("Message '" + message + "' not matched by any of the regex\n  - "
                        + regex.stream().map(Regex::toString).collect(joining("\n  - "))));

        @SuppressWarnings("unchecked")
        final T event = (T) matchedRegex.getRegex().getMapper()
                .map((RegexMapper mapper) -> mapper.map(matchedRegex))
                .orElseGet(() -> createEventLogEntry(matchedRegex));
        return event;
    }

    protected T createEventLogEntry(final MatchedRegex matchedRegex) {
        return createEventLogEntry(matchedRegex.getInput(), matchedRegex.getGroups());
    }

    protected T createEventLogEntry(String message, List<String> groups) {
        throw new UnsupportedOperationException();
    }
}
