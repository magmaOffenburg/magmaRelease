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
import kdo.ebnDevKit.ebnAccess.IEbnCompetence;
import kdo.ebnDevKit.gui.PopupMenuBuilder;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.util.ValueScaler;

/**
 * competence gui element.
 * @author Thomas Rinklin
 *
 */
public class GuiCompetence extends GuiNode implements Comparable<GuiCompetence>
{
	private final IEbnCompetence comp;

	private String name;

	private double activation;

	private double executability;

	private JPopupMenu menu = null;

	private final ValueScaler actScaler;

	// private double scaledAct;

	private double scaledExecAndActiv;

	public GuiCompetence(IEbnCompetence competence, ValueScaler actScaler, PopupMenuBuilder popupMenuBuilder)
	{
		super(popupMenuBuilder);

		this.comp = competence;
		this.actScaler = actScaler;

		if (popupMenuBuilder != null)
			menu = popupMenuBuilder.getCompetenceMenu(comp);

		refreshValues();
	}

	@Override
	public void refreshValues()
	{
		name = comp.getName();
		activation = comp.getActivation();
		executability = comp.getExecutability();
		double execAndActiv = comp.getActivationAndExecutability();

		actScaler.scaleValue(execAndActiv);
		scaledExecAndActiv = actScaler.scaleValue(execAndActiv, false);
	}

	@Override
	public void draw(Graphics2D g)
	{
		int outerSize = graphConf.getCompSize();
		int innerSize = graphConf.getCompInnerSize();
		Point innerOffset = addToXandY(offset, (outerSize - innerSize) / 2);

		// outer circle
		Color bgColor = new Color(0, (int) (255 * executability), 0);
		drawBorderCircle(g, outerSize, bgColor, offset); // outer circle

		// inner circle
		bgColor = new Color((int) (255 * scaledExecAndActiv), 0, 0);
		drawBorderCircle(g, innerSize, bgColor, innerOffset); // inner circle
		if (isSelected())
			g.fillOval(offset.x, offset.y, 4, 4);

		updateDrawedArea(new Point(outerSize, outerSize));
		updateClickarea(outerSize);

		// draw text background area
		String text = name + " " + graphConf.getNumberFormat().format(activation);
		int textOffset = (graphConf.getCompSize() / 2);
		drawStringDefault(g, textOffset, text);
	}

	/**
	 * returns the point where connections to goal elements can plug in
	 * @return point where connections to goal elements can plug in
	 */
	public Point getTopConnectionPoint()
	{
		return new Point(offset.x + (graphConf.getCompSize() / 2), offset.y);
	}

	public Point getBottomConnectionPoint()
	{
		int compSize = graphConf.getCompSize();
		return new Point(offset.x + (compSize / 2), offset.y + compSize);
	}

	@Override
	public void showMenu(Component invoker, Point point)
	{
		if (menu != null)
			menu.show(invoker, point.x, point.y);
	}

	@Override
	public int compareTo(GuiCompetence o)
	{
		return comp.getName().compareTo(o.comp.getName());
	}

	public IEbnCompetence getCompetence()
	{
		return comp;
	}
}
