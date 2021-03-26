/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Represents a proposition that is an atom that can be negated or not.
 */
public class Proposition
{
	/** source for the truth value */
	private PerceptionNode perceptionNode;

	/** true if the proposition is negated otherwise - false */
	private boolean isNegated;

	/** backlink to the node containing the proposition */
	private NetworkNode containingNode;

	/**
	 * Default constructor setting negated to false
	 */
	public Proposition()
	{
		isNegated = false;
	}

	/**
	 * Calls setProposition method.
	 * @param parent the containing network node of this proposition
	 * @param perceptionNode perception node from which this proposition gets its
	 *        truth value
	 * @param isNegated true, if this proposition is negated meaning not
	 *        proposition
	 */
	public Proposition(NetworkNode parent, PerceptionNode perceptionNode, boolean isNegated)
	{
		containingNode = parent;
		setProposition(perceptionNode, isNegated);
	}

	/**
	 * @param propName name of the proposition to look for
	 * @param ebn the ebn to which we belong
	 * @return the perception with the passed name if existing, a new perception
	 *         otherwise
	 * @throws NetworkConfigurationException if no corresponding belive is
	 *         available to ground the perception
	 */
	NetworkNode getOrCreateNetworkNode(String propName, ExtendedBehaviorNetwork ebn)
			throws NetworkConfigurationException
	{
		return ebn.getOrCreatePerception(propName);
	}

	/**
	 * Sets the values identifying this proposition.
	 * @param perceptionNode perception node from which this proposition gets its
	 *        truth value
	 * @param isNegated true, if this proposition is negated meaning not
	 *        proposition
	 */
	void setProposition(PerceptionNode perceptionNode, boolean isNegated)
	{
		this.perceptionNode = perceptionNode;
		this.isNegated = isNegated;
	}

	/**
	 * @return the perception from which this proposition gets its truth value
	 */
	public PerceptionNode getPerception()
	{
		return perceptionNode;
	}

	/**
	 * @return true if this proposition is negated
	 */
	public boolean isNegated()
	{
		return isNegated;
	}

	/**
	 * Checks whether this proposition has the same perception as source as the
	 * passed proposition
	 * @param other - proposition to compare this to
	 * @return true if this proposition has the same perception as source as the
	 *         passed proposition
	 */
	boolean isSame(Proposition other)
	{
		return (perceptionNode == other.getPerception());
	}

	/**
	 * Checks whether this module has same perception as passed proposition and
	 * both are negated or not negated
	 * @param other - proposition to compare this to
	 * @return true if this module has same perception as passed proposition and
	 *         both are negated or not negated
	 */
	public boolean isIdentic(Proposition other)
	{
		return (isSame(other) && (isNegated == other.isNegated()));
	}

	/**
	 * Checks whether this module has same perception as passed proposition and
	 * one of them is negated and the other not negated
	 * @param other - proposition to compare this to
	 * @return true if this module has same perception as passed proposition and
	 *         one of them is negated and the other not negated
	 */
	public boolean isInvers(Proposition other)
	{
		return (isSame(other) && (isNegated != other.isNegated()));
	}

	/**
	 * @return the name of this proposition which is the name of the perception
	 *         it belongs to
	 */
	public String getName()
	{
		return perceptionNode.getName();
	}

	/**
	 * @return truth value of this proposition which is the truth value of the
	 *         perception or 1.0 - the truth value of the perception if the
	 *         proposition is negated
	 */
	public double getTruthValue()
	{
		if (isNegated) {
			return (1.0 - perceptionNode.getTruthValue());
		}

		return perceptionNode.getTruthValue();
	}

	/**
	 * Returns a string representation of this proposition
	 *
	 * @return a string representation of this proposition
	 */
	@Override
	public String toString()
	{
		StringBuffer info = new StringBuffer(50);

		info.append(isNegated ? "not " : "").append(perceptionNode.getName());

		return info.toString();
	}

	/**
	 * @return the containingNode
	 */
	public NetworkNode getContainingNode()
	{
		return containingNode;
	}

	/**
	 * Returns the activation the corresponding competence module forwards for
	 * the goal passed
	 * @param goalIndex the index of the goal that is the source of activation
	 * @return the activation the corresponding competence module forwards
	 */
	public double getTransferedActivation(int goalIndex)
	{
		return ((Competence) getContainingNode()).getTransferedActivation(goalIndex);
	}

	/**
	 * Returns the activation that is forwarded by the corresponding goal
	 * @return the activation that is forwarded by the corresponding goal
	 */
	public double getGoalActivation()
	{
		return getContainingNode().getActivation();
	}

	/**
	 * Sets the node that contains this proposition
	 * @param node the node that contains this proposition
	 */
	public void setContainingNode(NetworkNode node)
	{
		containingNode = node;
	}
}
