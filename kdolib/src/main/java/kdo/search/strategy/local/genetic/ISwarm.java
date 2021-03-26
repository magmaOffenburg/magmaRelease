/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.genetic;

import kdo.domain.IPSOIndividuum;

public interface ISwarm extends IOptimizationStateGroup {
	IPSOIndividuum search(int iterations, long maxRuntime);

	void initializeParticles();

	void updateGroups();

	void updateParticleBest();

	void updateParticleVelocity(int maxIterations);

	void updateParticlePosition();
}
