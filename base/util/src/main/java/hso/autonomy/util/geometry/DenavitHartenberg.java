/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Denavit/Hartenberg Transformation implementation
 *
 * @author Ingo Schindler
 */
class DenavitHartenberg
{
	/**
	 * Transform a 3-dimensional vector with the given parameters using the
	 * Denavit/Hartenberg transformation
	 *
	 * @param origin Origin
	 * @param p parameters
	 * @return Transformed vector
	 */
	public static Vector3D transform(Vector3D origin, DenavitHartenbergParameters p)
	{
		// Matrix4d transformation;
		//
		// Matrix4d rotationX = new Matrix4d();
		// rotationX.rotX(p.getAlpha());
		//
		// Matrix4d rotationY = new Matrix4d();
		// rotationY.rotY(p.getBeta());
		//
		// Matrix4d rotationZ = new Matrix4d();
		// rotationZ.rotZ(p.getTheta());
		//
		// Matrix4d translation = new Matrix4d();
		// translation.rotZ(0);
		// translation.setTranslation(p.getDistance());
		//
		// transformation = rotationX;
		//
		// transformation.mul(translation);
		//
		// transformation.mul(rotationZ);
		//
		// return MatrixUtil.mulMatrixVector(transformation, origin);
		Rotation rotationX = Geometry.createXRotation(p.getAlpha());

		// Rotation rotationY = Geometry.createYRotation(p.getBeta());

		Rotation rotationZ = Geometry.createZRotation(p.getTheta());

		Vector3D distance = rotationX.applyTo(p.getDistance());

		return rotationX.applyTo(rotationZ).applyTo(origin).add(distance);
	}
}
