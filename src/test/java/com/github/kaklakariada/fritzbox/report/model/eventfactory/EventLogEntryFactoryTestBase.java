package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import static org.junit.Assert.assertEquals;
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

    protected void assertMatched(String message, T expectedEvent) {
        final T actual = createFactory().createEventLogEntryInternal(message);
        assertEquals(expectedEvent.toString(), actual.toString());
        assertEquals(expectedEvent, actual);
    }

    protected void assertMatchFailed(String message) {
        try {
            createFactory().createEventLogEntryInternal(message);
            fail("Exepected failure for message " + message);
        } catch (final IllegalArgumentException expected) {

        }
    }

    protected abstract EventLogEntryFactory<T> createFactory();
}
