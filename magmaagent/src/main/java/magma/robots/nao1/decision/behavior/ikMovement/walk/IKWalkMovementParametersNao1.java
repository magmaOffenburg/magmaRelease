/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao1.decision.behavior.ikMovement.walk;

import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;

/**
 * Represents the parameter set necessary for walking with walk movements
 *
 * @author Stefan Glaser
 */
public class IKWalkMovementParametersNao1 extends IKWalkMovementParametersBase
{
	public IKWalkMovementParametersNao1()
	{
		super(IKDynamicWalkMovement.NAME);
	}

	@Override
	public void setValues(String name)
	{
		super.setValues(name);

		// if (IKDynamicWalkMovement.NAME.equals(name)) {
		// // Dynamic walk parameter set
		// put(Param.WALK_HEIGHT, -0.275f);
		// put(Param.MAX_STEP_LENGTH, 0.09f);
		// put(Param.MAX_STEP_WIDTH, 0.08f);
		// } else {
		// // Static walk parameter set (default)
		// put(Param.WALK_HEIGHT, -0.275f);
		// put(Param.MAX_STEP_LENGTH, 0.09f);
		// put(Param.MAX_STEP_WIDTH, 0.1f);
		// }
		put(Param.CYCLE_PER_STEP, 11.0f);
		put(Param.WALK_HEIGHT, -0.275f);
		put(Param.WALK_WIDTH, 0.055000003f);
		put(Param.WALK_OFFSET, -0.003f);
		put(Param.MAX_STEP_LENGTH, 0.095000006f);
		put(Param.MAX_STEP_WIDTH, 0.08f);
		put(Param.MAX_STEP_HEIGHT, 0.021f);
		put(Param.MAX_TURN_ANGLE, 50.0f);
		put(Param.PUSHDOWN_FACTOR, 0.514f);
		put(Param.FOOT_SLANT_ANGLE, 0.0f);
		put(Param.MAX_FORWARD_LEANING, 0.0f);
		put(Param.MAX_SIDEWARDS_LEANING, 0.0f);
		put(Param.ACCELERATION, 0.0035f);
		put(Param.DECELERATION, 0.0035f);
		put(Param.TURN_ACCELERATION, 2.0f);
		put(Param.TURN_DECELERATION, 3.0f);
		put(Param.SWING_ARMS, 1.0f);
		put(Param.DYNAMIC_WALK, 1.0f);
		put(Param.SAGGITAL_ADJUSTMENT_FACTOR, 0.3f);
		put(Param.MAX_ABS_SAGGITAL_ADJUSTMENT, 100.0f);
		put(Param.CORONAL_ADJUSTMENT_FACTOR, 0.3f);
		put(Param.MAX_ABS_CORONAL_ADJUSTMENT, 100.0f);
	}
}
