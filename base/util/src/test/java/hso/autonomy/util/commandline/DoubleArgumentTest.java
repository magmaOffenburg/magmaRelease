/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.commandline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DoubleArgumentTest
{
	private DoubleArgument testee;

	@BeforeEach
	public void setUp()
	{
		testee = new DoubleArgument("argument", 5.0, 3, 10, null);
	}

	@Test
	public void testParseAllowedValue()
	{
		testParse(3.0, "--argument=3");
		testParse(7.0, "--argument=7");
		testParse(10.0, "--argument=10");
	}

	@Test
	public void testParseValueTooSmall()
	{
		assertThrows(ArgumentParsingException.class, () -> {
			testee.parse("--argument=2");
			testee.parse("--argument=-30");
		});
	}

	@Test
	public void testParseValueTooBig()
	{
		assertThrows(ArgumentParsingException.class, () -> {
			testParse(5.0, "--argument=11");
			testParse(5.0, "--argument=100");
		});
	}

	@Test
	public void testParseEmptyValue()
	{
		assertThrows(ArgumentParsingException.class, () -> { testParse(5.0, "--argument="); });
	}

	@Test
	public void testParseNonInteger()
	{
		assertThrows(ArgumentParsingException.class, () -> { testParse(5.0, "--argument=notInt"); });
	}

	@Test
	public void testConstructorNoBounds()
	{
		testee = new DoubleArgument("argument", 5.0, null);
		testParse(Double.NEGATIVE_INFINITY, "--argument=" + Double.NEGATIVE_INFINITY);
		testParse(Double.POSITIVE_INFINITY, "--argument=" + Double.POSITIVE_INFINITY);
	}

	@Test
	public void testConstructorNoMaxValue()
	{
		testee = new DoubleArgument("argument", 5.0, 3, null);
		testParse(Double.POSITIVE_INFINITY, "--argument=" + Double.POSITIVE_INFINITY);
	}

	private void testParse(Double expected, String... args)
	{
		assertEquals(expected, testee.parse(args));
	}
}
