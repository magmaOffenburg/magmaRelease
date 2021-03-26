/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author kdorer
 *
 */
public class Polynomial5DegreeInterpolatorTest
{
	/**
	 * Test method for
	 * {@link hso.autonomy.util.function.Polynomial5DegreeInterpolator#value(double)}.
	 */
	@Test
	public void testValue()
	{
		Polynomial5DegreeInterpolator testee = new Polynomial5DegreeInterpolator(0, 1, 0, 1, 1, 0, 1);
		assertEquals(0.5, testee.value(0.5), 0.001);
		assertEquals(0.1, testee.value(0.1), 0.001);
		assertEquals(0.9, testee.value(0.9), 0.001);
	}

	public static void main(String[] args)
	{
		// SupportPointFunction function = new SinFunction((float) (Math.PI *
		// 2.0),
		// 1.0f, 0, 0.0f, 1);

		double[] supportPoints = new double[] {0, -2, 4, 2, 5, 2, 6, 0};
		SupportPointFunction function = new PiecewiseBezierFunction(SupportPoint.fromArray(supportPoints), 1);

		double tf = 4;
		Polynomial5DegreeInterpolator testee =
				new Polynomial5DegreeInterpolator(function.value(0), function.derivative(0), function.derivative2(0),
						function.value(tf), function.derivative(tf), function.derivative2(tf), tf);

		System.out.println("x;interpolated;real");

		for (double x = 0.0; x < tf; x += 0.01) {
			double interpolated = testee.value(x);
			double real = function.value(x);
			System.out.println(x + ";" + interpolated + ";" + real);
		}
	}
}
