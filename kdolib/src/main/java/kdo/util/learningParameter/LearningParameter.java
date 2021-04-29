/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.util.learningParameter;

public class LearningParameter
{
	private boolean shouldBeLearned;

	private float stepSize;

	private final float maxValue;

	private final float minValue;

	public LearningParameter()
	{
		this(false, 1, 0, 1);
	}

	public LearningParameter(boolean shouldBeLearned, float stepSize, float minValue, float maxValue)
	{
		this.shouldBeLearned = shouldBeLearned;
		this.stepSize = stepSize;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	@Override
	public String toString()
	{
		return " learn: " + shouldBeLearned + " step: " + stepSize + " minValue: " + minValue +
				" maxValue: " + maxValue;
	}

	public boolean shouldBeLearned()
	{
		return shouldBeLearned;
	}

	public float getStepSize()
	{
		return stepSize;
	}

	public float getMaxValue()
	{
		return maxValue;
	}

	public float getMinValue()
	{
		return minValue;
	}
}
