/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic.representation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import kdo.domain.IGeneticProblem;
import kdo.domain.IIndividuum;
import kdo.domain.IOptimizationProblem;
import kdo.domain.IOptimizationState;
import kdo.search.strategy.local.genetic.IMutation;
import kdo.search.strategy.local.genetic.IOptimizationStateGroup;
import kdo.search.strategy.local.genetic.IPopulation;
import kdo.search.strategy.local.genetic.IReproduction;
import kdo.search.strategy.local.genetic.ISelection;
import kdo.search.strategy.local.genetic.fitness.IFitnessCalculator;
import kdo.util.observer.IObserver;

/**
 * @author klaus
 *
 */
public class Population extends OptimizationStateGroup implements IPopulation, IOptimizationStateGroup
{
	/** the diversity below which breeding is stopped */
	private static float MIN_DIVERSITY = 1.00001f;

	/** selection strategy */
	private ISelection selectionStrategy;

	/** mutation strategy */
	private IMutation mutationStrategy;

	/** the strategy for reproduction */
	private IReproduction reproductionStrategy;

	/** the number of individuums to take over from old generation */
	protected int oldToNewGenerationCount;

	/** the number of parents a new individuum has */
	private final int parentsPerIndividuum;

	/**
	 * @param domain the domain on which the population is breeded
	 * @param numberOfGenders the number of genders into which the population is
	 *        split
	 * @param numberOfIndividuums the number of individuums the population should
	 *        contain
	 * @param selectionStrategy the strategy that does selection and
	 *        recombination
	 * @param reproductionStrategy the strategy that creates a new individuum
	 *        from the chromosoms of parents
	 * @param mutationStrategy the strategy that does mutation of individuums
	 * @param oldToNewGenerationRatio the relative amount of best individuums to
	 *        take over into next generation for each gender
	 * @param parentsPerIndividuum the number of parents that form a new
	 *        individuum
	 * @param expensiveUtilityCalculation true if the utility calculation is
	 *        expensive
	 */
	public Population(IGeneticProblem domain, int numberOfGenders, int numberOfIndividuums,
			ISelection selectionStrategy, IReproduction reproductionStrategy, IMutation mutationStrategy,
			float oldToNewGenerationRatio, int parentsPerIndividuum, boolean expensiveUtilityCalculation,
			IFitnessCalculator fitnessCalculator, IObserver<IOptimizationStateGroup> observer,
			List<List<IOptimizationState>> individuums)
	{
		super((IOptimizationProblem) domain, numberOfGenders, numberOfIndividuums, expensiveUtilityCalculation,
				fitnessCalculator, observer, individuums);
		if (domain == null) {
			throw new IllegalArgumentException("No domain specified in GeneticSearchParams! : ");
		}

		int individuumsPerGender = numberOfIndividuums / numberOfGenders;
		if (individuums != null) {
			List<List<IOptimizationState>> tempIs = new ArrayList<>();
			for (List<IOptimizationState> genIs : individuums) {
				List<IOptimizationState> tempI = new ArrayList<>();
				tempI.addAll(genIs);
				tempIs.add(tempI);
			}
			this.states = tempIs;
		}

		this.selectionStrategy = selectionStrategy;
		this.reproductionStrategy = reproductionStrategy;
		this.mutationStrategy = mutationStrategy;
		this.oldToNewGenerationCount = (int) (individuumsPerGender * oldToNewGenerationRatio);
		this.parentsPerIndividuum = parentsPerIndividuum;
		iteration = 0;
		avgDiversity = 2.0f;
		this.numberOfStates = individuumsPerGender * numberOfGenders;
		utilitiesCalculated = false;
		this.expensiveUtilityCalculation = expensiveUtilityCalculation;
	}

	@Override
	protected List<List<IOptimizationState>> generateInitialPopulation(int genders, int size)
	{
		List<List<IOptimizationState>> result = new ArrayList<>();
		for (int gender = 0; gender < genders; gender++) {
			List<IOptimizationState> genderIndividuums = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				genderIndividuums.add(createRandomState());
			}
			result.add(genderIndividuums);
		}
		return result;
	}

	@Override
	protected List<List<IOptimizationState>> createNewGroupFromOld()
	{
		// increase age of individuals
		age();
		List<List<IOptimizationState>> newStates = super.createNewGroupFromOld();
		takeOverGoodIndividuums(newStates);
		mutate(newStates);
		calculateAverageDiversity(newStates);
		return newStates;
	}

	/**
	 * @param iterations
	 * @param maxRuntime
	 * @param generationsStart
	 * @return
	 */
	@Override
	protected boolean canStop(int maxGenerations, long maxRuntime)
	{
		if (iteration < maxGenerations && runtime < maxRuntime && avgDiversity > MIN_DIVERSITY && !interrupted) {
			return false;
		}
		System.out.print("Stopped optimization: ");
		if (iteration >= maxGenerations) {
			System.out.println("reached max generations: " + maxGenerations + " generations: " + iteration);
		} else if (runtime >= maxRuntime) {
			System.out.println("reached max runtime: " + maxRuntime + " runtime: " + runtime);
		} else if (avgDiversity < MIN_DIVERSITY) {
			System.out.println("reached min diversity: " + MIN_DIVERSITY + " avgDiversity: " + avgDiversity);
		} else {
			System.out.println("interrupted");
		}
		return true;
	}

	/**
	 * Adds one to the age of each individuum
	 */
	private void age()
	{
		for (List<IOptimizationState> indiList : states) {
			for (IOptimizationState currentIndividuum : indiList) {
				currentIndividuum.incrementIteration();
			}
		}
	}

	/**
	 * Runs mutations for all genders
	 */
	@SuppressWarnings("unchecked")
	private void mutate(List<List<IOptimizationState>> newStates)
	{
		for (List<IOptimizationState> indiListUncast : newStates) {
			List<IIndividuum> indiList = (List<IIndividuum>) (List<?>) indiListUncast;
			mutationStrategy.mutate(indiList);
		}
	}

	/**
	 * Removes all individuums from the list of individuums of all genders except
	 * for the n best
	 */
	private void takeOverGoodIndividuums(List<List<IOptimizationState>> newStates)
	{
		int genders = states.size();
		for (int gender = 0; gender < genders; gender++) {
			List<IOptimizationState> indiList = states.get(gender);
			List<IOptimizationState> newList = newStates.get(gender);
			int size = indiList.size();
			// take over n best
			newList.addAll(indiList.subList(size - oldToNewGenerationCount, size));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void createNewStates(List<List<IOptimizationState>> newStates)
	{
		super.createNewStates(newStates);

		int genders = states.size();
		for (int gender = 0; gender < genders; gender++) {
			List<IIndividuum> indiList = (List<IIndividuum>) (List<?>) states.get(gender);
			List<IOptimizationState> newList = newStates.get(gender);
			int size = indiList.size();

			selectionStrategy.onBeforeSelection(indiList);

			int newIndividuumsToGenerate = size - oldToNewGenerationCount;
			int generated = 0;
			// We have to make sure that also when having only 1 gender we select 2
			// parents
			IIndividuum[] parents = new IIndividuum[parentsPerIndividuum];
			while (generated < newIndividuumsToGenerate) {
				// select the parents
				for (int parentgender = 0; parentgender < parentsPerIndividuum; parentgender++) {
					List<IIndividuum> otherGender =
							(List<IIndividuum>) (List<?>) states.get((gender + parentgender) % genders);
					parents[parentgender] =
							selectionStrategy.selectIndividuum(this, otherGender, generated, parents, parentgender);
				}
				IOptimizationState child = reproductionStrategy.crossOver(parents);
				newList.add(child);
				generated++;
			}
		}
	}

	public IOptimizationState getBestIndividuum()
	{
		return getBestIndividuum(stateComparator);
	}

	@Override
	public IOptimizationState getBestIndividuum(Comparator<IOptimizationState> comparator)
	{
		if (!utilitiesCalculated && expensiveUtilityCalculation) {
			return null;
		}

		IOptimizationState best = null;
		for (List<IOptimizationState> genderListUncast : states) {
			List<IOptimizationState> genderList = genderListUncast;
			IOptimizationState bestOfGender = Collections.max(genderList, comparator);
			if (best == null || comparator.compare(bestOfGender, best) > 0) {
				best = bestOfGender;
			}
		}
		return best;
	}

	@Override
	public int getSize()
	{
		return numberOfStates;
	}

	@Override
	public int getGenders()
	{
		return states.size();
	}

	@Override
	public int getGenerations()
	{
		return iteration;
	}

	/**
	 * @return the number of different genes for each position
	 */
	protected int[] getDiversity(List<List<IOptimizationState>> newStates)
	{
		IIndividuum firstIndividuum = (IIndividuum) newStates.get(0).get(0);
		int genesize = firstIndividuum.getChromosom().length;
		int[] result = new int[genesize];

		// run through all gene positions
		for (int gene = 0; gene < genesize; gene++) {
			HashMap<Float, Float> currentMap = new HashMap<>(newStates.size());

			// run through all individuums
			for (List<IOptimizationState> currentGender : newStates) {
				for (IOptimizationState currentIndividuum : currentGender) {
					Float key = ((IIndividuum) currentIndividuum).getChromosom()[gene];
					// TODO we could also count the number of occurrences in the
					// value
					currentMap.put(key, key);
				}
			}
			result[gene] = currentMap.size();
		}

		return result;
	}

	/**
	 * @return the average diversity of all genes in the population
	 */
	@Override
	public float getAverageDiversity()
	{
		return avgDiversity;
	}

	/**
	 * Calculates the average diversity
	 * @return the average diversity of all genes in the population
	 */
	public float calculateAverageDiversity(List<List<IOptimizationState>> newStates)
	{
		int[] diversity = getDiversity(newStates);
		long sum = 0;
		for (int divers : diversity) {
			// calculate average
			sum += divers;
		}
		avgDiversity = sum / (float) diversity.length;
		return avgDiversity;
	}

	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer(1000);
		// print all individuums
		// for (IIndividuum individuum : individuums) {
		// buffer.append(individuum.getFitness()).append(": ");
		// int[] chromosom = individuum.getChromosom();
		// for (int gene: chromosom) {
		// buffer.append(gene).append(", ");
		// }
		// buffer.append("\n");
		// }

		// print diversity information
		// int[] diversity = getDiversity();
		// long sum = 0;
		// buffer.append("diversity: ");
		// for (int divers : diversity) {
		// // calculate average
		// sum += divers;
		// buffer.append(divers).append(", ");
		// }
		// buffer.append("avg diversity: ").append(sum / (float)
		// diversity.length);
		buffer.append("avg diversity: ").append(getAverageDiversity());
		buffer.append(" genders: ").append(states.size());
		buffer.append(" individuums: ").append(numberOfStates);
		buffer.append(" parentsPerIndividuum: ").append(parentsPerIndividuum);
		buffer.append(" oldToNewGenerationCount: ").append(oldToNewGenerationCount);

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

	@Override
	public void interrupt()
	{
		interrupted = true;
	}

	public List<IOptimizationState> getAllIndividuums()
	{
		List<IOptimizationState> result = new ArrayList<>();
		for (List<IOptimizationState> genderList : states) {
			result.addAll(genderList);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<IIndividuum> getSortedIndividuums()
	{
		if (!utilitiesCalculated && expensiveUtilityCalculation) {
			return null;
		}
		List<IIndividuum> result = (List<IIndividuum>) (List<?>) getAllIndividuums();
		Collections.sort(result, stateComparator);
		return result;
	}

	/**
	 * @param selectionStrategy the selectionStrategy to set
	 */
	@Override
	public void setSelectionStrategy(ISelection selectionStrategy)
	{
		this.selectionStrategy = selectionStrategy;
	}

	/**
	 * @param mutationStrategy the mutationStrategy to set
	 */
	@Override
	public void setMutationStrategy(IMutation mutationStrategy)
	{
		this.mutationStrategy = mutationStrategy;
	}

	/**
	 * @param reproductionStrategy the reproductionStrategy to set
	 */
	@Override
	public void setReproductionStrategy(IReproduction reproductionStrategy)
	{
		this.reproductionStrategy = reproductionStrategy;
	}

	@Override
	public void setOldToNew(float value)
	{
		oldToNewGenerationCount = (int) (getIndividuumsPerGender() * value);
	}

	@Override
	public void setIndividuumMutationProbability(float value)
	{
		mutationStrategy.setIndividuumMutationProbability(value);
	}

	@Override
	public void setGeneMutationProbability(float value)
	{
		mutationStrategy.setGeneMutationProbability(value);
	}

	/**
	 * @return
	 */
	private int getIndividuumsPerGender()
	{
		return states.get(0).size();
	}

	/**
	 * @return the parentsPerIndividuum
	 */
	@Override
	public int getParentsPerIndividuum()
	{
		return parentsPerIndividuum;
	}

	/**
	 * @return the utilitiesCalculated
	 */
	@Override
	public boolean isUtilitiesCalculated()
	{
		return utilitiesCalculated;
	}
}
