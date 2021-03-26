/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.common.spark;

/**
 * Play sides enumeration. Every enum stores the corresponding message used in
 * the server protocol.
 *
 * @author Stefan Glaser
 */
public enum PlaySide {
	/** We play from left to right */
	LEFT("left"),

	/** We play from right to left */
	RIGHT("right"),

	/** No, or unknown play side */
	UNKNOWN("unknown");

	private final String playSideString;

	PlaySide(String playSideString)
	{
		this.playSideString = playSideString;
	}

	/**
	 * Convert a play side into an enum
	 *
	 * @param playSideString Play side string
	 * @return Resulting enum
	 */
	public static PlaySide parsePlaySide(String playSideString)
	{
		for (PlaySide side : values()) {
			if (playSideString.equalsIgnoreCase(side.playSideString))
				return side;
		}

		return UNKNOWN;
	}

	public String getName()
	{
		return playSideString;
	}

	public PlaySide getOpposite()
	{
		switch (this) {
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		default:
			return UNKNOWN;
		}
	}
}
