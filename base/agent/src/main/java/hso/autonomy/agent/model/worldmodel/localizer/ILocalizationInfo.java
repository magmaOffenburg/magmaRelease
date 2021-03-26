/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer;

import hso.autonomy.util.geometry.IPose3D;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interface for holding the result of a localization run.
 *
 * @author Stefan Glaser
 */
public interface ILocalizationInfo {
	/**
	 * Retrieve the global time related to this localization information.
	 *
	 * @return the global time
	 */
	float getTime();

	/**
	 * Retrieve the most probable pose.
	 *
	 * @return the most probable pose
	 */
	IPose3D getLocalizedPose();

	/**
	 * Retrieve the most probable position.<br>
	 * Shortcut for:<br>
	 * <code>
	 * getLocalizedPose().getPosition();
	 * </code>
	 *
	 * @return the most probable position
	 */
	Vector3D getLocalizedPosition();

	/**
	 * Retrieve the most probable orientation.<br>
	 * Shortcut for:<br>
	 * <code>
	 * getLocalizedPose().getOrientation();
	 * </code>
	 *
	 * @return the most probable orientation
	 */
	Rotation getLocalizedOrientation();

	/**
	 * Retrieve the unfiltered localized position. Depending on the filter, this may be the same position as the
	 * localized position.
	 *
	 * @return the unfiltered position
	 */
	Vector3D getUnfilteredPosition();
}
