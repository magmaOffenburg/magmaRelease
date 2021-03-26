/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import java.io.Serializable;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author kdorer
 *
 */
public class PoseSpeed2D implements Serializable
{
	/** position and orientation of an object */
	private IPose2D pose;

	/** the speed there in local coordinates */
	private Vector2D speed;

	public PoseSpeed2D(IPose2D pose, Vector2D speed)
	{
		this.pose = pose;
		this.speed = speed;
	}

	public IPose2D getPose()
	{
		return pose;
	}

	public Vector2D getSpeed()
	{
		return speed;
	}

	@Override
	public String toString()
	{
		return pose.toString() + " " + speed.toString();
	}
}
