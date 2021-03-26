/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Deals with goaltracking, a mechanism to avoid activation spreading and
 * explosion
 */
public class GoalTrack implements IBaseGoalTrack
{
	/** activations received directly or indirectly by goals */
	private final double[] activations;

	/** newly calculated activations */
	private final double[] newActivations;

	/** the number of goals in the network */
	private final int noOfGoals;

	/**
	 * Initializes this object.
	 *
	 * @param numberOfGoals the overall (not current) number of goals in the
	 *        network
	 */
	public GoalTrack(int numberOfGoals)
	{
		noOfGoals = numberOfGoals;
		activations = new double[numberOfGoals];
		newActivations = new double[numberOfGoals];
		initialize();
	}

	/**
	 * Initializes this object by setting all activations to 0.0
	 */
	void initialize()
	{
		for (int i = 0; i < noOfGoals; i++) {
			activations[i] = 0.0;
			newActivations[i] = 0.0;
		}
	}

	/**
	 * @return the sum of activation received directly or indirectly by goals
	 */
	@Override
	public double getSumActivation()
	{
		int i;
		double sum = 0.0;

		for (i = 0; i < noOfGoals; i++) {
			sum += activations[i];
		}

		return sum;
	}

	/**
	 * If the passed activation of goal i is higher than any previous activation,
	 * take over the new value
	 * @param i - index of the goal to be checked
	 * @param value new value for the activation.
	 */
	@Override
	public void setActivation(int i, double value)
	{
		if (Math.abs(value) > Math.abs(newActivations[i])) {
			newActivations[i] = value;
		}
	}

	/**
	 * Returns goal activation
	 * @param i index of the goal of which the activation is returned
	 * @return the activation received directly or indirectly by goal i
	 */
	@Override
	public double getActivation(int i)
	{
		return activations[i];
	}

	/**
	 * Updates the activations to the newly calculated activations
	 */
	@Override
	public void setToNewActivation()
	{
		for (int i = 0; i < noOfGoals; i++) {
			activations[i] = newActivations[i];
			newActivations[i] = 0.0;
		}
	}

	/**
	 * @return the number of goals in the network
	 */
	@Override
	public int getNoOfGoals()
	{
		return noOfGoals;
	}
}
