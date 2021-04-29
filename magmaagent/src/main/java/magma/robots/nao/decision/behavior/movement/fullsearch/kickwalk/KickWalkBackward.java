/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import java.util.List;
import kdo.util.parameter.IParameterList;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.decision.behavior.base.KickDistribution;
import magma.agent.decision.behavior.base.KickEstimator;
import magma.agent.decision.behavior.base.KickWalkDecider;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class KickWalkBackward extends KickWalkStanding
{
	private static final String NAME = IBehaviorConstants.KICK_WALK_BACKWARD.BASE_NAME;

	public static KickWalkBackward instance(Side side, IRoboCupThoughtModel thoughtModel, ParameterMap params,
			float opponentMaxDistance, IWalkEstimator walkEstimator)
	{
		ParameterListComposite p = (ParameterListComposite) params.get(NAME);
		List<IParameterList> list = p.getList();
		List<PositionMovement> move = createMovement(list, thoughtModel);
		KickDistribution distribution = ((KickWalkParameters) list.get(0)).getDistribution();

		KickWalkBackward kickWalk = new KickWalkBackward(
				side, NAME, thoughtModel, move, distribution, walkEstimator, list, opponentMaxDistance);

		return kickWalk;
	}

	protected KickWalkBackward(Side side, String baseName, IRoboCupThoughtModel thoughtModel,
			List<PositionMovement> movements, KickDistribution distribution, IWalkEstimator walkEstimator,
			List<IParameterList> list, float opponentMaxDistance)
	{
		super(side, baseName, thoughtModel, movements, distribution, walkEstimator, list, opponentMaxDistance);
		KickEstimator estimator = kickDecider.getKickEstimator();
		kickDecider = new KickWalkDecider(estimator, distribution,
				side == Side.LEFT ? SupportFoot.LEFT : SupportFoot.RIGHT,
				new Pose2D(0.15, side == Side.LEFT ? -0.03 : 0.03), new Vector2D(0.002, 0), Angle.deg(-180), Angle.ZERO,
				7, 7, -10, opponentMaxDistance, 0.007f, 0f, 0.005f, 6, false, KICK_PRIORITY - 0.4f, //
				// KickWalk part
				getKickableArea(side, list), true, 5, 1, 0.990);
	}
}
