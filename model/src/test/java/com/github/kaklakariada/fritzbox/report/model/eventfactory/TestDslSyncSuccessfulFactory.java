/**
 * A Java API for parsing and processing status report mails of a FritzBox
 * Copyright (C) 2018 Christoph Pirkl <christoph at users.sourceforge.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General  License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General  License for more details.
 *
 * You should have received a copy of the GNU General  License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.kaklakariada.fritzbox.report.model.eventfactory;

import org.junit.jupiter.api.Test;

import com.github.kaklakariada.fritzbox.report.model.event.DslSyncSuccessful;

class TestDslSyncSuccessfulFactory extends EventLogEntryFactoryTestBase<DslSyncSuccessful> {

    @Test
    void testFailureTrailingWhitespace() {
        assertMatchFailed("DSL ist verfügbar (DSL-Synchronisierung besteht mit 46658/10048 kbit/s). ");
    }

    @Test
    void testFailureMissingTrailingPoint() {
        assertMatchFailed("DSL ist verfügbar (DSL-Synchronisierung besteht mit 46658/10048 kbit/s)");
    }

    @Test
    void testSuccess() {
        assertMatched("DSL ist verfügbar (DSL-Synchronisierung besteht mit 46658/10048 kbit/s).",
                new DslSyncSuccessful(46658, 10048));
    }

    @Override
    protected AbstractEventLogEntryFactory<DslSyncSuccessful> createFactory() {
        return new DslSyncSuccessfulFactory();
    }
}
