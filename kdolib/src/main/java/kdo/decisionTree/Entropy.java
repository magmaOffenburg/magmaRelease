/*
 * Copyright (c) 2012 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.decisionTree;

/**
 * @author kdorer
 *
 */
public class Entropy
{
	public static void main(String[] args)
	{
		// number of instances with corresponding value
		int[] attributes = {6, 4, 4};
		double entropy = calculateEntropy(attributes);
		System.out.println("Entropie: " + entropy);

		// outer: classifier inner: attribute value
		// int[][] subclass = { { 0, 1, 1 }, { 0, 1, 1 }, { 1, 0, 1 }, { 3, 0, 1 }
		// };
		// int[][] subclass = { { 2, 2, 3 }, { 1, 1, 0 }, { 1, 0, 0 } };
		int[][] subclass = {{4, 3, 1}, {0, 3, 3}};
		// int[][] subclass = { { 1, 2, 2 }, { 3, 0, 2 } };
		double gain = calculateGain(entropy, subclass);
		System.out.println("Gain: " + gain);

		//
		int[] data = {6, 8};
		// int[] data = { 2, 2, 2, 4 };
		double gainRatio = calculateGainRatio(gain, data);
		System.out.println("GainRatio: " + gainRatio);
	}

	public static double calculateEntropy(int[] attributes)
	{
		double log2 = Math.log(2);
		double entropy = 0;
		int sum = getSum(attributes);
		for (int i = 0; i < attributes.length; i++) {
			double probability = attributes[i] / (double) sum;
			if (probability > 0) {
				entropy += -probability * (Math.log(probability) / log2);
			}
		}

		return entropy;
	}

	/**
	 * @param attributes
	 * @return
	 */
	private static int getSum(int[] attributes)
	{
		int sum = 0;
		for (int i = 0; i < attributes.length; i++) {
			sum += attributes[i];
		}
		return sum;
	}

	public static double calculateGain(double entropy, int[][] data)
	{
		double gain = entropy;

		int fullSum = 0;
		for (int i = 0; i < data.length; i++) {
			fullSum += getSum(data[i]);
		}

		for (int i = 0; i < data.length; i++) {
			double subEntropy = calculateEntropy(data[i]);
			double sum = getSum(data[i]);
			gain += -(sum / fullSum) * subEntropy;
		}

		return gain;
	}

	public static double calculateGainRatio(double gain, int[] data)
	{
		double entropy = calculateEntropy(data);
		System.out.println("Information Content: " + entropy);
		return gain / entropy;
	}
}
