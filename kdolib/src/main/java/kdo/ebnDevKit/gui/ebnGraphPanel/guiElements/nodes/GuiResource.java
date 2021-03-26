/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JPopupMenu;
import kdo.ebnDevKit.ebnAccess.IEbnResource;
import kdo.ebnDevKit.gui.PopupMenuBuilder;

/**
 * resource gui element
 * @author Thomas Rinklin
 */
public class GuiResource extends GuiNode implements Comparable<GuiResource>
{
	private final IEbnResource resource;

	private String name;

	private JPopupMenu menu;

	private int amountAvailable;

	private int maxAmount;

	public GuiResource(IEbnResource res, PopupMenuBuilder popupMenuBuilder)
	{
		super(popupMenuBuilder);
		this.resource = res;

		if (popupMenuBuilder != null)
			menu = popupMenuBuilder.getResourceMenu(resource);
	}

	@Override
	public void refreshValues()
	{
		name = resource.getName();
		amountAvailable = resource.getAmountAvailable();
		maxAmount = resource.getMaximalAmount();
	}

	@Override
	public void draw(Graphics2D g)
	{
		int resSize = graphConf.getResourceSize();

		if (isSelected())
			g.fillOval(offset.x - 2, offset.y - 2, 4, 4);

		Color bgColor = new Color((int) ((255.0 * amountAvailable) / maxAmount), 0, 0);
		drawBorderCircle(g, resSize, bgColor, offset); // inner circle

		updateDrawedArea(new Point(resSize, resSize));
		updateClickarea(resSize);

		// create font
		int textOffset = (graphConf.getResourceSize() / 2);
		String text = name + " " + amountAvailable;
		drawStringDefault(g, textOffset, text);
	}

	@Override
	public void showMenu(Component invoker, Point point)
	{
		if (menu != null)
			menu.show(invoker, point.x, point.y);
	}

	@Override
	public int compareTo(GuiResource o)
	{
		return resource.getName().compareTo(o.resource.getName());
	}

	public Point getTopConnectionPoint()
	{
		return new Point(offset.x + (graphConf.getResourceSize() / 2), offset.y);
	}
}
