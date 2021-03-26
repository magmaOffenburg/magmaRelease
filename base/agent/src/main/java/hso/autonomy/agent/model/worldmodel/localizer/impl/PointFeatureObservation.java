/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer.impl;

import hso.autonomy.agent.model.worldmodel.localizer.IPointFeature;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeatureObservation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Class for holding a (possibly unlabeled) point feature observation, which can be used for localization.
 *
 * @author Stefan Glaser
 */
public class PointFeatureObservation extends FeatureObservationBase implements IPointFeatureObservation
{
	/** The observed position on the point. */
	private Vector3D position;

	/**
	 * Creates a new instance with the given position and type and no name.
	 *
	 * @param time the time of this observation
	 * @param type the observed feature type information
	 * @param position the observed position of the point
	 * @param hasDepth true, if the position contains full 3D information (including depth), false if it only contains
	 * directional information
	 */
	public PointFeatureObservation(double time, String type, Vector3D position, boolean hasDepth)
	{
		this(time, type, position, hasDepth, null);
	}

	/**
	 * Creates a new instance for the given type, name and position.
	 *
	 * @param time the time of this observation
	 * @param type the type of the point feature (e.g. flag, post, etc.)
	 * @param position the observed position of the point
	 * @param hasDepth true, if the position contains full 3D information (including depth), false if it only contains
	 * directional information
	 * @param name the unique name of the point observation, corresponding to a known {@link IPointFeature}.
	 */
	public PointFeatureObservation(double time, String type, Vector3D position, boolean hasDepth, String name)
	{
		super(time, type, hasDepth, name);

		this.position = position;
	}

	@Override
	public Vector3D getObservedPosition()
	{
		return position;
	}

	@Override
	public void assignDepthInfo(Vector3D position)
	{
		this.position = position;
		this.hasDepth = true;
	}

	@Override
	public String toString()
	{
		return super.toString() + " @ " + position;
	}
}
