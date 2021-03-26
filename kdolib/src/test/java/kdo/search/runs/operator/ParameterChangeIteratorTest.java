/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.runs.operator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import kdo.domain.IIndividuum;
import kdo.domain.model.ParameterChangeIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author dorer
 */
public class ParameterChangeIteratorTest
{
	private ParameterChangeIterator testee;

	private IIndividuum individuumMock;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		individuumMock = mock(IIndividuum.class);
	}

	@Test
	public void testHasNext()
	{
		float[] chromosom = {};
		float[] stepSizes = {};
		when(individuumMock.getChromosom()).thenReturn(chromosom);

		testee = new ParameterChangeIterator(individuumMock, stepSizes);
		assertFalse(testee.hasNext());
	}

	@Test
	public void testNext()
	{
		float[] chromosom = {5, 6};
		float[] stepSizes = {1, 2};
		when(individuumMock.getChromosom()).thenReturn(chromosom);

		testee = new ParameterChangeIterator(individuumMock, stepSizes);
		assertTrue(testee.hasNext());
		assertEquals("Change-0:4.0", testee.next().getName());
		assertEquals("Change+0:6.0", testee.next().getName());
		assertEquals("Change-1:4.0", testee.next().getName());
		assertEquals("Change+1:8.0", testee.next().getName());
		assertFalse(testee.hasNext());
	}
}
