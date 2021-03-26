/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Dummy class that is used in case that goaltracking is switched off
 */
class NoGoalTrack implements IBaseGoalTrack
{
	/** activation received directly or indirectly by goal */
	private double activation;

	/** newly calculated activation */
	private double newActivation;

	/**
	 * Default constructor
	 */
	public NoGoalTrack()
	{
		initialize();
	}

	/**
	 * Initializes activation and newActivation fields with zero values
	 */
	void initialize()
	{
		activation = 0.0;
		newActivation = 0.0;
	}

	/**
	 * @return the number of goals in the network
	 */
	@Override
	public int getNoOfGoals()
	{
		return 1;
	}

	/**
	 * @return the activation received directly or indirectly by goals
	 */
	@Override
	public double getSumActivation()
	{
		return activation;
	}

	/**
	 * If the passed activation of goal is higher than any previous activation,
	 * take over the new value
	 *
	 * @param i index of the goal for which the activation is set
	 * @param val new value for the activation
	 */
	@Override
	public void setActivation(int i, double val)
	{
		newActivation += val;
	}

	/**
	 * @param i index of the goal of which the activation is returned
	 * @return the activation received directly or indirectly by goal
	 */
	@Override
	public double getActivation(int i)
	{
		return activation;
	}

	/**
	 * Updates the activation to the newly calculated activation
	 */
	@Override
	public void setToNewActivation()
	{
		activation = newActivation;
		newActivation = 0.0;
	}
}
