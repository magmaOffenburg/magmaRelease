/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain.approximation;

import kdo.domain.IIndividuumView;
import kdo.domain.IProblem;
import kdo.domain.IProblemFactory;
import kdo.domain.approximation.model.FunctionProblem;
import kdo.domain.approximation.view.FunctionProblemView;
import kdo.util.IRandomSource;

/**
 * Factory that knows the problem and its views.
 * @author dorer
 */
public class ApproximationFactory implements IProblemFactory
{
	public String getProblemName()
	{
		return "FunctionProblem";
	}

	public IProblem getProblem(IRandomSource randomSource)
	{
		return FunctionProblem.getInstance(randomSource);
	}

	public IIndividuumView getIndividuumView()
	{
		return new FunctionProblemView();
	}
}
