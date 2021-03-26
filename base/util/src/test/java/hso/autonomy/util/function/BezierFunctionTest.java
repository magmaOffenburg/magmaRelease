/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author kdorer
 */
public class BezierFunctionTest
{
	private BezierFunction testee;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		Vector3D p0 = new Vector3D(0, 0, 0);
		Vector3D p1 = new Vector3D(1, 1, 0);
		Vector3D p2 = new Vector3D(2, 1, 0);
		Vector3D p3 = new Vector3D(3, 0, 0);
		testee = new BezierFunction(p0, p1, p2, p3);
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.function.BezierFunction#calculateBezierPoint(float)}.
	 */
	@Test
	public void testCalculateBezierPoint()
	{
		Vector3D result = testee.calculateBezierPoint(0.0f);
		assertEquals(0.0f, result.getX(), 0.0001);
		assertEquals(0.0f, result.getY(), 0.0001);

		result = testee.calculateBezierPoint(0.5f);
		assertEquals(1.5f, result.getX(), 0.0001);
		assertEquals(0.75f, result.getY(), 0.0001);

		result = testee.calculateBezierPoint(1.0f);
		assertEquals(3.0f, result.getX(), 0.0001);
		assertEquals(0.0f, result.getY(), 0.0001);
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.function.BezierFunction#calculateBezierPoint(float)}.
	 */
	@Test
	public void testCalculateBezierPointReal3D()
	{
		Vector3D p0 = new Vector3D(0, 0, 0);
		Vector3D p1 = new Vector3D(1, 1, 2);
		Vector3D p2 = new Vector3D(2, 1, 2);
		Vector3D p3 = new Vector3D(3, 0, 0);
		testee = new BezierFunction(p0, p1, p2, p3);

		Vector3D result = testee.calculateBezierPoint(0.5f);
		assertEquals(1.5f, result.getX(), 0.0001);
		assertEquals(0.75f, result.getY(), 0.0001);
		assertEquals(1.5f, result.getZ(), 0.0001);
	}
}
