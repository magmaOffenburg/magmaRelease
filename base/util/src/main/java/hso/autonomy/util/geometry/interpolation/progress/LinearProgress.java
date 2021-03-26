/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry.interpolation.progress;

/**
 * @author Stefan Glaser
 */
public class LinearProgress extends ProgressFunction
{
	@Override
	protected float calculateProgress(float t)
	{
		return t;
	}
}
