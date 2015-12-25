package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.Event;

public abstract class EventLogEntryFactoryTestBase<T extends Event> {

    protected LocalDateTime timestamp;

    @Before
    public void setup() {
        timestamp = LocalDateTime.now();
    }

    @Test
    public void testStringNoMatch() {
        assertMatchFailed("no match");
    }

    @Test
    public void testEmptyStringNoMatch() {
        assertMatchFailed("");
    }

    @Test(expected = NullPointerException.class)
    public void testNullStringNoMatch() {
        createFactory().createEventLogEntryInternal(timestamp, null);
    }

    protected void assertMatched(String message, T expectedEvent) {
        final T actual = createFactory().createEventLogEntryInternal(timestamp, message);
        assertEquals(expectedEvent.toString(), actual.toString());
        assertEquals(expectedEvent, actual);
    }

    protected void assertMatchFailed(String message) {
        try {
            createFactory().createEventLogEntryInternal(timestamp, message);
            fail("Exepected failure for message " + message);
        } catch (final IllegalArgumentException expected) {

        }
    }

    protected abstract EventLogEntryFactory<T> createFactory();
}
