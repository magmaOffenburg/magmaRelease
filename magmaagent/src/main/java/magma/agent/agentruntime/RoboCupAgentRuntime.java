/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.agentruntime;

import hso.autonomy.agent.agentruntime.AgentRuntime;
import hso.autonomy.agent.communication.perception.IPerceptionLogger;
import hso.autonomy.agent.communication.perception.IPerceptorMap;
import kdo.util.parameter.ParameterMap;
import magma.agent.communication.action.IRoboCupAction;
import magma.agent.communication.perception.IRoboCupPerception;
import magma.agent.decision.evaluator.IDecisionEvaluator;
import magma.agent.decision.evaluator.impl.DecisionEvaluationManager;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmeta.IRoboCupWorldMetaModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.util.roboviz.RoboVizDraw;

/**
 * The AgentRuntime is the core orchestrating component in the magma
 * agent-framework.
 * <h5>Tasks:</h5>
 * <ul>
 * <li>Create and setup components based on ComponentFactory</li>
 * <li>Manage internal triggering of all components during a "agent-cycle"</li>
 * </ul>
 *
 * @author Stefan Glaser
 */
public class RoboCupAgentRuntime extends AgentRuntime
{
	/** the meta model describing the rc-server */
	protected transient IRoboCupWorldMetaModel worldMetaModel;

	private IPerceptionLogger perceptionLogger;

	private transient IDecisionEvaluator evaluator;

	/**
	 * @param params - the configuration parameters to setup the client
	 */
	public RoboCupAgentRuntime(PlayerParameters params)
	{
		ComponentFactory factory = params.getComponentFactory();

		factory.loadProperties(params.getPlayerNumber());

		// meta models
		agentMetaModel = factory.getAgentMetaModel();
		worldMetaModel = factory.createWorldMetaModel(params.getServerVersion());

		// protocol layer
		channelManager = factory.createChannelManager(params.getChannelParams());

		perception = factory.createPerception();

		action = factory.createAction(channelManager, agentMetaModel);

		initialize(params);

		if (params.getChannelParams().shouldLogPerception()) {
			perceptionLogger = channelManager.getPerceptionLogger();
			perceptionLogger.start();
		}
	}

	public void initialize(PlayerParameters params)
	{
		ComponentFactory factory = params.getComponentFactory();

		// model layer
		IRoboCupAgentModel agentModel = factory.createAgentModel(getAgentMetaModel());

		IRoboCupWorldModel worldModel =
				factory.createWorldModel(agentModel, worldMetaModel, params.getTeamname(), params.getPlayerNumber());

		RoboVizDraw roboVizDraw = new RoboVizDraw(params.getRoboVizParams());

		IRoboCupThoughtModel thoughtModel = factory.createThoughtModel(agentModel, worldModel, roboVizDraw);
		thoughtModel.setRoleManager(factory.createRoleManager(thoughtModel, worldModel));
		thoughtModel.setKickPositionProfiler(factory.createKickPositionProfiler(thoughtModel));
		this.thoughtModel = thoughtModel;

		// control layer
		ParameterMap parameterMap = factory.createParameters();
		behaviors = factory.createBehaviors(thoughtModel, parameterMap);

		// decision making layer
		decisionMaker = factory.createDecisionMaker(
				behaviors, thoughtModel, params.getPlayerNumber(), params.getDecisionMakerName(), parameterMap);

		reportStats = params.getReportStats();
		evaluator = new DecisionEvaluationManager(decisionMaker, thoughtModel);
	}

	@Override
	public void update(IPerceptorMap content)
	{
		if (perceptionLogger != null) {
			perceptionLogger.log(content);
		}
		super.update(content);
	}

	@Override
	protected void onClientStopped()
	{
		if (reportStats) {
			new GameSeriesReporter(behaviors, getWorldModel().getPlaySide(), cycles).report();
		}
		if (perceptionLogger != null) {
			perceptionLogger.stop();
		}
	}

	public IRoboCupWorldMetaModel getWorldMetaModel()
	{
		return worldMetaModel;
	}

	public IRoboCupAgentMetaModel getAgentMetaModel()
	{
		return (IRoboCupAgentMetaModel) agentMetaModel;
	}

	@Override
	public IRoboCupPerception getPerception()
	{
		return (IRoboCupPerception) perception;
	}

	@Override
	public IRoboCupAction getAction()
	{
		return (IRoboCupAction) action;
	}

	@Override
	public IRoboCupAgentModel getAgentModel()
	{
		return (IRoboCupAgentModel) thoughtModel.getAgentModel();
	}

	@Override
	public IRoboCupWorldModel getWorldModel()
	{
		return (IRoboCupWorldModel) thoughtModel.getWorldModel();
	}

	@Override
	public IRoboCupThoughtModel getThoughtModel()
	{
		return (IRoboCupThoughtModel) super.getThoughtModel();
	}

	public float getCycleTime()
	{
		return getAgentModel().getCycleTime();
	}

	public int getGoalPredictionTime()
	{
		return getAgentModel().getGoalPredictionTime();
	}

	public float getTorsoZUpright()
	{
		return getAgentModel().getTorsoZUpright();
	}

	@Override
	protected void onEndUpdateLoop()
	{
		evaluator.evaluate();
	}
}
