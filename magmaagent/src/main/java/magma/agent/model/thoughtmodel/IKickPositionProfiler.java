/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel;

import hso.autonomy.util.geometry.Angle;
import java.util.SortedSet;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interface for a kick option profiler.
 *
 * @author Stefan Glaser
 */
public interface IKickPositionProfiler {
	/** Trigger a recalculation of the kick option profile on next request. */
	void resetProfile();

	/**
	 * Retrieve the intended kick direction.
	 *
	 * @return the intended kick direction (angle)
	 */
	Angle getIntendedKickDirection();

	/**
	 * Retrieve the intended kick distance.
	 *
	 * @return the intended kick distance
	 */
	double getIntendedKickDistance();

	/**
	 * Retrieve the set of kick position estimations.
	 *
	 * @return the set of kick position estimations
	 */
	SortedSet<KickPositionEstimation> getEvaluatedPositions();

	/**
	 * Retrieve the intended kick position in the global system.
	 *
	 * @return the global intended kick position
	 */
	Vector3D getIntendedKickPosition();
}
