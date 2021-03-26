/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain.model;

import java.util.Arrays;
import java.util.Iterator;
import kdo.domain.IIndividuum;
import kdo.domain.IOperator;

/**
 * Base class for Parameter adjustment neighborhood
 */
public class ParameterChangeIterator extends OperatorIteratorBase implements Iterator<IOperator>
{
	/** index of the previously changed parameter, -1 if not started */
	private int currentIndex;

	/** true if the current operator has been decreased already */
	private boolean decreased;

	/**
	 * stepSizes array containing the delta values by which to adjust each
	 * parameter
	 */
	private float[] stepSizes;

	/**
	 * Default constructor
	 * @param stepSizes array containing the delta values by which to adjust each
	 *        parameter
	 */
	public ParameterChangeIterator(IIndividuum state, float[] stepSizes)
	{
		super(state);
		currentIndex = 0;
		decreased = false;
		this.stepSizes = stepSizes;
	}

	public ParameterChangeIterator(IIndividuum state, float value)
	{
		super(state);
		currentIndex = 0;
		decreased = false;
		stepSizes = new float[state.getChromosom().length];
		Arrays.fill(stepSizes, value);
	}

	/**
	 * fetches the next Operator
	 */
	@Override
	protected void fetchNext()
	{
		if (currentIndex >= stepSizes.length) {
			// no more elements in the iterator
			nextElement = null;
			return;
		}

		IIndividuum individuum = (IIndividuum) state;

		if (!decreased) {
			float parameterValue = individuum.getChromosom()[currentIndex] - stepSizes[currentIndex];
			nextElement = new ParameterChangeOperator(
					"Change-" + currentIndex + ":" + parameterValue, 1.0f, currentIndex, parameterValue);
			decreased = true;
		} else {
			float parameterValue = individuum.getChromosom()[currentIndex] + stepSizes[currentIndex];
			nextElement = new ParameterChangeOperator(
					"Change+" + currentIndex + ":" + parameterValue, 1.0f, currentIndex, parameterValue);
			decreased = false;
			currentIndex++;
		}
	}
}
