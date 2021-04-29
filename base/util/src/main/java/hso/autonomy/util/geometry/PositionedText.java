/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import java.awt.Color;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author kdorer
 *
 */
public class PositionedText
{
	public final String text;
	public final Vector2D position;
	public final Color color;
	public final float thickness;

	/**
	 *
	 */
	public PositionedText(String text, Vector2D position, Color color, float thickness)
	{
		this.text = text;
		this.position = position;
		this.color = color;
		this.thickness = thickness;
	}
}
