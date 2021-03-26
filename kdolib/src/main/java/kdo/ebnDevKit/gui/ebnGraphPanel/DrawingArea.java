/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import javax.swing.JPanel;
import kdo.ebnDevKit.gui.PopupMenuBuilder;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.GuiElement;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.GuiElements;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.IPopUpMenu;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.ISelectable;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.links.GuiConnection;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiCompetence;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiGoal;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiNode;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiPerception;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiResource;

/**
 * panel for drawing the ebn.
 * @author Thomas Rinklin
 */
public class DrawingArea extends JPanel
{
	private static final long serialVersionUID = 6418463739843740206L;

	/** reference to the model */
	private final GuiElements guiElements;

	/** the current selected element */
	private ISelectable selectedElement = null;

	/** configuration for the graph panel */
	private final GraphConfiguration graphConfiguration;

	/** Popup menu */
	private IPopUpMenu menu;

	/** size of area used by the drawed elements */
	private Point usedArea;

	/**
	 * constructor
	 * @param guiElements reference to the gui elements
	 * @param graphConfiguration graphical configuration
	 */
	public DrawingArea(
			GuiElements guiElements, GraphConfiguration graphConfiguration, PopupMenuBuilder popupMenuBuilder)
	{
		this.guiElements = guiElements;
		this.graphConfiguration = graphConfiguration;

		this.setPreferredSize(new Dimension(500, 500));
		this.revalidate();

		this.addMouseListener(new DrawingAreaMouseListener());
		setPopupMenu(popupMenuBuilder);
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHints(graphConfiguration.getRenderingHints());

		// paint background white
		g2.setColor(Color.WHITE);
		g2.fill(new Rectangle(this.getSize()));

		g2.setColor(Color.BLACK);

		usedArea = new Point(graphConfiguration.getPadding(), graphConfiguration.getPadding());

		paintGoals(g2);
		paintCompetences(g2);
		paintPerception(g2);
		paintResources(g2);
		paintConnections(g2);

		usedArea.x += graphConfiguration.getPadding();
		usedArea.y += graphConfiguration.getPadding();
		this.setPreferredSize(new Dimension(usedArea.x, usedArea.y));
		this.revalidate();
	}

	private void paintGoals(Graphics2D g2)
	{
		int xSpacing = graphConfiguration.getGoalSpacing();
		Point startOffset = new Point(graphConfiguration.getPadding(), usedArea.y);
		Iterator<GuiGoal> itGoal = guiElements.getGoals();
		paintNodes(g2, xSpacing, startOffset, itGoal);
	}

	private void paintCompetences(Graphics2D g2)
	{
		usedArea.y += graphConfiguration.getGoalCompSpace();
		int xSpacing = graphConfiguration.getCompSpacing();
		Point startOffset = new Point(graphConfiguration.getPadding(), usedArea.y);
		Iterator<GuiCompetence> itComp = guiElements.getCompetences();
		paintNodes(g2, xSpacing, startOffset, itComp);
	}

	private void paintPerception(Graphics2D g2)
	{
		usedArea.y += graphConfiguration.getCompPercSpace();
		int xSpacing = graphConfiguration.getPercSpacing();
		Point startOffset = new Point(graphConfiguration.getPadding(), usedArea.y);
		Iterator<GuiPerception> itPerc = guiElements.getPercetions();
		paintNodes(g2, xSpacing, startOffset, itPerc);
	}

	private void paintResources(Graphics2D g2)
	{
		usedArea.y += graphConfiguration.getPercResSpace();
		int xSpacing = graphConfiguration.getResSpacing();
		Point startOffset = new Point(graphConfiguration.getPadding(), usedArea.y);
		Iterator<GuiResource> itRes = guiElements.getResources();
		paintNodes(g2, xSpacing, startOffset, itRes);
	}

	private void paintNodes(Graphics2D g2, int xSpacing, Point startOffset, Iterator<? extends GuiNode> itNodes)
	{
		Point offset = new Point(startOffset);
		int drawIndex = 0;
		while (itNodes.hasNext()) {
			GuiNode node = itNodes.next();

			node.setOffsetPoint(new Point(offset));
			node.setDrawIndex(drawIndex);
			node.prepare(graphConfiguration);
			node.draw(g2);

			Point nodeUsedArea = node.getNodeUsedArea();
			offset.x += nodeUsedArea.x + xSpacing;
			drawIndex++;

			Point drawnArea = node.getDrawnArea();
			Point farestPoint = new Point(offset);
			farestPoint.x += drawnArea.x;
			farestPoint.y += drawnArea.y;
			enhanceUsedArea(farestPoint);
		}
	}

	private void enhanceUsedArea(Point farestPoint)
	{
		usedArea = enhancePoint(usedArea, farestPoint);
	}

	public static Point enhancePoint(Point org, Point add)
	{
		if (org == null)
			return new Point(add);
		Point retPoint = new Point();
		retPoint.x = Math.max(org.x, add.x);
		retPoint.y = Math.max(org.y, add.y);

		return retPoint;
	}

	private void paintConnections(Graphics2D g2)
	{
		Iterator<GuiConnection<? extends GuiNode, ? extends GuiNode>> itGuiCon = guiElements.getConnections();

		while (itGuiCon.hasNext()) {
			GuiElement guiCon = itGuiCon.next();
			guiCon.prepare(graphConfiguration);
			guiCon.draw(g2);
		}
	}

	/**
	 * changes the selected component element to the passed one
	 * @param selectable element which should be selected, null if all elements
	 *        should be unselected
	 */
	private void changeSelectionTo(ISelectable selectable)
	{
		if (selectedElement != null)
			selectedElement.setSelected(false);

		if (selectable != null)
			selectable.setSelected(true);
		selectedElement = selectable;

		repaint();
	}

	/**
	 * Mouse Listener to get the mouseclicks from the user
	 * @author Thomas Rinklin
	 */
	class DrawingAreaMouseListener extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent e)
		{
			boolean clickedOnElement = false;

			if (e.getButton() != MouseEvent.BUTTON1 && e.getButton() != MouseEvent.BUTTON3) {
				return;
			}

			clickedOnElement = handleNode(e, clickedOnElement, guiElements.getGoals());
			clickedOnElement = handleNode(e, clickedOnElement, guiElements.getCompetences());
			clickedOnElement = handleNode(e, clickedOnElement, guiElements.getPercetions());
			clickedOnElement = handleNode(e, clickedOnElement, guiElements.getResources());
			if (!clickedOnElement) {
				if (selectedElement != null)
					changeSelectionTo(null);

				if (e.getButton() == MouseEvent.BUTTON3)
					menu.showMenu((Component) e.getSource(), e.getPoint());
			}
		}

		private boolean handleNode(MouseEvent e, boolean clickedOnElement, Iterator<? extends GuiNode> itNodes)
		{
			while (itNodes.hasNext()) {
				GuiNode guiNode = itNodes.next();
				if (guiNode.contains(e.getPoint())) {
					clickedOnElement = true;

					changeSelectionTo(guiNode);

					if (e.getButton() == MouseEvent.BUTTON3)
						guiNode.showMenu((Component) e.getSource(), e.getPoint());
				}
			}
			return clickedOnElement;
		}
	}

	private void setPopupMenu(PopupMenuBuilder popupMenuBuilder)
	{
		this.menu = new MainPopupMenu(popupMenuBuilder);
	}
}
