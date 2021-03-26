/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.path;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Circle2D;
import hso.autonomy.util.geometry.Pose2D;
import magma.util.benchmark.PathParameterWalkBenchmarkItem;

/**
 * @author Stefan Grossmann
 */
public class PathCircle extends PathBase
{
	/** circle that represents the pathCircle */
	public Circle2D circle;

	public PathCircle(Pose2D startPoint, Pose2D endPoint, PathParameterWalkBenchmarkItem item, Circle2D circle)
	{
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.item = item;
		this.circle = circle;
	}

	/**
	 * copy constructor
	 */
	public PathCircle(PathCircle other)
	{
		this.startPoint = new Pose2D(other.startPoint);
		this.endPoint = new Pose2D(other.endPoint);
		this.item = new PathParameterWalkBenchmarkItem(other.item);
		this.circle = new Circle2D(other.circle);
	}

	@Override
	public double getPathDistance()
	{
		boolean clockwise = isClockwise();
		Angle angle = Circle2D.getArc(
				this.circle.getAngleToPoint(startPoint), this.circle.getAngleToPoint(endPoint), clockwise);
		return item.getRadius() * Math.PI / 180 * angle.degreesPositive();
	}

	@Override
	public String toString()
	{
		return "Circle (" + round(getCost(), 2) + ") - (" + item.getForwardsBackwards() + "/" + item.getAngle() +
				") - (" + round(circle.getX(), 2) + "/" + round(circle.getY(), 2) + "/" + round(circle.getRadius(), 2) +
				") - Start(" + round(startPoint.getX(), 2) + "/" + round(startPoint.getY(), 2) + "/" +
				round(startPoint.getAngle().degreesPositive(), 2) + ") - End(" + round(endPoint.getX(), 2) + "/" +
				round(endPoint.getY(), 2) + "/" + round(endPoint.getAngle().degreesPositive(), 2) + ")";
	}

	@Override
	public String toWalk()
	{
		return "toWalk: Circle(" + round(startPoint.getX(), 2) + "/" + round(startPoint.getY(), 2) + ")to(" +
				round(endPoint.getX(), 2) + "/" + round(endPoint.getY(), 2) + ") --- (" + item.getForwardsBackwards() +
				"/" + item.getAngle() + ")";
	}

	@Override
	public Angle getEndPointAngle()
	{
		// System.out.println("circle");
		Angle angle = Angle.ANGLE_90;
		if (item.getAngle().degrees() > 0)
			angle = angle.subtract(Angle.ANGLE_180);
		return this.circle.getAngleToPoint(this.endPoint).subtract(angle);
	}

	@Override
	public double getDistanceToPath(Pose2D currPose)
	{
		return Math.abs(this.circle.getDistance(currPose) - this.circle.getRadius());
	}

	@Override
	public double getWalkedDistance(Pose2D currPose)
	{
		boolean clockwise = isClockwise();
		double walkedAngle = 0;
		double currPoseAngle = this.circle.getAngleToPoint(currPose).degreesPositive();
		Angle curr2StartAngle = Circle2D.getArc(
				currPoseAngle, this.circle.getAngleToPoint(this.startPoint).degreesPositive(), clockwise);
		Angle curr2EndAngle =
				Circle2D.getArc(currPoseAngle, this.circle.getAngleToPoint(this.endPoint).degreesPositive(), clockwise);

		if (curr2StartAngle.degreesPositive() < curr2EndAngle.degreesPositive()) {
			walkedAngle = -curr2StartAngle.degreesPositive();
		} else {
			walkedAngle = Circle2D.getArc(this.circle.getAngleToPoint(this.startPoint).degreesPositive(), currPoseAngle,
										  clockwise)
								  .degreesPositive();
		}
		return 2 * Math.PI * this.circle.getRadius() / 360 * walkedAngle;
	}

	@Override
	public void updateWithPose(Pose2D currPose, PathBase nextPathPart)
	{
		// Nothing to do!
	}

	/**
	 * checks if walking path part in clockwise
	 * @return true if its clockwise, else not
	 */
	private boolean isClockwise()
	{
		return this.item.getAngle().degrees() <= 0;
	}
}
