/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import magma.agent.decision.behavior.complex.path.PathStraight;
import org.junit.jupiter.api.Test;

public class PathStraightTest
{
	@Test
	public void stillOnPathTrueTest()
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, -2), new Pose2D(1, 3));
		assertTrue(pathStraight.stillOnPath(new Pose2D(0, 1.9)));
	}

	@Test
	public void stillOnPathFalseTest()
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, -2), new Pose2D(1, 3));
		assertFalse(pathStraight.stillOnPath(new Pose2D(-3, 1)));
	}

	@Test
	public void getPoseAtDistanceJustXTest() throws Exception
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, 0), new Pose2D(1, 0));
		assertEquals(new Pose2D(-1, 0, Angle.deg(0)), pathStraight.getPoseAtDistance(2));
	}

	@Test
	public void getPoseAtDistance0Test() throws Exception
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, 0), new Pose2D(1, 0));
		assertEquals(new Pose2D(-3, 0, Angle.deg(0)), pathStraight.getPoseAtDistance(0));
	}

	@Test
	public void getPoseAtDistanceFullTest() throws Exception
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, 0), new Pose2D(1, 0));
		assertEquals(new Pose2D(1, 0, Angle.deg(0)), pathStraight.getPoseAtDistance(4));
	}

	@Test
	public void getPoseAtDistanceXYTest() throws Exception
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, -3), new Pose2D(2, 2));
		Pose2D pose = pathStraight.getPoseAtDistance(Math.sqrt(2));
		assertEquals(-2, pose.getX(), 0.01);
		assertEquals(-2, pose.getY(), 0.01);
	}

	@Test
	public void getPoseAtDistanceNegTest() throws Exception
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, 0), new Pose2D(1, 0));
		assertEquals(pathStraight.getStartPoint(), pathStraight.getPoseAtDistance(-4));
	}

	@Test
	public void getPoseAtDistanceOutOfDistanceTest() throws Exception
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, 0), new Pose2D(1, 0));
		assertEquals(pathStraight.getEndPoint(), pathStraight.getPoseAtDistance(99));
	}

	@Test
	public void getDistanceToPathJustXAboveTest()
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, 0), new Pose2D(1, 0));
		assertEquals(5, pathStraight.getDistanceToPath(new Pose2D(-1, 5)), 0.01);
	}

	@Test
	public void getDistanceToPathJustXBelowTest()
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, 0), new Pose2D(1, 0));
		assertEquals(5, pathStraight.getDistanceToPath(new Pose2D(-1, -5)), 0.01);
	}

	@Test
	public void getDistanceToPathXYTest()
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, 0), new Pose2D(1, 0));
		assertEquals(5, pathStraight.getDistanceToPath(new Pose2D(-1, 5)), 0.01);
	}

	@Test
	public void getDistanceToPathBeforeStartTest()
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, 0), new Pose2D(1, 0));
		assertEquals(1, pathStraight.getDistanceToPath(new Pose2D(-4, 0)), 0.01);
	}

	@Test
	public void walkedDistanceOnPathTest()
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, 0), new Pose2D(1, 0));
		assertEquals(1, pathStraight.getWalkedDistance(new Pose2D(-2, 0)), 0.01);
	}

	@Test
	public void walkedDistanceBesidePathTest()
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, 0), new Pose2D(1, 0));
		assertEquals(1, pathStraight.getWalkedDistance(new Pose2D(-2, 2)), 0.01);
	}

	@Test
	public void walkedDistanceOnDiagPathTest()
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, -3), new Pose2D(2, 2));
		assertEquals(Math.sqrt(8), pathStraight.getWalkedDistance(new Pose2D(-1, -1)), 0.01);
	}

	@Test
	public void walkedDistanceBesideDiagPathTest()
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, -3), new Pose2D(2, 2));
		assertEquals(Math.sqrt(8), pathStraight.getWalkedDistance(new Pose2D(-2, 0)), 0.01);
	}

	@Test
	public void walkedDistanceBeforeStartPathTest()
	{
		PathStraight pathStraight = new PathStraight(new Pose2D(-3, -3), new Pose2D(2, 2));
		assertEquals(-Math.sqrt(2), pathStraight.getWalkedDistance(new Pose2D(-5, -3)), 0.01);
	}
}
