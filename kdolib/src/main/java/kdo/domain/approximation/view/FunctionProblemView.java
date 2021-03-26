/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain.approximation.view;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import kdo.domain.IIndividuumView;
import kdo.domain.approximation.model.FunctionIndividuum;
import kdo.domain.view.DefaultIndividuumView;

/**
 * Canvas that visualizes the function problem approximation
 * @author dorer
 */
public class FunctionProblemView extends DefaultIndividuumView implements IIndividuumView
{
	private static final long serialVersionUID = -7255909745075692000L;

	private JRadioButton sin;

	private JRadioButton cos;

	private JRadioButton log;

	/**
	 * Default Constructor
	 */
	public FunctionProblemView()
	{
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(3, 1));
		ButtonGroup radios = new ButtonGroup();
		sin = new JRadioButton("sin", true);
		sin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				((FunctionIndividuum) currentIndividuum.getRootIndividuum()).setFunction("sin");
				canvas.repaint();
			}
		});
		radios.add(sin);
		buttons.add(sin);

		cos = new JRadioButton("cos");
		cos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				((FunctionIndividuum) currentIndividuum.getRootIndividuum()).setFunction("cos");
				canvas.repaint();
			}
		});
		radios.add(cos);
		buttons.add(cos);

		log = new JRadioButton("log");
		log.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				((FunctionIndividuum) currentIndividuum.getRootIndividuum()).setFunction("log");
				canvas.repaint();
			}
		});
		radios.add(log);
		buttons.add(log);

		add(BorderLayout.EAST, buttons);
	}

	@Override
	protected Canvas createCanvas()
	{
		return new FunctionProblemCanvas();
	}

	class FunctionProblemCanvas extends Canvas
	{
		private static final long serialVersionUID = 1L;

		public FunctionProblemCanvas()
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
			for (int i = 0; i < chromosom.length; i++) {
				drawBar(g, (int) chromosom[i], i);
			}
		}

		/**
		 * Draws a 2D bar
		 * @param g the graphics context
		 * @param value the value to draw
		 * @param offset the x offset
		 */
		public void drawBar(Graphics g, int value, int offset)
		{
			int x = 10 + offset * 20;
			int y = 170;
			if (value > 0) {
				y -= value;
			}
			int parentID = currentIndividuum.getParentID(offset);
			if (currentIndividuum.isMutated(offset)) {
				g.setColor(Color.RED);
			} else {
				if (parentID < 2) {
					g.setColor(Color.BLUE);
				} else if (parentID == 2) {
					g.setColor(Color.GREEN);
				} else {
					int color = (parentID * 40) % 255;
					g.setColor(new Color(0, color, color));
				}
			}
			g.drawRect(x, y, 15, Math.abs(value));

			y = 170 - (int) ((FunctionIndividuum) currentIndividuum.getRootIndividuum()).getValue(offset);
			g.setColor(Color.BLACK);
			g.drawRect(x, y, 5, 5);
		}
	}
}
