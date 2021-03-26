/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.perception.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import hso.autonomy.agent.communication.perception.IAccelerometerPerceptor;
import hso.autonomy.agent.communication.perception.IGyroPerceptor;
import hso.autonomy.agent.communication.perception.IHingeJointPerceptor;
import hso.autonomy.agent.communication.perception.ILinePerceptor;
import hso.autonomy.agent.communication.perception.IPerceptorMap;
import hso.autonomy.agent.communication.perception.ITimerPerceptor;
import hso.autonomy.agent.communication.perception.IVisibleObjectPerceptor;
import hso.autonomy.agent.communication.perception.PerceptorConversionException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import magma.agent.communication.perception.IAgentStatePerceptor;
import magma.agent.communication.perception.IGameStatePerceptor;
import magma.agent.communication.perception.IHearPerceptor;
import magma.agent.communication.perception.IPlayerPos;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit Test class for a complete "End-to-End" Test case: SimSpark message in,
 * decode, pass decoded message to Perception, Check Perception.
 *
 * @author joachim
 * @author Simon Raffeiner
 *
 */
public class PerceptionTest
{
	private RoboCupPerception testee;

	private ServerMessageParser parser;

	@BeforeEach
	public void setUp()
	{
		// switching off logging
		// TODO: check if we want to switch off logging in general. Otherwise undo
		// this change in tearDown
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.OFF);

		testee = new RoboCupPerception();
		parser = new ServerMessageParser();
	}

	private void parse(String message) throws Exception
	{
		// TODO: this is not a good unit test, tests should be moved elsewhere
		IPerceptorMap perceptors = parser.parseMessage(message.getBytes());
		testee.updatePerceptors(perceptors);
	}

	/**
	 * Test accelerator perceptor parsing
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testAccelerometerPerceptor() throws Exception
	{
		String msg = "(ACC (n torso) (a -3.98 1.07 0.64))";

		parse(msg);

		IAccelerometerPerceptor accel = testee.getAccelerationPerceptor("torsoAccel");

		assertEquals(accel.getName(), "torsoAccel");
		Vector3D acceleration = accel.getAcceleration();
		assertEquals(-3.98, acceleration.getX(), 0.0000001);
		assertEquals(1.07, acceleration.getY(), 0.0000001);
		assertEquals(0.64, acceleration.getZ(), 0.0000001);
	}

	/**
	 * Test if accelerator perceptor patsing properly handles "NaN" values
	 *
	 * @throws Exception In case of fatal error
	 */
	//	@Test
	public void testAccelerometerPerceptorNANValues() throws Exception
	{
		String msg = "(ACC (n torso) (a -3.98 1.07 0.64))";
		parse(msg);

		// Test: nan in data
		parse("(ACC (n torso) (a nan -0.28 9.81))");

		// test that values did not change compared to last parse
		IAccelerometerPerceptor accel = testee.getAccelerationPerceptor("torsoAccel");
		Vector3D acceleration = accel.getAcceleration();
		assertEquals(-3.98, acceleration.getX(), 0.0000001);
		assertEquals(1.07, acceleration.getY(), 0.0000001);
		assertEquals(0.64, acceleration.getZ(), 0.0000001);
	}

	/**
	 * Test error handling in accelerator perceptor parser
	 */
	@Test
	public void testAccelerometerPerceptorException()
	{
		// Test: Missing data
		try {
			parse("(ACC )");
			fail("Exception expected but none got");
		} catch (Exception e) {
			// Exception expected
		}

		// Test: Missing gyro rates
		try {
			parse("(ACC (k torso) )");
			fail("Exception expected but none got");
		} catch (Exception e) {
			// Exception expected
		}

		// Test: Partial data
		try {
			parse("(ACC (k torso) (a 0.01 0.07))");
			fail("Exception expected but none got");
		} catch (Exception e) {
			// Exception expected
		}

		// Test: Illegal data format
		try {
			parse("(ACC (n torso) (accel 0.01 0.07))");
			fail("Exception expected but none got");
		} catch (Exception e) {
			// Exception expected
		}

		// Test: Too much data
		try {
			parse("(ACC (n torso) (a 0.01 0.07) (id 1))");
			fail("Exception expected but none got");
		} catch (Exception e) {
			// Exception expected
		}
	}

	/**
	 * Test agent state perceptor parsing
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testAgentStatePerceptor() throws Exception
	{
		String msg = "(AgentState (temp 25) (battery 12))";

		parse(msg);

		IAgentStatePerceptor state = testee.getAgentState();

		assertEquals("AgentState", state.getName());
		assertEquals(state.getTemperature(), 25);
		assertEquals(state.getBattery(), 12);
	}

	/**
	 * Test gyro perceptor parsing
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testGyroPerceptor() throws Exception
	{
		String msg = "(GYR (n torso) (rt 0.01 0.07 0.46))";

		parse(msg);

		IGyroPerceptor gyro = testee.getGyroRatePerceptor("torsoGyro");

		assertEquals(gyro.getName(), "torsoGyro");
		Vector3D rotation = gyro.getGyro();
		assertEquals(rotation.getX(), 0.01, 0.0000001);
		assertEquals(rotation.getY(), 0.07, 0.0000001);
		assertEquals(rotation.getZ(), 0.46, 0.0000001);
	}

	/**
	 * Test error handling in gyro perceptor parser
	 */
	@Test
	public void testGyroPerceptorException()
	{
		// Test: Missing data
		try {
			parse("(GYR )");
			fail("Exception expected but none got");
		} catch (Exception e) {
			// Exception expected
		}

		// Test: Missing gyro rates
		try {
			parse("(GYR (k torso) )");
			fail("Exception expected but none got");
		} catch (Exception e) {
			// Exception expected
		}

		// Test: Partial data
		try {
			parse("(GYR (k torso) (rt 0.01 0.07))");
			fail("Exception expected but none got");
		} catch (Exception e) {
			// Exception expected
		}

		// Test: Illegal data format
		try {
			parse("(GYR (n torso) (rotation 0.01 0.07))");
			fail("Exception expected but none got");
		} catch (Exception e) {
			// Exception expected
		}

		// Test: Too much data
		try {
			parse("(GYR (n torso) (rt 0.01 0.07) (id 1))");
			fail("Exception expected but none got");
		} catch (Exception e) {
			// Exception expected
		}
	}

	/**
	 * Test game state perceptor parsing
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testGameStatePerceptor() throws Exception
	{
		// normal call
		parse("(GS (t 7.98) (pm BeforeKickOff))");

		IGameStatePerceptor gs = testee.getGameState();

		assertEquals(7.98, gs.getTime(), 0.0001);
		assertEquals("BeforeKickOff", gs.getPlaymode());

		// string in time
		//		try {
		//			parse("(GS (t bk) (pm 34))");
		//			fail("Expected exception, but was none");
		//		} catch (PerceptorConversionException e) {
		//			 expected
		//		}

		assertEquals(7.98, gs.getTime(), 0.0001);
		assertEquals("BeforeKickOff", gs.getPlaymode());

		// number in playmode
		parse("(GS (t 65) (pm 34))");
		gs = testee.getGameState();

		assertEquals(65, gs.getTime(), 0.0001);
		assertEquals("34", gs.getPlaymode());

		// incomplete message
		try {
			parse("(GS (t 13");
			fail("Expected exception, but was none");
		} catch (PerceptorConversionException e) {
			// expected
		}

		assertEquals(65, gs.getTime(), 0.0001);
		assertEquals("34", gs.getPlaymode());

		// incomplete message
		try {
			parse("(GS ");
			fail("Expected exception, but was none");
		} catch (PerceptorConversionException e) {
			// expected
		}

		assertEquals(65, gs.getTime(), 0.0001);
		assertEquals("34", gs.getPlaymode());
	}

	/**
	 * Test error handling in game state perceptor parser
	 */
	@Test
	public void testGameStatePerceptorException()
	{
		try {
			parse("(GS (bla))");
			fail("Exception expected but none got");
		} catch (Exception e) {
		}

		try {
			parse("(GS (em BeforeKickOff))");
			fail("Exception expected but none got");
		} catch (Exception e) {
		}

		try {
			parse("(GS a)");
			fail("Exception expected but none got");
		} catch (Exception e) {
		}

		try {
			parse("(GS (t 7.98) (pm BlaKick) (bla bla))");
			fail("Exception expected but none got");
		} catch (Exception e) {
		}
	}

	/**
	 * Test game state perceptor parsing for the special case when an "unum"
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testGameStateWithUnum() throws Exception
	{
		// normal call
		parse("(GS (unum 1) (team left) (t 7.98) (pm BeforeKickOff))");
		IGameStatePerceptor gs = testee.getGameState();
		assertEquals(1, gs.getAgentNumber(), 0.0001);
		assertEquals("left", gs.getTeamSide());
		assertEquals(7.98, gs.getTime(), 0.0001);
		assertEquals("BeforeKickOff", gs.getPlaymode());
	}

	/**
	 * Test time perceptor parsing
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testTime() throws Exception
	{
		ITimerPerceptor time;

		// normal call
		parse("(time (now 231.18))");
		time = testee.getTime();

		assertEquals(231.18, time.getTime(), 0.0001);

		// char in float
		//		try {
		//			parse("(time (now 2.h18))");
		//			fail("Expected exception, but was none");
		//		} catch (PerceptorConversionException e) {
		//			 expected
		//		}

		assertEquals(231.18, time.getTime(), 0.0001);

		// incomplete 1
		try {
			parse("(time (now ");
			fail("Expected exception, but was none");
		} catch (PerceptorConversionException e) {
			// expected
		}

		assertEquals(231.18, time.getTime(), 0.0001);

		// incomplete 2
		try {
			parse("(time ");
			fail("Expected exception, but was none");
		} catch (PerceptorConversionException e) {
			// expected
		}

		assertEquals(231.18, time.getTime(), 0.0001);
	}

	/**
	 * Test error handling in time perceptor parser
	 */
	@Test
	public void testTimeException()
	{
		// Broken messages
		try {
			parse("(time)");
			fail("Exception expected but none got");
		} catch (Exception e) {
		}

		try {
			parse("(time 125.9)");
			fail("Exception expected but none got");
		} catch (Exception e) {
		}

		try {
			parse("(time (now 3.a) a)");
			fail("Exception expected but none got");
		} catch (Exception e) {
		}

		try {
			parse("(time (now 3.) (pm KickOff))");
			fail("Exception expected but none got");
		} catch (Exception e) {
		}
	}

	/**
	 * Test vision perceptor parsing
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testVision() throws Exception
	{
		IVisibleObjectPerceptor flag1;
		IVisibleObjectPerceptor goal2;
		IVisibleObjectPerceptor ball;
		IPlayerPos player1;
		IPlayerPos player2;
		IPlayerPos player3;

		// normal
		parse("(See (F1L (pol 19.11 111.69 -9.57)) "
				+ "(G1R (pol 9.88 139.29 -21.07)) (B (pol 18.34 4.66 -9.90)) "
				+ "(P (team RoboLog) (id 1) (pol 37.50 16.15 -0.00)))");

		player1 = testee.getVisiblePlayers().get(0);

		flag1 = testee.getVisibleObject("F1L");
		goal2 = testee.getVisibleObject("G1R");
		ball = testee.getVisibleObject("B");

		assertEquals("F1L", flag1.getName());
		assertEquals(19.11, flag1.getDistance(), 0.0001);
		assertEquals(111.69, flag1.getHorizontalAngleDeg(), 0.0001);
		assertEquals(-9.57, flag1.getLatitudeAngleDeg(), 0.0001);

		assertEquals("G1R", goal2.getName());
		assertEquals(9.88, goal2.getDistance(), 0.0001);
		assertEquals(139.29, goal2.getHorizontalAngleDeg(), 0.0001);
		assertEquals(-21.07, goal2.getLatitudeAngleDeg(), 0.0001);

		assertEquals("B", ball.getName());
		assertEquals(18.34, ball.getDistance(), 0.0001);
		assertEquals(4.66, ball.getHorizontalAngleDeg(), 0.0001);
		assertEquals(-9.9, ball.getLatitudeAngleDeg(), 0.0001);

		assertEquals("PRoboLog1", player1.getName());
		assertEquals(37.5, player1.getDistance(), 0.0001);
		assertEquals(16.15, player1.getHorizontalAngleDeg(), 0.0001);
		assertEquals(0, player1.getLatitudeAngleDeg(), 0.0001);
		assertEquals("RoboLog", player1.getTeamname());
		assertEquals(1, player1.getId());

		// new player
		parse("(See (P (team RoboLog) (id 1) (pol 3.50 16.5 -9.00)) "
				+ "(P (team magmaOffenburg) (id 2) (pol 5 1.15 0.01))"
				+ "(P (team RoboLog) (id 3) (llowerarm (pol 1.0 2.0 3.0))))");

		player1 = (IPlayerPos) testee.getVisibleObject("PRoboLog1");
		player2 = (IPlayerPos) testee.getVisibleObject("PmagmaOffenburg2");
		player3 = (IPlayerPos) testee.getVisibleObject("PRoboLog3");

		assertEquals(3, testee.getVisiblePlayers().size());

		assertEquals(3.5, player1.getDistance(), 0.0001);
		assertEquals(16.5, player1.getHorizontalAngleDeg(), 0.0001);
		assertEquals(-9, player1.getLatitudeAngleDeg(), 0.0001);
		assertEquals("RoboLog", player1.getTeamname());
		assertEquals(1, player1.getId());

		assertEquals(5, player2.getDistance(), 0.0001);
		assertEquals(1.15, player2.getHorizontalAngleDeg(), 0.0001);
		assertEquals(0.01, player2.getLatitudeAngleDeg(), 0.0001);
		assertEquals("magmaOffenburg", player2.getTeamname());
		assertEquals(2, player2.getId());

		assertEquals(1.0, player3.getDistance(), 0.0001);
		assertEquals(2.0, player3.getHorizontalAngleDeg(), 0.0001);
		assertEquals(3.0, player3.getLatitudeAngleDeg(), 0.0001);
		assertEquals("RoboLog", player3.getTeamname());
		assertEquals(3, player3.getId());

		// incomplete 1
		try {
			parse("(See (F1L (pol 1.11 111.6");
			fail("Expected exception, but was none");
		} catch (PerceptorConversionException e) {
			// expected
		}

		assertEquals(19.11, flag1.getDistance(), 0.0001);
		assertEquals(111.69, flag1.getHorizontalAngleDeg(), 0.0001);
		assertEquals(-9.57, flag1.getLatitudeAngleDeg(), 0.0001);

		// incomplete 2
		try {
			parse("(See (F1L (pol ");
			fail("Expected exception, but was none");
		} catch (PerceptorConversionException e) {
			// expected
		}

		assertEquals(19.11, flag1.getDistance(), 0.0001);
		assertEquals(111.69, flag1.getHorizontalAngleDeg(), 0.0001);
		assertEquals(-9.57, flag1.getLatitudeAngleDeg(), 0.0001);

		// incomplete 3
		try {
			parse("(See (F1L ");
			fail("Expected exception, but was none");
		} catch (PerceptorConversionException e) {
			// expected
		}

		assertEquals(19.11, flag1.getDistance(), 0.0001);
		assertEquals(111.69, flag1.getHorizontalAngleDeg(), 0.0001);
		assertEquals(-9.57, flag1.getLatitudeAngleDeg(), 0.0001);

		// incomplete 4
		try {
			parse("(See ");
			fail("Expected exception, but was none");
		} catch (PerceptorConversionException e) {
			// expected
		}

		assertEquals(19.11, flag1.getDistance(), 0.0001);
		assertEquals(111.69, flag1.getHorizontalAngleDeg(), 0.0001);
		assertEquals(-9.57, flag1.getLatitudeAngleDeg(), 0.0001);

		// incomplete 5
		try {
			parse("(See (P (team RoboLo) (id 2) ");
			fail("Expected exception, but was none");
		} catch (PerceptorConversionException e) {
			// expected
		}

		// incomplete 6
		try {
			parse("(See (P (team RoboLo");
			fail("Expected exception, but was none");
		} catch (PerceptorConversionException e) {
			// expected
		}
	}

	@Test
	public void testMultipleBodyParts() throws Exception
	{
		// new player
		parse("(See (P (team magma) (id 3) "
				+ "(head (pol 2.14 9.93 -7.25)) (rlowerarm (pol 2.15 3.62 -5.97))"
				+ " (llowerarm (pol 2.31 6.21 -5.32)) "
				+ "(rfoot (pol 2.26 -2.74 -5.34)) (lfoot (pol 2.49 0.0 -0.0))))");

		IPlayerPos player1 = testee.getVisiblePlayers().get(0);

		Map<String, Vector3D> parts = player1.getAllBodyParts();
		Vector3D part = parts.get("head");
		assertNotNull(part);
		part = parts.get("rlowerarm");
		assertNotNull(part);
		part = parts.get("llowerarm");
		assertNotNull(part);
		part = parts.get("rfoot");
		assertNotNull(part);
		part = parts.get("lfoot");
		assertNotNull(part);
		assertEquals(2.49, part.getX(), 0.0001);
	}

	/**
	 * Test error handling in vision perceptor parser
	 *
	 * @throws Exception Expected!
	 */
	@Test
	public void testPlayerPMissing() throws Exception
	{
		String message = "(See (B (pol 0.36 16.48 -60.37)) ( (id 0) (pol 1.50 -79.82 49.85)))";

		assertThrows(PerceptorConversionException.class, () -> { parse(message); });
	}

	/**
	 * Test hear perceptor parsing
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testHearPerceptor() throws Exception
	{
		String message = "(hear magma 7.98 self \"Das ist ein Test\")(hear magma 8.00 10 text)";

		parse(message);

		List<IHearPerceptor> hearList = testee.getHearPerceptors();
		for (IHearPerceptor hear : hearList) {
			if (hear.getName().equals("hear7.98self")) {
				assertEquals("hear7.98self", hear.getName());
				assertEquals(7.98, hear.getTime(), 0.0001);
				assertEquals("self", hear.getTarget());
				assertEquals("\"Das ist ein Test\"", hear.getMessage());
			} else {
				assertEquals(8.00, hear.getTime(), 0.0001);
				assertEquals("10", hear.getTarget());
				assertEquals("text", hear.getMessage());
			}
		}
	}

	/**
	 * Test error handling in hear perceptor parser
	 */
	@Test
	public void testHearPerceptorException() throws Exception
	{
		// Incomplete 1: Empty
		String message = "(hear)";

		try {
			parse(message);
			fail("Exception expected!");
		} catch (PerceptorConversionException e) {
		}

		// Incomplete 2: Missing target
		message = "(hear magma 7.98)";

		try {
			parse(message);
			fail("Exception expected!");
		} catch (PerceptorConversionException e) {
		}

		// Incomplete 3: Time is a string
		message = "(hear magma bla)";

		try {
			parse(message);
			fail("Exception expected!");
		} catch (PerceptorConversionException e) {
		}

		// Incomplete 4: Missing message
		//		message = "(hear magma 9.75 self)";
		//
		//		try {
		//			parse(message);
		//			fail("Exception expected!");
		//		} catch (PerceptorConversionException e) {
		//		}
	}

	/**
	 * Test hinge joint perceptor parsing
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testHingeJointPerceptor() throws Exception
	{
		String message = "(HJ (n hj1) (ax -1.347))";

		parse(message);

		IHingeJointPerceptor hinge = testee.getHingeJointPerceptor("hj1");

		assertEquals("hj1", hinge.getName());
		assertEquals(-1.347, hinge.getAxis(), 0.0001);
	}

	/**
	 * Test line perceptor parsing
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testLinePerceptor() throws Exception
	{
		String msg = "(See (L (pol 3.01 35.26 -33.07) (pol 2.64 60.13 -38.41)))";

		parse(msg);

		ILinePerceptor line = testee.getVisibleLines().get(0);
		assertEquals(3.01, line.getDistance(), 0.0001);
		assertEquals(35.26, line.getHorizontalAngleDeg(), 0.0001);
		assertEquals(-33.07, line.getLatitudeAngleDeg(), 0.0001);

		assertEquals(2.64, line.getDistance2(), 0.0001);
		assertEquals(60.13, line.getHorizontalAngleDeg2(), 0.0001);
		assertEquals(-38.41, line.getLatitudeAngleDeg2(), 0.0001);
	}

	/**
	 * Test line perceptor parsing
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testLinePerceptorNan() throws Exception
	{
		String msg = "(See (L (pol 9.88 nan -21.07) (pol 9.88 139.29 -21.07)))";

		parse(msg);
		// should not get an exception
		ILinePerceptor line = testee.getVisibleLines().get(0);

		assertEquals(9.88, line.getDistance2(), 0.0001);
		assertEquals(139.29, line.getHorizontalAngleDeg2(), 0.0001);
		assertEquals(-21.07, line.getLatitudeAngleDeg2(), 0.0001);
	}
}