/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer.impl;

import hso.autonomy.agent.model.worldmodel.localizer.IFeatureMap;
import hso.autonomy.agent.model.worldmodel.localizer.ILineFeatureObservation;
import hso.autonomy.agent.model.worldmodel.localizer.IPointCorrespondence;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeatureObservation;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.geometry.orientationFilter.IOrientationFilter;
import hso.autonomy.util.geometry.positionFilter.IPositionFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Implementation of the ILocalizer interface that uses triangulation for localization.
 *
 * @author Klaus Dorer
 */
public class LocalizerTriangulation extends SimpleLocalizerBase
{
	public LocalizerTriangulation()
	{
		this(null);
	}

	public LocalizerTriangulation(IPositionFilter positionFilter)
	{
		this(positionFilter, null);
	}

	public LocalizerTriangulation(IPositionFilter positionFilter, IOrientationFilter orientationFilter)
	{
		super(positionFilter, orientationFilter);
	}

	/**
	 * Calculates absolute position and directions by performing triangulation
	 * for all pairs of flags taking the average of all calculations.
	 * @return an array of two Vector3Ds, the first containing the absolute x,y,z
	 *         position of the viewer on the field, the second containing no real
	 *         vector, but the horizontal, latitudal and rotational absolute body
	 *         angles of the viewer
	 */
	@Override
	public IPose3D localize(IFeatureMap map, List<IPointFeatureObservation> pointObservations,
			List<ILineFeatureObservation> lineObservations, Rotation orientationEstimation)
	{
		IPose2D pose = localize2D(map, pointObservations, lineObservations, orientationEstimation);

		if (pose != null) {
			return new Pose3D(pose);
		}

		return null;
	}

	/**
	 * Calculates absolute position and directions by performing triangulation
	 * for all pairs of flags taking the average of all calculations.
	 * @return an array of two Vector3Ds, the first containing the absolute x,y,z
	 *         position of the viewer on the field, the second containing no real
	 *         vector, but the horizontal, latitudal and rotational absolute body
	 *         angles of the viewer
	 */
	public static IPose2D localize2D(IFeatureMap map, List<IPointFeatureObservation> pointObservations,
			List<ILineFeatureObservation> lineObservations, Rotation orientationEstimation)
	{
		List<IPointCorrespondence> pointRefs = new ArrayList<IPointCorrespondence>();
		LocalizerUtil.extractPointFeatureCorrespondences(pointRefs, map.getPointFeatures(), pointObservations);

		if (pointRefs.size() < 2) {
			// there are not 2 visible flags
			return null;
		}

		// sort the flags according to their angle
		Collections.sort(pointRefs, HorizontalAngleComparator.INSTANCE);

		// get the two flags with maximum angle difference
		int triangulations = pointRefs.size() * (pointRefs.size() - 1) / 2;
		assert triangulations > 0 : "we need at least one triangulation";

		List<Pose2D> results = new ArrayList<Pose2D>();

		for (int i = 0; i < pointRefs.size() - 1; i++) {
			IPointCorrespondence flag1 = pointRefs.get(i);
			for (int j = i + 1; j < pointRefs.size(); j++) {
				IPointCorrespondence flag2 = pointRefs.get(j);
				Pose2D res = triangulate(flag1, flag2);
				if (res != null) {
					results.add(res);
				}
			}
		}

		if (results.size() > 0) {
			Pose2D[] results2D = new Pose2D[results.size()];
			for (int i = 0; i < results.size(); i++) {
				results2D[i] = results.get(i);
			}

			return Pose2D.average(results2D);
		} else {
			return null;
		}
	}

	/**
	 * Calculates absolute position and directions from the two flags passed
	 * using triangulation. Absolute means with respect to the game fields
	 * coordinate system.
	 * @param flag1 first visible landmark with known position
	 * @param flag2 second visible landmark with known position (has to be right
	 *        of first)
	 * @return an array of two Vector3Ds, the first containing the absolute x,y,z
	 *         position of the viewer on the field, the second containing no real
	 *         vector, but the horizontal, latitudal and rotational absolute body
	 *         angles of the viewer
	 */
	public static Pose2D triangulate(IPointCorrespondence flag1, IPointCorrespondence flag2)
	{
		float flag1Direction;
		float flag2Direction;
		double r1, r2;		// the distance of the player to the flags
		double dist, dist2; // the distance (square) of the two flags
		double a;			// distance from one flag to intersection
		// point P3
		double h; // distance from P3 to the intersection
		// Points P1 and P2 of the two circles
		double x, x1, x2, x3;
		double y, y1, y2, y3;
		double ratio;
		Angle horizontalAbsDirection;
		float beta;

		// do the calculations
		flag1Direction = (float) flag1.getObservedPosition().getAlpha();
		flag2Direction = (float) flag2.getObservedPosition().getAlpha();

		r1 = flag1.getObservedPosition().getNorm();
		r2 = flag2.getObservedPosition().getNorm();

		x1 = flag1.getKnownPosition().getX();
		x2 = flag2.getKnownPosition().getX();

		y1 = flag1.getKnownPosition().getY();
		y2 = flag2.getKnownPosition().getY();

		// calculate the square distance of the two flags
		dist2 = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
		dist = Math.sqrt(dist2);
		if (dist > r1 + r2) {
			// the circles would not intersect
			dist = r1 + r2;
			dist2 = dist * dist;
		} else if ((r1 > r2) && (dist + r2 < r1)) {
			// the circles would not intersect
			dist = r1 - r2;
			dist2 = dist * dist;
		} else if ((r2 > r1) && (dist + r1 < r2)) {
			// the circles would not intersect
			dist = r2 - r1;
			dist2 = dist * dist;
		}

		r1 *= r1;
		r2 *= r2;

		// a = (r1^2 - r2^2 + d^2 ) / (2 d)
		// a = distance from flag1 to base point of height line
		a = (r1 - r2 + dist2) / (2.0 * dist);

		// h^2 = r1^2 - a^2
		// h = height of the triangle flag1, flag2 and my position
		h = r1 - a * a;
		if (h < 0.0)
			h = 0.0;
		else
			h = Math.sqrt(h);

		// calculate middle of intersection line
		// P3 = P1 + a ( P2 - P1 ) / d
		x3 = x1 + a * (x2 - x1) / dist;
		y3 = y1 + a * (y2 - y1) / dist;

		// two circles intersect usually in 2 points. Find out which one to
		// select
		if (flag1Direction > flag2Direction) {
			// result x = x3 + h ( y2 - y1 ) / d
			x = x3 + h * (y2 - y1) / dist;
			// result y = y3 - h ( x2 - x1 ) / d
			y = y3 - h * (x2 - x1) / dist;
		} else {
			x = x3 - h * (y2 - y1) / dist;
			y = y3 + h * (x2 - x1) / dist;
		}

		// TODO: add z position calculation
		Vector3D position = new Vector3D(x, y, 0);

		// calculate the absolute direction
		r1 = flag1.getObservedPosition().getNorm();
		ratio = (y1 - y) / r1;
		beta = (float) Math.asin(ratio);
		if (beta != beta) {
			// catches NaN case of asin
			return null;
		}
		if (x > x1) {
			horizontalAbsDirection = Angle.ANGLE_180.subtract(beta).subtract(flag1Direction);
		} else {
			horizontalAbsDirection = Angle.rad(beta).subtract(flag1Direction);
		}

		// TODO add vertical direction
		return new Pose2D(position, horizontalAbsDirection);
	}

	/**
	 * Comparator implementation for sorting point correspondences based on their observed horizontal angle.
	 *
	 * @author Stefan Glaser
	 */
	private static class HorizontalAngleComparator implements Comparator<IPointCorrespondence>
	{
		public static final HorizontalAngleComparator INSTANCE = new HorizontalAngleComparator();

		@Override
		public int compare(IPointCorrespondence o1, IPointCorrespondence o2)
		{
			double alpha1 = o1.getObservedPosition().getAlpha();
			double alpha2 = o2.getObservedPosition().getAlpha();

			if (alpha1 < alpha2) {
				return 1;
			} else if (alpha1 > alpha2) {
				return -1;
			}

			return 0;
		}
	}
}
