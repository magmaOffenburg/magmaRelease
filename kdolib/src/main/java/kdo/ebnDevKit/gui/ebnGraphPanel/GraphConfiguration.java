/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class which stores the graphical configuration centralized
 * @author Thomas Rinklin
 */
public class GraphConfiguration
{
	private final RenderingHints rh;

	private final AlphaComposite ac;

	private final Font font;

	private final Stroke inhibitionStroke;

	private final Stroke excitationStroke;

	private final Stroke executionStroke;

	private final Stroke resourceStroke;

	private final NumberFormat numberFormat;

	public enum ConnectionVisibility { ALWAYS, TARGET_OR_SOURCE_SELECTED, TARGET_SELECTED, SOURCE_SELECTED, NEVER }

	public enum NodeVisibility { ALWAYS, SELECTED, NEVER }

	public GraphConfiguration()
	{
		Map<RenderingHints.Key, Object> rhm = new HashMap<RenderingHints.Key, Object>();
		rhm.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		rhm.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rhm.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		rhm.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		rhm.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		rhm.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		rhm.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		rhm.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		rhm.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		rh = new RenderingHints(rhm);

		numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(3);

		// 30% transparency
		ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);

		font = new Font("Arial", Font.PLAIN, 12);

		float[] inhibDashpattern = {6f, 6f};
		inhibitionStroke = new BasicStroke(1, 1, 1, 1, inhibDashpattern, 0);

		excitationStroke = new BasicStroke();

		float[] execDashpattern = {15f, 15f};
		executionStroke = new BasicStroke(1, 1, 1, 1, execDashpattern, 0);

		float[] resDashpattern = {10f, 5f};
		resourceStroke = new BasicStroke(1, 1, 1, 1, resDashpattern, 0);
	}

	/**
	 * returns the rendering hints (for the rendering quality)
	 * @return the rendering hints
	 */
	public Map<?, ?> getRenderingHints()
	{
		return rh;
	}

	/**
	 * returns the alphacomposite for the text background
	 * @return the alphacomposite for the text background
	 */
	public AlphaComposite getAlphaComposite()
	{
		return ac;
	}

	/**
	 * retruns the font
	 * @return the font
	 */
	public Font getFont()
	{
		return font;
	}

	/**
	 * returns the border of the text background
	 * @return the border of the text background
	 */
	public int getTextBorder()
	{
		return 2;
	}

	/**
	 * returns the distance to the border
	 * @return distance to the border
	 */
	public int getPadding()
	{
		return 10;
	}

	/**
	 * returns the size of the goal elements
	 * @return the size of the goal elements
	 */
	public int getGoalSize()
	{
		return 60;
	}

	/**
	 * returns the size of the inner part of the goal elements
	 * @return the size of the inner part of the goal elements
	 */
	public int getGoalInnerSize()
	{
		return 40;
	}

	/**
	 * returns the distance between two adjoining goals
	 * @return distance between two adjoining goals
	 */
	public int getGoalSpacing()
	{
		return 60;
	}

	/**
	 * returns the distance between goals and competences
	 * @return distance between goals and competences
	 */
	public int getGoalCompSpace()
	{
		return 70;
	}

	/**
	 * returns the size of the competence elements
	 * @return the size of the competence elements
	 */
	public int getCompSize()
	{
		return 60;
	}

	/**
	 * returns the size of the inner part of the competence elements
	 * @return the size of the inner part of the competence elements
	 */
	public int getCompInnerSize()
	{
		return 40;
	}

	/**
	 * returns the distance between two adjoining competences
	 * @return distance between two adjoining competences
	 */
	public int getCompSpacing()
	{
		return 70;
	}

	/**
	 * returns the distance between competences and perceptions
	 * @return distance between competences and perceptions
	 */
	public int getCompPercSpace()
	{
		return 80;
	}

	/**
	 * returns the size of the perception elements
	 * @return the size of the perception elements
	 */
	public int getPercSize()
	{
		return 50;
	}

	/**
	 * returns the distance between two adjoining perceptions
	 * @return distance between two adjoining perceptions
	 */
	public int getPercSpacing()
	{
		return 40;
	}

	/**
	 * returns the distance between perceptions amd resources
	 * @return distance between perceptions amd resources
	 */
	public int getPercResSpace()
	{
		return 50;
	}

	/**
	 * returns the distance between two adjoining perceptions
	 * @return distance between two adjoining perceptions
	 */
	public int getResSpacing()
	{
		return 70;
	}

	/**
	 * returns the size of the resource elements
	 * @return the size of the resource elements
	 */
	public int getResourceSize()
	{
		return 30;
	}

	/**
	 * returns stroke of inhibitation links
	 * @return stroke of inhibitation links
	 */
	public Stroke getInhibitationStroke()
	{
		return inhibitionStroke;
	}

	/**
	 * returns stroke of excitation links
	 * @return stroke of excitation links
	 */
	public Stroke getExcitationStroke()
	{
		return excitationStroke;
	}

	/**
	 * returns the stroke of execution links
	 * @return stroke of execution links
	 */
	public Stroke getExecutionStroke()
	{
		return executionStroke;
	}

	/**
	 * returns the goal activation visibility
	 * @return the goal activation visibility
	 */
	public ConnectionVisibility goalActivationConnectionVisibility()
	{
		return ConnectionVisibility.ALWAYS;
	}

	/**
	 * returns the competence activation visibility
	 * @return the competence activation visibility
	 */
	public ConnectionVisibility competenceActivationConnectionVisibility()
	{
		return ConnectionVisibility.ALWAYS;
	}

	/**
	 * returns the execution link visibility
	 * @return the goal activation visibility
	 */
	public ConnectionVisibility executionConnectionVisibility()
	{
		return ConnectionVisibility.ALWAYS;
	}

	/**
	 * returns the resource link visibility
	 * @return the resource link visibility
	 */
	public ConnectionVisibility resourceConnectionVisibility()
	{
		return ConnectionVisibility.TARGET_OR_SOURCE_SELECTED;
	}

	/**
	 * returns the resource link stroke
	 * @return the resource link stroke
	 */
	public Stroke getResourceStroke()
	{
		return resourceStroke;
	}

	/**
	 * returns the number format
	 * @return the number format
	 */
	public NumberFormat getNumberFormat()
	{
		return numberFormat;
	}
}
