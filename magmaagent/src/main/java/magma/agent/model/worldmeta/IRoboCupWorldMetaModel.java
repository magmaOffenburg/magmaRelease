/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta;

import java.io.Serializable;
import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public interface IRoboCupWorldMetaModel extends Serializable {
	/**
	 * Returns the version of the meta model (server version)
	 *
	 * @return server version
	 */
	int getVersion();

	/**
	 * Returns the decay of the ball
	 *
	 * @return ball decay
	 */
	float getBallDecay();

	/**
	 * Returns the dimensions of the soccer field in the form: (length, width)
	 *
	 * @return the soccer field dimensions (meters)
	 */
	Vector2D getFieldDimensions();

	/**
	 * Returns the dimensions of the goal in the form: (depth, width, height)
	 *
	 * @return the goal dimensions (meters)
	 */
	Vector3D getGoalDimensions();

	/**
	 * Returns the dimensions of the penalty area in the form: (length, width)
	 * Note that the length here is width in naosoccersim.rb and vice versa. The
	 * width does NOT include the goal width.
	 *
	 * @return the penalty area dimensions (meters)
	 */
	Vector2D getPenaltyAreaDimensions();

	/**
	 * Returns the radius of the middle circle
	 *
	 * @return the middle circle radius (meters)
	 */
	float getMiddleCircleRadius();

	/**
	 * Returns a map of point feature configurations
	 *
	 * @return the map of point features
	 */
	Map<String, IPointFeatureConfiguration> getLandmarks();

	/**
	 * Returns a map of line feature configurations
	 *
	 * @return the map of line features
	 */
	Map<String, ILineFeatureConfiguration> getFieldLines();

	/**
	 * @return the ball radius in m
	 */
	float getBallRadius();
}
