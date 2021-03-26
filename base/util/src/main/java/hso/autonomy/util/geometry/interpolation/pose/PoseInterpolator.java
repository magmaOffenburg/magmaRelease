/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry.interpolation.pose;

import hso.autonomy.util.geometry.Pose6D;
import hso.autonomy.util.geometry.interpolation.IPoseInterpolator;
import hso.autonomy.util.geometry.interpolation.IValueInterpolator;
import hso.autonomy.util.geometry.interpolation.value.LinearValueInterpolator;

/**
 * @author Stefan Glaser
 */
public class PoseInterpolator implements IPoseInterpolator
{
	/** Value interpolator for the x-position */
	public IValueInterpolator xInterpolator;

	/** Value interpolator for the y-position */
	public IValueInterpolator yInterpolator;

	/** Value interpolator for the z-position */
	public IValueInterpolator zInterpolator;

	/** Value interpolator for the x-rotation */
	public IValueInterpolator xRotInterpolator;

	/** Value interpolator for the y-rotation */
	public IValueInterpolator yRotInterpolator;

	/** Value interpolator for the z-rotation */
	public IValueInterpolator zRotInterpolator;

	/**
	 * Default constructor to create a new PoseTrajectoryInterpolator instance
	 * with linear interpolators for all dimensions.
	 */
	public PoseInterpolator()
	{
		this(new LinearValueInterpolator(), new LinearValueInterpolator(), new LinearValueInterpolator(),
				new LinearValueInterpolator(), new LinearValueInterpolator(), new LinearValueInterpolator());
	}

	/**
	 * Create a new PoseTrajectoryInterpolator instance using the given
	 * interpolators.
	 *
	 * @param xInterpolator - the interpolator for x-position
	 * @param yInterpolator - the interpolator for y-position
	 * @param zInterpolator - the interpolator for z-position
	 * @param xRotInterpolator - the interpolator for x-rotation
	 * @param yRotInterpolator - the interpolator for y-rotation
	 * @param zRotInterpolator - the interpolator for z-rotation
	 */
	public PoseInterpolator(IValueInterpolator xInterpolator, IValueInterpolator yInterpolator,
			IValueInterpolator zInterpolator, IValueInterpolator xRotInterpolator, IValueInterpolator yRotInterpolator,
			IValueInterpolator zRotInterpolator)
	{
		this.xInterpolator = xInterpolator;
		this.yInterpolator = yInterpolator;
		this.zInterpolator = zInterpolator;
		this.xRotInterpolator = xRotInterpolator;
		this.yRotInterpolator = yRotInterpolator;
		this.zRotInterpolator = zRotInterpolator;
	}

	@Override
	public Pose6D interpolate(Pose6D initialPose, Pose6D targetPose, float t)
	{
		Pose6D res = new Pose6D();

		res.x = xInterpolator.interpolate(initialPose.x, targetPose.x, t);
		res.y = yInterpolator.interpolate(initialPose.y, targetPose.y, t);
		res.z = zInterpolator.interpolate(initialPose.z, targetPose.z, t);

		res.xAngle = xRotInterpolator.interpolate(initialPose.xAngle, targetPose.xAngle, t);
		res.yAngle = yRotInterpolator.interpolate(initialPose.yAngle, targetPose.yAngle, t);
		res.zAngle = zRotInterpolator.interpolate(initialPose.zAngle, targetPose.zAngle, t);

		return res;
	}
}
