/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.model.agentmodel.ik.impl;

import hso.autonomy.agent.model.agentmodel.IBodyPart;
import hso.autonomy.agent.model.agentmodel.IHingeJoint;
import hso.autonomy.agent.model.agentmodel.impl.ik.impl.JacobianTransposeAgentIKSolver;
import hso.autonomy.util.geometry.Geometry;
import magma.robots.nao.INaoConstants;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class NaoLegCalculator extends JacobianTransposeAgentIKSolver
{
	@Override
	public boolean solve(IBodyPart targetBody, Vector3D targetPosition, Vector3D targetAngles)
	{
		double[] angles = calculateJointAngles(targetBody, targetPosition, targetAngles);

		if (angles != null) {
			angles = getChangeAngles(targetBody, angles, true);

			// iterate through the body parts and set the change angles on the
			// corresponding hinge joints
			IBodyPart currentBody = targetBody;
			for (int i = 5; i >= 0; i--) {
				((IHingeJoint) currentBody.getJoint()).adjustAxisPosition(angles[i]);
				currentBody = currentBody.getParent();
			}

			return true;
		}

		return super.solve(targetBody, targetPosition, targetAngles);
	}

	public static double[] calculateJointAngles(IBodyPart targetBody, Vector3D targetPos, Vector3D targetAngles)
	{
		boolean rightLeg;

		// Check if the target foot body is either the left or right foot.
		// Otherwise reject calculation.
		if (INaoConstants.LFoot.equals(targetBody.getName())) {
			rightLeg = false;
		} else if (INaoConstants.RFoot.equals(targetBody.getName())) {
			rightLeg = true;
		} else {
			return null;
		}

		// Fetch hinge joints of the leg
		IHingeJoint[] hingeJoints = new IHingeJoint[6];
		IBodyPart currentBody = targetBody;
		for (int i = 5; i >= 0; i--) {
			hingeJoints[i] = (IHingeJoint) currentBody.getJoint();
			currentBody = currentBody.getParent();
		}

		// Calculate thigh and shank length of the leg
		IBodyPart ankle = targetBody.getParent();
		IBodyPart shank = ankle.getParent();
		IBodyPart thigh = shank.getParent();

		double thighLength = shank.getTranslation().add(shank.getAnchor()).subtract(thigh.getAnchor()).getNorm();
		double shankLength = ankle.getTranslation().add(ankle.getAnchor()).subtract(shank.getAnchor()).getNorm();

		// Calculate the side factor (+1/-1 for the right/left leg)
		int sideFactor = rightLeg ? 1 : -1;

		// Limit z-rotation to -90/1 and -1/90
		if (rightLeg) {
			if (targetAngles.getZ() < -90) {
				targetAngles = new Vector3D(targetAngles.getX(), targetAngles.getY(), -90);
			} else if (targetAngles.getZ() > 1) {
				targetAngles = new Vector3D(targetAngles.getX(), targetAngles.getY(), 1);
			}
		} else {
			if (targetAngles.getZ() > 90) {
				targetAngles = new Vector3D(targetAngles.getX(), targetAngles.getY(), 90);
			} else if (targetAngles.getZ() < -1) {
				targetAngles = new Vector3D(targetAngles.getX(), targetAngles.getY(), -1);
			}
		}

		// Transform target angles to rad
		Vector3D targetAgnelsRad = targetAngles.scalarMultiply(Math.PI / 180.0);

		// Create localTargetOtientation for the foot (without z-rotation)
		// Matrix3d localTargetOrientation = new Matrix3d();
		// localTargetOrientation.rotX(targetAgnelsRad.getX());
		// MatrixUtil.rotateY(localTargetOrientation, targetAgnelsRad.getY());

		Rotation localTargetOrientation = new Rotation(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR,
				targetAgnelsRad.getX(), targetAgnelsRad.getY(), 0);

		// Calculate foot orientation matrix
		// Matrix3d targetFootOrientation = new Matrix3d();
		// targetFootOrientation.setIdentity();
		// MatrixUtil.rotateZ(targetFootOrientation, targetAgnelsRad.getZ());
		// MatrixUtil.rotateX(targetFootOrientation, targetAgnelsRad.getX());
		// MatrixUtil.rotateY(targetFootOrientation, targetAgnelsRad.getY());

		Rotation targetFootOrientation = new Rotation(RotationOrder.ZXY, RotationConvention.VECTOR_OPERATOR,
				targetAgnelsRad.getZ(), targetAgnelsRad.getX(), targetAgnelsRad.getY());

		// Calculate local-targetPos as target vector from hip to ankle
		Vector3D torsoToHip = new Vector3D(0.055 * sideFactor, -0.01, -0.115);
		Vector3D ankleToFoot = new Vector3D(0, 0.03, -0.035);
		// ankleToFoot = MatrixUtil.mulMatrixVector(targetFootOrientation,
		// ankleToFoot);
		ankleToFoot = targetFootOrientation.applyTo(ankleToFoot);
		// Vector3D localTagetPos = new Vector3d(targetPos.getX(),
		// targetPos.getY(),
		// targetPos.getZ());
		// localTagetPos.sub(torsoToHip);
		// localTagetPos.sub(ankleToFoot);
		Vector3D localTagetPos = targetPos.subtract(torsoToHip).subtract(ankleToFoot);

		// Calculate hip and knee angle to reach the given distance
		// To reach a certain point with the leg, there is no other option than to
		// stretch the leg to the given distance. The result of this calculation
		// are two angles, one for the hip-pitch and one for the knee-pitch, with
		// which the given distance is reached.
		// An open issue is that if the position can't be reached because of other
		// limitations, one might want to stretch the leg more ore less to
		// compensate faulty positions or calculations. But changing the leg
		// length might have further impacts on other calculations again.
		double distance = localTagetPos.getNorm();
		double kneeAnlgeByDistance;
		double hipAnlgeByDistance;

		if (thighLength + shankLength < distance) {
			// Leg too short to reach the distance
			distance = thighLength + shankLength;
			kneeAnlgeByDistance = 0;
			hipAnlgeByDistance = 0;
			// TODO: Caclulate min distance by min angle of knee
		} else if (thighLength - shankLength > distance) {
			// Leg not able to reach a distance as short as the specified one
			distance = thighLength - shankLength;
			kneeAnlgeByDistance = -Math.PI;
			hipAnlgeByDistance = 0;
		} else {
			// Leg is able to reach the distance, so calculate the necessary angles
			// for hip and knee
			double y = (thighLength * thighLength - shankLength * shankLength + distance * distance) / (2 * distance);

			hipAnlgeByDistance = Math.acos(y / thighLength);
			kneeAnlgeByDistance = (hipAnlgeByDistance + (Math.PI / 2) - Math.asin((distance - y) / shankLength)) * -1;
		}

		// Calculate hip yaw-pitch angle to reach the given z-agnle
		// The joints in the hip are responsible for the resulting z-angle and for
		// reaching the x-, y-, z-coordinates of the given target position (not
		// reaching them directly, but more in a least square sense, that the leg
		// points towards the target-position). The problem in this configuration
		// is that with the yaw-pitch axis, we have singularities between
		// rotation-axes.
		// Example: We have no general yaw-axis, so we need to use the
		// yaw-pitch axis to reach the target z-angle in the first place. By doing
		// so, we also change the target position for the resulting Leg-System. If
		// we now use the pitch-axis to reach the target y-position, we again
		// change the z-angle of the resulting system, depending on the
		// applied roll-rotation. To get the pitch-axis not changing the final
		// z-angle again, we need to have a specific roll-rotation. This specific
		// roll-rotation can be calculated (hipRollByYawPitch) for a given
		// yaw-pitch-rotation, but this means, that once we have a target z-angle
		// != 0 and we enforce this specific roll-rotation, we have no option any
		// more to move in x-direction to reach the given target x-position.
		// But in contrast, if we also allow roll-rotation to reach the target
		// x-position when target z-angle != 0, we would change the resulting
		// z-angle again.
		double hipYawPitch;
		if (targetAgnelsRad.getZ() > Math.toRadians(89) || targetAgnelsRad.getZ() < Math.toRadians(-89)) {
			hipYawPitch = -Math.PI / 2;
		} else {
			hipYawPitch = sideFactor * Math.atan(Math.tan(targetAgnelsRad.getZ()) / Math.sin(Math.PI / 4));
		}

		hipYawPitch = limitRadAngle(hipYawPitch, hingeJoints[0]);

		// Transform local tragetPosition into System after the Yaw-Pitch joint
		// Matrix3d yawPitchTransform = new Matrix3d();
		// if (leg == Leg.RIGHT) {
		// yawPitchTransform.set(new AxisAngle4d(
		// new Vector3d(-0.7071, 0, 0.7071), hipYawPitch));
		// } else {
		// yawPitchTransform.set(new AxisAngle4d(
		// new Vector3d(-0.7071, 0, -0.7071), hipYawPitch));
		// }
		Rotation yawPitchTransform;
		if (rightLeg) {
			yawPitchTransform =
					new Rotation(new Vector3D(-0.7071, 0, 0.7071), hipYawPitch, RotationConvention.VECTOR_OPERATOR);
		} else {
			yawPitchTransform =
					new Rotation(new Vector3D(-0.7071, 0, -0.7071), hipYawPitch, RotationConvention.VECTOR_OPERATOR);
		}

		// Transform local tragetPosition into System after the Yaw-Pitch joint
		// yawPitchTransform.transpose();
		// Vector3d hipRollLocalTagetPos = MatrixUtil.mulMatrixVector(
		// yawPitchTransform, localTagetPos);
		// yawPitchTransform.transpose();
		Vector3D hipRollLocalTagetPos = yawPitchTransform.applyInverseTo(localTagetPos);

		// Calculate hip roll angle, to reach the local target position
		double hipRollByDistance = Math.atan2(-hipRollLocalTagetPos.getX(), -hipRollLocalTagetPos.getZ());

		// Calculate specific hip-roll angle needed to bring the pitch axis back
		// on the x-y-plane
		// double hipRollByYawPitch = Math.atan2(yawPitchTransform.m20,
		// yawPitchTransform.m22);
		double[][] tempMatrix = yawPitchTransform.getMatrix();
		// double hipRollByYawPitch = Math.atan2(tempMatrix[2][0],
		// tempMatrix[2][2]);

		double hipRoll;
		// hipRoll = hipRollByYawPitch;
		hipRoll = hipRollByDistance;

		hipRoll = limitRadAngle(hipRoll, hingeJoints[1]);

		// Transform local tragetPosition into System after the Yaw-Pitch and
		// Roll joint
		// Matrix3d yawPitchRollTransform = new Matrix3d(yawPitchTransform);
		// MatrixUtil.rotateY(yawPitchRollTransform, hipRoll);
		Rotation yawPitchRollTransform = yawPitchTransform.applyTo(Geometry.createYRotation(hipRoll));

		// yawPitchRollTransform.transpose();
		// Vector3d hipPitchLocalTagetPos = MatrixUtil.mulMatrixVector(
		// yawPitchRollTransform, localTagetPos);
		// yawPitchRollTransform.transpose();
		// // System.out.println(yawPitchRollTransform);
		Vector3D hipPitchLocalTagetPos = yawPitchRollTransform.applyInverseTo(localTagetPos);

		// // Calculate hip pitch angle, to reach the local target position
		double hipPitch = -Math.atan2(-hipPitchLocalTagetPos.getY(), -hipPitchLocalTagetPos.getZ());

		// Add distance enforced hipPitch
		hipPitch += hipAnlgeByDistance;

		hipPitch = limitRadAngle(hipPitch, hingeJoints[2]);

		double kneePitch = kneeAnlgeByDistance;
		kneePitch = limitRadAngle(kneePitch, hingeJoints[3]);

		// Calculate foot pitch angle and foot roll angle
		// Matrix3d tempMat = new Matrix3d(yawPitchRollTransform);
		// MatrixUtil.rotateX(tempMat, hipPitch);
		// MatrixUtil.rotateX(tempMat, kneePitch);
		//
		// Matrix3d footTransform = new Matrix3d(localTargetOrientation);
		// footTransform.transpose();
		// footTransform.mul(tempMat);
		Rotation footTransform = localTargetOrientation.applyInverseTo(yawPitchRollTransform.applyTo(
				Geometry.createXRotation(hipPitch).applyTo(Geometry.createXRotation(kneePitch))));

		// Fetch normal as the parent z-axis
		tempMatrix = footTransform.getMatrix();
		Vector3D normal = new Vector3D(tempMatrix[2][0], tempMatrix[2][1], tempMatrix[2][2]);

		// Create rotation for foot-pitch and -roll to initial orientation
		double footPitch = Math.toRadians(40);
		double footRoll = 0;

		if (normal.getZ() > 0.9999999999) {
			// North pole => up straight
			footPitch = 0;
			footRoll = 0;
		} else if (normal.getZ() < -0.9999999999) {
			// South pole => up down
			footPitch = 0;
			footRoll = Math.PI;
		} else {
			footPitch = Math.atan2(-normal.getY(), normal.getZ());
			footRoll = Math.asin(normal.getX());
		}

		footPitch = limitRadAngle(footPitch, hingeJoints[4]);
		footRoll = limitRadAngle(footRoll, hingeJoints[5]);

		// Copy and transform result
		double[] result = new double[6];
		result[0] = Math.toDegrees(hipYawPitch);
		result[1] = Math.toDegrees(hipRoll);
		result[2] = Math.toDegrees(hipPitch);
		result[3] = Math.toDegrees(kneePitch);
		result[4] = Math.toDegrees(footPitch);
		result[5] = Math.toDegrees(footRoll);

		return result;
	}

	public static double[] getChangeAngles(IBodyPart targetBody, double[] angles, boolean scaled)
	{
		// Check if the target foot body is either the left or right foot.
		// Otherwise reject calculation.
		if (!INaoConstants.LFoot.equals(targetBody.getName()) && !INaoConstants.RFoot.equals(targetBody.getName())) {
			return null;
		}

		double scale = 1;
		double[] angleChanges = new double[6];

		IBodyPart currentBody = targetBody;
		IHingeJoint joint;
		for (int i = 5; i >= 0; i--) {
			// fetch joint reference
			joint = (IHingeJoint) currentBody.getJoint();

			// calculate the change angle
			angleChanges[i] = angles[i] - joint.getAngle();

			// if required, calculate scale dependent on the max speed of the joint
			if (scaled && Math.abs(angleChanges[i]) > joint.getMaxSpeed()) {
				double tempScale = joint.getMaxSpeed() / Math.abs(angleChanges[i]);

				if (tempScale < scale) {
					scale = tempScale;
				}
			}

			currentBody = currentBody.getParent();
		}

		if (scaled) {
			// scale change angles with respect to the slowest joint
			for (int i = 0; i < angleChanges.length; i++) {
				angleChanges[i] *= scale;
			}
		}

		return angleChanges;
	}

	/**
	 * Limits the given angle to the bounds defined by the joint.
	 *
	 * @param angle the angle to limit
	 * @param joint the joint specifying min and max angles
	 * @return the limited angle
	 */
	private static double limitRadAngle(double angle, IHingeJoint joint)
	{
		double min = Math.toRadians(joint.getMinAngle());
		double max = Math.toRadians(joint.getMaxAngle());

		if (angle > max) {
			return max;
		} else if (angle < min) {
			return min;
		} else {
			return angle;
		}
	}
}
