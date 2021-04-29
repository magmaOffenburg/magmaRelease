/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.movement;

import hso.autonomy.agent.model.agentmodel.IAgentModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author dorer
 */
public class Movement implements Serializable
{
	/** the current phase we are in for performing */
	private int currentPhaseIndex;

	/** the phases of this behavior */
	private List<MovementPhase> phases;

	/** the name of this movement */
	private final String name;

	public Movement(String name)
	{
		this.name = name;
		currentPhaseIndex = 0;
		phases = new ArrayList<>();
	}

	/**
	 * Adds a phase to the list of phases
	 * @param phase the phase to add
	 */
	public Movement add(MovementPhase phase)
	{
		MovementPhase previousPhase = null;
		if (!phases.isEmpty()) {
			previousPhase = phases.get(phases.size() - 1);
		}
		phase.setSpeeds(previousPhase);
		phases.add(phase);
		return this;
	}

	public void init()
	{
		currentPhaseIndex = 0;
		phases.forEach(MovementPhase::init);
	}

	/**
	 * @param agent reference to the agent model
	 * @return false if this movement is over
	 */
	public boolean perform(IAgentModel agent)
	{
		if (isFinished()) {
			return false;
		}

		MovementPhase currentPhase = getCurrentPhase();
		boolean result = currentPhase.perform(agent);
		if (!result) {
			currentPhaseIndex++;
			if (isFinished()) {
				return false;
			}
		}
		return true;
	}

	public MovementPhase getCurrentPhase()
	{
		return phases.get(currentPhaseIndex);
	}

	public List<MovementPhase> getPhases()
	{
		return Collections.unmodifiableList(phases);
	}

	public MovementPhase getPhase(String name)
	{
		for (MovementPhase phase : phases) {
			if (phase.getName().equals(name)) {
				return phase;
			}
		}
		return null;
	}

	/**
	 * @return true if this movement is over
	 */
	public boolean isFinished()
	{
		return currentPhaseIndex >= phases.size();
	}

	/**
	 * goes to the next phase no matter if this phase is finished
	 */
	public boolean proceed()
	{
		getCurrentPhase().init();
		currentPhaseIndex++;
		return !isFinished();
	}

	public Movement getLeftVersion()
	{
		String newName = name.replace("Right", "Left");
		Movement result = new Movement(newName);
		for (MovementPhase current : phases) {
			MovementPhase newPhase = current.getLeftVersion();
			result.add(newPhase);
		}
		return result;
	}

	public void setRelativeSpeed(float speed)
	{
		for (MovementPhase phase : phases) {
			phase.setRelativeSpeed(speed);
		}
	}

	@Override
	public String toString()
	{
		return name + ": " + (isFinished() ? "finished" : getCurrentPhase());
	}
}
