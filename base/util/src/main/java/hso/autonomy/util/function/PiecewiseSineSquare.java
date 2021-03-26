/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import java.util.List;

/**
 *
 * @author kdorer
 */
public class PiecewiseSineSquare extends SupportPointFunction
{
	/** name identifying a this kind of function */
	public static final String NAME = "sinsquare";

	public PiecewiseSineSquare()
	{
		super(1);
	}

	public PiecewiseSineSquare(List<SupportPoint> supportPoints, float stiffness)
	{
		super(supportPoints, stiffness);
	}

	public PiecewiseSineSquare(PiecewiseSineSquare source)
	{
		super(source);
	}

	@Override
	protected int getMinimumSupportPointSize()
	{
		return 0;
	}

	@Override
	protected int getMaximumSupportPointSize()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public double value(double x)
	{
		// Special condition for logging use
		if (supportPoints.size() == 0) {
			return 0;
		}

		// Catch border conditions
		if (x <= getMinX()) {
			return supportPoints.get(0).y;
		} else if (x >= getMaxX()) {
			return supportPoints.get(supportPoints.size() - 1).y;
		}

		int index = 1;

		// Find index after the SupportPoint that is closest, but smaller or equal
		// to t
		while (index < (supportPoints.size() - 1) && x >= supportPoints.get(index).x) {
			index++;
		}

		SupportPoint sPointBefore = supportPoints.get(index - 1);
		SupportPoint sPointAfter = supportPoints.get(index);

		// sine square interpolation
		double deltay = sPointAfter.y - sPointBefore.y;
		double deltax = sPointAfter.x - sPointBefore.x;
		double sinsquare = Math.sin((x - sPointBefore.x) / deltax * (Math.PI / 2));

		return sPointBefore.y + deltay * sinsquare * sinsquare;
	}

	@Override
	public IFunction copy()
	{
		return new PiecewiseSineSquare(this);
	}
}
