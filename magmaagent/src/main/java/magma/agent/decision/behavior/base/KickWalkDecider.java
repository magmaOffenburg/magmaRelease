/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.base;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.Pose2D;
import magma.agent.decision.behavior.IKickWalkDecider;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.worldmodel.GameState;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author kdorer
 *
 */
public class KickWalkDecider extends KickDecider implements IKickWalkDecider
{
	/** the area in which the ball has to be to be kickable (2D local) */
	private Area2D.Float kickableArea;

	/** true if the base estimator checks should be performed */
	private boolean checkKickEstimator;

	/** the minimal number of cycles the force sensor had to be zero in last off ground situation (0 to switch off) */
	private int offGroundBefore;

	/** maximal number of cycles the foot sensor has force since last ground contact (big values switch off test) */
	private int maxOnGround;

	/** the minimal z value the torso's z axis must have */
	private double minUpVectorZ;

	/**
	 * Params comments see class KickParameters
	 */
	public KickWalkDecider(KickEstimator kickWalkEstimator, KickDistribution distribution, SupportFoot kickingFoot,
			Pose2D relativeRunToPose, Vector2D speedAtRunToPose, Angle relativeKickDirection, Angle kickDirection,
			double maxKickDistance, double minKickDistance, float opponentMinDistance, float opponentMaxDistance,
			float ballMaxSpeed, float ownMinSpeed, float ownMaxSpeed, int ballHitCycles, boolean unstable,
			float priority, //
			// for kick walk
			Area2D.Float kickableArea, boolean checkKickEstimator, int offGroundBefore, int maxOnGround,
			double minUpVectorZ)
	{
		super(kickWalkEstimator, distribution, kickingFoot, relativeRunToPose, speedAtRunToPose, relativeKickDirection,
				kickDirection, maxKickDistance, minKickDistance, opponentMinDistance, opponentMaxDistance, ballMaxSpeed,
				ownMinSpeed, ownMaxSpeed, ballHitCycles, unstable, priority);
		this.kickableArea = kickableArea;
		this.checkKickEstimator = checkKickEstimator;
		this.offGroundBefore = offGroundBefore;
		this.maxOnGround = maxOnGround;
		this.minUpVectorZ = minUpVectorZ;

		KickWalkEstimator estimator = (KickWalkEstimator) getKickEstimator();
		estimator.addApplicabilityCheck(this::checkPassMode);
	}

	protected float checkPassMode()
	{
		GameState currendGameState = getWorldModel().getGameState();

		// Don't do this kick if we are in these modes
		if (currendGameState == GameState.OWN_PASS || currendGameState == GameState.OWN_DIRECT_FREE_KICK ||
				currendGameState == GameState.OWN_KICK_IN || currendGameState == GameState.OWN_GOAL_KICK ||
				currendGameState == GameState.OWN_FREE_KICK) {
			return -1.0f;
		}

		return 0.0f;
	}

	@Override
	public Area2D.Float getKickableArea()
	{
		return kickableArea;
	}

	@Override
	public boolean checkKickEstimator()
	{
		return checkKickEstimator;
	}

	@Override
	public int getOffGroundBefore()
	{
		return offGroundBefore;
	}

	@Override
	public int getMaxOnGround()
	{
		return maxOnGround;
	}

	@Override
	public double getMinUpVectorZ()
	{
		return minUpVectorZ;
	}
}
