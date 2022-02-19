/**
 * A Java API for parsing and processing status report mails of a FritzBox
 * Copyright (C) 2018 Christoph Pirkl <christoph at users.sourceforge.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General  License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General  License for more details.
 *
 * You should have received a copy of the GNU General  License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.kaklakariada.fritzbox.report.model.Event;

import org.junit.jupiter.api.Test;

abstract class EventLogEntryFactoryTestBase<T extends Event> {

    final static String MAC_ADDRESS = "00:11:22:33:44:55";
    final static String IP_ADDRESS1 = "11.22.33.44";
    final static String IP_ADDRESS2 = "111.122.133.144";
    final static String IP_ADDRESS3 = "111.122.133.144";
    final static String IP_ADDRESS4 = "111.122.133.144";
    final static String IP_ADDRESS_INVALID = "256.122.133.144";
    final static String IPv6_ADDRESS = "FEDC:BA98:7654:3210:FEDC:BA98:7654:3210";
    final static String HOSTNAME = "host-name.domain";

    @Test
    void testStringNoMatch() {
        assertMatchFailed("no match");
    }

    @Test
    void testEmptyStringNoMatch() {
        assertMatchFailed("");
    }

    @Test
    void testNullStringNoMatch() {
        AbstractEventLogEntryFactory<T> factory = createFactory();
        assertThrows(NullPointerException.class, () -> factory.createEventLogEntryInternal(null));
    }

    @SuppressWarnings("unchecked")
    protected void assertMatched(String message, T expectedEvent) {
        assertAll(
                () -> assertEvent(expectedEvent, createFactory().createEventLogEntryInternal(message)),
                () -> assertEvent(expectedEvent, (T) new EventLogEntryFactory().createEventLogEntry(message)));
    }

    private void assertEvent(T expectedEvent, final T actualEvent) {
        assertAll(
                () -> assertThat(actualEvent, notNullValue()),
                () -> assertEquals(expectedEvent.toString(), actualEvent.toString()),
                () -> assertEquals(expectedEvent, actualEvent));
    }

    protected void assertMatchFailed(String message) {
        final AbstractEventLogEntryFactory<T> factory = createFactory();

        assertThrows(IllegalStateException.class, () -> factory.createEventLogEntryInternal(message));
    }

    protected abstract AbstractEventLogEntryFactory<T> createFactory();
}
