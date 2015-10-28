package org.chris.fritzbox.reports.mail.model;

import static org.junit.Assert.assertEquals;

import org.chris.fritzbox.reports.mail.model.DataVolume.Unit;
import org.junit.Test;

public class TestDataVolume {

	@Test(expected = IllegalArgumentException.class)
	public void testParseWrongUnit() {
		DataVolume.parse("12GB");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseNoInteger() {
		DataVolume.parse("12.3kB");
	}

	@Test
	public void testParse() {
		assertParse("12kB", 12, Unit.KB);
		assertParse(" 12kB ", 12, Unit.KB);
		assertParse("12MB", 12, Unit.MB);
	}

	private void assertParse(final String string, final int i, final Unit unit) {
		final DataVolume volume = DataVolume.parse(string);
		assertEquals(i, volume.getVolume());
		assertEquals(unit, volume.getUnit());
	}
}
