/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hso.autonomy.agent.model.worldmodel.InformationSource;
import hso.autonomy.agent.model.worldmodel.impl.WorldModel;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose3D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import magma.agent.communication.perception.IPlayerPos;
import magma.agent.communication.perception.IRoboCupPerception;
import magma.agent.communication.perception.impl.PlayerPos;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.worldmeta.impl.RCServerMetaModelV63;
import magma.agent.model.worldmodel.IPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link WorldModel} class
 */
public class WorldModelTest
{
	private RoboCupWorldModel testee;

	private IRoboCupPerception perceptionMock;

	private IRoboCupAgentModel agentModelMock;

	@BeforeEach
	public void setUp()
	{
		perceptionMock = mock(IRoboCupPerception.class);
		agentModelMock = mock(IRoboCupAgentModel.class);

		when(agentModelMock.getCycleTime()).thenReturn(0.02f);
		when(agentModelMock.getTorsoZUpright()).thenReturn(0.4f);
		testee = new RoboCupWorldModel(agentModelMock, null, RCServerMetaModelV63.INSTANCE, "team", 0);
	}

	/**
	 * Test for {@link RoboCupWorldModel#updatePlayers(IRoboCupPerception)}
	 */
	@Test
	public void testUpdatePlayers()
	{
		testee.thisPlayer = new ThisPlayer("self", 0, 0.02f, 0.4f);
		testee.thisPlayer.setPosition(new Vector3D(0.0, 0.0, 0.0));

		// Setup perception
		HashMap<String, Vector3D> bodyPartMap = new HashMap<>();
		IPlayerPos playerVision =
				new PlayerPos("Player", new Vector3D(2.0, 3.0, 0.0), 1, "testTeam", bodyPartMap, true, "P");
		List<IPlayerPos> playersVision = new ArrayList<>();
		playersVision.add(playerVision);
		when(perceptionMock.containsVision()).thenReturn(true);
		when(perceptionMock.getVisiblePlayers()).thenReturn(playersVision);

		testee.updatePlayers(perceptionMock);

		List<IPlayer> players = testee.getVisiblePlayers();
		assertEquals(1, players.size());
		IPlayer player = players.get(0);
		assertEquals(1, player.getID());
		assertEquals("testTeam", player.getTeamname());
		assertEquals(InformationSource.VISION, player.getInformationSource());

		// test clear of list if called twice
		testee.updatePlayers(perceptionMock);
		assertEquals(1, testee.getVisiblePlayers().size());
	}

	@Test
	public void testEstimateDepthStraight() throws Exception
	{
		// the ball is 2 m in front and has no height
		// camera is in 1m height, not tilted, we see the ball down
		Pose3D thisPlayerPose = new Pose3D(Vector3D.ZERO, Rotation.IDENTITY);
		Pose3D cameraPose = new Pose3D(new Vector3D(0, 0, 1), Rotation.IDENTITY);
		Vector3D position = new Vector3D(1, -0.25, -0.5);
		Vector3D result = testee.estimateDepth(thisPlayerPose, cameraPose, position, 0);
		assertEquals(2, result.getX(), 0.001);
		assertEquals(-0.5, result.getY(), 0.001);
		assertEquals(-1, result.getZ(), 0.001);
	}

	@Test
	public void testEstimateDepthLookDown() throws Exception
	{
		// the ball is 2 m distance to camera and has no height
		// camera is in 1m height, tilted 30 deg down, we see the ball in origin
		// of camera
		Pose3D thisPlayerPose = new Pose3D(Vector3D.ZERO, Rotation.IDENTITY);
		Pose3D cameraPose = new Pose3D(new Vector3D(0, 0, 1),
				new Rotation(Vector3D.PLUS_I, Angle.deg(-30).radians(), RotationConvention.VECTOR_OPERATOR));
		Vector3D position = new Vector3D(1, 0, 0);
		Vector3D result = testee.estimateDepth(thisPlayerPose, cameraPose, position, 0);
		assertEquals(2, result.getX(), 0.001);
		assertEquals(0, result.getY(), 0.001);
		assertEquals(0, result.getZ(), 0.001);
	}

	// 0,0500944302 -0,2473750635 1
	@Test
	public void testEstimateDepthLookDownRealData() throws Exception
	{
		// data from a bag file, expected values only roughly checked
		Pose3D thisPlayerPose = new Pose3D(Vector3D.ZERO, Rotation.IDENTITY);
		Pose3D cameraPose = new Pose3D(new Vector3D(0, 0, 1),
				new Rotation(Vector3D.PLUS_I, Angle.deg(-30).radians(), RotationConvention.VECTOR_OPERATOR));
		Vector3D position = new Vector3D(1, -0.05, 0.25);
		Vector3D result = testee.estimateDepth(thisPlayerPose, cameraPose, position, 0);
		assertEquals(3.5274, result.getX(), 0.001);
		assertEquals(-0.17637, result.getY(), 0.001);
		assertEquals(0.8818, result.getZ(), 0.001);
	}

	@Test
	public void testGoalLineIntersection()
	{
		float gx = testee.fieldHalfLength();
		Vector2D result = testee.goalLineIntersection(new Vector2D(0, 0), new Vector2D(1, 0), 0.1);
		assertEquals(gx, result.getX(), 0.001);
		assertEquals(0, result.getY(), 0.001);

		result = testee.goalLineIntersection(new Vector2D(gx - 1, 1), new Vector2D(gx, -1), 0.0);
		assertEquals(gx, result.getX(), 0.001);
		assertEquals(-1, result.getY(), 0.001);

		result = testee.goalLineIntersection(new Vector2D(gx, -1), new Vector2D(gx - 1, 1), 0.0);
		assertEquals(gx, result.getX(), 0.001);
		assertEquals(-1, result.getY(), 0.001);

		assertNull(testee.goalLineIntersection(new Vector2D(gx - 1, 1), new Vector2D(gx, -1), 0.1));
	}
}
