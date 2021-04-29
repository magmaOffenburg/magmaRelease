/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.commandline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StringArgumentTest
{
	private StringArgument testee;

	@BeforeEach
	public void setUp()
	{
		testee = new StringArgument("argument", "defaultValue", null);
	}

	@Test
	public void testParseCorrectArgument()
	{
		testParse("value", "--argument=value");
	}

	@Test
	public void testParseIncorrectArgument()
	{
		testParse("defaultValue", "-argument=value");
		testParse("defaultValue", "--arg=value");
	}

	@Test
	public void testParseMultipleArgs()
	{
		testParse("value", "--argument=value", "--argument2=value", "--argument3=value");
	}

	@Test
	public void testParseNull()
	{
		testParse("defaultValue", (String[]) null);
	}

	@Test
	public void testParseNullDefaultValue()
	{
		testee = new StringArgument("argument", null, null);
		testParse(null, "--invalid=invalid");
	}

	@Test
	public void testParseUseFirstValue()
	{
		testParse("value1", "--argument=value1", "--argument=value2", "--argument=value3");
	}

	@Test
	public void testParseRequiredArgument()
	{
		testee.setRequired(true);
		assertThrows(ArgumentParsingException.class, () -> { testParse("", ""); });
	}

	@Test
	public void testParseOptionalArgument()
	{
		testee.setRequired(false);
		testParse("defaultValue", "");
	}

	private void testParse(String expected, String... args)
	{
		assertEquals(expected, testee.parse(args));
	}
}
