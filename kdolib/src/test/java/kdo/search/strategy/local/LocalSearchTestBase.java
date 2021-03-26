/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import kdo.domain.IConcurrentProblem;
import kdo.domain.IIncrementalNeighborhoodIterator;
import kdo.domain.IOperator;
import kdo.domain.IProblem;
import kdo.domain.IProblemState;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author dorer
 *
 */
public class LocalSearchTestBase
{
	protected IConcurrentProblem problemMock;

	protected IProblemState initStateMock;

	protected IProblemState successorState1Mock;

	protected IIncrementalNeighborhoodIterator<IOperator> operatorIterator1Mock;

	protected IOperator operator1Mock;

	protected IIncrementalNeighborhoodIterator<IOperator> operatorIterator2Mock;

	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("unchecked")
	@BeforeEach
	public void setUp() throws Exception
	{
		problemMock = mock(IConcurrentProblem.class);

		operatorIterator1Mock = mock(IIncrementalNeighborhoodIterator.class);
		operator1Mock = mock(IOperator.class);
		initStateMock = createStateMock("init", problemMock, operatorIterator1Mock, 10.0f);

		operatorIterator2Mock = mock(IIncrementalNeighborhoodIterator.class);
		successorState1Mock = createStateMock("succ1", problemMock, operatorIterator2Mock, 5.0f);
		when(operator1Mock.getSuccessorState(initStateMock)).thenReturn(successorState1Mock);
	}

	/**
	 * @param name TODO
	 * @param utility TODO
	 *
	 */
	protected IProblemState createStateMock(
			String name, IProblem problem, IIncrementalNeighborhoodIterator<IOperator> operatorIterator, float utility)
	{
		IProblemState stateMock = mock(IProblemState.class, name);
		when(stateMock.getProblem()).thenReturn(problem);
		when(stateMock.operatorIterator()).thenReturn(operatorIterator);
		when(stateMock.getUtility()).thenReturn(utility);
		when(operatorIterator.nextNeighborhood()).thenReturn(false);
		stateMock.onSelection((IOperator) any());
		return stateMock;
	}
}
