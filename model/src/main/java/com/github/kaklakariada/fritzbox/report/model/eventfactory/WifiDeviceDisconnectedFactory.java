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

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiType;
import com.github.kaklakariada.fritzbox.report.model.regex.Regex;

public class WifiDeviceDisconnectedFactory extends AbstractEventLogEntryFactory<WifiDeviceDisconnected> {

    protected WifiDeviceDisconnectedFactory() {
        super(Regex.create(
                "WLAN-Gerät hat sich abgemeldet\\. MAC-Adresse: " + MAC_ADDRESS_REGEXP + ", Name: "
                        + EVERYTHING_UNTIL_PERIOD_REGEXP + "\\.",
                2,
                groups -> new WifiDeviceDisconnected(null, groups.get(0), groups.get(1))),

                Regex.create("WLAN-Gerät hat sich abgemeldet " + WIFI_TYPE_REGEXP + "\\. MAC-Adresse: "
                        + MAC_ADDRESS_REGEXP + ", Name: " +
                        EVERYTHING_UNTIL_PERIOD_REGEXP + "\\.", 3,
                        groups -> new WifiDeviceDisconnected(WifiType.parse(groups.get(0)), groups.get(1),
                                groups.get(2))));
    }
}
