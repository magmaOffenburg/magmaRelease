/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

public interface ICameraErrorModel {
	/**
	 * Retrieve the maximal distance for valid observations
	 * @return the maximal distance in meters
	 */
	double getMaxDistance();
}
