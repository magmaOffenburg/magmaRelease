/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.PoseSpeed2D;
import java.util.ArrayList;
import java.util.List;
import magma.agent.decision.behavior.IWalkEstimator.WalkMode;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author kdorer
 */
public class WalkEstimatorTest
{
	private WalkEstimator testee;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		// we do not want to change unit tests if we are able to run faster
		// FORWARD, BACKWARD, SIDEWARD, SIDEWARD, DIAGONAL, DIAGONAL, TURNING
		float[] speeds = {1.0f, 0.75f, 0.5f, 0.5f, 0.6f, 0.6f, 0.6f, 0.6f, 90};
		testee = new WalkEstimator(speeds);
	}

	@Test
	public void testEstimateWalkTime()
	{
		// we stand at 0,0 looking to other goal (abs angle 0)
		IPose2D pose1 = new Pose2D(0, 0, Angle.deg(-90));
		IPose2D pose2 = new Pose2D(2, 0, Angle.deg(45));

		List<PoseSpeed2D> path = getListFromPose(pose2);
		assertEquals(1 + 2 + 0.5, testee.estimateWalkTime(pose1, path, WalkMode.FORWARD), 0.001);

		path = getListFromPose(pose2);
		assertEquals(1 + 2 / 0.75 + 1.5, testee.estimateWalkTime(pose1, path, WalkMode.BACKWARD), 0.001);

		path = getListFromPose(pose2);
		assertEquals(0 + 4 + 1.5, testee.estimateWalkTime(pose1, path, WalkMode.LEFT_SIDE), 0.001);

		path = getListFromPose(pose2);
		assertEquals(2 + 4 + 0.5, testee.estimateWalkTime(pose1, path, WalkMode.RIGHT_SIDE), 0.001);
	}

	@Test
	public void testEstimateWalkTimeTwoPoints()
	{
		// we stand at 0,0 looking to other goal (abs angle 0)
		IPose2D pose1 = new Pose2D(0, 0, Angle.deg(-90));
		IPose2D pose2 = new Pose2D(1, 0, Angle.deg(45));
		IPose2D pose3 = new Pose2D(1, 1, Angle.deg(-90));

		List<PoseSpeed2D> path = getListFromPose(pose2, pose3);
		assertEquals(1 + 1 + 1 + 1 / 0.75, testee.estimateWalkTime(pose1, path, WalkMode.FORWARD), 0.001);

		path = getListFromPose(pose2, pose3);
		assertEquals(1 + 1 / 0.75 + 1 + 1 / 0.75, testee.estimateWalkTime(pose1, path, WalkMode.BACKWARD), 0.001);

		path = getListFromPose(pose2, pose3);
		assertEquals(0 + 1 / 0.5 + 1 / 0.75, testee.estimateWalkTime(pose1, path, WalkMode.LEFT_SIDE), 0.001);

		path = getListFromPose(pose2, pose3);
		assertEquals(2 + 1 / 0.5 + 1 + 2, testee.estimateWalkTime(pose1, path, WalkMode.RIGHT_SIDE), 0.001);
	}

	private List<PoseSpeed2D> getListFromPose(IPose2D... poses)
	{
		List<PoseSpeed2D> path = new ArrayList<>();
		for (IPose2D pose : poses) {
			PoseSpeed2D targetSpeed = new PoseSpeed2D(pose, Vector2D.ZERO);
			path.add(targetSpeed);
		}
		return path;
	}
}
