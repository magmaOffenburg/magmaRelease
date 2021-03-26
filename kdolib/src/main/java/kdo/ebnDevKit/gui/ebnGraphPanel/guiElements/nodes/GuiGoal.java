/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JPopupMenu;
import kdo.ebnDevKit.ebnAccess.IEbnGoal;
import kdo.ebnDevKit.gui.PopupMenuBuilder;

/**
 * goal gui element
 * @author Thomas Rinklin
 *
 */
public class GuiGoal extends GuiNode implements Comparable<GuiGoal>
{
	private final IEbnGoal goal;

	private final String name;

	private double activation;

	// private double completion;

	private JPopupMenu menu;

	private double staticImportance;

	public GuiGoal(IEbnGoal goal2, PopupMenuBuilder popupMenuBuilder)
	{
		super(popupMenuBuilder);
		this.goal = goal2;

		if (popupMenuBuilder != null)
			menu = popupMenuBuilder.getGoalMenu(goal);

		this.name = goal.getName();
		refreshValues();
	}

	@Override
	public void refreshValues()
	{
		this.activation = goal.getActivation();
		// this.completion = goal.getGoalConditionTruthValue();
		this.staticImportance = goal.getImportance();
	}

	@Override
	public void draw(Graphics2D g)
	{
		int outerSize = graphConf.getGoalSize();
		int innerSize = graphConf.getGoalInnerSize();
		Point innerOffset = addToXandY(offset, (outerSize - innerSize) / 2);

		Color bgColor = new Color((int) (255 * activation), 0, 0);
		drawBorderCircle(g, outerSize, bgColor, offset); // outer circle
		bgColor = new Color((int) (255 * staticImportance), 0, 0);
		drawBorderCircle(g, innerSize, bgColor, innerOffset); // inner circle
		if (isSelected())									  // mark, if selected
			g.fillOval(offset.x, offset.y, 4, 4);

		updateDrawedArea(new Point(outerSize, outerSize));
		updateClickarea(outerSize);

		String text = name;
		int textOffset = (graphConf.getGoalSize() / 2);
		drawStringDefault(g, textOffset, text);
	}

	/**
	 * returns point where connections to competence elements can connect to
	 * @return point where connections to competence elements can connect to
	 */
	public Point getConnectionPoint()
	{
		return new Point(offset.x + 30, offset.y + 60);
	}

	public IEbnGoal getGoal()
	{
		return goal;
	}

	@Override
	public void showMenu(Component invoker, Point point)
	{
		if (menu != null)
			menu.show(invoker, point.x, point.y);
	}

	@Override
	public int compareTo(GuiGoal o)
	{
		return goal.getName().compareTo(o.goal.getName());
	}
}