/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Represents goals of extended behavior networks. Extends the base class'
 * implementation by adding a situation dependent relevance condition.
 */
public class Goal extends NetworkNode
{
	/**
	 * the relevance condition that specifies in which situations the goal is
	 * relevant
	 */
	Condition relevanceCondition;

	/** the condition which specify how satisfied the goal is */
	private Proposition goalCondition;

	/** the global (situation independent) importance of the goal */
	private double importance;

	/** the index this goal has within the list of goals of the network */
	private int index;

	/**
	 * The default constructor
	 *
	 * @param controlName The name / identifier of this control.
	 */
	public Goal(String controlName)
	{
		super(controlName);
	}

	/**
	 * Constructor that specifies additionally the index this goal has within the
	 * list of goals of the network
	 *
	 * @param controlName The name / identifier of this control.
	 * @param index the index this goal has within the list of goals of the
	 *        network
	 */
	public Goal(String controlName, int index)
	{
		super(controlName);
		this.index = index;
		importance = 1.0;
	}

	/**
	 * @return the activation of this goal which is importance * relevance
	 */
	@Override
	public double getActivation()
	{
		return importance * getRelevance();
	}

	/**
	 * @return the current relevance of the goal
	 */
	public double getRelevance()
	{
		if (relevanceCondition != null) {
			return relevanceCondition.getTruthValue();
		}

		return 1.0;
	}

	/**
	 * @return the index within the list of all goals
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * @return the situation independent importance of this goal
	 */
	public double getImportance()
	{
		return importance;
	}

	/**
	 * Sets new value for the importance attribute.
	 * @param importance the situation independent importance of this goal
	 */
	public void setImportance(double importance)
	{
		this.importance = importance;
	}

	/**
	 * Sets new value to the relevanceCondition attribute.
	 * @param condition the relevance condition for this goal
	 */
	public void setRelevanceCondition(Condition condition)
	{
		relevanceCondition = condition;
	}

	/**
	 * Retrieve the relevance condition value
	 *
	 * @return Relevance condition value
	 */
	public Condition getRelevanceCondition()
	{
		return relevanceCondition;
	}

	/**
	 * Returns the condition of this goal.
	 *
	 * @return Proposition The proposition of this goalCondition.
	 */
	public Proposition getGoalCondition()
	{
		return goalCondition;
	}

	/**
	 * Sets new condition for the goal and the name equal to the name of the new
	 * condition
	 * @param proposition - new condition
	 */
	public void setGoalCondition(Proposition proposition)
	{
		proposition.setContainingNode(this);
		goalCondition = proposition;
		// we might want to have different goal name and condition
		setName(goalCondition.getName());
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
				.append(goalCondition.isNegated() ? "not " : "")
				.append(goalCondition.getPerception().getName())
				.append(", importance: ")
				.append(importance);

		if (relevanceCondition != null) {
			info.append(", relevance: ").append(relevanceCondition.toString(0));
		}

		return info.toString();
	}
}
