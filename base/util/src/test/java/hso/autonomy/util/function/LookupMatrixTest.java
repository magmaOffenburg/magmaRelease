/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author kdorer
 */
public class LookupMatrixTest
{
	private LookupMatrix testee;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		float[][] matrix = {{0, 1}, {2, 3}};
		testee = new LookupMatrix(matrix, 1, 1, 0, 0);
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.function.LookupMatrix#getValue(double, double)}.
	 */
	@Test
	public void testGetValue()
	{
		assertEquals(0, testee.getValue(0, 0), 0.0001);
		assertEquals(1, testee.getValue(0, 1), 0.0001);
		assertEquals(2, testee.getValue(1, 0), 0.0001);
		assertEquals(3, testee.getValue(1, 1), 0.0001);
		assertEquals(0.5, testee.getValue(0, 0.5), 0.0001);
		assertEquals(1.0, testee.getValue(0.5, 0), 0.0001);
		assertEquals(1.5, testee.getValue(0.5, 0.5), 0.0001);
		assertEquals(0, testee.getValue(-1, 0), 0.0001);
		assertEquals(1, testee.getValue(-1, 1), 0.0001);
		assertEquals(2, testee.getValue(1, -1), 0.0001);
	}

	// @Test
	// @Ignore
	// public void testReadFromFile()
	// {
	// testee = new LookupMatrix("motorMappings/JointToMotorLookupFootM2.csv");
	// // assertEquals(-89.368, testee.getValue(-17, -20), 0.0001);
	//
	// // test difference to analytical method
	// IJointToMotorMapper mapper = new SweatyFootJointToMotorMapper();
	//
	// for (double i = -15; i < 15; i += 0.1) {
	// for (double j = -15; j < 15; j += 0.1) {
	// double[] jointAngles = { i, j };
	// double value1 = mapper.jointToMotorAngle(jointAngles)[1];
	// double value2 = testee.getValue(i, j);
	// double delta = value2 - value1;
	// if (delta > 1.0) {
	// System.out.println("Delta: " + delta + " x:" + i + " y:" + j);
	// }
	// }
	// }
	// }

	@Test
	@Disabled
	public void testRuntime() throws FileNotFoundException
	{
		testee = new LookupMatrix("motorMappings/JointToMotorLookupFootM2.csv");
		long start = System.currentTimeMillis();
		for (double i = -15; i < 15; i += 0.01) {
			for (double j = -15; j < 15; j += 0.01) {
				testee.getValue(i, j);
			}
		}
		System.out.println("runtime: " + ((System.currentTimeMillis() - start) * 2));
	}
}
