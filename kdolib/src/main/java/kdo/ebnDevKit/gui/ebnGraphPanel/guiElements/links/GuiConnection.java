/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.links;

import kdo.ebnDevKit.gui.ebnGraphPanel.GraphConfiguration;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.GuiElement;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiNode;

/**
 * base class for all connections between ebn gui elements
 * @author Thomas Rinklin
 *
 */
public abstract class GuiConnection<S extends GuiNode, T extends GuiNode> extends GuiElement
{
	protected final S source;

	protected final T target;

	public GuiConnection(S source, T target)
	{
		this.source = source;
		this.target = target;
	}

	protected boolean shouldPrint(GraphConfiguration.ConnectionVisibility conVis)
	{
		switch (conVis) {
		case ALWAYS:
			return true;

		case TARGET_OR_SOURCE_SELECTED:
			return targetSelected() || sourceSelected();

		case TARGET_SELECTED:
			return targetSelected();

		case SOURCE_SELECTED:
			return sourceSelected();

		case NEVER:
			return false;
		}
		return false;
	}

	private boolean targetSelected()
	{
		return target.isSelected();
	}

	private boolean sourceSelected()
	{
		return source.isSelected();
	}
}
