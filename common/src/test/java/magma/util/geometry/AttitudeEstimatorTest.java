/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package magma.util.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author kdorer
 *
 */
public class AttitudeEstimatorTest
{
	private static final double PRECISION = 0.000001;

	private static final int SPEED_TEST_ITS = (int) 1e8;

	private AttitudeEstimator testee;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		// Create an attitude estimator
		testee = new AttitudeEstimator(true); // Explicitly specify the default
											  // value of true for quickLearn...
	}

	@Test
	public void testSetterGetter()
	{
		// Test: Test the reset/get/set characteristics of the attitude estimator
		// Declare variables
		double q2[] = {0.0, 0.0, 0.0, 0.0};
		double v1[] = {0.0, 0.0, 0.0};
		double v2[] = {0.0, 0.0, 0.0};
		double norm = 0.0;
		AttitudeEstimator.AccMethodEnum method = AttitudeEstimator.AccMethodEnum.ME_FUSED_YAW;

		// Check the initial estimator state
		method = testee.getAccMethod();
		assertEquals(AttitudeEstimator.AccMethodEnum.ME_DEFAULT, method);
		double[] q1 = testee.getAttitude();
		assertEquals(1.0, q1[0], PRECISION);
		assertEquals(0.0, q1[1], PRECISION);
		assertEquals(0.0, q1[2], PRECISION);
		assertEquals(0.0, q1[3], PRECISION);
		assertEquals(0.0, testee.eulerYaw(), PRECISION);
		assertEquals(0.0, testee.eulerPitch(), PRECISION);
		assertEquals(0.0, testee.eulerRoll(), PRECISION);
		assertEquals(0.0, testee.fusedYaw(), PRECISION);
		assertEquals(0.0, testee.fusedPitch(), PRECISION);
		assertEquals(0.0, testee.fusedRoll(), PRECISION);
		assertTrue(testee.fusedHemi());
		assertEquals(0.0, testee.getLambda(), PRECISION);
		testee.getMagCalib(v1);
		assertEquals(1.0, v1[0], PRECISION);
		assertEquals(0.0, v1[1], PRECISION);
		assertEquals(0.0, v1[2], PRECISION);
		double[] result = testee.getPIGains();
		assertEquals(2.20, result[0], PRECISION);
		assertEquals(2.65, result[1], PRECISION);
		assertEquals(10.0, result[2], PRECISION);
		assertEquals(1.25, result[3], PRECISION);
		assertEquals(3.0, testee.getQLTime(), PRECISION);
		testee.getGyroBias(v1);
		assertEquals(0.0, v1[0], PRECISION);
		assertEquals(0.0, v1[1], PRECISION);
		assertEquals(0.0, v1[2], PRECISION);

		// Test modifying the acc-only resolution method
		testee.setAccMethod(AttitudeEstimator.AccMethodEnum.ME_ZYX_YAW);
		method = testee.getAccMethod();
		assertEquals(AttitudeEstimator.AccMethodEnum.ME_ZYX_YAW, method);
		testee.setAccMethod(AttitudeEstimator.AccMethodEnum.ME_FUSED_YAW);
		method = testee.getAccMethod();
		assertEquals(AttitudeEstimator.AccMethodEnum.ME_FUSED_YAW, method);
		testee.setAccMethod(AttitudeEstimator.AccMethodEnum.ME_ABS_FUSED_YAW);
		method = testee.getAccMethod();
		assertEquals(AttitudeEstimator.AccMethodEnum.ME_ABS_FUSED_YAW, method);
		testee.setAccMethod(AttitudeEstimator.AccMethodEnum.ME_ZYX_YAW);
		method = testee.getAccMethod();
		assertEquals(AttitudeEstimator.AccMethodEnum.ME_ZYX_YAW, method);

		// Test modifying the bias estimate
		v2[0] = 2.1;
		v2[1] = 3.2;
		v2[2] = 4.3;
		testee.setGyroBias(v2);
		testee.getGyroBias(v1);
		assertEquals(2.1, v1[0], PRECISION);
		assertEquals(3.2, v1[1], PRECISION);
		assertEquals(4.3, v1[2], PRECISION);
		testee.setGyroBias(5.4, 4.3, 3.2);
		testee.getGyroBias(v1);
		assertEquals(5.4, v1[0], PRECISION);
		assertEquals(4.3, v1[1], PRECISION);
		assertEquals(3.2, v1[2], PRECISION);

		// Test modifying the lambda value
		testee.setLambda(1.0);
		assertEquals(1.0, testee.getLambda(), PRECISION);
		testee.resetLambda();
		assertEquals(0.0, testee.getLambda(), PRECISION);
		testee.setLambda(0.481);
		assertEquals(0.481, testee.getLambda(), PRECISION);

		// Test modifying the magnetometer calibration
		v2[0] = 1.2;
		v2[1] = 3.4;
		v2[2] = 5.6;
		testee.setMagCalib(v2);
		testee.getMagCalib(v1);
		assertEquals(1.2, v1[0], PRECISION);
		assertEquals(3.4, v1[1], PRECISION);
		assertEquals(5.6, v1[2], PRECISION);
		testee.setMagCalib(6.5, 4.3, 2.1);
		testee.getMagCalib(v1);
		assertEquals(6.5, v1[0], PRECISION);
		assertEquals(4.3, v1[1], PRECISION);
		assertEquals(2.1, v1[2], PRECISION);
		testee.setMagCalib(9.0, 0.0, 0.0);

		// Test modifying the configuration variables
		testee.setPIGains(4.00, 3.00, 1.70, 0.00);
		result = testee.getPIGains();
		assertEquals(4.00, result[0], PRECISION);
		assertEquals(3.00, result[1], PRECISION);
		assertEquals(10.0, result[2], PRECISION);
		assertEquals(1.25, result[3], PRECISION);
		testee.setPIGains(0.00, 7.40, 8.00, 3.00);
		result = testee.getPIGains();
		assertEquals(4.00, result[0], PRECISION);
		assertEquals(3.00, result[1], PRECISION);
		assertEquals(8.00, result[2], PRECISION);
		assertEquals(3.00, result[3], PRECISION);
		testee.setQLTime(2.0);
		assertEquals(2.0, testee.getQLTime(), PRECISION);

		// Test modifying the attitude estimate
		q2[0] = 1.2;
		q2[1] = 3.4;
		q2[2] = 5.6;
		q2[3] = 7.8;
		testee.setAttitude(q2);
		norm = Math.sqrt(q2[0] * q2[0] + q2[1] * q2[1] + q2[2] * q2[2] + q2[3] * q2[3]);
		q1 = testee.getAttitude();
		assertEquals(q2[0] / norm, q1[0], PRECISION);
		assertEquals(q2[1] / norm, q1[1], PRECISION);
		assertEquals(q2[2] / norm, q1[2], PRECISION);
		assertEquals(q2[3] / norm, q1[3], PRECISION);
		assertNotEquals(0.0, testee.eulerYaw());
		assertNotEquals(0.0, testee.eulerPitch());
		assertNotEquals(0.0, testee.eulerRoll());
		assertNotEquals(0.0, testee.fusedYaw());
		assertNotEquals(0.0, testee.fusedPitch());
		assertNotEquals(0.0, testee.fusedRoll());
		testee.setAttitude(0.0, 0.0, 0.0, 0.0);
		q1 = testee.getAttitude();
		assertEquals(1.0, q1[0], PRECISION);
		assertEquals(0.0, q1[1], PRECISION);
		assertEquals(0.0, q1[2], PRECISION);
		assertEquals(0.0, q1[3], PRECISION);
		assertEquals(0.0, testee.eulerYaw(), PRECISION);
		assertEquals(0.0, testee.eulerPitch(), PRECISION);
		assertEquals(0.0, testee.eulerRoll(), PRECISION);
		assertEquals(0.0, testee.fusedYaw(), PRECISION);
		assertEquals(0.0, testee.fusedPitch(), PRECISION);
		assertEquals(0.0, testee.fusedRoll(), PRECISION);
		assertTrue(testee.fusedHemi());
		testee.setAttitude(8.7, 6.5, 4.3, 2.1);
		norm = Math.sqrt(8.7 * 8.7 + 6.5 * 6.5 + 4.3 * 4.3 + 2.1 * 2.1);
		q1 = testee.getAttitude();
		assertEquals(8.7 / norm, q1[0], PRECISION);
		assertEquals(6.5 / norm, q1[1], PRECISION);
		assertEquals(4.3 / norm, q1[2], PRECISION);
		assertEquals(2.1 / norm, q1[3], PRECISION);
		assertNotEquals(0.0, testee.eulerYaw());
		assertNotEquals(0.0, testee.eulerPitch());
		assertNotEquals(0.0, testee.eulerRoll());
		assertNotEquals(0.0, testee.fusedYaw());
		assertNotEquals(0.0, testee.fusedPitch());
		assertNotEquals(0.0, testee.fusedRoll());
		testee.setAttitudeEuler(1.4, -0.5, 2.7);
		assertEquals(1.4, testee.eulerYaw(), PRECISION);
		assertEquals(-0.5, testee.eulerPitch(), PRECISION);
		assertEquals(2.7, testee.eulerRoll(), PRECISION);
		testee.setAttitudeFused(0.6, -0.4, 0.7, false);
		assertEquals(0.6, testee.fusedYaw(), PRECISION);
		assertEquals(-0.4, testee.fusedPitch(), PRECISION);
		assertEquals(0.7, testee.fusedRoll(), PRECISION);
		assertFalse(testee.fusedHemi());

		// Check the reset function does its job
		testee.reset(false, true);
		q1 = testee.getAttitude();
		assertEquals(1.0, q1[0], PRECISION);
		assertEquals(0.0, q1[1], PRECISION);
		assertEquals(0.0, q1[2], PRECISION);
		assertEquals(0.0, q1[3], PRECISION);
		assertEquals(0.0, testee.eulerYaw(), PRECISION);
		assertEquals(0.0, testee.eulerPitch(), PRECISION);
		assertEquals(0.0, testee.eulerRoll(), PRECISION);
		assertEquals(0.0, testee.fusedYaw(), PRECISION);
		assertEquals(0.0, testee.fusedPitch(), PRECISION);
		assertEquals(0.0, testee.fusedRoll(), PRECISION);
		assertTrue(testee.fusedHemi());
		assertEquals(1.0, testee.getLambda(), PRECISION);
		testee.getGyroBias(v1);
		assertEquals(0.0, v1[0], PRECISION);
		assertEquals(0.0, v1[1], PRECISION);
		assertEquals(0.0, v1[2], PRECISION);

		// Check that the configuration variables and magnetometer calibration
		// were left untouched by the reset
		testee.getMagCalib(v1);
		assertEquals(9.0, v1[0], PRECISION);
		assertEquals(0.0, v1[1], PRECISION);
		assertEquals(0.0, v1[2], PRECISION);
		result = testee.getPIGains();
		assertEquals(4.00, result[0], PRECISION);
		assertEquals(3.00, result[1], PRECISION);
		assertEquals(8.00, result[2], PRECISION);
		assertEquals(3.00, result[3], PRECISION);
		assertEquals(2.0, testee.getQLTime(), PRECISION);

		// Check that resetAll resets everything it should (more than reset)
		testee.resetAll(true);
		assertEquals(1.0, q1[0], PRECISION);
		assertEquals(0.0, q1[1], PRECISION);
		assertEquals(0.0, q1[2], PRECISION);
		assertEquals(0.0, q1[3], PRECISION);
		assertEquals(0.0, testee.eulerYaw(), PRECISION);
		assertEquals(0.0, testee.eulerPitch(), PRECISION);
		assertEquals(0.0, testee.eulerRoll(), PRECISION);
		assertEquals(0.0, testee.fusedYaw(), PRECISION);
		assertEquals(0.0, testee.fusedPitch(), PRECISION);
		assertEquals(0.0, testee.fusedRoll(), PRECISION);
		assertTrue(testee.fusedHemi());
		assertEquals(0.0, testee.getLambda(), PRECISION);
		testee.getGyroBias(v1);
		assertEquals(0.0, v1[0], PRECISION);
		assertEquals(0.0, v1[1], PRECISION);
		assertEquals(0.0, v1[2], PRECISION);
		testee.getMagCalib(v1);
		assertEquals(1.0, v1[0], PRECISION);
		assertEquals(0.0, v1[1], PRECISION);
		assertEquals(0.0, v1[2], PRECISION);
		result = testee.getPIGains();
		assertEquals(2.20, result[0], PRECISION);
		assertEquals(2.65, result[1], PRECISION);
		assertEquals(10.0, result[2], PRECISION);
		assertEquals(1.25, result[3], PRECISION);
		assertEquals(3.0, testee.getQLTime(), PRECISION);
	}

	private void assertNotEquals(double expected, double butWas)
	{
		assertTrue(butWas < expected - PRECISION || butWas > expected + PRECISION);
	}

	@Test
	public void testSetAttitudeEuler()
	{
		// Test: Test the correctness of the setAttitudeEuler() function

		// Case: Normal Euler angles
		testee.setAttitudeEuler(0.0, 0.0, 0.0);
		assertEquals(0.0, testee.eulerYaw(), 1e-11);
		assertEquals(0.0, testee.eulerPitch(), 1e-11);
		assertEquals(0.0, testee.eulerRoll(), 1e-11);
		testee.setAttitudeEuler(0.5, -0.5, 2.8);
		assertEquals(0.5, testee.eulerYaw(), 1e-11);
		assertEquals(-0.5, testee.eulerPitch(), 1e-11);
		assertEquals(2.8, testee.eulerRoll(), 1e-11);
		testee.setAttitudeEuler(1.9, -1.1, -2.3);
		assertEquals(1.9, testee.eulerYaw(), 1e-11);
		assertEquals(-1.1, testee.eulerPitch(), 1e-11);
		assertEquals(-2.3, testee.eulerRoll(), 1e-11);
		testee.setAttitudeEuler(-0.2, 1.3, 0.4);
		assertEquals(-0.2, testee.eulerYaw(), 1e-11);
		assertEquals(1.3, testee.eulerPitch(), 1e-11);
		assertEquals(0.4, testee.eulerRoll(), 1e-11);
		testee.setAttitudeEuler(-2.6, 0.1, -0.9);
		assertEquals(-2.6, testee.eulerYaw(), 1e-11);
		assertEquals(0.1, testee.eulerPitch(), 1e-11);
		assertEquals(-0.9, testee.eulerRoll(), 1e-11);

		// Case: Near-singular Euler angles
		testee.setAttitudeEuler(0.7, 1.57079, -0.3);
		assertEquals(0.70000, testee.eulerYaw(), 1e-9);
		assertEquals(1.57079, testee.eulerPitch(), 1e-9);
		assertEquals(-0.30000, testee.eulerRoll(), 1e-9);
		testee.setAttitudeEuler(1.8, -1.57079, 3.0);
		assertEquals(1.80000, testee.eulerYaw(), 1e-9);
		assertEquals(-1.57079, testee.eulerPitch(), 1e-9);
		assertEquals(3.00000, testee.eulerRoll(), 1e-9);
	}

	// Test: Test the correctness of the setAttitudeFused() function
	@Test
	public void testSetAttitudeFused()
	{
		// Case: Normal fused angles
		testee.setAttitudeFused(0.0, 0.0, 0.0, true);
		assertEquals(0.0, testee.fusedYaw(), 1e-14);
		assertEquals(0.0, testee.fusedPitch(), 1e-14);
		assertEquals(0.0, testee.fusedRoll(), 1e-14);
		assertTrue(testee.fusedHemi());
		testee.setAttitudeFused(-1.2389912679385777, 0.4504108626120746, 0.2309101949807438, true);
		assertEquals(-1.2389912679385777, testee.fusedYaw(), 1e-14);
		assertEquals(0.4504108626120746, testee.fusedPitch(), 1e-14);
		assertEquals(0.2309101949807438, testee.fusedRoll(), 1e-14);
		assertTrue(testee.fusedHemi());
		testee.setAttitudeFused(-1.1245333812991449, -1.1152938107481036, -0.1505999801369221, false);
		assertEquals(-1.1245333812991449, testee.fusedYaw(), 1e-14);
		assertEquals(-1.1152938107481036, testee.fusedPitch(), 1e-14);
		assertEquals(-0.1505999801369221, testee.fusedRoll(), 1e-14);
		assertFalse(testee.fusedHemi());
		testee.setAttitudeFused(-2.9940777491210464, -0.0180413873657193, 1.0164835253856090, false);
		assertEquals(-2.9940777491210464, testee.fusedYaw(), 1e-14);
		assertEquals(-0.0180413873657193, testee.fusedPitch(), 1e-14);
		assertEquals(1.0164835253856090, testee.fusedRoll(), 1e-14);
		assertFalse(testee.fusedHemi());
		testee.setAttitudeFused(-0.8827575035984855, -1.0299638573151797, 0.2067085358389749, true);
		assertEquals(-0.8827575035984855, testee.fusedYaw(), 1e-14);
		assertEquals(-1.0299638573151797, testee.fusedPitch(), 1e-14);
		assertEquals(0.2067085358389749, testee.fusedRoll(), 1e-14);
		assertTrue(testee.fusedHemi());

		// Case: Invalid fused angles (ones that fail the sine sum criterion)
		testee.setAttitudeFused(-0.2909856257679569, -1.1630855231035317, -0.6314208789414353, false);
		assertEquals(-0.2909856257679569, testee.fusedYaw(), 1e-14);
		assertEquals(-0.9993590078458838, testee.fusedPitch(), 1e-14);
		assertEquals(-0.5714373189490128, testee.fusedRoll(), 1e-14);
		assertTrue(testee.fusedHemi());
		testee.setAttitudeFused(-1.7489003673032701, 1.1472271331811683, -0.8937378526800521, true);
		assertEquals(-1.7489003673032701, testee.fusedYaw(), 1e-14);
		assertEquals(0.8634210833398374, testee.fusedPitch(), 1e-14);
		assertEquals(-0.7073752434550591, testee.fusedRoll(), 1e-14);
		assertTrue(testee.fusedHemi());
		testee.setAttitudeFused(2.4583008656951644, -1.3607318026738779, 0.9501187069481475, false);
		assertEquals(2.4583008656951644, testee.fusedYaw(), 1e-14);
		assertEquals(-0.8769820089949256, testee.fusedPitch(), 1e-14);
		assertEquals(0.6938143177999709, testee.fusedRoll(), 1e-14);
		assertTrue(testee.fusedHemi());
		testee.setAttitudeFused(-1.5523060420811190, 0.5956084196409952, 1.4156676884054307, true);
		assertEquals(-1.5523060420811190, testee.fusedYaw(), 1e-14);
		assertEquals(0.5164300632429631, testee.fusedPitch(), 1e-14);
		assertEquals(1.0543662635519337, testee.fusedRoll(), 1e-14);
		assertTrue(testee.fusedHemi());
	}

	// Test: Test the correctness of the updateEuler() function
	@Test
	public void testUpdateEuler()
	{
		// Set the current attitude estimate and magnetometer calibration
		testee.setMagCalib(1.0, 0.0, 0.0);

		// Case: Normal Euler angles
		testee.setAttitude(0.592552850843098, 0.707894251703962, 0.111054036045993, 0.368013380789475);
		assertEquals(0.7, testee.eulerYaw(), 1e-14);
		assertEquals(-0.4, testee.eulerPitch(), 1e-14);
		assertEquals(1.6, testee.eulerRoll(), 1e-14);
		testee.setAttitude(0.310125868152194, -0.652539210120707, 0.620265075419198, 0.305427178510867);
		assertEquals(-1.5, testee.eulerYaw(), 1e-14);
		assertEquals(0.9, testee.eulerPitch(), 1e-14);
		assertEquals(-3.1, testee.eulerRoll(), 1e-14);

		// Case: Near-singular Euler angles (more numerical issues with asin/atan2
		// => looser verification tolerances)
		testee.setAttitude(-0.126037693525368, -0.695781316126912, -0.126040295812392, 0.695784955119778);
		assertEquals(2.70000, testee.eulerYaw(), 1e-9);
		assertEquals(1.57079, testee.eulerPitch(), 1e-9);
		assertEquals(-0.80000, testee.eulerRoll(), 1e-9);
		testee.setAttitude(0.439544782044344, -0.553893538424300, -0.439544465585940, -0.553898000937147);
		assertEquals(-2.40000, testee.eulerYaw(), 1e-9);
		assertEquals(-1.57079, testee.eulerPitch(), 1e-9);
		assertEquals(0.60000, testee.eulerRoll(), 1e-9);
	}

	// Test: Test the correctness of the updateFused() function
	@Test
	public void testUpdateFused()
	{
		// Set the current attitude estimate and magnetometer calibration
		testee.setMagCalib(1.0, 0.0, 0.0);

		// Case: Normal fused angles
		testee.setAttitude(0.4821785020739293, 0.5681941774354589, 0.4837559843360982, 0.4589546998181812);
		assertEquals(1.5214534918546789, testee.fusedYaw(), 1e-14);
		assertEquals(-0.0550651280646345, testee.fusedPitch(), 1e-14);
		assertEquals(1.4441114693495201, testee.fusedRoll(), 1e-14);
		assertFalse(testee.fusedHemi());
		testee.setAttitude(-0.5747725522069820, 0.6113337041221015, -0.5417310040241355, -0.0493470841370070);
		assertEquals(0.1712899139689803, testee.fusedYaw(), 1e-14);
		assertEquals(0.7519705909187845, testee.fusedPitch(), 1e-14);
		assertEquals(-0.7066504864647706, testee.fusedRoll(), 1e-14);
		assertFalse(testee.fusedHemi());
		testee.setAttitude(-0.3208918327799606, 0.3458339031220823, 0.4891541368196635, 0.7335908761282912);
		assertEquals(-2.3168957445381952, testee.fusedYaw(), 1e-14);
		assertEquals(-0.9637426788118773, testee.fusedPitch(), 1e-14);
		assertEquals(0.5186722733459991, testee.fusedRoll(), 1e-14);
		assertTrue(testee.fusedHemi());
		testee.setAttitude(0.6233964573859717, 0.5317992944741803, -0.3207546279525302, 0.4750608760594984);
		assertEquals(1.3023404766264970, testee.fusedYaw(), 1e-14);
		assertEquals(-1.1318234423525451, testee.fusedPitch(), 1e-14);
		assertEquals(0.3664331247282780, testee.fusedRoll(), 1e-14);
		assertTrue(testee.fusedHemi());

		// Case: Fused angles at the crossover between the positive and negative z
		// hemispheres
		testee.setAttitude(0.7064483840022956, -0.6632516485507483, 0.2451473908572840, 0.0305071826153391);
		assertEquals(0.0863141334826512, testee.fusedYaw(), 1e-14);
		assertEquals(0.3971978161591546, testee.fusedPitch(), 1e-14);
		assertEquals(-1.1735985106357418, testee.fusedRoll(), 1e-14);
		assertTrue(testee.fusedHemi());
		testee.setAttitude(0.3183170816271715, -0.7070179228893456, 0.0112096705230417, -0.6314065532953872);
		assertEquals(-2.2076849618654277, testee.fusedYaw(), 1e-14);
		assertEquals(-1.0879889487677572, testee.fusedPitch(), 1e-14);
		assertEquals(-0.4828073780271395, testee.fusedRoll(), 1e-14);
		assertTrue(testee.fusedHemi());
		testee.setAttitude(0.0871006265553665, 0.6107171579599902, -0.3564050406114823, -0.7017217973340023);
		assertEquals(-2.8946075869572829, testee.fusedYaw(), 1e-14);
		assertEquals(0.9190419991352411, testee.fusedPitch(), 1e-14);
		assertEquals(0.6517543276596556, testee.fusedRoll(), 1e-14);
		assertTrue(testee.fusedHemi());
		testee.setAttitude(0.6912675571926681, 0.0400591627670449, 0.7059711454514606, -0.1488259709904069);
		assertEquals(-0.4241148971008855, testee.fusedYaw(), 1e-14);
		assertEquals(1.4154214376340313, testee.fusedPitch(), 1e-14);
		assertEquals(-0.1553748891608649, testee.fusedRoll(), 1e-14);
		assertTrue(testee.fusedHemi());
	}

	// Test: Test the correctness of the attitude estimator
	@Test
	public void testUpdate()
	{
		// Declare variables
		double q[] = new double[4];
		double b[] = new double[3];
		double mt[] = new double[3];

		testee = new AttitudeEstimator(false);

		// Initialise the attitude estimator
		testee.setAttitude(1.0, 0.0, 0.0, 0.0);
		testee.setGyroBias(0.0, 0.5, 1.0);
		testee.setMagCalib(1.0, 0.0, 0.0);
		testee.setPIGains(5.00, 1.50, 10.0, 1.25);

		// Verify the magnetometer calibration and PI gains
		testee.getMagCalib(mt);
		assertEquals(1.0, mt[0], 1e-15);
		assertEquals(0.0, mt[1], 1e-15);
		assertEquals(0.0, mt[2], 1e-15);
		double[] result = testee.getPIGains();
		assertEquals(5.00, result[0], PRECISION);
		assertEquals(1.50, result[1], PRECISION);
		assertEquals(10.0, result[2], PRECISION);
		assertEquals(1.25, result[3], PRECISION);

		// Check that our initial estimator state is correct
		q = testee.getAttitude();
		assertEquals(1.0, q[0], 1e-15);
		assertEquals(0.0, q[1], 1e-15);
		assertEquals(0.0, q[2], 1e-15);
		assertEquals(0.0, q[3], 1e-15);
		testee.getGyroBias(b);
		assertEquals(0.0, b[0], 1e-15);
		assertEquals(0.5, b[1], 1e-15);
		assertEquals(1.0, b[2], 1e-15);

		// Define the update function inputs
		double dt = 0.02;
		double acc[] = {-4.102821582419913e-01, 7.575655021799962e-01, 6.712045437704806e+00};
		double gyro[] = {6.403921931759489e-01, 9.360723675810693e-01, 7.894952671157460e-01};
		double mag[] = {-1.151522662792782e+00, -1.118054993770523e+00, 5.233922602069686e-01};

		// Run the estimation and print the outputs
		for (int k = 0; k < 500; k++) {
			// Perform the update
			testee.update(dt, gyro[0], gyro[1], gyro[2], acc[0], acc[1], acc[2], mag[0], mag[1], mag[2]);

			// Retrieve the new estimates
			q = testee.getAttitude();
			testee.getGyroBias(b);

			// Verify a couple of estimates, spread evenly throughout the
			// estimation run
			if (k == 99) {
				assertEquals(3.4292612121324739e-01, q[0], 1e-15);
				assertEquals(-1.9209071503758710e-02, q[1], 1e-15);
				assertEquals(8.8186908413074208e-02, q[2], 1e-15);
				assertEquals(9.3501644699232156e-01, q[3], 1e-15);
				assertEquals(5.0366756023736658e-01, b[0], 1e-15);
				assertEquals(7.2184655689754496e-01, b[1], 1e-15);
				assertEquals(3.4812977832258141e-01, b[2], 1e-15);
			}
			if (k == 199) {
				assertEquals(3.8226606451658535e-01, q[0], 1e-15);
				assertEquals(-8.6435175083959797e-03, q[1], 1e-15);
				assertEquals(6.8603975753226451e-02, q[2], 1e-15);
				assertEquals(9.2146157816532670e-01, q[3], 1e-15);
				assertEquals(6.1223321502854988e-01, b[0], 1e-15);
				assertEquals(8.9189462536685826e-01, b[1], 1e-15);
				assertEquals(6.9850786079300486e-01, b[2], 1e-15);
			}
			if (k == 299) {
				assertEquals(3.9026384986863527e-01, q[0], 1e-15);
				assertEquals(-6.4499730438504254e-03, q[1], 1e-15);
				assertEquals(6.4564403996416844e-02, q[2], 1e-15);
				assertEquals(9.1841382996448406e-01, q[3], 1e-15);
				assertEquals(6.3458476919266138e-01, b[0], 1e-15);
				assertEquals(9.2696127681439977e-01, b[1], 1e-15);
				assertEquals(7.7073028932131382e-01, b[2], 1e-15);
			}
			if (k == 399) {
				assertEquals(3.9190829854563736e-01, q[0], 1e-15);
				assertEquals(-5.9976183670213913e-03, q[1], 1e-15);
				assertEquals(6.3730744907061473e-02, q[2], 1e-15);
				assertEquals(9.1777464895178817e-01, q[3], 1e-15);
				assertEquals(6.3919442338859822e-01, b[0], 1e-15);
				assertEquals(9.3419322300964502e-01, b[1], 1e-15);
				assertEquals(7.8562502731071371e-01, b[2], 1e-15);
			}
			if (k == 499) {
				assertEquals(3.9224726363482920e-01, q[0], 1e-15);
				assertEquals(-5.9043191047627097e-03, q[1], 1e-15);
				assertEquals(6.3558774382524219e-02, q[2], 1e-15);
				assertEquals(9.1764236246257147e-01, q[3], 1e-15);
				assertEquals(6.4014515495033797e-01, b[0], 1e-15);
				assertEquals(9.3568479682624373e-01, b[1], 1e-15);
				assertEquals(7.8869703594960638e-01, b[2], 1e-15);
			}
		}
	}

	//
	// Special testing
	//

	// Run a certain number of cycles on a given estimator
	void executeCycles(AttitudeEstimator Est, int N, boolean useMag)
	{
		// Declare variables
		double magX, magY, magZ;

		// Set the magnetometer values
		if (useMag) {
			magX = -0.4;
			magY = 0.7;
			magZ = -0.1;
		} else {
			magX = 0.0;
			magY = 0.0;
			magZ = 0.0;
		}

		// Execute the required number of cycles of the estimator
		for (int i = 0; i < N; i++) {
			// FIXME KDO CYCLETIME dependency ?? 0.010 first argument?
			testee.update(0.010, 0.0, 0.05, -0.05, 0.4, -0.2, -6.0, magX, magY, magZ);
			testee.getAttitude();
		}
	}

	// Test: Test the execution time of the magnitude method
	@Test
	@Disabled
	public void testSpeed()
	{
		// Set up the estimator
		testee.setAccMethod(AttitudeEstimator.AccMethodEnum.ME_FUSED_YAW);
		testee.setAttitude(0.4, 0.7, -0.3, -0.5);
		testee.setMagCalib(1.0, 0.0, 0.0);
		executeCycles(testee, SPEED_TEST_ITS, true);
	}

	// Test: Test the execution time of the fused yaw method
	@Test
	@Disabled
	public void testSpeedFusedYaw()
	{
		// Set up the estimator
		testee.setAccMethod(AttitudeEstimator.AccMethodEnum.ME_FUSED_YAW);
		testee.setAttitude(0.4, 0.7, -0.3, -0.5);
		testee.setMagCalib(0.0, 0.0, 0.0);
		executeCycles(testee, SPEED_TEST_ITS, false);
	}

	// Test: Test the execution time of the ZYX yaw method
	@Test
	@Disabled
	public void testSpeedZYXYaw()
	{
		// Set up the estimator
		testee.setAccMethod(AttitudeEstimator.AccMethodEnum.ME_ZYX_YAW);
		testee.setAttitude(0.4, 0.7, -0.3, -0.5);
		testee.setMagCalib(0.0, 0.0, 0.0);
		executeCycles(testee, SPEED_TEST_ITS, false);
	}

	// Test: Test the correctness of the updateQy() function
	@Test
	public void testUpdateQy()
	{
		// Note: If some of the quaternion tests here fail, the first thing that
		// you should check is whether its actually correct,
		// just the wrong sign. i.e. If we're testing for (1,0,0,0) and we get
		// (-1,0,0,0) back, then the unit test will fail,
		// even though a correct output was returned.

		//
		// Test Fused Yaw Method
		//

		// Change the acc-only resolution method
		testee.resetAll(true);
		testee.setAccMethod(AttitudeEstimator.AccMethodEnum.ME_FUSED_YAW);

		// Set the current attitude estimate and magnetometer calibration
		testee.setAttitude(1.0, 0.0, 0.0, 0.0);
		testee.setMagCalib(1.0, 0.0, 0.0);

		// Case: Acc is zero
		testee.updateQy(0.0, 0.0, 0.0, 1.0, 0.0, 0.0);
		assertEquals(1.0, testee.m_Qy[0], 1e-15);
		assertEquals(0.0, testee.m_Qy[1], 1e-15);
		assertEquals(0.0, testee.m_Qy[2], 1e-15);
		assertEquals(0.0, testee.m_Qy[3], 1e-15);

		// Case: Acc and mag are both valid and unambiguous (m_Qhat should be
		// irrelevant)
		testee.updateQy(0.0, 0.0, 5.0, 3.0, 0.0, 0.0);
		assertEquals(1.0, testee.m_Qy[0], 1e-15);
		assertEquals(0.0, testee.m_Qy[1], 1e-15);
		assertEquals(0.0, testee.m_Qy[2], 1e-15);
		assertEquals(0.0, testee.m_Qy[3], 1e-15);
		testee.updateQy(-0.2, 0.4, 1.0, -1.2, 0.6, -0.7);
		assertEquals(-0.261154630414682, testee.m_Qy[0], 1e-15);
		assertEquals(-0.139805215990429, testee.m_Qy[1], 1e-15);
		assertEquals(0.154980763303808, testee.m_Qy[2], 1e-15);
		assertEquals(0.942461523671184, testee.m_Qy[3], 1e-15);
		testee.setMagCalib(0.5, 1.3, -0.8);
		testee.updateQy(1.97, -4.93, -2.18, 0.65, -2.37, 1.14);
		assertEquals(0.5031410611323102, testee.m_Qy[0], 1e-15);
		assertEquals(-0.8289865209174649, testee.m_Qy[1], 1e-15);
		assertEquals(0.0519991639383328, testee.m_Qy[2], 1e-15);
		assertEquals(-0.2385927653754926, testee.m_Qy[3], 1e-15);

		// Prevent mag from being used in the subsequent tests
		testee.setMagCalib(0.0, 0.0, 0.0);

		// Case: Fused angles representation of H with respect to G is not in
		// singularity (normal case)
		testee.setAttitude(0.7872358201641929, -0.2225070728459767, -0.0727954675120195, -0.5704832915113571);
		testee.updateQy(0.1487586731012882, -1.0318259348182854, 0.0520870133587073, 0.0, 0.0, 0.0);
		assertEquals(0.6405374228953400, testee.m_Qy[0], 1e-15);
		assertEquals(-0.6490624955723698, testee.m_Qy[1], 1e-15);
		assertEquals(0.2318772906953779, testee.m_Qy[2], 1e-15);
		assertEquals(-0.3386186775293635, testee.m_Qy[3], 1e-15);
		testee.setAttitude(0.0954283450355833, 0.7576104902164565, 0.5540402995009882, -0.3316008483200932);
		testee.updateQy(0.0758641771452659, 0.1295823593777883, 0.5303498834750111, 0.0, 0.0, 0.0);
		assertEquals(0.5651321197459419, testee.m_Qy[0], 1e-15);
		assertEquals(0.0106502828191484, testee.m_Qy[1], 1e-15);
		assertEquals(-0.1371028890010138, testee.m_Qy[2], 1e-15);
		assertEquals(-0.8134586999564921, testee.m_Qy[3], 1e-15);
		testee.setAttitude(0.2261653189676135, 0.8102974724140080, -0.5265224232161255, -0.1226433550844352);
		testee.updateQy(1.0683744235889208, 0.6702037497975503, -1.2053860886134233, 0.0, 0.0, 0.0);
		assertEquals(0.3694091065234676, testee.m_Qy[0], 1e-15);
		assertEquals(0.7255253534421092, testee.m_Qy[1], 1e-15);
		assertEquals(-0.5648710729361709, testee.m_Qy[2], 1e-15);
		assertEquals(0.1344267253568828, testee.m_Qy[3], 1e-15);
		testee.setAttitude(0.2124070617737524, -0.8257433870186585, 0.2402744656478841, 0.4640035345352651);
		testee.updateQy(0.2234554384126400, -0.7510358746009479, 0.5507195208552991, 0.0, 0.0, 0.0);
		assertEquals(0.8301785872662097, testee.m_Qy[0], 1e-15);
		assertEquals(-0.3668826991417991, testee.m_Qy[1], 1e-15);
		assertEquals(-0.2790864373533426, testee.m_Qy[2], 1e-15);
		assertEquals(0.3135464220821645, testee.m_Qy[3], 1e-15);
		testee.setAttitude(-0.5170315313926657, -0.3618330389030689, -0.5052351757087615, 0.5886362754116177);
		testee.updateQy(0.0060329438221631, -0.0119560450268350, 0.0132053470655010, 0.0, 0.0, 0.0);
		assertEquals(-0.3457637147069603, testee.m_Qy[0], 1e-15);
		assertEquals(0.2903157038678429, testee.m_Qy[1], 1e-15);
		assertEquals(-0.2542677246050404, testee.m_Qy[2], 1e-15);
		assertEquals(0.8552848472315745, testee.m_Qy[3], 1e-15);

		// Case: Fused angles representation of H with respect to G is in
		// singularity (acc is *exactly* opposite to what we expect to have)
		testee.setAttitude(0.1315050313554053, 0.0628385160278410, 0.5635331902597460, 0.8131347312145174);
		testee.updateQy(0.0460225400289575, -0.9329839804258333, -0.3569633287578298, 0.0, 0.0, 0.0);
		assertEquals(0.0628385160280403, testee.m_Qy[0], 1e-15);
		assertEquals(-0.1315050313552642, testee.m_Qy[1], 1e-15);
		assertEquals(0.8131347312144415, testee.m_Qy[2], 1e-15);
		assertEquals(-0.5635331902598663, testee.m_Qy[3], 1e-15);
		testee.setAttitude(-0.5996819528906139, -0.1391235269691680, 0.7878730512597646, -0.0168004380772317);
		testee.updateQy(-0.9496211724184447, -0.1403865118709524, 0.2801986013156501, 0.0, 0.0, 0.0);
		assertEquals(0.1391235269691764, testee.m_Qy[0], 1e-15);
		assertEquals(-0.5996819528905156, testee.m_Qy[1], 1e-15);
		assertEquals(0.0168004380772349, testee.m_Qy[2], 1e-15);
		assertEquals(0.7878730512598381, testee.m_Qy[3], 1e-15);
		testee.setAttitude(0.7445714456406411, -0.0297643106282695, -0.0256722525065951, -0.6663845613462749);
		testee.updateQy(-0.0778986064867350, 0.0101081261354275, -0.9969100425276831, 0.0, 0.0, 0.0);
		assertEquals(0.0297643106282849, testee.m_Qy[0], 1e-15);
		assertEquals(0.7445714456406466, testee.m_Qy[1], 1e-15);
		assertEquals(0.6663845613462737, testee.m_Qy[2], 1e-15);
		assertEquals(-0.0256722525064528, testee.m_Qy[3], 1e-15);
		testee.setAttitude(-0.5282105669075481, 0.4966906285813930, -0.0198348597853515, -0.6884029305747638);
		testee.updateQy(0.7048005336720857, 0.4974057257937632, -0.5058099956333281, 0.0, 0.0, 0.0);
		assertEquals(-0.4966906285813809, testee.m_Qy[0], 1e-15);
		assertEquals(-0.5282105669075945, testee.m_Qy[1], 1e-15);
		assertEquals(0.6884029305747393, testee.m_Qy[2], 1e-15);
		assertEquals(-0.0198348597852607, testee.m_Qy[3], 1e-15);
		testee.setAttitude(0.1972171024904266, -0.2741338695282224, 0.0214845912999399, 0.9410071457749077);
		testee.updateQy(0.5243981179390351, 0.0676934670115582, -0.8487780678285322, 0.0, 0.0, 0.0);
		assertEquals(-0.2741338695283436, testee.m_Qy[0], 1e-15);
		assertEquals(-0.1972171024904762, testee.m_Qy[1], 1e-15);
		assertEquals(0.9410071457748665, testee.m_Qy[2], 1e-15);
		assertEquals(-0.0214845912997490, testee.m_Qy[3], 1e-15);

		//
		// Test Absolute Fused Yaw Method
		//

		// Change the acc-only resolution method
		testee.resetAll(true);
		testee.setAccMethod(AttitudeEstimator.AccMethodEnum.ME_ABS_FUSED_YAW);

		// Set the current attitude estimate and magnetometer calibration
		testee.setAttitude(1.0, 0.0, 0.0, 0.0);
		testee.setMagCalib(1.0, 0.0, 0.0);

		// Case: Acc is zero
		testee.updateQy(0.0, 0.0, 0.0, 1.0, 0.0, 0.0);
		assertEquals(1.0, testee.m_Qy[0], 1e-15);
		assertEquals(0.0, testee.m_Qy[1], 1e-15);
		assertEquals(0.0, testee.m_Qy[2], 1e-15);
		assertEquals(0.0, testee.m_Qy[3], 1e-15);

		// Case: Acc and mag are both valid and unambiguous (m_Qhat should be
		// irrelevant)
		testee.updateQy(0.0, 0.0, 5.0, 3.0, 0.0, 0.0);
		assertEquals(1.0, testee.m_Qy[0], 1e-15);
		assertEquals(0.0, testee.m_Qy[1], 1e-15);
		assertEquals(0.0, testee.m_Qy[2], 1e-15);
		assertEquals(0.0, testee.m_Qy[3], 1e-15);
		testee.updateQy(-0.2, 0.4, 1.0, -1.2, 0.6, -0.7);
		assertEquals(-0.261154630414682, testee.m_Qy[0], 1e-15);
		assertEquals(-0.139805215990429, testee.m_Qy[1], 1e-15);
		assertEquals(0.154980763303808, testee.m_Qy[2], 1e-15);
		assertEquals(0.942461523671184, testee.m_Qy[3], 1e-15);
		testee.setMagCalib(0.5, 1.3, -0.8);
		testee.updateQy(1.97, -4.93, -2.18, 0.65, -2.37, 1.14);
		assertEquals(0.5031410611323102, testee.m_Qy[0], 1e-15);
		assertEquals(-0.8289865209174649, testee.m_Qy[1], 1e-15);
		assertEquals(0.0519991639383328, testee.m_Qy[2], 1e-15);
		assertEquals(-0.2385927653754926, testee.m_Qy[3], 1e-15);

		// Prevent mag from being used in the subsequent tests
		testee.setMagCalib(0.0, 0.0, 0.0);

		// Case: Match the fused yaw of Qy to the fused yaw of Qhat
		testee.setAttitude(0.0593498238343210, -0.3684008345914626, -0.6950113107487008, -0.6145874237360555);
		testee.updateQy(0.0653130869941388, 0.1408317758534581, 0.1502788389860246, 0.0, 0.0, 0.0);
		assertEquals(0.0885030635828862, testee.m_Qy[0], 1e-15);
		assertEquals(-0.1293711432148016, testee.m_Qy[1], 1e-15);
		assertEquals(-0.3680984596463857, testee.m_Qy[2], 1e-15);
		assertEquals(-0.9164790445207663, testee.m_Qy[3], 1e-15);
		testee.setAttitude(0.2985621434353671, 0.4008598917686713, -0.6370341556565788, 0.5868215045526177);
		testee.updateQy(-0.7511787061021573, -0.0191764762381048, -1.2509137048592693, 0.0, 0.0, 0.0);
		assertEquals(0.1211565733914288, testee.m_Qy[0], 1e-15);
		assertEquals(-0.8697464744764664, testee.m_Qy[1], 1e-15);
		assertEquals(0.4149158638592946, testee.m_Qy[2], 1e-15);
		assertEquals(0.2381322758000264, testee.m_Qy[3], 1e-15);
		testee.setAttitude(-0.7985544995138126, 0.3888651411734571, 0.4144559864391484, -0.1982948526592248);
		testee.updateQy(-1.0031569075807398, 0.6182787236419769, 1.3007056332429565, 0.0, 0.0, 0.0);
		assertEquals(-0.9055306095563346, testee.m_Qy[0], 1e-15);
		assertEquals(-0.1093983127730271, testee.m_Qy[1], 1e-15);
		assertEquals(-0.3427605795961903, testee.m_Qy[2], 1e-15);
		assertEquals(-0.2248588654997435, testee.m_Qy[3], 1e-15);

		// Case: Match the fused yaw of Qy to the ZYX yaw of Qhat
		testee.setAttitude(-0.0000000000001761, -0.8298054517748469, -0.5580527862171661, 0.0000000000004423);
		testee.updateQy(-0.3563023186939954, 0.7114454379278298, -0.7365563262840626, 0.0, 0.0, 0.0);
		assertEquals(0.3322763925454150, testee.m_Qy[0], 1e-15);
		assertEquals(0.4508929952701395, testee.m_Qy[1], 1e-15);
		assertEquals(0.7977178922176832, testee.m_Qy[2], 1e-15);
		assertEquals(0.2234593256257253, testee.m_Qy[3], 1e-15);
		testee.setAttitude(0.0000000000004696, 0.6242462264374359, -0.7812276548987634, 0.0000000000003575);
		testee.updateQy(-1.1986018908768623, -0.0602429958299813, -0.6726418006292408, 0.0, 0.0, 0.0);
		assertEquals(0.3155616000244389, testee.m_Qy[0], 1e-15);
		assertEquals(0.6461733136022717, testee.m_Qy[1], 1e-15);
		assertEquals(0.5717704839058757, testee.m_Qy[2], 1e-15);
		assertEquals(-0.3949170028149167, testee.m_Qy[3], 1e-15);
		testee.setAttitude(0.0000000000004138, -0.7882642741276257, 0.6153368460721717, -0.0000000000004642);
		testee.updateQy(-0.4992701502361627, -0.1169565130132472, -0.6190842722316107, 0.0, 0.0, 0.0);
		assertEquals(0.2672406851831245, testee.m_Qy[0], 1e-15);
		assertEquals(0.3944964989331197, testee.m_Qy[1], 1e-15);
		assertEquals(0.8540697200568568, testee.m_Qy[2], 1e-15);
		assertEquals(-0.2086140977843245, testee.m_Qy[3], 1e-15);

		// Case: Match the ZYX yaw of Qy to the fused yaw of Qhat
		testee.setAttitude(0.4255784371869725, -0.7491239509678703, 0.0811700907363579, -0.5011064919332353);
		testee.updateQy(0.0000000000001955, -0.0000000000002696, -1.0000000000000000, 0.0, 0.0, 0.0);
		assertEquals(0.0000000000000128, testee.m_Qy[0], 1e-15);
		assertEquals(-0.6473286269887536, testee.m_Qy[1], 1e-15);
		assertEquals(0.7622110263443157, testee.m_Qy[2], 1e-15);
		assertEquals(-0.0000000000001660, testee.m_Qy[3], 1e-15);
		testee.setAttitude(-0.5477793720826180, -0.7052459255081034, -0.3189083535083264, -0.3175899969104817);
		testee.updateQy(-0.0000000000002232, -0.0000000000001510, -1.0000000000000000, 0.0, 0.0, 0.0);
		assertEquals(-0.0000000000000094, testee.m_Qy[0], 1e-15);
		assertEquals(0.8651148256862977, testee.m_Qy[1], 1e-15);
		assertEquals(0.5015738613382547, testee.m_Qy[2], 1e-15);
		assertEquals(-0.0000000000001344, testee.m_Qy[3], 1e-15);
		testee.setAttitude(-0.1245519154975099, -0.9090227287084450, 0.3439131685051232, 0.1997203834517672);
		testee.updateQy(0.0000000000003709, -0.0000000000002079, -1.0000000000000000, 0.0, 0.0, 0.0);
		assertEquals(-0.0000000000001023, testee.m_Qy[0], 1e-15);
		assertEquals(-0.5291636424714049, testee.m_Qy[1], 1e-15);
		assertEquals(0.8485197932201671, testee.m_Qy[2], 1e-15);
		assertEquals(-0.0000000000001863, testee.m_Qy[3], 1e-15);

		// Case: Match the ZYX yaw of Qy to the ZYX yaw of Qhat
		testee.setAttitude(0.0000000000001691, 0.6285284121279358, 0.7777866257193780, -0.0000000000000190);
		testee.updateQy(-0.0000000000002650, -0.0000000000004164, -1.0000000000000000, 0.0, 0.0, 0.0);
		assertEquals(-0.0000000000000278, testee.m_Qy[0], 1e-15);
		assertEquals(0.6285284121279358, testee.m_Qy[1], 1e-15);
		assertEquals(0.7777866257193780, testee.m_Qy[2], 1e-15);
		assertEquals(-0.0000000000002452, testee.m_Qy[3], 1e-15);
		testee.setAttitude(0.0000000000000094, -0.6993407901302812, 0.7147884017385523, -0.0000000000004479);
		testee.updateQy(-0.0000000000000741, 0.0000000000001827, -1.0000000000000000, 0.0, 0.0, 0.0);
		assertEquals(-0.0000000000000374, testee.m_Qy[0], 1e-15);
		assertEquals(-0.6993407901302812, testee.m_Qy[1], 1e-15);
		assertEquals(0.7147884017385523, testee.m_Qy[2], 1e-15);
		assertEquals(0.0000000000000912, testee.m_Qy[3], 1e-15);
		testee.setAttitude(0.0000000000000062, -0.1197310989733336, -0.9928063577247266, 0.0000000000002385);
		testee.updateQy(-0.0000000000001517, 0.0000000000003649, -1.0000000000000000, 0.0, 0.0, 0.0);
		assertEquals(0.0000000000000971, testee.m_Qy[0], 1e-15);
		assertEquals(0.1197310989733335, testee.m_Qy[1], 1e-15);
		assertEquals(0.9928063577247266, testee.m_Qy[2], 1e-15);
		assertEquals(0.0000000000001720, testee.m_Qy[3], 1e-15);

		//
		// Test ZYX Yaw Method
		//

		// Set the acc-only resolution method
		testee.setAccMethod(AttitudeEstimator.AccMethodEnum.ME_ZYX_YAW);

		// Set the current attitude estimate and magnetometer calibration
		testee.setAttitude(1.0, 0.0, 0.0, 0.0);
		testee.setMagCalib(1.0, 0.0, 0.0);

		// Case: Acc is zero
		testee.updateQy(0.0, 0.0, 0.0, 1.0, 0.0, 0.0);
		assertEquals(1.0, testee.m_Qy[0], 1e-15);
		assertEquals(0.0, testee.m_Qy[1], 1e-15);
		assertEquals(0.0, testee.m_Qy[2], 1e-15);
		assertEquals(0.0, testee.m_Qy[3], 1e-15);

		// Case: Acc and mag are both valid and unambiguous (m_Qhat should be
		// irrelevant)
		testee.updateQy(0.0, 0.0, 5.0, 3.0, 0.0, 0.0);
		assertEquals(1.0, testee.m_Qy[0], 1e-15);
		assertEquals(0.0, testee.m_Qy[1], 1e-15);
		assertEquals(0.0, testee.m_Qy[2], 1e-15);
		assertEquals(0.0, testee.m_Qy[3], 1e-15);
		testee.updateQy(-0.2, 0.4, 1.0, -1.2, 0.6, -0.7);
		assertEquals(-0.261154630414682, testee.m_Qy[0], 1e-15);
		assertEquals(-0.139805215990429, testee.m_Qy[1], 1e-15);
		assertEquals(0.154980763303808, testee.m_Qy[2], 1e-15);
		assertEquals(0.942461523671184, testee.m_Qy[3], 1e-15);
		testee.setMagCalib(0.5, 1.3, -0.8);
		testee.updateQy(1.97, -4.93, -2.18, 0.65, -2.37, 1.14);
		assertEquals(0.5031410611323102, testee.m_Qy[0], 1e-15);
		assertEquals(-0.8289865209174649, testee.m_Qy[1], 1e-15);
		assertEquals(0.0519991639383328, testee.m_Qy[2], 1e-15);
		assertEquals(-0.2385927653754926, testee.m_Qy[3], 1e-15);

		// Case: Mag measurement needs to be discarded (match ZYX yaw)
		testee.setMagCalib(1.0, 1.0, 0.0);
		testee.updateQy(-1.7, 1.3, -0.2, -3.4, 2.6, -0.4); // Acc and mag are
														   // collinear
		assertEquals(0.584556422371574, testee.m_Qy[0], 1e-15);
		assertEquals(0.681365550628110, testee.m_Qy[1], 1e-15);
		assertEquals(0.286819101416163, testee.m_Qy[2], 1e-15);
		assertEquals(-0.334319575472664, testee.m_Qy[3], 1e-15);
		testee.setMagCalib(0.0, 0.0, 0.0); // Magnetometer calibration is zero
		testee.updateQy(-0.9, -3.2, 1.9, 0.3, -0.7, 1.0);
		assertEquals(0.8629533806823695, testee.m_Qy[0], 1e-15);
		assertEquals(-0.4912251049844832, testee.m_Qy[1], 1e-15);
		assertEquals(0.1028632093928026, testee.m_Qy[2], 1e-15);
		assertEquals(0.0585535580068824, testee.m_Qy[3], 1e-15);

		// Case: Mag measurement needs to be discarded (match ZXY yaw)
		testee.setMagCalib(0.0, 0.0, 0.0); // Magnetometer calibration is zero
		testee.setAttitudeEuler(0.5, -0.3, 1.4);
		testee.updateQy(-0.83838664359420356, 0.33705645287673092, -0.42836991422951487, 1.0, 1.0,
				1.0); // Acc is along the global
		// x-axis according to m_Qhat
		assertEquals(0.468159171699882, testee.m_Qy[0], 1e-15);
		assertEquals(0.654665634720466, testee.m_Qy[1], 1e-15);
		assertEquals(0.534413570028688, testee.m_Qy[2], 1e-15);
		assertEquals(-0.258151182136598, testee.m_Qy[3], 1e-15);
		testee.setMagCalib(0.0, 0.0, 0.0); // Magnetometer calibration is zero
		testee.setAttitudeEuler(-1.8, 0.9, 3.1);
		testee.updateQy(-0.257224178522156, -1.785612820648891, 0.250112508119670, 3.0, 2.0,
				1.0); // Acc is along the global x-axis
					  // according to m_Qhat
		assertEquals(0.727313453229958, testee.m_Qy[0], 1e-15);
		assertEquals(-0.602228026219683, testee.m_Qy[1], 1e-15);
		assertEquals(0.262027139930978, testee.m_Qy[2], 1e-15);
		assertEquals(-0.199194184467945, testee.m_Qy[3], 1e-15);

		// Case: Zero ZYX yaw assumption required
		testee.setMagCalib(0.0, 0.0, 0.0);	  // Magnetometer calibration is zero
		testee.m_Qhat[0] = 0.000000000000000; // Note: Normally this would not be
											  // possible as m_Qhat is private
											  // and setAttitude() normalises the
											  // passed quaternion.
		testee.m_Qhat[1] = 0.478828002247735; // This case is only achievable by
											  // breaking our model and having a
											  // non-normalised m_Qhat.
		testee.m_Qhat[2] = 0.232545603493783; // This particular quaternion has
											  // norm 1/sqrt(2) and a zero scalar
											  // component.
		testee.m_Qhat[3] = -0.465452775863626;
		testee.updateQy(-0.256320938468470, -0.124483754175689, 0.249161059424410, 1.0, 2.0,
				3.0); // Acc is along both the global
					  // x-axis and global y-axis
					  // according to m_Qhat
		assertEquals(0.9067313428959584, testee.m_Qy[0], 1e-15);
		assertEquals(-0.2139014942956990, testee.m_Qy[1], 1e-15);
		assertEquals(0.3537249448410367, testee.m_Qy[2], 1e-15);
		assertEquals(0.0834451073782317, testee.m_Qy[3], 1e-15);

		// Case: Zero ZXY yaw assumption required
		testee.setMagCalib(0.0, 0.0, 0.0);	  // Magnetometer calibration is zero
		testee.m_Qhat[0] = 0.000000000000000; // Note: Normally this would not be
											  // possible as m_Qhat is private
											  // and setAttitude() normalises the
											  // passed quaternion.
		testee.m_Qhat[1] = 0.000000000000000; // This case is only achievable by
											  // breaking our model and having a
											  // non-normalised m_Qhat.
		testee.m_Qhat[2] = 0.000000000000000; // This particular quaternion has
											  // norm 1/sqrt(2) and all
											  // components zero except for in
											  // the z coordinate.
		testee.m_Qhat[3] = 0.707106781186547;
		testee.updateQy(-3.4, 0.0, 0.0, 4.0, 5.0, 6.0); // Acc is along the
														// body-fixed x-axis, as
														// well as the global
														// x-axis and global
														// y-axis according to
														// m_Qhat
		assertEquals(0.707106781186547, testee.m_Qy[0], 1e-15);
		assertEquals(0.000000000000000, testee.m_Qy[1], 1e-15);
		assertEquals(0.707106781186548, testee.m_Qy[2], 1e-15);
		assertEquals(0.000000000000000, testee.m_Qy[3], 1e-15);
		testee.setMagCalib(0.0, 0.0, 0.0);	   // Magnetometer calibration is zero
		testee.m_Qhat[0] = 0.000000000000000;  // Note: Normally this would not be
											   // possible as m_Qhat is private
											   // and setAttitude() normalises the
											   // passed quaternion.
		testee.m_Qhat[1] = -0.707106781186547; // This case is only achievable by
											   // breaking our model and having a
											   // non-normalised m_Qhat.
		testee.m_Qhat[2] = 0.000000000000000;  // This particular quaternion has
											   // norm 1/sqrt(2) and all
											   // components zero except for in
											   // the x coordinate.
		testee.m_Qhat[3] = 0.000000000000000;
		testee.updateQy(1.7, 0.0, 0.0, 6.0, 5.0, 4.0); // Acc is along the
													   // body-fixed x-axis, as
													   // well as the global
													   // x-axis and global
													   // y-axis according to
													   // m_Qhat
		assertEquals(0.707106781186547, testee.m_Qy[0], 1e-15);
		assertEquals(0.000000000000000, testee.m_Qy[1], 1e-15);
		assertEquals(-0.707106781186548, testee.m_Qy[2], 1e-15);
		assertEquals(0.000000000000000, testee.m_Qy[3], 1e-15);
	}
}
