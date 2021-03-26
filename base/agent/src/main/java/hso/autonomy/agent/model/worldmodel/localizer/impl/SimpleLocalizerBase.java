/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer.impl;

import hso.autonomy.agent.model.agentmodel.ISensor;
import hso.autonomy.agent.model.worldmodel.localizer.IFeatureLocalizer;
import hso.autonomy.agent.model.worldmodel.localizer.IFeatureMap;
import hso.autonomy.agent.model.worldmodel.localizer.ILineFeatureObservation;
import hso.autonomy.agent.model.worldmodel.localizer.ILocalizationInfo;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeatureObservation;
import hso.autonomy.agent.model.worldmodel.localizer.IPositionCalculator;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.orientationFilter.IOrientationFilter;
import hso.autonomy.util.geometry.positionFilter.IPositionFilter;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Base class for simple feature based localizer implementations using {@link LocalizationInfo} as internal state and
 * position and orientation filters.
 *
 * @author Stefan Glaser
 */
public abstract class SimpleLocalizerBase implements IFeatureLocalizer, IPositionCalculator
{
	/** The internal state of the localizer. */
	protected LocalizationInfo state;

	/** The position filter. */
	protected IPositionFilter positionFilter;

	/** The orientation filter. */
	protected IOrientationFilter orientationFilter;

	protected Map<String, ISensor> sensors;

	public SimpleLocalizerBase()
	{
		this(null, null);
	}

	public SimpleLocalizerBase(IPositionFilter positionFilter)
	{
		this(positionFilter, null);
	}

	public SimpleLocalizerBase(IPositionFilter positionFilter, IOrientationFilter orientationFilter)
	{
		this.state = new LocalizationInfo();
		this.positionFilter = positionFilter;
		this.orientationFilter = orientationFilter;
	}

	@Override
	public ILocalizationInfo getState()
	{
		return state;
	}

	@Override
	public void reset(float time, IPose3D initialPoseEstimation)
	{
		state.set(time, initialPoseEstimation);

		if (positionFilter != null) {
			positionFilter.reset();
		}
		if (orientationFilter != null) {
			orientationFilter.reset();
		}
	}

	protected void update(float time, IPose3D pose)
	{
		update(time, pose.getPosition(), pose.getOrientation());
	}

	protected void update(float time, Vector3D position, Rotation orientation)
	{
		state.set(time, filterPosition(position), filterOrientation(orientation));
	}

	protected Vector3D filterPosition(Vector3D position)
	{
		if (positionFilter == null) {
			return position;
		}

		// TODO: Add speed to position filter
		return positionFilter.filterPosition(position, state.getLocalizedPose().getPosition(), Vector3D.ZERO);
	}

	protected Rotation filterOrientation(Rotation orientation)
	{
		if (orientationFilter == null) {
			return orientation;
		}

		return orientationFilter.filterOrientation(orientation);
	}

	@Override
	public boolean predict(float globalTime, IPose3D odometry, Rotation orientationEstimation, double cameraHeight)
	{
		if (orientationEstimation != null) {
			state.setLocalizedOrientation(orientationEstimation);
			return true;
		}

		return false;
	}

	@Override
	public boolean correct(float globalTime, IFeatureMap map, List<IPointFeatureObservation> pointObservations,
			List<ILineFeatureObservation> lineObservations, Rotation orientationEstimation, double cameraHeight)
	{
		LocalizerUtil.estimateDepth(pointObservations, lineObservations, cameraHeight, orientationEstimation);
		LocalizerUtil.assignPointObservations(map, state.getLocalizedPose(), pointObservations);
		LocalizerUtil.assignLineObservations(map, state.getLocalizedPose(), lineObservations);
		IPose3D localizedPose = localize(map, pointObservations, lineObservations, orientationEstimation);

		if (localizedPose != null) {
			update(globalTime, localizedPose);
			return true;
		} else if (orientationEstimation != null) {
			state.setLocalizedOrientation(orientationEstimation);
			return true;
		}

		return false;
	}

	@Override
	public Map<String, ISensor> getSensors()
	{
		return sensors;
	}
}
