/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.misc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import org.junit.jupiter.api.Test;

public class DurationUtilTest
{
	@Test
	public void testFormat()
	{
		testFormat("01:40:00", Duration.ofMinutes(100));
		testFormat("1000:00:00", Duration.ofHours(1000));
	}

	private void testFormat(String expected, Duration duration)
	{
		assertEquals(expected, DurationUtil.format(duration));
	}

	@Test
	public void testParse()
	{
		testParse(Duration.ofMinutes(100), "01:40:00");
		testParse(Duration.ofHours(1000), "1000:00:00");
	}

	private void testParse(Duration expected, String toParse)
	{
		assertEquals(expected.getSeconds(), DurationUtil.parse(toParse).getSeconds());
	}
}
