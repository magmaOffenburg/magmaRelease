/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import kdo.domain.IOperator;
import kdo.domain.IProblem;
import kdo.domain.IProblemState;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for all search based testcases providing some ready to use mocks
 *
 * @author Last modified by $Author: KDorer $
 * @version $Revision: 1.2 $
 */
public class SearchTestCaseBase
{
	protected IProblemState state1Mock;

	protected IProblemState state2Mock;

	protected IProblemState state3Mock;

	protected IOperator operator1Mock;

	protected IOperator operator2Mock;

	protected IOperator operator3Mock;

	protected IProblem problemMock;

	/**
	 * Sets up the test fixture before every call to a test method.
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		state1Mock = createStateMock(1.0f);
		state2Mock = createStateMock(2.0f);
		state3Mock = createStateMock(7.0f);
		operator1Mock = createOperatorMock(state1Mock);
		operator2Mock = createOperatorMock(state2Mock);
		operator3Mock = createOperatorMock(state3Mock);
		problemMock = createProblemMock(state1Mock);

		List<IOperator> operators = new ArrayList<>();
		operators.add(operator2Mock);
		operators.add(operator3Mock);
		when(state1Mock.getOperators()).thenReturn(operators);
		when(state2Mock.getOperators()).thenReturn(new ArrayList<IOperator>());
		when(state3Mock.getOperators()).thenReturn(new ArrayList<IOperator>());
	}

	/**
	 * Creates a default MockControl for a problem
	 * @param stateMock the problem's current state
	 * @return a default MockControl for a problem
	 */
	protected IProblem createProblemMock(IProblemState stateMock)
	{
		IProblem problemMock = createEmptyProblemMock();
		when(problemMock.getCurrentState()).thenReturn(stateMock);
		return problemMock;
	}

	/**
	 * Override this method if your test class needs more specific IProblem
	 * instances
	 * @return a newly created IProblem mock
	 */
	protected IProblem createEmptyProblemMock()
	{
		return mock(IProblem.class);
	}

	/**
	 * Creates a default MockControl for a problem state
	 *
	 * @return a default MockControl for a problem state
	 */
	protected IProblemState createStateMock(float utility)
	{
		IProblemState stateMock = createEmptyProblemStateMock();
		when(stateMock.getUtility()).thenReturn(utility);
		return stateMock;
	}

	/**
	 * Override this method if your test class needs more specific IProblemState
	 * instances
	 * @return a newly created IProblemState mock
	 */
	protected IProblemState createEmptyProblemStateMock()
	{
		return mock(IProblemState.class);
	}

	/**
	 * Creates a default MockControl for an operator having the passed state as
	 * successor. Calls replay.
	 * @param stateMock the control of the successor state of this operator
	 * @return a default MockControl for an operator
	 */
	protected IOperator createOperatorMock(IProblemState stateMock)
	{
		IOperator mock = mock(IOperator.class);
		when(mock.getSuccessorState((IProblemState) any(Object.class))).thenReturn(stateMock);
		when(mock.getCost()).thenReturn(1.0f);
		return mock;
	}

	/**
	 * Override this method if your test class needs more specific IOperator
	 * instances
	 * @return a newly created IOperator mock
	 */
	protected IOperator createEmptyOperatorMock()
	{
		return mock(IOperator.class);
	}
}
