/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class InverseKinematics
{
	public static double[] jacobianTranspose(Vector3D targetPos, Vector3D currentPos, Vector3D targetAngles,
			Vector3D currentAngles, double[][] jacobian, double deLimit)
	{
		double[] dTheta;
		// de = end_goal - e;
		Vector3D dePos3D = targetPos.subtract(currentPos);
		if (dePos3D.getNorm() > deLimit) {
			dePos3D = dePos3D.normalize().scalarMultiply(deLimit);
		}

		RealVector de;
		if (targetAngles != null && currentAngles != null) {
			Vector3D deAngle3D = targetAngles.subtract(currentAngles);
			if (deAngle3D.getNorm() > deLimit / 2) {
				deAngle3D = deAngle3D.normalize().scalarMultiply(deLimit / 2);
			}

			de = new ArrayRealVector(6);
			de.setEntry(3, deAngle3D.getX());
			de.setEntry(4, deAngle3D.getY());
			de.setEntry(5, deAngle3D.getZ());
		} else {
			de = new ArrayRealVector(3);
		}
		de.setEntry(0, dePos3D.getX());
		de.setEntry(1, dePos3D.getY());
		de.setEntry(2, dePos3D.getZ());

		// % Compute alpha constant as quotient of 2 inner products as
		// suggested
		// % by Buss & Kim, to minimize de after caller updates theta
		// JJTde = J*J'*de;
		RealMatrix jacobianMatrix = new Array2DRowRealMatrix(jacobian, false);
		RealMatrix JJ_T = jacobianMatrix.multiply(jacobianMatrix.transpose());
		RealVector JJ_Tde = JJ_T.operate(de);

		// alpha = (de'*JJTde)/(JJTde'*JJTde);
		double alpha = de.dotProduct(JJ_Tde) / JJ_Tde.dotProduct(JJ_Tde);

		RealVector dThetaVec = jacobianMatrix.transpose().operate(de);
		dTheta = dThetaVec.toArray();

		for (int i = 0; i < dTheta.length; i++) {
			dTheta[i] *= alpha;
		}
		return dTheta;
	}
}
