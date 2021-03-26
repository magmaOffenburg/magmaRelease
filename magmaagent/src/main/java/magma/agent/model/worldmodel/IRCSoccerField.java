/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel;

import hso.autonomy.agent.model.worldmodel.localizer.IFeatureMap;
import hso.autonomy.util.geometry.Area2D;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interface for a RoboCup soccer field feature map.
 *
 * @author Stefan Glaser
 */
public interface IRCSoccerField extends IFeatureMap {
	/** Half of the field length. */
	float fieldHalfLength();

	/** Half of the field width. */
	float fieldHalfWidth();

	/** Half of the goal width. */
	float goalHalfWidth();

	/** The height of the goals. */
	float goalHeight();

	/** The depth of the goals. */
	float goalDepth();

	/** Half of the penalty length. */
	float penaltyHalfLength();

	/** Half of the penalty width. */
	float penaltyWidth();

	/** Radius of the center circle. */
	float centerCircleRadius();

	/**
	 * Retrieve the position of the own goal (position between the goal posts).
	 *
	 * @return the position in the middle of the own goal posts
	 */
	Vector3D getOwnGoalPosition();

	/**
	 * Retrieve the position of the other goal (position between the goal posts).
	 *
	 * @return the position in the middle of the other goal posts
	 */
	Vector3D getOtherGoalPosition();

	/**
	 * Retrieve the field area in 2D.
	 *
	 * @return the field area
	 */
	Area2D.Float getFieldArea();
}
