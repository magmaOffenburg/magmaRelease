/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.fullsearch.jointspacewalk;

import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalkBehaviorMarker;
import magma.agent.decision.behavior.movement.SidedMovementBehavior;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.nao.decision.behavior.movement.fullsearch.FullSearchMovementFactory;
import magma.robots.nao.decision.behavior.movement.fullsearch.FullSearchMovementParameters;

public class JointSpaceWalkMovement extends SidedMovementBehavior implements IWalkBehaviorMarker
{
	private static final String NAME = IBehaviorConstants.JOINT_SPACE_WALK_MOVEMENT.BASE_NAME;

	public JointSpaceWalkMovement(Side side, IRoboCupThoughtModel thoughtModel, ParameterMap params)
	{
		super(side, NAME, thoughtModel,
				FullSearchMovementFactory.create((FullSearchMovementParameters) params.get(NAME), thoughtModel));
	}
}
