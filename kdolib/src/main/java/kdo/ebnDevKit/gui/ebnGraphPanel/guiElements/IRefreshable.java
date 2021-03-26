/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel.guiElements;

/**
 * interface for all gui elements, which draw runtime-information from the model
 * @author Thomas Rinklin
 */
public interface IRefreshable {
	/**
	 * refresh values from the model
	 */
	void refreshValues();
}
