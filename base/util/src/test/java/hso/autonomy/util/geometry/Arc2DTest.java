/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author sgrossma
 */
public class Arc2DTest
{
	private Arc2D testee;

	@BeforeEach
	public void setup()
	{
		testee = new Arc2D(0, 0, 1, Angle.deg(-5), Angle.deg(80));
	}

	@Test
	public void testGetLineIntersectionPoint()
	{
		assertEquals(0, testee.getLineSegmentIntersectionPoint(new Vector2D(-1, 1), new Vector2D(1, 1)).size());

		List<Vector2D> result = testee.getLineSegmentIntersectionPoint(new Vector2D(0, 0), new Vector2D(1, 0));
		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getX(), 0.0001);
		assertEquals(0, result.get(0).getY(), 0.0001);

		result = testee.getLineSegmentIntersectionPoint(new Vector2D(-1, -1), new Vector2D(1, 1));
		assertEquals(1, result.size());
		assertEquals(0.7071, result.get(0).getX(), 0.001);
		assertEquals(0.7071, result.get(0).getY(), 0.0001);

		testee = new Arc2D(0, 0, 1, Angle.deg(175), Angle.deg(-5));
		result = testee.getLineSegmentIntersectionPoint(new Vector2D(-1, -1), new Vector2D(1, 1));
		assertEquals(1, result.size());
		assertEquals(-0.7071, result.get(0).getX(), 0.001);
		assertEquals(-0.7071, result.get(0).getY(), 0.0001);

		testee = new Arc2D(0, 0, 1, Angle.deg(90), Angle.deg(-5));
		result = testee.getLineSegmentIntersectionPoint(new Vector2D(-1, 1), new Vector2D(1, -1));
		assertEquals(2, result.size());
		assertEquals(-0.7071, result.get(0).getX(), 0.001);
		assertEquals(0.7071, result.get(0).getY(), 0.0001);
		assertEquals(0.7071, result.get(1).getX(), 0.001);
		assertEquals(-0.7071, result.get(1).getY(), 0.0001);
	}
}
