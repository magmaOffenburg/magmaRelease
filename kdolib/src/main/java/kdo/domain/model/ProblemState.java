/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.domain.model;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import kdo.domain.IOperator;
import kdo.domain.IProblem;
import kdo.domain.IProblemState;
import kdo.util.learningParameter.ILearningParameterList;
import kdo.util.learningParameter.LearningParameterMap;

/**
 * Represents a single state of a problem
 */
public abstract class ProblemState implements IProblemState
{
	protected IProblem problem;

	public static long generatedStates = 0;

	/** the utility of this state */
	protected float utility;

	/** the number of times the agent visited this state */
	protected int explorationCount;

	/** the params that define this problem state, may be null */
	protected LearningParameterMap params;

	public ProblemState(IProblem problem)
	{
		this(problem, null);
	}

	public ProblemState(IProblem problem, LearningParameterMap params)
	{
		this.problem = problem;
		this.params = params;
		utility = Float.NEGATIVE_INFINITY;
		explorationCount = 0;
		generatedStates++;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see domain.IProblemState#getProblem()
	 */
	@Override
	public IProblem getProblem()
	{
		return problem;
	}

	/**
	 * Returns false by default. Overwrite for domains with specific goal states
	 *
	 * @see kdo.domain.IProblemState#checkGoalState()
	 */
	@Override
	public boolean checkGoalState()
	{
		return false;
	}

	/**
	 * @see kdo.domain.IProblemState#getOperators()
	 */
	@Override
	public List<IOperator> getOperators()
	{
		return problem.getOperators();
	}

	/**
	 * @see kdo.domain.IProblemState#operatorIterator()
	 */
	@Override
	public Iterator<IOperator> operatorIterator()
	{
		return problem.getOperators().iterator();
	}

	@Override
	public Iterator<IOperator> fullSearchIterator()
	{
		ILearningParameterList param = params.getParameter();
		return new ParameterIterator(this, param.getStepSizes(), param.getMinValues(), param.getMaxValues());
	}

	/**
	 * @see kdo.domain.IProblemState#getRandomOperator()
	 */
	@Override
	public IOperator getRandomOperator()
	{
		return getRandomOperator(1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see domain.IProblemState#getRandomOperator(int)
	 */
	@Override
	public IOperator getRandomOperator(int neighborhoodRadius)
	{
		return problem.getRandomOperator(neighborhoodRadius);
	}

	/**
	 * @see kdo.domain.IProblemState#display()
	 */
	@Override
	public void display()
	{
		System.out.println(toString());
	}

	/**
	 * @see kdo.domain.IProblemState#getHeuristicsValue()
	 */
	@Override
	public float getHeuristicsValue()
	{
		return 0.0f;
	}

	/**
	 * @see kdo.domain.IProblemState#calculateUtility()
	 */
	@Override
	public float calculateUtility()
	{
		return utility = 0.0f;
	}

	/**
	 * @see kdo.domain.IProblemState#getUtility()
	 */
	@Override
	public float getUtility()
	{
		if (utility == Float.NEGATIVE_INFINITY) {
			System.out.println("Utility nicht berechnet: " + this.toString());
		}
		return utility;
	}

	/**
	 * @see kdo.domain.IProblemState#setUtility(float)
	 */
	@Override
	public void setUtility(float util)
	{
		utility = util;
	}

	@Override
	public void resetUtility()
	{
		utility = Float.NEGATIVE_INFINITY;
	}

	/**
	 * @see kdo.domain.IProblemState#getReinforcement()
	 */
	@Override
	public float getReinforcement()
	{
		return 0.0f;
	}

	/**
	 * @see kdo.domain.IProblemState#getExplorationCount()
	 */
	@Override
	public int getExplorationCount()
	{
		return explorationCount;
	}

	/**
	 * @see kdo.domain.IProblemState#onExploration()
	 */
	@Override
	public void onExploration()
	{
		explorationCount++;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see domain.IProblemState#checkConsistency()
	 */
	@Override
	public void checkConsistency()
	{
		// the default implementation does nothing
	}

	/**
	 * Called if this state was selected by the optimization. The default
	 * implementation does nothing.
	 *
	 * @param bestOperator the operator that created this state
	 */
	@Override
	public void onSelection(IOperator bestOperator)
	{
	}

	/**
	 * Checks if this state is worse than the passed state. The default
	 * implementation checks if the utility is worse.
	 *
	 * @param successorState the state to check against
	 * @return true if this state is worse than the passed state
	 */
	@Override
	public boolean isWorseThan(IProblemState otherState)
	{
		return otherState.getUtility() > getUtility();
	}

	/**
	 * Empty default implementation
	 *
	 * @param filename path and name of the file to write
	 * @throws IOException if writing is not possible
	 */
	@Override
	public void writeToFile(String filename) throws IOException
	{
	}

	@Override
	public double[] getRuntimeProperties()
	{
		return null;
	}

	@Override
	public String getName()
	{
		return "";
	}
}