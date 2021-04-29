/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.commandline;

public class DoubleArgument extends Argument<Double>
{
	private final double minValue;

	private final double maxValue;

	public DoubleArgument(String name, Double defaultValue, String description)
	{
		this(name, defaultValue, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, description);
	}

	public DoubleArgument(String name, Double defaultValue, double minValue, String description)
	{
		this(name, defaultValue, minValue, Double.POSITIVE_INFINITY, description);
	}

	public DoubleArgument(String name, Double defaultValue, double minValue, double maxValue, String description)
	{
		super(name, defaultValue, description);
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	@Override
	protected String getValueHelp()
	{
		boolean lowerBound = minValue != Double.NEGATIVE_INFINITY;
		boolean upperBound = maxValue != Double.POSITIVE_INFINITY;
		if (lowerBound && upperBound) {
			return minValue + "-" + maxValue;
		} else if (upperBound) {
			return "-" + maxValue;
		} else if (lowerBound) {
			return minValue + "+";
		}
		return "int";
	}

	@Override
	protected Double extractValue(String value)
	{
		try {
			return verifyValue(Double.valueOf(value));
		} catch (NumberFormatException e) {
			error("%s Is not a valid integer.", getInvalidArgString(value));
			return null;
		}
	}

	private Double verifyValue(Double value)
	{
		if (value < minValue || value > maxValue) {
			error("%s Range is %f to %f.", getInvalidArgString(value), minValue, maxValue);
		}
		return value;
	}
}