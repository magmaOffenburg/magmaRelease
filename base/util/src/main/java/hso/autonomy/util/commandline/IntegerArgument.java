/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.commandline;

public class IntegerArgument extends Argument<Integer>
{
	private final int minValue;

	private final int maxValue;

	public IntegerArgument(String name, Integer defaultValue, String description)
	{
		this(name, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, description);
	}

	public IntegerArgument(String name, Integer defaultValue, int minValue, String description)
	{
		this(name, defaultValue, minValue, Integer.MAX_VALUE, description);
	}

	public IntegerArgument(String name, Integer defaultValue, int minValue, int maxValue, String description)
	{
		super(name, defaultValue, description);
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	@Override
	protected String getValueHelp()
	{
		boolean lowerBound = minValue != Integer.MIN_VALUE;
		boolean upperBound = maxValue != Integer.MAX_VALUE;
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
	protected Integer extractValue(String value)
	{
		try {
			return verifyValue(Integer.valueOf(value));
		} catch (NumberFormatException e) {
			error("%s Is not a valid integer.", getInvalidArgString(value));
			return null;
		}
	}

	private Integer verifyValue(Integer value)
	{
		if (value < minValue || value > maxValue) {
			error("%s Range is %d to %d.", getInvalidArgString(value), minValue, maxValue);
		}
		return value;
	}
}