/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author kdorer
 */
public class LookupMatrix3DTest
{
	private LookupMatrix3D testee;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		float[][][] matrix = {{{0, 1}, {2, 3}}, {{4, 5}, {6, 7}}};
		testee = new LookupMatrix3D(matrix, 1, 1, 1, 0, 0, 0);
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.function.LookupMatrix#getValue(double, double)}.
	 */
	@Test
	public void testGetValue()
	{
		assertEquals(0, testee.getValue(0, 0, 0), 0.0001);
		assertEquals(2, testee.getValue(0, 1, 0), 0.0001);
		assertEquals(4, testee.getValue(1, 0, 0), 0.0001);
		assertEquals(6, testee.getValue(1, 1, 0), 0.0001);
		assertEquals(7, testee.getValue(1, 1, 1), 0.0001);
		assertEquals(0.5, testee.getValue(0, 0, 0.5), 0.0001);
		assertEquals(6.8, testee.getValue(1, 1, 0.8), 0.0001);
		assertEquals(0, testee.getValue(0, 0, -1), 0.0001);
		assertEquals(1, testee.getValue(0, 0, 2), 0.0001);
	}

	@Test
	@Disabled
	public void testReadFromFile()
	{
		// testee = new
		// LookupMatrix3D("motorMappings/JointToMotorLookupFootM2.csv");
		// assertEquals(-89.368, testee.getValue(-17, -20), 0.0001);
	}
}
