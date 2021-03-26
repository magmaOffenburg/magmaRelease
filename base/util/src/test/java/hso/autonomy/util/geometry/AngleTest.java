/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * @author Klaus Dorer
 *
 */
public class AngleTest
{
	/**
	 * Test method for {@link hso.autonomy.util.geometry.Angle#degrees()}.
	 */
	@Test
	public void testNormalize()
	{
		assertEquals(-90.0, Angle.deg(270).degrees(), 0.0001);
		assertEquals(-170.0, Angle.deg(190).degrees(), 0.0001);
		assertEquals(170.0, Angle.deg(-190).degrees(), 0.0001);
		assertEquals(-170.0, Angle.deg(-170).degrees(), 0.0001);
		assertEquals(-180.0, Angle.deg(-180).degrees(), 0.0001);
		assertEquals(-180.0, Angle.deg(180).degrees(), 0.0001);
		assertEquals(10.0, Angle.deg(370).degrees(), 0.0001);
		assertEquals(-10.0, Angle.deg(-370).degrees(), 0.0001);
	}

	/**
	 * Test method for {@link hso.autonomy.util.geometry.Angle#degrees()}.
	 */
	@Test
	public void testDegreesPositive()
	{
		assertEquals(270.0, Angle.deg(-90).degreesPositive(), 0.0001);
		assertEquals(90.0, Angle.deg(90).degreesPositive(), 0.0001);
		assertEquals(0.0, Angle.deg(0).degreesPositive(), 0.0001);
		assertEquals(180.0, Angle.deg(180).degreesPositive(), 0.0001);
	}

	/**
	 * Test method for {@link hso.autonomy.util.geometry.Angle#add(Angle)}.
	 */
	@Test
	public void testAdd()
	{
		assertEquals(5.0, Angle.deg(2).add(Angle.deg(3)).degrees(), 0.0001);
		assertEquals(-160.0, Angle.deg(100).add(Angle.deg(100)).degrees(), 0.0001);
		assertEquals(14.422, Angle.deg(2).add(6.5).degrees(), 0.001);
	}

	/**
	 * Test method for {@link hso.autonomy.util.geometry.Angle#subtract(Angle)}.
	 */
	@Test
	public void testSubtract()
	{
		assertEquals(-1.0, Angle.deg(2).subtract(Angle.deg(3)).degrees(), 0.0001);
		assertEquals(160.0, Angle.deg(-100).subtract(Angle.deg(100)).degrees(), 0.0001);
	}

	/**
	 * Test method for {@link hso.autonomy.util.geometry.Angle#isLeftOf(Angle)}.
	 */
	@Test
	public void testIsLeftOf()
	{
		assertTrue(Angle.deg(10).isLeftOf(Angle.deg(-10)));
		assertFalse(Angle.deg(90).isLeftOf(Angle.deg(-100)));
		assertTrue(Angle.deg(-100).isLeftOf(Angle.deg(100)));
		assertFalse(Angle.deg(100).isLeftOf(Angle.deg(-100)));
	}

	/**
	 * Test method for {@link hso.autonomy.util.geometry.Angle#isRightOf(Angle)}
	 */
	@Test
	public void testIsRightOf()
	{
		assertTrue(Angle.deg(-10).isRightOf(Angle.deg(10)));
		assertFalse(Angle.deg(-100).isRightOf(Angle.deg(90)));
		assertTrue(Angle.deg(100).isRightOf(Angle.deg(-100)));
		assertFalse(Angle.deg(-100).isRightOf(Angle.deg(81)));
	}

	@Test
	public void testIsBetween()
	{
		assertTrue(Angle.deg(0).isBetween(Angle.deg(0), Angle.deg(20)));
		assertTrue(Angle.deg(10).isBetween(Angle.deg(0), Angle.deg(20)));
		assertFalse(Angle.deg(20).isBetween(Angle.deg(0), Angle.deg(20)));

		assertFalse(Angle.deg(20).isBetween(Angle.deg(30), Angle.deg(-20)));
		assertFalse(Angle.deg(160).isBetween(Angle.deg(170), Angle.deg(-170)));
		assertTrue(Angle.deg(175).isBetween(Angle.deg(170), Angle.deg(-170)));
		assertTrue(Angle.deg(-175).isBetween(Angle.deg(170), Angle.deg(-170)));
	}

	/**
	 * Test method for {@link hso.autonomy.util.geometry.Angle#average(Angle[])}.
	 */
	@Test
	public void testAveragePositive()
	{
		// Simple case: All angles positive
		Angle[] angles = new Angle[3];
		angles[0] = Angle.deg(9.9);
		angles[1] = Angle.deg(20.0);
		angles[2] = Angle.deg(30.1);

		assertEquals(Angle.average(angles).degrees(), 20.00, 0.0001);
	}

	/**
	 * Test method for {@link hso.autonomy.util.geometry.Angle#average(Angle[])}.
	 */
	@Test
	public void testAverageNegative()
	{
		// Simple case: All angles negative
		Angle[] angles = new Angle[3];
		angles[0] = Angle.deg(-9.9);
		angles[1] = Angle.deg(-20.0);
		angles[2] = Angle.deg(-30.1);

		assertEquals(-20.00, Angle.average(angles).degrees(), 0.0001);
	}

	/**
	 * Test method for {@link hso.autonomy.util.geometry.Angle#average(Angle[])}.
	 */
	@Test
	public void testAverageZeroVector()
	{
		Angle[] angles = new Angle[2];
		angles[0] = Angle.deg(90.0);
		angles[1] = Angle.deg(-90.0);
		assertEquals(0.0, Angle.average(angles).degrees(), 0.0001);

		// angles = new Angle[2];
		// angles[0] = Angle.deg(0.0);
		// angles[1] = Angle.deg(180.0);
		// assertEquals(0.0, Angle.average(angles).degrees(), 0.0001);
	}

	/**
	 * Test method for {@link hso.autonomy.util.geometry.Angle#average(Angle[])}.
	 */
	@Test
	public void testAverageMixed()
	{
		Angle[] angles = new Angle[2];
		angles[0] = Angle.deg(5);
		angles[1] = Angle.deg(15);
		assertEquals(10.0, Angle.average(angles).degrees(), 0.0001);

		angles = new Angle[3];
		angles[0] = Angle.deg(10);
		angles[1] = Angle.deg(10);
		angles[2] = Angle.deg(13);
		assertEquals(11.0, Angle.average(angles).degrees(), 0.0001);

		angles = new Angle[3];
		angles[0] = Angle.deg(10);
		angles[1] = Angle.deg(10);
		angles[2] = Angle.deg(40);
		assertEquals(20.0, Angle.average(angles).degrees(), 0.0001);

		angles = new Angle[6];
		angles[0] = Angle.deg(10);
		angles[1] = Angle.deg(10);
		angles[2] = Angle.deg(10);
		angles[3] = Angle.deg(10);
		angles[4] = Angle.deg(10);
		angles[5] = Angle.deg(40);
		assertEquals(15.0, Angle.average(angles).degrees(), 0.0001);

		// Harder case: Mixed signs
		angles = new Angle[3];
		angles[0] = Angle.deg(-12.5);
		angles[1] = Angle.deg(10.0);
		angles[2] = Angle.deg(41.3);
		assertEquals(12.9333, Angle.average(angles).degrees(), 0.0001);
	}

	/**
	 * Test method for {@link hso.autonomy.util.geometry.Angle#average(Angle[])}.
	 */
	@Test
	public void testAverageCornerCases()
	{
		// Corner case: More than 90 degrees in both directions
		Angle[] angles = new Angle[3];
		angles[0] = Angle.deg(95.0);
		angles[1] = Angle.deg(-100.0);
		angles[2] = Angle.deg(41.3);

		assertEquals(53.851, Angle.averageArcTan(angles).degrees(), 0.001);

		// Corner case: More than 90 degrees in both directions
		angles = new Angle[3];
		angles[0] = Angle.deg(-95.0);
		angles[1] = Angle.deg(100.0);
		angles[2] = Angle.deg(41.2);

		assertEquals(52.784, Angle.averageArcTan(angles).degrees(), 0.001);
	}

	/**
	 * Test method for {@link hso.autonomy.util.geometry.Angle#equals(Object)}.
	 */
	@Test
	public void testEquals()
	{
		assertEquals(Angle.deg(102.3), Angle.deg(102.3));
		assertEquals(Angle.deg(-12.4), Angle.deg(-12.4));

		assertFalse(Angle.deg(-10.1).equals(Angle.deg(-8.97)));
		assertFalse(Angle.deg(-10.1).equals(Angle.deg(10.3)));
		assertFalse(Angle.deg(24.46).equals(Angle.deg(-1.02)));
		assertFalse(Angle.deg(24.46).equals(Angle.deg(5.0001)));

		assertFalse(Angle.deg(-10.1).equals("12"));
	}

	/**
	 * Test method for {@link hso.autonomy.util.geometry.Angle#toString()}.
	 */
	@Test
	public void testToString()
	{
		assertEquals("1.12\u00b0", Angle.deg(1.1234).toString());
	}

	@Test
	public void testGetAdjacencyAngle()
	{
		assertEquals(10, Angle.deg(170).getAdjacencyAngle().degrees(), 0.001);
		assertEquals(-10, Angle.deg(-170).getAdjacencyAngle().degrees(), 0.001);
		assertEquals(-180, Angle.deg(0).getAdjacencyAngle().degrees(), 0.001);
	}

	@Test
	public void testLimit()
	{
		assertEquals(42, Angle.deg(42).limit(Angle.deg(0), Angle.deg(90)).degrees(), 0.001);
		assertEquals(15, Angle.deg(32).limit(Angle.deg(-25), Angle.deg(15)).degrees(), 0.001);
		assertEquals(-100, Angle.deg(-161).limit(Angle.deg(-100), Angle.deg(100)).degrees(), 0.001);
		assertEquals(-161, Angle.deg(-161).limit(Angle.deg(100), Angle.deg(-100)).degrees(), 0.001);
	}
}
