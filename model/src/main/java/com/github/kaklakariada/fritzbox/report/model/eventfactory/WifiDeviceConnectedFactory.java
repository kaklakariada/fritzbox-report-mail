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

import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceConnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiType;
import com.github.kaklakariada.fritzbox.report.model.regex.Regex;

public class WifiDeviceConnectedFactory extends AbstractEventLogEntryFactory<WifiDeviceConnected> {

        private static final String DOT_MAC_ADDRESS = "\\. MAC-Adresse: ";
        private static final String COMMA_MAC = ", MAC ";
        private static final String COMMA_IP = ", IP ";
        private static final String NEW_WIFI_DEVICE = "Neues WLAN-Gerät erstmalig angemeldet ";
        private static final String COMMA_NAME_COLON = ", Name: ";

        WifiDeviceConnectedFactory() {
                super(Regex.create(
                                "(?:Neues )?WLAN-Gerät (?:erstmalig )?(?:hat sich neu )?angemeldet\\. Geschwindigkeit "
                                                + EVERYTHING_UNTIL_PERIOD_REGEXP + ". MAC-Adresse: "
                                                + MAC_ADDRESS_REGEXP + COMMA_NAME_COLON
                                                + EVERYTHING_UNTIL_PERIOD_REGEXP + "\\.",
                                3,
                                groups -> new WifiDeviceConnected(groups.get(0), null, null, groups.get(1),
                                                groups.get(2))),

                                Regex.create(
                                                NEW_WIFI_DEVICE + WIFI_TYPE_REGEXP + ", "
                                                                + EVERYTHING_UNTIL_COMMA_REGEXP +
                                                                ", " + EVERYTHING_UNTIL_COMMA_REGEXP + COMMA_IP
                                                                + IPV4_ADDRESS_REGEXP + COMMA_MAC
                                                                + MAC_ADDRESS_REGEXP + "\\.",
                                                5,
                                                groups -> new WifiDeviceConnected(groups.get(1),
                                                                WifiType.parse(groups.get(0)), groups.get(3),
                                                                groups.get(4), groups.get(2))),

                                Regex.create(
                                                NEW_WIFI_DEVICE + WIFI_TYPE_REGEXP
                                                                + "\\. Geschwindigkeit "
                                                                + EVERYTHING_UNTIL_PERIOD_REGEXP + DOT_MAC_ADDRESS
                                                                + MAC_ADDRESS_REGEXP
                                                                + COMMA_NAME_COLON + EVERYTHING_UNTIL_PERIOD_REGEXP
                                                                + "\\.",
                                                4,
                                                groups -> new WifiDeviceConnected(groups.get(1),
                                                                WifiType.parse(groups.get(0)), null,
                                                                groups.get(2), groups.get(3))),

                                Regex.create(
                                                "WLAN-Gerät hat sich neu angemeldet " + WIFI_TYPE_REGEXP
                                                                + "\\. Geschwindigkeit "
                                                                + EVERYTHING_UNTIL_PERIOD_REGEXP
                                                                + DOT_MAC_ADDRESS + MAC_ADDRESS_REGEXP
                                                                + COMMA_NAME_COLON + EVERYTHING_UNTIL_PERIOD_REGEXP
                                                                + "\\.",
                                                4,
                                                groups -> new WifiDeviceConnected(groups.get(1),
                                                                WifiType.parse(groups.get(0)), null,
                                                                groups.get(2), groups.get(3))),

                                Regex.create(
                                                "WLAN-Gerät (?:hat sich neu )?angemeldet " + WIFI_TYPE_REGEXP + ", "
                                                                + EVERYTHING_UNTIL_COMMA_REGEXP + ", "
                                                                + EVERYTHING_UNTIL_COMMA_REGEXP + COMMA_IP
                                                                + IPV4_ADDRESS_REGEXP + COMMA_MAC
                                                                + MAC_ADDRESS_REGEXP + "(?:, Name: .+)?\\.",
                                                5,
                                                groups -> new WifiDeviceConnected(groups.get(1),
                                                                WifiType.parse(groups.get(0)), groups.get(3),
                                                                groups.get(4), groups.get(2))),

                                Regex.create(
                                                NEW_WIFI_DEVICE + WIFI_TYPE_REGEXP + ", "
                                                                + EVERYTHING_UNTIL_COMMA_REGEXP + ", "
                                                                + EVERYTHING_UNTIL_COMMA_REGEXP + COMMA_IP
                                                                + IPV4_ADDRESS_REGEXP + COMMA_MAC + MAC_ADDRESS_REGEXP
                                                                + COMMA_NAME_COLON
                                                                + EVERYTHING_UNTIL_PERIOD_REGEXP + "\\.",
                                                6,
                                                groups -> new WifiDeviceConnected(groups.get(1),
                                                                WifiType.parse(groups.get(0)), groups.get(3),
                                                                groups.get(4), groups.get(2))),

                                Regex.create(
                                                "WLAN-Gerät angemeldet " + WIFI_TYPE_REGEXP + ". Geschwindigkeit "
                                                                + EVERYTHING_UNTIL_PERIOD_REGEXP
                                                                + DOT_MAC_ADDRESS + MAC_ADDRESS_REGEXP
                                                                + COMMA_NAME_COLON + EVERYTHING_UNTIL_PERIOD_REGEXP
                                                                + "\\.",
                                                4,
                                                groups -> new WifiDeviceConnected(groups.get(1),
                                                                WifiType.parse(groups.get(0)), null,
                                                                groups.get(2),
                                                                groups.get(3))));
        }
}
