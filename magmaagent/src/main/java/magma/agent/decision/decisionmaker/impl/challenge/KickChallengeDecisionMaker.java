/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.decisionmaker.impl.challenge;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import java.awt.*;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.decisionmaker.impl.SoccerDecisionMaker;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class KickChallengeDecisionMaker extends SoccerDecisionMaker
{
	private static final float PENALTY_CIRCLE_RADIUS = 2f;

	private static final float SAFETY_DISTANCE = 0.5f;

	private int localizationDelay = 30;

	private Vector3D initialBallPosition;

	public KickChallengeDecisionMaker(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
	}

	@Override
	protected String beamHome()
	{
		return null;
	}

	@Override
	protected String move()
	{
		if (localizationDelay >= 0) {
			localizationDelay--;
			return null;
		}
		if (initialBallPosition == null) {
			initialBallPosition = getWorldModel().getBall().getPosition();
			getThoughtModel().getRoboVizDraw().drawPoint("initialBallPos", initialBallPosition, 3, Color.blue);
		}

		if (ballLeftPenaltyCircle()) {
			return IBehaviorConstants.STOP_INSTANTLY;
		}
		return IBehaviorConstants.KICK_CHALLENGE_ATTACK;
	}

	/** there's a score penalty for leaving this circle */
	private boolean ballLeftPenaltyCircle()
	{
		if (initialBallPosition == null) {
			return false;
		}
		IMoveableObject ball = getWorldModel().getBall();
		double distance = ball.getDistanceToXY(initialBallPosition);
		return distance > (PENALTY_CIRCLE_RADIUS - SAFETY_DISTANCE);
	}
}
