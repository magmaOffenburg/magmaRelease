/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent;

/**
 * Introduced for marking quick hacks with constants that are checked with an if
 * before running quick hack code. The ideal state of this class is that it is
 * empty. It can not be an interface since some attributes may be non final
 */
public abstract class UglyConstants
{
	// put here constants that mark quick hack parts of code that need to be
	// cleaned up after a competition
	public static boolean thinClient;
}
