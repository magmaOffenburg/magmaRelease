/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.search.strategy.twoPlayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the MiniMaxSearch class
 *
 * @author Last modified by $Author: KDorer $
 * @version $Revision: 1.1 $
 */
public class MiniMaxSearchTestCase
{
	/** strategy instance that can be used for testing */
	private MiniMaxSearch miniMax;

	/**
	 * Sets up the test fixture before every call to a test method.
	 */
	@BeforeEach
	protected void setUp() throws Exception
	{
		miniMax = new MiniMaxSearch();
		miniMax.getClass(); // to prevent warning
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
		// TicTacToe problem = TicTacToe.getInstance();
		// TicTacToeState state = new TicTacToeState(field, true);
		// problem.setCurrentState(state);
		// TicTacToeState successorState;
		// successorState = (TicTacToeState) miniMax.getBestSuccessorState(state);
		// assertEquals(0.0, successorState.getUtility(), 0.001);
	}

	/**
	 * Test the calculation of proper alpha beta values
	 * @throws Exception if any exception occurs during the test
	 */
	@Test
	public void testGetBestSuccessorStateWin() throws Exception
	{
		// char[][] field = { { '*', ' ', '*' }, { '*', 'O', 'O' },
		// { ' ', 'O', ' ' } };
		// TicTacToe problem = TicTacToe.getInstance();
		// TicTacToeState state = new TicTacToeState(field, true);
		// problem.setCurrentState(state);
		// TicTacToeState successorState;
		// successorState = (TicTacToeState) miniMax.getBestSuccessorState(state);
		// assertEquals(1.0, successorState.getUtility(), 0.001);
		//
		// // also the other player may win
		// state = new TicTacToeState(field, false);
		// problem.setCurrentState(state);
		// successorState = (TicTacToeState) miniMax.getBestSuccessorState(state);
		// assertEquals(1.0, successorState.getUtility(), 0.001);
	}

	/**
	 * Test the calculation of proper alpha beta values
	 * @throws Exception if any exception occurs during the test
	 */
	@Test
	public void testGetBestSuccessorStateWinLoose() throws Exception
	{
		// char[][] field = { { '*', ' ', '*' }, { '*', ' ', 'O' },
		// { ' ', 'O', ' ' } };
		// TicTacToe problem = TicTacToe.getInstance();
		// TicTacToeState state = new TicTacToeState(field, true);
		// problem.setCurrentState(state);
		// TicTacToeState successorState;
		// successorState = (TicTacToeState) miniMax.getBestSuccessorState(state);
		// assertEquals(1.0, successorState.getUtility(), 0.001);
		//
		// state = new TicTacToeState(field, false);
		// problem.setCurrentState(state);
		// successorState = (TicTacToeState) miniMax.getBestSuccessorState(state);
		// assertEquals(-1.0, successorState.getUtility(), 0.001);
	}
}
