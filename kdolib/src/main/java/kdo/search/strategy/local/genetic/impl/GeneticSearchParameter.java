/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.genetic.impl;

import kdo.search.strategy.local.genetic.IMutation;
import kdo.search.strategy.local.genetic.IReproduction;
import kdo.search.strategy.local.genetic.ISelection;
import kdo.search.strategy.local.genetic.mutation.NoMutation;
import kdo.search.strategy.local.genetic.mutation.RandomMutation;
import kdo.search.strategy.local.genetic.reproduction.MultiCrossover;
import kdo.search.strategy.local.genetic.reproduction.SingleCrossover;
import kdo.search.strategy.local.genetic.selection.EliteSelection;
import kdo.search.strategy.local.genetic.selection.MinDiversityMonteCarloSelection;
import kdo.search.strategy.local.genetic.selection.MonteCarloSelection;
import kdo.search.strategy.local.genetic.selection.RankSelection;
import kdo.util.IRandomSource;

public class GeneticSearchParameter extends OptimizationSearchParameter
{
	public static final String AI_LAB_SELECTION = "AILabSelection";

	public static final String MIN_DIVERSITY_MONTE_CARLO_SELECTION = "MinDiversitySelection";

	public static final String MONTE_CARLO_SELECTION = "MonteCarloSelection";

	public static final String RANK_SELECTION = "RankSelection";

	public static final String ELITE_SELECTION = "EliteSelection";

	public static final String SINGLE_CROSSOVER_RECOMBINATION = "SingleCrossover";

	public static final String MULTI_CROSSOVER_RECOMBINATION = "MultiCrossover";

	public static final String AI_LAB_RECOMBINATION = "AILabCrossover";

	public static final String RANDOM_MUTATION = "RandomMutation";

	public static final String NO_MUTATION = "NoMutation";

	private float oldToNew;

	private int parentsPerIndividuum;

	private float individuumMutationProbability;

	private float geneMutationProbability;

	private float eliteSelectionRatio;

	private String selectionStrategy;

	private String mutationStrategy;

	private String reproductionStrategy;

	public GeneticSearchParameter(IRandomSource random)
	{
		super(random);
		parentsPerIndividuum = 2;
		oldToNew = 0.1f;
		individuumMutationProbability = 0.0f; // 0.0 to switch off
		geneMutationProbability = 0.05f;
		eliteSelectionRatio = 0.5f;
		selectionStrategy = ELITE_SELECTION;
		mutationStrategy = NO_MUTATION;
		reproductionStrategy = SINGLE_CROSSOVER_RECOMBINATION;
	}

	public int getGenerations()
	{
		return this.getIterations();
	}

	public void setGenerations(int generations)
	{
		if (generations < 1) {
			throw new IllegalArgumentException("Invalid number of generations [1..maxint]: " + generations);
		}
		this.setIterations(generations);
	}

	public int getGenders()
	{
		return getGroups();
	}

	public void setGenders(int genders)
	{
		if (genders < 1 || genders > this.getPopulationSize()) {
			throw new IllegalArgumentException("Invalid number of genders [1..population]: " + genders);
		}
		this.setGroups(genders);
	}

	public float getOldToNew()
	{
		return oldToNew;
	}

	public void setOldToNew(float oldToNew)
	{
		if (oldToNew < 0.0 || oldToNew > 1.0) {
			throw new IllegalArgumentException("Invalid number of individuums taken over [0..1]: " + oldToNew);
		}

		this.oldToNew = oldToNew;
	}

	public int getParentsPerIndividuum()
	{
		return parentsPerIndividuum;
	}

	public void setParentsPerIndividuum(int parentsPerIndividuum)
	{
		if (parentsPerIndividuum < 2) {
			throw new IllegalArgumentException("Number of parents must be at least 1, but is: " + parentsPerIndividuum);
		}

		if (parentsPerIndividuum > this.getPopulationSize()) {
			throw new IllegalArgumentException("Number of parents may be at most size of population, but is: " +
											   parentsPerIndividuum + " population size: " + this.getPopulationSize());
		}

		this.parentsPerIndividuum = parentsPerIndividuum;
	}

	public float getIndividuumMutationProbability()
	{
		return individuumMutationProbability;
	}

	public void setIndividuumMutationProbability(float individuumMutationProbability)
	{
		if (individuumMutationProbability < 0.0 || individuumMutationProbability > 1.0) {
			throw new IllegalArgumentException("Invalid probability [0..1]: " + individuumMutationProbability);
		}
		this.individuumMutationProbability = individuumMutationProbability;
	}

	public float getGeneMutationProbability()
	{
		return geneMutationProbability;
	}

	public void setGeneMutationProbability(float geneMutationProbability)
	{
		if (geneMutationProbability < 0.0 || geneMutationProbability > 1.0) {
			throw new IllegalArgumentException("Invalid probability [0..1]: " + geneMutationProbability);
		}
		this.geneMutationProbability = geneMutationProbability;
	}

	/**
	 * @return the selectionStrategy
	 */
	public String getSelectionStrategy()
	{
		return selectionStrategy;
	}

	/**
	 * @param selectionStrategy the selectionStrategy to set
	 */
	public void setSelectionStrategy(String selectionStrategy)
	{
		this.selectionStrategy = selectionStrategy;
	}

	/**
	 * @return the mutationStrategy
	 */
	public String getMutationStrategy()
	{
		return mutationStrategy;
	}

	/**
	 * @param mutationStrategy the mutationStrategy to set
	 */
	public void setMutationStrategy(String mutationStrategy)
	{
		this.mutationStrategy = mutationStrategy;
	}

	/**
	 * @return the reproductionStrategy
	 */
	public String getReproductionStrategy()
	{
		return reproductionStrategy;
	}

	/**
	 * @param reproductionStrategy the reproductionStrategy to set
	 */
	public void setReproductionStrategy(String reproductionStrategy)
	{
		this.reproductionStrategy = reproductionStrategy;
	}

	public double getIndividuumsPerGender()
	{
		return (getPopulationSize() / getGenders());
	}

	/**
	 * @return the eliteSelectionRatio
	 */
	public float getEliteSelectionRatio()
	{
		return eliteSelectionRatio;
	}

	/**
	 * @param eliteSelectionRatio the eliteSelectionRatio to set
	 */
	public void setEliteSelectionRatio(float eliteSelectionRatio)
	{
		if (eliteSelectionRatio < 0.0 || eliteSelectionRatio > 1.0) {
			throw new IllegalArgumentException("Elite selection ratio outside range [0..1]: " + eliteSelectionRatio);
		}
		this.eliteSelectionRatio = eliteSelectionRatio;
	}

	/**
	 * @param strategyString the strategy for individuum selection
	 * @param params parameters to use, null if existing parameters should be
	 *        used
	 */
	public ISelection createSelectionStrategy()
	{
		ISelection strategy;
		if (selectionStrategy.equals(GeneticSearchParameter.ELITE_SELECTION)) {
			int bestCount = (int) (getIndividuumsPerGender() * getEliteSelectionRatio());
			if (bestCount < 0) {
				bestCount = 0;
			}
			strategy = new EliteSelection(bestCount, getRandomSource());

		} else if (selectionStrategy.equals(GeneticSearchParameter.RANK_SELECTION)) {
			strategy = new RankSelection(false, getRandomSource());

		} else if (selectionStrategy.equals(GeneticSearchParameter.MIN_DIVERSITY_MONTE_CARLO_SELECTION)) {
			strategy = new MinDiversityMonteCarloSelection(true, getRandomSource(), 0.1f, 10);
		} else if (selectionStrategy.equals(GeneticSearchParameter.MONTE_CARLO_SELECTION)) {
			strategy = new MonteCarloSelection(true, getRandomSource());

		} else {
			System.out.println("Unknown selection strategy: " + selectionStrategy + "! Using Monte Carlo...");
			strategy = new MonteCarloSelection(true, getRandomSource());
		}
		return strategy;
	}

	public IMutation createMutationStrategy()
	{
		IMutation strategy = null;
		if (mutationStrategy.equals(GeneticSearchParameter.RANDOM_MUTATION)) {
			strategy = new RandomMutation(
					getIndividuumMutationProbability(), getGeneMutationProbability(), getRandomSource());

		} else if (mutationStrategy.equals(GeneticSearchParameter.NO_MUTATION)) {
			strategy = new NoMutation();

		} else {
			System.out.println("Unknown mutation strategy: " + mutationStrategy + "! Using no mutation");
			strategy = new NoMutation();
		}
		return strategy;
	}

	public IReproduction createRecombinationStrategy()
	{
		IReproduction strategy;
		if (reproductionStrategy.equals(GeneticSearchParameter.MULTI_CROSSOVER_RECOMBINATION)) {
			strategy = new MultiCrossover(getRandomSource());
		} else if (reproductionStrategy.equals(GeneticSearchParameter.SINGLE_CROSSOVER_RECOMBINATION)) {
			strategy = new SingleCrossover(getRandomSource());
		} else {
			System.out.println("Unknown reproduction strategy: " + reproductionStrategy + "! Using Multi Crossover...");
			strategy = new MultiCrossover(getRandomSource());
		}
		return strategy;
	}

	@Override
	public String toString()
	{
		return super.toString() + "GeneticSearchParameter [oldToNew=" + oldToNew +
				", parentsPerIndividuum=" + parentsPerIndividuum +
				", individuumMutationProbability=" + individuumMutationProbability +
				", geneMutationProbability=" + geneMutationProbability +
				", eliteSelectionRatio=" + eliteSelectionRatio + ", selectionStrategy=" + selectionStrategy +
				", mutationStrategy=" + mutationStrategy + ", reproductionStrategy=" + reproductionStrategy + "]";
	}
}