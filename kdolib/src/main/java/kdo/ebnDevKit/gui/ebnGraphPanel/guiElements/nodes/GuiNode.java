/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import kdo.ebnDevKit.gui.PopupMenuBuilder;
import kdo.ebnDevKit.gui.ebnGraphPanel.DrawingArea;
import kdo.ebnDevKit.gui.ebnGraphPanel.GraphConfiguration;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.GuiElement;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.IPopUpMenu;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.IRefreshable;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.ISelectable;

/**
 * base class for all ebn gui elements
 * @author Thomas Rinklin
 */
public abstract class GuiNode extends GuiElement implements ISelectable, IPopUpMenu, IRefreshable
{
	/** builds the popup menu */
	protected final PopupMenuBuilder popupMenuBuilder;

	/** offset of this gui element */
	protected Point offset;

	/** clickarea */
	protected final Ellipse2D.Float clickarea;

	/** selected flag */
	private boolean selected;

	/** position of drawing */
	private int drawIndex;

	private Point drawnArea = null;

	public GuiNode(PopupMenuBuilder popupMenuBuilder)
	{
		this.popupMenuBuilder = popupMenuBuilder;
		offset = new Point();
		clickarea = new Ellipse2D.Float();
		drawIndex = -1;
	}

	/**
	 * sets or changes the offset
	 * @param p the new offset
	 */
	public void setOffsetPoint(Point p)
	{
		offset = p;
	}

	/**
	 * checks, if this point is inside of the clickarea
	 * @param p point to check
	 * @return true if the point is inside, false if not
	 */
	@Override
	public boolean contains(Point p)
	{
		return clickarea.contains(p);
	}

	@Override
	public boolean isSelected()
	{
		return selected;
	}

	@Override
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public void setDrawIndex(int drawIndex)
	{
		this.drawIndex = drawIndex;
	}

	public int getDrawIndex()
	{
		return this.drawIndex;
	}

	@Override
	public void prepare(GraphConfiguration graphConfiguration)
	{
		super.prepare(graphConfiguration);
		this.drawnArea = null;
	}

	protected Rectangle2D calcStringBounds(String text)
	{
		Font myFont = graphConf.getFont();
		return myFont.getStringBounds(
				text, new FontRenderContext(new AffineTransform(), true, true)); // get text bounds;
	}

	protected void drawString(Graphics2D g, String text, Rectangle2D rect, int xPosition, int yPosition)
	{
		int border = graphConf.getTextBorder();

		// draw text background area
		Composite originalComposite = g.getComposite(); // 0% transparency
		AlphaComposite ac1 = graphConf.getAlphaComposite();

		g.setColor(Color.WHITE);
		g.setComposite(ac1);

		int rectX = (int) rect.getMinX() + xPosition - border;
		int rectY = (int) rect.getMinY() + yPosition - border;
		int rectWidth = (int) rect.getWidth() + (2 * border);
		int rectHeight = (int) rect.getHeight() + (2 * border);
		g.fillRect(rectX, rectY, rectWidth, rectHeight);
		g.setComposite(originalComposite);

		// draw text
		g.setFont(graphConf.getFont());
		g.setColor(Color.BLACK);
		g.drawString(text, xPosition, yPosition);

		drawnArea = DrawingArea.enhancePoint(
				drawnArea, new Point(rectX + rectWidth - offset.x, rectY + rectHeight - offset.y));
		updateDrawedArea(new Point(rectX + rectWidth - offset.x, rectY + rectHeight - offset.y));
	}

	protected void updateDrawedArea(Point point)
	{
		drawnArea = DrawingArea.enhancePoint(drawnArea, point);
	}

	/**
	 * returns area which is used by the node. this is used to calculate the
	 * distance to other nodes
	 * @return area which is used by the node
	 */
	public Point getNodeUsedArea()
	{
		return new Point((int) clickarea.width, (int) clickarea.height);
	}

	/**
	 * returns area which is completely used by drawing the node + text +...
	 * @return area which is completely used by drawing the node + text +...
	 */
	public Point getDrawnArea()
	{
		if (drawnArea == null)
			return getNodeUsedArea();
		return drawnArea;
	}

	protected void updateClickarea(int outerSize)
	{
		clickarea.height = outerSize;
		clickarea.width = outerSize;
		clickarea.x = offset.x;
		clickarea.y = offset.y;
	}

	protected Point addToXandY(Point offset, int i)
	{
		return new Point(offset.x + i, offset.y + i);
	}

	protected void drawBorderCircle(Graphics2D g, int outerSize, Color bgColor, Point offset)
	{
		g.setColor(bgColor);
		g.fillOval(offset.x, offset.y, outerSize, outerSize); // area
		g.setColor(Color.BLACK);
		g.drawOval(offset.x, offset.y, outerSize, outerSize); // border
	}

	protected void drawStringDefault(Graphics2D g, int textOffset, String text)
	{
		Rectangle2D rect = calcStringBounds(text);
		int xPosition = offset.x + textOffset;
		int yPosition = offset.y + textOffset + (int) rect.getHeight();
		drawString(g, text, rect, xPosition, yPosition);
	}
}
