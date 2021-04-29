/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.domain.model;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import kdo.domain.IIndividuum;
import kdo.domain.IOperator;
import kdo.domain.IOptimizationState;
import kdo.domain.IProblem;
import kdo.domain.IProblemState;
import kdo.domain.IUtilityCalculator;
import kdo.domain.UtilityCalculatorParameters;

/**
 * Decorator for all Individuums to add visualization information
 * @author dorer
 */
public class VisualizationIndividuum implements IIndividuum
{
	/** the individuum to decorate */
	private IIndividuum decoratee;

	/** flag for each chromosom if it was mutated */
	private boolean[] mutated;

	/** flag for each chromosom from which parent it is (first, second,... */
	private int[] parentID;

	/**
	 * Default constructor
	 */
	public VisualizationIndividuum(IIndividuum decoratee)
	{
		this.decoratee = decoratee;
		int length = decoratee.getChromosom().length;
		mutated = new boolean[length];
		parentID = null;
	}

	@Override
	public int getAge()
	{
		return decoratee.getAge();
	}

	@Override
	public float[] getChromosom()
	{
		return decoratee.getChromosom();
	}

	@Override
	public float calculateFitness()
	{
		return decoratee.calculateFitness();
	}

	public float getRelativeDiversity(IIndividuum other)
	{
		return decoratee.getRelativeDiversity(other);
	}

	@Override
	public float getSelectionCriteria()
	{
		return decoratee.getSelectionCriteria();
	}

	@Override
	public void incrementAge()
	{
		decoratee.incrementAge();
	}

	@Override
	public void mutate(int i)
	{
		mutated[i] = true;
		decoratee.mutate(i);
	}

	@Override
	public boolean isMutated(int i)
	{
		return mutated[i];
	}

	@Override
	public void setSelectionCriteria(float fitnessShare)
	{
		decoratee.setSelectionCriteria(fitnessShare);
	}

	@Override
	public int compareTo(IOptimizationState o)
	{
		return decoratee.compareTo(o);
	}

	/**
	 * Writes this individuum to the passed file
	 * @param filename path and name of the file to write
	 * @throws IOException if writing is not possible
	 */
	@Override
	public void writeToFile(String filename) throws IOException
	{
		decoratee.writeToFile(filename);
	}

	/**
	 * @return
	 * @see kdo.domain.IIndividuum#getFitness()
	 */
	@Override
	public float getFitness()
	{
		return decoratee.getFitness();
	}

	/**
	 * @return
	 * @see kdo.domain.IProblemState#checkGoalState()
	 */
	@Override
	public boolean checkGoalState()
	{
		return decoratee.checkGoalState();
	}

	/**
	 * @return
	 * @see kdo.domain.IProblemState#getOperators()
	 */
	@Override
	public List<IOperator> getOperators()
	{
		return decoratee.getOperators();
	}

	/**
	 * @return
	 * @see kdo.domain.IProblemState#getProblem()
	 */
	@Override
	public IProblem getProblem()
	{
		return decoratee.getProblem();
	}

	/**
	 * @return
	 * @see kdo.domain.IProblemState#operatorIterator()
	 */
	@Override
	public Iterator<IOperator> operatorIterator()
	{
		return decoratee.operatorIterator();
	}

	/**
	 * @return
	 * @see kdo.domain.IProblemState#getRandomOperator()
	 */
	@Override
	public IOperator getRandomOperator()
	{
		return decoratee.getRandomOperator();
	}

	/**
	 *
	 * @see kdo.domain.IIndividuum#checkConsistency()
	 */
	@Override
	public void checkConsistency()
	{
		decoratee.checkConsistency();
	}

	/**
	 * @param neighborhoodRadius
	 * @return
	 * @see kdo.domain.IProblemState#getRandomOperator(int)
	 */
	@Override
	public IOperator getRandomOperator(int neighborhoodRadius)
	{
		return decoratee.getRandomOperator(neighborhoodRadius);
	}

	/**
	 *
	 * @see kdo.domain.IProblemState#display()
	 */
	@Override
	public void display()
	{
		decoratee.display();
	}

	/**
	 * @return
	 * @see kdo.domain.IProblemState#getHeuristicsValue()
	 */
	@Override
	public float getHeuristicsValue()
	{
		return decoratee.getHeuristicsValue();
	}

	/**
	 * @return
	 * @see kdo.domain.IProblemState#getUtility()
	 */
	@Override
	public float getUtility()
	{
		return decoratee.getUtility();
	}

	/**
	 * @param util
	 * @see kdo.domain.IProblemState#setUtility(float)
	 */
	@Override
	public void setUtility(float util)
	{
		decoratee.setUtility(util);
	}

	@Override
	public void resetUtility()
	{
		decoratee.resetUtility();
	}

	/**
	 * @return
	 * @see kdo.domain.IProblemState#getReinforcement()
	 */
	@Override
	public float getReinforcement()
	{
		return decoratee.getReinforcement();
	}

	/**
	 * @return
	 * @see kdo.domain.IProblemState#getExplorationCount()
	 */
	@Override
	public int getExplorationCount()
	{
		return decoratee.getExplorationCount();
	}

	/**
	 *
	 * @see kdo.domain.IProblemState#onExploration()
	 */
	@Override
	public void onExploration()
	{
		decoratee.onExploration();
	}

	/**
	 * @param bestOperator
	 * @see kdo.domain.IProblemState#onSelection(kdo.domain.IOperator)
	 */
	@Override
	public void onSelection(IOperator bestOperator)
	{
		decoratee.onSelection(bestOperator);
	}

	/**
	 * @param successorState
	 * @return
	 * @see kdo.domain.IProblemState#isWorseThan(kdo.domain.IProblemState)
	 */
	@Override
	public boolean isWorseThan(IProblemState successorState)
	{
		return decoratee.isWorseThan(successorState);
	}

	@Override
	public IIndividuum getRootIndividuum()
	{
		return decoratee.getRootIndividuum();
	}

	@Override
	public void setParentIDs(int[] parentID)
	{
		this.parentID = parentID;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kdo.domain.IIndividuum#getParentID(int)
	 */
	@Override
	public int getParentID(int index)
	{
		if (parentID == null) {
			return 0;
		}
		return parentID[index];
	}

	@Override
	public String toString()
	{
		return "utility: " + getUtility() + " age: " + getAge();
	}

	@Override
	public double[] getRuntimeProperties()
	{
		return decoratee.getRuntimeProperties();
	}

	@Override
	public float getRelativeDiversity(IOptimizationState other)
	{
		return decoratee.getRelativeDiversity(other);
	}

	@Override
	public float[] getState()
	{
		return this.getChromosom();
	}

	@Override
	public int getIteration()
	{
		return this.getAge();
	}

	@Override
	public void incrementIteration()
	{
		this.incrementAge();
	}

	@Override
	public float calculateUtility()
	{
		return this.calculateFitness();
	}

	@Override
	public IUtilityCalculator getUtilityCalculator(UtilityCalculatorParameters ucParameter)
	{
		return decoratee.getUtilityCalculator(ucParameter);
	}

	@Override
	public Iterator<IOperator> fullSearchIterator()
	{
		return decoratee.fullSearchIterator();
	}

	@Override
	public String getName()
	{
		return decoratee.getName();
	}

	@Override
	public void storeResult()
	{
		decoratee.storeResult();
	}
}
