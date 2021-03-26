/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.jupiter.api.Test;

public class PolygonTest
{
	@Test
	public void testContains()
	{
		double x1 = 0.4;
		double y1 = 1.7;
		double x2 = 0.98;
		double y2 = 1.96;

		double px = 0.69;
		double py = 1.83;

		Polygon polygon =
				new Polygon(new Vector2D(x1, y1), new Vector2D(x2, y1), new Vector2D(x2, y2), new Vector2D(x1, y2));
		Vector2D point = new Vector2D(px, py);
		assertTrue(polygon.contains(point));
	}
}
