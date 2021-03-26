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
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import kdo.domain.IOperator;
import kdo.domain.ITwoPlayerProblemState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the AlphaBetaSearch class
 *
 * @author Last modified by $Author: KDorer $
 * @version $Revision: 1.2 $
 */
public class AlphaBetaCacheSearchTestCase extends TwoPlayerSearchTestCaseBase
{
	/** alpha beta strategy instance that can be used for testing */
	private AlphaBetaCacheSearch alphaBetaCache;

	/**
	 * Sets up the test fixture before every call to a test method.
	 */
	@Override
	@BeforeEach
	public void setUp() throws Exception
	{
		super.setUp();

		alphaBetaCache = new AlphaBetaCacheSearch(500);
	}

	/**
	 * Test that no operators are returned by initial state
	 * @throws Exception if any exception occurs during the test
	 */
	@Test
	public void testTree() throws Exception
	{
		List<IOperator> operators = new ArrayList<>();
		operators.add(operator3Mock);
		operators.add(operator1Mock);
		operators.add(operator1Mock);
		operators.add(operator2Mock);
		when(state1Mock.getOperators()).thenReturn(operators);
		List<IOperator> operators2 = new ArrayList<>();
		operators.add(operator3Mock);
		when(state2Mock.getOperators()).thenReturn(operators2);
		when(state3Mock.getOperators()).thenReturn(new ArrayList<IOperator>());

		ITwoPlayerProblemState resultState = alphaBetaCache.getBestSuccessorState((ITwoPlayerProblemState) state1Mock);
		assertTrue(resultState == state3Mock, "Invalid state returned");
		// todo: create test that uses buffer
		// assertEquals(true, alphaBetaCache.getBufferHits() > 0);
	}

	/**
	 * Test the calculation of proper alpha beta values
	 * @throws Exception if any exception occurs during the test
	 */
	@Test
	public void testGetBestSuccessorState() throws Exception
	{
		// char[][] field = { { ' ', ' ', ' ' }, { ' ', ' ', ' ' },
		// { ' ', ' ', ' ' } };
		// TicTacToeState state = new TicTacToeState(field, true);
		// TicTacToe problem = TicTacToe.getInstance();
		// problem.setCurrentState(state);
		// TicTacToeState successorState;
		// successorState = (TicTacToeState) alphaBetaCache
		// .getBestSuccessorState((ITwoPlayerProblemState) problem
		// .getCurrentState());
		// assertEquals(0.0, successorState.getUtility(), 0.001);
		// assertEquals(true, alphaBetaCache.getBufferHits() > 0);
	}

	/**
	 * Compare the calculation of alpha beta and alpha beta with cache
	 * @throws Exception if any exception occurs during the test
	 */
	@Test
	public void testGetBestSuccessorStateCompareToAlphaBeta() throws Exception
	{
		// VierGewinnt problem = VierGewinnt.getInstance(VierGewinnt.LEVEL_MEDIUM,
		// null, null);
		// AlphaBetaSearch alphaBeta = new AlphaBetaSearch();
		//
		// VierGewinntState successorAlphaBeta;
		// VierGewinntState successorAlphaBetaCache;
		//
		// // check for several cutoff depths
		// for (int depth = 3; depth < 8; depth++) {
		// VierGewinntStrategie.depthMediumMax = depth;
		// VierGewinntStrategie.depthMediumMin = depth;
		//
		// successorAlphaBetaCache = (VierGewinntState) alphaBetaCache
		// .getBestSuccessorState((ITwoPlayerProblemState) problem
		// .getCurrentState());
		// successorAlphaBeta = (VierGewinntState) alphaBeta
		// .getBestSuccessorState((ITwoPlayerProblemState) problem
		// .getCurrentState());
		//
		// assertEquals("unequal utility at depth " + depth, successorAlphaBeta
		// .getUtility(), successorAlphaBetaCache.getUtility(), 0.01);
		// assertEquals("unequal states at depth " + depth, successorAlphaBeta,
		// successorAlphaBetaCache);
		// }
	}
}
