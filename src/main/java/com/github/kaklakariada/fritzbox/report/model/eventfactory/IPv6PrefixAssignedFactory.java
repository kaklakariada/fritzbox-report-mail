/**
 * A Java API for parsing and processing status report mails of a FritzBox
 * Copyright (C) 2018 Christoph Pirkl <christoph at users.sourceforge.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
