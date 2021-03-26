/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import hso.autonomy.util.geometry.Geometry;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Lookup of a 2D function from a matrix using bilinear interpolation.
 * @author kdorer
 */
public class LookupFunction
{
	/** the value matrix */
	private float[] data;

	/**
	 * Reads a lookup matrix from file
	 * @param filename the file to read
	 */
	/**
	 * how many values per int are in the matrix. So if the step size is 0.5,
	 * scaleX = 2
	 */
	private double scaleX;

	/**
	 * the displacement of the first value from 0. So if the values start from
	 * -40 offsetX is 40
	 */
	private double offsetX;

	/** the value that is added to the final result to adjust zero point */
	private double valueOffset;

	public LookupFunction(String filename) throws FileNotFoundException
	{
		readFromFile(filename);
	}

	/**
	 * For test purposes
	 */
	public LookupFunction(float[] data, double scaleX, double offsetX)
	{
		this.data = data;
		this.scaleX = scaleX;
		this.offsetX = offsetX;
	}

	public LookupFunction(int lengthX, double scaleX2, double offsetX2)
	{
		this(new float[lengthX], scaleX2, offsetX2);
	}

	public double getValue(double x)
	{
		double xMapped = getXIndex(x);
		return valueOffset + linearInterpolation(xMapped);
	}

	protected double linearInterpolation(double x)
	{
		if (x < 0) {
			return data[0];
		}
		int xInt = (int) x;

		if (xInt >= data.length - 1) {
			// the point is outside x area
			return data[data.length - 1];
		}
		return Geometry.linearInterpolation(xInt, data[xInt], xInt + 1, data[xInt + 1], x);
	}

	private double getXIndex(double index)
	{
		return (index + offsetX) * scaleX;
	}

	/**
	 * @throws FileNotFoundException
	 */
	private void readFromFile(String filename) throws FileNotFoundException
	{
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream is = cl.getResourceAsStream(filename);
		if (is == null) {
			throw new FileNotFoundException(filename);
		}
		BufferedReader in2 = new BufferedReader(new InputStreamReader(is));
		try {
			String line = in2.readLine();
			// read the date from the first line
			String[] csvLineContents = line.split(";");
			int index = 0;
			int lengthX = Integer.valueOf(csvLineContents[index++]);
			offsetX = Double.valueOf(csvLineContents[index++]);
			scaleX = Double.valueOf(csvLineContents[index++]);
			valueOffset = Double.valueOf(csvLineContents[index++]);

			data = new float[lengthX];
			readContent(in2, lengthX);

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * @throws IOException
	 */
	protected void readContent(BufferedReader in2, int lengthX) throws IOException
	{
		// read all data for each value
		String line = in2.readLine();
		String[] csvLineContents = line.split(";");
		if (csvLineContents.length != lengthX) {
			System.err.println("Invalid number of values: " + csvLineContents.length + "! Expected: " + lengthX);
			System.exit(-1);
		}
		for (int x = 0; x < lengthX; x++) {
			float value = Float.valueOf(csvLineContents[x]);
			data[x] = value;
		}
	}
}
