/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.common.spark;

/**
 * Identifier for the team's color
 * @author kdorer
 */
public enum TeamColor {
	BLUE,
	RED,
	UNKNOWN;

	public TeamColor getOpponent()
	{
		switch (this) {
		case BLUE:
			return RED;
		case RED:
			return BLUE;
		default:
			return UNKNOWN;
		}
	}
}
