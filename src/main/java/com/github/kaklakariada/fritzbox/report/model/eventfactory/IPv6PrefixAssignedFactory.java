package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import java.util.List;

import com.github.kaklakariada.fritzbox.report.model.event.IPv6PrefixAssigned;

public class IPv6PrefixAssignedFactory extends AbstractEventLogEntryFactory<IPv6PrefixAssigned> {

    protected IPv6PrefixAssignedFactory() {
        super("IPv6-Präfix wurde erfolgreich bezogen. Neues Präfix: " + NON_WHITESPACE_REGEXP, 1);
    }

    @Override
    protected IPv6PrefixAssigned createEventLogEntry(String message, List<String> groups) {
        return new IPv6PrefixAssigned(groups.get(0));
    }
}
