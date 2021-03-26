/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.common.challenge;

import hso.autonomy.util.geometry.Area2D;

public class KeepAwayArea
{
	private static final float CENTER_X = 0;

	private static final float CENTER_Y = 0;

	private static final float WIDTH = 20;

	private static final float LENGTH = 20;

	private static final float WIDTH_REDUCTION_RATE = 4;

	private static final float LENGTH_REDUCTION_RATE = 4;

	private static final Area2D.Float area = new Area2D.Float(
			CENTER_X - LENGTH / 2.0f, CENTER_X + LENGTH / 2.0f, CENTER_Y - WIDTH / 2.0f, CENTER_Y + WIDTH / 2.0f);

	public static Area2D.Float calculate(float time)
	{
		time /= 60;
		float lengthReduction = LENGTH_REDUCTION_RATE / 2.0f * time;
		float widthReduction = WIDTH_REDUCTION_RATE / 2.0f * time;
		return area.applyBorder(Math.min(area.getMaxX(), lengthReduction), Math.min(area.getMaxY(), widthReduction));
	}
}
