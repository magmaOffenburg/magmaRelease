/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain.model.chromosom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import kdo.util.IRandomSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author kdorer
 *
 */
public class UniformChromosomStrategyTest
{
	private UniformChromosomStrategy testee;

	private IRandomSource rand;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		rand = mock(IRandomSource.class);
		float[][] area = {{-1.0f, 2.0f}, {3.0f, 5.0f}};
		testee = new UniformChromosomStrategy(rand, area);
	}

	/**
	 * Test method for
	 * {@link kdo.domain.model.chromosom.UniformChromosomStrategy#getRandomAlele(int, float[])}
	 * .
	 */
	@Test
	public void testGetRandomAlele()
	{
		when(rand.nextFloat()).thenReturn(0.0f).thenReturn(0.5f).thenReturn(1.0f);
		assertEquals(-1.0f, testee.getRandomAlele(0, null), 0.001f);
		assertEquals(0.5f, testee.getRandomAlele(0, null), 0.001f);
		assertEquals(5, testee.getRandomAlele(1, null), 0.001f);
	}

	/**
	 * Test method for
	 * {@link kdo.domain.model.chromosom.BaseChromosomStrategy#getRandomChromosom()}
	 * .
	 */
	@Test
	public void testGetRandomChromosom()
	{
		when(rand.nextFloat()).thenReturn(0.0f).thenReturn(1.0f);
		float[] result = testee.getRandomChromosom();
		assertEquals(-1.0f, result[0], 0.0001f);
		assertEquals(5.0f, result[1], 0.0001f);
	}

	@Test
	public void testUniformChromosomStrategy()
	{
		try {
			// invalid area
			float[][] area = {{1, 0}};
			testee = new UniformChromosomStrategy(rand, area);
			fail("Expected exception");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
}
