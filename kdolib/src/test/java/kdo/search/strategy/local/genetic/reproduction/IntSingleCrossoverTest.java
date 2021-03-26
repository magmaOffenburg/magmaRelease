/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.genetic.reproduction;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kdo.util.IRandomSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author klaus
 *
 */
public class IntSingleCrossoverTest
{
	/** the instance to test */
	SingleCrossover testee;

	/** mock for random number generation */
	IRandomSource randomMock;

	/** array of two parents to be used */
	float[][] parents = {{1, 2, 3, 4, 5, 6}, {11, 12, 13, 14, 15, 16}};

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		randomMock = mock(IRandomSource.class);
		when(randomMock.nextBoolean()).thenReturn(false);
		testee = new SingleCrossover(randomMock);
	}

	/**
	 * Test method for
	 * {@link kdo.search.strategy.local.genetic.reproduction.SingleCrossover#crossOver(domain.IIndividuum,
	 * domain.IIndividuum)}
	 * .
	 */
	@Test
	public void testCreateNewChromosomStart()
	{
		when(randomMock.nextInt(5)).thenReturn(0);

		CrossoverInfo crossoverInfo = testee.createNewChromosom(parents);
		float[] newChromosom = crossoverInfo.getChromosom();
		float[] expected = {1, 12, 13, 14, 15, 16};
		assertTrue(Arrays.equals(expected, newChromosom), "Unequal arrays");

		int[] newParentIDs = crossoverInfo.getParentID();
		int[] expectedIDs = {1, 2, 2, 2, 2, 2};
		assertTrue(Arrays.equals(expectedIDs, newParentIDs), "Unequal arrays");
	}

	/**
	 * Test method for
	 * {@link kdo.search.strategy.local.genetic.reproduction.SingleCrossover#crossOver(domain.IIndividuum,
	 * domain.IIndividuum)}
	 * .
	 */
	@Test
	public void testCreateNewChromosomEnd()
	{
		when(randomMock.nextInt(5)).thenReturn(4);

		CrossoverInfo crossoverInfo = testee.createNewChromosom(parents);
		float[] newChromosom = crossoverInfo.getChromosom();
		float[] expected = {1, 2, 3, 4, 5, 16};
		assertTrue(Arrays.equals(expected, newChromosom), "Unequal arrays");

		int[] newParentIDs = crossoverInfo.getParentID();
		int[] expectedIDs = {1, 1, 1, 1, 1, 2};
		assertTrue(Arrays.equals(expectedIDs, newParentIDs), "Unequal arrays");
	}
}
