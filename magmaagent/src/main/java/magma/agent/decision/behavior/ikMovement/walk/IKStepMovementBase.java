/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement.walk;

import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.geometry.Pose6D;
import hso.autonomy.util.geometry.interpolation.pose.PoseInterpolator;
import hso.autonomy.util.geometry.interpolation.progress.CosineProgress;
import hso.autonomy.util.geometry.interpolation.value.LinearValueInterpolator;
import hso.autonomy.util.geometry.interpolation.value.SineLinearValueInterpolator;
import kdo.util.parameter.IParameterList;
import magma.agent.IHumanoidConstants;
import magma.agent.decision.behavior.ikMovement.IIKMovement;
import magma.agent.decision.behavior.ikMovement.IKMovementBase;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author Stefan Glaser
 */
public abstract class IKStepMovementBase extends IKMovementBase
{
	/** a couple of parameters that are subject to learning */
	protected transient IKWalkMovementParametersBase params;

	/** value interpolator for the step height of the free foot */
	protected SineLinearValueInterpolator freeFootHeightInterpolator;

	/** value interpolator for the step height of the support foot */
	protected PushDownSineValueInterpolator supportFootHeightInterpolator;

	/** pose trajectory interpolator for the free foot */
	protected PoseInterpolator freeFootTrajectoryInterpolator;

	/** pose trajectory interpolator for the support foot */
	protected PoseInterpolator supportFootTrajectoryInterpolator;

	public IKStepMovementBase(String name, IRoboCupThoughtModel thoughtModel, IParameterList params)
	{
		super(name, thoughtModel, 12, 0);

		this.params = (IKWalkMovementParametersBase) params;
		setMovementCycles((int) this.params.getCyclesPerStep());

		supportFoot = SupportFoot.LEFT;
	}

	@Override
	protected void createTrajectoryInterpolators()
	{
		super.createTrajectoryInterpolators();

		supportFootHeightInterpolator = new PushDownSineValueInterpolator();
		supportFootTrajectoryInterpolator = new PoseInterpolator();
		supportFootTrajectoryInterpolator.zInterpolator = supportFootHeightInterpolator;

		freeFootHeightInterpolator = new SineLinearValueInterpolator();
		freeFootTrajectoryInterpolator = new PoseInterpolator(new LinearValueInterpolator(),
				new LinearValueInterpolator(new CosineProgress()), freeFootHeightInterpolator,
				new LinearValueInterpolator(), new LinearValueInterpolator(), new LinearValueInterpolator());
	}

	@Override
	protected void setDefaultInitialPoses()
	{
		super.setDefaultInitialPoses();

		if (params != null) {
			if (isStatic) {
				leftFootInitialPose.z = params.getWalkHeight();
				leftFootInitialPose.x = -1 * params.getWalkWidth();

				rightFootInitialPose.z = params.getWalkHeight();
				rightFootInitialPose.x = params.getWalkWidth();
			} else {
				// Process the top view z-normalized orientation
				Rotation topViewOrientation =
						Geometry.getTopViewOrientation(worldModel.getThisPlayer().getOrientation());
				Vector3D com = agentModel.getCenterOfMass();

				// Fetch current poses
				leftFootInitialPose = calculateTopViewFootPose(
						agentModel.getBodyPart(IHumanoidConstants.LFoot).getPose(), topViewOrientation, com);
				rightFootInitialPose = calculateTopViewFootPose(
						agentModel.getBodyPart(IHumanoidConstants.RFoot).getPose(), topViewOrientation, com);
			}
		}
	}

	@Override
	public void init(IIKMovement other)
	{
		if (other != null) {
			// If we are morphing from an existing movement, determine the next
			// support foot based on the previously performed movement. Otherwise
			// keep the current support foot (since it may have been set from
			// outside).
			if (other.getNextSupportFoot() == SupportFoot.LEFT) {
				supportFoot = SupportFoot.LEFT;
			} else {
				supportFoot = SupportFoot.RIGHT;
			}
		}

		// Set trajectory interpolator references according to the support foot
		if (supportFoot == SupportFoot.LEFT) {
			leftFootInterpolator = supportFootTrajectoryInterpolator;
			rightFootInterpolator = freeFootTrajectoryInterpolator;
		} else {
			leftFootInterpolator = freeFootTrajectoryInterpolator;
			rightFootInterpolator = supportFootTrajectoryInterpolator;
		}

		super.init(other);
	}

	public void setSupportFoot(SupportFoot supportFoot)
	{
		this.supportFoot = supportFoot;
	}

	@Override
	public SupportFoot getNextSupportFoot()
	{
		return supportFoot == SupportFoot.LEFT ? SupportFoot.RIGHT : SupportFoot.LEFT;
	}

	protected Pose6D calculateTopViewFootPose(Pose3D localFootPose, Rotation topViewOrientation, Vector3D centerOfMass)
	{
		Vector3D temp = topViewOrientation.applyTo(localFootPose.getPosition().subtract(centerOfMass));

		temp = new Vector3D(temp.getX(), temp.getY(), params.getWalkHeight());

		return new Pose6D(temp, topViewOrientation.applyTo(localFootPose.getOrientation()), RotationOrder.XYZ);
	}

	/**
	 * HACK METHOD!!! Only for temporary use!! This method stupidly provides the
	 * adjustment factors used for simulated Nao robot.<br>
	 * Statement from 3. feb 2014 :)
	 *
	 * @return {1, 1} if isStatic is true, {0.2, 0.2*0.9} otherwise
	 */
	@Deprecated
	@Override
	protected Vector2D getAdjustmentTargetsByStaticIndicator()
	{
		if (params.getDynamicWalk()) {
			return params.getAdjustmentFactors();
		}
		isStatic = true;
		return new Vector2D(1, 1);
	}
}