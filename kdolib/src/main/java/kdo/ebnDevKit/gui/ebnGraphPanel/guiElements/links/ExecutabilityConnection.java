/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.links;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnProposition;
import kdo.ebnDevKit.gui.ebnGraphPanel.GraphConfiguration;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.IRefreshable;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiCompetence;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiPerception;

/**
 * Class for drawing executability connections
 * @author Thomas Rinklin
 */
public class ExecutabilityConnection extends GuiConnection<GuiCompetence, GuiPerception> implements IRefreshable
{
	private Stroke stroke;

	private Shape line;

	private final IEbnProposition ebnProposition;

	private double truthValue;

	private Color color;

	public ExecutabilityConnection(GuiCompetence source, GuiPerception target, IEbnProposition ebnProposition)
	{
		super(source, target);
		this.ebnProposition = ebnProposition;
	}

	@Override
	public void refreshValues()
	{
		double truthVal = ebnProposition.getPerception().getTruthValue();

		if (ebnProposition.isNegated())
			truthVal = 1 - truthVal;
		truthValue = truthVal;
	}

	@Override
	public void prepare(GraphConfiguration graphConfiguration)
	{
		super.prepare(graphConfiguration);

		stroke = graphConfiguration.getExecutionStroke();

		Point p1 = source.getBottomConnectionPoint();
		Point p2 = target.getTopConnectionPoint();

		line = new Line2D.Float(p1, p2);
		color = new Color((int) (255 * truthValue), 0, 0);
	}

	@Override
	public void draw(Graphics2D g)
	{
		if (shouldPrint(graphConf.executionConnectionVisibility())) {
			g.setColor(color);

			Stroke oldStroke = g.getStroke();
			g.setStroke(stroke);
			g.draw(line);
			g.setStroke(oldStroke);
		}
	}
}
