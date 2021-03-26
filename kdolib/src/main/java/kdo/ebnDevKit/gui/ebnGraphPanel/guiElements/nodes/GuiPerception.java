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
import kdo.ebnDevKit.ebnAccess.IEbnPerception;
import kdo.ebnDevKit.gui.PopupMenuBuilder;

/**
 * perception gui element
 * @author Thomas Rinklin
 *
 */
public class GuiPerception extends GuiNode implements Comparable<GuiPerception>
{
	private final IEbnPerception perception;

	private String name;

	private double truthValue;

	private JPopupMenu menu;

	public GuiPerception(IEbnPerception perception2, PopupMenuBuilder popupMenuBuilder)
	{
		super(popupMenuBuilder);
		this.perception = perception2;

		if (popupMenuBuilder != null)
			menu = popupMenuBuilder.getPerceptionMenu(perception);

		refreshValues();
	}

	@Override
	public void refreshValues()
	{
		this.name = perception.getName();
		this.truthValue = perception.getTruthValue();
	}

	@Override
	public void draw(Graphics2D g)
	{
		int percSize = graphConf.getPercSize();

		Color bgColor = new Color((int) (255 * truthValue), 0, 0);
		drawBorderCircle(g, percSize, bgColor, offset); // inner circle
		if (isSelected())
			g.fillOval(offset.x, offset.y, 4, 4);

		updateDrawedArea(new Point(percSize, percSize));
		updateClickarea(percSize);

		// create font
		int textOffset = (graphConf.getPercSize() / 2);
		String text = name;
		drawStringDefault(g, textOffset, text);
	}

	@Override
	public void showMenu(Component invoker, Point point)
	{
		if (menu != null)
			menu.show(invoker, point.x, point.y);
	}

	public IEbnPerception getPerception()
	{
		return perception;
	}

	@Override
	public int compareTo(GuiPerception o)
	{
		return perception.getName().compareTo(o.perception.getName());
	}

	public Point getTopConnectionPoint()
	{
		return new Point(offset.x + (graphConf.getPercSize() / 2), offset.y);
	}
}
