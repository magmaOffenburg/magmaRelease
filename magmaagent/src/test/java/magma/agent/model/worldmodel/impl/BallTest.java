/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import hso.autonomy.agent.model.worldmodel.impl.VisibleObject;
import magma.agent.model.worldmodel.IBall;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Klaus Dorer
 *
 */
public class BallTest
{
	private VisibleObject testee;

	private ThisPlayer thisPlayer;

	@BeforeEach
	public void setUp()
	{
		testee = new Ball(0.042f, .94f, Vector3D.ZERO, IBall.COLLISION_DISTANCE, 0.02f);
		thisPlayer = new ThisPlayer("testTeam", -1, 0.02f, 0.4f);
	}

	/**
	 * Test method for
	 * {@link
	 * hso.autonomy.agent.model.worldmodel.impl.VisibleObject#getDirectionTo(IVisibleObject)}
	 * .
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testGetDirectionTo() throws Exception
	{
		thisPlayer.setPosition(new Vector3D(1.0, 1.0, 0.0));
		testee.setPosition(new Vector3D(2.0, 2.0, 0.0));

		assertEquals(-135.0, testee.getDirectionTo(thisPlayer).degrees(), 0.0001);
		assertEquals(45.0, thisPlayer.getDirectionTo(testee).degrees(), 0.0001);
	}
}
