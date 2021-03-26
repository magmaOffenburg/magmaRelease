/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork.feedforward;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import kdo.neuralNetwork.INeuralNetwork;
import org.junit.jupiter.api.Test;

public class NetworkBuilderTest
{
	@Test
	public void testBuildNetwork()
	{
		List<String[]> content = new ArrayList<>();
		String[][] text = {
				//
				{"Backpropagation"},				   //
				{"Adam", "learnrate", "0.003"},		   //
				{"layer", "2", "3", "Step", "Hidden"}, //
				{"0", "1", "0"},					   // weights
				{"1", "0", "1"},					   //
				{"0", "1", "0"},					   // bias
				{"layer", "3", "1", "None", "Out"},	   //
				{"0"},								   // weights
				{"1"},								   //
				{"2"},								   //
				{"3"},								   // bias
		};

		for (String[] line : text) {
			content.add(line);
		}

		INeuralNetwork network = NetworkBuilder.buildNetwork(content);
		assertEquals(2, network.getNoOfLayers());
		assertEquals("Adam", network.getOptimizer().getName());
		float[] input = {1, -1};
		float[] recall = network.recall(input);
		assertEquals(4, recall[0], 0.0001);
	}
}
