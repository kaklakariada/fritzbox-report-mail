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
