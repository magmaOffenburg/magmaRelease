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
public class GoalLinkTest extends EBNTestBase
{
	private GoalLink testee;

	private Proposition sourcePropositionMock;

	private Effect destinationPropositionMock;

	@Override
	@BeforeEach
	public void setUp()
	{
		super.setUp();
		sourcePropositionMock = mock(Proposition.class);
		destinationPropositionMock = mock(Effect.class);
		testee = new GoalLink(params, sourcePropositionMock, destinationPropositionMock);
	}

	@Test
	public void testGetGoalActivation()
	{
		when(sourcePropositionMock.getGoalActivation()).thenReturn(1.0);
		when(destinationPropositionMock.getInfluence()).thenReturn(1.0);
		double expected = params.getGamma() * 1.0 * 1.0;
		assertEquals(expected, testee.getActivation(), 0.0001);
	}

	@Test
	public void testGetGoalActivation2()
	{
		when(sourcePropositionMock.getGoalActivation()).thenReturn(0.4);
		when(destinationPropositionMock.getInfluence()).thenReturn(0.6);
		double expected = params.getGamma() * 0.6 * 0.4;
		assertEquals(expected, testee.getActivation(), 0.0001);
	}
}
