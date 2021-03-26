/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmeta;

import hso.autonomy.util.geometry.IPose3D;
import java.io.Serializable;

/**
 * Base interface for all sensor configurations.
 *
 * @author Stefan Glaser
 */
public interface ISensorConfiguration extends Serializable {
	/**
	 * Retrieve the name of the sensor.
	 *
	 * @return the sensor name
	 */
	String getName();

	/**
	 * Retrieve the perceptor name.
	 *
	 * @return the name of the perceptor
	 */
	String getPerceptorName();

	/**
	 * Retrieve the pose where this sensor is mounted on its corresponding body part.
	 *
	 * @return the pose relative to the body part the sensor is mounted to
	 */
	IPose3D getPose();
}
