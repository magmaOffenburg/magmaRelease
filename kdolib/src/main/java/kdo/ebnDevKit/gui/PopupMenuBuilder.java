/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui;

import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import kdo.ebnDevKit.ebnAccess.IEbnAccess;
import kdo.ebnDevKit.ebnAccess.IEbnCompetence;
import kdo.ebnDevKit.ebnAccess.IEbnGoal;
import kdo.ebnDevKit.ebnAccess.IEbnPerception;
import kdo.ebnDevKit.ebnAccess.IEbnResource;

/**
 * this class builds popupmenus. as handler it uses the ones provided by the
 * ModifierActionListeners
 * @author Thomas Rinklin
 */
public class PopupMenuBuilder
{
	private final ModifierActionListeners modAct;

	private final IEbnAccess ebnAccess;

	public PopupMenuBuilder(ModifierActionListeners modifierActions, IEbnAccess ebnAccess)
	{
		this.modAct = modifierActions;
		this.ebnAccess = ebnAccess;
	}

	public JPopupMenu getMainMenu()
	{
		JPopupMenu menu = new JPopupMenu("Main Menu");

		addItem(menu, "Add Goal", modAct.new AddGoal());
		addItem(menu, "Add Competence", modAct.new AddCompetence());
		addItem(menu, "Add Perception", modAct.new AddPerception());
		addItem(menu, "Add Resource", modAct.new AddResource());

		return menu;
	}

	public JPopupMenu getCompetenceMenu(IEbnCompetence comp)
	{
		JPopupMenu menu = new JPopupMenu("Competence Menu");

		addItem(menu, "Edit Competence", modAct.new EditCompetence(comp));
		addItem(menu, "Remove Competence", modAct.new RemoveCompetence(comp));

		return menu;
	}

	public JPopupMenu getGoalMenu(IEbnGoal goal)
	{
		JPopupMenu menu = new JPopupMenu("Goal Menu");

		addItem(menu, "Edit Goal", modAct.new EditGoal(goal));
		addItem(menu, "Remove Goal", modAct.new RemoveGoal(goal));

		return menu;
	}

	public JPopupMenu getResourceMenu(IEbnResource resource)
	{
		JPopupMenu menu = new JPopupMenu("Resource Menu");

		JMenuItem item = createItem("Remove Resource", modAct.new RemoveResource(resource));
		if (ebnAccess.isResourceUsed(resource)) {
			item.setEnabled(false);
			item.setToolTipText("Perception is still used!");
		}

		menu.add(item);
		return menu;
	}

	public JPopupMenu getPerceptionMenu(IEbnPerception perception)
	{
		JPopupMenu menu = new JPopupMenu("Perception Menu");

		JMenuItem item = createItem("Remove Perception", modAct.new RemovePerception(perception));

		if (ebnAccess.isPerceptionUsed(perception)) {
			item.setEnabled(false);
			item.setToolTipText("Perception is still used!");
		}
		menu.add(item);
		return menu;
	}

	private void addItem(JPopupMenu menu, String string, ActionListener actionListener)
	{
		menu.add(createItem(string, actionListener));
	}

	private JMenuItem createItem(String text, ActionListener actionListener)
	{
		JMenuItem item = new JMenuItem(text);
		item.addActionListener(actionListener);
		return item;
	}
}
