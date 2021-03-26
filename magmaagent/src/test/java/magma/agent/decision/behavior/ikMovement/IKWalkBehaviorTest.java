/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;
import magma.agent.model.agentmodel.SupportFoot;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IKWalkBehaviorTest
{
	@Disabled
	@Test
	public void testCalculateFreeFootPose()
	{
		IKWalkBehavior testee = new IKWalkBehavior(null, null, null);
		double Y_OFFSET = 2 * new IKWalkMovementParametersBase().getWalkWidth();

		// test zero pose
		Pose2D globalSupportFootPose = new Pose2D();
		Step step = new Step();
		Pose2D result = testee.calculateFreeFootPose(globalSupportFootPose, SupportFoot.LEFT, step);

		assertEquals(0, result.x, 0.0001);
		assertEquals(-Y_OFFSET, result.y, 0.0001);
		assertEquals(0, result.angle.degrees(), 0.0001);

		// test some random pose with no rotation in the step
		globalSupportFootPose.x = 5;
		globalSupportFootPose.y = 1;
		globalSupportFootPose.angle = Angle.deg(90);
		step.sideward = 0;
		step.forward = 0.1;
		step.turn = Angle.ZERO;
		result = testee.calculateFreeFootPose(globalSupportFootPose, SupportFoot.LEFT, step);

		assertEquals(globalSupportFootPose.x + Y_OFFSET, result.x, 0.0001);
		assertEquals(globalSupportFootPose.y + 2 * step.forward, result.y, 0.0001);
		assertEquals(90, result.angle.degrees(), 0.0001);

		// test the same with the alternative foot
		result = testee.calculateFreeFootPose(globalSupportFootPose, SupportFoot.RIGHT, step);

		assertEquals(globalSupportFootPose.x - Y_OFFSET, result.x, 0.0001);
		assertEquals(globalSupportFootPose.y + 2 * step.forward, result.y, 0.0001);
		assertEquals(90, result.angle.degrees(), 0.0001);

		// test the full banana where no turn is possible
		globalSupportFootPose.x = -8;
		globalSupportFootPose.y = 3;
		globalSupportFootPose.angle = Angle.deg(-40);
		step.sideward = 0.03;
		step.forward = 0.1;
		step.turn = Angle.deg(30);
		result = testee.calculateFreeFootPose(globalSupportFootPose, SupportFoot.LEFT, step);
		Vector2D stepVector = globalSupportFootPose.angle.applyTo(2 * step.forward, -Y_OFFSET - 2 * step.sideward);

		assertEquals(globalSupportFootPose.x + stepVector.getX(), result.x, 0.0001);
		assertEquals(globalSupportFootPose.y + stepVector.getY(), result.y, 0.0001);
		assertEquals(-40, result.angle.degrees(), 0.0001);

		// test the same with the alternative support foot where the turn is
		// possible, but the side step is restricted
		result = testee.calculateFreeFootPose(globalSupportFootPose, SupportFoot.RIGHT, step);
		stepVector = globalSupportFootPose.angle.applyTo(
				step.turn.applyTo(2 * step.forward, Y_OFFSET - 0.04)); // 0.04 is the internal limit

		assertEquals(globalSupportFootPose.x + stepVector.getX(), result.x, 0.0001);
		assertEquals(globalSupportFootPose.y + stepVector.getY(), result.y, 0.0001);
		assertEquals(20, result.angle.degrees(), 0.0001);
	}
}
