/*
 * Copyright (c) 2001-2004 Living Systems(R) GmbH, Germany.
 * All rights reserved.
 * Original Author: kdorer
 *
 * $Source: /cvs/Research/lecture/AI/testsrc/search/strategy/twoPlayer/TwoPlayerSearchTestCaseBase.java,v $
 * $Date: 2007/01/05 15:56:11 $
 */
package kdo.search.strategy.twoPlayer;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import kdo.domain.IProblemState;
import kdo.domain.ITwoPlayerProblemState;
import kdo.search.SearchTestCaseBase;

/**
 * Extends SearchTestCaseBase with two player specific features
 *
 * @author Last modified by $Author: KDorer $
 * @version $Revision: 1.2 $
 */
public class TwoPlayerSearchTestCaseBase extends SearchTestCaseBase
{
	/**
	 * Adds replies to two player problem state methods
	 *
	 * @param utility the utility of the state
	 * @return a new IProblemState mock
	 *
	 * @see kdo.search.SearchTestCaseBase#createStateMock(float)
	 */
	@Override
	protected IProblemState createStateMock(float utility)
	{
		ITwoPlayerProblemState stateMock = (ITwoPlayerProblemState) super.createStateMock(utility);
		when(stateMock.getNextPlayerFlag()).thenReturn(true);
		when(stateMock.isCutoffState(anyInt(), anyBoolean())).thenReturn(false);
		when(stateMock.getTwoPlayerUtility(anyBoolean())).thenReturn(utility);
		return stateMock;
	}

	/**
	 * @return a newly created ITwoPlayerProblemState mock
	 */
	@Override
	protected IProblemState createEmptyProblemStateMock()
	{
		return mock(ITwoPlayerProblemState.class);
	}
}
