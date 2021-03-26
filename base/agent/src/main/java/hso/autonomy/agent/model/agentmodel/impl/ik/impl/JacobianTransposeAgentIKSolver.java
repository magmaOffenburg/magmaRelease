/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl.ik.impl;

import hso.autonomy.agent.model.agentmodel.IBodyPart;
import hso.autonomy.agent.model.agentmodel.IHingeJoint;
import hso.autonomy.agent.model.agentmodel.impl.ik.IAgentIKSolver;
import hso.autonomy.util.geometry.InverseKinematics;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.misc.FuzzyCompare;
import org.apache.commons.math3.geometry.euclidean.threed.CardanEulerSingularityException;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class JacobianTransposeAgentIKSolver implements IAgentIKSolver
{
	private final int iterations;

	private final double deLimit;

	private final double deTargetRange;

	public JacobianTransposeAgentIKSolver()
	{
		this(20, 0.001, 0.001);
	}

	public JacobianTransposeAgentIKSolver(int iterations, double deLimit, double deTargetRange)
	{
		this.iterations = iterations;
		this.deLimit = deLimit;
		this.deTargetRange = deTargetRange;
	}

	@Override
	public boolean solve(IBodyPart targetBody, Vector3D targetPosition, Vector3D targetAngles)
	{
		Vector3D currentPos;
		// TODO: this should be the end effector of this body part
		Vector3D localEndEffectorPos = Vector3D.ZERO;

		double[][] jacobian;
		double[] dTheta;

		// Do some iterations
		for (int i = 0; i < iterations; i++) {
			// Transform local end effector position to the root-body system
			Pose3D pose = targetBody.getPose(localEndEffectorPos);
			currentPos = pose.getPosition();

			// Angles HACK
			double[] eulerAngles;
			try {
				eulerAngles = pose.getOrientation().getAngles(RotationOrder.YXZ, RotationConvention.VECTOR_OPERATOR);
			} catch (CardanEulerSingularityException e) {
				eulerAngles = new double[] {0, 0, 0};
				e.printStackTrace();
			}

			Vector3D currentAngles = new Vector3D(eulerAngles[2], eulerAngles[1], eulerAngles[0]);
			// Angles HACK

			// compute next dTheta
			jacobian = targetBody.getJacobian(localEndEffectorPos, targetAngles);
			dTheta = InverseKinematics.jacobianTranspose(
					targetPosition, currentPos, targetAngles, currentAngles, jacobian, deLimit);

			// If we reached the target position, stop the loop
			if (FuzzyCompare.eq(targetPosition, currentPos, deTargetRange)) {
				// System.out.println("reached");
				return true;
			}

			IBodyPart parentPart = targetBody;

			int j = dTheta.length - 1;
			while (parentPart.getJoint() != null) {
				((IHingeJoint) parentPart.getJoint()).adjustAxisPosition(Math.toDegrees(dTheta[j--]));
				parentPart = parentPart.getParent();
			}
		}

		return true;
	}

	@Override
	public double[] calculateDeltaAngles(IBodyPart targetBody, Vector3D targetPosition, Vector3D targetAngles)
	{
		// here we incrementally calculate the angles and need to change the body
		// model, so just calculating without changing is difficult.
		return null;
	}
}
