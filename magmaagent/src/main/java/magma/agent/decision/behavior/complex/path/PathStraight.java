/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.path;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import magma.util.benchmark.PathParameterWalkBenchmarkItem;

public class PathStraight extends PathBase
{
	/** max walked speed */
	private final double MAX_SPEED = 0.841;

	/** distance needed to slow agent */
	private final double BREAK_DISTANCE = 0.6;

	public PathStraight(Pose2D startPoint, Pose2D endPoint)
	{
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.item = new PathParameterWalkBenchmarkItem(100, 0, 0, 1, MAX_SPEED, 0);
	}

	@Override
	public boolean endOfPathPartReached(Pose2D currPose)
	{
		double dist2EndPoint = currPose.getDistanceTo(endPoint);
		return super.endOfPathPartReached(currPose) && dist2EndPoint < DISTANCE_TOLERANCE;
	}

	@Override
	public double getWalkedDistance(Pose2D currPose)
	{
		double dist2Curr = startPoint.getDistanceTo(currPose);
		Angle alpha = startPoint.getAngleTo(currPose).subtract(startPoint.getAngleTo(endPoint));
		return Math.cos(alpha.radians()) * dist2Curr;
	}

	@Override
	public double getPathDistance()
	{
		return startPoint.getDistanceTo(endPoint);
	}

	@Override
	public String toString()
	{
		return "Straight (" + round(getCost(), 2) + ") - (" + MAX_SPEED + ") - Start(" + round(startPoint.getX(), 2) +
				"/" + round(startPoint.getY(), 2) + "/" + round(startPoint.getAngle().degreesPositive(), 2) +
				") - End(" + round(endPoint.getX(), 2) + "/" + round(endPoint.getY(), 2) + "/" +
				round(endPoint.getAngle().degreesPositive(), 2) + ")";
	}

	@Override
	public String toWalk()
	{
		return "toWalk: Straight(" + round(startPoint.getX(), 2) + "/" + round(startPoint.getY(), 2) + ")to(" +
				round(endPoint.getX(), 2) + "/" + round(endPoint.getY(), 2) + ")";
	}

	@Override
	public Angle getEndPointAngle()
	{
		return new Pose2D(startPoint).getAngleTo(new Pose2D(endPoint));
	}

	/**
	 * calculate a pose at a defined distance on path
	 * @param distance: at which the pose is wanted
	 * @return the pose
	 */
	public Pose2D getPoseAtDistance(double distance)
	{
		if (distance < 0)
			distance = 0;

		if (distance > this.getPathDistance())
			distance = this.getPathDistance();

		double factor = distance / this.getPathDistance();
		double x = (this.endPoint.x - this.startPoint.x) * factor;
		double y = (this.endPoint.y - this.startPoint.y) * factor;
		return new Pose2D(startPoint.x + x, startPoint.y + y, startPoint.getAngleTo(endPoint));
	}

	@Override
	public double getDistanceToPath(Pose2D currPose)
	{
		double dist2path = 0;
		double dist2Curr = 0;
		Pose2D endPose = new Pose2D(endPoint);
		Pose2D startPose = new Pose2D(startPoint);

		Angle alpha = endPose.getAngleTo(currPose).subtract(endPose.getAngleTo(startPose));

		// check with angle if currPose is beside path -> before endpoint
		if (alpha.degrees() < 90 && alpha.degrees() > -90) {
			// check distance to straight path
			dist2Curr = endPoint.getDistanceTo(currPose);
		} else {
			// currPose behind endpoint
			alpha = startPose.getAngleTo(currPose).subtract(startPose.getAngleTo(endPose));
			dist2Curr = startPoint.getDistanceTo(currPose);
		}

		if (alpha.degrees() == 0)
			return startPoint.getDistanceTo(currPose);
		dist2path = Math.sin(alpha.radians()) * dist2Curr;
		return Math.abs(dist2path);
	}

	@Override
	public void updateWithPose(Pose2D currPose, PathBase nextPathPart)
	{
		double walkedDist = this.getWalkedDistance(currPose);
		double pathDist = this.getPathDistance();
		double toWalkDist = pathDist - walkedDist;
		Angle delta;
		delta = currPose.getAngleTo(this.getPoseAtDistance(walkedDist + (pathDist - walkedDist) / 2));
		walkedDist = this.getWalkedDistance(currPose);
		item.setAngle((int) (delta.degrees() * (toWalkDist / pathDist)));
		if (toWalkDist < BREAK_DISTANCE) {
			double speedThere = 0;
			if (nextPathPart != null)
				speedThere = nextPathPart.getSpeed();
			double speed = ((speedThere * (toWalkDist / BREAK_DISTANCE))) / MAX_SPEED * 100;
			item.setForwardsBackwards(speed);
		}
	}
}