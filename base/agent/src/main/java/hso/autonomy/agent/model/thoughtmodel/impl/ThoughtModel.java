/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.thoughtmodel.impl;

import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.agent.model.thoughtmodel.ITruthValue;
import hso.autonomy.agent.model.worldmodel.IWorldModel;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Stefan Glaser
 */
public abstract class ThoughtModel implements IThoughtModel, Serializable
{
	private final IAgentModel agentModel;

	private final IWorldModel worldModel;

	protected final Map<String, ITruthValue> truthValues;

	public ThoughtModel(IAgentModel agentModel, IWorldModel worldModel)
	{
		this.agentModel = agentModel;
		this.worldModel = worldModel;
		truthValues = new HashMap<>();
	}

	@Override
	public IAgentModel getAgentModel()
	{
		return agentModel;
	}

	@Override
	public IWorldModel getWorldModel()
	{
		return worldModel;
	}

	@Override
	public Map<String, ITruthValue> getTruthValues()
	{
		return truthValues;
	}

	@Override
	public ITruthValue getTruthValue(String name)
	{
		return truthValues.get(name);
	}

	/**
	 * Check if the truth value with the given name is valid.
	 *
	 * @param name the name of the truth value
	 * @return true if a truth value with the given name exists and is valid, false otherwise
	 */
	protected boolean isTrue(String name)
	{
		ITruthValue tv = truthValues.get(name);
		return tv != null ? tv.isValid() : false;
	}

	@Override
	public boolean update(IPerception perception)
	{
		boolean result = false;
		if (perception != null) {
			// Trigger agent model to update all internal sensor values
			result = agentModel.update(perception);

			// Trigger world model to process vision information
			result = worldModel.update(perception) || result;

			// update all truth values with state
			for (ITruthValue value : truthValues.values()) {
				value.update(this);
			}
		}

		return result;
	}
}
