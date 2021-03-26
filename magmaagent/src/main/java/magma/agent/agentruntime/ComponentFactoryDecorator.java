/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.agentruntime;

import hso.autonomy.agent.communication.action.IActionPerformer;
import hso.autonomy.agent.communication.channel.IChannelManager;
import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.agentmodel.ISensorFactory;
import hso.autonomy.agent.model.agentmodel.impl.ik.IAgentIKSolver;
import hso.autonomy.agent.model.worldmodel.IWorldModel;
import hso.autonomy.agent.model.worldmodel.localizer.IFeatureLocalizer;
import kdo.util.parameter.ParameterMap;
import magma.agent.communication.action.IRoboCupAction;
import magma.agent.communication.channel.impl.ChannelParameters;
import magma.agent.communication.perception.IRoboCupPerception;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.thoughtmodel.IKickPositionProfiler;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.strategy.IRoleManager;
import magma.agent.model.worldmeta.IRoboCupWorldMetaModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.util.roboviz.RoboVizDraw;

public class ComponentFactoryDecorator extends ComponentFactory
{
	protected final ComponentFactory decoratee;

	public ComponentFactoryDecorator(ComponentFactory decoratee)
	{
		this.decoratee = decoratee;
	}

	@Override
	public IAgentIKSolver createAgentIKSolver()
	{
		return decoratee.createAgentIKSolver();
	}

	@Override
	public IRoboCupWorldMetaModel createWorldMetaModel(int serverVersion)
	{
		return decoratee.createWorldMetaModel(serverVersion);
	}

	@Override
	public IRoboCupPerception createPerception()
	{
		return decoratee.createPerception();
	}

	@Override
	public IRoboCupAction createAction(IActionPerformer actionPerformer, IAgentMetaModel metaModel)
	{
		return decoratee.createAction(actionPerformer, metaModel);
	}

	@Override
	public IRoboCupAgentModel createAgentModel(IRoboCupAgentMetaModel metaModel)
	{
		return decoratee.createAgentModel(metaModel);
	}

	@Override
	protected ISensorFactory createSensorFactory()
	{
		return decoratee.createSensorFactory();
	}

	@Override
	public IRoboCupWorldModel createWorldModel(
			IRoboCupAgentModel agentModel, IRoboCupWorldMetaModel metaModel, String teamName, int playerNumber)
	{
		return decoratee.createWorldModel(agentModel, metaModel, teamName, playerNumber);
	}

	@Override
	public IRoboCupThoughtModel createThoughtModel(
			IAgentModel agentModel, IRoboCupWorldModel worldModel, RoboVizDraw roboVizDraw)
	{
		return decoratee.createThoughtModel(agentModel, worldModel, roboVizDraw);
	}

	@Override
	public IRoleManager createRoleManager(
			IRoboCupThoughtModel thoughtModel, IWorldModel worldModel, String teamStrategyName)
	{
		return decoratee.createRoleManager(thoughtModel, worldModel, teamStrategyName);
	}

	@Override
	public IKickPositionProfiler createKickPositionProfiler(IRoboCupThoughtModel thoughtModel)
	{
		return decoratee.createKickPositionProfiler(thoughtModel);
	}

	@Override
	protected IFeatureLocalizer createLocalizer(IRoboCupAgentModel agentModel)
	{
		return decoratee.createLocalizer(agentModel);
	}

	@Override
	public IDecisionMaker createDecisionMaker(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel,
			int playerNumber, String decisionMakerName, ParameterMap learningParam)
	{
		return decoratee.createDecisionMaker(behaviors, thoughtModel, playerNumber, decisionMakerName, learningParam);
	}

	@Override
	public BehaviorMap createBehaviors(IRoboCupThoughtModel thoughtModel, ParameterMap params)
	{
		return decoratee.createBehaviors(thoughtModel, params);
	}

	@Override
	public ParameterMap createParameters()
	{
		return decoratee.createParameters();
	}

	@Override
	public IRoboCupAgentMetaModel getAgentMetaModel()
	{
		return decoratee.getAgentMetaModel();
	}

	@Override
	public IChannelManager createChannelManager(ChannelParameters info)
	{
		return decoratee.createChannelManager(info);
	}

	@Override
	protected String getBehaviorFilesBasePath()
	{
		return "";
	}

	@Override
	protected void createSpecificBehaviors(
			IRoboCupThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors)
	{
	}

	@Override
	protected ParameterMap createSpecificParameters()
	{
		return new ParameterMap();
	}

	@Override
	public IWalkEstimator createWalkEstimator()
	{
		return decoratee.createWalkEstimator();
	}

	@Override
	public void loadProperties(int playerNumber)
	{
		decoratee.loadProperties(playerNumber);
	}
}
