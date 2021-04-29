/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.domain.view;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import kdo.domain.IIndividuum;
import kdo.domain.IIndividuumView;
import kdo.search.strategy.local.genetic.IPopulation;

/**
 * Canvas that visualizes the function problem approximation
 * @author dorer
 */
public class DefaultIndividuumView extends JPanel implements IIndividuumView
{
	private static final long serialVersionUID = -7255909745075692000L;

	/** the individuum to display */
	protected IIndividuum currentIndividuum;

	/** the overall population */
	protected IPopulation population;

	/** the canvas to display */
	protected Canvas canvas;

	/**
	 * Default Constructor
	 */
	public DefaultIndividuumView()
	{
		canvas = createCanvas();
		add(BorderLayout.CENTER, canvas);
	}

	protected Canvas createCanvas()
	{
		return new DefaultCanvas();
	}

	/**
	 * @param currentIndividuum the currentIndividuum to set
	 */
	@Override
	public void setCurrentIndividuum(IIndividuum currentIndividuum)
	{
		this.currentIndividuum = currentIndividuum;
		canvas.repaint();
	}

	@Override
	public JPanel getDisplayPanel()
	{
		return this;
	}

	class DefaultCanvas extends Canvas
	{
		private static final long serialVersionUID = 1L;

		public DefaultCanvas()
		{
			setSize(700, 400);
			setBackground(Color.WHITE);
			setVisible(true);
		}

		@Override
		public void paint(Graphics g)
		{
			super.paint(g);
			if (currentIndividuum == null) {
				return;
			}

			float[] chromosom = currentIndividuum.getChromosom();
			int length = Math.min(10, chromosom.length);
			int x = 10;
			int y = 10;
			g.drawString("fitness: " + currentIndividuum.getFitness(), x, y);

			y += 20;
			g.drawString("age: " + currentIndividuum.getAge(), x, y);

			y += 20;
			g.drawString("chromosom: ", x, y);
			x += 50;
			for (int i = 0; i < length; i++) {
				x += 70;
				g.drawString(chromosom[i] + ", ", x, y);
			}
			x += 50;
			g.drawString("...", x, y);
		}
	}

	@Override
	public void setPopulation(IPopulation population)
	{
		this.population = population;
	}
}
