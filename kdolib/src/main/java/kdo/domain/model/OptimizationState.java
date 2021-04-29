/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package kdo.domain.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import kdo.domain.IOptimizationState;
import kdo.domain.IProblem;
import kdo.util.learningParameter.LearningParameterMap;

/**
 * @author Mithril
 *
 */
public abstract class OptimizationState extends ProblemState implements IOptimizationState
{
	protected float[] state;

	protected int iteration;

	/**
	 * @param problem
	 * @param state
	 */
	public OptimizationState(IProblem problem, float[] state)
	{
		this(problem, state, null);
	}

	/**
	 * @param problem
	 * @param state
	 */
	public OptimizationState(IProblem problem, float[] state, LearningParameterMap params)
	{
		super(problem, params);
		this.state = state;
		iteration = 0;
	}

	@Override
	public int compareTo(IOptimizationState o)
	{
		return Float.compare(getUtility(), o.getUtility());
	}

	@Override
	public float getRelativeDiversity(IOptimizationState other)
	{
		float[] state1 = getState();
		float[] state2 = other.getState();
		int sum = 0;
		for (int i = 0; i < state1.length; i++) {
			if (state1[i] != state2[i]) {
				sum++;
			}
		}
		return sum / (float) state1.length;
	}

	@Override
	public float[] getState()
	{
		return state;
	}

	@Override
	public int getIteration()
	{
		return iteration;
	}

	@Override
	public void incrementIteration()
	{
		iteration++;
	}

	/**
	 * Writes the gene information to a file with utility
	 * @param resultState the state for which to write
	 */
	@Override
	public void storeResult()
	{
		String filename = "generesult.txt";
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
			out.write(getUtility() + ", " + Arrays.toString(getState()) + "\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Compares the states by iteration (ascending)
	 * @author cfueller
	 */
	public static class StateIterationComparator implements Comparator<IOptimizationState>
	{
		/**
		 * @see Comparator#compare(Object, Object)
		 */
		@Override
		public int compare(IOptimizationState o1, IOptimizationState o2)
		{
			if (o1.getIteration() < o2.getIteration()) {
				return -1;
			} else if (o1.getIteration() > o2.getIteration()) {
				return 1;
			}
			return 0;
		}
	}

	/**
	 * Compares the states by utility (ascending)
	 * @author cfueller
	 */
	public static class StateUtilityComparator implements Comparator<IOptimizationState>
	{
		/**
		 * @see Comparator#compare(Object, Object)
		 */
		@Override
		public int compare(IOptimizationState o1, IOptimizationState o2)
		{
			if (o1.getUtility() < o2.getUtility()) {
				return -1;
			} else if (o1.getUtility() > o2.getUtility()) {
				return 1;
			}
			return 0;
		}
	}

	/**
	 * Compares the states by utility (ascending) and allele of genes
	 */
	public static class StateUtilityGeneComparator implements Comparator<IOptimizationState>
	{
		@Override
		public int compare(IOptimizationState o1, IOptimizationState o2)
		{
			if (o1.getUtility() < o2.getUtility()) {
				return -1;
			} else if (o1.getUtility() > o2.getUtility()) {
				return 1;
			}
			float[] state1 = o1.getState();
			float[] state2 = o2.getState();
			for (int i = 0; i < state1.length; i++) {
				if (state1[i] < state2[i]) {
					return -1;
				} else if (state1[i] > state2[i]) {
					return 1;
				}
			}
			return 0;
		}
	}
}
