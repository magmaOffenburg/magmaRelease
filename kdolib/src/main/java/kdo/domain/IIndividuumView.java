/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.domain;

import javax.swing.JPanel;
import kdo.search.strategy.local.genetic.IPopulation;

/**
 * Interface for all views displaying a single individuum
 * @author dorer
 */
public interface IIndividuumView {
	/**
	 * @return an instance of the view to display while optimizing
	 */
	JPanel getDisplayPanel();

	/**
	 * Sets the individuum to display
	 * @param current the individuum to display
	 */
	void setCurrentIndividuum(IIndividuum current);

	/**
	 * Sets the population
	 * @param population the current whole population
	 */
	void setPopulation(IPopulation population);
}
