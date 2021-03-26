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
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * This localization method applies the umeyama method on the reference points
 * (and reference lines - not yet implemented) to determine the current
 * orientation in the 3D space. This orientation is then used to transform the
 * local positions of the reference points and lines to calculate the position.
 *
 * @author Stefan Glaser
 */
public class LocalizerUmeyama extends SimpleLocalizerBase
{
	@Override
	public IPose3D localize(IFeatureMap map, List<IPointFeatureObservation> pointObservations,
			List<ILineFeatureObservation> lineObservations, Rotation orientationEstimation)
	{
		List<IPointCorrespondence> pointRefs = new ArrayList<IPointCorrespondence>();
		LocalizerUtil.extractPointFeatureCorrespondences(pointRefs, map.getPointFeatures(), pointObservations);
		LocalizerUtil.extractLineFeatureCorrespondences(pointRefs, map.getLineFeatures(), lineObservations);

		// We wanna see at least 3 reference points
		if (pointRefs.size() < 3) {
			return null;
		}

		Vector3D[] visionVecArr = new Vector3D[pointRefs.size()];
		Vector3D[] fixedVecArr = new Vector3D[pointRefs.size()];
		Vector3D fixedCOM;
		Vector3D visionCOM;

		// Transform the global and local positions of each point correspondence to the body-system and store the
		// resulting vectors in the corresponding arrays.
		int i = 0;
		for (IPointCorrespondence pc : pointRefs) {
			// Add fixedPosition
			fixedVecArr[i] = pc.getKnownPosition();

			// Add localPosition
			visionVecArr[i] = pc.getObservedPosition();
			i++;
		}

		// Calculate centroid of fixed points
		fixedCOM = Vector3D.ZERO;
		for (Vector3D vec : fixedVecArr) {
			fixedCOM = fixedCOM.add(vec);
		}
		fixedCOM = fixedCOM.scalarMultiply(1.0 / fixedVecArr.length);

		// Calculate centroid of vision points
		visionCOM = Vector3D.ZERO;
		for (Vector3D vec : visionVecArr) {
			visionCOM = visionCOM.add(vec);
		}
		visionCOM = visionCOM.scalarMultiply(1.0 / visionVecArr.length);

		// Calculate fixed covariance vectors
		for (i = 0; i < fixedVecArr.length; i++) {
			fixedVecArr[i] = fixedVecArr[i].subtract(fixedCOM);
		}
		// Calculate vision covariance vectors
		for (i = 0; i < visionVecArr.length; i++) {
			visionVecArr[i] = visionVecArr[i].subtract(visionCOM);
		}

		// Determine orientation by umeyama
		Rotation orientation = LocalizerUtil.umeyama(visionVecArr, fixedVecArr);

		// TODO: Here is maybe an open problem! The calculated orientation is with
		// respect to the centroid, not to our own position and is therefore not
		// necessarily the same as our orientation!
		// We maybe have to translate the vision vectors before and after the
		// rotation according to the centroid before position estimation and then
		// perform a second umeyama with all known positions adjusted to the
		// localized position.

		// Determine position and return result
		return new Pose3D(LocalizerUtil.calculatePosition(pointRefs, orientation), orientation);
	}
}
