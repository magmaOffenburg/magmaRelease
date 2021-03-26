/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hso.autonomy.util.misc.FuzzyCompare;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author kdorer
 */
public class SphereTest
{
	private Sphere testee;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		testee = new Sphere(new Vector3D(1, -2, -3), Math.sqrt(14));
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.geometry.Sphere#intersect(hso.autonomy.util.geometry.Circle3D)}
	 * .
	 */
	@Test
	public void testIntersectTwoPointSameXZ()
	{
		testee = new Sphere(new Vector3D(0, -1, 0), 1);
		Circle3D circle = new Circle3D(new Vector3D(0, 0, 0), 1);
		Vector3D[] intersect = testee.intersect(circle);

		double cos = Math.cos(Math.toRadians(60));
		double sin = Math.sin(Math.toRadians(60));

		Vector3D expected = new Vector3D(0, -cos, sin);
		testTrue(expected, intersect[0]);
		expected = new Vector3D(0, -cos, -sin);
		testTrue(expected, intersect[1]);
	}

	@Test
	public void testIntersectOrigin()
	{
		Circle3D circle = new Circle3D(new Vector3D(0, 1, 0), 1);
		Vector3D[] intersect = testee.intersect(circle);

		Vector3D expected = Vector3D.ZERO;
		testTrue(expected, intersect[0]);
		expected = new Vector3D(0, 1, -1);
		testTrue(expected, intersect[1]);
	}

	@Test
	public void testIntersectOnePoint()
	{
		testee = new Sphere(new Vector3D(2, 2, 2), Math.sqrt(12));
		Circle3D circle = new Circle3D(new Vector3D(0, -1, -1), Math.sqrt(2));
		Vector3D[] intersect = testee.intersect(circle);

		Vector3D expected = Vector3D.ZERO;
		testTrue(expected, intersect[0]);
		expected = Vector3D.ZERO;
		testTrue(expected, intersect[1]);
	}

	@Test
	public void testNoIntersect()
	{
		Circle3D circle = new Circle3D(new Vector3D(0, 3, 0), 1);
		Vector3D[] intersect = testee.intersect(circle);
		assertEquals(0, intersect.length);
	}

	protected void testTrue(Vector3D expected, Vector3D intersect)
	{
		assertTrue(FuzzyCompare.eq(expected, intersect, 0.0001), "Not equal: " + expected + "/" + intersect);
	}
}
