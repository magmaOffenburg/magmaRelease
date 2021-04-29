/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk;

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import java.util.List;
import kdo.util.parameter.IParameterList;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.decision.behavior.base.KickDistribution;
import magma.agent.decision.behavior.base.KickWalkDecider;
import magma.agent.decision.behavior.base.KickWalkEstimator;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.nao.INaoConstants;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Kick ball while walking.
 *
 * @author Klaus Dorer
 */
public class KickWalkStanding extends KickWalk implements INaoConstants
{
	private static final String NAME = IBehaviorConstants.KICK_WALK_STANDING.BASE_NAME;

	protected static final double MAX_KICK_DISTANCE = 7;
	protected static final float KICK_PRIORITY = 100;

	public static KickWalkStanding instance(Side side, IRoboCupThoughtModel thoughtModel, ParameterMap params,
			float opponentMaxDistance, IWalkEstimator walkEstimator)
	{
		ParameterListComposite p = (ParameterListComposite) params.get(NAME);
		List<IParameterList> list = p.getList();
		List<PositionMovement> move = createMovement(list, thoughtModel);
		KickDistribution distribution = ((KickWalkParameters) list.get(0)).getDistribution();
		KickWalkStanding kickWalk = new KickWalkStanding(
				side, NAME, thoughtModel, move, distribution, walkEstimator, list, opponentMaxDistance);

		return kickWalk;
	}

	protected KickWalkStanding(Side side, String baseName, IRoboCupThoughtModel thoughtModel,
			List<PositionMovement> movements, KickDistribution distribution, IWalkEstimator walkEstimator,
			List<IParameterList> list, float opponentMaxDistance)
	{
		super(side, baseName, thoughtModel, movements, distribution, walkEstimator, list);
		KickWalkEstimator kickWalkEstimator = new KickWalkEstimator(thoughtModel, walkEstimator, this);
		kickDecider = new KickWalkDecider(kickWalkEstimator, distribution,
				side == Side.LEFT ? SupportFoot.LEFT : SupportFoot.RIGHT,
				new Pose2D(-0.16, side == Side.LEFT ? -0.03 : 0.03), new Vector2D(0.002, 0), Angle.ZERO, Angle.ZERO,
				MAX_KICK_DISTANCE, MAX_KICK_DISTANCE, -10, opponentMaxDistance, 0.007f, 0f, 0.005f, 6, false,
				KICK_PRIORITY, //
				// Kick Walk parameters
				getKickableArea(side, list), true, 5, 1, 0.990);
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		// we only switch from walk
		if (actualBehavior.getRootBehavior().getName().equals(IBehaviorConstants.IK_WALK)) {
			actualBehavior.abort();
			return this;
		} else {
			return actualBehavior;
		}
	}
}
