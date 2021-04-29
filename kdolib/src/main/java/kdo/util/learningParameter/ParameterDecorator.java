/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.util.learningParameter;

import kdo.util.parameter.Parameter;

public class ParameterDecorator
{
	private Parameter decoratee;

	private LearningParameter learning;

	public ParameterDecorator(Parameter decoratee, LearningParameter learning)
	{
		this.decoratee = decoratee;
		this.learning = learning;
	}

	@Override
	public String toString()
	{
		return decoratee.toString() + learning.toString();
	}

	public String getName()
	{
		return decoratee.getName();
	}

	public float getValue()
	{
		return decoratee.getValue();
	}

	public void setValue(float value)
	{
		decoratee.setValue(value);
	}

	public boolean shouldBeLearned()
	{
		return learning.shouldBeLearned();
	}

	public float getStepSize()
	{
		return learning.getStepSize();
	}

	public float getMaxValue()
	{
		return learning.getMaxValue();
	}

	public float getMinValue()
	{
		return learning.getMinValue();
	}
}
