/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.links;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Float;
import kdo.ebnDevKit.gui.ebnGraphPanel.GraphConfiguration;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiCompetence;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiResource;

public class ResourceConnection extends GuiConnection<GuiCompetence, GuiResource>
{
	private Float line;

	public ResourceConnection(GuiCompetence source, GuiResource target)
	{
		super(source, target);
	}

	@Override
	public void prepare(GraphConfiguration graphConfiguration)
	{
		super.prepare(graphConfiguration);
		Point p1 = source.getBottomConnectionPoint();
		Point p2 = target.getTopConnectionPoint();
		line = new Line2D.Float(p1, p2);
	}

	@Override
	public void draw(Graphics2D g)
	{
		if (shouldPrint(graphConf.resourceConnectionVisibility())) {
			Stroke oldStroke = g.getStroke();
			g.setColor(Color.BLACK);
			g.setStroke(graphConf.getResourceStroke());
			g.draw(line);
			g.setStroke(oldStroke);
		}
	}
}
