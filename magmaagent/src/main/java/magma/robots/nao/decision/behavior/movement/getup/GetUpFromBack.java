/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.getup;

import hso.autonomy.agent.decision.behavior.IBehavior;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IKeepBehavior;
import magma.agent.decision.behavior.movement.Movement;
import magma.agent.decision.behavior.movement.MovementBehavior;
import magma.agent.decision.behavior.movement.MovementPhase;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.nao.INaoJoints;

/**
 * This behavior will get up the agent when it lies on the back.<br>
 * Just don't ask why...
 *
 * @author Stefan Glaser
 */
public class GetUpFromBack extends MovementBehavior implements INaoJoints
{
	private Movement stretch;

	private static Movement createMovement(ParameterMap params)
	{
		GetUpFromBackParameters param = (GetUpFromBackParameters) params.get(IBehaviorConstants.GET_UP_BACK);

		Movement getUp = new Movement("getupBack");

		// skipWhenFinished instead of fixed # of cycles to get same starting
		// conditions each time
		getUp.add(new MovementPhase("phase1", 50, true)
						  .add(NeckPitch, 0, 7f)
						  .add(NeckYaw, 0, 7f)
						  .add(LShoulderPitch, -90, 7f)
						  .add(LShoulderYaw, 0, 7f)
						  .add(LArmYaw, 0, 7f)
						  .add(LArmRoll, 0, 7f)

						  .add(LHipYawPitch, 0, 7f)
						  .add(LHipPitch, 0, 7f)
						  .add(LHipRoll, 0, 7f)
						  .add(LKneePitch, 0, 7f)
						  .add(LFootPitch, 0, 7f)
						  .add(LFootRoll, 0, 7f)

						  .add(RShoulderPitch, -90, 7f)
						  .add(RShoulderYaw, 0, 7f)
						  .add(RArmYaw, 0, 7f)
						  .add(RArmRoll, 0, 7f)

						  .add(RHipYawPitch, 0, 7f)
						  .add(RHipPitch, 0, 7f)
						  .add(RHipRoll, 0, 7f)
						  .add(RKneePitch, 0, 7f)
						  .add(RFootPitch, 0, 7f)
						  .add(RFootRoll, 0, 7f));

		// a small delay seems to help stability for some robot types. seems a bit
		// hacky though...
		getUp.add(new MovementPhase("wait", param.getTime1()));

		getUp.add(new MovementPhase("phase2", param.getTime2())
						  .add(LShoulderPitch, 0, 7f)
						  .add(LHipPitch, param.getHipPitch(), 7f)
						  .add(RShoulderPitch, 0, 7f)
						  .add(RHipPitch, param.getHipPitch(), 7f));

		getUp.add(new MovementPhase("phase3", param.getTime3())
						  .add(LShoulderPitch, 0, 7f)
						  .add(LHipYawPitch, param.getHipYawPitch(), 7f)
						  .add(LHipPitch, param.getHipPitch(), param.getHipPitchSpeed())
						  .add(LHipRoll, param.getHipRoll(), 7f)
						  .add(LKneePitch, param.getKneePitch(), 7f)
						  .add(LFootPitch, param.getFootPitch(), param.getFootPitchSpeed())

						  .add(RShoulderPitch, 0, 7f)
						  .add(RHipYawPitch, param.getHipYawPitch(), 7f)
						  .add(RHipPitch, param.getHipPitch(), param.getHipPitchSpeed())
						  .add(RHipRoll, -param.getHipRoll(), 7f)
						  .add(RKneePitch, param.getKneePitch(), 7f)
						  .add(RFootPitch, param.getFootPitch(), param.getFootPitchSpeed()));

		getUp.add(new MovementPhase("phase4", param.getTime4())
						  .add(LShoulderPitch, 0, 7f)
						  .add(LHipYawPitch, 0, 7f)
						  .add(LHipPitch, 35, 7f)
						  .add(LHipRoll, 0, 7f)
						  .add(LKneePitch, -70, 7f)
						  .add(LFootPitch, 35, 5)

						  .add(RShoulderPitch, 0, 7f)
						  .add(RHipYawPitch, 0, 7f)
						  .add(RHipPitch, 35, 7f)
						  .add(RHipRoll, 0, 7f)
						  .add(RKneePitch, -70, 7f)

						  .add(RFootPitch, 35, 5));
		return getUp;
	}

	public GetUpFromBack(IRoboCupThoughtModel thoughtModel, ParameterMap params)
	{
		super(IBehaviorConstants.GET_UP_BACK, thoughtModel, createMovement(params));

		stretch = new Movement("stretch");

		// skipWhenFinished instead of fixed # of cycles to get same starting
		// conditions each time
		stretch.add(new MovementPhase("phase1", 50)
							.add(NeckPitch, 0, 7f)
							.add(NeckYaw, 0, 7f)
							.add(LShoulderPitch, -90, 7f)
							.add(LShoulderYaw, 0, 7f)
							.add(LArmYaw, 0, 7f)
							.add(LArmRoll, 0, 7f)

							.add(LHipYawPitch, 0, 7f)
							.add(LHipPitch, 0, 7f)
							.add(LHipRoll, 0, 7f)
							.add(LKneePitch, 0, 7f)
							.add(LFootPitch, 0, 7f)
							.add(LFootRoll, 0, 7f)

							.add(RShoulderPitch, -90, 7f)
							.add(RShoulderYaw, 0, 7f)
							.add(RArmYaw, 0, 7f)
							.add(RArmRoll, 0, 7f)

							.add(RHipYawPitch, 0, 7f)
							.add(RHipPitch, 0, 7f)
							.add(RHipRoll, 0, 7f)
							.add(RKneePitch, 0, 7f)
							.add(RFootPitch, 0, 7f)
							.add(RFootRoll, 0, 7f));
	}

	@Override
	protected Movement getNextMovement()
	{
		if (currentMovement == initialMovement) {
			if ((getConsecutivePerforms() % 5) == 0 && getConsecutivePerforms() > 5) {
				return stretch;
			}
		}
		return null;
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		IBehavior realBehavior = actualBehavior.getRootBehavior();

		if (realBehavior instanceof IKeepBehavior) {
			// don't switch directly from keep behaviors
			return super.switchFrom(actualBehavior);
		}

		// by default allow switching to this behavior immediately, since the get
		// up from back has the highest priority
		if (actualBehavior.isFinished()) {
			actualBehavior.onLeavingBehavior(this);
		} else {
			actualBehavior.abort();
		}

		return this;
	}
}
