/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.commandline;

/**
 * Generic base class for command line arguments.
 */
public abstract class Argument<T>
{
	protected final String name;

	/** value to use if parsing fails */
	protected final T defaultValue;

	protected final String description;

	private boolean required = false;

	/** true if this parameter was found in the call to parse */
	private boolean specified = false;

	/**
	 * Should be called when parsing is finished. Checks for any unrecognized
	 * arguments and prints a warning if one is found.
	 */
	public static void endParse(String... args)
	{
		for (String arg : args) {
			if (!arg.equals("")) {
				error("Unknown argument '%s'.", arg);
			}
		}
	}

	public Argument(String name, T defaultValue, String description)
	{
		this.name = name;
		this.defaultValue = defaultValue;
		this.description = description;
		specified = false;
	}

	/** Error if the argument is missing rather than using its default value */
	public void setRequired(boolean required)
	{
		this.required = required;
	}

	public boolean isSpecified()
	{
		return specified;
	}

	/**
	 * Attempts to find and parse this argument from the args array. Parsed
	 * arguments are replaced with an empty string.
	 */
	public T parse(String... args)
	{
		if (args == null) {
			return defaultValue;
		}

		T result = defaultValue;
		specified = false;

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (matchesName(arg)) {
				args[i] = "";
				String stringValue = extractStringValue(arg);
				if (specified) {
					System.out.printf(
							"Duplicate '--%s' argument with value '%s' found, using the first value '%s' instead.\n",
							name, stringValue, result);
					continue;
				}
				specified = true;

				if (stringValue != null) {
					result = extractValue(stringValue);
				}
			}
		}

		if (required && !specified) {
			error("Missing non-optional argument: \n%s", getHelp());
		}

		return result;
	}

	/**
	 * The name as it's used on the command line: <br>
	 * <code>--argumentname=</code>
	 */
	protected String getFormattedName()
	{
		return "--" + name + "=";
	}

	public String getHelp()
	{
		String help = getFormattedName();
		String valueHelp = getValueHelp();
		String defaultHelp = getDefaultHelp();

		if (valueHelp != null) {
			help += "<" + valueHelp + ">";
		}

		if (defaultHelp != null || description != null) {
			help += " : ";
		}

		if (description != null) {
			help += description;
			if (defaultHelp != null) {
				help += " ";
			}
		}

		if (defaultHelp != null) {
			help += defaultHelp;
		}

		return help;
	}

	protected abstract String getValueHelp();

	protected String getDefaultHelp()
	{
		if (required) {
			return null;
		}
		return "(default: " + defaultValue + ")";
	}

	protected boolean matchesName(String arg)
	{
		return arg.startsWith(getFormattedName());
	}

	/** To be overridden in subclasses. */
	protected abstract T extractValue(String value);

	protected String extractStringValue(String arg)
	{
		return arg.replaceFirst(getFormattedName(), "");
	}

	protected String getInvalidArgString(Object value)
	{
		return String.format("Invalid '--%s' value: '%s'.", name, value);
	}

	protected static void error(String format, Object... args)
	{
		throw new ArgumentParsingException(String.format(format, args));
	}
}
