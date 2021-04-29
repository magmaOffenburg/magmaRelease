/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk;

import static magma.agent.IHumanoidJoints.LArmRoll;
import static magma.agent.IHumanoidJoints.LArmYaw;
import static magma.agent.IHumanoidJoints.LShoulderPitch;
import static magma.agent.IHumanoidJoints.LShoulderYaw;
import static magma.agent.IHumanoidJoints.RArmRoll;
import static magma.agent.IHumanoidJoints.RArmYaw;
import static magma.agent.IHumanoidJoints.RShoulderPitch;
import static magma.agent.IHumanoidJoints.RShoulderYaw;

import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.Pose2D;
import java.util.ArrayList;
import java.util.List;
import kdo.util.parameter.IParameterList;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IKick;
import magma.agent.decision.behavior.IKickDecider;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.decision.behavior.base.KickDistribution;
import magma.agent.decision.behavior.base.KickWalkDecider;
import magma.agent.decision.behavior.base.KickWalkEstimator;
import magma.agent.decision.behavior.movement.Movement;
import magma.agent.decision.behavior.movement.SidedMovementBehavior;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.nao.decision.behavior.movement.fullsearch.FullSearchMovementFactory;
import magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk.KickWalkParameters.CustomParam;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Kick ball while walking.
 *
 * @author Klaus Dorer
 */
public class KickWalk extends SidedMovementBehavior implements IKick
{
	protected static final String NAME = IBehaviorConstants.KICK_WALK.BASE_NAME;

	protected static final double MAX_KICK_DISTANCE = 4;

	protected transient IKickDecider kickDecider;

	protected List<PositionMovement> movements;

	public static KickWalk instance(
			Side side, IRoboCupThoughtModel thoughtModel, ParameterMap params, IWalkEstimator walkEstimator)
	{
		ParameterListComposite p = (ParameterListComposite) params.get(NAME);
		List<IParameterList> list = p.getList();
		List<PositionMovement> move = createMovement(list, thoughtModel);
		KickDistribution distribution = ((KickWalkParameters) list.get(0)).getDistribution();
		return new KickWalk(side, NAME, thoughtModel, move, distribution, walkEstimator, list);
	}

	protected static List<PositionMovement> createMovement(
			List<IParameterList> parameterLists, IThoughtModel thoughtModel)
	{
		List<PositionMovement> movements = new ArrayList<>();

		for (IParameterList params : parameterLists) {
			KickWalkParameters p = (KickWalkParameters) params;
			Movement movement = FullSearchMovementFactory.create(p, thoughtModel);

			// first phase also has arm movement
			float armSpeed = 4f;
			if (movement.getPhases().size() > 0) {
				movement.getPhases()
						.get(0)								//
						.add(LShoulderPitch, -60, armSpeed) //
						.add(LShoulderYaw, 0, armSpeed)		//
						.add(LArmRoll, 0, armSpeed)			//
						.add(LArmYaw, 0, armSpeed)			//
						.add(RShoulderPitch, -60, armSpeed) //
						.add(RShoulderYaw, 0, armSpeed)		//
						.add(RArmRoll, 0, armSpeed)			//
						.add(RArmYaw, 0, armSpeed);
			}

			movements.add(new PositionMovement(p.ballPos, movement));
		}

		return movements;
	}

	protected KickWalk(Side side, String baseName, IRoboCupThoughtModel thoughtModel, List<PositionMovement> movements,
			KickDistribution distribution, IWalkEstimator walkEstimator, List<IParameterList> list)
	{
		super(side, baseName, thoughtModel, movements.get(0).getMovement());

		this.movements = movements;
		KickWalkEstimator kickWalkEstimator = new KickWalkEstimator(thoughtModel, walkEstimator, this);
		kickDecider = new KickWalkDecider(kickWalkEstimator, distribution,
				side == Side.LEFT ? SupportFoot.LEFT : SupportFoot.RIGHT, new Pose2D(-0.12, 0), Vector2D.ZERO,
				Angle.ZERO, Angle.ZERO, MAX_KICK_DISTANCE, MAX_KICK_DISTANCE, 0,
				KickWalkDecider.DEFAULT_MAX_OPP_DISTANCE, 0.007f, 0f, 100f, 1, false, 10, getKickableArea(side, list),
				false, 0, 100, 0.0);
	}

	protected static Area2D.Float getKickableArea(Side side, List<IParameterList> list)
	{
		// we take the first of a list to look up kickable area
		KickWalkParameters param = (KickWalkParameters) list.get(0);
		float minX = param.get(CustomParam.MIN_X_KICK);
		float maxX = param.get(CustomParam.MAX_X_KICK);
		float maxY = param.get(CustomParam.MAX_Y_KICK);

		if (side == Side.LEFT) {
			return new Area2D.Float(minX, maxX, 0, maxY);
		}
		return new Area2D.Float(minX, maxX, -maxY, 0);
	}

	@Override
	public IKickDecider getKickDecider()
	{
		return kickDecider;
	}

	@Override
	public void perform()
	{
		if (justStarted) {
			// mirror all left sided behaviors
			if (flipSides) {
				for (PositionMovement posMovement : movements) {
					posMovement.setMovement(posMovement.getMovement().getLeftVersion());
					Vector3D ballPos = posMovement.getBallPos();
					posMovement.setBallPosition(new Vector3D(ballPos.getX(), -ballPos.getY(), ballPos.getZ()));
				}

				flipSides = false;
			}

			Vector3D localBallPosition =
					getWorldModel().getThisPlayer().calculateLocalPosition(getWorldModel().getBall().getPosition());
			double min = Double.MAX_VALUE;
			PositionMovement minMovement = null;
			for (PositionMovement movement : movements) {
				double distance = Vector3D.distance(movement.getBallPos(), localBallPosition);

				if (distance < min) {
					min = distance;
					minMovement = movement;
				}
			}
			initialMovement = minMovement.getMovement();
			currentMovement = initialMovement;
			// Which kick is used
			//			System.out.println("Team: " + getWorldModel().getThisPlayer().getTeamname() + " Choose kick: " +
			// getName() +
			//							   " ballPos: " + minMovement.getBallPos() + " ball pos: " + localBallPosition +
			//							   " kickableArea: " + kickableArea + "	 applicability: " + getApplicability() +
			//							   " utility: " + getKickUtility());
		}

		super.perform();
	}

	public PositionMovement getPositionMovement(int index)
	{
		return movements.get(index);
	}
}
