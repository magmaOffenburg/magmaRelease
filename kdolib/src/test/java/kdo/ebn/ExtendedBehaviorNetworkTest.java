/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Klaus Dorer
 *
 */
public class ExtendedBehaviorNetworkTest extends EBNTestBase
{
	/** an EBN without any nodes */
	private ExtendedBehaviorNetwork emptyNetwork;

	/** an EBN with all kinds of nodes */
	private ExtendedBehaviorNetwork fullNetwork;

	@Override
	@BeforeEach
	public void setUp()
	{
		super.setUp();
		emptyNetwork = new ExtendedBehaviorNetwork("emptyNet", null);

		fullNetwork = new ExtendedBehaviorNetwork("fullNet", null);
		fullNetwork.addPerception(perceptionTrue1);
		fullNetwork.addPerception(perceptionFalse1);
		fullNetwork.addGoal(goalTrue1);
		fullNetwork.addGoal(goalNotTrue1);
		fullNetwork.addResource(resource1);
	}

	/**
	 * Test method for
	 * {@link magma.agent.decision.ebn.ExtendedBehaviorNetwork#updatePerceptions()}
	 * .
	 */
	@Test
	public void testUpdatePerceptions()
	{
		IEBNPerception beliefMock = mock(IEBNPerception.class);
		when(beliefMock.getName()).thenReturn("testBelief");
		when(beliefMock.getTruthValue()).thenReturn(1.0f);
		when(beliefMock.getTruthValue()).thenReturn(1.0f);
		PerceptionNode ourPerception = new PerceptionNode(beliefMock);
		emptyNetwork.addPerception(ourPerception);
		emptyNetwork.addPerception(perceptionTrue2);
		// has to be called to set outdated to false
		ourPerception.getTruthValue();
		// this call should come from cached truth value
		ourPerception.getTruthValue();

		emptyNetwork.updatePerceptions();
		ourPerception.getTruthValue();
	}

	/**
	 * Test for {@link ExtendedBehaviorNetwork#addCompetenceModule(Competence)}
	 *
	 * @throws NetworkConfigurationException In case of fatal error
	 */
	@Test
	public void testAddCompetenceModule() throws NetworkConfigurationException
	{
		fullNetwork.addCompetenceModule(competence1);
		assertEquals(1, fullNetwork.getCompetenceModulesCount());
		// we just check one connection, to check if connections were done
		// other connections are checked in own test
		assertTrue(competence1.getGoalConnections().hasNext());
	}

	/**
	 * Test for {@link ExtendedBehaviorNetwork#addCompetenceModule(Competence)}
	 *
	 * @throws NetworkConfigurationException In case of fatal error
	 */
	@Test
	public void testAddCompetenceModuleResource() throws NetworkConfigurationException
	{
		fullNetwork.addCompetenceModule(competence2);
		assertEquals(1, fullNetwork.getCompetenceModulesCount());
		assertTrue(competence2.getGoalConnections().hasNext());
	}

	/**
	 * Test for {@link ExtendedBehaviorNetwork#spreadActivation()}
	 *
	 * @throws NetworkConfigurationException In case of fatal error
	 */
	@Test
	public void testSpreadActivation() throws NetworkConfigurationException
	{
		fullNetwork.addCompetenceModule(competence1);
		fullNetwork.addCompetenceModule(competence2);
		assertEquals(2, fullNetwork.getCompetenceModulesCount());

		fullNetwork.spreadActivation();
		assertEquals(1.0, competence1.getExecutability(), 0.01);
		assertEquals(1.0, competence2.getExecutability(), 0.01);
		assertEquals(0.43, competence1.getActivation(), 0.01);
		assertEquals(0.43, competence2.getActivation(), 0.01);
	}

	/**
	 * Test for {@link ExtendedBehaviorNetwork#decide()}
	 *
	 * @throws NetworkConfigurationException In case of fatal error
	 */
	@Test
	public void testDecide() throws NetworkConfigurationException
	{
		fullNetwork.addCompetenceModule(competence1);
		fullNetwork.addCompetenceModule(competence2);

		assertTrue(fullNetwork.decide());
		verify(actionMock1, times(1)).perform();
	}

	/**
	 * Test for {@link ExtendedBehaviorNetwork#decide()}
	 *
	 * @throws NetworkConfigurationException In case of fatal error
	 */
	@Test
	public void testDecideConcurrent() throws NetworkConfigurationException
	{
		fullNetwork.addCompetenceModule(competence1);
		fullNetwork.addCompetenceModule(competence2);
		resource1.setActivation(0.2);

		assertTrue(fullNetwork.decide());
		verify(actionMock1, times(1)).perform();
		verify(actionMock2, times(1)).perform();
	}
}
