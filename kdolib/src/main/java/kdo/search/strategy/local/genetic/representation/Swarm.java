/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.genetic.representation;

import java.util.ArrayList;
import java.util.List;
import kdo.domain.IOptimizationProblem;
import kdo.domain.IOptimizationState;
import kdo.domain.IPSOIndividuum;
import kdo.domain.model.Particle;
import kdo.search.strategy.local.genetic.ISwarm;
import kdo.search.strategy.local.genetic.fitness.IFitnessCalculator;
import kdo.util.observer.IObserver;

public class Swarm extends OptimizationStateGroup implements ISwarm
{
	private IObserver<ISwarm> observer;

	public Swarm(IOptimizationProblem domain)
	{
		super(domain);
	}

	public Swarm(IOptimizationProblem domain, int numberOfGroups, int numberOfStates,
			boolean expensiveUtilityCalculation, IFitnessCalculator fitnessCalculator, IObserver<ISwarm> observer,
			List<List<IOptimizationState>> states)
	{
		super(domain, numberOfGroups, numberOfStates, expensiveUtilityCalculation, fitnessCalculator, null, states);
		// TODO care for observer of super type
		this.observer = observer;
	}

	@Override
	public void initializeParticles()
	{
		if (states == null) {
			states = generateInitialPopulation(numberOfGroups, numberOfStates);
		}
		for (List<IOptimizationState> stateGroup : states) {
			for (IOptimizationState state : stateGroup) {
				((IPSOIndividuum) state).initializeVelocity();
			}
		}
	}

	@Override
	public void updateGroups()
	{
		float[] bestOfGroup = null;
		float bestValue = 0f;
		for (List<IOptimizationState> stateGroup : states) {
			for (IOptimizationState state : stateGroup) {
				if (bestOfGroup == null ||
						(domain.isMaximize() && bestValue < ((IPSOIndividuum) state).getCurrentValue()) ||
						(!domain.isMaximize() && bestValue > ((IPSOIndividuum) state).getCurrentValue())) {
					bestOfGroup = ((IPSOIndividuum) state).getState();
					bestValue = ((IPSOIndividuum) state).getCurrentValue();
				}
			}
		}
		for (List<IOptimizationState> stateGroup : states) {
			for (IOptimizationState state : stateGroup) {
				((IPSOIndividuum) state).updateBestNeighbor(bestOfGroup, bestValue);
			}
		}
		observer.update(this);
	}

	@Override
	protected List<List<IOptimizationState>> generateInitialPopulation(int numberOfGroups, int numberOfStates)
	{
		List<List<IOptimizationState>> result = new ArrayList<>();

		int actualStates = 0;

		for (int groups = 0; groups < numberOfGroups; groups++) {
			List<IOptimizationState> group = new ArrayList<>();
			result.add(group);
		}

		while (actualStates < numberOfStates) {
			for (int i = 0; i < numberOfGroups && actualStates < numberOfStates; i++) {
				result.get(i).add(createRandomState());
				actualStates++;
			}
		}

		return result;
	}

	@Override
	public IPSOIndividuum search(int iterations, long maxRuntime)
	{
		long start = System.currentTimeMillis();
		IPSOIndividuum bestIndividuum = null;
		runtime = 0;
		int generationsStart = iteration;
		while (!canStop(generationsStart + iterations, maxRuntime)) {
			// Update position-value & personal best of every Particle
			updateParticleBest();

			// Update the Groups and the bestOfNeighborhood (global)
			updateGroups();

			// System.out.println(this);

			// Update velocity of every Particle
			updateParticleVelocity(iterations);

			// Update position of every particle
			updateParticlePosition();

			IPSOIndividuum firstIndividuum = (IPSOIndividuum) states.get(0).get(0);
			float bestNeighborValue = firstIndividuum.getBestNeighborValue();
			float[] bestNeighbor = firstIndividuum.getBestNeighborPosition();

			if (bestIndividuum == null ||
					(bestIndividuum.getCurrentValue() < bestNeighborValue && domain.isMaximize()) ||
					(bestIndividuum.getCurrentValue() > bestNeighborValue && !domain.isMaximize())) {
				bestIndividuum = new Particle(domain, bestNeighbor);
				bestIndividuum.setUtility(bestNeighborValue);
			}

			// System.out.println(
			// "it: " + iteration + " best: " + bestIndividuum.getUtility()
			// + " state: " + Arrays.toString(bestIndividuum.getState())
			// + " speed: " + Arrays.toString(bestNeighbor));

			iteration++;
			runtime = System.currentTimeMillis() - start;
		}
		if (bestIndividuum == null) {
			// no swarm search possible
			return null;
		}
		// printSituation(bestIndividuum, generations);
		observer.update(this);
		return bestIndividuum;
	}

	@Override
	public void updateParticleBest()
	{
		for (List<IOptimizationState> stateGroup : states) {
			for (IOptimizationState state : stateGroup) {
				((IPSOIndividuum) state).updateBest();
			}
		}
	}

	@Override
	public void updateParticleVelocity(int maxIterations)
	{
		for (List<IOptimizationState> stateGroup : states) {
			for (IOptimizationState state : stateGroup) {
				((IPSOIndividuum) state).updateVelocity(iteration, maxIterations);
			}
		}
	}

	@Override
	public void updateParticlePosition()
	{
		for (List<IOptimizationState> stateGroup : states) {
			for (IOptimizationState state : stateGroup) {
				((IPSOIndividuum) state).updatePosition();
			}
		}
	}

	@Override
	public String toString()
	{
		String result = "Swarm:\n";
		for (List<IOptimizationState> stateGroup : states) {
			for (IOptimizationState state : stateGroup) {
				result += ((IPSOIndividuum) state).toString();
			}
		}
		return result;
	}
}
