/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel.guiElements;

import java.awt.Graphics2D;
import kdo.ebnDevKit.gui.ebnGraphPanel.GraphConfiguration;

/**
 * interface for all drawable elements
 * @author Thomas Rinklin
 */
public interface IDrawable {
	/**
	 * prepare drawing
	 * @param graphConfiguration graphical configuration
	 */
	void prepare(GraphConfiguration graphConfiguration);

	/**
	 * draw an element
	 * @param g graphics context
	 */
	void draw(Graphics2D g);
}
