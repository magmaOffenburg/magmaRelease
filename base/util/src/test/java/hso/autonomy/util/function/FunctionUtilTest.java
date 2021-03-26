/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.Test;

public class FunctionUtilTest
{
	@Test
	public void testLinearBezierPoint()
	{
		Vector3D p0 = new Vector3D(0, 0, 0);
		Vector3D p1 = new Vector3D(1, 1, 0);

		Vector3D result = FunctionUtil.bezierPoint(p0, p1, 0.0f);
		assertEquals(0.0f, result.getX(), 0.0001);
		assertEquals(0.0f, result.getY(), 0.0001);

		result = FunctionUtil.bezierPoint(p0, p1, 0.5f);
		assertEquals(0.5f, result.getX(), 0.0001);
		assertEquals(0.5f, result.getY(), 0.0001);

		result = FunctionUtil.bezierPoint(p0, p1, 1.0f);
		assertEquals(1.0f, result.getX(), 0.0001);
		assertEquals(1.0f, result.getY(), 0.0001);
	}

	@Test
	public void testQuadraticBezierPoint()
	{
		Vector3D p0 = new Vector3D(0, 0, 0);
		Vector3D p1 = new Vector3D(0, 1, 0);
		Vector3D p2 = new Vector3D(1, 1, 0);

		Vector3D result = FunctionUtil.bezierPoint(p0, p1, p2, 0.0f);
		assertEquals(0.0f, result.getX(), 0.0001);
		assertEquals(0.0f, result.getY(), 0.0001);

		result = FunctionUtil.bezierPoint(p0, p1, p2, 0.5f);
		assertEquals(0.25f, result.getX(), 0.0001);
		assertEquals(0.75f, result.getY(), 0.0001);

		result = FunctionUtil.bezierPoint(p0, p1, p2, 1.0f);
		assertEquals(1.0f, result.getX(), 0.0001);
		assertEquals(1.0f, result.getY(), 0.0001);
	}

	@Test
	public void testCubicBezierPoint()
	{
		Vector3D p0 = new Vector3D(0, 0, 0);
		Vector3D p1 = new Vector3D(1, 1, 0);
		Vector3D p2 = new Vector3D(2, 1, 0);
		Vector3D p3 = new Vector3D(3, 0, 0);

		Vector3D result = FunctionUtil.bezierPoint(p0, p1, p2, p3, 0.0f);
		assertEquals(0.0f, result.getX(), 0.0001);
		assertEquals(0.0f, result.getY(), 0.0001);

		result = FunctionUtil.bezierPoint(p0, p1, p2, p3, 0.5f);
		assertEquals(1.5f, result.getX(), 0.0001);
		assertEquals(0.75f, result.getY(), 0.0001);

		result = FunctionUtil.bezierPoint(p0, p1, p2, p3, 1.0f);
		assertEquals(3.0f, result.getX(), 0.0001);
		assertEquals(0.0f, result.getY(), 0.0001);
	}

	@Test
	public void testCubicBezierPointReal3D()
	{
		Vector3D p0 = new Vector3D(0, 0, 0);
		Vector3D p1 = new Vector3D(1, 1, 2);
		Vector3D p2 = new Vector3D(2, 1, 2);
		Vector3D p3 = new Vector3D(3, 0, 0);

		Vector3D result = FunctionUtil.bezierPoint(p0, p1, p2, p3, 0.5f);
		assertEquals(1.5f, result.getX(), 0.0001);
		assertEquals(0.75f, result.getY(), 0.0001);
		assertEquals(1.5f, result.getZ(), 0.0001);
	}

	@Test
	public void testLinearBezierPoint_BA()
	{
		List<Vector3D> points = new ArrayList<>();
		points.add(new Vector3D(0, 0, 0));
		points.add(new Vector3D(1, 1, 0));

		Vector3D result = FunctionUtil.bezierPointBA3D(points, 0.0f);
		assertEquals(0.0f, result.getX(), 0.0001);
		assertEquals(0.0f, result.getY(), 0.0001);

		result = FunctionUtil.bezierPointBA3D(points, 0.5f);
		assertEquals(0.5f, result.getX(), 0.0001);
		assertEquals(0.5f, result.getY(), 0.0001);

		result = FunctionUtil.bezierPointBA3D(points, 1.0f);
		assertEquals(1.0f, result.getX(), 0.0001);
		assertEquals(1.0f, result.getY(), 0.0001);
	}

	@Test
	public void testQuadraticBezierPoint_BA()
	{
		List<Vector3D> points = new ArrayList<>();
		points.add(new Vector3D(0, 0, 0));
		points.add(new Vector3D(0, 1, 0));
		points.add(new Vector3D(1, 1, 0));

		Vector3D result = FunctionUtil.bezierPointBA3D(points, 0.0f);
		assertEquals(0.0f, result.getX(), 0.0001);
		assertEquals(0.0f, result.getY(), 0.0001);

		result = FunctionUtil.bezierPointBA3D(points, 0.5f);
		assertEquals(0.25f, result.getX(), 0.0001);
		assertEquals(0.75f, result.getY(), 0.0001);

		result = FunctionUtil.bezierPointBA3D(points, 1.0f);
		assertEquals(1.0f, result.getX(), 0.0001);
		assertEquals(1.0f, result.getY(), 0.0001);
	}

	@Test
	public void testCubicBezierPoint_BA()
	{
		List<Vector3D> points = new ArrayList<>();
		points.add(new Vector3D(0, 0, 0));
		points.add(new Vector3D(1, 1, 0));
		points.add(new Vector3D(2, 1, 0));
		points.add(new Vector3D(3, 0, 0));

		Vector3D result = FunctionUtil.bezierPoint3D(points, 0.0f);
		assertEquals(0.0f, result.getX(), 0.0001);
		assertEquals(0.0f, result.getY(), 0.0001);

		result = FunctionUtil.bezierPoint3D(points, 0.5f);
		assertEquals(1.5f, result.getX(), 0.0001);
		assertEquals(0.75f, result.getY(), 0.0001);

		result = FunctionUtil.bezierPoint3D(points, 1.0f);
		assertEquals(3.0f, result.getX(), 0.0001);
		assertEquals(0.0f, result.getY(), 0.0001);
	}

	@Test
	public void testCubicBezierPointReal3D_BA()
	{
		List<Vector3D> points = new ArrayList<>();
		points.add(new Vector3D(0, 0, 0));
		points.add(new Vector3D(1, 1, 2));
		points.add(new Vector3D(2, 1, 2));
		points.add(new Vector3D(3, 0, 0));

		Vector3D result = FunctionUtil.bezierPointBA3D(points, 0.5f);
		assertEquals(1.5f, result.getX(), 0.0001);
		assertEquals(0.75f, result.getY(), 0.0001);
		assertEquals(1.5f, result.getZ(), 0.0001);
	}
}
