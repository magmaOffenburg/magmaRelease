/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.commandline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IntegerArgumentTest
{
	private IntegerArgument testee;

	@BeforeEach
	public void setUp()
	{
		testee = new IntegerArgument("argument", 5, 3, 10, null);
	}

	@Test
	public void testParseAllowedValue()
	{
		testParse(3, "--argument=3");
		testParse(7, "--argument=7");
		testParse(10, "--argument=10");
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
			testParse(5, "--argument=11");
			testParse(5, "--argument=100");
		});
	}

	@Test
	public void testParseEmptyValue()
	{
		assertThrows(ArgumentParsingException.class, () -> { testParse(5, "--argument="); });
	}

	@Test
	public void testParseNonInteger()
	{
		assertThrows(ArgumentParsingException.class, () -> { testParse(5, "--argument=notInt"); });
	}

	@Test
	public void testConstructorNoBounds()
	{
		testee = new IntegerArgument("argument", 5, null);
		testParse(Integer.MIN_VALUE, "--argument=" + Integer.MIN_VALUE);
		testParse(Integer.MAX_VALUE, "--argument=" + Integer.MAX_VALUE);
	}

	@Test
	public void testConstructorNoMaxValue()
	{
		testee = new IntegerArgument("argument", 5, 3, null);
		testParse(Integer.MAX_VALUE, "--argument=" + Integer.MAX_VALUE);
	}

	private void testParse(Integer expected, String... args)
	{
		assertEquals(expected, testee.parse(args));
	}
}
