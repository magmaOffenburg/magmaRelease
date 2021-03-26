/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.common.humanoid;

/**
 * Enum representing possible penalties in the Humanoid League.
 *
 * @author Stefan Glaser
 */
public enum HLPenalty {
	/** No penalty. */
	NONE,

	/** Some penalty causing the robot not to play. */
	SOME,

	/** A substitute player. */
	SUBSTITUTE;
}
