/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import java.util.HashMap;
import java.util.Map;
import magma.agent.IMagmaConstants;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Klaus Dorer, korak
 *
 */
public class PlayerTest
{
	private static final float TEST_RUN_SPEED = 0.5f;

	private static final float TEST_TURN_SPEED = 20.0f;

	private static final float TEST_SIDE_SPEED = 0.1f;

	private Player testee;

	@BeforeEach
	public void setUp() throws Exception
	{
		testee = new Player(0, "testTeam", true, 0.02f);
		testee.forwardSpeed = TEST_RUN_SPEED;
		testee.turnSpeed = TEST_TURN_SPEED;
		testee.sideStepSpeed = TEST_SIDE_SPEED;
		testee.setPosition(new Vector3D(0.0, 0.0, 0.0));
		testee.setLastSeenTime(5.0f);
		testee.setGlobalOrientation(Rotation.IDENTITY);
	}

	/**
	 * Test for {@link Player#getTimeToTurnAndRun(Vector3D, Angle)}
	 */
	@Test
	public void testGetTimeToTurnAndRunAlreadyThere()
	{
		Vector3D position = new Vector3D(0.0, 0.0, 0.0);
		Angle directionAtTarget = Angle.deg(0);
		assertEquals(0.0, testee.getTimeToTurnAndRun(position, directionAtTarget), 0.001);
	}

	/**
	 * Test for {@link Player#getTimeToTurnAndRun(Vector3D, Angle)}
	 */
	@Test
	public void testGetTimeToTurnAndRunNoTurnButRun()
	{
		Vector3D position = new Vector3D(3.0, 0.0, 0.0);
		Angle directionAtTarget = Angle.deg(0);
		assertEquals(3.0 / TEST_RUN_SPEED, testee.getTimeToTurnAndRun(position, directionAtTarget), 0.001);
	}

	/**
	 * Test for {@link Player#getTimeToTurnAndRun(Vector3D, Angle)}
	 */
	@Test
	public void testGetTimeToTurnAndRunTurnAtStart()
	{
		Vector3D position = new Vector3D(0.0, 0.0, 0.0);
		Angle directionAtTarget = Angle.deg(0);
		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(-60)));
		assertEquals(60 / TEST_TURN_SPEED, testee.getTimeToTurnAndRun(position, directionAtTarget), 0.001);
	}

	/**
	 * Test for {@link Player#getTimeToTurnAndRun(Vector3D, Angle)}
	 */
	@Test
	public void testGetTimeToTurnAndRunTurnAtEnd()
	{
		Vector3D position = new Vector3D(0.0, 0.0, 0.0);
		Angle directionAtTarget = Angle.deg(80);
		assertEquals(80 / TEST_TURN_SPEED, testee.getTimeToTurnAndRun(position, directionAtTarget), 0.001);
	}

	/**
	 * Test for {@link Player#getTimeToTurnAndRun(Vector3D, Angle)}
	 */
	@Test
	public void testGetTimeToTurnAndRunAll()
	{
		Vector3D position = new Vector3D(-3.0, 0.0, 0.0);
		Angle directionAtTarget = Angle.deg(160);
		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(-160)));
		float expected = (float) (20 / TEST_TURN_SPEED + 3.0 / TEST_RUN_SPEED + 20 / TEST_TURN_SPEED);
		assertEquals(expected, testee.getTimeToTurnAndRun(position, directionAtTarget), 0.001);
	}

	/**
	 * Test for {@link Player#getTimeForSideStep(Vector3D, Angle, boolean)}
	 */
	@Test
	public void testGetTimeForSideStep()
	{
		Vector3D position = new Vector3D(3.0, 0.0, 0.0);
		Angle directionAtTarget = Angle.deg(80);
		float expected = (float) (90 / TEST_TURN_SPEED + 3.0 / TEST_SIDE_SPEED + 170 / TEST_TURN_SPEED);
		assertEquals(expected, testee.getTimeForSideStep(position, directionAtTarget, true), 0.001);
	}

	/*
	 * Test for {@Link Player#updateLying(float time)}
	 */
	@Test
	public void testSetLying()
	{
		// Test the case in which we're lying and no time has passed
		Map<String, Vector3D> allBodyParts = new HashMap<>();
		allBodyParts.put("a", new Vector3D(0, 0, 0));
		allBodyParts.put("b", new Vector3D(0, 0, 0));
		allBodyParts.put("c", new Vector3D(0, 0, 0));
		testee.setBodyParts(allBodyParts);
		testee.updateLying(0);
		assertTrue(testee.isLying());

		// Test the case in which we're standing and little time has passed
		allBodyParts = new HashMap<>();
		allBodyParts.put("a", new Vector3D(0, 0, 0.4));
		allBodyParts.put("b", new Vector3D(0, 0, 0));
		allBodyParts.put("c", new Vector3D(0, 0, 0));
		testee.setBodyParts(allBodyParts);
		testee.updateLying(0.2f);
		assertTrue(testee.isLying());

		// Test the case in which we're standing and the time has almost passed
		allBodyParts = new HashMap<>();
		allBodyParts.put("a", new Vector3D(0, 0, 0.4));
		allBodyParts.put("b", new Vector3D(0, 0, 0));
		allBodyParts.put("c", new Vector3D(0, 0, 0));
		testee.setBodyParts(allBodyParts);
		testee.updateLying(0.4f);
		assertTrue(testee.isLying());

		// Test the case in which we're standing and the time has passed
		allBodyParts = new HashMap<>();
		allBodyParts.put("a", new Vector3D(0, 0, 0.4));
		allBodyParts.put("b", new Vector3D(0, 0, 0));
		allBodyParts.put("c", new Vector3D(0, 0, 0));
		testee.setBodyParts(allBodyParts);
		testee.updateLying(IMagmaConstants.TIME_DELAY_LYING);
		assertFalse(testee.isLying());
	}

	/*
	 * anotherTest for {@Link Player#updateLying(float time)}
	 */
	@Test
	public void testIsLying()
	{
		// Test the case in which we're standing and no time has passed
		Map<String, Vector3D> allBodyParts = new HashMap<>();
		allBodyParts.put("a", new Vector3D(0, 0, 0.4));
		allBodyParts.put("b", new Vector3D(0, 0, 0));
		allBodyParts.put("c", new Vector3D(0, 0, 0));
		testee.setBodyParts(allBodyParts);
		testee.updateLying(0);
		assertFalse(testee.isLying());

		// Test the case in which we're standing and little time has passed
		allBodyParts = new HashMap<>();
		allBodyParts.put("a", new Vector3D(0, 0, 0.0));
		allBodyParts.put("b", new Vector3D(0, 0, 0));
		allBodyParts.put("c", new Vector3D(0, 0, 0));
		testee.setBodyParts(allBodyParts);
		testee.updateLying(0.2f);
		assertTrue(testee.isLying());
	}
}
