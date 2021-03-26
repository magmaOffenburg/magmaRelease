/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.model.agentmodel.IFieldOfView;
import hso.autonomy.util.geometry.Angle;
import java.io.Serializable;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Simple class representing the field of view of a camera, lidar, radar, etc.
 *
 * @author Stefan Glaser
 */
public class FieldOfView implements Serializable, IFieldOfView
{
	/** The horizontal view of view range (+/- angle). */
	public final Angle horizontal;

	/** The vertical view of view range (+/- angle). */
	public final Angle vertical;

	/**
	 * Construct a new field of view from the given horizontal and vertical angles.<br>
	 * <b>Node:</b> The horizontal/vertical angles are interpreted as a +/- range. This means if the total
	 * horizontal/vertical field of view is 120 degrees, the required angle argument is a 60 degrees angle.
	 *
	 * @param horizontal the horizontal field of view angle
	 * @param vertical the vertical field of view angle
	 */
	public FieldOfView(Angle horizontal, Angle vertical)
	{
		this.horizontal = horizontal;
		this.vertical = vertical;
	}

	@Override
	public Angle horizontal()
	{
		return horizontal;
	}

	@Override
	public Angle vertical()
	{
		return vertical;
	}

	@Override
	public boolean isInside(Vector3D point)
	{
		return Math.abs(point.getAlpha()) < horizontal.radians() && Math.abs(point.getDelta()) < vertical.radians();
	}
}