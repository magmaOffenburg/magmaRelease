/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import java.util.ArrayList;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents a spline function. Adapter to the PolynomialSplineFunction class.
 *
 * @author Klaus Dorer
 */
public class SplineFunction extends SupportPointFunction
{
	/** name identifying a spline */
	public static final String NAME = "spline";

	private PolynomialSplineFunction spline;

	/**
	 * Creates a Spline Function with the given SupportPoints.
	 */
	public SplineFunction(double[] xValues, double[] yValues, float stiffness)
	{
		super(SupportPoint.fromArrays(xValues, yValues), stiffness);
	}

	/**
	 * Creates a Spline Function with the given SupportPoints.
	 */
	public SplineFunction(ArrayList<SupportPoint> points, float stiffness)
	{
		super(points, stiffness);
	}

	// /**
	// * Creates a Spline Function. Adapter to the PolynomialSplineFunction
	// class.
	// * The supporting points passed have to make sure that x0 is the minimum x
	// * value needed later and xn is the highest x value.
	// * @param xValues the x coordinates of the supporting points
	// * @param yValues the y coordinates of the supporting points
	// */
	// public SplineFunction(double xValues[], double yValues[])
	// {
	// this(xValues, yValues, 0.0);
	// }

	// /**
	// * Creates the function from a spline line in a motor function file
	// * @param csvLineContents the values that were comma separated in the file
	// */
	// public SplineFunction(String[] csvLineContents)
	// {
	// super(csvLineContents);
	// initialize();
	// }

	@Override
	public String getName()
	{
		return NAME;
	}

	// /**
	// * Creates a Spline Function. Adapter to the PolynomialSplineFunction
	// class.
	// * The supporting points passed have to make sure that x0 is the minimum x
	// * value needed later and xn is the highest x value.
	// * @param xValues the x coordinates of the supporting points
	// * @param yValues the y coordinates of the supporting points
	// * @param phase phase subtracted from x. Has to be within xMin and xMax
	// */
	// private SplineFunction(double xValues[], double yValues[], double phase)
	// {
	// super(xValues, yValues, phase);
	// initialize();
	// }

	/**
	 * Copy constructor
	 *
	 * @param source The source object to copy
	 */
	private SplineFunction(SplineFunction source)
	{
		super(source);
	}

	@Override
	protected int getMinimumSupportPointSize()
	{
		return 3;
	}

	@Override
	protected int getMaximumSupportPointSize()
	{
		return Integer.MAX_VALUE;
	}

	/**
	 * Creates a spline function from the previously set x and y values
	 */
	@Override
	public void initialize() throws IllegalArgumentException
	{
		super.initialize();

		double[] xValues = new double[supportPoints.size()];
		double[] yValues = new double[supportPoints.size()];

		for (int i = 0; i < supportPoints.size(); i++) {
			xValues[i] = supportPoints.get(i).x;
			yValues[i] = supportPoints.get(i).y;
		}

		SplineInterpolator interpolator = new SplineInterpolator();
		spline = interpolator.interpolate(xValues, yValues);
	}

	/**
	 * Returns the spline value at t.
	 *
	 * @param t the coordinate at which to return the functions value
	 * @return the spline value at coordinate t
	 */
	@Override
	public double value(double t)
	{
		// Catch border conditions
		if (t <= minXValue) {
			return supportPoints.get(0).y;
		} else if (t >= maxXValue) {
			return supportPoints.get(supportPoints.size() - 1).y;
		}

		// Try to interpolate
		try {
			return spline.value(t);
		} catch (OutOfRangeException e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	@Override
	public IFunction copy()
	{
		return new SplineFunction(this);
	}

	@Override
	public Vector2D moveTangentPointBeforeTo(int index, float x, float y)
	{
		return null;
	}

	@Override
	public Vector2D moveTangentPointAfterTo(int index, float x, float y)
	{
		return null;
	}
}