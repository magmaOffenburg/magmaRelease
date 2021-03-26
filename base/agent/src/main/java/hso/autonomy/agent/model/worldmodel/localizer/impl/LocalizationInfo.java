/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer.impl;

import hso.autonomy.agent.model.worldmodel.localizer.ILocalizationInfo;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose3D;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Default implementation for {@link ILocalizationInfo} interface.
 *
 * @author Stefan Glaser
 */
public class LocalizationInfo implements ILocalizationInfo
{
	/** The time the localization happened. */
	private float localizationTime;

	/** The localized pose. */
	private IPose3D localizedPose;

	/** The unfiltered position. */
	private Vector3D unfilteredPos;

	/**
	 * Create a new instance with the identity pose.
	 */
	public LocalizationInfo()
	{
		this(0, new Pose3D());
	}

	/**
	 * Create a new instance from position and orientation.
	 *
	 * @param localizationTime - the global time the localization happened
	 * @param localizedPosition - the most probable position
	 * @param localizedOrientation - the most probable orientation
	 */
	public LocalizationInfo(float localizationTime, Vector3D localizedPosition, Rotation localizedOrientation)
	{
		this(localizationTime, new Pose3D(localizedPosition, localizedOrientation));
	}

	/**
	 * Create a new instance from a pose.
	 *
	 * @param localizationTime - the global time the localization happened
	 * @param localizedPose - the most probable pose
	 */
	public LocalizationInfo(float localizationTime, IPose3D localizedPose)
	{
		this.localizationTime = localizationTime;
		this.localizedPose = localizedPose;
		this.unfilteredPos = null;
	}

	@Override
	public float getTime()
	{
		return localizationTime;
	}

	@Override
	public IPose3D getLocalizedPose()
	{
		return localizedPose;
	}

	@Override
	public Vector3D getLocalizedPosition()
	{
		return localizedPose.getPosition();
	}

	public void setLocalizedPosition(Vector3D position)
	{
		localizedPose = new Pose3D(position, localizedPose.getOrientation());
	}

	@Override
	public Rotation getLocalizedOrientation()
	{
		return localizedPose.getOrientation();
	}

	public void setLocalizedOrientation(Rotation orientation)
	{
		localizedPose = new Pose3D(localizedPose.getPosition(), orientation);
	}

	@Override
	public Vector3D getUnfilteredPosition()
	{
		return unfilteredPos != null ? unfilteredPos : localizedPose.getPosition();
	}

	public void setUnfilteredPos(Vector3D unfilteredPos)
	{
		this.unfilteredPos = unfilteredPos;
	}

	/**
	 * Set the localized pose and corresponding time.
	 *
	 * @param globalTime the time the localization happened
	 * @param localizedPose the new localized pose
	 */
	public LocalizationInfo set(float globalTime, IPose3D localizedPose)
	{
		this.localizationTime = globalTime;
		this.localizedPose = localizedPose;

		return this;
	}

	/**
	 * Set the localized pose and corresponding time.
	 *
	 * @param globalTime the time the localization happened
	 * @param localizedPosition the new localized position
	 * @param localizedOrientation the new localized orientation
	 */
	public LocalizationInfo set(float globalTime, Vector3D localizedPosition, Rotation localizedOrientation)
	{
		return this.set(globalTime, new Pose3D(localizedPosition, localizedOrientation));
	}
}
