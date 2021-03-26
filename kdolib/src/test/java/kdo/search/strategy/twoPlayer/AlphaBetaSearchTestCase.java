/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.search.strategy.twoPlayer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import kdo.domain.IOperator;
import kdo.domain.ITwoPlayerProblemState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the AlphaBetaSearch class
 *
 * @author Last modified by $Author: KDorer $
 * @version $Revision: 1.3 $
 */
public class AlphaBetaSearchTestCase extends TwoPlayerSearchTestCaseBase
{
	/** alpha beta strategy instance that can be used for testing */
	private AlphaBetaSearch alphaBeta;

	/**
	 * Sets up the test fixture before every call to a test method.
	 */
	@Override
	@BeforeEach
	public void setUp() throws Exception
	{
		super.setUp();

		alphaBeta = new AlphaBetaSearch();
	}

	/**
	 * Test cutoff
	 * @throws Exception if any exception occurs during the test
	 */
	@Test
	public void testInitialCutoff() throws Exception
	{
		when(((ITwoPlayerProblemState) state1Mock).isCutoffState(anyInt(), anyBoolean())).thenReturn(true);

		ITwoPlayerProblemState resultState = alphaBeta.getBestSuccessorState((ITwoPlayerProblemState) state1Mock);
		assertTrue(resultState == state1Mock, "Invalid state returned");
	}

	/**
	 * Test that no operators are returned by initial state
	 * @throws Exception if any exception occurs during the test
	 */
	@Test
	public void testNoOperators() throws Exception
	{
		when(state1Mock.getOperators()).thenReturn(new ArrayList<IOperator>());

		ITwoPlayerProblemState resultState = alphaBeta.getBestSuccessorState((ITwoPlayerProblemState) state1Mock);
		assertTrue(resultState == state1Mock, "Invalid state returned");
	}

	/**
	 * Test that no operators are returned by initial state
	 * @throws Exception if any exception occurs during the test
	 */
	@Test
	public void testTree() throws Exception
	{
		ITwoPlayerProblemState resultState = alphaBeta.getBestSuccessorState((ITwoPlayerProblemState) state1Mock);
		assertTrue(resultState == state3Mock, "Invalid state returned");
	}
}
