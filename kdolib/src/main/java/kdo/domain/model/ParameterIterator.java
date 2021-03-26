/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain.model;

import java.util.Iterator;
import kdo.domain.IIndividuum;
import kdo.domain.IOperator;
import kdo.domain.IProblemState;

/**
 * Base class for Parameter adjustment neighborhood
 */
public class ParameterIterator extends OperatorIteratorBase implements Iterator<IOperator>
{
	/**
	 * stepSizes array containing the delta values by which to adjust each
	 * parameter
	 */
	private float[] stepSizes;

	private final float[] minValues;

	private final float[] maxValues;

	private float[] parameterValues;

	private boolean isFirstCall;

	/**
	 * Default constructor
	 * @param stepSizes array containing the delta values by which to adjust each
	 *        parameter
	 */
	public ParameterIterator(IProblemState state, float[] stepSizes, float[] minValues, float[] maxValues)
	{
		super(state);
		this.minValues = minValues;
		this.maxValues = maxValues;
		this.stepSizes = stepSizes;
		IIndividuum individuum = (IIndividuum) state;
		parameterValues = individuum.getChromosom();
		isFirstCall = true;
	}

	/**
	 * fetches the next Operator
	 */
	@Override
	protected void fetchNext()
	{
		boolean atEnd = true;

		if (isFirstCall) {
			System.arraycopy(minValues, 0, parameterValues, 0, parameterValues.length);
			isFirstCall = false;
		} else {
			for (int i = 0; i < parameterValues.length; i++) {
				parameterValues[i] += stepSizes[i];
				if (parameterValues[i] < maxValues[i]) {
					atEnd = false;
					break;
				}
				parameterValues[i] = minValues[i];
			}
			if (atEnd) {
				// no more elements in the iterator
				nextElement = null;
				return;
			}
		}

		nextElement = new ParameterReplaceOperator("Replace", 1.0f, parameterValues.clone());
	}
}
