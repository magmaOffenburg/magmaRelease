/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.reinforcement;

/**
 * Base class for all TD algorithms providing learning rate update strategy
 *
 * @author KDorer
 */
public abstract class TemporalDifference implements IValueFunctionUpdate
{
	protected ILearnrateStrategy learnrateStrategy;

	public TemporalDifference()
	{
		this(new FixedLearnrate(0.01f));
	}

	public TemporalDifference(ILearnrateStrategy learnrateStrategy)
	{
		this.learnrateStrategy = learnrateStrategy;
	}
}
