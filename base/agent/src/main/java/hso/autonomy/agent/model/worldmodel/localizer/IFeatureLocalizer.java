/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer;

import hso.autonomy.agent.model.agentmodel.ISensor;
import hso.autonomy.util.geometry.IPose3D;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;

/**
 * Implementations are able to calculate the agent's position and orientation
 * from the passed relative information of point and line features.
 *
 * @author Stefan Glaser
 */
public interface IFeatureLocalizer {
	/**
	 * Retrieve the internal state of the localizer.
	 *
	 * @return the current internal state of the localizer
	 */
	ILocalizationInfo getState();

	/**
	 * Reset the internal state of the localizer.
	 *
	 * @param initialPoseEstimation - an estimation of the new initial pose
	 */
	void reset(float globalTime, IPose3D initialPoseEstimation);

	/**
	 * Predict the absolute pose estimation based on odometry information.
	 *
	 * @param odometry the odometry information
	 * @param orientationEstimation the estimation of the current orientation
	 * @param cameraHeight the current height of the camera above ground
	 *
	 * @return true if prediction was successful (internal state got updated), false otherwise
	 */
	boolean predict(float globalTime, IPose3D odometry, Rotation orientationEstimation, double cameraHeight);

	/**
	 * Correct the absolute position and orientation estimation based on sensor measurements.<br>
	 * <br>
	 * In case the observations do not contain any depth information, this method tries to approximate them.
	 * This method also assigns the unknown point and line feature observations to their corresponding feature in the
	 * map.
	 *
	 * @param map the map of the environment
	 * @param pointObservations the current point feature observations
	 * @param lineObservations the current line feature observations
	 * @param orientationEstimation the estimation of the current orientation
	 * @param cameraHeight the current height of the camera above ground
	 *
	 * @return true if correction was successful (internal state got updated), false otherwise
	 */
	boolean correct(float globalTime, IFeatureMap map, List<IPointFeatureObservation> pointObservations,
			List<ILineFeatureObservation> lineObservations, Rotation orientationEstimation, double cameraHeight);

	/**
	 * Retrieve the map with all used sensors for this localizer
	 * @return map of all sensors
	 */
	Map<String, ISensor> getSensors();
}
