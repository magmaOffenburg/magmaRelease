/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.command;

/**
 * Represents a command that can be done and undone
 *
 * @author Klaus Dorer
 */
public interface ICommand {
	/**
	 * @return the name of this command
	 */
	String getName();

	/**
	 * Performs this command
	 * @return true if the command was performed successfully
	 */
	boolean perform();

	/**
	 * Called to undo the changes of the command that have been done through
	 * perform
	 */
	void undo();

	/**
	 * Checks if this command is mergeable (in the undo list) with the passed
	 * command
	 * @param newCommand the command to check mergability
	 * @return true if this command can be merged with the passed
	 */
	boolean isMergeable(ICommand newCommand);

	/**
	 * Merges this command with the passed command so that this command
	 * represents both
	 * @param newCommand the command to merge into this
	 */
	void merge(ICommand newCommand);
}
