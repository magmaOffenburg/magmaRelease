/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose6D;
import kdo.util.parameter.IParameterList;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.ikMovement.balancing.IKBalanceOnLegMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKCoMShiftingStepMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKGetOnLegStepMovement;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class IKStabilizeOnLegBehavior extends IKMovementBehaviorBase
{
	/** The free foot. */
	protected final SupportFoot freeFoot;

	/** The movement for the final step. */
	protected final IKCoMShiftingStepMovement finalStepMvmt;

	/** The movement to get on one leg after the final step */
	protected final IKGetOnLegStepMovement getOnLegMvmt;

	/**
	 * The movement to balance on the leg, which we just got on via the
	 * {@link #getOnLegMvmt} and to reach the balancing target posture.
	 */
	protected final IKBalanceOnLegMovement balanceOnLegMvmt;

	protected Pose2D targetPose;

	/**
	 * @param name The name of the kick behavior.
	 * @param thoughtModel the thought model.
	 * @param freeFoot The kicking foot.
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
	 * @param params some parameters
	 */
	public IKStabilizeOnLegBehavior(String name, IRoboCupThoughtModel thoughtModel, SupportFoot freeFoot,
			Pose6D supportFootBalancingPose, Pose6D freeFootBalancingPose, Vector3D balancingLeaning,
			IParameterList params)
	{
		super(name, thoughtModel);

		this.freeFoot = freeFoot;

		finalStepMvmt = new IKCoMShiftingStepMovement(thoughtModel, params);
		getOnLegMvmt = new IKGetOnLegStepMovement(thoughtModel, params);
		balanceOnLegMvmt = new IKBalanceOnLegMovement(
				thoughtModel, supportFootBalancingPose, freeFootBalancingPose, balancingLeaning, 18, 500);

		targetPose = new Pose2D(0.13, 0.08);
	}

	@Override
	public void init()
	{
		super.init();
		currentMovement = null;
	}

	public SupportFoot getFreeFoot()
	{
		return freeFoot;
	}

	public void setFreeFootTargetPose(Pose2D targetPose)
	{
		this.targetPose.copy(targetPose);
	}

	@Override
	protected IIKMovement decideNextMovement()
	{
		IIKMovement nextMovement;

		if (currentMovement == null) {
			// Do final step
			finalStepMvmt.setSupportFoot(freeFoot);
			finalStepMvmt.setFreeFootTargetPose(targetPose);

			nextMovement = finalStepMvmt;
		} else if (currentMovement == finalStepMvmt) {
			// Do get on leg
			nextMovement = getOnLegMvmt;
		} else if (currentMovement == getOnLegMvmt) {
			// Do balance on leg
			nextMovement = balanceOnLegMvmt;
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

		return currentMovement == balanceOnLegMvmt;
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		IBehavior realBehavior = actualBehavior.getRootBehavior();

		if (realBehavior instanceof IKWalkBehavior) {
			// IKStaticWalkMovement walkMvmt = (IKStaticWalkMovement)
			// ((IKWalkBehavior) realBehavior)
			// .getCurrentMovement();
			// double turnAngle = walkMvmt.getCurrentStep().turnAngle;
			// if ((kickingFoot == SupportFoot.LEFT && turnAngle > 20)
			// || (kickingFoot == SupportFoot.RIGHT && turnAngle < -20)) {
			// // System.out
			// //
			// .println("not switching to kick because of not matching previous
			// turn");
			// return actualBehavior;
			// }

			SupportFoot nextSupportFoot = ((IKWalkBehavior) realBehavior).getCurrentMovement().getNextSupportFoot();

			if (realBehavior.isFinished() && nextSupportFoot == freeFoot) {
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

	public static IKStabilizeOnLegBehavior getStabilizeOnRightLeg(
			IRoboCupThoughtModel thoughtModel, ParameterMap parameterMap)
	{
		StabilizeParams params = StabilizeParams.getLeftKickStraightParams();

		return new IKStabilizeOnLegBehavior(IBehaviorConstants.STABILIZE.RIGHT, thoughtModel, SupportFoot.LEFT,
				new Pose6D(params.supportFootStabilizationPosition),
				new Pose6D(params.freeFootTargetPosition, params.freeFootTargetAngles),
				new Rotation(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR,
						Math.toRadians(params.intendedTargetLeaningForwards),
						Math.toRadians(params.intendedTargetLeaningSidewards), 0)
						.applyTo(Vector3D.PLUS_K),
				parameterMap.get(IBehaviorConstants.STABILIZE.BASE_NAME));
	}

	public static IKStabilizeOnLegBehavior getStabilizeOnLeftLeg(
			IRoboCupThoughtModel thoughtModel, ParameterMap parameterMap)
	{
		StabilizeParams params = StabilizeParams.getRightKickStraightParams();

		return new IKStabilizeOnLegBehavior(IBehaviorConstants.STABILIZE.LEFT, thoughtModel, SupportFoot.RIGHT,
				new Pose6D(params.supportFootStabilizationPosition),
				new Pose6D(params.freeFootTargetPosition, params.freeFootTargetAngles),
				new Rotation(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR,
						Math.toRadians(params.intendedTargetLeaningForwards),
						Math.toRadians(params.intendedTargetLeaningSidewards), 0)
						.applyTo(Vector3D.PLUS_K),
				parameterMap.get(IBehaviorConstants.STABILIZE.BASE_NAME));
	}
}