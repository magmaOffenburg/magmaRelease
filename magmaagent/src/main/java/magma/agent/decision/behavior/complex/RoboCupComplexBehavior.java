/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.complex.ComplexBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel;

/**
 * @author kdorer
 *
 */
public abstract class RoboCupComplexBehavior extends ComplexBehavior
{
	public RoboCupComplexBehavior(
			String name, IThoughtModel thoughtModel, BehaviorMap behaviors, String defaultBehaviorName)
	{
		super(name, thoughtModel, behaviors, defaultBehaviorName);
	}

	public RoboCupComplexBehavior(String name, IThoughtModel thoughtModel, BehaviorMap behaviors)
	{
		super(name, thoughtModel, behaviors);
	}

	public IRoboCupThoughtModel getThoughtModel()
	{
		return (IRoboCupThoughtModel) super.getThoughtModel();
	}

	public IRoboCupWorldModel getWorldModel()
	{
		return (IRoboCupWorldModel) super.getWorldModel();
	}

	public IRoboCupAgentModel getAgentModel()
	{
		return (IRoboCupAgentModel) super.getAgentModel();
	}
}
