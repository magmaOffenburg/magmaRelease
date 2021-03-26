/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

/**
 * @author kdorer
 *
 */
public class Polynomial5DegreeInterpolator
{
	private double a0;

	private double a1;

	private double a2;

	private double a3;

	private double a4;

	private double a5;

	public Polynomial5DegreeInterpolator(
			double pos0, double speed0, double acc0, double posf, double speedf, double accf, double tf)
	{
		double tf2 = tf * tf;
		double tf3 = tf * tf2;
		double tf4 = tf2 * tf2;
		double tf5 = tf * tf4;
		a0 = pos0;
		a1 = speed0;
		a2 = acc0 * 0.5;
		a3 = (20 * posf - 20 * pos0 - (8 * speedf + 12 * speed0) * tf - (3 * acc0 - accf) * tf2) / (2.0 * tf3);
		a4 = (30 * pos0 - 30 * posf + (14 * speedf + 16 * speed0) * tf + (3 * acc0 - 2 * accf) * tf2) / (2.0 * tf4);
		a5 = (12 * posf - 12 * pos0 - (6 * speedf + 6 * speed0) * tf - (acc0 - accf) * tf2) / (2.0 * tf5);
	}

	public double value(double x)
	{
		double x2 = x * x;
		double x4 = x2 * x2;
		return a0 + a1 * x + a2 * x2 + a3 * x2 * x + a4 * x4 + a5 * x4 * x;
	}
}
