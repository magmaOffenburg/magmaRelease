/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.base;

import hso.autonomy.util.geometry.Geometry;
import java.util.ArrayList;
import java.util.Arrays;
import magma.agent.decision.behavior.IKick;
import magma.agent.decision.behavior.IKickWalkDecider;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IThisPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Class to decide if a Kick while walking is desirable (getApplicability()) and currently performable
 * (getExecutability())
 *
 * @author Klaus Dorer
 */
public class KickWalkEstimator extends KickEstimator
{
	public KickWalkEstimator(IRoboCupThoughtModel thoughtModel, IWalkEstimator walkEstimator, IKick kick)
	{
		super(thoughtModel, walkEstimator, kick);
		executabilityCheckSuppliers.addAll(new ArrayList<>(Arrays.asList(this::checkFootForce, this::checkUpVector,
				this::checkLocalBallPosition, this::checkKickableArea, this::checkAngleDeviation)));
		executabilityConditionResults = new int[executabilityCheckSuppliers.size()];

		applicabilityCheckSuppliers.addAll(new ArrayList<>(
				Arrays.asList(this::checkGoalDistanceApplicability, this::checkAngleDeviationApplicability)));
		applicabilityConditionResults = new int[applicabilityCheckSuppliers.size()];
	}

	@Override
	protected String getExecutabilityCheckNames()
	{
		return super.getExecutabilityCheckNames() +
				"checkFootForce;checkUpVector;checkLocalBallPosition;checkKickableArea;checkAngleDeviation";
	}

	@Override
	protected String getAvailabilityCheckNames()
	{
		return super.getAvailabilityCheckNames() + "checkGoalDistanceApplicability;checkAngleDeviationApplicability;";
	}

	@Override
	public float getExecutability()
	{
		return super.getExecutability() * 100;
	}

	protected Float checkFootForce()
	{
		int offGroundBefore = getKickDecider().getOffGroundBefore();
		int maxOnGround = getKickDecider().getMaxOnGround();
		IRoboCupAgentModel agentModel = getThoughtModel().getAgentModel();
		if (!agentModel.hasFootForceSensors() ||
				agentModel.getStepFoot(offGroundBefore, maxOnGround) ==
						((getKickDecider().getKickingFoot() == SupportFoot.LEFT) ? SupportFoot.RIGHT
																				 : SupportFoot.LEFT)) {
			// foot force sensors suitable
			return 0.0f;
		}
		return -1.0f;
	}

	protected Float checkUpVector()
	{
		IThisPlayer thisPlayer = worldModel.getThisPlayer();
		double up = thisPlayer.getUpVectorZ();
		if (up < getKickDecider().getMinUpVectorZ()) {
			// we are not upright enough
			// System.out.println("Not upright: " + up);
			return -1.0f;
		}
		return 0.0f;
	}

	protected Float checkKickableArea()
	{
		IThisPlayer thisPlayer = worldModel.getThisPlayer();
		// use this version when future ball position is desired
		// Vector3D ballPos = kickEstimator.getBallPosAtKick(kickParams);

		// use this version when current ball position is desired
		Vector3D ballPos = worldModel.getBall().getPosition();

		if (!thisPlayer.isInsideArea(ballPos, getKickDecider().getKickableArea())) {
			// System.out.println("Outside area ball: " + thisPlayer.calculateLocalPosition(ballPos));
			return -1.0f;
		}
		return 0.0f;
	}

	/**
	 * Checks the deviation of desired kick angle and the angle the kick would go with respect to our current body
	 * direction
	 * @return 1/10th of the absolute difference in kick angle (in degrees), -1 if not applicable
	 */
	public Float checkAngleDeviation()
	{
		IThisPlayer thisPlayer = worldModel.getThisPlayer();
		IKickWalkDecider decider = getKickDecider();
		float absAngleDeviation = (float) Math.abs(
				decider.getKickDirection()
						.subtract(thisPlayer.getHorizontalAngle().add(decider.getRelativeKickDirection()))
						.degrees());
		double ballGoalDistance = worldModel.getBall().getDistanceToXY(worldModel.getOtherGoalPosition());
		float maxDeviation = (float) (Geometry.getLinearFuzzyValue(8, 15, true, ballGoalDistance) * 40 + 10);
		if (absAngleDeviation > maxDeviation) {
			// System.out.println("Abs angle deviation: " + absAngleDeviation);
			return -1.0f;
		}
		// System.out.println("Angle deviation: " + maxDeviation);
		return (maxDeviation - absAngleDeviation) / 10;
	}

	protected Float checkLocalBallPosition()
	{
		return 0.0f;
	}

	/**
	 * Checks the distance of the ball to the goal. Can be used to disallow kicks for goal kicks
	 * @return >= 0 if ok, -1 if not ok
	 */
	protected Float checkGoalDistanceApplicability()
	{
		return 0.0f;
	}

	/**
	 * Checks the deviation of desired kick angle and the angle the kick would go with respect to our current body
	 * direction
	 * @return we do not require checks for kicks that stop running near the ball
	 */
	protected Float checkAngleDeviationApplicability()
	{
		return 0.0f;
	}

	@Override
	protected IKickWalkDecider getKickDecider()
	{
		return (IKickWalkDecider) kick.getKickDecider();
	}
}
