package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.Event;

public abstract class EventLogEntryFactoryTestBase<T extends Event> {

    public final static String MAC_ADDRESS = "00:11:22:33:44:55";
    public final static String IP_ADDRESS = "11.22.33.44";
    public final static String HOSTNAME = "hostname";

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
        createFactory().createEventLogEntryInternal(null);
    }

    @SuppressWarnings("unchecked")
    protected void assertMatched(String message, T expectedEvent) {
        assertEvent(expectedEvent, createFactory().createEventLogEntryInternal(message));
        assertEvent(expectedEvent, (T) new EventLogEntryFactory().createEventLogEntry(message));
    }

    private void assertEvent(T expectedEvent, final T actualEvent) {
        assertThat(actualEvent, notNullValue());
        assertEquals(expectedEvent.toString(), actualEvent.toString());
        assertEquals(expectedEvent, actualEvent);
    }

    protected void assertMatchFailed(String message) {
        try {
            createFactory().createEventLogEntryInternal(message);
            fail("Exepected failure for message " + message);
        } catch (final IllegalArgumentException expected) {

        }
    }

    protected abstract AbstractEventLogEntryFactory<T> createFactory();
}
