/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.genetic.representation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import kdo.domain.IOptimizationProblem;
import kdo.domain.IOptimizationState;
import kdo.search.strategy.local.genetic.IOptimizationStateGroup;
import kdo.search.strategy.local.genetic.fitness.IFitnessCalculator;
import kdo.util.observer.IObserver;

/**
 * @author klaus
 *
 */
public abstract class OptimizationStateGroup implements IOptimizationStateGroup
{
	/** the states of this stateGroup, a list for each gender */
	protected List<List<IOptimizationState>> states;

	/** the comparator to use to compare states */
	protected final Comparator<IOptimizationState> stateComparator;

	/** the overall number of states in all genders */
	protected int numberOfStates;

	/** the application domain to which this stateGroup is related */
	protected final IOptimizationProblem domain;

	/** the count for the iteration this StateGroup has been entered */
	protected int iteration;

	/** observer to inform about state changes */
	protected IObserver<IOptimizationStateGroup> observer;

	/** the time breeding is running now */
	protected long runtime;

	/** flag indicating that an interrupt is requested */
	protected boolean interrupted;

	/** the average diversity over all states and dimensions */
	protected float avgDiversity;

	/** the average utility over all states and dimensions */
	protected double avgUtility;

	/** fitness calculation strategy */
	private IFitnessCalculator fitnessCalculator;

	/**
	 * true if the utilities of states has been calculated during iteration,
	 * false if the stateGroup has just been created
	 */
	protected boolean utilitiesCalculated;

	/**
	 * true if the utility calculation is expensive and should be avoided if
	 * possible
	 */
	protected boolean expensiveUtilityCalculation;

	protected int numberOfGroups;

	/**
	 * @param domain the domain on which the stateGroup is positioned
	 * @param numberOfGroups the number of smaller groups into which the
	 *        stateGroup is split
	 * @param numberOfStates the number of states the stateGroup should contain
	 * @param expensiveUtilityCalculation true if the utility calculation is
	 *        expensive
	 */
	protected OptimizationStateGroup(IOptimizationProblem domain)
	{
		this.domain = domain;
		stateComparator = domain.getStateComparator();
	}

	public OptimizationStateGroup(IOptimizationProblem domain, int numberOfGroups, int numberOfStates,
			boolean expensiveUtilityCalculation, IFitnessCalculator fitnessCalculator,
			IObserver<IOptimizationStateGroup> observer, List<List<IOptimizationState>> states)
	{
		this.numberOfGroups = numberOfGroups;
		this.observer = observer;
		this.numberOfStates = numberOfStates;
		utilitiesCalculated = false;
		this.expensiveUtilityCalculation = expensiveUtilityCalculation;
		if (domain == null) {
			throw new IllegalArgumentException("No domain specified in GeneticSearchParams! : ");
		}
		this.domain = domain;

		if (numberOfStates < 4 || numberOfStates < numberOfGroups) {
			throw new IllegalArgumentException("Too few states in the stateGroup! : " + numberOfStates);
		}
		this.states = states;
		if (states == null) {
			int statesPerGroup = numberOfStates / numberOfGroups;
			this.states = generateInitialPopulation(numberOfGroups, statesPerGroup);
		}
		this.stateComparator = domain.getStateComparator();
		iteration = 0;
		this.fitnessCalculator = fitnessCalculator;
	}

	protected abstract List<List<IOptimizationState>> generateInitialPopulation(int numberOfGroups, int numberOfStates);

	/**
	 * Creates a randomly created state from the application domain this
	 * stateGroup is based on. The state is not added to the stateGroup.
	 * @return a randomly created state from the application domain this
	 *         stateGroup is based on
	 */
	@Override
	public IOptimizationState createRandomState()
	{
		return domain.getRandomState();
	}

	/**
	 *
	 * @param domain the problem to solve using genetic algorithms
	 * @param iterations the number of generations to run
	 * @param maxRuntime the maximal time to breed in ms
	 * @return the best state in the final stateGroup
	 */
	public IOptimizationState breed(int iterations, long maxRuntime)
	{
		long start = System.currentTimeMillis();
		IOptimizationState bestState = null;
		runtime = 0;
		int generationsStart = iteration;
		// printStates(states);
		avgUtility = calculateFitness();
		sortIndividuums();
		utilitiesCalculated = true;
		runtime = System.currentTimeMillis() - start;

		while (!canStop(generationsStart + iterations, maxRuntime)) {
			bestState = findBestState(bestState);
			if (bestState != null && expensiveUtilityCalculation) {
				System.out.println("generation: " + iteration + " average: " + avgUtility +
								   " best: " + bestState.getUtility() + " runtime: " + runtime);
			}

			states = createNewGroupFromOld();
			avgUtility = calculateFitness();
			sortIndividuums();

			// printStates(states);

			iteration++;
			runtime = System.currentTimeMillis() - start;
		}
		interrupted = false;
		if (bestState == null) {
			// no breeding possible
			return null;
		}
		if (observer != null) {
			observer.update(this);
		}
		return bestState;
	}

	public double calculateFitness()
	{
		return fitnessCalculator.calculateFitness(states);
	}

	//	private void printStates(List<List<IOptimizationState>> theStates)
	//	{
	//		StringBuffer result = new StringBuffer();
	//		result.append("Next generation: genders: " + theStates.size());
	//		for (List<IOptimizationState> genderList : theStates) {
	//			result.append(" count: " + genderList.size());
	//		}
	//		System.out.println(result.toString());
	//	}

	protected List<List<IOptimizationState>> createNewGroupFromOld()
	{
		List<List<IOptimizationState>> newStates = new ArrayList<>(states.size());
		createNewStates(newStates);
		return newStates;
	}

	protected IOptimizationState findBestState(IOptimizationState bestState)
	{
		IOptimizationState bestInPopulation = getBestState();
		// calculate fitness of each state
		if (bestState == null || stateComparator.compare(bestInPopulation, bestState) > 0) {
			bestState = bestInPopulation;
		}
		if (observer != null && (bestState == bestInPopulation || iteration % 10 == 0)) {
			observer.update(this);
		}
		return bestState;
	}

	/**
	 * @param iterations
	 * @param maxRuntime
	 * @param generationsStart
	 * @return
	 */
	protected boolean canStop(int maxGenerations, long maxRuntime)
	{
		return !(iteration < maxGenerations && runtime < maxRuntime);
	}

	/**
	 * Creates new states for all genders. The default creates empty lists
	 */
	protected void createNewStates(List<List<IOptimizationState>> newStates)
	{
		int genders = states.size();
		for (int net = 0; net < genders; net++) {
			newStates.add(new ArrayList<>());
		}
	}

	@Override
	public IOptimizationState getBestState()
	{
		return getBestState(getStateComparator());
	}

	public IOptimizationState getBestState(Comparator<IOptimizationState> comparator)
	{
		if (!utilitiesCalculated && expensiveUtilityCalculation) {
			return null;
		}

		IOptimizationState best = null;
		for (List<IOptimizationState> genderList : states) {
			IOptimizationState bestOfGender = Collections.max(genderList, comparator);
			if (best == null || comparator.compare(bestOfGender, best) > 0) {
				best = bestOfGender;
			}
		}
		return best;
	}

	public Comparator<IOptimizationState> getStateComparator()
	{
		return stateComparator;
	}

	@Override
	public int getSize()
	{
		return numberOfStates;
	}

	public int getGenders()
	{
		return states.size();
	}

	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer(1000);
		// print all states
		// for (List<IOptimizationState> state : states) {
		// buffer.append(state.getFitness()).append(": ");
		// int[] chromosom = state.getChromosom();
		// for (int gene: chromosom) {
		// buffer.append(gene).append(", ");
		// }
		// buffer.append("\n");
		// }

		// System.out
		// .println("Selected: "
		// + Arrays
		// .toString(((SelectionStrategy) selectionStrategy).sumSelected));
		// System.out
		// .println("Avg fitness: "
		// + Arrays
		// .toString(((SelectionStrategy) selectionStrategy).averageFitness));

		return buffer.toString();
	}

	/**
	 * @return the time that breeding has taken until now
	 */
	@Override
	public long getRuntime()
	{
		return runtime;
	}

	public void interrupt()
	{
		interrupted = true;
	}

	public List<IOptimizationState> getAllStates()
	{
		List<IOptimizationState> result = new ArrayList<>();
		for (List<IOptimizationState> genderList : states) {
			result.addAll(genderList);
		}
		return result;
	}

	@Override
	public List<IOptimizationState> getSortedStates()
	{
		if (!utilitiesCalculated && expensiveUtilityCalculation) {
			return null;
		}
		List<IOptimizationState> result = getAllStates();
		Collections.sort(result, getStateComparator());
		return result;
	}

	@Override
	public boolean isUtilitiesCalculated()
	{
		return utilitiesCalculated;
	}

	@Override
	public int getIterations()
	{
		return iteration;
	}

	@Override
	public float getAverageDiversity()
	{
		return avgDiversity;
	}

	@Override
	public double getAverageUtility()
	{
		return avgUtility;
	}

	protected void sortIndividuums()
	{
		for (List<IOptimizationState> indiList : states) {
			Collections.sort(indiList, stateComparator);
		}
	}
}
