package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import java.util.List;

import com.github.kaklakariada.fritzbox.report.model.event.IPv4InternetConnectionEstablished;

public class IPv4InternetConnectionEstablishedFactory
        extends AbstractEventLogEntryFactory<IPv4InternetConnectionEstablished> {

    protected IPv4InternetConnectionEstablishedFactory() {
        super("Internetverbindung wurde erfolgreich hergestellt. IP-Adresse: " + IPV4_ADDRESS_REGEXP + ", DNS-Server: "
                + IPV4_ADDRESS_REGEXP + " und " + IPV4_ADDRESS_REGEXP + ", Gateway: " + IPV4_ADDRESS_REGEXP
                + ", Breitband-PoP: " + NON_WHITESPACE_REGEXP, 5);
    }

    @Override
    protected IPv4InternetConnectionEstablished createEventLogEntry(String message, List<String> groups) {
        return new IPv4InternetConnectionEstablished(groups.get(0), groups.get(1), groups.get(2), groups.get(3),
                groups.get(4));
    }
}
