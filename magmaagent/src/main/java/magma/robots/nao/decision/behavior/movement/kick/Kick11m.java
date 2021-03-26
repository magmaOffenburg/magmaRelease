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

public class Kick11m extends KickMovement
{
	private static final String NAME = IBehaviorConstants.KICK_11M.KICK.BASE_NAME;

	public static final float MAX_KICK_DISTANCE = 11.0f;

	private static Movement createMovement(String name, ParameterMap params)
	{
		Kick11mParameters param = (Kick11mParameters) params.get(name);

		Movement kick = new Movement("kick11m");

		float sleft = 1f;
		float sright = 1f;
		kick.add(new MovementPhase(PHASE_GET_SUPPORT_LEG, param.getTime0())
						 .add(LHipYawPitch, 0, sleft)
						 .add(LHipRoll, -12, sleft)
						 .add(LHipPitch, 28.44, sleft)
						 .add(LKneePitch, -46.33, sleft)
						 .add(LFootPitch, 31, sleft)
						 .add(LFootRoll, 12, sleft)

						 .add(RHipYawPitch, 0, sright)
						 .add(RHipRoll, -12, sright)
						 .add(RHipPitch, 28.44, sright)
						 .add(RKneePitch, -130, sright)
						 .add(RFootPitch, 75, sright)
						 .add(RFootRoll, 12, sright)
						 .add(RToePitch, param.getRTP1(), 1.5f)

						 .add(LShoulderPitch, -50, INaoConstants.MAX_JOINT_SPEED)
						 .add(LShoulderYaw, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(LArmRoll, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(LArmYaw, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(RShoulderPitch, -50, INaoConstants.MAX_JOINT_SPEED)
						 .add(RShoulderYaw, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(RArmRoll, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(RArmYaw, 0, INaoConstants.MAX_JOINT_SPEED));

		// faster version but slightly less reliable
		// kick.add(new MovementPhase(PHASE_KICK, 28)//
		kick.add(new MovementPhase(PHASE_KICK, param.getTime1())
						 .add(LHipYawPitch, -50)
						 .add(LHipRoll, param.getLHR1())
						 .add(LHipPitch, 0)
						 .add(LKneePitch, 0)
						 .add(LFootPitch, 0)
						 .add(LFootRoll, 12, 0.1f)

						 .add(RHipYawPitch, 0)
						 .add(RHipRoll, -12)
						 .add(RHipPitch, -25, 5)
						 .add(RKneePitch, -130, 5f)
						 .add(RFootPitch, -50, 5)
						 .add(RFootRoll, 12)

						 .add(LShoulderPitch, -50, INaoConstants.MAX_JOINT_SPEED)
						 .add(LShoulderYaw, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(LArmRoll, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(LArmYaw, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(RShoulderPitch, -50, INaoConstants.MAX_JOINT_SPEED)
						 .add(RShoulderYaw, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(RArmRoll, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(RArmYaw, 0, INaoConstants.MAX_JOINT_SPEED));

		kick.add(new MovementPhase(PHASE_KICK_2, param.getTime2())
						 .add(LHipYawPitch, 0)
						 .add(LHipRoll, param.getLHR1())
						 .add(LHipPitch, param.getLHP2())
						 .add(LKneePitch, param.getLKP2())
						 .add(LFootPitch, param.getLFP2())
						 .add(LFootRoll, 12)

						 .add(RHipYawPitch, param.getRHYP2())
						 .add(RHipRoll, -12)
						 .add(RHipPitch, param.getRHP2())
						 .add(RKneePitch, param.getRKP2())
						 .add(RFootPitch, param.getRFP2())
						 .add(RFootRoll, 12)
						 .add(RToePitch, param.getRTP2(), INaoConstants.MAX_JOINT_SPEED)

						 .add(LShoulderPitch, -120)
						 .add(RShoulderPitch, -120));

		kick.add(new MovementPhase(PHASE_BALL_HIT, 26)
						 .add(LHipYawPitch, 0)
						 .add(LHipRoll, param.getLHR1(), INaoConstants.MAX_JOINT_SPEED)
						 .add(LHipPitch, -30, INaoConstants.MAX_JOINT_SPEED)
						 .add(LKneePitch, -60, INaoConstants.MAX_JOINT_SPEED)
						 .add(LFootPitch, -30, INaoConstants.MAX_JOINT_SPEED)
						 .add(LFootRoll, 12, INaoConstants.MAX_JOINT_SPEED)

						 .add(RHipYawPitch, -40, INaoConstants.MAX_JOINT_SPEED)
						 .add(RHipRoll, -12, INaoConstants.MAX_JOINT_SPEED)
						 .add(RHipPitch, 90, INaoConstants.MAX_JOINT_SPEED)
						 .add(RKneePitch, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(RFootPitch, 40, INaoConstants.MAX_JOINT_SPEED)
						 .add(RFootRoll, 12, INaoConstants.MAX_JOINT_SPEED)

						 .add(LShoulderPitch, -120, INaoConstants.MAX_JOINT_SPEED)
						 .add(RShoulderPitch, -120, INaoConstants.MAX_JOINT_SPEED));

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
						 .add(LFootPitch, 15, 2f)

						 .add(LShoulderPitch, -90, INaoConstants.MAX_JOINT_SPEED)
						 .add(LShoulderYaw, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(LArmRoll, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(LArmYaw, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(RShoulderPitch, -90, INaoConstants.MAX_JOINT_SPEED)
						 .add(RShoulderYaw, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(RArmRoll, 0, INaoConstants.MAX_JOINT_SPEED)
						 .add(RArmYaw, 0, INaoConstants.MAX_JOINT_SPEED));

		return kick;
	}

	public Kick11m(Side side, IRoboCupThoughtModel thoughtModel, ParameterMap params)
	{
		super(side, NAME, thoughtModel, params, createMovement(NAME, params), MAX_KICK_DISTANCE, true);
	}
}
