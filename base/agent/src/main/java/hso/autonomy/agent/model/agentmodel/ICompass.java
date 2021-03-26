/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

import hso.autonomy.util.geometry.Angle;

/**
 * @author david
 *
 */
public interface ICompass extends ISensor {
	Angle getAngle();

	void setAngle(Angle angle);

	void reset();
}
