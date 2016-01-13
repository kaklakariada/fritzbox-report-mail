package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import org.junit.Test;

import com.github.kaklakariada.fritzbox.report.model.event.IPv4InternetConnectionEstablished;

public class TestIPv4InternetConnectionEstablishedFactory
        extends EventLogEntryFactoryTestBase<IPv4InternetConnectionEstablished> {

    @Test
    public void testFailureInvalidIpAddress() {
        assertMatchFailed("Internetverbindung wurde erfolgreich hergestellt. IP-Adresse: " + IP_ADDRESS_INVALID
                + ", DNS-Server: " + IP_ADDRESS2 + " und " + IP_ADDRESS3 + ", Gateway: " + IP_ADDRESS4
                + ", Breitband-PoP: ABCD21-se800-B226E1910E02MB");
    }

    @Test
    public void testFailureTrailingWhitespace() {
        assertMatchFailed("Internetverbindung wurde erfolgreich hergestellt. IP-Adresse: " + IP_ADDRESS1
                + ", DNS-Server: " + IP_ADDRESS2 + " und " + IP_ADDRESS3 + ", Gateway: " + IP_ADDRESS4
                + ", Breitband-PoP: ABCD21-se800-B226E1910E02MB  ");
    }

    @Test
    public void testSuccess() {
        assertMatched(
                "Internetverbindung wurde erfolgreich hergestellt. IP-Adresse: " + IP_ADDRESS1 + ", DNS-Server: "
                        + IP_ADDRESS2 + " und " + IP_ADDRESS3 + ", Gateway: " + IP_ADDRESS4
                        + ", Breitband-PoP: ABCD21-se800-B226E1910E02MB",
                new IPv4InternetConnectionEstablished(IP_ADDRESS1, IP_ADDRESS2, IP_ADDRESS3, IP_ADDRESS4,
                        "ABCD21-se800-B226E1910E02MB"));
    }

    @Override
    protected AbstractEventLogEntryFactory<IPv4InternetConnectionEstablished> createFactory() {
        return new IPv4InternetConnectionEstablishedFactory();
    }
}
