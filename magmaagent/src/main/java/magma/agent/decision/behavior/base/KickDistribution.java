/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.base;

import hso.autonomy.util.geometry.Angle;
import java.io.Serializable;
import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * Probability distribution of kick distance and angle. Usually measured over
 * 1000 avgOutRuns with magmaLearning's KickProblem.
 */
public class KickDistribution implements Serializable
{
	public static final double DISTANCE_INTERVAL = 0.5;

	public static final double ANGLE_INTERVAL = 2;

	private final double[] distance;

	private final double[] angle;

	public KickDistribution(double kickDistance, Angle kickAngle)
	{
		NormalDistribution distanceDistribution = new NormalDistribution(kickDistance, 0.6);
		int size = (int) ((kickDistance + 3) / DISTANCE_INTERVAL);
		distance = new double[size];
		for (int i = 0; i < distance.length; i++) {
			distance[i] = distanceDistribution.density(i * DISTANCE_INTERVAL) * DISTANCE_INTERVAL;
		}

		NormalDistribution angleDistribution = new NormalDistribution(kickAngle.degrees(), 1);
		size = (int) ((kickAngle.degrees() + 30) / ANGLE_INTERVAL);
		angle = new double[size];
		for (int i = 0; i < angle.length; i++) {
			angle[i] = angleDistribution.density(i * ANGLE_INTERVAL - size) * ANGLE_INTERVAL;
		}
	}

	public KickDistribution(double[] distance, double[] angle)
	{
		this.distance = distance;
		this.angle = angle;
	}

	/** @param randomValue random value between 0 and 1 */
	public double getDistanceSample(double randomValue)
	{
		return sample(randomValue, distance) * DISTANCE_INTERVAL;
	}

	/** @param randomValue random value between 0 and 1 */
	public double getAngleSample(double randomValue)
	{
		return sample(randomValue, angle) * ANGLE_INTERVAL;
	}

	private double sample(double randomValue, double[] probabilities)
	{
		double sum = 0;

		for (int i = 0; i < probabilities.length; i++) {
			sum += probabilities[i];
			if (randomValue < sum) {
				return i;
			}
		}

		return probabilities.length - 1;
	}
}
