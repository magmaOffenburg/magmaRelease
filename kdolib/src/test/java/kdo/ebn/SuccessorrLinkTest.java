/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Klaus Dorer
 *
 */
public class SuccessorrLinkTest extends EBNTestBase
{
	private SuccessorLink testee;

	private Proposition sourcePropositionMock;

	private Effect destinationPropositionMock;

	@Override
	@BeforeEach
	public void setUp()
	{
		super.setUp();
		sourcePropositionMock = mock(Proposition.class);
		destinationPropositionMock = mock(Effect.class);
		testee = new SuccessorLink(params, sourcePropositionMock, destinationPropositionMock);
	}

	/**
	 * Test method for
	 * {@link magma.agent.decision.ebn.ConflictorLink#getGoalActivation(int)}.
	 */
	@Test
	public void testGetGoalActivation()
	{
		when(sourcePropositionMock.getTransferedActivation(0)).thenReturn(1.0);
		when(sourcePropositionMock.getTruthValue()).thenReturn(1.0);
		when(destinationPropositionMock.getInfluence()).thenReturn(1.0);
		assertEquals(0.0, testee.getGoalActivation(0), 0.0001);
		assertEquals(0.0, testee.getActivation(), 0.0001);
	}

	/**
	 * Test method for
	 * {@link magma.agent.decision.ebn.ConflictorLink#getGoalActivation(int)}.
	 */
	@Test
	public void testGetGoalActivation2()
	{
		when(sourcePropositionMock.getTransferedActivation(0)).thenReturn(0.5);
		when(sourcePropositionMock.getTruthValue()).thenReturn(0.3);
		when(destinationPropositionMock.getInfluence()).thenReturn(0.5);
		double expected = params.getGamma() * 0.5 * 0.7 * 0.5;
		assertEquals(expected, testee.getGoalActivation(0), 0.0001);
		assertEquals(expected, testee.getActivation(), 0.0001);
	}
}
