/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.channel.impl;

public class ChannelParameters
{
	private final String teamname;

	private byte teamID;

	private final int playerNumber;

	private final String host;

	private final int port;

	private boolean logPerception;

	public ChannelParameters(String teamname, byte teamID, int playerNumber, String host, int port)
	{
		this.teamname = teamname;
		this.teamID = teamID;
		this.playerNumber = playerNumber;
		this.host = host;
		this.port = port;
		this.logPerception = false;
	}

	public ChannelParameters(
			String teamname, byte teamID, int playerNumber, String host, int port, boolean logPerception)
	{
		this.teamname = teamname;
		this.teamID = teamID;
		this.playerNumber = playerNumber;
		this.host = host;
		this.port = port;
		this.logPerception = logPerception;
	}

	/**
	 * Retrieve the team name
	 *
	 * @return Team name
	 */
	public String getTeamname()
	{
		return teamname;
	}

	/**
	 * Retrieve the player number
	 *
	 * @return Player number
	 */
	public int getPlayerNumber()
	{
		return playerNumber;
	}

	/**
	 * Retrieve the server host
	 *
	 * @return Server host string
	 */
	public String getHost()
	{
		return host;
	}

	/**
	 * Retrieve the server port
	 *
	 * @return Server port
	 */
	public int getPort()
	{
		return port;
	}

	public byte getTeamID()
	{
		return teamID;
	}

	public void setTeamID(byte teamID)
	{
		this.teamID = teamID;
	}

	public boolean shouldLogPerception()
	{
		return logPerception;
	}
}