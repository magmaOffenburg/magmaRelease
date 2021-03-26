/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.naoGazebo.general.agentruntime;

import kdo.util.parameter.ParameterMap;
import magma.robots.nao.general.agentruntime.NaoComponentFactory;
import magma.robots.naoGazebo.decision.behavior.ikMovement.walk.IKWalkMovementParametersNaoGazebo;

public class NaoGazeboComponentFactory extends NaoComponentFactory
{
	public static final String NAME = "NaoGazebo";

	@Override
	protected ParameterMap createSpecificParameters()
	{
		ParameterMap result = super.createSpecificParameters();
		result.put(IK_MOVEMENT, new IKWalkMovementParametersNaoGazebo());
		return result;
	}
}
