/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao3.decision.behavior.ikMovement.walk;

import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;

/**
 * Represents the parameter set necessary for walking with walk movements
 *
 * @author Stefan Glaser
 */
public class IKWalkMovementParametersNao3 extends IKWalkMovementParametersBase
{
	public IKWalkMovementParametersNao3()
	{
		super(IKDynamicWalkMovement.NAME);
	}

	@Override
	public void setValues(String name)
	{
		super.setValues(name);

		if (IKDynamicWalkMovement.NAME.equals(name)) {
			// Dynamic walk parameter set
			put(Param.WALK_HEIGHT, -0.275f);
			put(Param.MAX_STEP_LENGTH, 0.09f);
			put(Param.MAX_STEP_WIDTH, 0.08f);

		} else {
			// Static walk parameter set (default)
			put(Param.WALK_HEIGHT, -0.275f);
			put(Param.MAX_STEP_LENGTH, 0.09f);
			put(Param.MAX_STEP_WIDTH, 0.1f);
		}
	}
}
