/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.impl;

import magma.agent.decision.behavior.IBehaviorConstants;

/**
 * Enum for mapping specific behavior names to head command numbers.
 *
 * @author Stefan Glaser
 */
public enum HeadCommands {
	FOCUS_BALL(IBehaviorConstants.FOCUS_BALL, 1),

	LOOK_AROUND(IBehaviorConstants.TURN_HEAD, 2);

	private final String name;

	private final int id;

	private HeadCommands(String behaviorName, int id)
	{
		this.name = behaviorName;
		this.id = id;
	}

	/**
	 * Retrieve the enum value to a behavior name
	 *
	 * @param behaviorName - the name of the behavior
	 * @return Resulting enum value
	 */
	public static HeadCommands getCommandFor(String behaviorName)
	{
		for (HeadCommands command : values()) {
			if (command.name.equals(behaviorName))
				return command;
		}
		return FOCUS_BALL;
	}

	/**
	 * Retrieve the behavior name by Id
	 *
	 * @param behaviorId - the Id of the behavior
	 * @return Resulting behavior
	 */
	public static HeadCommands getCommandFor(int behaviorId)
	{
		for (HeadCommands command : values()) {
			if (command.id == behaviorId)
				return command;
		}
		return FOCUS_BALL;
	}

	/**
	 * Retrieve the name of the behavior corresponding to this enum value.
	 *
	 * @return The name of the corresponding behavior
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Retrieve the id of the command.
	 *
	 * @return the command id
	 */
	public int getId()
	{
		return id;
	}
}
