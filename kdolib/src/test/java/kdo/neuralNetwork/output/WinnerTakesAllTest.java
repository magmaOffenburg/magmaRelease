/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class WinnerTakesAllTest
{
	@Test
	public void testOutput()
	{
		float[] output = {1, 2, 0.5f};
		WinnerTakesAll testee = new WinnerTakesAll(false, 0);
		float[] result = testee.output(output);
		assertEquals(0, result[0], 0.01);
		assertEquals(2, result[1], 0.01);
		assertEquals(0, result[2], 0.01);
	}

	@Test
	public void testOutputReplace()
	{
		float[] output = {1, 2, 3.5f};
		WinnerTakesAll testee = new WinnerTakesAll(true, 1);
		float[] result = testee.output(output);
		assertEquals(0, result[0], 0.01);
		assertEquals(0, result[1], 0.01);
		assertEquals(1, result[2], 0.01);
	}
}
