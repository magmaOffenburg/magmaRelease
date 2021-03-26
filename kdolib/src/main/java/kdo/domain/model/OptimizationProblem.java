/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain.model;

import java.util.Comparator;
import java.util.List;
import kdo.domain.IChromosomStrategy;
import kdo.domain.IOperator;
import kdo.domain.IOptimizationProblem;
import kdo.domain.IOptimizationState;
import kdo.util.IRandomSource;
import kdo.util.learningParameter.ILearningParameterList;

public abstract class OptimizationProblem extends Problem implements IOptimizationProblem
{
	/** source for pseudo random numbers */
	protected IRandomSource rand;

	/** the strategy with which to create new alele values */
	protected IChromosomStrategy chromosomStrategy;

	protected float[][] area;

	public OptimizationProblem(List<IOperator> operators, IRandomSource rand, IChromosomStrategy chromosomStrategy)
	{
		super(operators);
		this.rand = rand;
		area = null;
		this.chromosomStrategy = chromosomStrategy;
	}

	public OptimizationProblem(
			List<IOperator> operators, IRandomSource rand, IChromosomStrategy chromosomStrategy, float[][] area)
	{
		super(operators);
		this.rand = rand;
		this.area = area;
		this.chromosomStrategy = chromosomStrategy;
	}

	@Override
	public IOptimizationState getRandomState()
	{
		return createState(chromosomStrategy.getRandomChromosom());
	}

	@Override
	public Comparator<IOptimizationState> getStateComparator()
	{
		return new OptimizationState.StateUtilityComparator();
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
	public float[] getMinValues()
	{
		return null;
	}

	@Override
	public float[] getMaxValues()
	{
		return null;
	}

	@Override
	public float[][] getArea()
	{
		return area;
	}

	@Override
	public ILearningParameterList createParameters(float[] dimension)
	{
		return null;
	}

	@Override
	public boolean isMaximize()
	{
		return true;
	}
}
