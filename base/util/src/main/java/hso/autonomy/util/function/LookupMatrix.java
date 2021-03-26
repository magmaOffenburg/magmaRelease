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
public class LookupMatrix
{
	/** the value matrix */
	private float[][] matrix;

	/**
	 * Reads a lookup matrix from file
	 * @param filename the file to read
	 */
	/**
	 * how many values per int are in the matrix. So if the step size is 0.5,
	 * scalex = 2
	 */
	private double scaleX;

	private double scaleY;

	/**
	 * the displacement of the first value from 0. So if the values start from
	 * -40 offsetX is 40
	 */
	private double offsetX;

	private double offsetY;

	public LookupMatrix(String filename) throws FileNotFoundException
	{
		readFromFile(filename);
	}

	/**
	 * For test purposes
	 */
	public LookupMatrix(float[][] matrix, double scaleX, double scaleY, double offsetX, double offsetY)
	{
		this.matrix = matrix;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public LookupMatrix(int lengthX, int lengthY, double scaleX2, double scaleY2, double offsetX2, double offsetY2)
	{
		this(new float[lengthX][lengthY], scaleX2, scaleY2, offsetX2, offsetY2);
	}

	public double getValue(double x, double y)
	{
		double xMapped = getXIndex(x);
		double yMapped = getYIndex(y);
		return biLinearInterpolation(xMapped, yMapped);
	}

	protected double biLinearInterpolation(double x, double y)
	{
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		int xInt = (int) x;
		int yInt = (int) y;

		if (xInt >= matrix.length - 1) {
			xInt = matrix.length - 1;
			if (yInt >= matrix[0].length - 1) {
				// the point is outside both areas
				return matrix[xInt][matrix[0].length - 1];
			} else {
				// the point is outside x area
				return Geometry.linearInterpolation(yInt, matrix[xInt][yInt], yInt + 1, matrix[xInt][yInt + 1], y);
			}
		}

		if (yInt >= matrix[0].length - 1) {
			// the point is outside y area
			yInt = matrix[0].length - 1;
			return Geometry.linearInterpolation(xInt, matrix[xInt][yInt], xInt + 1, matrix[xInt + 1][yInt], x);
		}

		double xinter1 = Geometry.linearInterpolation(xInt, matrix[xInt][yInt], xInt + 1, matrix[xInt + 1][yInt], x);
		double xinter2 =
				Geometry.linearInterpolation(xInt, matrix[xInt][yInt + 1], xInt + 1, matrix[xInt + 1][yInt + 1], x);
		return Geometry.linearInterpolation(yInt, xinter1, yInt + 1, xinter2, y);
	}

	private double getXIndex(double index)
	{
		return (index + offsetX) * scaleX;
	}

	private double getYIndex(double index)
	{
		return (index + offsetY) * scaleY;
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
			offsetX = Double.valueOf(csvLineContents[index++]);
			offsetY = Double.valueOf(csvLineContents[index++]);
			scaleX = Double.valueOf(csvLineContents[index++]);
			scaleY = Double.valueOf(csvLineContents[index++]);
			matrix = new float[lengthX][lengthY];

			readContent(in2, lengthX, lengthY);

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * @throws IOException
	 */
	protected void readContent(BufferedReader in2, int lengthX, int lengthY) throws IOException
	{
		// read all data for each value
		for (int x = 0; x < lengthX; x++) {
			String line = in2.readLine();
			String[] csvLineContents = line.split(";");
			if (csvLineContents.length != lengthY) {
				System.err.println("Invalid number of values: " + csvLineContents.length + "! Expected: " + lengthY);
				System.exit(-1);
			}
			for (int y = 0; y < lengthY; y++) {
				float value = Float.valueOf(csvLineContents[y]);
				matrix[x][y] = value;
			}
		}
	}
}
