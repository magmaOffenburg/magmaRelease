/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel.guiElements;

import java.awt.Point;

/**
 * interface for selectable elements
 * @author Thomas Rinklin
 */
public interface ISelectable {
	boolean isSelected();

	/**
	 * select or unselect an element
	 * @param selected true, the element should be selected, false if not
	 */
	void setSelected(boolean selected);

	/**
	 * checks, if this point is inside of the click area
	 * @param p point to check
	 * @return true if the point is inside, false if not
	 */
	boolean contains(Point p);
}
