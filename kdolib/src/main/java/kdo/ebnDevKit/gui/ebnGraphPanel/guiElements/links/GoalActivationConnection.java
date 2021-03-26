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
import kdo.ebnDevKit.ebnAccess.IEbnActivationFlow.IEbnGoalActivationFlow;
import kdo.ebnDevKit.gui.ebnGraphPanel.GraphConfiguration;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiCompetence;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiGoal;

/**
 * Activation connection between a goal and a competence
 * @author Thomas Rinklin
 *
 */
public class GoalActivationConnection extends GuiConnection<GuiGoal, GuiCompetence>
{
	public final boolean excitation;

	private Stroke stroke;

	private Shape line;

	public GoalActivationConnection(GuiGoal source, GuiCompetence target, IEbnGoalActivationFlow ebnGoalActivationFlow)
	{
		super(source, target);
		this.excitation = ebnGoalActivationFlow.isExcitation();
	}

	@Override
	public void prepare(GraphConfiguration graphConfiguration)
	{
		super.prepare(graphConfiguration);

		stroke = excitation ? graphConfiguration.getExcitationStroke() : graphConfiguration.getInhibitationStroke();

		Point p1 = source.getConnectionPoint();
		Point p2 = target.getTopConnectionPoint();

		line = new Line2D.Float(p1, p2);
	}

	@Override
	public void draw(Graphics2D g)
	{
		if (shouldPrint(graphConf.goalActivationConnectionVisibility())) {
			g.setColor(Color.BLACK);

			Stroke oldStroke = g.getStroke();
			g.setStroke(stroke);
			g.draw(line);
			g.setStroke(oldStroke);
		}
	}
}