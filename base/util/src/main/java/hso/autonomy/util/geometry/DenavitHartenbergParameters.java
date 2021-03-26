/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import java.io.Serializable;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Holds parameters needed to perform Denavit/Hartenberg Transformations
 * @author Ingo Schindler
 *
 */
public class DenavitHartenbergParameters implements Serializable
{
	private final double alpha;

	private final double beta;

	private double theta;

	private final Vector3D distance;

	/**
	 * Constructor
	 *
	 * @param theta Theta value
	 * @param alpha Alpha value
	 * @param beta Beta value
	 * @param distance Distance
	 */
	public DenavitHartenbergParameters(double theta, double alpha, double beta, Vector3D distance)
	{
		this.theta = theta;
		this.alpha = alpha;
		this.beta = beta;

		this.distance = distance;
	}

	/**
	 * Get Alpha value in radians
	 *
	 * @return Value
	 */
	public double getAlpha()
	{
		return Math.toRadians(alpha);
	}

	/**
	 * Get Beta value in radians
	 *
	 * @return Value
	 */
	public double getBeta()
	{
		return Math.toRadians(beta);
	}

	/**
	 * Get Theta value in radians
	 *
	 * @return Value
	 */
	public double getTheta()
	{
		return Math.toRadians(theta);
	}

	/**
	 * Set Theta value
	 *
	 * @param value New value
	 */
	public void setTheta(double value)
	{
		theta = value;
	}

	/**
	 * Get distance in 3-dimensional space
	 *
	 * @return Distance
	 */
	public Vector3D getDistance()
	{
		return distance;
	}
}
