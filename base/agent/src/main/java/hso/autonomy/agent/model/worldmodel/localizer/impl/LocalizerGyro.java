/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer.impl;

import hso.autonomy.agent.model.worldmodel.localizer.IFeatureMap;
import hso.autonomy.agent.model.worldmodel.localizer.ILineFeatureObservation;
import hso.autonomy.agent.model.worldmodel.localizer.IPointCorrespondence;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeatureObservation;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.geometry.orientationFilter.IOrientationFilter;
import hso.autonomy.util.geometry.positionFilter.IPositionFilter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;

/**
 * Implements a 3-dimensional localizer based on point and line features in combination with an estimated orientation.
 *
 * @author Stefan Glaser
 */
public class LocalizerGyro extends SimpleLocalizerBase
{
	public LocalizerGyro()
	{
		this(null);
	}

	public LocalizerGyro(IPositionFilter positionFilter)
	{
		this(positionFilter, null);
	}

	public LocalizerGyro(IPositionFilter positionFilter, IOrientationFilter orientationFilter)
	{
		super(positionFilter, orientationFilter);
	}

	@Override
	public IPose3D localize(IFeatureMap map, List<IPointFeatureObservation> pointObservations,
			List<ILineFeatureObservation> lineObservations, Rotation orientationEstimation)
	{
		if (orientationEstimation == null) {
			// there is no gyro information available
			return null;
		}

		List<IPointCorrespondence> pointRefs = new ArrayList<IPointCorrespondence>();
		LocalizerUtil.extractPointFeatureCorrespondences(pointRefs, map.getPointFeatures(), pointObservations);
		LocalizerUtil.extractLineFeatureCorrespondences(pointRefs, map.getLineFeatures(), lineObservations);

		if (pointRefs.size() == 0) {
			// there is no visible flag or line
			return null;
		}

		return new Pose3D(LocalizerUtil.calculatePosition(pointRefs, orientationEstimation), orientationEstimation);
	}

	//	// ==================================================
	//	// Below: experimental localizer implementations
	//	// ==================================================
	//	/**
	//	 * This method searches for line pints near by known flags, to extent the
	//	 * fix-point base for umeyama orientation estimation.
	//	 */
	//	private Pose3D lineSearchingUmeyamaLocalization(
	//			Collection<IPointFeature> flagsList, List<ILineFeatureObservation> lines)
	//	{
	//		if (flagsList.size() < 3 || (lines != null && lines.size() < 2)) {
	//			return null;
	//		}
	//
	//		List<Vector3D> visionVec = new ArrayList<>();
	//		List<Vector3D> fixedVec = new ArrayList<>();
	//		Vector3D fixedCOM;
	//		Vector3D visionCOM;
	//		Vector3D temp;
	//
	//		for (IPointFeature flag : flagsList) {
	//			// Add fixedPosition
	//			fixedVec.add(flag.getKnownPosition());
	//
	//			// Add localPosition
	//			visionVec.add(flag.getLocalPosition());
	//		}
	//
	//		// Search for lines, ending at fix-points
	//		for (IPointFeature flag : flagsList) {
	//			// Check on corner flag
	//			if (FuzzyCompare.eq(0, flag.getKnownPosition().getZ(), 0.001)) {
	//				for (ILineFeatureObservation line : lines) {
	//					if (FuzzyCompare.eq(flag.getLocalPosition(), line.getObservedPosition1(), 0.5)) {
	//						// We found a border line, starting/ending at the fix-point
	//						fixedVec.add(flag.getKnownPosition());
	//						visionVec.add(line.getObservedPosition1());
	//						// System.out.println("found matching line to corner flag");
	//					}
	//
	//					if (FuzzyCompare.eq(flag.getLocalPosition(), line.getObservedPosition2(), 0.5)) {
	//						// We found a border line, starting/ending at the fix-point
	//						fixedVec.add(flag.getKnownPosition());
	//						visionVec.add(line.getObservedPosition2());
	//						// System.out.println("found matching line to corner flag");
	//					}
	//				}
	//			}
	//
	//			// Check on goal post flag
	//			if (flag.getKnownPosition().getZ() > 0.1) {
	//				for (ILineFeatureObservation line : lines) {
	//					if (FuzzyCompare.eq(flag.getLocalPosition(), line.getObservedPosition1(), 1.5)) {
	//						// We found a border line, starting/ending at the fix-point
	//						temp = flag.getKnownPosition();
	//
	//						if (temp.getY() > 0) {
	//							// Upper goal post
	//							temp = temp.add(new Vector3D(0, 0.9, -0.8));
	//						} else {
	//							// Lower goal post
	//							temp = temp.add(new Vector3D(0, -0.9, -0.8));
	//						}
	//						fixedVec.add(temp);
	//
	//						if (line.getObservedPosition2() != null) {
	//							if (temp.getX() > 0) {
	//								temp = temp.add(new Vector3D(-1.8, 0, 0));
	//							} else {
	//								temp = temp.add(new Vector3D(1.8, 0, 0));
	//							}
	//							fixedVec.add(temp);
	//						}
	//
	//						visionVec.add(line.getObservedPosition1());
	//
	//						if (line.getObservedPosition2() != null) {
	//							visionVec.add(line.getObservedPosition2());
	//						}
	//
	//						// System.out.println("found matching line to goal post");
	//					}
	//
	//					if (FuzzyCompare.eq(flag.getLocalPosition(), line.getObservedPosition2(), 1.5)) {
	//						// We found a border line, starting/ending at the fix-point
	//						temp = flag.getKnownPosition();
	//
	//						if (temp.getY() > 0) {
	//							// Upper goal post
	//							temp = temp.add(new Vector3D(0, 0.9, -0.8));
	//						} else {
	//							// Lower goal post
	//							temp = temp.add(new Vector3D(0, -0.9, -0.8));
	//						}
	//						fixedVec.add(temp);
	//
	//						if (line.getObservedPosition1() != null) {
	//							if (temp.getX() > 0) {
	//								temp = temp.add(new Vector3D(-1.8, 0, 0));
	//							} else {
	//								temp = temp.add(new Vector3D(1.8, 0, 0));
	//							}
	//							fixedVec.add(temp);
	//						}
	//
	//						visionVec.add(line.getObservedPosition2());
	//
	//						if (line.getObservedPosition1() != null) {
	//							visionVec.add(line.getObservedPosition1());
	//						}
	//
	//						// System.out.println("found matching line to goal post");
	//					}
	//				}
	//			}
	//		}
	//
	//		fixedCOM = Vector3D.ZERO;
	//		for (Vector3D vec : fixedVec) {
	//			fixedCOM = fixedCOM.add(vec);
	//		}
	//		fixedCOM = fixedCOM.scalarMultiply(1.0 / fixedVec.size());
	//
	//		visionCOM = Vector3D.ZERO;
	//		for (Vector3D vec : visionVec) {
	//			visionCOM = visionCOM.add(vec);
	//		}
	//		visionCOM = visionCOM.scalarMultiply(1.0 / visionVec.size());
	//
	//		Vector3D[] fixedVecArr = new Vector3D[visionVec.size()];
	//		for (int i = 0; i < fixedVec.size(); i++) {
	//			fixedVecArr[i] = fixedVec.get(i).subtract(fixedCOM);
	//		}
	//
	//		Vector3D[] visionVecArr = new Vector3D[visionVec.size()];
	//		for (int i = 0; i < visionVec.size(); i++) {
	//			visionVecArr[i] = visionVec.get(i).subtract(visionCOM);
	//		}
	//
	//		Rotation orientation = LocalizerUtil.umeyama(visionVecArr, fixedVecArr);
	//
	//		return new Pose3D(LocalizerUtil.calculatePosition(flagsList, orientation), orientation);
	//	}
}
