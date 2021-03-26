/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.thoughtmodel.impl;

import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.agent.model.thoughtmodel.ITruthValue;
import java.util.Arrays;
import java.util.List;

/**
 * Class for combining multiple truth values via <i>AND</i> condition.
 *
 * @author Stefan Glaser
 */
public class CompositeTruthValue extends TruthValue
{
	/** The array of truth values. */
	private final List<ITruthValue> truthValues;

	/** Indicator for AND or OR operation. */
	private final boolean logicalAnd;

	public CompositeTruthValue(boolean logicalAnd, ITruthValue... truthValues)
	{
		this.logicalAnd = logicalAnd;
		this.truthValues = Arrays.asList(truthValues);
	}

	@Override
	public void update(IThoughtModel thoughtModel)
	{
		boolean combinedValidity = logicalAnd;

		for (ITruthValue tv : truthValues) {
			tv.update(thoughtModel);

			if (logicalAnd) {
				combinedValidity = combinedValidity && tv.isValid();
			} else {
				combinedValidity = combinedValidity || tv.isValid();
			}
		}

		setValidity(combinedValidity, thoughtModel.getWorldModel().getGlobalTime());
	}
}
