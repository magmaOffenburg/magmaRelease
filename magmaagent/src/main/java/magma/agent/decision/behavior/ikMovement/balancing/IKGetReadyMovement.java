/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement.balancing;

import hso.autonomy.agent.model.agentmodel.IBodyPart;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.geometry.Pose6D;
import magma.agent.IHumanoidConstants;
import magma.agent.decision.behavior.ikMovement.IKMovementBase;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class IKGetReadyMovement extends IKMovementBase
{
	private static final float BALANCING_HEIGHT = -0.71f;

	private static final float BALANCING_OFFSET = 0.0f;

	private static final float BALANCING_WIDTH = 0.15f;

	protected int CYCLES_PER_CM = 20;

	protected final Vector3D leftFootTargetPosition;

	protected final Vector3D rightFootTargetPosition;

	public IKGetReadyMovement(IRoboCupThoughtModel thoughtModel)
	{
		super("GetReadyMovement", thoughtModel, 20, 50);

		leftFootTargetPosition = new Vector3D(-BALANCING_WIDTH, BALANCING_OFFSET, BALANCING_HEIGHT);
		rightFootTargetPosition = new Vector3D(BALANCING_WIDTH, BALANCING_OFFSET, BALANCING_HEIGHT);

		isStatic = true;
	}

	@Override
	protected void calculateMovementTrajectory()
	{
		IBodyPart tempBody = agentModel.getBodyPart(IHumanoidConstants.LFoot);
		Pose3D currentLeftFootPose = tempBody.getPose();
		// boolean leftFootForce = tempBody.getSensor(IForceResistance.class)
		// .getForce().getNorm() > 0.001;

		tempBody = agentModel.getBodyPart(IHumanoidConstants.RFoot);
		Pose3D currentRightFootPose = tempBody.getPose();
		// boolean rightFootForce = tempBody.getSensor(IForceResistance.class)
		// .getForce().getNorm() > 0.001;

		// Process the top view z-normalized orientation
		Rotation topViewOrientation = Rotation.IDENTITY;
		// Geometry.getTopViewOrientation(worldModel.getThisPlayer().getOrientation());

		// Process the top view location of the centerOfMass
		Vector3D topViewCenterOfMass = topViewOrientation.applyTo(agentModel.getStaticPivotPoint());

		// Transform current foot positions into top-view CoM system
		Vector3D initialLeftFootPosition =
				topViewOrientation.applyTo(currentLeftFootPose.getPosition()).subtract(topViewCenterOfMass);
		Vector3D initialRightFootPosition =
				topViewOrientation.applyTo(currentRightFootPose.getPosition()).subtract(topViewCenterOfMass);

		// // Correct initial foot positions to common initial foot height
		// if (!leftFootForce || !rightFootForce) {
		// if (initialLeftFootPosition.getZ() < initialRightFootPosition.getZ()) {
		// initialRightFootPosition = new Vector3D(
		// initialRightFootPosition.getX(),
		// initialRightFootPosition.getY(),
		// initialLeftFootPosition.getZ());
		// } else {
		// initialLeftFootPosition = new Vector3D(
		// initialLeftFootPosition.getX(),
		// initialLeftFootPosition.getY(),
		// initialRightFootPosition.getZ());
		// }
		// }

		// initial poses
		leftFootInitialPose = new Pose6D(initialLeftFootPosition);
		leftFootInitialPose.zAngle = -1 * Math.toDegrees(Geometry.getTopViewZAngle(
												  topViewOrientation.applyTo(currentLeftFootPose.getOrientation())));
		// leftFootInitialPose.xAngle = 0;
		// leftFootInitialPose.yAngle = 0;

		rightFootInitialPose = new Pose6D(initialRightFootPosition);
		rightFootInitialPose.zAngle = -1 * Math.toDegrees(Geometry.getTopViewZAngle(
												   topViewOrientation.applyTo(currentRightFootPose.getOrientation())));
		// rightFootInitialPose.xAngle = 0;
		// rightFootInitialPose.yAngle = 0;

		// target poses
		Pose6D leftFootTargetPose = new Pose6D(leftFootTargetPosition);
		Pose6D rightFootTargetPose = new Pose6D(rightFootTargetPosition);

		// Check for maximum movement distance to determine the endurance of the
		// getReady
		double maxDistance = Math.max(leftFootTargetPosition.subtract(initialLeftFootPosition).getNorm(),
				rightFootTargetPosition.subtract(initialRightFootPosition).getNorm());
		int newMovementCycles = (int) (maxDistance * 100 * CYCLES_PER_CM);
		if (newMovementCycles < 50) {
			newMovementCycles = 50;
		}
		setMovementCycles(newMovementCycles);

		interpolateMovement(leftFootTargetPose, rightFootTargetPose);
	}
}
