/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.command;

import java.util.ArrayList;
import java.util.List;

/**
 * List containing commands for undo functionality
 *
 * @author Klaus Dorer
 */
public class UndoList
{
	/** the list holding the undo commands */
	private final List<ICommand> commands;

	/** index of the current active command */
	private int undoIndex;

	/** the maximal number of entries that should be stored */
	private final int maxCommands;

	/**
	 * Instantiates an UndoList object and initializes the maximum number of
	 * entries
	 *
	 * @param max Maximum number of entries
	 */
	public UndoList(int max)
	{
		commands = new ArrayList<>();
		maxCommands = max;
		undoIndex = -1;
	}

	/**
	 * Adds the passed command to the list of commands. If the maximal amount is
	 * exceeded the oldest command is removed.
	 * @param newCommand the command to add
	 * @return True if successful, False if not
	 */
	public boolean addCommand(ICommand newCommand)
	{
		// make sure that all commands after the undo index are removed
		for (int i = commands.size() - 1; i > undoIndex; i--) {
			commands.remove(i);
		}

		// check if the new command can be merged with the last
		if (commands.size() > 0 && commands.get(commands.size() - 1).isMergeable(newCommand)) {
			commands.get(commands.size() - 1).merge(newCommand);
			return true;
		}

		boolean result = commands.add(newCommand);
		undoIndex++;

		// make sure that list does not grow too big
		if (commands.size() > maxCommands) {
			commands.remove(0);
			undoIndex--;
		}

		return result;
	}

	/**
	 * Returns the command at the undo position and moves the pointer for the
	 * next undo command to the previous command
	 * @return the command at the undo position, null if no more undo available
	 */
	public ICommand getUndoCommand()
	{
		if (undoIndex >= 0) {
			return commands.get(undoIndex--);
		}

		return null;
	}

	/**
	 * Moves the pointer for the next undo command to the next in the list and
	 * returns it
	 * @return the command at the redo position, null if no more undo available
	 */
	public ICommand getRedoCommand()
	{
		if (undoIndex < commands.size() - 1) {
			return commands.get(++undoIndex);
		}

		return null;
	}

	/**
	 * @return the index of the next command that will be returned if undo is
	 *         called
	 */
	int getUndoIndex()
	{
		return undoIndex;
	}

	/**
	 * @return the number of commands stored in the list
	 */
	public int size()
	{
		return commands.size();
	}
}
