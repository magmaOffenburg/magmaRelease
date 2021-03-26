/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer.impl;

import java.util.List;

public class PFImportanceEvaluator
{
	public static double gauss(double sample, double mean, double stddev)
	{
		return 1.0 / Math.sqrt(2 * Math.pow(stddev, 2) * Math.PI) *
				Math.pow(Math.E, -Math.pow(sample - mean, 2) / (2 * Math.pow(stddev, 2)));
	}

	public static double calculateImportance(List<Double> values, double stdDev, double mean)
	{
		double product = gauss(values.get(0), mean, stdDev);
		for (int i = 1; i < values.size(); i++) {
			product *= gauss(values.get(i), mean, stdDev);
		}
		return product;
	}

	public static boolean isResamplingRequired(double[] probabilities, double threshold)
	{
		// this implementation calculates the effective sample size
		double essSum = 0.0;
		for (double probability : probabilities) {
			essSum += Math.pow((probabilities.length * probability) - 1, 2);
		}

		// estimate the number of "near-zero-weighted particles"
		double coefficientVariation = (1.0 / probabilities.length) * essSum;
		// update the sample size to check if resampling is required
		double effectiveSampleSize = probabilities.length / (1 + coefficientVariation);

		//		System.out.println("current ESS: " + effectiveSampleSize);
		return effectiveSampleSize < probabilities.length * threshold;
	}
}
