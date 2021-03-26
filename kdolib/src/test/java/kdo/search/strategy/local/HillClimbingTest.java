/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import kdo.domain.IProblemState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author dorer
 *
 */
public class HillClimbingTest extends LocalSearchTestBase
{
	private HillClimbing testee;

	/**
	 * @throws java.lang.Exception
	 */
	@Override
	@BeforeEach
	public void setUp() throws Exception
	{
		super.setUp();
		testee = new HillClimbing(-1);
	}

	@Test
	public void testSearchNoOperators()
	{
		when(operatorIterator1Mock.hasNext()).thenReturn(false);
		when(initStateMock.calculateUtility()).thenReturn(1.0f);

		IProblemState result = testee.search(initStateMock);
		assertEquals(initStateMock, result);
	}

	@Test
	public void testSearchOperatorToWorseState()
	{
		when(operatorIterator1Mock.hasNext()).thenReturn(true).thenReturn(false);
		when(operatorIterator1Mock.next()).thenReturn(operator1Mock);
		when(initStateMock.isWorseThan(successorState1Mock)).thenReturn(false);
		when(initStateMock.calculateUtility()).thenReturn(1.0f);

		IProblemState result = testee.search(initStateMock);
		assertEquals(initStateMock, result);
	}

	@Test
	public void testSearchOperatorToBetterState()
	{
		when(operatorIterator1Mock.hasNext()).thenReturn(true).thenReturn(false);
		when(operatorIterator1Mock.next()).thenReturn(operator1Mock);
		when(initStateMock.isWorseThan(successorState1Mock)).thenReturn(true);
		when(initStateMock.calculateUtility()).thenReturn(1.0f);
		when(successorState1Mock.calculateUtility()).thenReturn(2.0f);
		when(operatorIterator2Mock.hasNext()).thenReturn(false);

		IProblemState result = testee.search(initStateMock);
		assertEquals(successorState1Mock, result);
	}
}
