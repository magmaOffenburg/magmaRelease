/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Arc2D extends Circle2D
{
	/** the angle where to start the arc */
	private Angle startAngle;

	/** the angle where to end the arc */
	private Angle endAngle;

	public Arc2D(double x, double y, double radius, Angle startAngle, Angle endAngle)
	{
		super(x, y, radius);
		this.startAngle = startAngle;
		this.endAngle = endAngle;
	}

	public Angle getStartAngle()
	{
		return startAngle;
	}

	public Angle getEndAngle()
	{
		return endAngle;
	}

	@Override
	public List<Vector2D> getLineSegmentIntersectionPoint(Vector2D pointA, Vector2D pointB)
	{
		List<Vector2D> result = super.getLineSegmentIntersectionPoint(pointA, pointB);

		// check if points are in the arc part of the circle
		Vector2D center = getCenter();
		Iterator<Vector2D> iterator = result.iterator();
		while (iterator.hasNext()) {
			Vector2D point = iterator.next();
			Vector2D cp = point.subtract(center);
			Angle angle = Angle.to(cp);
			if (!angle.isBetween(startAngle, endAngle)) {
				iterator.remove();
			}
		}
		return result;
	}
}
