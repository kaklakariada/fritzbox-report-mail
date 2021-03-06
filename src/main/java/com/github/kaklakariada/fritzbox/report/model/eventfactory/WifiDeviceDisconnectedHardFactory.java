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

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnectedHard;

public class WifiDeviceDisconnectedHardFactory extends AbstractEventLogEntryFactory<WifiDeviceDisconnectedHard> {

    protected WifiDeviceDisconnectedHardFactory() {
        super("WLAN-Gerät wird abgemeldet: WLAN-Gerät antwortet nicht. MAC-Adresse: " + MAC_ADDRESS_REGEXP + ", Name: "
                + EVERYTHING_UNTIL_PERIOD_REGEXP + ". \\(#(\\d+)\\).", 3);
    }

    @Override
    protected WifiDeviceDisconnectedHard createEventLogEntry(final String message, final List<String> groups) {
        return new WifiDeviceDisconnectedHard(groups.get(0), groups.get(1), groups.get(2));
    }
}
