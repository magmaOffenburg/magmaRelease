/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import hso.autonomy.agent.model.worldmodel.impl.MovableObject;
import hso.autonomy.util.misc.FuzzyCompare;
import magma.agent.IMagmaConstants;
import magma.agent.model.worldmodel.IBall;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Srinivas
 *
 */
public class MovableObjectTest
{
	private MovableObject testee;

	@BeforeEach
	public void setUp()
	{
		testee = new Player(IMagmaConstants.UNKNOWN_PLAYER_NUMBER, IMagmaConstants.DEFAULT_TEAMNAME, true, 0.02f);
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.agent.model.worldmodel.impl.MovableObject#getFuturePositions(int)}.
	 */
	@Test
	public void testGetFuturePositionsPlayer()
	{
		testee.updateFromVision(null, null, new Vector3D(0.0, 0.0, 0.0), 1.0f);
		Vector3D[] expected = new Vector3D[3];
		expected[0] = new Vector3D(0.0, 0.0, 0.0);
		expected[1] = new Vector3D(0.0, 0.0, 0.0);
		expected[2] = new Vector3D(0.0, 0.0, 0.0);

		testVectorArray(expected, testee.getFuturePositions(3));

		testee.updateFromVision(null, null, new Vector3D(1.0, 0.0, 0.0), 2.0f);

		expected[0] = new Vector3D(1.02, 0.0, 0.0);
		expected[1] = new Vector3D(1.04, 0.0, 0.0);
		expected[2] = new Vector3D(1.06, 0.0, 0.0);

		testVectorArray(expected, testee.getFuturePositions(3));
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.agent.model.worldmodel.impl.MovableObject#getSpeed()}.
	 */
	@Test
	public void testGetSpeedForPlayers()
	{
		/** Tests for Players */

		// first-time perception, no prevPosition
		testee.updateFromVision(null, null, new Vector3D(0.0, 0.0, 0.0), 1.0f);
		testVector(new Vector3D(0.0, 0.0, 0.0), testee.getSpeed());

		// speed < 2
		testee.updateFromVision(null, null, new Vector3D(4.0, 0.0, 0.0), 2.0f);
		testVector(new Vector3D(0.08, 0.0, 0.0), testee.getSpeed());

		// speed > 2
		testee.updateFromVision(null, null, new Vector3D(7.0, 0.0, 0.0), 3.0f);
		testVector(new Vector3D(0.06, 0.0, 0.0), testee.getSpeed());

		// speed < 2, time gap > 1
		testee.updateFromVision(null, null, new Vector3D(8.0, 0.0, 0.0), 5.0f);
		testVector(new Vector3D(0.01, 0.0, 0.0), testee.getSpeed());
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.agent.model.worldmodel.impl.MovableObject#getSpeed()}.
	 */
	@Test
	@Disabled
	public void testGetSpeedForBall()
	{
		/** Tests for Ball */
		testee = new Ball(0.042f, .94f, Vector3D.ZERO, IBall.COLLISION_DISTANCE, 0.02f);

		// first-time perception, initial position 0,0
		testee.updateFromVision(null, null, new Vector3D(2.0, 0.0, 0.0), 1.0f);
		testVector(new Vector3D(2.0, 0.0, 0.0), testee.getSpeed());

		// speed < 6
		testee.updateFromVision(null, null, new Vector3D(3.0, 2.0, 0.0), 2.0f);
		testVector(new Vector3D(1.0, 2.0, 0.0), testee.getSpeed());

		// speed > 6
		testee.updateFromVision(null, null, new Vector3D(6.0, -6.0, 0.0), 3.0f);
		testVector(new Vector3D(1.0, 2.0, 0.0), testee.getSpeed());

		// speed < 6, time gap > 1
		testee.updateFromVision(null, null, new Vector3D(4.0, -4.0, 0.0), 5.0f);
		testVector(new Vector3D(-1.0, 1.0, 0.0), testee.getSpeed());
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.agent.model.worldmodel.impl.MovableObject#getFuturePositions(int)}.
	 */
	@Test
	@Disabled
	public void testGetFuturePositionsBall()
	{
		testee = new Ball(0.042f, .94f, Vector3D.ZERO, IBall.COLLISION_DISTANCE, 0.02f);

		testee.updateFromVision(null, null, new Vector3D(2.0, 1.0, 0.0), 1.0f);
		Vector3D[] expected = new Vector3D[3];
		expected[0] = new Vector3D(3.88, 1.94, 0.0);
		expected[1] = new Vector3D(5.6472, 2.8236, 0.0);
		expected[2] = new Vector3D(7.308368, 3.654184, 0.0);

		testVectorArray(expected, testee.getFuturePositions(3));

		testee.updateFromVision(null, null, new Vector3D(0.0, 1.0, 0.0), 2.0f);

		expected[0] = new Vector3D(-1.88, 1.0, 0.0);
		expected[1] = new Vector3D(-3.647199, 1.0, 0.0);
		expected[2] = new Vector3D(-5.308368, 1.0, 0.0);

		testVectorArray(expected, testee.getFuturePositions(3));
	}

	private void testVector(Vector3D expected, Vector3D was)
	{
		assertTrue(FuzzyCompare.eq(expected, was, 0.0001f), "Expected: " + expected + " but was: " + was);
	}

	private void testVectorArray(Vector3D[] expected, Vector3D[] was)
	{
		for (int i = 0; i < expected.length; i++) {
			testVector(expected[i], was[i]);
		}
	}
}
