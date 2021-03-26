/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Represents a perception of an agent. The perceptions truth value is updated
 * by calling a perception method of the agent.
 */
public class PerceptionNode extends NetworkNode
{
	/**
	 * cache of the truth value represented by this perception as double of [0 ..
	 * 1]
	 */
	private double truthValue;

	/** flag indicating if the cache is out dated or not */
	private boolean outdated;

	/** link to the source of the truth value */
	final IEBNPerception belief;

	/**
	 * the default constructor of an action control
	 *
	 * @param beliefLink the belief from which to get the current truth value
	 */
	public PerceptionNode(IEBNPerception beliefLink)
	{
		super(beliefLink.getName());
		belief = beliefLink;
		truthValue = 0.0;
		outdated = true;
	}

	/**
	 * Returns the truth value of this perception as boolean value.
	 * @return the truth value represented by this perception as boolean value
	 */
	public double getTruthValue()
	{
		if (outdated) {
			try {
				truthValue = belief.getTruthValue();
				outdated = false;
			} catch (Exception ex) {
				System.out.println("Error when updating perception: " + getName() + " - " + ex);
			}
		}
		return truthValue;
	}

	/**
	 * Returns a string representation of this object
	 * @return a string representation of this object
	 */
	@Override
	public String toString()
	{
		return super.toString() + " Value: " + truthValue;
	}

	@Override
	public void perform()
	{
		outdated = true;
	}

	@Override
	public double getActivation()
	{
		return getTruthValue();
	}

	/**
	 * @return the link to the belief
	 */
	public IEBNPerception getBeliefLink()
	{
		return belief;
	}
}
