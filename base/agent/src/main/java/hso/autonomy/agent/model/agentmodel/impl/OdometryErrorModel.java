/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.model.agentmodel.IOdometryErrorModel;
import hso.autonomy.util.geometry.Angle;
import java.util.Random;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public abstract class OdometryErrorModel implements IOdometryErrorModel
{
	/** The standard deviation for the x component of the odometry information (in mtr) */
	protected double stdDeviationX;

	/** The standard deviation for the y component of the odometry information (in mtr) */
	protected double stdDeviationY;

	/** The standard deviation for the horizontal angle of the odometry information (in degrees) */
	protected double stdDeviationTheta;

	private Random random;

	public OdometryErrorModel(double stdDeviationX, double stdDeviationY, double stdDeviationTheta)
	{
		this.stdDeviationX = stdDeviationX;
		this.stdDeviationY = stdDeviationY;
		this.stdDeviationTheta = stdDeviationTheta;
		random = new Random();
	}

	@Override
	public Vector2D applyNoiseToTrans(Vector2D trans)
	{
		double noiseX = applyNoise(stdDeviationX, trans.getX());
		double noiseY = applyNoise(stdDeviationY, trans.getY());
		return new Vector2D(noiseX, noiseY);
	}

	@Override
	public Angle applyNoiseToRotation(Angle deltaZAngle)
	{
		return Angle.deg(applyNoise(stdDeviationTheta, deltaZAngle.degrees()));
	}

	protected double applyNoise(double stdDeviation, double input)
	{
		return random.nextGaussian() * stdDeviation + input;
	}
}
