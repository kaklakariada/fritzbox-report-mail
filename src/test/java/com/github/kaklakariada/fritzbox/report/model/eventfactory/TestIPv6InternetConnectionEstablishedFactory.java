package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.IPv6InternetConnectionEstablished;

public class TestIPv6InternetConnectionEstablishedFactory
        extends EventLogEntryFactoryTestBase<IPv6InternetConnectionEstablished> {

    @Test
    public void testFailureTrailingWhitespace() {
        assertMatchFailed("Internetverbindung IPv6 wurde erfolgreich hergestellt. IP-Adresse: " + IPv6_ADDRESS + " ");
    }

    @Test
    public void testSuccess() {
        assertMatched("Internetverbindung IPv6 wurde erfolgreich hergestellt. IP-Adresse: " + IPv6_ADDRESS,
                new IPv6InternetConnectionEstablished(IPv6_ADDRESS));
    }

    @Override
    protected AbstractEventLogEntryFactory<IPv6InternetConnectionEstablished> createFactory() {
        return new IPv6InternetConnectionEstablishedFactory();
    }
}
