/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots;

import hso.autonomy.agent.agentruntime.AgentRuntime;
import hso.autonomy.util.commandline.Argument;
import hso.autonomy.util.commandline.BooleanArgument;
import hso.autonomy.util.commandline.HelpArgument;
import hso.autonomy.util.commandline.IntegerArgument;
import hso.autonomy.util.commandline.StringArgument;
import java.util.ArrayList;
import java.util.Collection;
import magma.agent.IHumanoidConstants;
import magma.agent.IMagmaConstants;
import magma.agent.agentruntime.ComponentFactory;
import magma.agent.agentruntime.PlayerParameters;
import magma.agent.agentruntime.RoboCupAgentRuntime;
import magma.common.spark.IConnectionConstants;
import magma.util.roboviz.RoboVizDraw;
import magma.util.roboviz.RoboVizParameters;

/**
 * This is the main class of the application used to start the magma RoboCup
 * agent. Run with --help for documentation on available arguments.
 *
 * @author Klaus Dorer
 */
public class RoboCupClient
{
	/**
	 * Instantiates and starts an agent.
	 */
	public static void main(String[] args)
	{
		AgentRuntime client = new RoboCupAgentRuntime(parseArgs(args));
		client.startClient();
	}

	private static PlayerParameters parseArgs(String[] args)
	{
		StringArgument teamNameArgument =
				new StringArgument("teamname", IMagmaConstants.DEFAULT_TEAMNAME, "name of the team");
		IntegerArgument teamIDArgument =
				new IntegerArgument("teamid", (int) IMagmaConstants.DEFAULT_TEAMID, 0, 127, "ID of the team");
		IntegerArgument playerIDArgument = new IntegerArgument(
				"playerid", 8, 1, IMagmaConstants.NUMBER_OF_PLAYERS_PER_TEAM, "player ID of the agent");
		StringArgument serverArgument = new StringArgument("server", IConnectionConstants.SERVER_IP, "server IP");
		IntegerArgument portArgument = new IntegerArgument("port", IConnectionConstants.AGENT_PORT, 0, "server port");
		StringArgument gameControllerIPArgument =
				new StringArgument("gameControllerIP", "localhost", "gameController IP");
		IntegerArgument serverVersionArgument = new IntegerArgument(
				"serverversion", IMagmaConstants.DEFAULT_SERVER_VERSION, 60, 72, "SimSpark server version");
		IntegerArgument fieldVersionArgument =
				new IntegerArgument("fieldversion", IHumanoidConstants.DEFAULT_FIELD_VERSION, 2014,
						IHumanoidConstants.DEFAULT_FIELD_VERSION, "Field version for humanoid");
		Collection<String> decisionMakers = new ArrayList<>();
		decisionMakers.addAll(DecisionMakerConfigurationHelper.NAO_DECISION_MAKERS.keySet());
		decisionMakers.addAll(DecisionMakerConfigurationHelper.SWEATY_DECISION_MAKERS.keySet());
		StringArgument decisionMakerArgument =
				new StringArgument("decisionmaker", IMagmaConstants.DEFAULT_DECISION_MAKER,
						decisionMakers.toArray(new String[0]), "decision maker of the agent");
		StringArgument factoryArgument = new StringArgument("factory", IMagmaConstants.DEFAULT_FACTORY,
				RobotConfigurationHelper.ROBOT_MODELS.keySet().toArray(new String[0]), "factory (robot model)");
		BooleanArgument roboVizDebugArgument = new BooleanArgument("roboVizDebug", "enables RoboViz visual debugging");
		StringArgument roboVizServerArgument =
				new StringArgument("roboVizServer", RoboVizDraw.DEFAULT_HOST, "RoboViz host IP");
		IntegerArgument roboVizPortArgument =
				new IntegerArgument("roboVizPort", RoboVizDraw.DEFAULT_PORT, 0, "RoboViz draw port");
		BooleanArgument reportStatsArgument =
				new BooleanArgument("reportStats", "report stats to the game series tool");
		BooleanArgument logPerceptionArgument =
				new BooleanArgument("logPerception", "log perceptions into json log file");
		StringArgument mixedTeamIPArgument = new StringArgument("mixedTeamIP", "localhost", "Teammate IP");
		IntegerArgument mixedTeamInPort = new IntegerArgument("mtInPort", 54321, 0, "mixed-team listening port");
		IntegerArgument mixedTeamOutPort = new IntegerArgument("mtOutPort", 12345, 0, "mixed-team sending port");
		BooleanArgument thinClientArgument = new BooleanArgument("thinClient", "enables the thin client");

		new HelpArgument(teamNameArgument, teamIDArgument, playerIDArgument, serverArgument, portArgument,
				serverVersionArgument, decisionMakerArgument, factoryArgument, roboVizDebugArgument,
				roboVizServerArgument, roboVizPortArgument, reportStatsArgument, gameControllerIPArgument,
				logPerceptionArgument, mixedTeamIPArgument, mixedTeamInPort, mixedTeamOutPort)
				.parse(args);

		String teamName = teamNameArgument.parse(args);
		byte teamID = Byte.parseByte(Integer.toString(teamIDArgument.parse(args)));
		int playerID = playerIDArgument.parse(args);
		String server = serverArgument.parse(args);
		int port = portArgument.parse(args);
		String gameControllerIP = gameControllerIPArgument.parse(args);
		int serverVersion = serverVersionArgument.parse(args);
		int fieldVersion = fieldVersionArgument.parse(args);
		String decisionMakerName = decisionMakerArgument.parse(args);
		String factory = factoryArgument.parse(args);
		boolean roboVizDebug = roboVizDebugArgument.parse(args);
		String roboVizServer = roboVizServerArgument.parse(args);
		int roboVizPort = roboVizPortArgument.parse(args);
		boolean reportStats = reportStatsArgument.parse(args);
		boolean logPerception = logPerceptionArgument.parse(args);
		String mixedTeamIP = mixedTeamIPArgument.parse(args);
		int mtInPort = mixedTeamInPort.parse(args);
		int mtOutPort = mixedTeamOutPort.parse(args);
		boolean thinClient = thinClientArgument.parse(args);
		Argument.endParse(args);

		RoboVizParameters roboVizParams = new RoboVizParameters(roboVizDebug, roboVizServer, roboVizPort, playerID);
		ComponentFactory componentFactory = RobotConfigurationHelper.getComponentFactory(factory, playerID, thinClient);

		return new PlayerParameters(teamName, teamID, playerID, server, port, serverVersion, componentFactory,
				decisionMakerName, roboVizParams, reportStats, thinClient);
	}
}
