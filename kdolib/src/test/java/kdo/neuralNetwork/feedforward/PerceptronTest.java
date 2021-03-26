/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork.feedforward;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import kdo.neuralNetwork.data.InOutDataList;
import kdo.neuralNetwork.data.InOutPattern;
import kdo.neuralNetwork.optimizer.IOptimizer;
import kdo.neuralNetwork.optimizer.SGDOptimizer;
import kdo.util.FixRandomSource;
import kdo.util.IRandomSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author dorer
 *
 */
public class PerceptronTest
{
	private Perceptron testee;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		IRandomSource rand = new FixRandomSource(0.8);
		IOptimizer optimizer = new SGDOptimizer(1.0f);
		testee = new Perceptron(2, 1, rand);
		testee.setOptimizer(optimizer);
	}

	/**
	 * Test method for
	 * {@link kdo.neuralNetwork.feedforward.Backpropagation#recall(float[])}.
	 */
	@Test
	public void testRecall()
	{
		float[] input = {1, 0};
		float[] expectedOutput = {1.0f};
		float[] output = testee.recall(input);
		checkOutput(expectedOutput, output);
	}

	/**
	 * Test method for
	 * {@link kdo.neuralNetwork.feedforward.Backpropagation#train(float[], float[])}
	 * .
	 */
	@Test
	public void testTrain()
	{
		float[] input = {1, 0};
		float[] goalOutput = {0f};
		List<InOutPattern> batch = Collections.singletonList(new InOutPattern(input, goalOutput));
		float error = testee.train(batch);
		assertEquals(1.0, error, 0.001);
		float[] expectedOutput = {0f};
		float[] output = testee.recall(input);
		checkOutput(expectedOutput, output);
	}

	@Test
	public void testTrainAnd()
	{
		InOutDataList example = InOutDataList.getAndPattern();
		testee.setLearnrate(0.1f);
		double error = testee.trainAll(example, 1, 100, 0.001, -1, new Random(0));
		assertTrue(error <= 0.001, "Perceptron did not learn and");
	}

	private void checkOutput(float[] expected, float[] output)
	{
		for (int i = 0; i < output.length; i++) {
			assertEquals(expected[i], output[i], 0.001, "value: " + i + " unexpected");
		}
	}
}
