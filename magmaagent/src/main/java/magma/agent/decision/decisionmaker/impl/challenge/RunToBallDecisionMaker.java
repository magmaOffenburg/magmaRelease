/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.decisionmaker.impl.challenge;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.PoseSpeed2D;
import magma.agent.decision.behavior.IBeam;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.complex.walk.WalkToPosition;
import magma.agent.decision.decisionmaker.impl.SoccerDecisionMaker;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class RunToBallDecisionMaker extends SoccerDecisionMaker
{
	public RunToBallDecisionMaker(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
	}

	@Override
	protected String beamHome()
	{
		if (getThoughtModel().shouldBeam()) {
			((IBeam) behaviors.get(IBehaviorConstants.BEAM_TO_POSITION)).setPose(new Pose2D(-10, 0));
			return IBehaviorConstants.BEAM_TO_POSITION;
		}
		return null;
	}

	@Override
	protected String move()
	{
		Vector3D ballPos = getWorldModel().getBall().getPosition();
		WalkToPosition walkToPosition = (WalkToPosition) behaviors.get(IBehaviorConstants.WALK_TO_POSITION);
		walkToPosition.setPosition(new PoseSpeed2D(new Pose2D(ballPos), new Vector2D(0.6, 0)), 100, false, 0.8);
		return walkToPosition.getName();
	}
}
