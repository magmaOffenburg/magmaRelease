/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hso.autonomy.util.geometry.Circle2D;
import hso.autonomy.util.geometry.Pose2D;
import magma.util.benchmark.PathParameterWalkBenchmarkItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PathCircleTest
{
	private PathCircle pathCircle, pathCircleInverse;

	@BeforeEach
	public void setUp()
	{
		pathCircle = new PathCircle(new Pose2D(0, -3), new Pose2D(0, 3),
				new PathParameterWalkBenchmarkItem(100, 0, 5, 1, 0.5, 3), new Circle2D(0, 0, 3));
		pathCircleInverse = new PathCircle(new Pose2D(0, 3), new Pose2D(0, -3),
				new PathParameterWalkBenchmarkItem(100, 0, -5, 1, 0.5, 3), new Circle2D(0, 0, 3));
	}

	@Test
	public void stillOnPathTrueTest()
	{
		assertTrue(pathCircle.stillOnPath(new Pose2D(3, 0.5)));
	}

	@Test
	public void stillOnPathInverseTrueTest()
	{
		assertTrue(pathCircleInverse.stillOnPath(new Pose2D(3, 0.5)));
	}

	@Test
	public void stillOnPathFalseDistTest()
	{
		assertFalse(pathCircle.stillOnPath(new Pose2D(3, 1.5)));
	}

	@Test
	public void stillOnPathInverseFalseDistTest()
	{
		assertFalse(pathCircleInverse.stillOnPath(new Pose2D(3, 1.5)));
	}

	@Test
	public void stillOnPathFalseAngleTest()
	{
		assertFalse(pathCircle.stillOnPath(new Pose2D(-6, -0.5)));
	}

	@Test
	public void stillOnPathInverseFalseAngleTest()
	{
		assertFalse(pathCircleInverse.stillOnPath(new Pose2D(-6, -0.5)));
	}

	@Test
	public void getDistance()
	{
		PathCircle pathCircle = new PathCircle(new Pose2D(0, -3), new Pose2D(0, 3),
				new PathParameterWalkBenchmarkItem(0, 0, 0, 0, 0, 3.0), new Circle2D(0, 0, 3));
		assertEquals(9.42, pathCircle.getPathDistance(), 0.1);
	}
}
