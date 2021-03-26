/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement.experimental;

import magma.agent.decision.behavior.ikMovement.walk.IKStaticWalkMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase.Param;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class IKForwardMovementDemo extends IKStaticWalkMovement
{
	public IKForwardMovementDemo(IRoboCupThoughtModel thoughtModel, IKWalkMovementParametersBase params)
	{
		super("ForwardDemo", thoughtModel, params);

		setMovementCycles(10);

		params.put(Param.WALK_WIDTH, 0.065f);
		params.put(Param.WALK_HEIGHT, -0.255f);
		params.put(Param.WALK_OFFSET, 0.035f);
		params.put(Param.PUSHDOWN_FACTOR, 0.5f);
		params.put(Param.FOOT_SLANT_ANGLE, 5);

		currentStep.upward = 0.02;
		currentStep.forward = 0;

		// // NimbRo
		// setMovementCycles(12);
		// currentStep.z_targetDistance = 0.0;
		// params.put(Param.WALK_HEIGHT, -0.4f);
		// params.put(Param.WALK_OFFSET, -0.07f);
		// params.put(Param.WALK_WIDTH, 0.115f);
		// params.put(Param.PUSHDOWN_FACTOR, 0.1f);
		// params.put(Param.FOOT_SLANT_ANGLE, 0);
		// isStatic = false;

		// // Sweaty
		// setMovementCycles(17);
		// currentStep.z_targetDistance = 0.03;
		// params.put(Param.WALK_HEIGHT, -0.6f);
		// params.put(Param.MAX_STEP_LENGTH, 0.16f);
		// params.put(Param.MAX_STEP_WIDTH, 0.1f);
		// params.put(Param.WALK_WIDTH, 0.15f);
		// params.put(Param.CYCLE_PER_STEP, 20.0f);
		// params.put(Param.MAX_STEP_HEIGHT, 0.03f);
		// params.put(Param.ACCELERATION, 0.001f);
		// params.put(Param.SWING_ARMS, 0f);
		// params.put(Param.DYNAMIC_WALK, 0f);
		// params.put(Param.FOOT_SLANT_ANGLE, 0);
		// params.put(Param.WALK_OFFSET, 0.035f);
		// params.put(Param.PUSHDOWN_FACTOR, 0.4f);
	}

	@Override
	public boolean update()
	{
		// // NimbRo
		// if (currentStep.z_targetDistance < 0.018) {
		// currentStep.z_targetDistance += 0.0002;
		// }

		// // Sweaty
		// if (currentStep.y_targetDistance < 0.16) {
		// currentStep.y_targetDistance += 0.0005;
		// }

		if (currentStep.forward < 0.08) {
			currentStep.forward += 0.0005;
		}

		return super.update();
	}

	@Override
	public Vector3D getIntendedLeaningVector()
	{
		return Vector3D.PLUS_K;
		// return new Rotation(Vector3D.PLUS_I, Math.toRadians(-5))
		// .applyTo(Vector3D.PLUS_K);
	}
}
