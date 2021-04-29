/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.commandline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BooleanArgumentTest
{
	private BooleanArgument testee;

	@BeforeEach
	public void setUp()
	{
		testee = new BooleanArgument("flag", null);
	}

	@Test
	public void testParseIsPresent()
	{
		testParse(true, "--flag");
	}

	@Test
	public void testParseIsNotPresent()
	{
		testParse(false, "--flags");
		testParse(false, "--flag=value");
	}

	private void testParse(Boolean expected, String... args)
	{
		assertEquals(expected, testee.parse(args));
	}
}
