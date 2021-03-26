/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer.impl;

import hso.autonomy.agent.model.agentmodel.IFieldOfView;
import hso.autonomy.agent.model.worldmodel.localizer.IFeatureMap;
import hso.autonomy.agent.model.worldmodel.localizer.ILineFeature;
import hso.autonomy.agent.model.worldmodel.localizer.ILineFeatureObservation;
import hso.autonomy.agent.model.worldmodel.localizer.IPointCorrespondence;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeature;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeatureObservation;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.VectorUtils;
import hso.autonomy.util.misc.FuzzyCompare;
import java.security.InvalidParameterException;
import java.util.*;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

/**
 * This class provides several util functions for localization.
 *
 * @author Stefan Glaser
 */
public class LocalizerUtil
{
	/**
	 * Calculates a normal vector to the plane defined by the point set. To do
	 * so, at least 3 points on the plane has to be passed.<br>
	 * This method performs an SVD of the covariance matrix to get the smallest
	 * eigenvector (which is already a normal vector of the least square fitting
	 * plane). After the calculation of the eigenvector it is ensured, that the
	 * returned normal vector always points towards the center of the coordinate
	 * system. (this means for example, if this method is used to compute the
	 * normal of the ground by a bunch of points on ground level, the
	 * resulting normal points always to the sky, since we can't be below the
	 * ground)
	 *
	 * @param points the points forming a plane
	 * @return a normal vector of the plane, always pointing towards the center
	 *         of the coordinate system
	 */
	public static Vector3D calculatePlaneNormal(List<Vector3D> points)
	{
		// Check for minimum 3 points
		if (points.size() < 3) {
			return null;
		}

		Vector3D centroid;
		double[][] cov = new double[points.size()][3];
		double x = 0, y = 0, z = 0;

		// Calculate centroid
		for (Vector3D vec : points) {
			x += vec.getX();
			y += vec.getY();
			z += vec.getZ();
		}
		centroid = new Vector3D(x / points.size(), y / points.size(), z / points.size());

		// Calculate co-variance matrix
		int i = 0;
		for (Vector3D vec : points) {
			cov[i][0] = vec.getX() - centroid.getX();
			cov[i][1] = vec.getY() - centroid.getY();
			cov[i][2] = vec.getZ() - centroid.getZ();
			i++;
		}

		// Compute SVD
		RealMatrix covMatrix = new Array2DRowRealMatrix(cov, false);
		SingularValueDecomposition svd = new SingularValueDecomposition(covMatrix);
		RealMatrix V = svd.getV();

		// Take the eigenvector to the smalles eigenvalue (which should in our
		// case be the third one - so take the third column of V)
		Vector3D res = new Vector3D(V.getEntry(0, 2), V.getEntry(1, 2), V.getEntry(2, 2));

		// Check if the normal points towards the bottom - and if so, let it point
		// towards the sky
		if (Vector3D.dotProduct(centroid.normalize(), res) > 0) {
			res = res.negate();
		}

		return res;
	}

	/**
	 * This method calculates the position from a set of point correspondences and the given orientation. The position
	 * is calculated by using the given orientation to transform the local (observed) position of a point feature into
	 * the global coordinate system. After this transformation, we can simply subtract this vector from the known
	 * position to retrieve our position. With more than one point correspondence, the resulting positions to the
	 * different point correspondences are simply averaged.
	 *
	 * @param pointRefs - a collection of point correspondences for position calculation
	 * @param orientation - the rotation to transform local positions into global positions
	 * @return the position
	 */
	public static Vector3D calculatePosition(Collection<IPointCorrespondence> pointRefs, Rotation orientation)
	{
		// localized x-, y-, z-Position
		double x = 0;
		double y = 0;
		double z = 0;
		int noOfUsedRefPoints = 0;

		Vector3D myPos;

		// iterate through all point references
		if (pointRefs != null) {
			for (IPointCorrespondence flag : pointRefs) {
				myPos = calculatePosition(flag.getObservedPosition(), flag.getKnownPosition(), orientation);

				// add calculated position
				x += myPos.getX();
				y += myPos.getY();
				z += myPos.getZ();
				noOfUsedRefPoints++;
			}
		}

		if (noOfUsedRefPoints == 0) {
			return null;
		}

		// average results
		x /= noOfUsedRefPoints;
		y /= noOfUsedRefPoints;
		z /= noOfUsedRefPoints;

		return new Vector3D(x, y, z);
	}

	/**
	 * This method calculates the position based on a single local-to-known
	 * position combination and the given orientation.
	 *
	 * @param localPosition - the position in the global body system
	 * @param knownPosition - the known position in the global system
	 * @param orientation - the transformation from the local to the global
	 *        system
	 * @return resulting position
	 */
	private static Vector3D calculatePosition(Vector3D localPosition, Vector3D knownPosition, Rotation orientation)
	{
		return knownPosition.subtract(orientation.applyTo(localPosition));
	}

	/**
	 * An implementation of the umeyama point set registration method (known from
	 * image processing). The result is an 3D rotation, which aligns the given
	 * moving point set best to the fixed point set, in the sense of least square
	 * distances.
	 *
	 * @param movingVec - the array of moving points to register
	 * @param fixedVec - the array of corresponding fixed points
	 * @return the 3D rotation, which aligns the given moving point set best to
	 *         the fixed point set, in the sense of least square distances
	 */
	public static Rotation umeyama(Vector3D[] movingVec, Vector3D[] fixedVec)
	{
		if (movingVec.length != fixedVec.length) {
			throw new InvalidParameterException();
		}

		double[][] cov = new double[][] {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

		for (int i = 0; i < movingVec.length; i++) {
			cov[0][0] += fixedVec[i].getX() * movingVec[i].getX();
			cov[0][1] += fixedVec[i].getX() * movingVec[i].getY();
			cov[0][2] += fixedVec[i].getX() * movingVec[i].getZ();

			cov[1][0] += fixedVec[i].getY() * movingVec[i].getX();
			cov[1][1] += fixedVec[i].getY() * movingVec[i].getY();
			cov[1][2] += fixedVec[i].getY() * movingVec[i].getZ();

			cov[2][0] += fixedVec[i].getZ() * movingVec[i].getX();
			cov[2][1] += fixedVec[i].getZ() * movingVec[i].getY();
			cov[2][2] += fixedVec[i].getZ() * movingVec[i].getZ();
		}

		// Compute SVD
		RealMatrix covMatrix = new Array2DRowRealMatrix(cov, false);
		SingularValueDecomposition svd = new SingularValueDecomposition(covMatrix);

		RealMatrix U = svd.getU();
		RealMatrix S = new Array2DRowRealMatrix(new double[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
		RealMatrix V = svd.getV();

		LUDecomposition U_LUDec = new LUDecomposition(U);
		LUDecomposition V_LUDec = new LUDecomposition(U);

		if (U_LUDec.getDeterminant() * V_LUDec.getDeterminant() == -1) {
			S.setEntry(2, 2, -1);
		}
		U = U.multiply(S).multiply(V.transpose());

		return Geometry.toRotation(U);
	}

	/**
	 * Estimate the depth of the given point and line feature observations.<br>
	 * <br>
	 * This method uses the provided orientation and height to calculate the intersection of the observed direction with
	 * the ground plane. This information is then used to scale the observations accordingly. Observations that have
	 * depth information already assigned to them are unchanged.
	 *
	 * @param pointObservations the list of point observations
	 * @param lineObservations the list of line observations
	 * @param height the height of the camera
	 * @param orientation the orientation of the camera
	 *
	 * @return true, if depth information could successfully be assigned, false if not enough or invalid information is
	 * given to perform a depth estimation
	 */
	public static boolean estimateDepth(List<IPointFeatureObservation> pointObservations,
			List<ILineFeatureObservation> lineObservations, double height, Rotation orientation)
	{
		if (orientation == null) {
			// Need an orientation to estimate the depths
			return false;
		}

		if (pointObservations != null) {
			for (int i = 0; i < pointObservations.size(); i++) {
				IPointFeatureObservation o = pointObservations.get(i);

				if (!o.hasDepth()) {
					Vector3D observedPos = o.getObservedPosition();
					Vector3D globalObservedPos = orientation.applyTo(observedPos);
					if (globalObservedPos.getZ() < 0) {
						double scale = -height / globalObservedPos.getZ();
						o.assignDepthInfo(observedPos.scalarMultiply(scale));
					} else {
						// This should not happen as we expect to observe locations intersection the ground, but the
						// current observation is above the horizon. Remove the observation to avoid confusion.
						pointObservations.remove(i);
						i--;
					}
				}
			}
		}

		if (lineObservations != null) {
			for (int i = 0; i < lineObservations.size(); i++) {
				ILineFeatureObservation o = lineObservations.get(i);

				if (!o.hasDepth()) {
					Vector3D observedPos1 = o.getObservedPosition1();
					Vector3D observedPos2 = o.getObservedPosition2();
					Vector3D globalObservedPos1 = orientation.applyTo(observedPos1);
					Vector3D globalObservedPos2 = orientation.applyTo(observedPos2);
					if (globalObservedPos1.getZ() < 0 && globalObservedPos2.getZ() < 0) {
						double scale1 = -height / globalObservedPos1.getZ();
						double scale2 = -height / globalObservedPos2.getZ();
						o.assignDepthInfo(observedPos1.scalarMultiply(scale1), observedPos2.scalarMultiply(scale2));
					} else {
						// This should not happen as we expect to observe locations intersection the ground, but the
						// current observation is above the horizon. Remove the observation to avoid confusion.
						lineObservations.remove(i);
						i--;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Estimate the depth of the given direction vector.<br>
	 * <br>
	 * This method uses the provided orientation and height information to calculate the intersection point of the
	 * direction vector with a plane parallel to the ground at the given height above ground. This information is then
	 * used to scale the observations accordingly. Observations that have depth information already assigned to them are
	 * unchanged.
	 *
	 * @param dirVec the direction vector
	 * @param orientation the orientation of the camera
	 * @param height the height of the camera
	 * @param heightAboveGround the height of the plane to intersect
	 *
	 * @return the dirVec scaled to the appropriate length
	 */
	public static Vector3D estimateDepth(Vector3D dirVec, Rotation orientation, double height, double heightAboveGround)
	{
		Vector3D globalDir = orientation.applyTo(dirVec);
		// TODO: Maybe return null if z component of global direction vector is positive (observation in the sky)
		double scale = (heightAboveGround - height) / globalDir.getZ();
		return dirVec.scalarMultiply(scale);
	}

	/**
	 * Match the observed point features to known point features and assign feature correspondences.
	 *
	 * @param map the feature map
	 * @param estimatedPose the current pose estimate
	 * @param observations a list of point feature observations to assign
	 */
	public static void assignPointObservations(
			IFeatureMap map, IPose3D estimatedPose, List<IPointFeatureObservation> observations)
	{
		for (IPointFeatureObservation o : observations) {
			if (o.isAssigned()) {
				continue;
			}

			// compare each seen point feature observation...
			Vector3D globalObservedPos = estimatedPose.applyTo(o.getObservedPosition());
			double shortestDistance = Double.MAX_VALUE;
			IPointFeature nearestPointFeature = null;
			Collection<IPointFeature> candidateFeatures = map.getPointFeatures(o.getType());

			// with each known point feature of the corresponding type to assign the closest one
			for (IPointFeature feature : candidateFeatures) {
				double distanceSeenKnown =
						VectorUtils.getDistanceBetweenXY(feature.getKnownPosition(), globalObservedPos);

				if (distanceSeenKnown < shortestDistance) {
					nearestPointFeature = feature;
					shortestDistance = distanceSeenKnown;
				}
			}

			// only assign point feature if the distance is closer than deviation
			double deviation = 1.0;
			if (shortestDistance < deviation) {
				o.assign(nearestPointFeature.getName());
			}
		}
	}

	/**
	 * Try to assign line observations to line features.
	 *
	 * @param map the map of the environment
	 * @param estimatedPose the current pose estimation
	 * @param observations the current line observations
	 * @return true, if at least one line observation was assigned, false otherwise
	 */
	public static boolean assignLineObservations(
			IFeatureMap map, IPose3D estimatedPose, List<ILineFeatureObservation> observations)
	{
		if (observations == null || estimatedPose == null) {
			return false;
		}

		boolean assignedLine = false;
		final double range = 0.2;

		for (ILineFeatureObservation o : observations) {
			if (o.isAssigned()) {
				continue;
			}

			for (ILineFeature lineFeature : map.getLineFeatures().values()) {
				Vector3D llPos1 = estimatedPose.applyTo(o.getObservedPosition1());
				Vector3D llPos2 = estimatedPose.applyTo(o.getObservedPosition2());
				Vector3D rlPos1 = lineFeature.getKnownPosition1();
				Vector3D rlPos2 = lineFeature.getKnownPosition2();
				if (FuzzyCompare.eq(llPos1, rlPos1, range) && FuzzyCompare.eq(llPos2, rlPos2, range)) {
					// Only add fully observed lines
					o.assign(lineFeature.getName(), false);
					assignedLine = true;
				}
				if (FuzzyCompare.eq(llPos1, rlPos2, range) && FuzzyCompare.eq(llPos2, rlPos1, range)) {
					// Only add fully observed lines
					o.assign(lineFeature.getName(), true);
					assignedLine = true;
				}
			}
		}

		return assignedLine;

		// 1. idea
		// Create an utility system, in which we assign an utility (or likelyhood)
		// to each visible line and each reference line. After that we check, if
		// all reference lines are just preferred once. If this is the case, we
		// have a collision free assignment and hope everything is fine. If we
		// have collisions, we have to take further checks on the
		// second-most-preferred reference lines, etc.

		// 2. idea
		// Try to build a set of relations between all known reference lines
		// (like: "x.dir == y.dir && x.p1 == y.p1-(0,14) && x.p2 == y.p2-(0,14)"
		// or "x.p1 == refPoint.p", etc.) and then try to match the set of visible
		// lines in such a way, that no relation is violated.
		// Example:
		// 1. Normalize all vision information according to the normal of the
		// least square fitting plane through all line points (--> remove x- and
		// y-rotation).
		// 2. Take the longest unassigned line you see and try to find a unique
		// assignment with respect to all the other visible lines
		// 3. If an unique assignment was possible or not, remove the current line
		// from the set of lines to assign. Continue with Step 2 as long as the
		// set of lines to assign is not empty.
		// 4. If one run finished without assigning all lines, take the remaining
		// unassigned lines and restart with step 2. Stop if no single line could
		// be assigned during one run.

		// 3. idea
		// Try to start with an known reference point and search for lines which
		// end at the reference point and then start to assign lines from that
		// reference point on. Since all lines are interconnected with some other
		// lines, we can follow up the graph, until we don't see no matching
		// lines any more.
	}

	/**
	 * Extract a list of point correspondences from the given point feature map and observations list.
	 *
	 * @param correspondences a list for storing the point correspondences
	 * @param features the map of known point features
	 * @param observations a list of (assigned) point feature observations
	 */
	public static void extractPointFeatureCorrespondences(List<IPointCorrespondence> correspondences,
			Map<String, IPointFeature> features, List<IPointFeatureObservation> observations)
	{
		if (features == null || observations == null) {
			return;
		}

		for (IPointFeatureObservation o : observations) {
			if (o.getName() != null) {
				IPointFeature feature = features.get(o.getName());

				if (feature != null) {
					correspondences.add(new PointCorrespondence(o.getObservedPosition(), feature.getKnownPosition()));
				}
			}
		}
	}

	/**
	 * Extract a list of line correspondences from the given line feature map and observations list.
	 *
	 * @param correspondences a list for storing the point correspondences
	 * @param features the map of known line features
	 * @param observations a list of (assigned) line feature observations
	 */
	public static void extractLineFeatureCorrespondences(List<IPointCorrespondence> correspondences,
			Map<String, ILineFeature> features, List<ILineFeatureObservation> observations)
	{
		if (features == null || observations == null) {
			return;
		}

		for (ILineFeatureObservation o : observations) {
			if (o.isAssigned()) {
				ILineFeature feature = features.get(o.getName());

				if (feature != null) {
					// TODO: Adjust known positions of the line feature in case it is only partially observed
					Vector3D adjustedKnownPos1 = feature.getKnownPosition1();
					Vector3D adjustedKnownPos2 = feature.getKnownPosition2();

					correspondences.add(new PointCorrespondence(o.getObservedPosition1(), adjustedKnownPos1));
					correspondences.add(new PointCorrespondence(o.getObservedPosition2(), adjustedKnownPos2));
				}
			}
		}
	}

	/**
	 * Filter the list of all available point features included in the map to get only those which are
	 * plausible(visible), depending on the given pose.
	 * @param features the list of known point features
	 * @param pose the (estimated) pose
	 * @param fov the camera field of view
	 * @param maxDistance the maximum plausible distance
	 * @return map of filtered point features
	 */
	public static List<IPointFeature> extractPlausiblePointFeatures(
			Collection<IPointFeature> features, IPose3D pose, IFieldOfView fov, double maxDistance)
	{
		List<IPointFeature> filteredFeatures = new ArrayList<>();

		features.forEach(f -> {
			Vector3D knownLocalPosition = pose.applyInverseTo(f.getKnownPosition());
			if (fov.isInside(knownLocalPosition) && knownLocalPosition.getNorm() < maxDistance) {
				filteredFeatures.add(f);
			}
		});
		return filteredFeatures;
	}

	/**
	 * Filter the list of all available point features included in the map to get only those which are
	 * 	 * plausible(visible), depending on the given pose and for the given type.
	 * @param map map containing all available features
	 * @param type type of features to filter
	 * @param pose the (estimated) pose
	 * @param fov the camera field of view
	 * @param maxDistance the maximum plausible distance
	 * @return map of filtered point features
	 */
	public static List<IPointFeature> extractPlausiblePointFeatures(
			IFeatureMap map, String type, IPose3D pose, IFieldOfView fov, double maxDistance)
	{
		return extractPlausiblePointFeatures(map.getPointFeatures(type), pose, fov, maxDistance);
	}
}
