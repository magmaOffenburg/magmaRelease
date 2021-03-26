/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class CompetenceTest extends EBNTestBase
{
	private List<Competence> competences;

	private List<Goal> goals;

	private List<Resource> resources;

	@Override
	@BeforeEach
	public void setUp()
	{
		super.setUp();
		competences = new ArrayList<>();
		competences.add(competence1);

		goals = new ArrayList<>();
		goals.add(goalTrue1);
		goals.add(goalNotTrue1);

		resources = new ArrayList<>();
		resources.add(resource0);
		resources.add(resource1);
		resources.add(resource2);
	}

	@Test
	public void testAddPrecondition()
	{
		Proposition precondition = new Proposition(null, perceptionTrue1, false);
		emptyCompetence.addPrecondition(precondition);
		assertEquals(emptyCompetence, precondition.getContainingNode());
		assertEquals(precondition, emptyCompetence.getPrecondition().getPropositions().next());
	}

	@Test
	public void testAddEffect()
	{
		Effect effect = new Effect(null, perceptionTrue1, false, 1.0, null);
		emptyCompetence.addEffect(effect);
		assertEquals(emptyCompetence, effect.getContainingNode());
		assertEquals(effect, emptyCompetence.getEffects().next());
	}

	@Test
	public void testAddGoalLink()
	{
		Proposition source = new Proposition(null, perceptionTrue1, false);
		Proposition destination = new Proposition(null, perceptionTrue1, false);
		destination.setContainingNode(emptyCompetence);
		emptyCompetence.addGoalLink(source, destination);
		Iterator<GoalConnection> connections = emptyCompetence.getGoalConnections();
		Connection newConnection = connections.next();
		assertTrue(newConnection instanceof GoalLink);
		assertEquals(source, newConnection.getSourceProposition());
		assertEquals(destination, newConnection.getDestinationProposition());
		assertFalse(connections.hasNext());
	}

	@Test
	public void testAddProtectedGoalLink()
	{
		Proposition source = new Proposition(null, perceptionTrue1, false);
		Proposition destination = new Proposition(null, perceptionTrue1, true);
		destination.setContainingNode(emptyCompetence);
		emptyCompetence.addProtectedGoalLink(source, destination);
		Iterator<GoalConnection> connections = emptyCompetence.getGoalConnections();
		Connection newConnection = connections.next();
		assertTrue(newConnection instanceof ProtectedGoalLink);
		assertEquals(source, newConnection.getSourceProposition());
		assertEquals(destination, newConnection.getDestinationProposition());
		assertFalse(connections.hasNext());
	}

	@Test
	public void testAddSuccessor()
	{
		Proposition source = new Proposition(null, perceptionTrue1, false);
		Proposition destination = new Proposition(null, perceptionTrue1, true);
		destination.setContainingNode(emptyCompetence);
		emptyCompetence.addSuccessor(source, destination);
		Iterator<CompetenceConnection> connections = emptyCompetence.getCompetenceConnections();
		Connection newConnection = connections.next();
		assertTrue(newConnection instanceof SuccessorLink);
		assertEquals(source, newConnection.getSourceProposition());
		assertEquals(destination, newConnection.getDestinationProposition());
		assertFalse(connections.hasNext());
	}

	@Test
	public void testAddConflictor()
	{
		Proposition source = new Proposition(null, perceptionTrue1, false);
		Proposition destination = new Proposition(null, perceptionTrue1, true);
		destination.setContainingNode(emptyCompetence);
		emptyCompetence.addConflictor(source, destination);
		Iterator<CompetenceConnection> connections = emptyCompetence.getCompetenceConnections();
		Connection newConnection = connections.next();
		assertTrue(newConnection instanceof ConflictorLink);
		assertEquals(source, newConnection.getSourceProposition());
		assertEquals(destination, newConnection.getDestinationProposition());
		assertFalse(connections.hasNext());
	}

	@Test
	public void testConnectCompetence() throws Exception
	{
		competence2.connectCompetence(competences.iterator(), goals.iterator(), resources.iterator());

		// check goal links
		Iterator<GoalConnection> connect1 = competence2.getGoalConnections();
		Connection connection = connect1.next();
		assertTrue(connection instanceof GoalLink);
		assertEquals(goalTrue1, connection.getSourceModule());
		assertEquals(competence2, connection.getDestinationModule());
		connection = connect1.next();
		assertTrue(connection instanceof ProtectedGoalLink);
		assertEquals(goalNotTrue1, connection.getSourceModule());
		assertEquals(competence2, connection.getDestinationModule());
		assertFalse(connect1.hasNext());

		// check competence links
		Iterator<CompetenceConnection> connections = competence2.getCompetenceConnections();
		connection = connections.next();
		assertTrue(connection instanceof SuccessorLink);
		assertEquals(competence1, connection.getSourceModule());
		assertEquals(competence2, connection.getDestinationModule());
		connection = connections.next();
		assertTrue(connection instanceof ConflictorLink);
		assertEquals(competence1, connection.getSourceModule());
		assertEquals(competence2, connection.getDestinationModule());
		assertFalse(connections.hasNext());

		// check link of other competence
		connections = competence1.getCompetenceConnections();
		connection = connections.next();
		assertTrue(connection instanceof SuccessorLink);
		assertEquals(competence2, connection.getSourceModule());
		assertEquals(competence1, connection.getDestinationModule());
		connection = connections.next();
		assertTrue(connection instanceof ConflictorLink);
		assertEquals(competence2, connection.getSourceModule());
		assertEquals(competence1, connection.getDestinationModule());
		assertFalse(connections.hasNext());

		// check resource links
		Iterator<Connection> connect3 = competence2.getResourceConnections();
		connection = connect3.next();
		assertTrue(connection instanceof ResourceLink);
		assertEquals(resource1, connection.getSourceModule());
		assertEquals(competence2, connection.getDestinationModule());
		assertFalse(connect3.hasNext());
	}

	@Test
	public void testConnectCompetenceNoResourceNode()
	{
		// add not existing resource
		competence2.addResource(new ResourceProposition(competence2, perceptionFalse2, 1));

		assertThrows(NetworkConfigurationException.class, () -> {
			competence2.connectCompetence(competences.iterator(), goals.iterator(), resources.iterator());
			// we when an exception here
		});
	}

	@Test
	public void testCalculateExternActivationOne()
	{
		int goalIndex = 0;
		GoalConnection link1Mock = mock(GoalConnection.class);
		when(link1Mock.getActivation()).thenReturn(0.7);
		when(link1Mock.getGoalIndex()).thenReturn(goalIndex);
		emptyCompetence.addGoalConnection(link1Mock);
		emptyCompetence.calculateExternActivation();
		emptyCompetence.getGoalTracking().setToNewActivation();
		assertEquals(0.7, emptyCompetence.getGoalTracking().getActivation(goalIndex), 0.001);
	}

	@Test
	public void testCalculateExternActivationTwo()
	{
		GoalConnection link1Mock = mock(GoalConnection.class);
		when(link1Mock.getActivation()).thenReturn(0.7);
		when(link1Mock.getGoalIndex()).thenReturn(0);
		GoalConnection link2Mock = mock(GoalConnection.class);
		when(link2Mock.getActivation()).thenReturn(0.6);
		when(link2Mock.getGoalIndex()).thenReturn(0);
		emptyCompetence.addGoalConnection(link1Mock);
		emptyCompetence.addGoalConnection(link2Mock);
		emptyCompetence.calculateExternActivation();
		emptyCompetence.getGoalTracking().setToNewActivation();
		assertEquals(0.7, emptyCompetence.getGoalTracking().getActivation(0), 0.001);
	}

	@Test
	public void testCalculateSpreadingActivation()
	{
		CompetenceConnection link1Mock = mock(CompetenceConnection.class);
		when(link1Mock.getGoalActivation(0)).thenReturn(0.7);
		when(link1Mock.getGoalActivation(1)).thenReturn(0.4);
		emptyCompetence.addCompetenceConnection(link1Mock);
		emptyCompetence.calculateSpreadingActivation();
		emptyCompetence.getGoalTracking().setToNewActivation();
		assertEquals(0.7, emptyCompetence.getGoalTracking().getActivation(0), 0.001);
		assertEquals(0.4, emptyCompetence.getGoalTracking().getActivation(1), 0.001);
	}

	@Test
	public void testCalculateExecutability()
	{
		assertEquals(1.0, emptyCompetence.calculateExecutability(), 0.001);
		assertEquals(1.0, competence1.calculateExecutability(), 0.001);
	}

	@Test
	public void testCalculateExecutabilityFuzzy()
	{
		Competence comp = new Competence("testCompetence", 1, params);
		Proposition propMock1 = mock(Proposition.class);
		propMock1.setContainingNode(comp);
		when(propMock1.getTruthValue()).thenReturn(0.6);
		Proposition propMock2 = mock(Proposition.class);
		propMock2.setContainingNode(comp);
		when(propMock2.getTruthValue()).thenReturn(0.7);
		comp.addPrecondition(propMock1);
		comp.addPrecondition(propMock2);
		assertEquals(0.6 * 0.7, comp.calculateExecutability(), 0.001);
	}

	@Test
	public void testCalculateActivationEmpty()
	{
		assertEquals(0.0, emptyCompetence.calculateActivation(), 0.001);
	}

	@Test
	public void testCalculateActivation()
	{
		GoalConnection link1Mock = mock(GoalConnection.class);
		when(link1Mock.getActivation()).thenReturn(0.7);
		when(link1Mock.getGoalIndex()).thenReturn(0);
		GoalConnection link2Mock = mock(GoalConnection.class);
		when(link2Mock.getActivation()).thenReturn(-0.3);
		when(link2Mock.getGoalIndex()).thenReturn(1);
		emptyCompetence.addGoalConnection(link1Mock);
		emptyCompetence.addGoalConnection(link2Mock);
		assertEquals(0.4, emptyCompetence.calculateActivation(), 0.00001);
		emptyCompetence.setToNewActivation();
		assertEquals(params.getBeta() * 0.4 + 0.4, emptyCompetence.calculateActivation(), 0.00001);
	}

	@Test
	public void testPerform() throws Exception
	{
		preparePerformTest();
		competence1.perform();
		assertTrue(competence1.isExecuted());
		verify(actionMock1, times(1)).perform();
	}

	/**
	 * @throws NetworkConfigurationException In case of fatal error
	 */
	private void preparePerformTest() throws NetworkConfigurationException
	{
		competence1.connectCompetence(competences.iterator(), goals.iterator(), resources.iterator());
		competence1.calculateExecutability();
		competence1.calculateActivation();
		competence1.setToNewActivation();
	}

	@Disabled
	@Test
	public void testPerformWithException() throws Exception
	{
		preparePerformTest();

		doThrow(new RuntimeException()).when(actionMock1).perform();

		competence1.perform();
		assertFalse(competence1.isExecuted());
		verify(actionMock1, times(1)).perform();
	}

	@Test
	public void testPerformWithResourceNotEnoughAmount() throws Exception
	{
		competence1.addResource(new ResourceProposition(competence1, resource0, 1));
		preparePerformTest();
		resource0.setActivation(0.2);

		competence1.perform();
		assertFalse(competence1.isExecuted());
	}

	@Test
	public void testPerformWithResource() throws Exception
	{
		competence1.addResource(new ResourceProposition(competence1, resource1, 1));
		preparePerformTest();
		resource1.setActivation(0.2);

		competence1.perform();
		assertTrue(competence1.isExecuted(), "Action was not executed");
		verify(actionMock1, times(1)).perform();
	}
}
