/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package magma.agent.model.thoughtmodel;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * An estimation for utility to kick to a single point on the field
 * @author kdorer
 */
public class KickPositionEstimation implements Comparable<KickPositionEstimation>
{
	/** the represented position */
	public final Vector3D position;

	/** the utility of this point */
	public final float utility;

	public KickPositionEstimation(Vector3D position, float utility)
	{
		super();
		this.position = position;
		this.utility = utility;
	}

	@Override
	public int compareTo(KickPositionEstimation o)
	{
		float delta = o.utility - utility;
		if (delta < 0) {
			return -1;
		} else if (delta > 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public String toString()
	{
		return "utility: " + utility + " pos: " + position;
	}
}
