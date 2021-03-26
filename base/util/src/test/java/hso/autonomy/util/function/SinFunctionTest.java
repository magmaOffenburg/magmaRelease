/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Klaus Dorer
 *
 */
public class SinFunctionTest
{
	private SinFunction testee;

	/**
	 * Create a SinFunction from fixed parameters
	 *
	 * @return New object
	 */
	private SinFunction createFromParams()
	{
		return new SinFunction(1.5f, 8.1f, (float) (0.5673f * 1.5 / (2 * Math.PI)), -1.0f, 1);
	}

	@BeforeEach
	public void setup()
	{
		testee = new SinFunction((float) (Math.PI * 2.0), 1.0f, 0, 0.0f, 1);
	}

	/**
	 * Test method for {@link hso.autonomy.util.function.SinFunction#value(double)} .
	 */
	@Test
	public void testValuesParams()
	{
		testee = createFromParams();
		assertEquals(3.694, testee.value(-2.717), 0.0001);
		assertEquals(-5.3525, testee.value(0.0), 0.0001);
		assertEquals(7.0922, testee.value(5.0), 0.0001);
		assertEquals(-0.791, testee.value(Math.PI), 0.0001);
	}

	@Test
	public void testDerivative()
	{
		assertEquals(Math.cos(1.0), testee.derivative(1.0), 0.0001);
		assertEquals(Math.cos(1.5), testee.derivative(1.5), 0.0001);
		assertEquals(Math.cos(2.0), testee.derivative(2.0), 0.0001);
	}

	@Test
	public void testDerivative2()
	{
		assertEquals(-Math.sin(1.0), testee.derivative2(1.0), 0.0001);
		assertEquals(-Math.sin(1.5), testee.derivative2(1.5), 0.0001);
		assertEquals(-Math.sin(2.0), testee.derivative2(2.0), 0.0001);
	}

	public static void main(String[] args)
	{
		SupportPointFunction function = new SinFunction((float) (Math.PI * 2.0), 1.0f, 0, 0.0f, 1);

		// test precision
		System.out.println("x; sin(x); value(x); cos(x); derivative(x); -sin(x); derivative2(x) ");

		double dxError = 0;
		double d2xError = 0;
		for (double x = 0.0; x < 5; x += 0.1) {
			double y = function.value(x);
			double dx = function.derivative(x);
			double d2x = function.derivative2(x);
			double sinx = Math.sin(x);
			double cosx = Math.cos(x);
			double deltaDX = cosx - dx;
			dxError += deltaDX * deltaDX;
			double deltaD2X = -sinx - d2x;
			// d2xError += deltaD2X * deltaD2X;
			d2xError += Math.abs(deltaD2X);
			System.out.println(x + "; " + sinx + "; " + y + "; " + cosx + "; " + dx + "; " + (-sinx) + "; " + d2x);
		}

		System.out.println("Sum SquareError dx: " + dxError + " d2x: " + d2xError);

		// test runtime
		long start = System.nanoTime();
		int i = 0;
		for (double x = 0; x < 1; x += 0.000001) {
			function.value(x);
			function.derivative(x);
			function.derivative2(x);
			i++;
		}
		long end = System.nanoTime();
		System.out.println("time : " + (end - start) / 1000000 + " for runs: " + i);
	}
}
