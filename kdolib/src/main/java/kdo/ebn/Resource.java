/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Represents a resource node of an extended behavior network. A resource node
 * is defined by the amount of resource units available in the current
 * situation, and an activation threshold that defines the amount of activation
 * a module must have to get the resource. It is inheriting from perception
 * since resources are special kinds of perceptions.
 */
public class Resource extends PerceptionNode
{
	/**
	 * flag indicating if this resource has been requested and therefore theta
	 * has to be reduced
	 */
	private boolean isRequested;

	/** the current activation threshold */
	private double activation;

	/** initial value for the activation */
	private double initialActivation;

	/** the current amount of available resource units */
	private int amountAvailable;

	/**
	 * Calls parent constructor and sets isRequested to false
	 * @param beliefLink the belief from which to get the current truth value
	 */
	public Resource(IResourceBelief beliefLink)
	{
		super(beliefLink);
		isRequested = false;
		resetAmountAvailable();
	}

	/**
	 * Reduces the current threshold by the specified amount but not below
	 * 0.00001
	 * @param deltaTheta the amount by which to reduce the activation level
	 */
	public void reduceActivationLevel(double deltaTheta)
	{
		if (isRequested) {
			double newActivation = getActivation();

			newActivation -= deltaTheta;

			if (newActivation < 0.00001) {
				newActivation = 0.00001;
			}

			setActivation(newActivation);
			isRequested = false;
		}
	}

	/**
	 * Checks whether the activity passed is less than the resources threshold
	 * @param activity the activity of the competence module to check against the
	 *        threshold
	 * @return true, if the activity passed is less than the resources threshold
	 */
	public boolean isActivityLowerThanThreshold(double activity)
	{
		isRequested = true;
		return activity < activation;
	}

	/**
	 * Sets new value to the activation attribute
	 * @param value the new threshold of this node
	 */
	public void setActivation(double value)
	{
		activation = value;
	}

	/**
	 * Sets initial value for the activation
	 * @param value the new threshold of this node
	 */
	public void setInitialActivation(double value)
	{
		initialActivation = value;
	}

	/**
	 * @return the threshold of this resource node
	 */
	@Override
	public double getActivation()
	{
		return activation;
	}

	/**
	 * Resets activation to the parsed value
	 * @param theta the new threshold of this module
	 */
	public void resetActivationLevel(double theta)
	{
		setActivation(theta);
	}

	/**
	 * Sets activation to initial value
	 */
	void restoreInitialActivation()
	{
		setActivation(initialActivation);
	}

	/**
	 * Decrease resource amount available
	 * @param reduction the number of units by which the available amount of
	 *        resource units is decreased
	 */
	public void reduceAmountAvailable(int reduction)
	{
		amountAvailable -= reduction;
	}

	/**
	 * Increase resource amount available
	 * @param increase the number of units by which the available amount of
	 *        resource units is increased
	 */
	public void increaseAmountAvailable(int increase)
	{
		amountAvailable += increase;
	}

	/**
	 * Sets the available amount of resource units to the maximal amount
	 * available
	 */
	void resetAmountAvailable()
	{
		amountAvailable = getMaximalAmount();
	}

	/**
	 * @return amount of available resource units
	 */
	public int getAmountAvailable()
	{
		return amountAvailable;
	}

	/**
	 * @return maximal possible amount for the resource
	 */
	public int getMaximalAmount()
	{
		return ((IResourceBelief) belief).getAmountAvailable();
	}

	/**
	 * Returns the information about this goal.
	 *
	 * @param tabs - the number of tabs used to make indentation in the output.
	 * @return the information about this goal.
	 */
	@Override
	public String toString(int tabs)
	{
		StringBuffer info = new StringBuffer(100);

		info.append(PrintUtil.addTabs(tabs))
				.append(getName())
				.append(", maximal available amount: ")
				.append(getMaximalAmount());

		return info.toString();
	}

	/**
	 */
	void resetResource()
	{
		if (getAmountAvailable() < getMaximalAmount()) {
			restoreInitialActivation();
			resetAmountAvailable();
		}
	}
}
