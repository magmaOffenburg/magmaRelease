/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.concurrent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import kdo.domain.IProblemState;
import kdo.search.strategy.local.LocalSearchTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author dorer
 *
 */
public class HillClimbingCCTest extends LocalSearchTestBase
{
	private HillClimbingCC testee;

	/**
	 * @throws java.lang.Exception
	 */
	@Override
	@BeforeEach
	public void setUp() throws Exception
	{
		super.setUp();
		testee = new HillClimbingCC(-1, -1);
	}

	@Test
	public void testSearchInitialStateOutdated()
	{
		when(problemMock.getConcurrentState()).thenReturn(successorState1Mock);
		when(problemMock.concurrentStateHasChanged(initStateMock)).thenReturn(true);
		when(problemMock.concurrentStateHasChanged(successorState1Mock)).thenReturn(false).thenReturn(false);
		when(operatorIterator2Mock.hasNext()).thenReturn(false);
		when(initStateMock.calculateUtility()).thenReturn(1.0f);

		IProblemState result = testee.search(initStateMock);
		assertEquals(successorState1Mock, result);
	}

	@Test
	public void testSearchPerformStateChangeOutdated()
	{
		when(problemMock.getConcurrentState()).thenReturn(initStateMock).thenReturn(successorState1Mock);
		when(problemMock.concurrentStateHasChanged(initStateMock)).thenReturn(false);
		when(operatorIterator1Mock.hasNext()).thenReturn(true).thenReturn(false);
		when(operatorIterator1Mock.next()).thenReturn(operator1Mock);
		when(initStateMock.calculateUtility()).thenReturn(1.0f);
		when(successorState1Mock.calculateUtility()).thenReturn(2.0f);
		when(initStateMock.isWorseThan(successorState1Mock)).thenReturn(true);
		when(operatorIterator2Mock.hasNext()).thenReturn(false);
		when(problemMock.concurrentStateHasChanged(successorState1Mock)).thenReturn(false);

		when(problemMock.switchConcurrentState(initStateMock, successorState1Mock)).thenReturn(null);

		IProblemState result = testee.search(initStateMock);
		assertEquals(successorState1Mock, result);
	}

	@Test
	public void testSearchDoNotStopIfConcurrentChange()
	{
		when(problemMock.getConcurrentState()).thenReturn(initStateMock).thenReturn(successorState1Mock);
		when(problemMock.concurrentStateHasChanged(initStateMock)).thenReturn(false).thenReturn(true);
		when(operatorIterator1Mock.hasNext()).thenReturn(false);
		when(operatorIterator2Mock.hasNext()).thenReturn(false);
		when(problemMock.concurrentStateHasChanged(successorState1Mock)).thenReturn(false);
		when(problemMock.switchConcurrentState(initStateMock, initStateMock)).thenReturn(null);
		when(initStateMock.calculateUtility()).thenReturn(1.0f);

		IProblemState result = testee.search(initStateMock);
		assertEquals(successorState1Mock, result);
	}
}
