/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain;

import java.util.Comparator;
import kdo.util.learningParameter.ILearningParameterList;

public interface IOptimizationProblem extends IProblem {
	/**
	 * Factory method for creating an state using the dimensions passed
	 * @param dimension the dimensions of the state
	 * @return an state using the dimensions passed
	 */
	IOptimizationState createState(float[] dimension);

	/**
	 * @return a random state of this domain
	 */
	IOptimizationState getRandomState();

	/**
	 * @return the comparator to use to compare states in this domain
	 */
	Comparator<IOptimizationState> getStateComparator();

	float[] getMinValues();

	float[] getMaxValues();

	ILearningParameterList createParameters(float[] dimension);

	boolean isMaximize();

	float[][] getArea();
}
