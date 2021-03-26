/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.naoGazebo.decision.behavior.ikMovement.walk;

import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;

public class IKWalkMovementParametersNaoGazebo extends IKWalkMovementParametersBase
{
	@Override
	public void setValues(String name)
	{
		super.setValues(name);

		put(Param.DYNAMIC_WALK, 0);
		put(Param.CYCLE_PER_STEP, 13);
		put(Param.SWING_ARMS, 0);
		put(Param.WALK_WIDTH, 0.045f);
		put(Param.PUSHDOWN_FACTOR, 0);
		put(Param.SAGGITAL_ADJUSTMENT_FACTOR, 0f);
		put(Param.CORONAL_ADJUSTMENT_FACTOR, 0f);
	}
}
