/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.domain;

import kdo.util.IRandomSource;

/**
 * Interface for all problem factories that offers the problem and its views.
 * @author dorer
 */
public interface IProblemFactory {
	/**
	 * @return the name of the problem to solve
	 */
	String getProblemName();

	/**
	 * @return an instance of the problem to solve
	 * @param randomSource the random source to use, null if none is needed
	 */
	IProblem getProblem(IRandomSource randomSource);

	/**
	 * @return the view to display an individuum
	 */
	IIndividuumView getIndividuumView();
}
