/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.domain.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import kdo.domain.IIndividuum;
import kdo.domain.IProblem;
import kdo.domain.IUtilityCalculator;
import kdo.domain.UtilityCalculatorParameters;
import kdo.util.learningParameter.LearningParameterMap;

/**
 * Base class for all Individuums of a genetic population
 * @author dorer
 */
public abstract class Individuum extends OptimizationState implements IIndividuum
{
	/** the cumulative normalized fitness share of this individuum */
	private float selectionCriteria;

	public Individuum(IProblem problem, float[] chromosom)
	{
		this(problem, chromosom, null);
	}

	public Individuum(IProblem problem, float[] chromosom, LearningParameterMap params)
	{
		super(problem, chromosom, params);
	}

	@Override
	public int getAge()
	{
		return getIteration();
	}

	@Override
	public float[] getChromosom()
	{
		return getState();
	}

	@Override
	public float calculateFitness()
	{
		return calculateUtility();
	}

	@Override
	public float getFitness()
	{
		// we have to make sure that fitness is a positive value
		return getUtility();
	}

	@Override
	public float getSelectionCriteria()
	{
		return selectionCriteria;
	}

	@Override
	public void incrementAge()
	{
		incrementIteration();
	}

	@Override
	public void mutate(int i)
	{
		getChromosom()[i] = ((GeneticProblem) problem).getRandomAlele(i, getChromosom());
		resetUtility();
	}

	@Override
	public boolean isMutated(int i)
	{
		return false;
	}

	@Override
	public void setSelectionCriteria(float fitnessShare)
	{
		this.selectionCriteria = fitnessShare;
	}

	/**
	 * Writes this individuum to the passed file
	 * @param filename path and name of the file to write
	 * @throws IOException if writing is not possible
	 */
	@Override
	public void writeToFile(String filename) throws IOException
	{
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		out.write(Arrays.toString(getChromosom()));
		out.close();
	}

	/**
	 * Compares the individuums by age (ascending)
	 * @author dorer
	 */
	public static class IndividuumAgeComparator implements Comparator<IIndividuum>
	{
		/**
		 * @see Comparator#compare(Object, Object)
		 */
		@Override
		public int compare(IIndividuum o1, IIndividuum o2)
		{
			if (o1.getAge() < o2.getAge()) {
				return -1;
			} else if (o1.getAge() > o2.getAge()) {
				return 1;
			}
			return 0;
		}
	}

	/**
	 * Compares the individuums by fitness (ascending)
	 * @author dorer
	 */
	public static class IndividuumFitnessComparator implements Comparator<IIndividuum>
	{
		/**
		 * @see Comparator#compare(Object, Object)
		 */
		@Override
		public int compare(IIndividuum o1, IIndividuum o2)
		{
			if (o1.getFitness() < o2.getFitness()) {
				return -1;
			} else if (o1.getFitness() > o2.getFitness()) {
				return 1;
			}
			return 0;
		}
	}

	@Override
	public IIndividuum getRootIndividuum()
	{
		return this;
	}

	@Override
	public void setParentIDs(int[] parentID)
	{
	}

	@Override
	public int getParentID(int index)
	{
		return 0;
	}

	@Override
	public IUtilityCalculator getUtilityCalculator(UtilityCalculatorParameters ucParameter)
	{
		// for downward compatibility we implement this method returning null.
		// domains using utility calculators have to override this method.
		return null;
	}
}
