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
 * This behavior will get up the agent when it lies on the front.<br>
 * Just don't ask why...
 *
 * @author Stefan Glaser
 *
 */
public class GetUpFromFront extends MovementBehavior implements INaoJoints
{
	/** movement to get on back */
	protected Movement salto;

	/** old getup movement */
	protected Movement oldGetup;

	private static Movement createMovement(ParameterMap params)
	{
		GetUpFromFrontParameters param = (GetUpFromFrontParameters) params.get(IBehaviorConstants.GET_UP_FRONT);

		Movement getUp = new Movement("getupFront");
		getUp.add(new MovementPhase("phase1", param.getTime1())
						  .add(LShoulderPitch, 50, 7)
						  .add(RShoulderPitch, 50, 7)
						  .add(LArmYaw, 0, 7)
						  .add(RArmYaw, 0, 7)
						  .add(LHipPitch, 0, 7)
						  .add(RHipPitch, 0, 7)
						  .add(LKneePitch, 0, 7)
						  .add(RKneePitch, 0, 7)
						  .add(LFootPitch, 0, 7)
						  .add(RFootPitch, 0, 7)
						  .add(LFootPitch, 0, 7)
						  .add(RFootPitch, 0, 7));

		getUp.add(new MovementPhase("phase2", param.getTime2())
						  .add(LHipPitch, 100, 7)
						  .add(RHipPitch, 100, 7)
						  .add(LKneePitch, -130, 7)
						  .add(RKneePitch, -130, 7)
						  .add(LFootPitch, 75, 7)
						  .add(RFootPitch, 75, 7)
						  .add(RShoulderPitch, 50, 7)
						  .add(LShoulderPitch, 50, 7));

		getUp.add(new MovementPhase("phase3", param.getTime3())
						  .add(LHipRoll, 45, 7)
						  .add(RHipRoll, -45, 7)
						  .add(LFootRoll, -45, 7)
						  .add(RFootRoll, 45, 7)
						  .add(LHipYawPitch, -60, 7)
						  .add(RHipYawPitch, -60, 7)
						  .add(LKneePitch, -130, 7)
						  .add(RKneePitch, -130, 7)
						  .add(RShoulderPitch, 50, 7)
						  .add(LShoulderPitch, 50, 7));

		getUp.add(new MovementPhase("phase4", param.getTime4())
						  .add(LHipYawPitch, -90, 7)
						  .add(RHipYawPitch, -90, 7)
						  .add(LKneePitch, -100, 7)
						  .add(RKneePitch, -100, 7)
						  .add(LFootPitch, -45, 7)
						  .add(RFootPitch, -45, 7)
						  .add(LFootRoll, -25, 7)
						  .add(RFootRoll, 25, 7));

		getUp.add(new MovementPhase("phase5", param.getTime5()) //
						  .add(RShoulderPitch, 0, 7)
						  .add(LShoulderPitch, 0, 7));

		getUp.add(new MovementPhase("phase6", param.getTime6())
						  .add(LHipYawPitch, -60, 7)
						  .add(RHipYawPitch, -60, 7)
						  .add(LKneePitch, -120, 7)
						  .add(RKneePitch, -120, 7)
						  .add(LFootPitch, 45, 7)
						  .add(RFootPitch, 45, 7));

		getUp.add(new MovementPhase("phase7", param.getTime7())
						  .add(LHipPitch, 100, 7)
						  .add(RHipPitch, 100, 7)
						  .add(LKneePitch, -120, 7)
						  .add(RKneePitch, -120, 7)
						  .add(LFootPitch, 45, 7)
						  .add(RFootPitch, 45, 7)
						  .add(LHipRoll, 45, 7)
						  .add(RHipRoll, -45, 7)
						  .add(LFootRoll, -45, 7)
						  .add(RFootRoll, 45, 7)
						  .add(LHipYawPitch, -60, 7)
						  .add(RHipYawPitch, -60, 7)
						  .add(LShoulderPitch, 0, 7)
						  .add(RShoulderPitch, 0, 7));

		getUp.add(new MovementPhase("phase8", param.getTime8())
						  .add(LHipPitch, param.getHipPitch(), 7)
						  .add(RHipPitch, param.getHipPitch(), 7)
						  .add(LKneePitch, param.getKneePitch(), 7)
						  .add(RKneePitch, param.getKneePitch(), 7)
						  .add(LFootPitch, param.getFootPitch(), 7)
						  .add(RFootPitch, param.getFootPitch(), 7)
						  .add(LHipRoll, param.getHipRoll(), 7)
						  .add(RHipRoll, -param.getHipRoll(), 7)
						  .add(LFootRoll, param.getFootRoll(), 7)
						  .add(RFootRoll, -param.getFootRoll(), 7)
						  .add(LHipYawPitch, param.getHipYawPitch(), 7)
						  .add(RHipYawPitch, param.getHipYawPitch(), 7)
						  .add(LShoulderPitch, 0, 7)
						  .add(RShoulderPitch, 0, 7));

		getUp.add(new MovementPhase("phase9", param.getTime9())
						  .add(LHipYawPitch, 0, 7)
						  .add(RHipYawPitch, 0, 7)
						  .add(LHipRoll, 0, 7)
						  .add(RHipRoll, 0, 7)
						  .add(LHipPitch, 28.44, 7)
						  .add(RHipPitch, 28.44, 7)
						  .add(LKneePitch, -46.33, 7)
						  .add(RKneePitch, -46.33, 7)
						  .add(LFootPitch, 31, 7)
						  .add(RFootPitch, 31, 7)
						  .add(LFootRoll, 0, 7)
						  .add(RFootRoll, 0, 7)
						  .add(LShoulderPitch, -90, 7)
						  .add(RShoulderPitch, -90, 7)
						  .add(LShoulderYaw, 0, 7)
						  .add(RShoulderYaw, 0, 7)
						  .add(LArmRoll, 0, 7)
						  .add(RArmRoll, 0, 7)
						  .add(LArmYaw, 0, 7)
						  .add(RArmYaw, 0, 7));
		return getUp;
	}

	private static Movement createMovementOld(ParameterMap params)
	{
		Movement getUp = new Movement("getupFront");
		getUp.add(new MovementPhase("phase1", 30)
						  .add(LShoulderPitch, 0, 7f)
						  .add(LShoulderYaw, -10, 7f)
						  .add(LArmYaw, 0, 7f)
						  .add(LArmRoll, 0, 7f)

						  .add(LHipYawPitch, 0, 7f)
						  .add(LHipPitch, 100, 7f)
						  .add(LHipRoll, 0, 7f)
						  .add(LKneePitch, -130, 7f)
						  .add(LFootPitch, 75, 7f)
						  .add(LFootRoll, 0, 7f)

						  .add(RShoulderPitch, 0, 7f)
						  .add(RShoulderYaw, 10, 7f)
						  .add(RArmYaw, 0, 7f)
						  .add(RArmRoll, 0, 7f)

						  .add(RHipYawPitch, 0, 7f)
						  .add(RHipPitch, 100, 7f)
						  .add(RHipRoll, 0, 7f)
						  .add(RKneePitch, -130, 7f)
						  .add(RFootPitch, 75, 7f)
						  .add(RFootRoll, 0, 7f));
		getUp.add(new MovementPhase("phase2", 20)
						  .add(LShoulderPitch, -20, 1)
						  .add(LHipYawPitch, -90, 4)
						  .add(RShoulderPitch, -20, 1)
						  .add(RHipYawPitch, -90, 4));
		getUp.add(new MovementPhase("phase3", 30)
						  .add(LShoulderPitch, -35, 1)
						  .add(LHipYawPitch, -90, 7f)
						  .add(LFootPitch, 0, 4)
						  .add(RShoulderPitch, -35, 1)
						  .add(RHipYawPitch, -90, 7f)
						  .add(RFootPitch, 0, 4));
		getUp.add(new MovementPhase("phase4", 25)
						  .add(LShoulderPitch, 0, 1)
						  .add(LHipYawPitch, 0, 1.4f)
						  .add(LHipPitch, 0, 1.0f)
						  .add(LHipRoll, 0, 1)
						  .add(LKneePitch, 0, 2)
						  .add(LFootPitch, 0, 1)
						  .add(RShoulderPitch, 0, 1)
						  .add(RHipYawPitch, 0, 1.4f)
						  .add(RHipPitch, 0, 1.0f)
						  .add(RHipRoll, 0, 1)
						  .add(RKneePitch, 0, 2)
						  .add(RFootPitch, 0, 1));

		getUp.add(new MovementPhase("phase5", 20)
						  .add(LShoulderPitch, 0, 3.5f)
						  .add(LHipYawPitch, 0, 4.5f)
						  .add(LHipPitch, 35, 4.5f)
						  .add(LHipRoll, 0, 3.5f)
						  .add(LKneePitch, -70, 7)
						  .add(LFootPitch, 35, 3)

						  .add(RShoulderPitch, 0, 3.5f)
						  .add(RHipYawPitch, 0, 4.5f)
						  .add(RHipPitch, 35, 4.5f)
						  .add(RHipRoll, 0, 3.5f)
						  .add(RKneePitch, -70, 7)
						  .add(RFootPitch, 35, 3));

		return getUp;
	}

	private Movement createSalto()
	{
		Movement salto = new Movement("salto");
		salto.add(new MovementPhase("phase1", 15)
						  .add(LShoulderPitch, 90, 7f)
						  .add(LShoulderYaw, -10, 7f)
						  .add(LArmYaw, 0, 7f)
						  .add(LArmRoll, 0, 7f)

						  .add(RShoulderPitch, 90, 7f)
						  .add(RShoulderYaw, 10, 7f)
						  .add(RArmYaw, 0, 7f)
						  .add(RArmRoll, 0, 7f));
		salto.add(new MovementPhase("phase2", 10)
						  .add(LShoulderPitch, 90, 7f)
						  .add(LShoulderYaw, -10, 7f)
						  .add(LArmYaw, 0, 7f)
						  .add(LArmRoll, 0, 7f)

						  .add(LHipYawPitch, 0, 7f)
						  .add(LHipPitch, 0, 7f)
						  .add(LHipRoll, 0, 7f)
						  .add(LKneePitch, 0, 7f)
						  .add(LFootPitch, 0, 7f)
						  .add(LFootRoll, 0, 7f)

						  .add(RShoulderPitch, 90, 7f)
						  .add(RShoulderYaw, 10, 7f)
						  .add(RArmYaw, 0, 7f)
						  .add(RArmRoll, 0, 7f)

						  .add(RHipYawPitch, 0, 7f)
						  .add(RHipPitch, 0, 7f)
						  .add(RHipRoll, 0, 7f)
						  .add(RKneePitch, 0, 7f)
						  .add(RFootPitch, 0, 7f)
						  .add(RFootRoll, 0, 7f));
		return salto;
	}

	public GetUpFromFront(IRoboCupThoughtModel thoughtModel, ParameterMap params)
	{
		super(IBehaviorConstants.GET_UP_FRONT, thoughtModel, createMovement(params));

		oldGetup = createMovementOld(params);
		salto = createSalto();
	}

	@Override
	public void perform()
	{
		if (!currentMovement.isFinished()) {
			// try to avoid standing on hands
			if (currentMovement == initialMovement) {
				double up = getWorldModel().getThisPlayer().getUpVectorZ();
				if (up < -0.6) {
					switchTo(salto);
				} else if (up < -0.5 || getConsecutivePerforms() > 2) {
					switchTo(oldGetup);
				}
			}
		}

		super.perform();
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		IBehavior realBehavior = actualBehavior.getRootBehavior();

		if (realBehavior instanceof GetUpFromBack || realBehavior instanceof IKeepBehavior) {
			// don't switch directly from get up back and keep behaviors
			return super.switchFrom(actualBehavior);
		}

		// by default allow switching to this behavior immediately
		if (actualBehavior.isFinished()) {
			actualBehavior.onLeavingBehavior(this);
		} else {
			actualBehavior.abort();
		}

		return this;
	}
}
