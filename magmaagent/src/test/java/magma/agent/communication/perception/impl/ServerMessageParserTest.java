/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.perception.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hso.autonomy.agent.communication.perception.IPerceptor;
import hso.autonomy.agent.communication.perception.ITimerPerceptor;
import hso.autonomy.agent.communication.perception.IVisibleObjectPerceptor;
import hso.autonomy.agent.communication.perception.impl.ForceResistancePerceptor;
import hso.autonomy.agent.communication.perception.impl.GyroPerceptor;
import hso.autonomy.agent.communication.perception.impl.HingeJointPerceptor;
import hso.autonomy.agent.communication.perception.impl.TimePerceptor;
import hso.autonomy.agent.communication.perception.impl.TouchPerceptor;
import java.util.Map;
import magma.agent.communication.perception.IGameStatePerceptor;
import magma.agent.communication.perception.IHearPerceptor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Simon Raffeiner
 */
public class ServerMessageParserTest
{
	private ServerMessageParser testee;

	@BeforeEach
	public void setUp()
	{
		testee = new ServerMessageParser();
	}

	private Map<String, IPerceptor> parseMessage(String message) throws Exception
	{
		return testee.parseMessage(message.getBytes());
	}

	/**
	 * Test for {@link ServerMessageParser#parseMessage(byte[])}
	 */
	@Test
	public void testGyroPerceptor() throws Exception
	{
		String msg = "(GYR (n torso) (rt 0.01 0.07 0.46))";

		Map<String, IPerceptor> list = parseMessage(msg);

		assertEquals(list.size(), 1);

		IPerceptor perceptor = list.values().iterator().next();
		assertTrue(perceptor instanceof GyroPerceptor);

		GyroPerceptor gyro = (GyroPerceptor) perceptor;

		assertEquals("torsoGyro", gyro.getName());

		Vector3D rotation = gyro.getGyro();
		assertEquals(rotation.getX(), 0.01f, 0.0001);
		assertEquals(rotation.getY(), 0.07f, 0.0001);
		assertEquals(rotation.getZ(), 0.46f, 0.0001);
	}

	/**
	 * Test for {@link ServerMessageParser#parseMessage(byte[])}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testHingeJointPerceptor() throws Exception
	{
		String msg = "(HJ (n laj3) (ax -1.02))";

		Map<String, IPerceptor> list = parseMessage(msg);

		assertEquals(list.size(), 1);

		IPerceptor perceptor = list.values().iterator().next();
		assertTrue(perceptor instanceof HingeJointPerceptor);

		HingeJointPerceptor hinge = (HingeJointPerceptor) perceptor;

		assertEquals(hinge.getName(), "laj3");
		assertEquals(hinge.getAxis(), -1.02f, 0.0001);
	}

	/**
	 * Test for {@link ServerMessageParser#parseMessage(byte[])}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testForceResistancePerceptor() throws Exception
	{
		String msg = "(FRP (n lf) (c -0.14 0.08 -0.05) (f 1.12 -0.26 13.07))";

		Map<String, IPerceptor> list = parseMessage(msg);

		assertEquals(list.size(), 1);

		IPerceptor perceptor = list.values().iterator().next();
		assertTrue(perceptor instanceof ForceResistancePerceptor);

		ForceResistancePerceptor frp = (ForceResistancePerceptor) perceptor;

		assertEquals(frp.getName(), "lf");

		Vector3D origin = frp.getForceOrigin();
		Vector3D force = frp.getForce();

		assertEquals(origin.getX(), -0.14f, 0.0001);
		assertEquals(origin.getY(), 0.08f, 0.0001);
		assertEquals(origin.getZ(), -0.05f, 0.0001);
		assertEquals(force.getX(), 1.12f, 0.0001);
		assertEquals(force.getY(), -0.26f, 0.0001);
		assertEquals(force.getZ(), 13.07f, 0.0001);
	}

	/**
	 * Test for {@link ServerMessageParser#parseMessage(byte[])}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testVisionPerceptor() throws Exception
	{
		String msg = "(See (F1L (pol 19.11 111.69 -9.57)) "
					 + "(G1R (pol 9.88 139.29 -21.07)) (B (pol 18.34 4.66 -9.90)) "
					 + "(P (team RoboLog) (id 1) (pol 37.50 16.15 -0.00))"
					 + "(P (team RoboLog) (id 2) (pol 37.50 16.15 -0.00)))";

		Map<String, IPerceptor> map = parseMessage(msg);

		assertEquals(map.size(), 5);

		// Check Flags
		IVisibleObjectPerceptor flag1 = (IVisibleObjectPerceptor) map.get("F1L");
		IVisibleObjectPerceptor goal2 = (IVisibleObjectPerceptor) map.get("G1R");
		IVisibleObjectPerceptor ball = (IVisibleObjectPerceptor) map.get("B");
		PlayerPos player1 = (PlayerPos) map.get("PRoboLog1");

		assertEquals(19.11, flag1.getDistance(), 0.0001, "");
		assertEquals(111.69, flag1.getHorizontalAngleDeg(), 0.0001, "");
		assertEquals(-9.57, flag1.getLatitudeAngleDeg(), 0.0001, "");
		assertEquals(9.88, goal2.getDistance(), 0.0001, "");
		assertEquals(139.29, goal2.getHorizontalAngleDeg(), 0.0001, "");
		assertEquals(-21.07, goal2.getLatitudeAngleDeg(), 0.0001, "");
		assertEquals(18.34, ball.getDistance(), 0.0001, "");
		assertEquals(4.66, ball.getHorizontalAngleDeg(), 0.0001, "");
		assertEquals(-9.9, ball.getLatitudeAngleDeg(), 0.0001, "");
		assertEquals(37.5, player1.getDistance(), 0.0001, "");
		assertEquals(16.15, player1.getHorizontalAngleDeg(), 0.0001, "");
		assertEquals(0, player1.getLatitudeAngleDeg(), 0.0001, "");
		assertEquals("RoboLog", player1.getTeamname(), "");
		assertEquals(1, player1.getId(), "");

		PlayerPos player2 = (PlayerPos) map.get("PRoboLog2");
		;
		assertEquals(2, player2.getId(), "");
		assertEquals(37.5, player2.getDistance(), 0.001);
	}

	/**
	 * Test for {@link ServerMessageParser#parseMessage(byte[])}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testVisionPerceptorMinimumMessage() throws Exception
	{
		// Minimum message
		parseMessage("(See)");
	}

	/**
	 * Test for {@link ServerMessageParser#parseMessage(byte[])}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testVisionPerceptorWithOnePlayer() throws Exception
	{
		// Message containing one player
		Map<String, IPerceptor> map = parseMessage("(See (P (team RoboLog) (pol 37.50 16.15 -0.00)))");
		PlayerPos player2 = (PlayerPos) map.values().iterator().next();
		assertEquals("RoboLog", player2.getTeamname());
		assertEquals(37.5, player2.getDistance(), 0.001);
		assertEquals(16.15, player2.getHorizontalAngleDeg(), 0.001);
	}

	/**
	 * Test for {@link ServerMessageParser#parseMessage(byte[])}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testVisionPerceptorWithLimbInformation() throws Exception
	{
		// Player message with lower arm information
		Map<String, IPerceptor> map = parseMessage("(See (P (team RoboLog) "
												   + "(llowerarm (pol 37.50 16.15 -0.00))))");

		PlayerPos player2 = (PlayerPos) map.values().iterator().next();
		assertEquals("RoboLog", player2.getTeamname());
		assertEquals(37.5, player2.getDistance(), 0.001);
		assertEquals(16.15, player2.getHorizontalAngleDeg(), 0.001);
	}

	/**
	 * Test for {@link ServerMessageParser#parseMessage(byte[])}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testVisionPerceptorWithRealInformation() throws Exception
	{
		// message from server logfile
		Map<String, IPerceptor> map =
				parseMessage("(See (F2R (pol 7.34 50.15 -4.31)) "
							 + "(P (team magmaOffenburg) (id 2) (head (pol 2.36 -41.55 -0.00)) "
							 + "(rlowerarm (pol 2.38 -37.08 -1.47)) (llowerarm (pol 2.20 -39.95 -1.89)) "
							 + "(rfoot (pol 2.45 -40.18 -12.30)) (lfoot (pol 2.36 -41.55 -12.58))) "
							 + "(P (team magmaOffenburg) (id 3) (rlowerarm (pol 0.19 48.14 -22.49))))");

		PlayerPos player2 = (PlayerPos) map.get("PmagmaOffenburg2");

		assertEquals(1.7661, player2.getBodyPartPosition("head").getX(), 0.001);
		assertEquals(-1.5653, player2.getBodyPartPosition("head").getY(), 0.001);
		assertEquals(-0.00, player2.getBodyPartPosition("head").getZ(), 0.001);

		assertEquals("magmaOffenburg", player2.getTeamname());
		assertEquals(2.3377, player2.getDistance(), 0.001);
		assertEquals(-40.052, player2.getHorizontalAngleDeg(), 0.001);

		PlayerPos player3 = (PlayerPos) map.get("PmagmaOffenburg3");
		assertEquals("magmaOffenburg", player3.getTeamname());
		assertEquals(0.19, player3.getDistance(), 0.001);
		assertEquals(48.14, player3.getHorizontalAngleDeg(), 0.001);
	}

	/**
	 * Test for {@link ServerMessageParser#parseMessage(byte[])}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testAgentStatePerceptor() throws Exception
	{
		String msg = "(AgentState (temp 48) (battery 75))";

		Map<String, IPerceptor> list = parseMessage(msg);

		assertEquals(list.size(), 1);

		IPerceptor perceptor = list.values().iterator().next();
		assertTrue(perceptor instanceof AgentStatePerceptor);

		AgentStatePerceptor agentState = (AgentStatePerceptor) perceptor;

		assertEquals(agentState.getTemperature(), 48);
		assertEquals(agentState.getBattery(), 75);
	}

	/**
	 * Test for {@link ServerMessageParser#parseMessage(byte[])}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testGameStatePerceptor() throws Exception
	{
		String msg = "(GS (t 1.12) (pm BeforeKickOff))";

		Map<String, IPerceptor> list = parseMessage(msg);

		assertEquals(list.size(), 1);

		IPerceptor perceptor = list.values().iterator().next();
		assertTrue(perceptor instanceof GameStatePerceptor);

		IGameStatePerceptor gamestate = (IGameStatePerceptor) perceptor;

		assertEquals(gamestate.getTime(), 1.12f, 0.0001);
		assertEquals("BeforeKickOff", gamestate.getPlaymode());
	}

	/**
	 * Test for {@link ServerMessageParser#parseMessage(byte[])}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testTouchPerceptor() throws Exception
	{
		String msg = "(TCH n bumper val 1)";

		Map<String, IPerceptor> list = parseMessage(msg);

		assertEquals(list.size(), 1);

		IPerceptor perceptor = list.values().iterator().next();
		assertTrue(perceptor instanceof TouchPerceptor);

		TouchPerceptor touch = (TouchPerceptor) perceptor;

		assertEquals(touch.getName(), "bumper");
		assertTrue(touch.getState());
	}

	/**
	 * Test for {@link ServerMessageParser#parseMessage(byte[])}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testHearPerceptor() throws Exception
	{
		String msg = "(hear magma 12.3 self \"Hello RoboCup World\")";

		Map<String, IPerceptor> list = parseMessage(msg);

		assertEquals(list.size(), 1);

		IPerceptor perceptor = list.values().iterator().next();
		assertTrue(perceptor instanceof HearPerceptor);

		IHearPerceptor hear = (IHearPerceptor) perceptor;

		assertEquals(hear.getTime(), 12.3f, 0.0001f);
		assertEquals("magma", hear.getTeam());
		assertEquals("self", hear.getTarget());
		assertEquals("\"Hello RoboCup World\"", hear.getMessage());
	}

	/**
	 * Test for {@link ServerMessageParser#parseMessage(byte[])}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testTimePerceptor() throws Exception
	{
		String msg = "(time (now 2.18))";

		Map<String, IPerceptor> list = parseMessage(msg);

		assertEquals(list.size(), 1);

		IPerceptor perceptor = list.values().iterator().next();
		assertTrue(perceptor instanceof TimePerceptor);

		ITimerPerceptor time = (ITimerPerceptor) perceptor;

		assertEquals(time.getTime(), 2.18f, 0.0001f);
	}

	/**
	 * Test for {@link ServerMessageParser#parseMessage(byte[])}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testGoalsScored() throws Exception
	{
		String msg = "(GS (t 1.0)(pm goal_r))";

		Map<String, IPerceptor> list = parseMessage(msg);

		assertEquals(list.size(), 1);

		IPerceptor perceptor = list.values().iterator().next();
		assertTrue(perceptor instanceof GameStatePerceptor);

		// previous playmode will be goal_r. number of goals will not be updated.
		/*
		 * msg = "((GS)(t)(2.0)(pm)(goal_r))"; p.update(msg);
		 * testee.updateGoalsScored(p); assertEquals(1,
		 * testee.getGoalsWeScored());
		 *
		 * msg = "((GS)(t)(2.0)(pm)(goal_l))"; p.update(msg);
		 * testee.updateGoalsScored(p); assertEquals(1,
		 * testee.getGoalsTheyScored());
		 *
		 * msg = "((GS)(t)(2.0)(pm)(playon))"; p.update(msg);
		 * testee.updateGoalsScored(p); assertEquals(1,
		 * testee.getGoalsTheyScored());
		 *
		 * msg = "((GS)(t)(2.0)(pm)(goal_r))"; p.update(msg);
		 * testee.updateGoalsScored(p); assertEquals(2,
		 * testee.getGoalsWeScored());
		 */
	}

	/**
	 * Test for {@link ServerMessageParser#parseMessage(byte[])}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testHeavyRobocupString() throws Exception
	{
		String msg =
				"(time (now 136.99))(GS (t 126.47) (pm PlayOn))(GYR (n torso) (rt 12.09 -0.08 -5.45))(HJ (n hj1) (ax -115.78))(HJ (n hj2) (ax -4.80))(See (G1L (pol 9.20 -48.87 13.29)) (G2L (pol 8.97 -40.23 13.15)) (F2L (pol 9.35 -19.44 7.39)) (P (team magmaOffenburg) (id 2) (rlowerarm (pol 0.25 27.34 -57.54))))(HJ (n raj1) (ax -89.43))(HJ (n raj2) (ax -7.84))(HJ (n raj3) (ax 0.18))(HJ (n raj4) (ax -0.06))(HJ (n laj1) (ax -86.08))(HJ (n laj2) (ax 13.36))(HJ (n laj3) (ax -1.05))(HJ (n laj4) (ax 0.69))(HJ (n rlj1) (ax -0.11))(HJ (n rlj2) (ax 14.36))(HJ (n rlj3) (ax 42.12))(HJ (n rlj4) (ax -69.36))(HJ (n rlj5) (ax 18.74))(FRP (n rf) (c 0.00 -0.08 -0.01) (f -0.19 2.64 36.08))(HJ (n rlj6) (ax -14.37))(HJ (n llj1) (ax -0.13))(HJ (n llj2) (ax 14.37))(HJ (n llj3) (ax 41.93))(HJ (n llj4) (ax -69.18))(HJ (n llj5) (ax 18.55))(FRP (n lf) (c -0.02 -0.03 -0.02) (f -0.13 -2.46 8.90))(HJ (n llj6) (ax -14.37))";

		Map<String, IPerceptor> list = parseMessage(msg);

		assertNotNull(list);
	}
}
