/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.misc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 *
 * @author kdorer
 */
public class ValueUtilTest
{
	@Test
	public void testLimitAbsFloatFloat()
	{
		assertEquals(9, ValueUtil.limitAbs(10, 9), 0.001);
		assertEquals(-9, ValueUtil.limitAbs(-10, 9), 0.001);
		assertEquals(8, ValueUtil.limitAbs(8, 9), 0.001);
	}

	@Test
	public void testGetValueAdjustment()
	{
		assertEquals(1, ValueUtil.getValueAdjustment(5, 7, 1, 2), 0.001);
		assertEquals(-2, ValueUtil.getValueAdjustment(5, 3, 1, 2), 0.001);
		assertEquals(0.5, ValueUtil.getValueAdjustment(5, 5.5, 1, 2), 0.001);
		assertEquals(-0.5, ValueUtil.getValueAdjustment(5, 4.5, 1, 2), 0.001);
	}

	@Test
	public void testGetScale()
	{
		double[] values = {1, -2, 3};
		assertEquals(1, ValueUtil.getScale(values, 7), 0.001);
		assertEquals(0.5, ValueUtil.getScale(values, 3), 0.001);
	}

	@Test
	public void testRemap()
	{
		assertEquals(-0.4, ValueUtil.remap(3, 0, 10, -1, 1), 0.001);
		assertEquals(0, ValueUtil.remap(0, 0, 0, 0, 0), 0.001);
	}

	@Test
	public void testClip()
	{
		float[] values = {-2.0f, 0, -1.0f, 1.0f, 2.0f, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, -0.1f, 0.1f};
		float[] expected = {-1.0f, 0, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -0.1f, 0.1f};
		double[] result = ValueUtil.clip(values, -1.0f, 1.0f);
		for (int i = 0; i < result.length; i++) {
			assertEquals(expected[i], result[i], 0.00001);
		}
	}
}
