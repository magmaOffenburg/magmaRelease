/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.domain.model;

import java.util.Comparator;
import java.util.List;
import kdo.domain.IChromosomStrategy;
import kdo.domain.IGeneticProblem;
import kdo.domain.IIndividuum;
import kdo.domain.IOperator;
import kdo.domain.IOptimizationState;
import kdo.util.IRandomSource;

/**
 * Base class for Genetic Problems. Since it does not add any implementation
 * needs to its subclasses this can also be used for normal problems.
 * @author dorer
 */
public abstract class GeneticProblem extends OptimizationProblem implements IGeneticProblem
{
	/**
	 * Default constructor
	 * @param operators the operators that are available in all states
	 * @param rand the source for random numbers to use
	 */
	public GeneticProblem(List<IOperator> operators, IRandomSource rand, IChromosomStrategy chromosomStrategy)
	{
		super(operators, rand, chromosomStrategy);
	}

	public GeneticProblem(
			List<IOperator> operators, IRandomSource rand, IChromosomStrategy chromosomStrategy, float[][] area)
	{
		super(operators, rand, chromosomStrategy, area);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see domain.model.OptimizationProblem#getRandomState()
	 */
	@Override
	public IOptimizationState getRandomState()
	{
		return getRandomIndividuum();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see domain.IGeneticProblem#getRandomIndividuum()
	 */
	public IIndividuum getRandomIndividuum()
	{
		return createIndividuum(chromosomStrategy.getRandomChromosom());
	}

	@Override
	public float getRandomAlele(int gene, float chromosom[])
	{
		return chromosomStrategy.getRandomAlele(gene, chromosom);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see domain.IGeneticProblem#getIndividuumComparator()
	 */
	public Comparator<IIndividuum> getIndividuumComparator()
	{
		return new Individuum.IndividuumFitnessComparator();
	}

	/**
	 * @return the random source
	 */
	public IRandomSource getRand()
	{
		return rand;
	}

	/**
	 * @return the alele
	 */
	public IChromosomStrategy getChromosomStrategy()
	{
		return chromosomStrategy;
	}

	/**
	 * @param chromosomStrategy the chromosom strategy to set
	 */
	public void setChromosomStrategy(IChromosomStrategy strategy)
	{
		this.chromosomStrategy = strategy;
	}

	@Override
	public IOptimizationState createState(float[] dimension)
	{
		return createIndividuum(dimension);
	}
}
