/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel.guiElements;

import java.awt.Component;
import java.awt.Point;

/**
 * interface for elements which can show an popup menu
 * @author Thomas Rinklin
 *
 */
public interface IPopUpMenu {
	// TODO is this really needed
	void showMenu(Component invoker, Point point);
}