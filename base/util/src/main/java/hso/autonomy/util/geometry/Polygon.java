/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Polygon
{
	/** The centroid of the polygon. */
	private Vector2D centroid;

	/** The area of the polygon. */
	private double area;

	/** The polygon corner points (in clockwise order). */
	private List<Vector2D> points;

	public Polygon(Vector2D p1, Vector2D p2, Vector2D p3)
	{
		points = new ArrayList<>();
		points.add(p1);
		points.add(p2);
		points.add(p3);
		init();
	}

	public Polygon(Area2D.Float area)
	{
		points = new ArrayList<>();
		points.add(area.getBottomLeft());
		points.add(area.getBottomRight());
		points.add(area.getTopRight());
		points.add(area.getTopLeft());
		init();
	}

	public Polygon(Vector2D p1, Vector2D p2, Vector2D p3, Vector2D p4)
	{
		points = new ArrayList<>();
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		init();
	}

	public Polygon(List<Vector2D> points)
	{
		this.points = points;
		init();
	}

	private void init()
	{
		centroid = calculateAverage();
		area = calculateArea();
		checkArea();
	}

	public Vector2D getCentroid()
	{
		return centroid;
	}

	public double getArea()
	{
		return area;
	}

	public List<Vector2D> getPoints()
	{
		return points;
	}

	public List<Vector3D> getPoints3D()
	{
		return VectorUtils.to3D(points);
	}

	// https://stackoverflow.com/a/2922778/2631715
	public boolean contains(Vector2D point)
	{
		int i, j;
		boolean c = false;
		for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {
			Vector2D vi = points.get(i);
			Vector2D vj = points.get(j);
			double testX = point.getX();
			double testY = point.getY();
			if (((vi.getY() > testY) != (vj.getY() > testY)) &&
					(testX < (vj.getX() - vi.getX()) * (testY - vi.getY()) / (vj.getY() - vi.getY()) + vi.getX()))
				c = !c;
		}
		return c;
	}

	public double getDistanceTo(Vector2D point)
	{
		int size = points.size();
		if (size == 0) {
			return 0;
		}

		double minDistance = points.get(0).distance(point);
		double distance;
		for (int idx0 = size - 1, idx1 = 0; idx1 < size; idx0 = idx1++) {
			distance = getDistanceToLine(points.get(idx0), points.get(idx1), point);
			if (distance < minDistance) {
				minDistance = distance;
			}
		}

		return minDistance;
	}

	private static double getDistanceToLine(Vector2D lineStart, Vector2D lineEnd, Vector2D point)
	{
		Vector2D v = lineEnd.subtract(lineStart);
		Vector2D w = point.subtract(lineStart);

		double c1 = w.dotProduct(v);

		// c1 <= 0 --> before line-start
		if (c1 > 0) {
			double c2 = v.dotProduct(v);

			if (c2 <= c1) {
				// after line-end
				w = point.subtract(lineEnd);
			} else {
				w = point.subtract(lineStart.add(new Vector2D(v.getX() * (c1 / c2), v.getY() * (c1 / c2))));
			}
		}

		return w.getNorm();
	}

	public Line2D getClosestPolyLine(Vector2D point)
	{
		int size = points.size();
		if (size <= 1) {
			return new Line2D();
		}

		double minDistance = points.get(0).distance(point);
		Vector2D p1 = points.get(size - 1);
		Vector2D p2 = points.get(0);
		double distance;
		for (int idx = 1; idx < size; idx++) {
			distance = getDistanceToLine(points.get(idx - 1), points.get(idx), point);
			if (distance < minDistance) {
				minDistance = distance;
				p1 = points.get(idx - 1);
				p2 = points.get(idx);
			}
		}

		return new Line2D(p1, p2);
	}

	public double getAverageDistance(Polygon other)
	{
		List<Vector2D> otherPoints = other.getPoints();
		if (points.size() == 0 || otherPoints.size() == 0) {
			return 0;
		} else if (points.size() == 1) {
			return other.getDistanceTo(points.get(0));
		} else if (otherPoints.size() == 1) {
			return getDistanceTo(otherPoints.get(0));
		}

		double distanceSum = 0;
		for (Vector2D p : points) {
			distanceSum += other.getDistanceTo(p);
		}

		return distanceSum / points.size();
	}

	// https://stackoverflow.com/a/27309428/2631715
	public boolean intersects(Polygon other)
	{
		for (int x = 0; x < 2; x++) {
			Polygon polygon = (x == 0) ? this : other;

			for (int i1 = 0; i1 < polygon.getPoints().size(); i1++) {
				int i2 = (i1 + 1) % polygon.getPoints().size();
				Vector2D p1 = polygon.getPoints().get(i1);
				Vector2D p2 = polygon.getPoints().get(i2);

				Vector2D normal = new Vector2D(p2.getY() - p1.getY(), p1.getX() - p2.getX());

				double minA = Double.MAX_VALUE;
				double maxA = Double.MIN_VALUE;

				for (Vector2D p : getPoints()) {
					double projected = normal.getX() * p.getX() + normal.getY() * p.getY();

					if (projected < minA) {
						minA = projected;
					}
					if (projected > maxA) {
						maxA = projected;
					}
				}

				double minB = Double.MAX_VALUE;
				double maxB = Double.MIN_VALUE;

				for (Vector2D p : other.getPoints()) {
					double projected = normal.getX() * p.getX() + normal.getY() * p.getY();

					if (projected < minB) {
						minB = projected;
					}
					if (projected > maxB) {
						maxB = projected;
					}
				}

				if (maxA < minB || maxB < minA) {
					return false;
				}
			}
		}

		return true;
	}

	public boolean intersects(Area2D.Float area)
	{
		return intersects(new Polygon(area));
	}

	public Polygon transform(IPose2D transformation)
	{
		List<Vector2D> transformedPoints = new ArrayList<>();
		for (Vector2D point : points) {
			transformedPoints.add(transformation.applyTo(point));
		}
		return new Polygon(transformedPoints);
	}

	// TODO transform Methode fuer IPose3D
	public Polygon transform(IPose3D transformation)
	{
		List<Vector2D> transformedPoints = new ArrayList<>();
		for (Vector2D point : points) {
			transformedPoints.add(VectorUtils.to2D(transformation.applyTo(VectorUtils.to3D(point))));
		}
		return new Polygon(transformedPoints);
	}

	public Area2D.Float getEncompassingArea()
	{
		double minX = Float.POSITIVE_INFINITY;
		double maxX = Float.NEGATIVE_INFINITY;
		double minY = Float.POSITIVE_INFINITY;
		double maxY = Float.NEGATIVE_INFINITY;

		for (Vector2D point : points) {
			double x = point.getX();
			double y = point.getY();

			if (x < minX) {
				minX = x;
			}
			if (x > maxX) {
				maxX = x;
			}
			if (y < minY) {
				minY = y;
			}
			if (y > maxY) {
				maxY = y;
			}
		}

		return new Area2D.Float(minX, maxX, minY, maxY);
	}

	private void checkArea()
	{
		if (area < 0) {
			// Counter clockwise order, thus flip elements
			area = -area;
			Collections.reverse(points);
		}
	}

	private Vector2D calculateAverage()
	{
		if (points.size() == 0) {
			return new Vector2D(0, 0);
		}
		double sumX = 0;
		double sumY = 0;

		for (Vector2D vector2d : points) {
			sumX += vector2d.getX();
			sumY += vector2d.getY();
		}

		return new Vector2D(sumX / points.size(), sumY / points.size());
	}

	private double calculateArea()
	{
		int size = points.size();
		if (size <= 2) {
			return 0;
		}

		double sum = 0;
		// Counter clockwise order is important!
		int i = size - 1;
		sum += points.get(0).getX() * points.get(size - 1).getY();
		sum -= points.get(0).getY() * points.get(size - 1).getX();
		for (; i > 0; i--) {
			sum += points.get(i).getX() * points.get(i - 1).getY();
			sum -= points.get(i).getX() * points.get(i - 1).getY();
		}

		return sum / 2;
	}

	@Override
	public String toString()
	{
		return "(" +
				String.join(", ",
						points.stream().map(p -> p.toString(new DecimalFormat("#.##"))).collect(Collectors.toList())) +
				")";
	}
}
