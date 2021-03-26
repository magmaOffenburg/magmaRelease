/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author sgrossma
 */
public class Circle2DTest
{
	private Circle2D c1;
	private Circle2D c2;
	private Circle2D testee;

	@BeforeEach
	public void setup()
	{
		testee = new Circle2D(0, 0, 1);
	}

	/**
	 * tests calculating circle by three given points
	 * @throws Exception
	 */
	@Test
	public void createWithThreePointsTest() throws Exception
	{
		assertEquals(new Circle2D(0, 1, 1),
				Circle2D.createWithThreePoints(new Pose2D(), new Pose2D(0, 2), new Pose2D(-1, 1)));
	}

	/**
	 * tests calculating circle by three given negative points
	 * @throws Exception
	 */
	@Test
	public void createWithThreePointsNegativePointsTest() throws Exception
	{
		assertEquals(new Circle2D(-1, -1, 1),
				Circle2D.createWithThreePoints(new Pose2D(-1, 0), new Pose2D(-2, -1), new Pose2D(-1, -2)));
	}

	/**
	 * tests calculating circle by three given negative points
	 */
	// @Test
	// public void createWithThreePointsNotACircleTest() {
	// assertEquals(new Circle2D(-1,-1,1), Circle2D.createWithThreePoints(new
	// Vector3D(-1,-2,0), new Vector3D(-2,-1,0), new Vector3D(-1,-2,0)));
	// }

	@Test
	public void getPointOnCircleDeg90Test()
	{
		Pose2D point = testee.getPointOnCircle(Angle.deg(90));
		assertEquals(0, point.getX(), 0.01);
		assertEquals(1, point.getY(), 0.01);
	}

	@Test
	public void getPointOnCircleDeg180Test()
	{
		Pose2D point = testee.getPointOnCircle(Angle.deg(180));
		assertEquals(-1, point.getX(), 0.01);
		assertEquals(0, point.getY(), 0.01);
	}

	@Test
	public void getPointOnCircleDeg270Test()
	{
		Pose2D point = testee.getPointOnCircle(Angle.deg(270));
		assertEquals(0, point.getX(), 0.01);
		assertEquals(-1, point.getY(), 0.01);
	}

	@Test
	public void getPointOnCircleDeg360Test()
	{
		Pose2D point = testee.getPointOnCircle(Angle.deg(360));
		assertEquals(1, point.getX(), 0.01);
		assertEquals(0, point.getY(), 0.01);
	}

	@Test
	public void getAngleOfPoint1_0Test()
	{
		assertEquals(Angle.deg(0).degrees(), testee.getAngleToPoint(new Pose2D(1, 0)).degrees(), 0.01);
	}

	@Test
	public void getAngleOfPoint0_1Test()
	{
		assertEquals(Angle.deg(90).degrees(), testee.getAngleToPoint(new Pose2D(0, 1)).degrees(), 0.01);
	}

	@Test
	public void getAngleOfPoint1n_0Test()
	{
		assertEquals(Angle.deg(180).degrees(), testee.getAngleToPoint(new Pose2D(-1, 0)).degrees(), 0.01);
	}

	@Test
	public void getAngleOfPoint0_1nTest()
	{
		assertEquals(Angle.deg(270).degrees(), testee.getAngleToPoint(new Pose2D(0, -1)).degrees(), 0.01);
	}

	// @Test(expected = Exception.class)
	// public void getAngleOfPointPointNotOnCircleTest()
	// {
	// new Circle2D(0,0,1).getAngleOfPoint(new Pose2D(2,0));
	// }

	@Test
	public void checkOuterTouchTrueTest()
	{
		scenarioTouchOuter();
		assertTrue(c1.checkOuterTouch(c2));
	}

	@Test
	public void checkOuterTouchFalseTest()
	{
		scenario2CirclesOutOfAll();
		assertFalse(c1.checkOuterTouch(c2));

		scenarioIntersectBeforeMiddle();
		assertFalse(c1.checkOuterTouch(c2));

		scenarioIntersectBehindMiddle();
		assertFalse(c1.checkOuterTouch(c2));

		scenarioTouchInner();
		assertFalse(c1.checkOuterTouch(c2));
	}

	@Test
	public void checkInnerTouchTrueTest()
	{
		scenarioTouchInner();
		assertTrue(c1.checkInnerTouch(c2));
	}

	@Test
	public void checkInnerTouchFalseTest()
	{
		scenario2CirclesOutOfAll();
		assertFalse(c1.checkInnerTouch(c2));

		scenarioIntersectBeforeMiddle();
		assertFalse(c1.checkInnerTouch(c2));

		scenarioIntersectBehindMiddle();
		assertFalse(c1.checkInnerTouch(c2));

		scenarioTouchOuter();
		assertFalse(c1.checkInnerTouch(c2));

		scenario2EqualCircles();
		assertFalse(c1.checkInnerTouch(c2));
	}

	@Test
	public void checkIntersectTrueTest()
	{
		scenarioIntersectBeforeMiddle();
		assertTrue(c1.checkIntersect(c2));

		scenarioIntersectBehindMiddle();
		assertTrue(c1.checkIntersect(c2));
	}

	@Test
	public void checkIntersectFalseTest()
	{
		scenario2CirclesOutOfAll();
		assertFalse(c1.checkIntersect(c2));

		scenarioTouchOuter();
		assertFalse(c1.checkIntersect(c2));

		scenarioTouchInner();
		assertFalse(c1.checkIntersect(c2));

		scenario2EqualCircles();
		assertFalse(c1.checkIntersect(c2));
	}

	@Test
	public void checkInnerCircleTrueTest()
	{
		scenarioInnerCircle();
		assertTrue(c1.checkInnerCircle(c2));

		scenarioInnerCircle2();
		assertTrue(c1.checkInnerCircle(c2));

		scenarioInnerCircle3();
		assertTrue(c1.checkInnerCircle(c2));
	}

	@Test
	public void checkInnerCircleFalseTest()
	{
		scenario2CirclesOutOfAll();
		assertFalse(c1.checkInnerCircle(c2));

		scenarioTouchOuter();
		assertFalse(c1.checkInnerCircle(c2));

		scenarioTouchInner();
		assertFalse(c1.checkInnerCircle(c2));

		scenario2EqualCircles();
		assertFalse(c1.checkInnerCircle(c2));
	}

	@Test
	public void relocateXYTest()
	{
		c1 = new Circle2D(-2, 0, 1);
		c1.relocate(3, 2);
		assertEquals(new Circle2D(3, 2, 1), c1);
	}

	@Test
	public void relocatePoseTest()
	{
		c1 = new Circle2D(-2, 0, 1);
		c1.relocate(new Pose2D(5, 0));
		assertEquals(new Circle2D(5, 0, 1), c1);
	}

	@Test
	public void getDistancePoseTest()
	{
		c1 = new Circle2D(-2, 0, 1);
		c2 = new Circle2D(5, 0, 1);
		assertEquals(7.0, c1.getDistance(c2), 0);
	}

	@Test
	public void getDistanceCircleTest()
	{
		c1 = new Circle2D(-2, 0, 1);
		assertEquals(7.0, c1.getDistance(new Pose2D(5, 0)), 0);
	}

	@Test
	public void equalsTrueTest()
	{
		c1 = new Circle2D(-2, 0, 1);
		c2 = new Circle2D(-2, 0, 1);
		assertEquals(c1, c2);
	}

	@Test
	public void equalsFalseTest()
	{
		c1 = new Circle2D(-2, 1, 1);
		c2 = new Circle2D(-2, 0, 1);
		assertFalse(c1.equals(c2));
	}

	@Test
	public void calculateTangent3_GreaterStartTest()
	{
		Circle2D c1 = new Circle2D(0, -2, 3);
		Circle2D c2 = new Circle2D(5, 0, 1);
		Tangent pS = c1.calculateTangent(c2, 3);

		assertEquals(0, pS.getP1().x, 0.01);
		assertEquals(1, pS.getP1().y, 0.01);
		assertEquals(5, pS.getP2().x, 0.01);
		assertEquals(1, pS.getP2().y, 0.01);
	}

	@Test
	public void calculateTangent1_SmallerStartTest()
	{
		Circle2D c1 = new Circle2D(5, 0, 1);
		Circle2D c2 = new Circle2D(0, -2, 3);
		Tangent pS = c1.calculateTangent(c2, 1);

		assertEquals(5, pS.getP1().x, 0.01);
		assertEquals(1, pS.getP1().y, 0.01);
		assertEquals(0, pS.getP2().x, 0.01);
		assertEquals(1, pS.getP2().y, 0.01);
	}

	@Test
	public void calculateTangent3_90degTest()
	{
		Circle2D c1 = new Circle2D(0, 0, 3);
		Circle2D c2 = new Circle2D(-2, 3, 1);
		Tangent pS = c1.calculateTangent(c2, 3);

		assertEquals(-3, pS.getP1().x, 0.01);
		assertEquals(0, pS.getP1().y, 0.01);
		assertEquals(-3, pS.getP2().x, 0.01);
		assertEquals(3, pS.getP2().y, 0.01);
	}

	@Test
	public void calculateTangent3_180degTest()
	{
		Circle2D c1 = new Circle2D(0, 0, 3);
		Circle2D c2 = new Circle2D(-3, -2, 1);
		Tangent pS = c1.calculateTangent(c2, 3);

		assertEquals(0, pS.getP1().x, 0.01);
		assertEquals(-3, pS.getP1().y, 0.01);
		assertEquals(-3, pS.getP2().x, 0.01);
		assertEquals(-3, pS.getP2().y, 0.01);
	}

	@Test
	public void calculateTangent3_270degTest()
	{
		Circle2D c1 = new Circle2D(0, 0, 3);
		Circle2D c2 = new Circle2D(2, -2, 1);
		Tangent pS = c1.calculateTangent(c2, 3);

		assertEquals(3, pS.getP1().x, 0.01);
		assertEquals(0, pS.getP1().y, 0.01);
		assertEquals(3, pS.getP2().x, 0.01);
		assertEquals(-2, pS.getP2().y, 0.01);
	}

	@Test
	public void calculateTangent1_GreaterStartTest()
	{
		Circle2D c1 = new Circle2D(0, -2, 3);
		Circle2D c2 = new Circle2D(5, -4, 1);
		Tangent pS = c1.calculateTangent(c2, 1);

		assertEquals(0, pS.getP1().x, 0.01);
		assertEquals(-5, pS.getP1().y, 0.01);
		assertEquals(5, pS.getP2().x, 0.01);
		assertEquals(-5, pS.getP2().y, 0.01);
	}

	@Test
	public void calculateTangent3_SmallerStartTest()
	{
		Circle2D c1 = new Circle2D(5, -4, 1);
		Circle2D c2 = new Circle2D(0, -2, 3);
		Tangent pS = c1.calculateTangent(c2, 3);

		assertEquals(5, pS.getP1().x, 0.01);
		assertEquals(-5, pS.getP1().y, 0.01);
		assertEquals(0, pS.getP2().x, 0.01);
		assertEquals(-5, pS.getP2().y, 0.01);
	}

	@Test
	public void getArcHPLPLeft()
	{
		assertEquals(180, Circle2D.getArc(180, 0, false).degreesPositive(), 0.1);
	}

	@Test
	public void getArcLPHPLeft()
	{
		assertEquals(90, Circle2D.getArc(90, 180, false).degreesPositive(), 0.1);
	}

	@Test
	public void getArcHPLPRight()
	{
		assertEquals(180, Circle2D.getArc(180, 0, true).degreesPositive(), 0.1);
	}

	@Test
	public void getArcLPHPRight()
	{
		assertEquals(270, Circle2D.getArc(90, 180, true).degreesPositive(), 0.1);
	}

	@Test
	public void getArcLPHPRight350()
	{
		assertEquals(350, Circle2D.getArc(270, 260, false).degreesPositive(), 0.1);
	}

	@Test
	public void testGetLineIntersectionPoint()
	{
		assertEquals(0, testee.getLineSegmentIntersectionPoint(new Vector2D(-2, 2), new Vector2D(2, 2)).size());
		assertEquals(
				0, testee.getLineSegmentIntersectionPoint(new Vector2D(-0.5, 0.5), new Vector2D(0.5, -0.5)).size());

		List<Vector2D> result = testee.getLineSegmentIntersectionPoint(new Vector2D(-1, 1), new Vector2D(1, 1));
		assertEquals(1, result.size());
		assertEquals(0, result.get(0).getX(), 0.0001);
		assertEquals(1, result.get(0).getY(), 0.0001);

		result = testee.getLineSegmentIntersectionPoint(new Vector2D(0, 0), new Vector2D(1, 0));
		assertEquals(1, result.size());
		assertEquals(1, result.get(0).getX(), 0.0001);
		assertEquals(0, result.get(0).getY(), 0.0001);

		result = testee.getLineSegmentIntersectionPoint(new Vector2D(-1, 1), new Vector2D(1, -1));
		assertEquals(2, result.size());
		assertEquals(-0.7071, result.get(0).getX(), 0.001);
		assertEquals(0.7071, result.get(0).getY(), 0.0001);
		assertEquals(0.7071, result.get(1).getX(), 0.001);
		assertEquals(-0.7071, result.get(1).getY(), 0.0001);
	}

	private void scenario2CirclesOutOfAll()
	{
		c1 = new Circle2D(-2, 0, 1);
		c2 = new Circle2D(2, 0, 1);
	}

	private void scenarioIntersectBeforeMiddle()
	{
		c1 = new Circle2D(-2, 0, 2);
		c2 = new Circle2D(1, 0, 2);
	}

	private void scenarioIntersectBehindMiddle()
	{
		c1 = new Circle2D(-1, 0, 3);
		c2 = new Circle2D(1, 0, 3);
	}

	private void scenarioTouchOuter()
	{
		c1 = new Circle2D(1, 0, 1);
		c2 = new Circle2D(-1, 0, 1);
	}

	private void scenarioTouchInner()
	{
		c1 = new Circle2D(0, 0, 4);
		c2 = new Circle2D(3, 0, 1);
	}

	private void scenario2EqualCircles()
	{
		c1 = new Circle2D(0, 0, 1);
		c2 = new Circle2D(0, 0, 1);
	}

	private void scenarioInnerCircle()
	{
		c1 = new Circle2D(0, 0, 10);
		c2 = new Circle2D(2, 0, 1);
	}

	private void scenarioInnerCircle2()
	{
		c1 = new Circle2D(0, 0, 10);
		c2 = new Circle2D(0, 0, 1);
	}

	private void scenarioInnerCircle3()
	{
		c1 = new Circle2D(0, 0, 10);
		c2 = new Circle2D(1, 0, 4);
	}
}
