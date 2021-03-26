/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.impl.Perception;
import java.util.List;
import java.util.stream.Collectors;
import magma.agent.communication.perception.IAgentStatePerceptor;
import magma.agent.communication.perception.IGameStatePerceptor;
import magma.agent.communication.perception.IHearPerceptor;
import magma.agent.communication.perception.IPlayerPos;
import magma.agent.communication.perception.IRoboCupPerception;

/**
 * Represents all data the agent is able to perceive from its environment.
 * Should be updated in every simulation cycle.
 *
 * @author Klaus Dorer, Simon Raffeiner, Stefan Glaser
 */
public class RoboCupPerception extends Perception implements IRoboCupPerception
{
	@Override
	public IAgentStatePerceptor getAgentState()
	{
		return (IAgentStatePerceptor) perceptors.get("AgentState");
	}

	@Override
	public IGameStatePerceptor getGameState()
	{
		return (IGameStatePerceptor) perceptors.get(IGameStatePerceptor.NAME);
	}

	@Override
	public List<IHearPerceptor> getHearPerceptors()
	{
		return perceptors.values()
				.stream()
				.filter(perceptor -> perceptor instanceof IHearPerceptor)
				.map(perceptor -> (IHearPerceptor) perceptor)
				.collect(Collectors.toList());
	}

	@Override
	public List<IPlayerPos> getVisiblePlayers()
	{
		return perceptors.values()
				.stream()
				.filter(perceptor -> perceptor instanceof IPlayerPos)
				.map(perceptor -> (IPlayerPos) perceptor)
				.collect(Collectors.toList());
	}
}