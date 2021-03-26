/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package kdo.ebnDevKit.gui.ebnGraphPanel;

import java.awt.Component;
import java.awt.Point;
import javax.swing.JPopupMenu;
import kdo.ebnDevKit.gui.PopupMenuBuilder;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.IPopUpMenu;

/**
 * popupmenu which uses the PopupMenuBuilder and the ModifierActionListeners
 * @author Thomas Rinklin
 */
public class MainPopupMenu implements IPopUpMenu
{
	private JPopupMenu menu;

	public MainPopupMenu(PopupMenuBuilder popupMenuBuilder)
	{
		if (popupMenuBuilder != null)
			menu = popupMenuBuilder.getMainMenu();
	}

	@Override
	public void showMenu(Component invoker, Point point)
	{
		if (menu != null)
			menu.show(invoker, point.x, point.y);
	}
}