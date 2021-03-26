/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose6D;
import kdo.util.parameter.IParameterList;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBehaviorConstants.SidedBehaviorConstants;
import magma.agent.decision.behavior.IKick;
import magma.agent.decision.behavior.IKickDecider;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.decision.behavior.base.KickEstimator;
import magma.agent.decision.behavior.ikMovement.KickMovementParameters.Param;
import magma.agent.decision.behavior.ikMovement.balancing.IKBalanceOnLegMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKFinalBallStepMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKGetOnLegStepMovement;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author Stefan Glaser
 */
public class IKKickBehavior extends IKMovementBehaviorBase implements IKick
{
	/** The movement for the final step. */
	protected final IKFinalBallStepMovement finalStepMvmt;

	/** The movement to get on one leg after the final step */
	protected final IKGetOnLegStepMovement getOnLegMvmt;

	/**
	 * The movement to balance on the leg, which we just got on via the
	 * {@link #getOnLegMvmt} and to reach the balancing target posture.
	 */
	protected final IKBalanceOnLegMovement balanceOnLegMvmt;

	/**
	 * The movement which finally kicks, after we successfully balanced on one
	 * leg and reached the initial posture.
	 */
	protected IIKMovement kickMvmt;

	protected transient IKKickDecider kickDecider;

	/**
	 * @param name The name of the kick behavior.
	 * @param thoughtModel the thought model.
	 * @param kickingFoot The kicking foot.
	 * @param relativeSupportFootPose The target pose of the stabilizing foot
	 *        relative to the ball and intended kick direction. This pose is used
	 *        to calculate the parameters of the final step in order to place the
	 *        stabilizing foot accurately before kicking. (global system)
	 * @param supportFootBalancingPose The target balancing pose of the support
	 *        foot. This pose is used as target pose for the support foot during
	 *        the balancing process (after the final step is performed). Together
	 *        with the freeFootBalancingPose and the balancingLeaning vector,
	 *        this pose defines an initial body posture.
	 * @param freeFootBalancingPose The target balancing pose of the free foot.
	 *        This pose is used as target pose for the free foot during the
	 *        balancing process (after the final step is performed). Together
	 *        with the supportFootBalancingPose and the balancingLeaning vector,
	 *        this pose defines an initial body posture.
	 * @param balancingLeaning The target balancing leaning. This leaning vector
	 *        is used as the intended leaning during the balancing process (after
	 *        the final step is performed). Together with the
	 *        supportFootBalancingPose and the freeFootBalancingPose, this vector
	 *        defines an initial body posture.
	 * @param relativeRunToPose The pose relative to the ball and intended kick
	 *        direction, to which we should navigate in order to be able to
	 *        perform this kick. (global system)
	 * @param relativeKickDirection The relative kick direction when stabilized
	 *        (used to rotate the relativeSupportFootPose and relativeRunToPose
	 *        with respect to the relative kick direction)
	 * @param maxKickDistance The maximum kick distance.
	 * @param kickMvmt The kick movement.
	 * @param stabilizeTime time for stabilization phase in cycles
	 */
	public IKKickBehavior(String name, IRoboCupThoughtModel thoughtModel, SupportFoot kickingFoot,
			Pose2D relativeSupportFootPose, Pose6D supportFootBalancingPose, Pose6D freeFootBalancingPose,
			Vector3D balancingLeaning, Pose2D relativeRunToPose, Angle relativeKickDirection, double maxKickDistance,
			IIKMovement kickMvmt, int stabilizeTime, float opponentMinDistance, int ballHitCycles, float minXOffset,
			IParameterList params, IWalkEstimator walkEstimator)
	{
		super(name, thoughtModel);

		Pose2D kickDirPose = new Pose2D(0, 0, relativeKickDirection);

		this.kickMvmt = kickMvmt;
		finalStepMvmt = new IKFinalBallStepMovement(
				thoughtModel, kickDirPose.applyInverseTo(relativeSupportFootPose), kickingFoot, params);
		getOnLegMvmt = new IKGetOnLegStepMovement(thoughtModel, params);
		balanceOnLegMvmt = new IKBalanceOnLegMovement(
				thoughtModel, supportFootBalancingPose, freeFootBalancingPose, balancingLeaning, stabilizeTime, 0);
		KickEstimator kickEstimator = new KickEstimator(thoughtModel, walkEstimator, this);
		kickDecider = new IKKickDecider(kickEstimator, null, minXOffset, kickingFoot, relativeRunToPose,
				new Vector2D(0.02, 0), relativeKickDirection, Angle.ZERO, maxKickDistance, maxKickDistance,
				opponentMinDistance, IKKickDecider.DEFAULT_MAX_OPP_DISTANCE, 0.007f, 0, 100, ballHitCycles, false,
				1000);
	}

	@Override
	public void init()
	{
		super.init();
		currentMovement = null;
	}

	@Override
	public void preDecisionUpdate()
	{
		// calculate stabilizing foot target pose
		finalStepMvmt.setSupportFoot(kickDecider.getKickingFoot());
		finalStepMvmt.setIntendedKickDirection(kickDecider.getKickDirection());
		Pose2D targetPose =
				finalStepMvmt.calculateStabilizationLegTargetPose(kickDecider.getKickEstimator().getBallPosAtKick());
		kickDecider.setTargetPose(targetPose);
	}

	@Override
	public IKickDecider getKickDecider()
	{
		return kickDecider;
	}

	@Override
	protected IIKMovement decideNextMovement()
	{
		IIKMovement nextMovement;

		if (currentMovement == null) {
			// Do final step
			finalStepMvmt.setSupportFoot(kickDecider.getKickingFoot());
			finalStepMvmt.setIntendedKickDirection(kickDecider.getIntendedKickDirection());

			nextMovement = finalStepMvmt;
		} else if (currentMovement == finalStepMvmt) {
			// Do get on leg
			nextMovement = getOnLegMvmt;
		} else if (currentMovement == getOnLegMvmt) {
			// Do balance on leg
			nextMovement = balanceOnLegMvmt;
		} else if (kickMvmt != null) {
			// Do kick
			nextMovement = kickMvmt;
		} else {
			nextMovement = currentMovement;
		}

		return nextMovement;
	}

	@Override
	public boolean isFinished()
	{
		if (currentMovement == null || !currentMovement.isFinished()) {
			return false;
		}

		return (currentMovement == kickMvmt) || (kickMvmt == null && currentMovement == balanceOnLegMvmt);
	}

	public int getStabilizeCycles()
	{
		return finalStepMvmt.getMovementCycles() + getOnLegMvmt.getMovementCycles() +
				balanceOnLegMvmt.getMovementCycles();
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		IBehavior realBehavior = actualBehavior.getRootBehavior();

		if (realBehavior instanceof IKWalkBehavior) {
			SupportFoot nextSupportFoot = ((IKWalkBehavior) realBehavior).getCurrentMovement().getNextSupportFoot();

			if (realBehavior.isFinished() && nextSupportFoot == kickDecider.getKickingFoot()) {
				double upright = getWorldModel().getThisPlayer().getUpVectorZ();
				if (upright > 0.99) {
					actualBehavior.onLeavingBehavior(this);
					return this;
				} else {
					return actualBehavior;
				}
			} else {
				return actualBehavior;
			}
		}

		return super.switchFrom(actualBehavior);
	}

	public static IKKickBehavior getKickStabilizationLeft(String kickName, SidedBehaviorConstants stabilize,
			IRoboCupThoughtModel thoughtModel, ParameterMap params, float opponentMinDistance, float maxKickDistance,
			IWalkEstimator walkEstimator)
	{
		KickMovementParameters kickParams = (KickMovementParameters) params.get(kickName);
		StabilizeParams stabilizeParams = StabilizeParams.getLeftKickStraightParams();

		return new IKKickBehavior(stabilize.LEFT, thoughtModel, SupportFoot.LEFT,
				new Pose2D(kickParams.getPosY(), -kickParams.getPosX()),
				new Pose6D(stabilizeParams.supportFootStabilizationPosition),
				new Pose6D(stabilizeParams.freeFootTargetPosition, stabilizeParams.freeFootTargetAngles),
				new Rotation(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR,
						Math.toRadians(stabilizeParams.intendedTargetLeaningForwards),
						Math.toRadians(stabilizeParams.intendedTargetLeaningSidewards), 0)
						.applyTo(Vector3D.PLUS_K),
				new Pose2D(kickParams.get(Param.RUN_TO_X), -kickParams.get(Param.RUN_TO_Y)),
				Angle.deg(-kickParams.getKickAngle()), maxKickDistance, null,
				(int) kickParams.get(Param.STABILIZE_TIME), opponentMinDistance, 1, kickParams.get(Param.MIN_X_OFFSET),
				params.get(stabilize.BASE_NAME), walkEstimator);
	}

	public static IKKickBehavior getKickStabilizationRight(String kickName, SidedBehaviorConstants stabilize,
			IRoboCupThoughtModel thoughtModel, ParameterMap params, float opponentMinDistance, float maxKickDistance,
			IWalkEstimator walkEstimator)
	{
		KickMovementParameters kickParams = (KickMovementParameters) params.get(kickName);
		StabilizeParams stabilizeParams = StabilizeParams.getRightKickStraightParams();

		return new IKKickBehavior(stabilize.RIGHT, thoughtModel, SupportFoot.RIGHT,
				new Pose2D(kickParams.getPosY(), kickParams.getPosX()),
				new Pose6D(stabilizeParams.supportFootStabilizationPosition),
				new Pose6D(stabilizeParams.freeFootTargetPosition, stabilizeParams.freeFootTargetAngles),
				new Rotation(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR,
						Math.toRadians(stabilizeParams.intendedTargetLeaningForwards),
						Math.toRadians(stabilizeParams.intendedTargetLeaningSidewards), 0)
						.applyTo(Vector3D.PLUS_K),
				new Pose2D(kickParams.get(Param.RUN_TO_X), kickParams.get(Param.RUN_TO_Y)),
				Angle.deg(kickParams.getKickAngle()), maxKickDistance, null, (int) kickParams.get(Param.STABILIZE_TIME),
				opponentMinDistance, 1, kickParams.get(Param.MIN_X_OFFSET), params.get(stabilize.BASE_NAME),
				walkEstimator);
	}
}
