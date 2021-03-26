/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.decisionmaker.impl.challenge;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.util.geometry.Area2D;
import java.awt.Color;
import magma.agent.decision.decisionmaker.impl.SoccerDecisionMaker;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.impl.KickPositionProfiler;
import magma.agent.model.thoughtmodel.strategy.impl.RoleManager;
import magma.agent.model.thoughtmodel.strategy.impl.strategies.KeepAwayChallengeStrategy;
import magma.common.challenge.KeepAwayArea;

public class KeepAwayChallengeDecisionMaker extends SoccerDecisionMaker
{
	private final KickPositionProfiler kickPositionProfiler;

	public KeepAwayChallengeDecisionMaker(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);

		kickPositionProfiler = new KickPositionProfiler(getThoughtModel());
		thoughtModel.setKickPositionProfiler(kickPositionProfiler);

		thoughtModel.setRoleManager(new RoleManager(getWorldModel(), new KeepAwayChallengeStrategy(thoughtModel)));
	}

	@Override
	public String decideNextBehavior()
	{
		Area2D.Float keepAwayArea = KeepAwayArea.calculate(getWorldModel().getGameTime());

		kickPositionProfiler.setPlayableArea(keepAwayArea.applyBorder(KickPositionProfiler.DEFAULT_AREA_BORDER));
		getThoughtModel().getRoboVizDraw().drawArea("keepAwayArea", keepAwayArea, 2, Color.RED);

		return super.decideNextBehavior();
	}
}
