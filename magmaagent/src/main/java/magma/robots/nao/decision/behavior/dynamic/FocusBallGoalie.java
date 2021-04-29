/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.dynamic;

import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.basic.RoboCupBehavior;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class FocusBallGoalie extends RoboCupBehavior
{
	public FocusBallGoalie(IRoboCupThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.FOCUS_BALL_GOALIE, thoughtModel);
	}

	@Override
	public void perform()
	{
		super.perform();
		Vector3D ballPos = getWorldModel().getBall().getLocalPosition();
		FocusBall.focusBall(getAgentModel(), ballPos);
	}
}
