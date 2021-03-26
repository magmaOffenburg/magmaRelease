/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel.guiElements;

import kdo.ebnDevKit.gui.ebnGraphPanel.GraphConfiguration;

public abstract class GuiElement implements IDrawable
{
	/** graph configuration */
	protected GraphConfiguration graphConf;

	@Override
	public void prepare(GraphConfiguration graphConfiguration)
	{
		this.graphConf = graphConfiguration;
	}
}
