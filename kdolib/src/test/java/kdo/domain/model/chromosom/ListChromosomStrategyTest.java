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
public class ListChromosomStrategyTest
{
	private static final int SIZE = 3;

	private ListChromosomStrategy testee;

	private IRandomSource rand;

	@BeforeEach
	public void setUp() throws Exception
	{
		rand = mock(IRandomSource.class);
		float[] values = {8.5f, -2, 6, 11};
		testee = new ListChromosomStrategy(rand, values, SIZE);
	}

	@Test
	public void testGetRandomAlele()
	{
		when(rand.nextInt(SIZE)).thenReturn(0).thenReturn(3);
		assertEquals(8.5f, testee.getRandomAlele(0, null), 0.001f);
		assertEquals(11f, testee.getRandomAlele(0, null), 0.001f);
	}
}
