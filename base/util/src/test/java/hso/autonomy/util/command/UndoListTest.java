/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Klaus Dorer
 */
public class UndoListTest
{
	private UndoList testee;

	private TestCommand command1;

	private ICommand command2;

	private ICommand command3;

	@BeforeEach
	public void setUp()
	{
		testee = new UndoList(2);
		command1 = new TestCommand("command1");
		command2 = new TestCommand("command2");
		command3 = new TestCommand("command3");
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.command.UndoList#addCommand(hso.autonomy.util.command.ICommand)}
	 * .
	 */
	@Test
	public void testAddCommand()
	{
		assertEquals(0, testee.size());
		assertEquals(-1, testee.getUndoIndex());

		testee.addCommand(command1);
		assertEquals(1, testee.size());
		assertEquals(0, testee.getUndoIndex());

		testee.addCommand(command2);
		assertEquals(2, testee.size());

		testee.addCommand(command3);
		assertEquals(2, testee.size());
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.command.UndoList#addCommand(hso.autonomy.util.command.ICommand)}
	 * .
	 */
	@Test
	public void testAddMergeableCommand()
	{
		command1.setMergeable(true);
		testee.addCommand(command1);
		assertEquals(1, testee.size());

		testee.addCommand(command2);
		assertEquals(1, testee.size());
		assertEquals(0, testee.getUndoIndex());
		assertEquals(command1, testee.getUndoCommand());
	}

	/**
	 * Test method for {@link hso.autonomy.util.command.UndoList#addCommand(ICommand)}
	 */
	@Test
	public void testAddCommandRemovesLaterInList()
	{
		testee.addCommand(command1);
		testee.addCommand(command2);
		assertEquals(command2, testee.getUndoCommand());
		assertEquals(command1, testee.getUndoCommand());

		testee.addCommand(command3);
		assertEquals(1, testee.size());
		assertEquals(0, testee.getUndoIndex());
	}

	/**
	 * Test method for {@link hso.autonomy.util.command.UndoList#getUndoCommand()}.
	 */
	@Test
	public void testGetUndoCommand()
	{
		assertNull(testee.getUndoCommand());
		assertNull(testee.getUndoCommand());

		testee.addCommand(command1);
		assertEquals(command1, testee.getUndoCommand());

		testee.addCommand(command1);
		testee.addCommand(command2);
		assertEquals(command2, testee.getUndoCommand());
		assertEquals(command1, testee.getUndoCommand());

		testee.addCommand(command1);
		testee.addCommand(command2);
		testee.addCommand(command3);
		assertEquals(command3, testee.getUndoCommand());
		assertEquals(command2, testee.getUndoCommand());
		assertNull(testee.getUndoCommand());
	}

	/**
	 * Test method for {@link hso.autonomy.util.command.UndoList#getRedoCommand()}.
	 */
	@Test
	public void testGetRedoCommand()
	{
		assertNull(testee.getRedoCommand());
		assertNull(testee.getRedoCommand());

		testee.addCommand(command1);
		testee.addCommand(command2);
		assertNull(testee.getRedoCommand());
		assertEquals(command2, testee.getUndoCommand());
		assertEquals(command2, testee.getRedoCommand());

		assertEquals(command2, testee.getUndoCommand());
		assertEquals(command1, testee.getUndoCommand());

		ICommand redo = testee.getRedoCommand();
		assertEquals(command1, redo);
		assertEquals("command1", redo.getName());
		assertTrue(redo.perform());

		redo = testee.getRedoCommand();
		assertEquals(command2, redo);
		assertEquals("command2", redo.getName());
		assertTrue(redo.perform());

		assertNull(testee.getRedoCommand());
	}
}
