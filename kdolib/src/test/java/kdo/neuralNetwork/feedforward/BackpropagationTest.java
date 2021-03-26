/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.neuralNetwork.feedforward;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import kdo.neuralNetwork.data.InOutDataList;
import kdo.neuralNetwork.data.InOutPattern;
import kdo.neuralNetwork.optimizer.IOptimizer;
import kdo.neuralNetwork.optimizer.SGDOptimizer;
import kdo.neuralNetwork.output.IdentityOutputFunction;
import kdo.neuralNetwork.transfer.ITransferFunction;
import kdo.neuralNetwork.transfer.SigmoidTransferFunction;
import kdo.util.FixRandomSource;
import kdo.util.IRandomSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author dorer
 *
 */
public class BackpropagationTest
{
	Backpropagation testee;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		IRandomSource rand = new FixRandomSource(1.0);
		IOptimizer optimizer = new SGDOptimizer(1.0f);
		ITransferFunction transfer = new SigmoidTransferFunction();
		int[] nodes = {2, 2, 1};
		testee = new Backpropagation(new IdentityOutputFunction());
		testee.setOptimizer(optimizer);
		testee.addLayers(nodes, transfer, rand);
	}

	/**
	 * Test method for
	 * {@link kdo.neuralNetwork.feedforward.Backpropagation#recall(float[])}.
	 */
	@Test
	public void testRecall()
	{
		float[] input = {1, 0};
		float[] expectedOutput = {0.94056f};
		float[] output = testee.recall(input);
		checkOutput(expectedOutput, output);
	}

	/**
	 * Test method for
	 * {@link kdo.neuralNetwork.feedforward.Backpropagation#train(float[], float[])}
	 * .
	 */
	@Test
	public void testTrainMinorAdaption()
	{
		float[] input = {1, 0};
		float[] expectedOutput = {0.94056f};
		List<InOutPattern> batch = Collections.singletonList(new InOutPattern(input, expectedOutput));
		float error = testee.train(batch);
		assertEquals(0.0, error, 0.001);
	}

	@Test
	public void testTrainMajorAdaption()
	{
		float[] input = {1, 0};
		float[] expectedOutput = {0.0f};
		List<InOutPattern> batch = Collections.singletonList(new InOutPattern(input, expectedOutput));
		testee.train(batch);
		System.out.println(testee);
		float[] output = testee.recall(input);
		assertEquals(0.932466, output[0], 0.001);
	}

	@Test
	public void testTrainAll()
	{
		float[] input = {1, 0};
		float[] expectedOutput = {0.0f};
		List<InOutPattern> batch = Collections.singletonList(new InOutPattern(input, expectedOutput));
		InOutDataList data = new InOutDataList(batch);
		testee.trainAll(data, 1, 1, 0.001, -1, new Random(0));
		System.out.println(testee);
		float[] output = testee.recall(input);
		assertEquals(0.932466, output[0], 0.001);
	}

	private void checkOutput(float[] expected, float[] output)
	{
		for (int i = 0; i < output.length; i++) {
			assertEquals(expected[i], output[i], 0.001, "value: " + i + " unexpected");
		}
	}
}
