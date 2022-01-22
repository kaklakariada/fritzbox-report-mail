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

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceConnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiType;
import com.github.kaklakariada.fritzbox.report.model.regex.MatchedRegex;
import com.github.kaklakariada.fritzbox.report.model.regex.Regex;

public class WifiDeviceConnectedFactory extends AbstractEventLogEntryFactory<WifiDeviceConnected> {

    WifiDeviceConnectedFactory() {
        super(Regex.create("(?:Neues )?WLAN-Gerät (?:erstmalig )?(?:hat sich neu )?angemeldet\\. Geschwindigkeit "
                + EVERYTHING_UNTIL_PERIOD_REGEXP + ". MAC-Adresse: " + MAC_ADDRESS_REGEXP + ", Name: "
                + EVERYTHING_UNTIL_PERIOD_REGEXP + "\\.", 3),
                Regex.create(
                        "WLAN-Gerät angemeldet \\(([^)]+)\\), " + EVERYTHING_UNTIL_COMMA_REGEXP + ", "
                                + EVERYTHING_UNTIL_COMMA_REGEXP + ", IP " + IPV4_ADDRESS_REGEXP + ", MAC "
                                + MAC_ADDRESS_REGEXP + "\\.",
                        5),
                Regex.create(
                        "WLAN-Gerät angemeldet \\(([^)]+)\\)\\. Geschwindigkeit " + EVERYTHING_UNTIL_PERIOD_REGEXP
                                + "\\. MAC-Adresse: " + MAC_ADDRESS_REGEXP + ", Name: " + EVERYTHING_UNTIL_PERIOD_REGEXP
                                + "\\.",
                        4));
    }

    @Override
    protected WifiDeviceConnected createEventLogEntry(MatchedRegex regexp) {
        final List<String> groups = regexp.getGroups();
        switch (groups.size()) {
        case 3:
            return new WifiDeviceConnected(groups.get(0), null, null, groups.get(1), groups.get(2));
        case 4:
            return new WifiDeviceConnected(groups.get(1), WifiType.parse(groups.get(0)), null, groups.get(2),
                    groups.get(3));
        case 5:
            return new WifiDeviceConnected(groups.get(1), WifiType.parse(groups.get(0)), groups.get(3), groups.get(4),
                    groups.get(2));
        default:
            throw new IllegalArgumentException("Unexpected group count: " + regexp);
        }
    }
}
