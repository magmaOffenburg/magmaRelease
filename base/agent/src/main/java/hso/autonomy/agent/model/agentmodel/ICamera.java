/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

/**
 * Interface for a camera sensor detecting geometric features.
 *
 * @author Stefan Glaser
 */
public interface ICamera extends ISensor {
	/**
	 * Retrieve the field of view of this camera.
	 *
	 * @return the camera field of view
	 */
	IFieldOfView getFoV();

	/**
	 * Retrieve the detection error model.
	 *
	 * @return the error model for this cameras' detections
	 */
	ICameraErrorModel getErrorModel();

	/**
	 * Set the error model for this camera to get more valid observations
	 * @param errorModel the error model for this camera
	 */
	void setErrorModel(ICameraErrorModel errorModel);
}
