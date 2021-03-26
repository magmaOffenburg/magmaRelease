/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.common.humanoid;

/**
 * Enum for holding possible roles for the humanoid league.
 *
 * @author Stefan Glaser
 */
public enum HLRole {
	/** An idling player role. */
	IDLING("Dummy"),

	/** An undefined role. */
	OTHER("Other"),

	/** The striker role. */
	STRIKER("Striker"),

	/** The supporter role */
	SUPPORTER("Supporter"),

	/** The defender role. */
	DEFENDER("Defender"),

	/** The goalie role */
	GOALIE("Goalie");

	/** The name of the role. */
	public final String name;

	private HLRole(String name)
	{
		this.name = name;
	}

	public static HLRole getRoleForID(int id)
	{
		if (id >= 0 && id < values().length) {
			return values()[id];
		}

		return OTHER;
	}

	public static HLRole getRoleForName(String name)
	{
		for (HLRole role : values()) {
			if (role.name.equals(name)) {
				return role;
			}
		}

		return OTHER;
	}
}
