package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.IPv6PrefixAssigned;

public class TestIPv6PrefixAssignedFactory extends EventLogEntryFactoryTestBase<IPv6PrefixAssigned> {

    @Test
    public void testNoMatchTrailingWhitespace() {
        assertMatchFailed("IPv6-Präfix wurde erfolgreich bezogen. Neues Präfix: prefix ");
    }

    @Test
    public void testMatch() {
        assertMatched("IPv6-Präfix wurde erfolgreich bezogen. Neues Präfix: prefix", new IPv6PrefixAssigned("prefix"));
    }

    @Override
    protected AbstractEventLogEntryFactory<IPv6PrefixAssigned> createFactory() {
        return new IPv6PrefixAssignedFactory();
    }
}
