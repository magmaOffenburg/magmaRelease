/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.commandline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class EnumArgumentTest
{
	private enum TestEnum { VALUE, OTHER_VALUE }

	@Test
	public void testValidValues()
	{
		EnumArgument<TestEnum> arg = new EnumArgument<>("test", TestEnum.VALUE, "", TestEnum.class);

		assertEquals(arg.parse(""), TestEnum.VALUE);
		assertEquals(arg.parse("--test=value"), TestEnum.VALUE);
		assertEquals(arg.parse("--test=otherValue"), TestEnum.OTHER_VALUE);
	}

	@Test
	public void testNullDefault()
	{
		EnumArgument<TestEnum> arg = new EnumArgument<>("test", null, "", TestEnum.class);
		assertNull(arg.parse(""));
	}

	@Test
	public void testInvalidValue()
	{
		EnumArgument<TestEnum> arg = new EnumArgument<>("test", null, "", TestEnum.class);
		assertThrows(ArgumentParsingException.class, () -> { assertNull(arg.parse("--test=invalid")); });
	}
}
