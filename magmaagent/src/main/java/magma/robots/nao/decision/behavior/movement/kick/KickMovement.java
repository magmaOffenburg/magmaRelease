/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.kick;

import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IKickBaseDecider;
import magma.agent.decision.behavior.IKickMovement;
import magma.agent.decision.behavior.base.KickBaseDecider;
import magma.agent.decision.behavior.ikMovement.KickMovementParameters;
import magma.agent.decision.behavior.movement.Movement;
import magma.agent.decision.behavior.movement.MovementPhase;
import magma.agent.decision.behavior.movement.SidedMovementBehavior;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.robots.nao.INaoConstants;
import magma.robots.nao.INaoJoints;

public abstract class KickMovement extends SidedMovementBehavior implements IKickMovement, INaoJoints
{
	public static final String PHASE_GET_SUPPORT_LEG = "getSupportLeg";

	public static final String PHASE_KICK = "kick";

	public static final String PHASE_KICK_2 = "kick2";

	public static final String PHASE_BALL_HIT = "ballHit";

	public static final String PHASE_MOVE_TO_NORMAL = "moveToNormal";

	public static final String PHASE_MOVE_TO_NORMAL_2 = "moveToNormal2";

	protected float cancelDistance;

	protected float kickPower;

	private boolean wasCancelled;

	protected transient final IKickBaseDecider decider;

	public KickMovement(Side side, String baseName, IRoboCupThoughtModel thoughtModel, ParameterMap params,
			Movement initialMovement, double maxKickDistance, boolean unstable)
	{
		super(side, baseName, thoughtModel, initialMovement);
		KickMovementParameters kickMovementParameters = (KickMovementParameters) params.get(baseName);
		cancelDistance = kickMovementParameters.get(KickMovementParameters.Param.CANCEL_DISTANCE);
		int ballHitCycles = initialMovement.getPhase(PHASE_GET_SUPPORT_LEG).getCycles() +
							initialMovement.getPhase(PHASE_KICK).getCycles() +
							initialMovement.getPhase(PHASE_KICK_2).getCycles();
		decider = new KickBaseDecider(kickMovementParameters.getDistribution(), maxKickDistance, maxKickDistance,
				ballHitCycles, unstable, 1000);
	}

	@Override
	public IKickBaseDecider getKickDecider()
	{
		return decider;
	}

	private double getDistanceToBall()
	{
		IRoboCupWorldModel worldModel = getWorldModel();
		String otherFootName = side == Side.RIGHT ? INaoConstants.LFoot : INaoConstants.RFoot;
		Pose3D currentOtherFootPose = getAgentModel().getBodyPart(otherFootName).getPose();
		Pose2D globalOtherFootPose = worldModel.getThisPlayer().calculateGlobalBodyPose2D(currentOtherFootPose);
		return worldModel.getBall().getDistanceToXY(globalOtherFootPose.getPosition());
	}

	@Override
	public void init()
	{
		super.init();
		wasCancelled = false;
	}

	@Override
	public void perform()
	{
		if (!currentMovement.isFinished()) {
			MovementPhase currentPhase = initialMovement.getCurrentPhase();
			if (currentPhase.getName().equals(PHASE_KICK) && getDistanceToBall() > cancelDistance) {
				isFinished = true;
				wasCancelled = true;
				return;
			}
		}
		super.perform();
	}

	public boolean wasCancelled()
	{
		return wasCancelled;
	}
}
