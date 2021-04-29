/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent;

/**
 * Collection of constants that are valid for all humanoid robots
 *
 * @author kdorer
 */
public interface IHumanoidConstants {
	// Field version
	int DEFAULT_FIELD_VERSION = 2019;

	// Body parts
	String Head = "head";

	String LFoot = "lfoot";

	String RFoot = "rfoot";

	// Sensors
	String TorsoAccelerometer = "TorsoAccel";

	// ForceResistance-Constants
	String RFootForce = "RFootForce";

	String LFootForce = "LFootForce";
}
