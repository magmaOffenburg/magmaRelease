/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain;

public interface IPSOIndividuum extends IOptimizationState {
	// TODO: getVelocity depending on Dimensions in Problem
	float[] getVelocity();

	void initializeVelocity();

	// TODO: getCurrentPosition, getBestPosition / getCurrentValue, getBestValue
	float[] getCurrentPosition();

	float[] getBestPosition();

	float getCurrentValue();

	float getBestValue();

	// TODO: getBestNeighborPosition / getBestNeighborValue
	float[] getBestNeighborPosition();

	float getBestNeighborValue();

	// updateBestNeighbor
	void updateBestNeighbor(float[] bestNeighbor, float bestValue);

	// updateBest
	void updateBest();

	// updateVelocity
	void updateVelocity(int iteration, int maxIterations);

	// updatePosition
	void updatePosition();
}
