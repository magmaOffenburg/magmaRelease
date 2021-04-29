/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.util.learningParameter;

import kdo.util.parameter.IParameterList;

/**
 *
 * @author kdorer
 */
public interface ILearningParameterList extends IParameterList {
	/**
	 * @return the array of stepSizes for the parameters that are subject to
	 *         learn
	 */
	float[] getStepSizes();

	/**
	 * @return the array of minValues for the parameters that are subject to
	 *         learn
	 */
	float[] getMinValues();

	/**
	 * @return the array of maxValues for the parameters that are subject to
	 *         learn
	 */
	float[] getMaxValues();

	/**
	 * @return the a copy of the array of parameters
	 */
	float[] toChromosom();

	/**
	 * @param chromosom the parameter array to set
	 */
	void fromChromosom(float[] chromosom);

	/**
	 * @return the list decorated by this
	 */
	IParameterList getDecoratedList();
}