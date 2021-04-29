/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.base;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.PoseSpeed2D;
import hso.autonomy.util.geometry.VectorUtils;
import hso.autonomy.util.properties.PropertyManager;
import java.util.ArrayList;
import java.util.List;
import magma.agent.decision.behavior.IWalkEstimator;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Calculates an estimation for how long it takes to walk somewhere
 * @author kdorer
 */
public class WalkEstimator implements IWalkEstimator
{
	private final float speed[];

	private final float turnSpeed;

	// the relative body direction lookup in degrees indexed by WalkMode
	private final float WALK_DIR[] = {0, 180, 90, -90, 45, -45, 135, -135};

	public WalkEstimator(float speeds[])
	{
		this.speed = speeds;
		turnSpeed = speeds[8];
	}

	public WalkEstimator(String propertiesPrefix)
	{
		this.speed = new float[8];
		this.speed[0] = PropertyManager.getFloat(propertiesPrefix + ".forward");
		this.speed[1] = PropertyManager.getFloat(propertiesPrefix + ".backward");
		this.speed[2] = PropertyManager.getFloat(propertiesPrefix + ".left");
		this.speed[3] = PropertyManager.getFloat(propertiesPrefix + ".right");
		this.speed[4] = PropertyManager.getFloat(propertiesPrefix + ".diagonalLeft");
		this.speed[5] = PropertyManager.getFloat(propertiesPrefix + ".diagonalRight");
		this.speed[6] = PropertyManager.getFloat(propertiesPrefix + ".diagonalBackLeft");
		this.speed[7] = PropertyManager.getFloat(propertiesPrefix + ".diagonalBackRight");
		turnSpeed = PropertyManager.getFloat(propertiesPrefix + ".turn");
	}

	@Override
	public WalkMode getFastestWalkMode(IPose2D thisPlayerPose, List<PoseSpeed2D> poses)
	{
		float bestTime = Float.POSITIVE_INFINITY;
		WalkMode bestMode = null;
		for (WalkMode mode : WalkMode.values()) {
			float time = estimateWalkTime(thisPlayerPose, new ArrayList<>(poses), mode);
			if (time < bestTime) {
				bestTime = time;
				bestMode = mode;
			}
		}
		return bestMode;
	}

	@Override
	public float getFastestWalkTime(IPose2D thisPlayerPose, List<PoseSpeed2D> poses)
	{
		float bestTime = Float.POSITIVE_INFINITY;
		for (WalkMode mode : WalkMode.values()) {
			float time = estimateWalkTime(thisPlayerPose, new ArrayList<>(poses), mode);
			if (time < bestTime) {
				bestTime = time;
			}
		}
		return bestTime;
	}

	/**
	 * Estimates the time (in s) it takes to get to the specified position turned
	 * into the specified direction.
	 * @param mode how to get there
	 * @return the time (in s)
	 */
	float estimateWalkTime(IPose2D currentPose, List<PoseSpeed2D> futurePoses, WalkMode mode)
	{
		if (futurePoses.size() > 2) {
			System.out.println(
					"This method (WalkEstimator#estimateWalkTime()) will get slow for paths longer than two!");
		}

		float walkTime = 0;
		if (futurePoses.isEmpty()) {
			// end of recursion
			return walkTime;
		}

		// there is at least one entry
		IPose2D firstPose = futurePoses.remove(0).getPose();

		// time for turning into desired walk mode direction
		Angle directionTo = VectorUtils.getDirectionTo(currentPose.getPosition(), firstPose.getPosition());
		Angle dirToGo = directionTo.subtract(currentPose.getAngle());
		Angle dirToTurn = dirToGo.subtract(Angle.deg(WALK_DIR[mode.ordinal()]));
		walkTime += Math.abs(dirToTurn.degrees()) / turnSpeed;

		// time for getting there
		double distanceToFinal = Vector2D.distance(currentPose.getPosition(), firstPose.getPosition());
		walkTime += distanceToFinal / speed[mode.ordinal()];

		if (futurePoses.size() > 0) {
			// we have more than one position to go, consider two
			float minTime = Float.MAX_VALUE;

			// try with each walk mode the next leg
			for (WalkMode mode2 : WalkMode.values()) {
				List<PoseSpeed2D> newList = new ArrayList<>(futurePoses);
				Angle heading = directionTo.subtract(Angle.deg(WALK_DIR[mode.ordinal()]));
				IPose2D nextPose = new Pose2D(firstPose.getPosition(), heading);
				float nextTime = estimateWalkTime(nextPose, newList, mode2);

				if (nextTime < minTime) {
					minTime = nextTime;
				}
			}
			walkTime += minTime;

		} else {
			// time for turning into final direction
			dirToGo = directionTo.subtract(firstPose.getAngle());
			dirToTurn = dirToGo.subtract(Angle.deg(WALK_DIR[mode.ordinal()]));
			walkTime += Math.abs(dirToTurn.degrees()) / turnSpeed;
		}

		return walkTime;
	}

	@Override
	public float getTurningSpeed()
	{
		return turnSpeed;
	}

	@Override
	public float getWalkSpeed(WalkMode walkMode)
	{
		return speed[walkMode.ordinal()];
	}

	@Override
	public void setWalkSpeed(WalkMode walkMode, float speed)
	{
		this.speed[walkMode.ordinal()] = speed;
	}
}
