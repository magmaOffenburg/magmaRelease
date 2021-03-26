/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package magma.agent.decision.behavior.movement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author kdorer
 *
 */
public class MovementPhaseTest
{
	private MovementPhase testee;

	private MovementPhase previous;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		previous = new MovementPhase("previous", 10).add("J1", 10);
		testee = new MovementPhase("testee", 10).add("J1", 30).add("J2", 40);
	}

	/**
	 * Test method for
	 * {@link
	 * magma.agent.decision.behavior.movement.MovementPhase#setSpeeds(magma.agent.decision.behavior.movement.MovementPhase)}
	 * .
	 */
	@Test
	public void testSetSpeedsWithPreviousPhase()
	{
		testee.setSpeeds(previous);
		assertEquals(2.0f, testee.find("J1").getSpeed(), 0.001);
		assertEquals(4.0f, testee.find("J2").getSpeed(), 0.001);
	}

	public void testSetSpeedsNoPreviousPhase()
	{
		testee.setSpeeds(null);
		assertEquals(3.0f, testee.find("J1").getSpeed(), 0.001);
	}

	public void testSetSpeedsEmptyCycle()
	{
		testee = new MovementPhase("testee", 0).add("J1", 30);
		testee.setSpeeds(previous);
		assertEquals(1.0f, testee.find("J1").getSpeed(), 0.001);
	}

	public void testSetSpeedsNegative()
	{
		testee = new MovementPhase("testee", 10).add("J1", -10);
		testee.setSpeeds(previous);
		assertEquals(2.0f, testee.find("J1").getSpeed(), 0.001);
	}
}
