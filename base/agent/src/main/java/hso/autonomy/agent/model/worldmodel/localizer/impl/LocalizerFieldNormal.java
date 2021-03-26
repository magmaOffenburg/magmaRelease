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
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.geometry.orientationFilter.IOrientationFilter;
import hso.autonomy.util.geometry.positionFilter.IPositionFilter;
import hso.autonomy.util.misc.FuzzyCompare;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * This localization method first calculates a normal to the field ground based
 * on all unlabeled reference lines and reference points on ground level, before
 * it determines the z rotation by relative angle difference of two flags. The
 * resulting orientation is then used together with the reference points to
 * calculate the position.
 *
 * @author Stefan Glaser
 */
public class LocalizerFieldNormal extends SimpleLocalizerBase
{
	public LocalizerFieldNormal()
	{
		this(null);
	}

	public LocalizerFieldNormal(IPositionFilter positionFilter)
	{
		this(positionFilter, null);
	}

	public LocalizerFieldNormal(IPositionFilter positionFilter, IOrientationFilter orientationFilter)
	{
		super(positionFilter, orientationFilter);
	}

	@Override
	public IPose3D localize(IFeatureMap map, List<IPointFeatureObservation> pointObservations,
			List<ILineFeatureObservation> lineObservations, Rotation orientationEstimation)
	{
		return localize3D(map, pointObservations, lineObservations, orientationEstimation);
	}

	public static IPose3D localize3D(IFeatureMap map, List<IPointFeatureObservation> pointObservations,
			List<ILineFeatureObservation> lineObservations, Rotation orientationEstimation)
	{
		Rotation rotation = orientationEstimation;
		List<IPointCorrespondence> pointRefs = new ArrayList<IPointCorrespondence>();
		LocalizerUtil.extractPointFeatureCorrespondences(pointRefs, map.getPointFeatures(), pointObservations);

		// We wanna see at least 3 lines and 2 flags to rely on the field normal orientation estimation.
		// Though, the theoretical minimum amount of lines is 2.
		if (lineObservations != null && lineObservations.size() >= 3 && pointRefs.size() >= 2) {
			List<Vector3D> bottomPoints = new ArrayList<>();
			Vector3D temp;

			// Add fixpoints on ground level to list of bottom-points
			for (IPointCorrespondence flag : pointRefs) {
				if (FuzzyCompare.eq(0, flag.getKnownPosition().getZ(), 0.001)) {
					bottomPoints.add(flag.getObservedPosition());
				}
			}

			// Add all seen lines to list of bottom-points
			for (ILineFeatureObservation line : lineObservations) {
				temp = line.getObservedPosition1();
				if (temp != null) {
					bottomPoints.add(temp);
				}
				temp = line.getObservedPosition2();
				if (temp != null) {
					bottomPoints.add(temp);
				}
			}

			// Calculate field normal
			Vector3D normal = LocalizerUtil.calculatePlaneNormal(bottomPoints);
			// Create rotation based on field normal. Since the field normal is the
			// global z-axis, simply create a rotation that transforms the normal back
			// to the global z-axis.
			Rotation normalRotation = new Rotation(normal, Vector3D.PLUS_K);

			// Calculate z rotation based on normalized local positions of the
			// reference points. Since we still have no clue about our position, we
			// can't just simply use the angle difference to one reference point.
			// Instead we have to use an independent calculation of the angles between
			// two reference points in the local and known system to get the z
			// rotation.
			IPointCorrespondence flag1;
			IPointCorrespondence flag2;
			Vector3D flag1Pos;
			Vector3D flag1KnownPos;
			Vector3D flag2Pos;
			Vector3D flag2KnownPos;
			Vector3D vec1to2_3d;
			Vector3D vec1to2Known_3d;
			List<Angle> angles = new ArrayList<>();

			// Instead of just using the reference points to determine the z rotation,
			// the assigned lines also contribute to a more precise result.
			// Theoretically just two unlabeled lines and {at least one assigned line
			// or tow reference points} are required to result in a complete
			// orientation estimation.
			// Therefore: Add reference points for all proper positions of each
			// assigned line to the list of reference points
			LocalizerUtil.extractLineFeatureCorrespondences(pointRefs, map.getLineFeatures(), lineObservations);

			for (int i = 0; i < pointRefs.size() - 1; i++) {
				flag1 = pointRefs.get(i);
				flag1KnownPos = flag1.getKnownPosition();
				flag1Pos = normalRotation.applyTo(flag1.getObservedPosition());

				for (int j = i + 1; j < pointRefs.size(); j++) {
					flag2 = pointRefs.get(j);
					temp = flag2.getKnownPosition();
					// Check if the second reference point is at the same known position
					// as the first reference point (can happen, since we also use
					// reference lines). In this case, the selected reference point
					// combination can't provide a rotation estimation.
					if (FuzzyCompare.eq(temp, flag1.getKnownPosition(), 0.01)) {
						continue;
					}

					flag2KnownPos = temp;
					flag2Pos = normalRotation.applyTo(flag2.getObservedPosition());

					vec1to2Known_3d = flag2KnownPos.subtract(flag1KnownPos);
					vec1to2_3d = flag2Pos.subtract(flag1Pos);

					Angle knownDirection = Angle.rad(Math.atan2(vec1to2Known_3d.getY(), vec1to2Known_3d.getX()));
					Angle visionDirection = Angle.rad(Math.atan2(vec1to2_3d.getY(), vec1to2_3d.getX()));

					angles.add(knownDirection.subtract(visionDirection));
				}
			}

			// Add the new calculated z rotation to the previous field normal
			// orientation to get the final agent orientation
			Angle[] anglesArray = angles.toArray(new Angle[angles.size()]);
			double averageAngle = Angle.average(anglesArray).radians();
			Rotation zRotation = Geometry.createZRotation(averageAngle);

			rotation = zRotation.applyTo(normalRotation);
		} else {
			// Fallback: Simply extract all possible line feature correspondences and move on
			LocalizerUtil.extractLineFeatureCorrespondences(pointRefs, map.getLineFeatures(), lineObservations);
		}

		// Calculate position based on the calculated / estimated orientation and the reference points
		if (pointRefs.size() > 0 && rotation != null) {
			return new Pose3D(LocalizerUtil.calculatePosition(pointRefs, rotation), rotation);
		}

		return null;
	}
}
