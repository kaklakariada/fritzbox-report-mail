/**
 * A Java API for parsing and processing status report mails of a FritzBox
 * Copyright (C) 2015 Christoph Pirkl <christoph at users.sourceforge.net>
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

import com.github.kaklakariada.fritzbox.report.model.event.InternetDisconnected;

public class TestInternetDisconnectedFactory extends EventLogEntryFactoryTestBase<InternetDisconnected> {

    @Test
    public void testFailureTrailingWhitespace() {
        assertMatchFailed("Internetverbindung wurde getrennt. ");
    }

    @Test
    public void testFailureMissingTrailingPoint() {
        assertMatchFailed("Internetverbindung wurde getrennt");
    }

    @Test
    public void testSuccess() {
        assertMatched("Internetverbindung wurde getrennt.", new InternetDisconnected());
    }

    @Override
    protected AbstractEventLogEntryFactory<InternetDisconnected> createFactory() {
        return new InternetDisconnectedFactory();
    }
}
