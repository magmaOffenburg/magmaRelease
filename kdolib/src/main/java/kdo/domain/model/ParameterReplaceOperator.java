/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain.model;

import kdo.domain.IGeneticProblem;
import kdo.domain.IProblemState;

/**
 * Allows state transitions by adjusting all of the parameters
 * @author dorer
 */
public class ParameterReplaceOperator extends Operator
{
	/** values of the parameters to change */
	private float[] parameterValues;

	/**
	 * @param name name of the operator to display
	 * @param cost the cost for applying this operator
	 * @param parameterValues new value for the parameters when applying this
	 *        operator
	 */
	public ParameterReplaceOperator(String name, float cost, float[] parameterValues)
	{
		super(name, cost);
		this.parameterValues = parameterValues;
	}

	@Override
	public IProblemState getSuccessorState(IProblemState state)
	{
		return ((IGeneticProblem) state.getProblem()).createIndividuum(parameterValues);
	}
}
