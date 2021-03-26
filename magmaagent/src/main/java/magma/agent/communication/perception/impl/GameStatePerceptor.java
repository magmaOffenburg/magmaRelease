/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.impl.Perceptor;
import magma.agent.communication.perception.IGameStatePerceptor;
import magma.common.spark.TeamColor;

/**
 * Game state perceptor, currently features the current time, playmode, team
 * name and agent number
 *
 * @author Simon Raffeiner
 */
public class GameStatePerceptor extends Perceptor implements IGameStatePerceptor
{
	private final String playmode;

	private final String teamSide;

	/** color of team. Currently unknown in simspark! */
	private final TeamColor teamColor;

	private final int unum;

	private final float time;

	private final int scoreLeft;

	private final int scoreRight;

	/*
	 * gameState: BeforeKickOff KickOff Left KickOff Right PlayOn KickIn Left
	 * KickIn Right corner kick left corner kick right goal kick left goal kick
	 * right offside left offside right GameOver Goal Left Goal Right free kick
	 * left free kick right unknown
	 */

	/**
	 * Default constructor, initializes time to 0.00 and PlayMode to "unknown"
	 */
	public GameStatePerceptor()
	{
		this(0.00f, "BeforeKickOff", "unknown", TeamColor.BLUE, 0, -1, -1);
	}

	public GameStatePerceptor(
			float time, String playmode, String teamSide, TeamColor teamColor, int unum, int scoreLeft, int scoreRight)
	{
		super(IGameStatePerceptor.NAME);

		this.time = time;
		this.playmode = playmode;
		this.teamSide = teamSide;
		this.teamColor = teamColor;
		this.unum = unum;
		this.scoreLeft = scoreLeft;
		this.scoreRight = scoreRight;
	}

	@Override
	public String getPlaymode()
	{
		return playmode;
	}

	@Override
	public float getTime()
	{
		return time;
	}

	@Override
	public String getTeamSide()
	{
		return teamSide;
	}

	@Override
	public TeamColor getTeamColor()
	{
		return teamColor;
	}

	@Override
	public int getAgentNumber()
	{
		return unum;
	}

	@Override
	public int getScoreLeft()
	{
		return scoreLeft;
	}

	@Override
	public int getScoreRight()
	{
		return scoreRight;
	}
}
