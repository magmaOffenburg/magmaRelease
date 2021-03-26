/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement.walk;

import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.Pose6D;
import hso.autonomy.util.geometry.interpolation.pose.PoseInterpolator;
import magma.agent.IHumanoidConstants;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class IKDynamicWalkMovement extends IKStaticWalkMovement
{
	public static final String NAME = "DynamicWalkMovement";

	public static final String NAME_STABLE = "DynamicWalkMovementStable";

	public static final String NAME_LOW_ACC = "DynamicWalkMovementLowAcc";

	private PoseInterpolator initialPoseInterpolator;

	public IKDynamicWalkMovement(IRoboCupThoughtModel thoughtModel, IKWalkMovementParametersBase params)
	{
		this(NAME, thoughtModel, params);
	}

	public IKDynamicWalkMovement(String name, IRoboCupThoughtModel thoughtModel, IKWalkMovementParametersBase params)
	{
		super(name, thoughtModel, params);
		initialPoseInterpolator = new PoseInterpolator();
		isStatic = false;
	}

	@Override
	protected void calculateMovementTrajectory()
	{
		// reset the height of the initial positions to the walk height
		leftFootInitialPose.z = params.getWalkHeight();
		rightFootInitialPose.z = params.getWalkHeight();

		double ySpeed = currentStep.forward / params.getMaxStepLength();
		double xSpeed = currentStep.sideward / params.getMaxStepWidth();
		double turnSpeed = currentStep.turn.degrees() / params.getMaxTurnAngle().degrees();
		int cycleTime = (int) params.getCyclesPerStep();
		float speed = (float) Math.max(Math.abs(turnSpeed), Math.sqrt(ySpeed * ySpeed + xSpeed * xSpeed));

		Rotation orientation = worldModel.getThisPlayer().getOrientation();

		// if (params.getDynamicWalk()) {
		// if (Math.abs(xSpeed) > Math.abs(ySpeed)) {
		// isStatic = true;
		// } else {
		// isStatic = false;
		// }
		// } else {
		// isStatic = true;
		// }

		// if (Math.abs(orientation.getMatrix()[2][0]) > 0.34) {
		// // 70 deg sidewise leaning
		// cycleTime -= 2;
		// } else if (Math.abs(orientation.getMatrix()[2][0]) > 0.17) {
		// // 80 deg sidewise leaning
		// cycleTime -= 1;
		// }
		double[][] matrix = orientation.getMatrix();
		double hangingSide = Math.abs(matrix[2][0]);
		double hangingAdjust = Geometry.getLinearFuzzyValue(0.05, 0.5, true, hangingSide) * 0.03;

		// EXPERIMENT: adjust step size to front/back hanging
		//		double hangingFront = matrix[2][1];
		//		// double adjust = hangingFront * hangingFront * hangingFront;
		//		// adjust = ValueUtil.limitAbs(adjust, 0.2);
		//		// currentStep.y_targetDistance -= adjust;
		//		int cycleAdjust = (int) (hangingFront * 0);
		//		// System.out.println("hangingFront: " + hangingFront + " adjust: " + cycleAdjust);
		//		if (cycleAdjust > 0) {
		//			cycleTime += cycleAdjust;
		//		}
		// EXPERIMENT: adjust step size to front/back hanging

		if (!params.getDynamicWalk()) {
			// do not adjust initial pose in case of static walk (sweaty)
			setMovementCycles(cycleTime);

		} else {
			if (speed < 0.9) {
				float t = 0f;

				if (speed > 0.4) {
					// Calculate interpolation progress between static and dynamic
					// initial poses
					t = ((speed - 0.4f) / 0.5f);
					setMovementCycles(cycleTime - 2);
				} else {
					setMovementCycles(cycleTime);
				}

				// Process the top view z-normalized orientation
				Rotation topViewOrientation = Geometry.getTopViewOrientation(orientation);
				Vector3D com = agentModel.getCenterOfMass();

				// EXPERIMENT KDO shift com depending on acceleration
				// double accelerationY = currentStep.forward - previousStep.forward;
				// com = com.add(new Vector3D(0, accelerationY * 2, 0));
				//	double accelerationX = currentStep.sideward - previousStep.sideward;
				//	com = com.add(new Vector3D(accelerationX * 1, 0, 0));
				// System.out.println("acceleration: " + accelerationY);

				// Fetch current poses
				Pose6D currentLeftFootPose = calculateTopViewFootPose(
						agentModel.getBodyPart(IHumanoidConstants.LFoot).getPose(), topViewOrientation, com);
				Pose6D currentRightFootPose = calculateTopViewFootPose(
						agentModel.getBodyPart(IHumanoidConstants.RFoot).getPose(), topViewOrientation, com);

				leftFootInitialPose = initialPoseInterpolator.interpolate(currentLeftFootPose, leftFootInitialPose, t);
				rightFootInitialPose =
						initialPoseInterpolator.interpolate(currentRightFootPose, rightFootInitialPose, t);
			} else {
				setMovementCycles(cycleTime - 2);
			}
		}

		// target poses
		Pose6D leftFootTargetPose = calculateFootTargetPose(currentStep, SupportFoot.LEFT, supportFoot, hangingAdjust);
		Pose6D rightFootTargetPose =
				calculateFootTargetPose(currentStep, SupportFoot.RIGHT, supportFoot, hangingAdjust);

		interpolateMovement(leftFootTargetPose, rightFootTargetPose);
	}
}
