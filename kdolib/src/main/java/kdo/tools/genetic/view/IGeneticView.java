/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.tools.genetic.view;

import kdo.tools.genetic.model.GeneticOptimization;

public interface IGeneticView {
	void update(Boolean running);

	void updateView();

	void setModel(GeneticOptimization model);
}