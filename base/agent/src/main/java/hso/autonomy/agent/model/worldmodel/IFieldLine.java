/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel;

import hso.autonomy.agent.model.worldmodel.localizer.ILineFeature;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * This interface describes a visible field line.
 *
 * @author Stefan Glaser
 */
public interface IFieldLine extends IVisibleObject, ILineFeature {
	/**
	 * @return the first local position of the line (relative to the global root
	 *         body system)
	 */
	Vector3D getLocalPosition1();

	/**
	 * @return the second local position of the line (relative to the global root
	 *         body system)
	 */
	Vector3D getLocalPosition2();

	/**
	 * @return the first position of the line in the global coordinate system
	 */
	Vector3D getPosition1();

	/**
	 * @return the second position of the line in the global coordinate system
	 */
	Vector3D getPosition2();
}
