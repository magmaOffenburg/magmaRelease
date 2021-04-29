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
import magma.agent.decision.behavior.base.KickWalkDecider;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class KickWalkSide extends KickWalkStanding
{
	private static final String NAME = IBehaviorConstants.KICK_WALK_SIDE.BASE_NAME;

	protected static final double MAX_KICK_DISTANCE = 5;

	public static KickWalkSide instance(Side side, IRoboCupThoughtModel thoughtModel, ParameterMap params,
			float opponentMaxDistance, IWalkEstimator walkEstimator)
	{
		ParameterListComposite p = (ParameterListComposite) params.get(NAME);
		List<IParameterList> list = p.getList();
		List<PositionMovement> move = createMovement(list, thoughtModel);
		KickWalkSide kickWalk =
				new KickWalkSide(side, NAME, thoughtModel, move, null, walkEstimator, list, opponentMaxDistance);

		return kickWalk;
	}

	protected KickWalkSide(Side side, String baseName, IRoboCupThoughtModel thoughtModel,
			List<PositionMovement> movements, KickDistribution distribution, IWalkEstimator walkEstimator,
			List<IParameterList> list, float opponentMaxDistance)
	{
		super(side, baseName, thoughtModel, movements, distribution, walkEstimator, list, opponentMaxDistance);
		kickDecider = new KickWalkDecider(kickDecider.getKickEstimator(), distribution,
				side == Side.LEFT ? SupportFoot.LEFT : SupportFoot.RIGHT,
				side == Side.LEFT ? new Pose2D(-0.15, -0.05) : new Pose2D(-0.15, 0.05), new Vector2D(0.002, 0),
				side == Side.LEFT ? Angle.deg(-90) : Angle.ANGLE_90, Angle.ZERO, MAX_KICK_DISTANCE, MAX_KICK_DISTANCE,
				-10, opponentMaxDistance, 0.007f, 0f, 0.005f, 6, false, 100, //
				// Kick Walk parameters
				getKickableArea(side, list), true, 5, 1, 0.990);
	}
}
