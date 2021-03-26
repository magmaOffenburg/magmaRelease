/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import java.util.ArrayList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents a sin function with amplitude, period, phaseShift and offset by
 * using three SupportPoints:<br>
 * 1: (phaseShift, offset)<br>
 * 2: (phaseShift + phase/4, offset+amplitude)<br>
 * 3: (phaseShift + phase/2, offset)<br>
 *
 * @author Stefan Glaser
 */
public class SinFunction extends SupportPointFunction
{
	/** name identifying a sine function */
	public static final String NAME = "sine";

	/** The phaseShift of the sin-function */
	float phaseShift;

	/** The yOffset of the sin-function */
	float yOffset;

	/** The period of the sin-function */
	float period;

	/** The amplitude of the sin-function */
	float amplitude;

	/**
	 * Default Constructor. Creates a new SinFunction with a period of 20 and an
	 * amplitude of 1.
	 */
	public SinFunction()
	{
		super(1);
	}

	/**
	 * Construct a new SinFunction from 3 special SupportPoints.
	 *
	 * @param points - a list of 3 special SupportPoints representing a sine
	 *        function
	 */
	public SinFunction(ArrayList<SupportPoint> points, float stiffness)
	{
		super(points, stiffness);
	}

	/**
	 * Construct a new SinFunction with the given properties.
	 *
	 * @param period - the period
	 * @param amplitude - the amplitude
	 * @param phaseShift - the phase shift
	 * @param offset - the offset in Y-direction
	 */
	public SinFunction(float period, float amplitude, float phaseShift, float offset, float stiffness)
	{
		super(stiffness);

		setSineSupportPoints(period, amplitude, phaseShift, offset);
	}

	/**
	 * Copy constructor.
	 *
	 * @param source The source object to copy
	 */
	public SinFunction(SinFunction source)
	{
		this(source.supportPoints, source.stiffness);
	}

	@Override
	protected void setDefaultSupportPoints()
	{
		setSineSupportPoints(20, 1, 0, 0);
	}

	@Override
	protected int getMaximumSupportPointSize()
	{
		return 3;
	}

	@Override
	protected int getMinimumSupportPointSize()
	{
		return 3;
	}

	/**
	 * Set the properties of this sine function to the given values and create
	 * the corresponding SupportPoints.
	 */
	private void setSineSupportPoints(float period, float amplitude, float phaseShift, float yOffset)
	{
		if (supportPoints.size() != 3) {
			if (supportPoints.size() > 3) {
				for (int i = supportPoints.size() - 1; i > 2; i--) {
					supportPoints.remove(i);
				}
			} else {
				for (int i = supportPoints.size(); i < 3; i++) {
					supportPoints.add(new SupportPoint(0, 0));
				}
			}
		}

		SupportPoint s0 = supportPoints.get(0);
		SupportPoint s1 = supportPoints.get(1);
		SupportPoint s2 = supportPoints.get(2);

		s0.x = phaseShift;
		s0.y = yOffset;
		s1.x = phaseShift + period / 4;
		s1.y = yOffset + amplitude;
		s2.x = phaseShift + period / 2;
		s2.y = yOffset;

		initialize();
	}

	/**
	 * Get Function name
	 *
	 * @return String
	 */
	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public double value(double t)
	{
		return amplitude * Math.sin((t - phaseShift) / period * 2 * Math.PI) + yOffset;
	}

	@Override
	public Vector2D moveSupportPointTo(int index, float x, float y)
	{
		if (index < 0 || index > 2) {
			return null;
		}

		Vector2D previousPos = null;

		// Truncate x value of SupportPoint
		x = truncateX(x);

		if (index == 0) {
			// shift whole function
			previousPos = supportPoints.get(0).asVector2D();
			setSineSupportPoints(period, amplitude, x, y);
		} else if (index == 1) {
			// shift amplitude
			previousPos = supportPoints.get(1).asVector2D();
			setSineSupportPoints(period, y - yOffset, phaseShift, yOffset);
		} else {
			// shift period
			previousPos = supportPoints.get(2).asVector2D();
			float newPeriod = (x - phaseShift) * 2;
			if (newPeriod < 0) {
				newPeriod *= -1;
			}
			if (newPeriod < 0.2) {
				newPeriod = 0.2f;
			}
			setSineSupportPoints(newPeriod, amplitude, phaseShift, yOffset);
		}

		initialize();

		return previousPos;
	}

	@Override
	public void initialize()
	{
		SupportPoint sp0 = supportPoints.get(0);
		SupportPoint sp1 = supportPoints.get(1);
		SupportPoint sp2 = supportPoints.get(2);

		phaseShift = sp0.x;
		yOffset = sp0.y;
		period = (sp2.x - phaseShift) * 2;
		amplitude = sp1.y - yOffset;

		sp1.x = sp0.x + period / 4;
		sp2.y = sp0.y;

		if (period < 0) {
			minXValue = phaseShift + period / 2;
			maxXValue = phaseShift;
		} else {
			minXValue = phaseShift;
			maxXValue = phaseShift + period / 2;
		}
		minYValue = yOffset - Math.abs(amplitude);
		maxYValue = yOffset + Math.abs(amplitude);
	}

	@Override
	public IFunction copy()
	{
		return new SinFunction(this);
	}

	public float[] getSinParameters()
	{
		return new float[] {period, amplitude, phaseShift, yOffset};
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