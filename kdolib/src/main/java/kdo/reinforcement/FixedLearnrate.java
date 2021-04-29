/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.reinforcement;

/**
 * Class for all learnrates that do not change over time
 * @author klaus
 */
public class FixedLearnrate implements ILearnrateStrategy
{
	private float learnrate;

	public FixedLearnrate(float learnrate)
	{
		this.learnrate = learnrate;
	}

	@Override
	public float getAlpha(int explorationCount)
	{
		return learnrate;
	}
}
