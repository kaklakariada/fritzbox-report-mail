package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import java.util.List;

import com.github.kaklakariada.fritzbox.report.model.event.IPv6InternetConnectionEstablished;

public class IPv6InternetConnectionEstablishedFactory
        extends AbstractEventLogEntryFactory<IPv6InternetConnectionEstablished> {

    protected IPv6InternetConnectionEstablishedFactory() {
        super("Internetverbindung IPv6 wurde erfolgreich hergestellt. IP-Adresse: " + NON_WHITESPACE_REGEXP, 1);
    }

    @Override
    protected IPv6InternetConnectionEstablished createEventLogEntry(String message, List<String> groups) {
        return new IPv6InternetConnectionEstablished(groups.get(0));
    }
}
