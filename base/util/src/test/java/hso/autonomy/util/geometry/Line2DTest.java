/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.jupiter.api.Test;

public class Line2DTest
{
	@Test
	public void getClosestPoseTest()
	{
		Line2D testee = new Line2D(0, 0, Angle.rad(0), 10);
		IPose2D result = testee.getClosestPose(new Vector2D(1, 1));
		GeometryTestUtil.comparePose2D(1, 0, Angle.deg(0), result);

		result = testee.getClosestPose(new Vector2D(2, 2));
		GeometryTestUtil.comparePose2D(2, 0, Angle.deg(0), result);

		Line2D testee2 = new Line2D(-1.0, -1.0, Angle.rad(Math.PI / 4), 10.0);
		result = testee2.getClosestPose(new Vector2D(2, 0));
		GeometryTestUtil.comparePose2D(1, 1, Angle.rad(Math.PI * 0.25), result);

		result = testee2.getClosestPose(new Vector2D(0, 0));
		GeometryTestUtil.comparePose2D(0, 0, Angle.rad(Math.PI * 0.25), result);
	};

	@Test
	public void getTrailTest()
	{
		Line2D testee = new Line2D(1, 1, Angle.deg(-45));
		List<Vector2D> points = testee.getTrail(new Vector2D(1, 1), new Vector2D(3, -1.1));
		//		     for(Vector2d point : points)
		//   {
		//		     std::cout << "point x: " << point(0) << " y: " << point(1) << endl;
		//   }
		assertEquals(points.size(), 10, 0.001);
		assertEquals(points.get(0).getX(), 1.205, 0.001);
		assertEquals(points.get(4).getY(), -0.025, 0.0001);
		assertEquals(points.get(9).getX(), 3.05, 0.0001);
	}
}
