/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.agentruntime;

import hso.autonomy.agent.communication.action.IAction;
import hso.autonomy.agent.communication.channel.IChannelManager;
import hso.autonomy.agent.communication.channel.IChannelManager.ChannelManagerStatus;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.communication.perception.IPerceptorMap;
import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.agent.model.worldmodel.IWorldModel;
import hso.autonomy.util.observer.IObserver;
import java.io.Serializable;

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
public abstract class AgentRuntime implements IObserver<IPerceptorMap>, Serializable
{
	/** the meta model describing the robot model */
	protected transient IAgentMetaModel agentMetaModel;

	/** low level perception processing of the agent */
	protected transient IPerception perception;

	/** low level action processing of the agent */
	protected transient IAction action;

	/** the model for all visible objects */
	protected IThoughtModel thoughtModel;

	/** all behaviors available for the agent */
	protected transient BehaviorMap behaviors;

	/** decision making instance */
	protected IDecisionMaker decisionMaker;

	private boolean printExceptionOnce = false;

	protected transient IChannelManager channelManager;

	protected boolean reportStats;

	protected int cycles = 0;

	/**
	 * This method handles the main-loop of the magma agent framework. It is
	 * called from the IServerConnection, when a new server-message is received.
	 * The main loop is responsible for triggering all components of the agent
	 * system in the correct sequence, in order to ensure a proper application
	 * flow! <br>
	 * If you are new to the agent framework, this method is a good starting
	 * point for understanding the flow of the agent framework. <b>Beware of
	 * changing the call order! This will definitively have massive effects on
	 * the agent system behavior!</b>
	 */
	@Override
	public void update(IPerceptorMap content)
	{
		try {
			// this is a model update call
			perception.updatePerceptors(content);

			// then trigger models to update all internal sensor-values and
			// process vision-sensor information
			// (Localizer component is triggered inside WorldModel).
			boolean decide = thoughtModel.update(perception);

			if (decide) {
				// allow behaviors to do pre-decision updates
				onBeforeDecide();

				// Once the model-layer is updated, trigger the DecisionMaker in
				// order to make a decision, based on the updated situation.
				onDecide();

				// After a decision is taken, new commands should be set in the
				// model layer and are now ready to send - therefore trigger the
				// ThoughtModel to reflect the effector-commands
				onPrepareAction();

				// At the end of each cycle call the action to send the actual
				// actions (all effector-values) to the server.
				onSendAction();
			}

			onEndUpdateLoop();
			cycles++;

		} catch (Throwable e) {
			System.err.println("Crash!!! " + e);
			if (!printExceptionOnce) {
				e.printStackTrace();
				printExceptionOnce = true;
			}
		}
	}

	protected void onPrepareAction()
	{
		thoughtModel.mapStateToAction(action, false);
	}

	protected void onSendAction()
	{
		action.sendAction();
	}

	protected void onBeforeDecide()
	{
		for (IBehavior behavior : behaviors.getMap().values()) {
			behavior.preDecisionUpdate();
		}
	}

	protected void onDecide()
	{
		decisionMaker.decide();
	}

	protected void onEndUpdateLoop()
	{
	}

	/**
	 * Starts the connection to the server, will only return after disconnection
	 *
	 * @return true, if connection was successful and the agent stopped receiving
	 *         messages, false if connection was refused or the server shut down
	 */
	public boolean startClient()
	{
		if (!channelManager.start()) {
			return false;
		}

		while (channelManager.isConnected()) {
			try {
				synchronized (channelManager)
				{
					channelManager.wait();
				}
				IPerceptorMap nextMap;
				while ((nextMap = channelManager.getNextPerceptorMap()) != null) {
					update(nextMap);
				}
			} catch (InterruptedException | RuntimeException e) {
				e.printStackTrace();
			}
		}

		onClientStopped();

		return channelManager.getStatus() != ChannelManagerStatus.LOST_MAIN_CONNECTION;
	}

	protected void onClientStopped()
	{
	}

	/**
	 * Check if the agent is connected to the server
	 */
	public boolean isConnected()
	{
		return channelManager.isConnected();
	}

	/**
	 * Stops the connection to the server after the next message was received
	 */
	public void stopClient()
	{
		channelManager.stop();
	}

	public IPerception getPerception()
	{
		return perception;
	}

	public IAction getAction()
	{
		return action;
	}

	public IAgentModel getAgentModel()
	{
		return thoughtModel.getAgentModel();
	}

	public IWorldModel getWorldModel()
	{
		return thoughtModel.getWorldModel();
	}

	public IThoughtModel getThoughtModel()
	{
		return thoughtModel;
	}

	public BehaviorMap getBehaviors()
	{
		return behaviors;
	}

	public IDecisionMaker getDecisionMaker()
	{
		return decisionMaker;
	}

	public IChannelManager getChannelManager()
	{
		return channelManager;
	}
}
