/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain.model;

import java.util.Arrays;
import kdo.domain.IGeneticProblem;
import kdo.domain.IIndividuum;
import kdo.domain.IProblemState;

/**
 * Allows state transitions by adjusting one of the parameters
 * @author dorer
 */
public class ParameterChangeOperator extends Operator
{
	/** index of the parameter to change */
	private int parameterIndex;

	/** value of the parameter to change */
	private float parameterValue;

	/**
	 * @param name name of the operator to display
	 * @param cost the cost for applying this operator
	 * @param parameterIndex index of the parameter to change
	 * @param parameterValue new value for the parameter when applying this
	 *        operator
	 */
	public ParameterChangeOperator(String name, float cost, int parameterIndex, float parameterValue)
	{
		super(name, cost);
		this.parameterIndex = parameterIndex;
		this.parameterValue = parameterValue;
	}

	@Override
	public IProblemState getSuccessorState(IProblemState state)
	{
		float[] oldChromosom = ((IIndividuum) state).getChromosom();
		float[] newChromosom = Arrays.copyOf(oldChromosom, oldChromosom.length);
		newChromosom[parameterIndex] = parameterValue;
		return ((IGeneticProblem) state.getProblem()).createIndividuum(newChromosom);
	}
}
