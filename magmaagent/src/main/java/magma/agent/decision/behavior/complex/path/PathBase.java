/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.path;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalk;
import magma.agent.model.agentmodel.SupportFoot;
import magma.util.benchmark.PathParameterWalkBenchmarkItem;

/**
 * @author Stefan Grossmann
 */
public abstract class PathBase
{
	/** start tolerance for path */
	protected static final double DISTANCE_TOLERANCE = 0.20;

	/** start point of path part */
	protected Pose2D startPoint;

	/** end point of path part */
	protected Pose2D endPoint;

	/** benchmark item that matches to current path part */
	public PathParameterWalkBenchmarkItem item;

	public abstract Angle getEndPointAngle();

	public abstract double getPathDistance();

	public abstract double getDistanceToPath(Pose2D currPose);

	public abstract double getWalkedDistance(Pose2D currPose);

	public abstract void updateWithPose(Pose2D currPose, PathBase nextPathPart);

	@Override
	public abstract String toString();

	public abstract String toWalk();

	/**
	 * get speed for path part
	 * @return avg speed of path part
	 */
	public double getSpeed()
	{
		return item.getAvgSpeed();
	}

	/**
	 * run walk behavior with parameters of benchmarkItem
	 * @param behaviors: that are currently loaded
	 * @return walk behavior
	 */
	public IWalk walk(BehaviorMap behaviors)
	{
		IWalk walk = (IWalk) behaviors.get(IBehaviorConstants.WALK);
		walk.walk(item.getForwardsBackwards(), item.getRightLeft(), item.getAngle());
		return walk;
	}

	/**
	 * calculate cost for path part
	 * @return costs
	 */
	public double getCost()
	{
		return getPathDistance() / getSpeed();
	}

	/**
	 * checks if current position is still on path
	 * @param currPose: current position
	 * @return true if its on path, else false
	 */
	public boolean stillOnPath(Pose2D currPose)
	{
		double toleranceAtPose = (this.getDistanceLeft(currPose) / this.getPathDistance() * DISTANCE_TOLERANCE);
		return this.getDistanceToPath(currPose) < (toleranceAtPose + 0.05);
	}

	/**
	 * rounds value by frac decimal places
	 * @param value: value that has to be rounded
	 * @param frac: count of decimal places
	 * @return rounded value
	 */
	public double round(final double value, final int frac)
	{
		return Math.round(Math.pow(10.0, frac) * value) / Math.pow(10.0, frac);
	}

	/**
	 * checks if end of this part is reached
	 * @param currPose: current position for check
	 * @return true if end of part is reached, else false
	 */
	public boolean endOfPathPartReached(Pose2D currPose)
	{
		Angle delta = currPose.getAngle().subtract(getEndPointAngle());
		return Math.abs(delta.degrees()) < 10;
	}

	/**
	 * get the distance that is left on the path
	 * @param currPose: current position
	 * @return the distance that is left
	 */
	public double getDistanceLeft(Pose2D currPose)
	{
		return this.getPathDistance() - this.getWalkedDistance(currPose);
	}

	/**
	 * run step behavior with given parameters
	 * @param behaviors: currently loaded behaviors
	 * @param currPose: current position
	 * @param path: path that wanted to walk
	 * @param footTarget: the target for the defined support foot
	 * @param supportFoot: defined support foot
	 * @return step behavior
	 */
	public IBehavior step(BehaviorMap behaviors, Pose2D currPose, Path path, Pose2D footTarget, SupportFoot supportFoot)
	{
		WalkPath walk = (WalkPath) behaviors.get(IBehaviorConstants.WALK_PATH);
		walk.step(path, footTarget, supportFoot);
		return walk;
	}

	// Getter and Setter
	public Pose2D getStartPoint()
	{
		return startPoint;
	}

	public void setStartPoint(Pose2D startPoint)
	{
		this.startPoint = startPoint;
	}

	public Pose2D getEndPoint()
	{
		return endPoint;
	}

	public void setEndPoint(Pose2D endPoint)
	{
		this.endPoint = endPoint;
	}
}
