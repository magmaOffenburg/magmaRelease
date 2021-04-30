/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.agentruntime;

import magma.agent.IMagmaConstants;
import magma.agent.UglyConstants;
import magma.agent.communication.channel.impl.ChannelParameters;
import magma.common.spark.IConnectionConstants;
import magma.util.roboviz.RoboVizParameters;

/**
 * Stores all necessary player parameters (Server connection, decision maker etc.)
 *
 * @author Klaus Dorer
 */
public class PlayerParameters
{
	private final ChannelParameters channelParams;

	private final int serverVersion;

	private final String decisionMakerName;

	private String teamStrategyName;

	private final ComponentFactory factory;

	private RoboVizParameters roboVizParams;

	private final boolean reportStats;

	private final boolean thinClient;

	/**
	 * Instantiates and initializes a new PlayerParameters object
	 *
	 * @param teamname Team name
	 * @param playerNumber Player number
	 * @param host Server host
	 * @param port Server port
	 * @param serverVersion Server version
	 * @param factory Reference to the component factory
	 * @param decisionMakerName Name of the decision maker
	 */
	public PlayerParameters(String teamname, byte teamID, int playerNumber, String host, int port, int serverVersion,
			ComponentFactory factory, String decisionMakerName, boolean reportStats, boolean thinClient)
	{
		this(new ChannelParameters(teamname, teamID, playerNumber, host, port), serverVersion, factory,
				decisionMakerName, reportStats, thinClient);
	}

	public PlayerParameters(String teamname, String teamStrategyName, byte teamID, int playerNumber, String host,
			int port, int serverVersion, ComponentFactory factory, String decisionMakerName,
			RoboVizParameters roboVizParams, boolean reportStats, boolean thinClient)
	{
		this(new ChannelParameters(teamname, teamID, playerNumber, host, port), serverVersion, factory,
				decisionMakerName, reportStats, thinClient);
		this.teamStrategyName = teamStrategyName;
		this.roboVizParams = roboVizParams;
	}

	public PlayerParameters(String teamname, byte teamID, int playerNumber, String host, int port, int serverVersion,
			ComponentFactory factory, String decisionMakerName, RoboVizParameters roboVizParams, boolean reportStats,
			boolean thinClient)
	{
		this(teamname, null, teamID, playerNumber, host, port, serverVersion, factory, decisionMakerName, roboVizParams,
				reportStats, thinClient);
	}

	public PlayerParameters(ChannelParameters channelParams, int serverVersion, ComponentFactory factory,
			String decisionMakerName, boolean reportStats, boolean thinClient)
	{
		this.channelParams = channelParams;
		this.serverVersion = serverVersion;
		this.factory = factory;
		this.decisionMakerName = decisionMakerName;
		this.reportStats = reportStats;
		this.thinClient = thinClient;
		UglyConstants.thinClient = thinClient;
	}

	public PlayerParameters(ComponentFactory factory)
	{
		this(IMagmaConstants.DEFAULT_TEAMNAME, IMagmaConstants.DEFAULT_TEAMID, 8, IConnectionConstants.SERVER_IP,
				IConnectionConstants.AGENT_PORT, IMagmaConstants.DEFAULT_SERVER_VERSION, factory,
				IMagmaConstants.DEFAULT_DECISION_MAKER, false, false);
	}

	/**
	 * @return the parameters that are used to setup channels
	 */
	public ChannelParameters getChannelParams()
	{
		return channelParams;
	}

	/**
	 * Retrieve the team name
	 *
	 * @return Team name
	 */
	public String getTeamname()
	{
		return channelParams.getTeamname();
	}

	public byte getTeamID()
	{
		return channelParams.getTeamID();
	}

	/**
	 * Retrieve the player number
	 *
	 * @return Player number
	 */
	public int getPlayerNumber()
	{
		return channelParams.getPlayerNumber();
	}

	/**
	 * Retrieve the server host
	 *
	 * @return Server host string
	 */
	public String getHost()
	{
		return channelParams.getHost();
	}

	/**
	 * Retrieve the server port
	 *
	 * @return Server port
	 */
	public int getPort()
	{
		return channelParams.getPort();
	}

	/**
	 * Retrieve the server version
	 *
	 * @return Server version
	 */
	public int getServerVersion()
	{
		return serverVersion;
	}

	/**
	 * Retrieve the reference to the component factory
	 *
	 * @return Component factory reference
	 */
	public ComponentFactory getComponentFactory()
	{
		return factory;
	}

	/**
	 * Retrieve the decision maker name
	 *
	 * @return Decision maker name
	 */
	public String getDecisionMakerName()
	{
		return decisionMakerName;
	}

	public RoboVizParameters getRoboVizParams()
	{
		return roboVizParams;
	}

	public boolean getReportStats()
	{
		return reportStats;
	}

	public String getTeamStrategyName()
	{
		return teamStrategyName;
	}

	public boolean isThinClient()
	{
		return thinClient;
	}
}