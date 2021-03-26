/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.thoughtmodel;

import hso.autonomy.agent.communication.action.IAction;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.worldmodel.IWorldModel;
import java.util.Map;

/**
 * Represents all information related to thoughts of the agent.<br>
 * While the {@link hso.autonomy.agent.model.worldmodel.IWorldModel} and {@link IAgentModel}
 * represent the measured states based on sensor information, the IThoughtModel
 * is meant to represent a higher level of information abstraction. Furthermore,
 * it is the central model to store all information that comes from inside the
 * agent and cannot be measured.<br>
 * To give a simple example: <i>IFOCalculator</i>:<br>
 * The location information for each player is a measured value and contained in
 * the {@link hso.autonomy.agent.model.worldmodel.IWorldModel}. The question which player is
 * closest to the ball can be calculated based on this information but
 * represents a thought which is not directly related to a sensor value. This
 * "thought" (which player is closest to the ball) can be integrated and
 * buffered in the IThoughtModel, which improves reusability of common
 * calculations and therefore reduces runtime.
 *
 * @author Stefan Glaser
 */
public interface IThoughtModel {
	IAgentModel getAgentModel();

	IWorldModel getWorldModel();

	/**
	 * Update models based on new perception information.
	 *
	 * @param perception The actual perception or <code>null</code> if only the
	 *        internal state of the IThoughtModel should be refreshed
	 */
	boolean update(IPerception perception);

	/**
	 * This method is called from the AgentRuntime in order to map the
	 * target-state of the model layer into effector-actions, which can then be
	 * sent to the server.
	 * @param action the action component to direct the effectors to
	 * @param controlled true if this runtime is remote controlled
	 */
	void mapStateToAction(IAction action, boolean controlled);

	/**
	 * @return the map of truth values of this model
	 */
	Map<String, ITruthValue> getTruthValues();

	/**
	 * @param name the name of a truth value
	 * @return the truth value with the passed name
	 */
	ITruthValue getTruthValue(String name);
}
