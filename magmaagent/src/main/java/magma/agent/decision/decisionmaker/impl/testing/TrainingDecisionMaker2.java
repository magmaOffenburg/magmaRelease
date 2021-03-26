/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.decisionmaker.impl.testing;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.util.geometry.Pose2D;
import magma.agent.decision.behavior.IBeam;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

/**
 * Decision maker used to trigger training behaviors. Feel free to change it for
 * your own convenience.
 *
 * @author Klaus Dorer
 */
public class TrainingDecisionMaker2 extends TrainingDecisionMaker
{
	public TrainingDecisionMaker2(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
	}

	@Override
	protected String beamHome()
	{
		if (!haveBeamed) {
			haveBeamed = true;
			((IBeam) behaviors.get(IBehaviorConstants.BEAM_TO_POSITION)).setPose(new Pose2D(-4.0f, 0.0f));
			return IBehaviorConstants.BEAM_TO_POSITION;
		}
		return null;
	}

	@Override
	protected String performFocusBall()
	{
		return null;
	}

	@Override
	protected String move()
	{
		return IBehaviorConstants.JOINT_SPACE_WALK;
	}
}
