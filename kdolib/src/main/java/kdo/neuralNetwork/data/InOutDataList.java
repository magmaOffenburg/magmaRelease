/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.neuralNetwork.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import kdo.util.IRandomSource;

/**
 * Represents a list of input and expected output data
 */
public class InOutDataList
{
	/** the list of data */
	private List<InOutPattern> data;

	public InOutDataList()
	{
		data = new ArrayList<>();
	}

	public InOutDataList(List<InOutPattern> result)
	{
		data = new ArrayList<>(result);
	}

	public InOutDataList(InOutPattern[] result)
	{
		data = new ArrayList<>(Arrays.asList(result));
	}

	/**
	 * @return an unmodifiable list of all data
	 */
	public List<InOutPattern> getData()
	{
		return Collections.unmodifiableList(data);
	}

	/**
	 *
	 * @param fromIndex low endpoint (inclusive) of the subList
	 * @param toIndex high endpoint (exclusive) of the subList
	 * @return an unmodifiable subset of the list of all data
	 */
	public InOutDataList getSubset(int fromIndex, int toIndex)
	{
		return new InOutDataList(data.subList(fromIndex, toIndex));
	}

	/**
	 *
	 * @param rand
	 * @param howMany
	 * @return an array of two lists with InOutPatterns. 0: the random sample, 1:
	 *         the examples not selected
	 */
	public InOutDataList[] getRandomSubset(IRandomSource rand, int howMany)
	{
		if (howMany > data.size()) {
			throw new IllegalArgumentException("subset too large: " + howMany + " max: " + data.size());
		}

		List<InOutPattern> nonSelected = new ArrayList<>(data);
		List<InOutPattern> selected = new ArrayList<>(howMany);
		for (int i = 0; i < howMany; i++) {
			int index = rand.nextInt(nonSelected.size());
			selected.add(nonSelected.remove(index));
		}
		InOutDataList[] result = new InOutDataList[2];
		result[0] = new InOutDataList(selected);
		result[1] = new InOutDataList(nonSelected);
		return result;
	}

	/**
	 * Appends the passed patterns to the list of existing patterns
	 * @param other the data list to add
	 */
	public void merge(InOutDataList other)
	{
		data.addAll(other.data);
	}

	/**
	 * Shuffles the order of input output patterns
	 */
	public void shuffle(Random rand)
	{
		Collections.shuffle(data, rand);
	}

	/**
	 * @return the number of input entries in the first pattern, -1 if no pattern
	 *         in the list
	 */
	public int getInputCount()
	{
		if (data.isEmpty()) {
			return -1;
		}
		return data.get(0).getInputCount();
	}

	/**
	 * @return the number of output entries in the first pattern, -1 if no
	 *         pattern in the list
	 */
	public int getOutputCount()
	{
		if (data.isEmpty()) {
			return -1;
		}
		return data.get(0).getOutputCount();
	}

	/**
	 * @return the number of in-out patterns in the list
	 */
	public int size()
	{
		return data.size();
	}

	public static InOutDataList getAndPattern()
	{
		List<InOutPattern> result = new ArrayList<>();
		result.add(new InOutPattern(new float[] {1.0f, 1.0f}, new float[] {1.0f}));
		result.add(new InOutPattern(new float[] {1.0f, 0.0f}, new float[] {0.0f}));
		result.add(new InOutPattern(new float[] {0.0f, 1.0f}, new float[] {0.0f}));
		result.add(new InOutPattern(new float[] {0.0f, 0.0f}, new float[] {0.0f}));
		// bias as weight
		// result.add(new InOutPattern(new float[] { 1.0f, 1.0f, 1.0f }, new
		// float[] { 1.0f }));
		// result.add(new InOutPattern(new float[] { 1.0f, 1.0f, 0.0f }, new
		// float[] { 0.0f }));
		// result.add(new InOutPattern(new float[] { 1.0f, 0.0f, 1.0f }, new
		// float[] { 0.0f }));
		// result.add(new InOutPattern(new float[] { 1.0f, 0.0f, 0.0f }, new
		// float[] { 0.0f }));
		return new InOutDataList(result);
	}

	public static InOutDataList getOrPattern()
	{
		List<InOutPattern> result = new ArrayList<>();
		result.add(new InOutPattern(new float[] {1.0f, 1.0f}, new float[] {1.0f}));
		result.add(new InOutPattern(new float[] {1.0f, 0.0f}, new float[] {1.0f}));
		result.add(new InOutPattern(new float[] {0.0f, 1.0f}, new float[] {1.0f}));
		result.add(new InOutPattern(new float[] {0.0f, 0.0f}, new float[] {0.0f}));
		// bias as weight
		// result.add(new InOutPattern(new float[] { 1.0f, 1.0f, 1.0f }, new
		// float[] { 1.0f }));
		// result.add(new InOutPattern(new float[] { 1.0f, 1.0f, 0.0f }, new
		// float[] { 1.0f }));
		// result.add(new InOutPattern(new float[] { 1.0f, 0.0f, 1.0f }, new
		// float[] { 1.0f }));
		// result.add(new InOutPattern(new float[] { 1.0f, 0.0f, 0.0f }, new
		// float[] { 0.0f }));
		return new InOutDataList(result);
	}

	public static InOutDataList getXorPattern()
	{
		List<InOutPattern> result = new ArrayList<>();
		result.add(new InOutPattern(new float[] {1.0f, 1.0f}, new float[] {0.0f}));
		result.add(new InOutPattern(new float[] {1.0f, 0.0f}, new float[] {1.0f}));
		result.add(new InOutPattern(new float[] {0.0f, 1.0f}, new float[] {1.0f}));
		result.add(new InOutPattern(new float[] {0.0f, 0.0f}, new float[] {0.0f}));
		// bias as weight
		// result.add(new InOutPattern(new float[] {1.0f, 1.0f, 1.0f}, new float[]
		// {0.0f}));
		// result.add(new InOutPattern(new float[] {1.0f, 1.0f, 0.0f}, new float[]
		// {1.0f}));
		// result.add(new InOutPattern(new float[] {1.0f, 0.0f, 1.0f}, new float[]
		// {1.0f}));
		// result.add(new InOutPattern(new float[] {1.0f, 0.0f, 0.0f}, new float[]
		// {0.0f}));
		return new InOutDataList(result);
	}

	public static InOutDataList getNotPattern()
	{
		InOutPattern[] result = {new InOutPattern(new float[] {1.0f}, new float[] {0.0f}),
				new InOutPattern(new float[] {0.0f}, new float[] {1.0f})};
		return new InOutDataList(result);
	}

	public static InOutDataList getLecturePattern()
	{
		InOutPattern[] result = {new InOutPattern(new float[] {1.0f, 1.0f}, new float[] {1.0f})};
		return new InOutDataList(result);
	}

	/**
	 * Reads a SSSN network file format
	 * @param filename the file to read
	 * @return the input output patterns of the file
	 */
	public static InOutDataList getPatternsFromFile(String filename)
	{
		BufferedReader in = null;
		String data;
		StringTokenizer tokenizer;
		String token;
		List<InOutPattern> result = new ArrayList<>();

		try {
			ClassLoader cl = InOutDataList.class.getClassLoader();
			in = new BufferedReader(new InputStreamReader(cl.getResourceAsStream(filename)));

			// read over header
			for (int i = 0; i < 4; i++) {
				data = in.readLine();
			}

			// number of patterns
			int noOfPatterns = readSize(in);
			int noOfInputs = readSize(in);
			int noOfOutputs = readSize(in);
			data = in.readLine();

			for (int pattern = 0; pattern < noOfPatterns; pattern++) {
				// read input
				float[] input = new float[noOfInputs];
				data = readOverCommentLines(in);
				tokenizer = new StringTokenizer(data, " ");
				for (int i = 0; i < noOfInputs; i++) {
					token = tokenizer.nextToken();
					input[i] = Float.valueOf(token).floatValue();
				}

				float[] output = new float[noOfOutputs];
				if (noOfOutputs > 0) {
					// read output
					data = readOverCommentLines(in);
					tokenizer = new StringTokenizer(data, " ");
					for (int i = 0; i < noOfOutputs; i++) {
						token = tokenizer.nextToken();
						output[i] = Float.valueOf(token).floatValue();
					}
				}
				result.add(new InOutPattern(input, output));
			}

		} catch (IOException e) {
			System.out.println("Could not open file for reading! " + e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				System.out.println("Could not close file! " + e);
			}
		}
		return new InOutDataList(result);
	}

	/**
	 * Reads a decision tree comma or tab separated file format
	 * @param filename the file to read
	 * @return the input output patterns of the file
	 */
	public static InOutDataList getPatternsFromDecisionTreeFile(String dataFilename, String domFilename)
	{
		Map<String, List<String>> attributes = readDomFile(domFilename);
		List<InOutPattern> result = readDataFile(dataFilename, attributes);
		return new InOutDataList(result);
	}

	private static Map<String, List<String>> readDomFile(String domFilename)
	{
		BufferedReader in = null;
		String data;
		Map<String, List<String>> attributes = new HashMap<>();
		try {
			in = new BufferedReader(new FileReader(domFilename));
			do {
				data = in.readLine();
			} while (!data.startsWith("dom"));

			do {
				StringTokenizer tokenizer = new StringTokenizer(data, " ()");
				// should be keyword dom
				String token = tokenizer.nextToken();
				// read name of attribute
				String attributeName = tokenizer.nextToken();

				// read over brackets
				token = tokenizer.nextToken();
				token = tokenizer.nextToken();

				List<String> values = new ArrayList<>();
				while (!(token = tokenizer.nextToken()).contains("}")) {
					// parse attribute values
					token = token.replace(",", "");
					values.add(token);
				}
				attributes.put(attributeName, values);

			} while ((data = in.readLine()) != null);

		} catch (IOException e) {
			System.out.println("Could not open file for reading! " + e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				System.out.println("Could not close file! " + e);
			}
		}
		return attributes;
	}

	// currently only working for categorical data!!
	private static List<InOutPattern> readDataFile(String dataFilename, Map<String, List<String>> attributes)
	{
		BufferedReader in = null;
		String data;
		List<InOutPattern> result = new ArrayList<>();
		try {
			in = new BufferedReader(new FileReader(dataFilename));

			// read attribute names line
			data = in.readLine();
			StringTokenizer tokenizer = new StringTokenizer(data, ",");
			List<String> names = new ArrayList<>();
			while (tokenizer.hasMoreTokens()) {
				// parse attribute names
				String token = tokenizer.nextToken();
				names.add(token);
			}

			while ((data = in.readLine()) != null) {
				tokenizer = new StringTokenizer(data, ",");
				List<Float> netInput = new ArrayList<>();
				List<Float> netOutput = new ArrayList<>();
				int element = 0;
				while (tokenizer.hasMoreTokens()) {
					// parse attribute values
					String token = tokenizer.nextToken();
					String attribute = names.get(element);
					List<String> values = attributes.get(attribute);
					if (element < attributes.size() - 1) {
						netInput.addAll(encode(token, values));
					} else {
						netOutput.addAll(encode(token, values));
					}
					element++;
				}

				float[] inp = toArray(netInput);
				float[] outp = toArray(netOutput);
				InOutPattern pattern = new InOutPattern(inp, outp);
				result.add(pattern);
			}
		} catch (IOException e) {
			System.out.println("Could not open file for reading! " + e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				System.out.println("Could not close file! " + e);
			}
		}
		return result;
	}

	private static List<Float> encode(String value, List<String> possibleAttributeValues)
	{
		// we do a simple position based encoding for now
		List<Float> result = new ArrayList<>();
		int index = possibleAttributeValues.indexOf(value);
		if (index < 0) {
			throw new IllegalArgumentException(
					"Attribute value not listed in dom file: " + value + " values: " + possibleAttributeValues);
		}

		for (int i = 0; i < possibleAttributeValues.size(); i++) {
			if (i == index) {
				result.add(Float.valueOf(1.0f));
			} else {
				result.add(Float.valueOf(0.0f));
			}
		}
		return result;
	}

	private static float[] toArray(List<Float> floatList)
	{
		float[] result = new float[floatList.size()];
		int i = 0;
		for (Float entry : floatList) {
			result[i] = entry.floatValue();
			i++;
		}
		return result;
	}

	private static String readOverCommentLines(BufferedReader in) throws IOException
	{
		String data;
		do {
			data = in.readLine();
		} while (data.startsWith("#"));
		return data;
	}

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static int readSize(BufferedReader in) throws IOException
	{
		StringTokenizer tokenizer;
		String data = in.readLine();
		tokenizer = new StringTokenizer(data, ":");
		tokenizer.nextToken();
		return Integer.valueOf(tokenizer.nextToken().trim()).intValue();
	}

	/**
	 *
	 * @param example the dataset to write
	 */
	public static void writeRandomDataFile(
			String dataFilename, String newFilename, String learnFilename, float probability, IRandomSource rand)
	{
		BufferedReader in = null;
		PrintWriter out = null;
		PrintWriter learnOut = null;
		try {
			in = new BufferedReader(new FileReader(dataFilename));
			out = new PrintWriter(new FileWriter(newFilename));
			if (learnFilename != null) {
				learnOut = new PrintWriter(new FileWriter(learnFilename));
			}

			// attribute line
			String data = in.readLine();
			out.println(data);
			if (learnFilename != null) {
				learnOut.println(data);
			}

			while ((data = in.readLine()) != null) {
				if (rand.nextFloat() < probability) {
					out.println(data);
				} else {
					if (learnFilename != null) {
						learnOut.println(data);
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Could not open file for reading! " + e);
		} finally {
			try {
				in.close();
				out.close();
				if (learnFilename != null) {
					learnOut.close();
				}
			} catch (IOException e) {
				System.out.println("Could not close file! " + e);
			}
		}
	}

	public Iterator<List<InOutPattern>> iterator(int batchSize)
	{
		return new DataListIterator(batchSize);
	}

	class DataListIterator implements Iterator<List<InOutPattern>>
	{
		private int batchSize;

		private int nextBatchStart;

		public DataListIterator(int batchSize)
		{
			this.batchSize = batchSize;
			nextBatchStart = 0;
		}

		@Override
		public boolean hasNext()
		{
			return nextBatchStart < data.size();
		}

		@Override
		public List<InOutPattern> next()
		{
			int endIndex = Math.min(data.size(), nextBatchStart + batchSize);
			List<InOutPattern> result = data.subList(nextBatchStart, endIndex);
			nextBatchStart = endIndex;
			return result;
		}
	}
}
