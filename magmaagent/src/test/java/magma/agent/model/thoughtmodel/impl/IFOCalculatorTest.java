/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.impl.WorldModelBaseTest;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for the {@link IFOCalculator} class
 *
 * @author Klaus Dorer
 */
public class IFOCalculatorTest extends WorldModelBaseTest
{
	private IFOCalculator testee;

	private List<IPlayer> sourceList;

	@Override
	@BeforeEach
	public void setUp()
	{
		super.setUp();
		sourceList = new ArrayList<>();
		sourceList.add(playerMock1);
		sourceList.add(playerMock2);
		sourceList.add(playerMock3);
		sourceList.add(playerMock4);

		testee = new IFOCalculator(worldModelMock);
	}

	/**
	 * Test for {@link IFOCalculator#getTeammatesAtBall(List)}
	 */
	@Test
	public void getTeammatesAtBall()
	{
		Vector3D ballPosition = new Vector3D(2, 0, 0);
		when(ballMock.getPosition()).thenReturn(ballPosition);
		when(playerMock2.getDistanceToXY(ballPosition)).thenReturn(1.0);
		when(playerMock2.isLying()).thenReturn(false);
		when(playerMock4.getDistanceToXY(ballPosition)).thenReturn(2.0);
		when(playerMock4.isLying()).thenReturn(false);

		List<IPlayer> result = testee.getTeammatesAtBall(sourceList);
		assertEquals(2, result.size());
		assertEquals(playerMock2, result.get(0));
		assertEquals(playerMock4, result.get(1));
	}

	/**
	 * Test for {@link IFOCalculator#getTeammatesAtBall(List)}
	 */
	@Test
	public void getTeammatesAtBallLying()
	{
		Vector3D ballPosition = new Vector3D(2, 0, 0);
		when(ballMock.getPosition()).thenReturn(ballPosition);
		when(playerMock2.getDistanceToXY(ballPosition)).thenReturn(1.0);
		when(playerMock2.isLying()).thenReturn(true);
		when(playerMock4.getDistanceToXY(ballPosition)).thenReturn(2.0);
		when(playerMock4.isLying()).thenReturn(false);

		List<IPlayer> result = testee.getTeammatesAtBall(sourceList);
		assertEquals(2, result.size());
		assertEquals(playerMock4, result.get(0));
		assertEquals(playerMock2, result.get(1));
	}
}
