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
package com.github.kaklakariada.fritzbox.report.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.kaklakariada.fritzbox.report.model.DataVolume.Unit;

import org.junit.jupiter.api.Test;

class TestDataVolume {

    void testParseUnitGb() {
        assertParse("12GB", 12, Unit.GB);
    }

    @Test
    void testParseNoInteger() {
        assertParse("12.3kB", 12.3, Unit.KB);
    }

    @Test
    void testParseUppercaseKB() {
        assertParse("12.3KB", 12.3, Unit.KB);
    }

    @Test
    void testParse() {
        assertParse("12kB", 12, Unit.KB);
        assertParse(" 12kB ", 12, Unit.KB);
        assertParse("12MB", 12, Unit.MB);
    }

    private void assertParse(final String string, final double expected, final Unit unit) {
        final DataVolume volume = DataVolume.parse(string);
        assertEquals(expected, volume.getVolume(), 0.00001);
        assertEquals(unit, volume.getUnit());
    }
}
