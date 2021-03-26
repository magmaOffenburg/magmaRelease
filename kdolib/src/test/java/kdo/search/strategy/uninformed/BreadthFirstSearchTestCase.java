/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.strategy.uninformed;

import static org.mockito.Mockito.when;

import java.util.List;
import kdo.search.SearchTestCaseBase;
import kdo.search.representation.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author KDorer
 *
 * Tests the BreadthFirstSearch Implementation of the ISearchStrategy Interface
 */
public class BreadthFirstSearchTestCase extends SearchTestCaseBase
{
	// the instance to use for testing
	BreadthFirstSearch testee;

	/**
	 * Sets up the test fixture before every call to a test method.
	 */
	@Override
	@BeforeEach
	public void setUp() throws Exception
	{
		super.setUp();

		// create a new instance each time
		testee = new BreadthFirstSearch();
	}

	/**
	 * Test search in case of reaching end of searchspace
	 * @throws Exception if any exception occurs during the test
	 */
	@Test
	public void testNoSolution() throws Exception
	{
		when(state3Mock.checkGoalState()).thenReturn(false);
		List<Node> result = testee.search(state3Mock);
		Assertions.assertEquals(null, result);
	}

	/**
	 * Test search in case of root node is goal state
	 * @throws Exception if any exception occurs during the test
	 */
	@Test
	public void testRootNodeGoalState() throws Exception
	{
		when(state3Mock.checkGoalState()).thenReturn(true);
		List<Node> result = testee.search(state3Mock);
		Assertions.assertEquals(1, result.size(), "Unexpected solution length");
		Assertions.assertEquals(state3Mock, result.get(0).getState(), "Unexpected solution");
	}

	/**
	 * Test search in case of a two level tree
	 * @throws Exception if any exception occurs during the test
	 */
	@Test
	public void testTreeWith2Levels() throws Exception
	{
		when(state1Mock.checkGoalState()).thenReturn(false);
		when(state2Mock.checkGoalState()).thenReturn(false);
		when(state3Mock.checkGoalState()).thenReturn(true);
		List<Node> result = testee.search(state1Mock);
		Assertions.assertEquals(2, result.size(), "Unexpected solution length");
		Assertions.assertEquals(state1Mock, result.get(0).getState(), "Unexpected solution");
		Assertions.assertEquals(state3Mock, result.get(1).getState(), "Unexpected solution");
	}
}
