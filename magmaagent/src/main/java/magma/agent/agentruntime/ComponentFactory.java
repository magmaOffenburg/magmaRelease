/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.agentruntime;

import hso.autonomy.agent.communication.action.IActionPerformer;
import hso.autonomy.agent.communication.channel.IChannelManager;
import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.basic.None;
import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.agentmodel.ISensorFactory;
import hso.autonomy.agent.model.agentmodel.impl.DefaultSensorFactory;
import hso.autonomy.agent.model.agentmodel.impl.ik.IAgentIKSolver;
import hso.autonomy.agent.model.agentmodel.impl.ik.impl.JacobianTransposeAgentIKSolver;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.agent.model.worldmodel.IWorldModel;
import hso.autonomy.agent.model.worldmodel.localizer.IFeatureLocalizer;
import hso.autonomy.agent.model.worldmodel.localizer.impl.KalmanLocalizer;
import hso.autonomy.agent.model.worldmodel.localizer.impl.LocalizerFieldNormal;
import hso.autonomy.util.file.FileUtil;
import java.io.IOException;
import java.net.URISyntaxException;
import kdo.util.parameter.ParameterMap;
import magma.agent.UglyConstants;
import magma.agent.communication.action.IRoboCupAction;
import magma.agent.communication.action.impl.RoboCupAction;
import magma.agent.communication.channel.impl.ChannelParameters;
import magma.agent.communication.perception.IRoboCupPerception;
import magma.agent.communication.perception.impl.RoboCupPerception;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.decision.behavior.basic.BeamHome;
import magma.agent.decision.behavior.basic.BeamToPosition;
import magma.agent.decision.behavior.basic.SayPositions;
import magma.agent.decision.behavior.basic.StopInstantly;
import magma.agent.decision.behavior.supportPoint.FunctionBehavior;
import magma.agent.decision.behavior.supportPoint.FunctionBehaviorParameters;
import magma.agent.decision.decisionmaker.impl.GoalieDecisionMaker;
import magma.agent.decision.decisionmaker.impl.SoccerGoalieDecisionMaker;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.agentmodel.impl.RoboCupAgentModel;
import magma.agent.model.thoughtmodel.IKickPositionProfiler;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.impl.KickPositionProfilerGoal;
import magma.agent.model.thoughtmodel.impl.RoboCupThoughtModel;
import magma.agent.model.thoughtmodel.impl.RoboCupThoughtModelThin;
import magma.agent.model.thoughtmodel.strategy.IRoleManager;
import magma.agent.model.thoughtmodel.strategy.impl.RoleManager;
import magma.agent.model.thoughtmodel.strategy.impl.strategies.StrategyConfigurationHelper;
import magma.agent.model.worldmeta.IRoboCupWorldMetaModel;
import magma.agent.model.worldmeta.RCServerConfigurationHelper;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.impl.RoboCupWorldModel;
import magma.util.roboviz.RoboVizDraw;

/**
 * Factory class for the components of our RoboCup player
 *
 * @author Klaus Dorer
 */
public abstract class ComponentFactory implements IBehaviorConstants
{
	public abstract IRoboCupAgentMetaModel getAgentMetaModel();

	/**
	 * @return the channel manager including the channels
	 */
	public abstract IChannelManager createChannelManager(ChannelParameters info);

	public IAgentIKSolver createAgentIKSolver()
	{
		return new JacobianTransposeAgentIKSolver();
	}

	/**
	 * Create a new world meta model based on serverVersion.
	 *
	 * @param serverVersion - the version of the meta model
	 */
	public IRoboCupWorldMetaModel createWorldMetaModel(int serverVersion)
	{
		return RCServerConfigurationHelper.getRCServerMetalModel(serverVersion);
	}

	public IRoboCupPerception createPerception()
	{
		return new RoboCupPerception();
	}

	/**
	 * Create an new Action
	 *
	 * @param actionPerformer Server connection
	 */
	public IRoboCupAction createAction(IActionPerformer actionPerformer, IAgentMetaModel metaModel)
	{
		return new RoboCupAction(actionPerformer);
	}

	public IRoboCupAgentModel createAgentModel(IRoboCupAgentMetaModel metaModel)
	{
		return new RoboCupAgentModel(metaModel, createSensorFactory(), createAgentIKSolver());
	}

	protected ISensorFactory createSensorFactory()
	{
		return new DefaultSensorFactory();
	}

	public IRoboCupWorldModel createWorldModel(
			IRoboCupAgentModel agentModel, IRoboCupWorldMetaModel worldMetaModel, String teamName, int playerNumber)
	{
		return new RoboCupWorldModel(agentModel, createLocalizer(agentModel), worldMetaModel, teamName, playerNumber);
	}

	public IRoboCupThoughtModel createThoughtModel(
			IAgentModel agentModel, IRoboCupWorldModel worldModel, RoboVizDraw roboVizDraw)
	{
		if (UglyConstants.thinClient) {
			return new RoboCupThoughtModelThin(agentModel, worldModel, roboVizDraw);
		} else {
			return new RoboCupThoughtModel(agentModel, worldModel, roboVizDraw);
		}
	}

	public final IRoleManager createRoleManager(IRoboCupThoughtModel thoughtModel, IWorldModel worldModel)
	{
		return createRoleManager(thoughtModel, worldModel, StrategyConfigurationHelper.DEFAULT_STRATEGY);
	}

	public IRoleManager createRoleManager(
			IRoboCupThoughtModel thoughtModel, IWorldModel worldModel, String teamStrategyName)
	{
		return new RoleManager(
				worldModel, StrategyConfigurationHelper.STRATEGIES.get(teamStrategyName).create(thoughtModel));
	}

	public IKickPositionProfiler createKickPositionProfiler(IRoboCupThoughtModel thoughtModel)
	{
		return new KickPositionProfilerGoal(thoughtModel);
	}

	protected IFeatureLocalizer createLocalizer(IRoboCupAgentModel agentModel)
	{
		// reset distance is typically used after fouls
		return new KalmanLocalizer(new LocalizerFieldNormal(), 1.0);
	}

	public IDecisionMaker createDecisionMaker(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel,
			int playerNumber, String decisionMakerName, ParameterMap learningParam)
	{
		if (playerNumber == 1) {
			return new GoalieDecisionMaker(behaviors, thoughtModel);
		} else {
			return new SoccerGoalieDecisionMaker(behaviors, thoughtModel);
		}
	}

	/**
	 * Create all behavior objects that are used during the game
	 *
	 * @param thoughtModel Reference to the ThoughtModel
	 * @param params Behavior parametrization, null if default should be used
	 * @return A map of available behaviors
	 */
	public BehaviorMap createBehaviors(IRoboCupThoughtModel thoughtModel, ParameterMap params)
	{
		BehaviorMap behaviors = new BehaviorMap();

		readFunctionBehaviors(getBehaviorFilesBasePath(), thoughtModel, behaviors);

		behaviors.put(new None(thoughtModel));
		behaviors.put(new StopInstantly(thoughtModel));
		behaviors.put(new BeamHome(thoughtModel));
		behaviors.put(new BeamToPosition(thoughtModel));
		behaviors.put(new SayPositions(thoughtModel));

		createSpecificBehaviors(thoughtModel, params, behaviors);

		return behaviors;
	}

	/**
	 * Return the path to the behavior files base folder. This method is used to
	 * dynamically read all function behaviors.
	 *
	 * @return the path to the behavior files base folder
	 */
	protected abstract String getBehaviorFilesBasePath();

	/**
	 * Create all specific behavior objects.
	 *
	 * @param thoughtModel - the thought model
	 * @param params - behavior parametrization, null if default should be used
	 * @param behaviors - a map for storing the created specific behaviors
	 */
	protected abstract void createSpecificBehaviors(
			IRoboCupThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors);

	/**
	 * Reads function behavior files, creates corresponding objects and adds them
	 * to the map of behaviors.
	 */
	protected final void readFunctionBehaviors(String pathName, IThoughtModel thoughtModel, BehaviorMap behaviors)
	{
		try {
			String[] files = FileUtil.getResourceListing(pathName);
			for (String fileName : files) {
				createFunctionBehavior(thoughtModel, behaviors, pathName + fileName);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Function behavior file not found" + pathName);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			System.err.println("URI exception when reading: " + pathName);
		}
	}

	private void createFunctionBehavior(IThoughtModel thoughtModel, BehaviorMap behaviors, String filePath)
			throws IOException
	{
		if (!filePath.endsWith(FunctionBehaviorParameters.EXTENSION)) {
			return;
		}

		FunctionBehaviorParameters parameters = FunctionBehaviorParameters.readBehaviorFile(filePath);
		String name = parameters.getName();
		if (parameters.getMirror()) {
			// assume the file contains the right version
			behaviors.put(new FunctionBehavior(name + "Right", thoughtModel, parameters, filePath, false));
			behaviors.put(new FunctionBehavior(name + "Left", thoughtModel, parameters, filePath, true));
		} else {
			behaviors.put(new FunctionBehavior(name, thoughtModel, parameters, filePath, false));
		}
	}

	/**
	 * Creates behavior or decision maker parameters.
	 *
	 * @return a parameter map that has to contain all ParameterLists needed for default behaviors
	 */
	public ParameterMap createParameters()
	{
		ParameterMap result = new ParameterMap();

		// specific parameters have higher priority than general and are put after
		// them in order to allow overwriting
		result.putAll(createSpecificParameters());

		return result;
	}

	/**
	 * Overwrite this to create robot specific parametrization
	 */
	protected abstract ParameterMap createSpecificParameters();

	/**
	 * Instance for estimating times it takes to walk to
	 */
	public abstract IWalkEstimator createWalkEstimator();

	public abstract void loadProperties(int playerNumber);
}
