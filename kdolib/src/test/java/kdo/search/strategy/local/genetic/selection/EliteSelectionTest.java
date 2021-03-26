/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.genetic.selection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import kdo.domain.IGeneticProblem;
import kdo.domain.IIndividuum;
import kdo.domain.IOptimizationState;
import kdo.search.strategy.local.genetic.IMutation;
import kdo.search.strategy.local.genetic.IPopulation;
import kdo.search.strategy.local.genetic.IReproduction;
import kdo.search.strategy.local.genetic.ISelection;
import kdo.search.strategy.local.genetic.fitness.SerialFitnessCalculator;
import kdo.search.strategy.local.genetic.representation.Population;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author klaus
 *
 */
public class EliteSelectionTest
{
	/** the instance under test */
	EliteSelection testee;

	/** mock for the underlying reproduction strategy */
	IReproduction reproductionStrategyMock;

	/** mock for the underlying selection strategy */
	ISelection selectionStrategyMock;

	/** mock for the underlying mutation strategy */
	IMutation mutationStrategyMock;

	/** mock for the problem domain */
	IGeneticProblem domainMock;

	/** mock for all new individuums that are created during crossover */
	IIndividuum newIndividuumMock;

	/** mocks for a starting population's individuums */
	IIndividuum parent1Mock;

	IIndividuum parent2Mock;

	IIndividuum parent3Mock;

	/** a test population containing the three parent mocks */
	IPopulation population;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		newIndividuumMock = mock(IIndividuum.class);
		parent1Mock = mock(IIndividuum.class);
		parent2Mock = mock(IIndividuum.class);
		parent3Mock = mock(IIndividuum.class);
		when(parent1Mock.compareTo(parent2Mock)).thenReturn(1);
		when(parent1Mock.compareTo(parent3Mock)).thenReturn(1);
		when(parent2Mock.compareTo(parent3Mock)).thenReturn(1);

		List<List<IOptimizationState>> allIndividuums = new ArrayList<>();
		List<IOptimizationState> individuums = new ArrayList<>();
		individuums.add(parent1Mock);
		individuums.add(parent2Mock);
		individuums.add(parent3Mock);
		allIndividuums.add(individuums);

		reproductionStrategyMock = mock(IReproduction.class);
		selectionStrategyMock = mock(ISelection.class);
		mutationStrategyMock = mock(IMutation.class);
		domainMock = mock(IGeneticProblem.class);
		population = new Population(domainMock, 3, 2, selectionStrategyMock, reproductionStrategyMock,
				mutationStrategyMock, 0.1f, 2, false, new SerialFitnessCalculator(), null, allIndividuums);
	}

	/**
	 * Test method for
	 * {@link
	 * kdo.search.strategy.local.genetic.selection.EliteSelection#doSelection(kdo.search.strategy.local.genetic.IPopulation)}
	 * .
	 */
	@Test
	@Disabled
	public void testDoSelectionTwoTakeOver()
	{
		// we have to create new tests
		// when(reproductionStrategyMock.crossOver(parent1Mock, parent2Mock))
		// .andReturn(newIndividuumMock);
		// replay(reproductionStrategyMock);
		// testee = new EliteSelection(2, reproductionStrategyMock, null);
		//
		// // perform test
		// List<IIndividuum> newPopulation = testee.doSelection(population);
		//
		// verify(reproductionStrategyMock);
		// assertEquals(3, newPopulation.size());
		// assertEquals(parent1Mock, newPopulation.get(0));
		// assertEquals(parent2Mock, newPopulation.get(1));
		// assertEquals(newIndividuumMock, newPopulation.get(2));
	}
}
