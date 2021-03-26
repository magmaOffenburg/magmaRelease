/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Klaus Dorer
 *
 */
public class PiecewiseLinearFunctionTest
{
	private PiecewiseLinearFunction testee;

	/**
	 * P0: &nbsp; 0, &nbsp;-2<br>
	 * P1: &nbsp; 4, &nbsp; 2<br>
	 * P2: &nbsp; 5, &nbsp; 2<br>
	 * P3: &nbsp; 6, &nbsp; 0
	 */
	private double[] supportPoints;

	@BeforeEach
	public void setUp()
	{
		supportPoints = new double[] {//
				0, -2,				  //
				4, 2,				  //
				5, 2,				  //
				6, 0};
		testee = new PiecewiseLinearFunction(SupportPoint.fromArray(supportPoints), 1);
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.function.PiecewiseLinearFunction#value(double)} .
	 */
	@Test
	public void testValueValid()
	{
		assertEquals(-2, testee.value(0), 0.0001);
		assertEquals(0, testee.value(6), 0.0001);
		assertEquals(1, testee.value(5.5), 0.0001);
		assertEquals(2, testee.value(4.5), 0.0001);
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.function.PiecewiseLinearFunction#getName()} .
	 */
	@Test
	public void testGetName()
	{
		assertEquals(PiecewiseLinearFunction.NAME, testee.getName());
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.function.PiecewiseLinearFunction#addPoint(double,
	 * double)}
	 * .
	 */
	@Test
	public void testAddPointFirst()
	{
		testee.addSupportPoint(-1, 7);
		assertEquals(5, testee.getSupportPointSize());
		assertEquals(-1, testee.getSupportPoint(0).x, 0.0001);
		assertEquals(7, testee.getSupportPoint(0).y, 0.0001);
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.function.PiecewiseLinearFunction#addPoint(double,
	 * double)}
	 * .
	 */
	@Test
	public void testAddPointMiddle()
	{
		testee.addSupportPoint(4.5f, 7f);
		assertEquals(5, testee.getSupportPointSize());
		assertEquals(4.5, testee.getSupportPoint(2).x, 0.0001);
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.function.PiecewiseLinearFunction#addPoint(double,
	 * double)}
	 * .
	 */
	@Test
	public void testAddPointLast()
	{
		testee.addSupportPoint(8, 7);
		assertEquals(5, testee.getSupportPointSize());
		assertEquals(8, testee.getSupportPoint(4).x, 0.0001);
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.function.PiecewiseLinearFunction#removePoint(int)} .
	 */
	@Test
	public void testHasSupportPointInArea()
	{
		assertFalse(testee.hasSupportPointInArea(1.0, 2.0));
		assertTrue(testee.hasSupportPointInArea(3.9, 4.1));
		assertTrue(testee.hasSupportPointInArea(3.0, 7));
		assertTrue(testee.hasSupportPointInArea(-1.0, 1.0));
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.function.PiecewiseLinearFunction#removePoint(int)} .
	 */
	@Test
	public void testRemovePointFirst()
	{
		testee.removeSupportPoint(0);
		assertEquals(3, testee.getSupportPointSize(), 0.0001);
		assertEquals(4, testee.getSupportPoint(0).x, 0.0001);
		assertEquals(2, testee.getSupportPoint(0).y, 0.0001);
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.function.PiecewiseLinearFunction#removePoint(int)} .
	 */
	@Test
	public void testRemovePointMiddle()
	{
		testee.removeSupportPoint(2);
		assertEquals(3, testee.getSupportPointSize(), 0.0001);
		assertEquals(6, testee.getSupportPoint(2).x, 0.0001);
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.function.PiecewiseLinearFunction#removePoint(int)} .
	 */
	@Test
	public void testRemovePointLast()
	{
		testee.removeSupportPoint(3);
		assertEquals(3, testee.getSupportPointSize(), 0.0001);
		assertEquals(5, testee.getSupportPoint(2).x, 0.0001);
	}

	/**
	 * Test method for
	 * {@link
	 * hso.autonomy.util.function.PiecewiseLinearFunction#changePointCoordinates(int,
	 * double, double)}
	 */
	@Test
	public void testChangePointCoordinates()
	{
		// Test Move P1 from (4, 2) to (3, -2.5)
		// Order: --> no change expected
		Vector2D old = testee.moveSupportPointTo(1, 3.0f, -2.5f);

		assertEquals(old.getX(), 4.0, 0.00001);
		assertEquals(old.getY(), 2.0, 0.00001);

		assertEquals(testee.getSupportPoint(0).x, supportPoints[0], 0.000001);
		assertEquals(testee.getSupportPoint(0).y, supportPoints[1], 0.000001);
		assertEquals(testee.getSupportPoint(1).x, 3.0, 0.000001);
		assertEquals(testee.getSupportPoint(1).y, -2.5, 0.000001);
		assertEquals(testee.getSupportPoint(2).x, supportPoints[4], 0.000001);
		assertEquals(testee.getSupportPoint(2).y, supportPoints[5], 0.000001);
		assertEquals(testee.getSupportPoint(3).x, supportPoints[6], 0.000001);
		assertEquals(testee.getSupportPoint(3).y, supportPoints[7], 0.000001);
	}

	/**
	 * Test method for
	 * {@link
	 * hso.autonomy.util.function.PiecewiseLinearFunction#changePointCoordinates(int,
	 * double, double)}
	 */
	@Test
	public void testChangePointCoordinatesLessThanPrevious()
	{
		// Test Move P2 from (5, 2) to (3, -2.5)
		// Order: --> P1 and P2 should exchange
		Vector2D old = testee.moveSupportPointTo(2, 3.0f, -2.5f);

		assertEquals(old.getX(), 5.0, 0.00001);
		assertEquals(old.getY(), 2.0, 0.00001);

		assertEquals(testee.getSupportPoint(0).x, supportPoints[0], 0.000001);
		assertEquals(testee.getSupportPoint(0).y, supportPoints[1], 0.000001);
		assertEquals(testee.getSupportPoint(1).x, 3.0, 0.000001);
		assertEquals(testee.getSupportPoint(1).y, -2.5, 0.000001);
		assertEquals(testee.getSupportPoint(2).x, supportPoints[2], 0.000001);
		assertEquals(testee.getSupportPoint(2).y, supportPoints[3], 0.000001);
		assertEquals(testee.getSupportPoint(3).x, supportPoints[6], 0.000001);
		assertEquals(testee.getSupportPoint(3).y, supportPoints[7], 0.000001);
	}

	/**
	 * Test method for
	 * {@link
	 * hso.autonomy.util.function.PiecewiseLinearFunction#changePointCoordinates(int,
	 * double, double)}
	 */
	@Test
	public void testChangePointCoordinatesMoreThanNext()
	{
		// Test Move P2 from (5, 2) to (7, 2.5)
		// Order: --> P3 and P2 should exchange
		Vector2D old = testee.moveSupportPointTo(2, 7.0f, 2.5f);

		assertEquals(old.getX(), 5.0, 0.00001);
		assertEquals(old.getY(), 2.0, 0.00001);

		assertEquals(testee.getSupportPoint(0).x, supportPoints[0], 0.000001);
		assertEquals(testee.getSupportPoint(0).y, supportPoints[1], 0.000001);
		assertEquals(testee.getSupportPoint(1).x, supportPoints[2], 0.000001);
		assertEquals(testee.getSupportPoint(1).y, supportPoints[3], 0.000001);
		assertEquals(testee.getSupportPoint(2).x, supportPoints[6], 0.000001);
		assertEquals(testee.getSupportPoint(2).y, supportPoints[7], 0.000001);
		assertEquals(testee.getSupportPoint(3).x, 7.0f, 0.000001);
		assertEquals(testee.getSupportPoint(3).y, 2.5f, 0.000001);
	}

	/**
	 * Test method for {@link hso.autonomy.util.function.PiecewiseLinearFunction#copy()}
	 */
	@Test
	public void testCopy()
	{
		PiecewiseLinearFunction copy = (PiecewiseLinearFunction) testee.copy();

		assertEquals(testee.getMaxX(), copy.getMaxX(), 0.0000001);
		assertEquals(testee.getMinX(), copy.getMinX(), 0.0000001);
		assertEquals(testee.getMaxY(), copy.getMaxY(), 0.0000001);
		assertEquals(testee.getMinY(), copy.getMinY(), 0.0000001);
		assertEquals(testee.getName(), copy.getName());
		assertEquals(testee.getSupportPointSize(), copy.getSupportPointSize(), 0.0000001);

		SupportPoint sp1, sp2;
		for (int i = 0; i < testee.getSupportPointSize(); i++) {
			sp1 = testee.getSupportPoint(i);
			sp2 = copy.getSupportPoint(i);
			if (sp1 == sp2) {
				fail("Copy of function is not deep!");
			}

			assertEquals(sp1.x, sp2.x, 0.0000001);
			assertEquals(sp1.y, sp2.y, 0.0000001);
		}
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.function.PiecewiseLinearFunction#toCSVString()}
	 */
	@Test
	public void testToCSVString()
	{
		assertEquals("linear,4,0,0.0,-2.0,4.0,2.0,5.0,2.0,6.0,0.0", testee.toCSVString());
	}

	@Test
	public void testSimplify()
	{
		double[] xValues = new double[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		double[] yValues = new double[] {0, 0, 2, 2, 2, 2, 2, 4, 4, 4};

		double[] expectedX = new double[] {0, 1, 2, 6, 7, 9};
		double[] expectedY = new double[] {0, 0, 2, 2, 4, 4};

		testSimplify(xValues, yValues, expectedX, expectedY);
	}

	@Test
	public void testSimplifyMinSize()
	{
		double[] xValues = new double[] {0, 1, 2};
		double[] yValues = new double[] {1, 1, 1};

		double[] expectedX = new double[] {0, 2};
		double[] expectedY = new double[] {1, 1};

		testSimplify(xValues, yValues, expectedX, expectedY);
	}

	@Test
	public void testSimplifyAlreadySimplified()
	{
		double[] xValues = new double[] {0, 1, 2, 3, 4, 5};
		double[] yValues = new double[] {1, 2, 1, 1, 3, 3};

		testSimplify(xValues, yValues, xValues, yValues);
	}

	private void testSimplify(double[] xValues, double[] yValues, double[] expectedX, double[] expectedY)
	{
		assertEquals(xValues.length, yValues.length);
		assertEquals(expectedX.length, expectedY.length);

		testee = new PiecewiseLinearFunction(SupportPoint.fromArrays(xValues, yValues), 1);
		testee.simplify();

		assertEquals(expectedX.length, testee.getSupportPointSize());

		for (int i = 0; i < testee.getSupportPointSize(); i++) {
			assertEquals(expectedX[i], testee.getSupportPoint(i).x, 0.0000001);
			assertEquals(expectedY[i], testee.getSupportPoint(i).y, 0.0000001);
		}
	}
}
