/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.kick;

import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.movement.Movement;
import magma.agent.decision.behavior.movement.MovementPhase;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.nao.INaoConstants;

/**
 * Kick ball straight forward. This Behavior assumes that stabilizing on one leg
 * was done already
 *
 * @author Stefan Glaser
 */
public class Kick8m extends KickMovement
{
	private static final String NAME = IBehaviorConstants.KICK_8M.KICK.BASE_NAME;

	public static final float MAX_KICK_DISTANCE = 8.5f;

	private static Movement createMovement(String name, ParameterMap params)
	{
		Kick8mParameters param = (Kick8mParameters) params.get(name);

		Movement kick = new Movement("kick8m");

		kick.add(new MovementPhase(PHASE_GET_SUPPORT_LEG, param.getTime0())
						 .add(RFootRoll, param.getRHR2(), 0.8f)
						 .add(RHipRoll, -param.getRHR2(), 0.8f)
						 .add(RHipPitch, param.getRHP1(), 2f)
						 .add(RKneePitch, param.getRKP1(), 3)
						 .add(RFootPitch, param.getRFP1(), 1.5f)
						 .add(RHipYawPitch, 0, 1)
						 .add(RToePitch, param.getRTP1(), 1.5f)

						 .add(LFootRoll, param.getLHR1() * 0.7, 0.5f)
						 .add(LHipRoll, param.getLHR1(), 0.5f)
						 .add(LHipPitch, param.getLHP1(), 2f)
						 .add(LKneePitch, 0, 4f)
						 .add(LFootPitch, 0, 2.4f)
						 .add(LHipYawPitch, param.getLHYP1(), param.getLHYPS1()));

		kick.add(new MovementPhase(PHASE_KICK, param.getTime1())
						 .add(RHipPitch, param.getRHP2(), 3)
						 .add(RKneePitch, param.getRKP2(), 5f));

		kick.add(new MovementPhase(PHASE_KICK_2, param.getTime2())
						 .add(RHipPitch, param.getRHP2(), INaoConstants.MAX_JOINT_SPEED)
						 .add(RKneePitch, param.getRKP2(), INaoConstants.MAX_JOINT_SPEED)
						 .add(RFootPitch, param.getRFP2(), param.getRFPS2())
						 .add(RToePitch, param.getRTP2(), INaoConstants.MAX_JOINT_SPEED));

		kick.add(new MovementPhase(PHASE_BALL_HIT, param.getTime3())
						 .add(LHipPitch, param.getLHP2(), INaoConstants.MAX_JOINT_SPEED)
						 .add(LFootPitch, 5, 2f)
						 .add(RHipPitch, param.getRHP2(), INaoConstants.MAX_JOINT_SPEED)
						 .add(RKneePitch, param.getRKP2(), INaoConstants.MAX_JOINT_SPEED)
						 .add(RFootPitch, param.getRFP2(), INaoConstants.MAX_JOINT_SPEED)
						 .add(RHipYawPitch, param.getRHYP2(), param.getRHYPS2())
						 .add(LHipYawPitch, 0f, param.getLHYPS2()));

		kick.add(new MovementPhase(PHASE_MOVE_TO_NORMAL, 10)
						 .add(LHipPitch, 5, 2f)
						 .add(LKneePitch, 0, 4f)
						 .add(LFootPitch, 0, 2f)
						 .add(LHipYawPitch, 0f, 4f)

						 .add(RHipYawPitch, 0, 6)
						 .add(RHipRoll, -20, 6)

						 .add(RHipPitch, 20, 2f)
						 .add(RKneePitch, -48, 4f)
						 .add(RFootPitch, 20, 4f)
						 .add(RToePitch, 0, INaoConstants.MAX_JOINT_SPEED));

		kick.add(new MovementPhase(PHASE_MOVE_TO_NORMAL_2, 20)
						 .add(RFootRoll, 0, 1f)
						 .add(RHipRoll, 0, 1f)
						 .add(LFootRoll, 0, 1f)
						 .add(LHipRoll, 0, 1f)
						 .add(RHipPitch, 15, 2f)
						 .add(RKneePitch, -30, 4f)
						 .add(RFootPitch, 15, 2f)
						 .add(LHipPitch, 15, 2f)
						 .add(LKneePitch, -30, 4f)
						 .add(LFootPitch, 15, 2f));
		return kick;
	}

	public Kick8m(Side side, IRoboCupThoughtModel thoughtModel, ParameterMap params)
	{
		super(side, NAME, thoughtModel, params, createMovement(NAME, params), MAX_KICK_DISTANCE, false);
		// don't try to cancel this kick
		cancelDistance = Float.MAX_VALUE;
	}

	@Override
	public void perform()
	{
		if (!currentMovement.isFinished()) {
			MovementPhase currentPhase = initialMovement.getCurrentPhase();
			if (currentPhase.getName().equals(PHASE_BALL_HIT)) {
				currentPhase.setRelativeSpeed(1 / kickPower);
			}
		}
		super.perform();
	}
}
