/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer;

import hso.autonomy.util.geometry.IPose3D;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;

/**
 * Simple interface for a component calculating the position on a map based on feature observations.
 *
 * @author Stefan Glaser, Klaus Dorer
 */
public interface IPositionCalculator {
	IPose3D localize(IFeatureMap map, List<IPointFeatureObservation> pointObservations,
			List<ILineFeatureObservation> lineObservations, Rotation orientationEstimation);
}
