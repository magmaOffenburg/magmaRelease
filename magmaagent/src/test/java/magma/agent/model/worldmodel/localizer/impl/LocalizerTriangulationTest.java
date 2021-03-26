/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel.localizer.impl;

import static java.lang.Math.toRadians;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import hso.autonomy.agent.model.worldmodel.localizer.ILocalizationInfo;
import hso.autonomy.agent.model.worldmodel.localizer.impl.LocalizerTriangulation;
import hso.autonomy.util.misc.FuzzyCompare;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link LocalizerTriangulation}
 *
 * @author Klaus Dorer, Stefan Glaser
 */
public class LocalizerTriangulationTest extends LocalizerBaseTest
{
	@Override
	@BeforeEach
	public void setUp()
	{
		super.setUp();
		testee = new LocalizerTriangulation();
	}

	/**
	 * Test for
	 * {@link LocalizerTriangulation#localize(java.util.HashMap, float, float, magma.agent.agentmodel.IGyroRate)}
	 */
	@Test
	public void testLocalize()
	{
		Vector3D flagVecF1R = new Vector3D(7.21110, new Vector3D(toRadians(33.6900), 0));
		when(F1RObs.getObservedPosition()).thenReturn(flagVecF1R);

		Vector3D flagVecF2R = new Vector3D(7.21110, new Vector3D(toRadians(-33.6900), 0));
		when(F2RObs.getObservedPosition()).thenReturn(flagVecF2R);

		pointObservations.add(F1RObs);
		pointObservations.add(F2RObs);

		assertTrue(testee.correct(0, map, pointObservations, lineObservations, null, 0));
		ILocalizationInfo localizeInfo = testee.getState();
		assertTrue(FuzzyCompare.eq(new Vector3D(0, 0, 0), localizeInfo.getLocalizedPosition(), 0.001f));
	}

	/**
	 * Test for
	 * {@link LocalizerTriangulation#localize(java.util.HashMap, float, float, magma.agent.agentmodel.IGyroRate)}
	 */
	@Test
	public void testLocalizeMoreFlagsRight()
	{
		Vector3D flagVecF1R = new Vector3D(7.21110, new Vector3D(toRadians(33.6900), 0));
		when(F1RObs.getObservedPosition()).thenReturn(flagVecF1R);

		Vector3D flagVecF2R = new Vector3D(7.21110, new Vector3D(toRadians(-33.6900), 0));
		when(F2RObs.getObservedPosition()).thenReturn(flagVecF2R);

		Vector3D flagVecG1R = new Vector3D(6.22, new Vector3D(toRadians(15.94), 0));
		when(G1RObs.getObservedPosition()).thenReturn(flagVecG1R);

		Vector3D flagVecG2R = new Vector3D(6.22, new Vector3D(toRadians(-15.94), 0));
		when(G2RObs.getObservedPosition()).thenReturn(flagVecG2R);

		// player is at position -4,-1 looking 45 deg
		pointObservations.add(G1RObs);
		pointObservations.add(G2RObs);
		pointObservations.add(F1RObs);
		pointObservations.add(F2RObs);

		assertTrue(testee.correct(0, map, pointObservations, lineObservations, null, 0));
		ILocalizationInfo localizeInfo = testee.getState();
		assertTrue(FuzzyCompare.eq(new Vector3D(-0.149, -0.001, 0), localizeInfo.getLocalizedPosition(), 0.01f));
		// assertEquals(-1.592629, localizeInfo.getOrientationX().degrees(),
		// 0.001);
	}

	/**
	 * Test for
	 * {@link LocalizerTriangulation#localize(java.util.HashMap, float, float, magma.agent.agentmodel.IGyroRate)}
	 */
	@Test
	public void testLocalizeMoreFlagsLeft()
	{
		Vector3D flagVecF1L = new Vector3D(7.21110, new Vector3D(toRadians(-33.6900), 0));
		when(F1LObs.getObservedPosition()).thenReturn(flagVecF1L);

		Vector3D flagVecF2L = new Vector3D(7.21110, new Vector3D(toRadians(33.6900), 0));
		when(F2LObs.getObservedPosition()).thenReturn(flagVecF2L);

		Vector3D flagVecG1L = new Vector3D(6.08, new Vector3D(toRadians(-15.94), 0));
		when(G1LObs.getObservedPosition()).thenReturn(flagVecG1L);

		Vector3D flagVecG2L = new Vector3D(6.08, new Vector3D(toRadians(15.94), 0));
		when(G2LObs.getObservedPosition()).thenReturn(flagVecG2L);

		pointObservations.add(G1LObs);
		pointObservations.add(G2LObs);
		pointObservations.add(F1LObs);
		pointObservations.add(F2LObs);

		assertTrue(testee.correct(0, map, pointObservations, lineObservations, null, 0));
		ILocalizationInfo localizeInfo = testee.getState();
		assertTrue(FuzzyCompare.eq(new Vector3D(0.0336, 0.0, 0), localizeInfo.getLocalizedPosition(), 0.01f));
		// assertEquals(3.1144, localizeInfo.getOrientationX().radians(), 0.001);
	}

	/**
	 * Test for
	 * {@link LocalizerTriangulation#localize(java.util.HashMap, float, float, magma.agent.agentmodel.IGyroRate)}
	 */
	@Test
	public void testLocalizeTurned()
	{
		Vector3D flagVecF1L = new Vector3D(5.385164, new Vector3D(1.16590, 0));
		when(F1LObs.getObservedPosition()).thenReturn(flagVecF1L);

		Vector3D flagVecF1R = new Vector3D(11.18034, new Vector3D(-0.32175, 0));
		when(F1RObs.getObservedPosition()).thenReturn(flagVecF1R);

		pointObservations.add(F1LObs);
		pointObservations.add(F1RObs);

		assertTrue(testee.correct(0, map, pointObservations, lineObservations, null, 0));
		ILocalizationInfo localizeInfo = testee.getState();
		assertTrue(FuzzyCompare.eq(new Vector3D(-4.0, -1.0, 0), localizeInfo.getLocalizedPosition(), 0.001f));
		// assertEquals(45, localizeInfo.getOrientationX().degrees(), 0.001);
	}

	/**
	 * Test for
	 * {@link LocalizerTriangulation#localize(java.util.HashMap, float, float, magma.agent.agentmodel.IGyroRate)}
	 */
	@Test
	public void testLocalizeGoalPosts()
	{
		Vector3D flagVecG1R = new Vector3D(6.22, new Vector3D(toRadians(15.94), 0));
		when(G1RObs.getObservedPosition()).thenReturn(flagVecG1R);

		Vector3D flagVecF2R = new Vector3D(7.35, new Vector3D(toRadians(-23.67), 0));
		when(F2RObs.getObservedPosition()).thenReturn(flagVecF2R);

		pointObservations.add(G1RObs);
		pointObservations.add(F2RObs);

		assertTrue(testee.correct(0, map, pointObservations, lineObservations, null, 0));
		ILocalizationInfo localizeInfo = testee.getState();
		assertTrue(FuzzyCompare.eq(new Vector3D(-0.178, -0.0187, 0), localizeInfo.getLocalizedPosition(), 0.01f));
		// assertEquals(-0.162, localizeInfo.getOrientationX().radians(), 0.001);
	}

	/**
	 * Special test case for a bug
	 * {@link LocalizerTriangulation#localize(java.util.HashMap, float, float, magma.agent.agentmodel.IGyroRate)}
	 * that occurred at some time
	 */
	@Test
	public void testLocalizeBug1()
	{
		Vector3D flagVecG2R = new Vector3D(6.22, new Vector3D(toRadians(48.779998), 0));
		when(G2RObs.getObservedPosition()).thenReturn(flagVecG2R);

		Vector3D flagVecF2R = new Vector3D(7.34, new Vector3D(toRadians(21.93000), 0));
		when(F2RObs.getObservedPosition()).thenReturn(flagVecF2R);

		pointObservations.add(G2RObs);
		pointObservations.add(F2RObs);

		assertTrue(testee.correct(0, map, pointObservations, lineObservations, null, 0));
		ILocalizationInfo localizeInfo = testee.getState();
		assertTrue(FuzzyCompare.eq(new Vector3D(-0.185829, -0.0489, 0), localizeInfo.getLocalizedPosition(), 0.01f));
		// assertEquals(-0.95624, localizeInfo.getOrientationX().radians(),
		// 0.001);
	}
}
