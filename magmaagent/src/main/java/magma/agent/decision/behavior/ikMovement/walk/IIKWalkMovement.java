/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement.walk;

import magma.agent.decision.behavior.ikMovement.IIKMovement;
import magma.agent.decision.behavior.ikMovement.Step;

public interface IIKWalkMovement extends IIKMovement {
	/**
	 * @return the parameters of the walk
	 */
	IKWalkMovementParametersBase getWalkParameters();

	/**
	 * @return the currently performed step parameters
	 */
	Step getCurrentStep();

	/**
	 * Called to set the next step parameters.
	 *
	 * @param nextStep - the intended next step parameters
	 */
	IKStaticWalkMovement setNextStep(Step nextStep);

	/**
	 * @return the current internal speed assumption of the walk model. This
	 *         method is just for temporary use, as long as we don't have a
	 *         proper speed determination. The value returned is between 0
	 *         indicating no speed and 1 for full speed.
	 */
	double getSpeed();
}
