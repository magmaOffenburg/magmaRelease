/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain.model.chromosom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import kdo.util.IRandomSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author kdorer
 *
 */
public class GausianChromosomStrategyTest
{
	private GausianChromosomStrategy testee;

	private IRandomSource rand;

	@BeforeEach
	public void setUp() throws Exception
	{
		rand = mock(IRandomSource.class);
		testee = new GausianChromosomStrategy(rand, 5.0f, 2.0f, 2);
	}

	@Test
	public void testGetRandomAlele()
	{
		when(rand.nextGaussian()).thenReturn(0.0).thenReturn(-0.5);
		assertEquals(5.0f, testee.getRandomAlele(0, null), 0.001f);
		assertEquals(4.0f, testee.getRandomAlele(0, null), 0.001f);
	}

	@Test
	public void testGausianChromosomStrategy()
	{
		when(rand.nextGaussian()).thenReturn(1.0).thenReturn(1.0);
		float[] mean = {-1, 0, 2};
		testee = new GausianChromosomStrategy(rand, mean, 0.5f);
		assertEquals(-0.5, testee.getRandomAlele(0, null), 0.001f);
		assertEquals(0.01f, testee.getRandomAlele(1, null), 0.001f);
	}
}
