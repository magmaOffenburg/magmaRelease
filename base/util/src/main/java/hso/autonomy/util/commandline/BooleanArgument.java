/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.commandline;

/**
 * Flag argument (--flag). Returns whether or not the flag is present, has no
 * value.
 */
public class BooleanArgument extends Argument<Boolean>
{
	public BooleanArgument(String name, String description)
	{
		super(name, false, description);
	}

	@Override
	protected boolean matchesName(String arg)
	{
		return arg.equals(getFormattedName());
	}

	@Override
	protected String extractStringValue(String arg)
	{
		return "true";
	}

	@Override
	protected Boolean extractValue(String value)
	{
		return true;
	}

	@Override
	protected String getFormattedName()
	{
		return "--" + name;
	}

	@Override
	protected String getValueHelp()
	{
		return null;
	}

	@Override
	protected String getDefaultHelp()
	{
		return null;
	}
}
