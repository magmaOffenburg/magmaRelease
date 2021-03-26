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
public class LookupMatrix3D
{
	/** the value matrix */
	private LookupMatrix[] lookup;

	/**
	 * Reads a lookup matrix from file
	 * @param filename the file to read
	 */
	/**
	 * how many values per int are in the matrix. So if the step size is 0.5,
	 * scalez = 2
	 */
	private double scaleX;

	/**
	 * the displacement of the first value from 0. So if the values start from
	 * -40 offsetZ is 40
	 */
	private double offsetX;

	/** the value that is added to the final result to adjust zero point */
	private double valueOffset;

	public LookupMatrix3D(String filename) throws FileNotFoundException
	{
		readFromFile(filename);
	}

	/**
	 * For test purposes
	 */
	public LookupMatrix3D(float[][][] matrix, double scaleX, double scaleY, double scaleZ, double offsetX,
			double offsetY, double offsetZ)
	{
		lookup = new LookupMatrix[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			lookup[i] = new LookupMatrix(matrix[i], scaleY, scaleZ, offsetY, offsetZ);
		}
		this.scaleX = scaleX;
		this.offsetX = offsetX;
	}

	public double getValue(double x, double y, double z)
	{
		double xMapped = getXIndex(x);
		return valueOffset + linearInterpolation(xMapped, y, z);
	}

	protected double linearInterpolation(double x, double y, double z)
	{
		if (x < 0) {
			x = 0;
		}
		int xInt = (int) x;

		if (xInt >= lookup.length - 1) {
			xInt = lookup.length - 1;
			// the point is outside x area
			return lookup[xInt].getValue(y, z);
		}

		double value1 = lookup[xInt].getValue(y, z);
		double value2 = lookup[xInt + 1].getValue(y, z);

		return Geometry.linearInterpolation(xInt, value1, xInt + 1, value2, x);
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
		System.out.println("Read file: " + filename);
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
			int lengthY = Integer.valueOf(csvLineContents[index++]);
			int lengthZ = Integer.valueOf(csvLineContents[index++]);
			offsetX = Double.valueOf(csvLineContents[index++]);
			double offsetY = Double.valueOf(csvLineContents[index++]);
			double offsetZ = Double.valueOf(csvLineContents[index++]);
			scaleX = Double.valueOf(csvLineContents[index++]);
			double scaleY = Double.valueOf(csvLineContents[index++]);
			double scaleZ = Double.valueOf(csvLineContents[index++]);
			valueOffset = Double.valueOf(csvLineContents[index++]);

			lookup = new LookupMatrix[lengthX];
			for (int i = 0; i < lengthX; i++) {
				lookup[i] = new LookupMatrix(lengthY, lengthZ, scaleY, scaleZ, offsetY, offsetZ);
				lookup[i].readContent(in2, lengthY, lengthZ);
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
