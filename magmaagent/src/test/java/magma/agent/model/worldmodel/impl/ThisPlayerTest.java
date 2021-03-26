/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.misc.FuzzyCompare;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Klaus Dorer
 *
 */
public class ThisPlayerTest
{
	private ThisPlayer testee;

	@BeforeEach
	public void setUp()
	{
		testee = new ThisPlayer("testThisPlayer", 0, 0.02f, 0.4f);
		testee.setPosition(new Vector3D(0.0, 0.0, 0.0));
		testee.setGlobalOrientation(Rotation.IDENTITY);
	}

	/**
	 * Test method for
	 * {@link
	 * magma.agent.model.worldmodel.impl.ThisPlayer#calculateGlobalPosition(Vector3D
	 * localPosition)}
	 * .
	 */
	@Test
	public void testCalculateGlobalPositionX()
	{
		Vector3D localPos = new Vector3D(2.0, 0.0, 0.0);
		testee.setPosition(new Vector3D(1.0, 0.0, 0.0));

		Vector3D position = testee.calculateGlobalPosition(localPos);
		testVector(new Vector3D(3.0, 0.0, 0.0), position);
	}

	/**
	 * Test method for
	 * {@link
	 * magma.agent.model.worldmodel.impl.ThisPlayer#calculateGlobalPosition(Vector3D
	 * localPosition)}
	 * .
	 */
	@Test
	public void testCalculateGlobalPositionY()
	{
		Vector3D localPos = new Vector3D(0.0, 2.0, 0.0);
		testee.setPosition(new Vector3D(0.0, 1.0, 0.0));

		Vector3D position = testee.calculateGlobalPosition(localPos);
		testVector(new Vector3D(0.0, 3.0, 0.0), position);
	}

	/**
	 * Test method for
	 * {@link
	 * magma.agent.model.worldmodel.impl.ThisPlayer#calculateGlobalPosition(Vector3D
	 * localPosition)}
	 * .
	 */
	@Test
	public void testCalculateGlobalPositionZ()
	{
		Vector3D localPos = new Vector3D(0.0, 0.0, 2.0);
		testee.setPosition(new Vector3D(0.0, 0.0, 1.0));

		Vector3D position = testee.calculateGlobalPosition(localPos);
		testVector(new Vector3D(0.0, 0.0, 3.0), position);
	}

	/**
	 * Test method for
	 * {@link
	 * magma.agent.model.worldmodel.impl.ThisPlayer#calculateGlobalPosition(Vector3D
	 * localPosition)}
	 * .
	 */
	@Test
	public void testCalculateGlobalPositionXYZ()
	{
		Vector3D localPos = new Vector3D(1.0, 2.0, 3.0);
		testee.setPosition(new Vector3D(4.0, -2.0, 1.0));

		Vector3D position = testee.calculateGlobalPosition(localPos);
		testVector(new Vector3D(5.0, 0.0, 4.0), position);
	}

	/**
	 * Test method for
	 * {@link
	 * magma.agent.model.worldmodel.impl.ThisPlayer#calculateGlobalPosition(Vector3D
	 * localPosition)}
	 * .
	 */
	@Test
	public void testCalculateGlobalPositionRotation90()
	{
		Vector3D localPos = new Vector3D(2.0, 0.0, 0.0);
		testee.setPosition(new Vector3D(0.0, 0.0, 0.0));
		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(90)));

		Vector3D position = testee.calculateGlobalPosition(localPos);
		testVector(new Vector3D(0.0, 2.0, 0.0), position);
	}

	/**
	 * Test method for
	 * {@link
	 * magma.agent.model.worldmodel.impl.ThisPlayer#calculateGlobalPosition(Vector3D
	 * localPosition)}
	 * .
	 */
	@Test
	public void testCalculateGlobalPositionRotation45()
	{
		Vector3D localPos = new Vector3D(2.0, 2.0, 0.0);
		testee.setPosition(new Vector3D(0.0, 0.0, 0.0));
		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(-45.0)));
		double x = 2.0 / Math.cos(Math.toRadians(-45.0));

		Vector3D position = testee.calculateGlobalPosition(localPos);
		testVector(new Vector3D(x, 0.0, 0.0), position);
	}

	/**
	 * Test method for
	 * {@link
	 * magma.agent.model.worldmodel.impl.ThisPlayer#calculateGlobalPosition(Vector3D
	 * localPosition)}
	 * .
	 */
	@Test
	public void testCalculateGlobalPositionRotation45Translation()
	{
		Vector3D localPos = new Vector3D(0.0, Math.sqrt(3 * 3 + 3 * 3), 0.0);
		testee.setPosition(new Vector3D(-3.0, -3.0, 0.0));
		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(-45)));

		Vector3D position = testee.calculateGlobalPosition(localPos);
		testVector(new Vector3D(0.0, 0.0, 0.0), position);
	}

	/**
	 * Compare two vectors using fuzzy comparison
	 *
	 * @param expected Expected vector
	 * @param was Actual vector
	 */
	private void testVector(Vector3D expected, Vector3D was)
	{
		assertTrue(FuzzyCompare.eq(expected, was, 0.00001f), "Expected: (" + expected.getX() + ", " + expected.getY() +
																	 ", " + expected.getZ() + ") was: (" + was.getX() +
																	 ", " + was.getY() + ", " + was.getZ() + ")");
	}

	@Test
	public void testIsInsideArea()
	{
		Area2D.Float area = new Area2D.Float(0, 1, -1, 1);

		testee.setPosition(new Vector3D(2.0, 0.0, 0.0));
		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(90)));

		assertFalse(testee.isInsideArea(new Vector3D(0, 0, 0), area));
		assertFalse(testee.isInsideArea(new Vector3D(2, -0.1, 0), area));
		assertTrue(testee.isInsideArea(new Vector3D(1.01, 0.99, 0), area));
		assertTrue(testee.isInsideArea(new Vector3D(2.9, 0.99, 0), area));
	}

	/**
	 * Test for {@link ThisPlayer#positionIsLeft(Vector3D)}
	 */
	@Test
	public void testIsLeftOf()
	{
		Vector3D position = new Vector3D(0.0, 1.0, 0.0);
		assertTrue(testee.positionIsLeft(position));

		position = new Vector3D(0.0, -1.0, 0.0);
		assertFalse(testee.positionIsLeft(position));

		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(-100)));
		assertTrue(testee.positionIsLeft(position));

		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(100)));
		assertTrue(testee.positionIsLeft(position));

		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(80)));
		assertFalse(testee.positionIsLeft(position));
	}

	/**
	 * Test for {@link ThisPlayer#positionIsRight(Vector3D)}
	 */
	@Test
	public void testIsRightOf()
	{
		Vector3D position = new Vector3D(0.0, 1.0, 0.0);
		assertFalse(testee.positionIsRight(position));

		position = new Vector3D(0.0, -1.0, 0.0);
		assertTrue(testee.positionIsRight(position));

		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(-100)));
		assertFalse(testee.positionIsRight(position));

		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(100)));
		assertFalse(testee.positionIsRight(position));

		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(80)));
		assertTrue(testee.positionIsRight(position));
	}

	/**
	 * Test for {@link ThisPlayer#positionIsRight(Vector3D)}
	 */
	@Test
	public void testIsBehind()
	{
		Vector3D position = new Vector3D(1.0, 0.0, 0.0);
		assertFalse(testee.positionIsBehind(position));

		position = new Vector3D(-1.0, 0.0, 0.0);
		assertTrue(testee.positionIsBehind(position));

		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(-100)));
		assertFalse(testee.positionIsBehind(position));

		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(100)));
		assertFalse(testee.positionIsBehind(position));

		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(80)));
		assertTrue(testee.positionIsBehind(position));
	}
}
