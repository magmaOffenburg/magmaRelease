/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.walk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.basic.None;
import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import java.util.ArrayList;
import java.util.List;
import magma.agent.decision.behavior.base.WalkEstimator;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.impl.WorldModelBaseTest;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WalkToPositionTest extends WorldModelBaseTest
{
	private WalkToPosition testee;

	private Pose2D targetPosOr;

	private List<IVisibleObject> obstacles;

	private Vector3D targetPos;

	private ArrayList<IPlayer> teammatesAtBall;

	@Override
	@BeforeEach
	public void setUp()
	{
		super.setUp();
		IRoboCupThoughtModel thoughtModelMock = mock(IRoboCupThoughtModel.class);
		targetPos = new Vector3D(4, 0, 0);
		targetPosOr = new Pose2D(targetPos);
		BehaviorMap behaviors = new BehaviorMap();
		when(thoughtModelMock.getAgentModel()).thenReturn(null);
		when(thoughtModelMock.getWorldModel()).thenReturn(worldModelMock);
		obstacles = new ArrayList<>();
		when(playerMock1.getName()).thenReturn("player1");
		when(thoughtModelMock.getObstacles()).thenReturn(obstacles);
		teammatesAtBall = new ArrayList<>();
		when(thoughtModelMock.getTeammatesAtBall()).thenReturn(teammatesAtBall);

		behaviors.put(new None(thoughtModelMock));
		float[] speeds = {0.8f, 0.7f, 0.5f, 0.5f, 0.65f, 0.65f, 0.65f, 0.65f, 180};
		testee = new WalkToPosition(thoughtModelMock, behaviors, new WalkEstimator(speeds));
	}

	@Test
	public void testAvoidCollisionBehind()
	{
		Vector3D playerPos = new Vector3D(2, 0, 0);
		Vector3D mePos = new Vector3D(0, 0, 0);
		Vector3D localTargetPos = new Vector3D(-4, 0, 0);
		Pose2D localTargetPosOr = new Pose2D(localTargetPos);

		when(playerMock1.getPosition()).thenReturn(playerPos);
		when(playerMock1.getCollisionDistance()).thenReturn(0.4);
		obstacles.add(playerMock1);
		// distance to target position
		when(thisPlayerMock.getDistanceToXY((Vector3D) any())).thenReturn(4.0);
		// distance to playerMock1
		when(thisPlayerMock.getDistanceToXY((Vector3D) any())).thenReturn(2.0);
		when(thisPlayerMock.getPosition()).thenReturn(mePos);

		IPose2D result = testee.avoidCollision(localTargetPosOr);

		assertSame(localTargetPosOr, result);
	}

	@Test
	public void testAvoidCollisionTooFar()
	{
		Vector3D playerPos = new Vector3D(-2, 0, 0);
		when(playerMock1.getPosition()).thenReturn(playerPos);
		obstacles.add(playerMock1);
		when(thisPlayerMock.getDistanceToXY(targetPos)).thenReturn(4.0);
		when(thisPlayerMock.getDistanceToXY(playerPos)).thenReturn(6.0);
		when(thisPlayerMock.positionIsBehind(playerPos)).thenReturn(false);
		IPose2D result = testee.avoidCollision(targetPosOr);
		assertSame(targetPosOr, result);
	}

	@Test
	public void testAvoidCollisionTooFarFromLine()
	{
		Vector3D playerPos = new Vector3D(2, 0.5, 0);
		Vector3D mePos = new Vector3D(0, 2.0, 0);
		when(playerMock1.getPosition()).thenReturn(playerPos);
		when(playerMock1.getCollisionDistance()).thenReturn(0.4);
		obstacles.add(playerMock1);
		when(thisPlayerMock.getDistanceToXY((Vector3D) any())).thenReturn(4.0);
		when(thisPlayerMock.getDistanceToXY((Vector3D) any())).thenReturn(3.0);
		when(thisPlayerMock.positionIsBehind(playerPos)).thenReturn(false);
		when(thisPlayerMock.getPosition()).thenReturn(mePos);

		IPose2D result = testee.avoidCollision(targetPosOr);

		assertSame(targetPosOr, result);
	}

	@Test
	public void testAvoidCollisionOtherSideOfOrigin()
	{
		Vector3D playerPos = new Vector3D(2, -1.2, 0);
		Vector3D mePos = new Vector3D(0, -1.0, 0);
		Vector3D localTargetPos = new Vector3D(4, -1.0, 0);
		Pose2D localTargetPosOr = new Pose2D(localTargetPos);

		when(playerMock1.getPosition()).thenReturn(playerPos);
		when(playerMock1.getCollisionDistance()).thenReturn(0.4);
		obstacles.add(playerMock1);
		when(thisPlayerMock.getDistanceToXY((Vector2D) any())).thenReturn(4.0);
		when(thisPlayerMock.getDistanceToXY((Vector3D) any())).thenReturn(3.0);
		when(thisPlayerMock.getDistanceToXY((Vector3D) any())).thenReturn(1.0);
		when(thisPlayerMock.getDistanceToXY((Vector3D) any())).thenReturn(1.1);
		when(thisPlayerMock.positionIsBehind(playerPos)).thenReturn(false);
		when(thisPlayerMock.getPosition()).thenReturn(mePos);

		IPose2D result = testee.avoidCollision(localTargetPosOr);

		assertEquals(-5.7106, result.getAngle().degrees(), 0.0001);
		assertEquals(2.0, result.getX(), 0.0001);
		assertEquals(-0.8, result.getY(), 0.0001);
	}

	@Test
	public void testAvoidCollisionNeedIntermediate1()
	{
		Vector3D playerPos = new Vector3D(2, 0.1, 0);
		Vector3D mePos = new Vector3D(0, 0, 0);
		when(playerMock1.getPosition()).thenReturn(playerPos);
		when(playerMock1.getCollisionDistance()).thenReturn(0.4);
		obstacles.add(playerMock1);
		when(thisPlayerMock.getDistanceToXY((Vector2D) any())).thenReturn(4.0);
		when(thisPlayerMock.getDistanceToXY((Vector3D) any())).thenReturn(3.0);
		when(thisPlayerMock.getDistanceToXY((Vector3D) any())).thenReturn(1.2);
		when(thisPlayerMock.getDistanceToXY((Vector3D) any())).thenReturn(1.1);
		when(thisPlayerMock.positionIsBehind(playerPos)).thenReturn(false);
		when(thisPlayerMock.getPosition()).thenReturn(mePos);

		IPose2D result = testee.avoidCollision(targetPosOr);

		assertEquals(8.5307, result.getAngle().degrees(), 0.0001);
		assertEquals(2.0, result.getX(), 0.0001);
		assertEquals(-0.3, result.getY(), 0.0001);
	}

	@Test
	public void testAvoidCollisionNeedIntermediate2()
	{
		Vector3D playerPos = new Vector3D(2, -0.1, 0);
		Vector3D mePos = new Vector3D(0, 0, 0);
		when(playerMock1.getPosition()).thenReturn(playerPos);
		when(playerMock1.getCollisionDistance()).thenReturn(0.4);
		obstacles.add(playerMock1);
		when(thisPlayerMock.getDistanceToXY((Vector2D) any())).thenReturn(4.0);
		when(thisPlayerMock.getDistanceToXY((Vector3D) any())).thenReturn(3.0);
		when(thisPlayerMock.getDistanceToXY((Vector3D) any())).thenReturn(1.0);
		when(thisPlayerMock.getDistanceToXY((Vector3D) any())).thenReturn(1.1);
		when(thisPlayerMock.positionIsBehind(playerPos)).thenReturn(false);
		when(thisPlayerMock.getPosition()).thenReturn(mePos);

		IPose2D result = testee.avoidCollision(targetPosOr);

		assertEquals(-8.5307, result.getAngle().degrees(), 0.0001);
		assertEquals(2.0, result.getX(), 0.0001);
		assertEquals(0.3, result.getY(), 0.0001);
	}

	/*
	 * Test for {@Link WalkToPosition#avoidTeammate(Vector3D newPos)}
	 */
	@Test
	public void testAvoidTeammateFarFromNewPos()
	{
		Vector3D playerPos = new Vector3D(2, -0.1, 0);
		Vector3D newPos = new Vector3D(8, 0, 0);
		Vector3D mePos = new Vector3D(0, 0, 0);
		when(playerMock1.getPosition()).thenReturn(playerPos);
		teammatesAtBall.add(playerMock1);
		when(playerMock1.getDistanceToXY(newPos)).thenReturn(playerPos.distance(newPos));
		when(thisPlayerMock.getPosition()).thenReturn(mePos);

		Vector3D result = testee.avoidTeammate(newPos);

		assertEquals(8, result.getX(), 0.0001);
		assertEquals(0, result.getY(), 0.0001);
		assertEquals(0, result.getZ(), 0.0001);
	}

	/*
	 * Another Test for {@Link WalkToPosition#avoidTeammate(Vector3D newPos)}
	 * Tests what happens one we are near enough already
	 */
	@Test
	public void testAvoidTeammateCloseToNewPos()
	{
		Vector3D playerPos = new Vector3D(2, -0.1, 0);
		Vector3D newPos = new Vector3D(0.9, 0, 0);
		Vector3D mePos = new Vector3D(0, 0, 0);
		when(playerMock1.getPosition()).thenReturn(playerPos);
		teammatesAtBall.add(playerMock1);
		when(playerMock1.getDistanceToXY(newPos)).thenReturn(0.2);
		when(thisPlayerMock.getPosition()).thenReturn(mePos);

		Vector3D result = testee.avoidTeammate(newPos);

		assertEquals(0, result.getX(), 0.0001);
		assertEquals(0, result.getY(), 0.0001);
		assertEquals(0, result.getZ(), 0.0001);
	}
}
