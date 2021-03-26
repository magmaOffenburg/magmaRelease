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
import java.awt.geom.QuadCurve2D;
import kdo.ebnDevKit.ebnAccess.IEbnActivationFlow.IEbnCompetenceActivationFlow;
import kdo.ebnDevKit.gui.ebnGraphPanel.GraphConfiguration;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiCompetence;

/**
 * activation connection between competences
 * @author Thomas Rinklin
 *
 */
public class CompActivationConnection extends GuiConnection<GuiCompetence, GuiCompetence>
{
	/** excitation flag */
	private final boolean excitation;

	private Stroke stroke;

	private Shape quadcurve;

	public CompActivationConnection(
			GuiCompetence source, GuiCompetence target, IEbnCompetenceActivationFlow ebnCompActivationFlow)
	{
		super(source, target);
		this.excitation = ebnCompActivationFlow.isExcitation();
	}

	@Override
	public void prepare(GraphConfiguration graphConfiguration)
	{
		super.prepare(graphConfiguration);

		stroke = excitation ? graphConfiguration.getExcitationStroke() : graphConfiguration.getInhibitationStroke();

		Point pSource;
		Point pTarget;
		Point pControl;
		boolean leftToRight = source.getDrawIndex() < target.getDrawIndex();

		if (leftToRight) {
			pSource = source.getTopConnectionPoint();
			pTarget = target.getTopConnectionPoint();
		} else {
			pSource = source.getBottomConnectionPoint();
			pTarget = target.getBottomConnectionPoint();
		}

		pControl = new Point();
		pControl.x = (pTarget.x + pSource.x) / 2;
		pControl.y = (pTarget.y + pSource.y) / 2;

		if (leftToRight) {
			pControl.y -= 40;
		} else {
			pControl.y += 40;
		}

		quadcurve = new QuadCurve2D.Float(pSource.x, pSource.y, pControl.x, pControl.y, pTarget.x, pTarget.y);
	}

	@Override
	public void draw(Graphics2D g)
	{
		if (shouldPrint(graphConf.competenceActivationConnectionVisibility())) {
			g.setColor(Color.BLACK);
			Stroke oldStroke = g.getStroke();
			g.setStroke(stroke);
			g.draw(quadcurve);
			g.setStroke(oldStroke);
		}
	}
}
