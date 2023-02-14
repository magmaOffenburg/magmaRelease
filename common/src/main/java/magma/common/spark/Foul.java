/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.common.spark;

public class Foul
{
	public enum FoulType {
		CROWDING(0, "crowding"),
		TOUCHING(1, "touching"),
		ILLEGAL_DEFENCE(2, "illegal defence"),
		ILLEGAL_ATTACK(3, "illegal attack"),
		INCAPABLE(4, "incapable"),
		ILLEGAL_KICK_OFF(5, "illegal kickoff"),
		CHARGING(6, "charging"),
		SELF_COLLISION(7, "self collision"),
		BALL_HOLDING(8, "ball holding");

		private int index;

		private String name;

		FoulType(int index, String name)
		{
			this.index = index;
			this.name = name;
		}

		public String toString()
		{
			return name;
		}

		public String getName()
		{
			return name;
		}
	}

	public final float time;

	public final int index;

	public final FoulType type;

	public final PlaySide playSide;

	public final int agentID;

	public Foul(float time, int index, FoulType type, int team, int agentID)
	{
		this.time = time;
		this.index = index;
		this.type = type;
		this.agentID = agentID;
		playSide = getPlaySide(team);
	}

	private PlaySide getPlaySide(int team)
	{
		switch (team) {
		case 1:
			return PlaySide.LEFT;
		case 2:
			return PlaySide.RIGHT;
		default:
			return PlaySide.UNKNOWN;
		}
	}
}
