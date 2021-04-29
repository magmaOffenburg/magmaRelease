/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.commandline;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("rawtypes")
public class HelpArgument extends BooleanArgument
{
	private final ArrayList<Argument> arguments;

	public HelpArgument(Argument... arguments)
	{
		super("help", "display this message and exit");
		this.arguments = new ArrayList<>(Arrays.asList(arguments));
	}

	@Override
	public Boolean parse(String... args)
	{
		if (super.parse(args)) {
			printHelp();
			System.exit(0);
		}
		return false;
	}

	public void printHelp()
	{
		for (Argument argument : arguments) {
			System.out.println(argument.getHelp());
		}
	}
}
