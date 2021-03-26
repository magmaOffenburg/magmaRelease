/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * A Force Resistance Perceptor is a perceptor which exists at some point in the
 * three-dimensional space (the origin) and a force applies to it.
 *
 * @author Simon Raffeiner
 */
public interface IForceResistancePerceptor extends IPerceptor {
	/**
	 * @return Force origin vector
	 */
	Vector3D getForceOrigin();

	/**
	 * @return Force vector
	 */
	Vector3D getForce();
}