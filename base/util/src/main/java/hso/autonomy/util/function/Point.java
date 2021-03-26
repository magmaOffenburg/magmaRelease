/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * A simple class representing a two dimensional point for functions.
 *
 * @author Stefan Glaser
 */
public class Point implements IPoint
{
	/** The x coordinate of the point */
	protected float x;

	/** The y coordinate of the point */
	protected float y;

	public Point()
	{
		this(0, 0);
	}

	public Point(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public Point(Point source)
	{
		this.x = source.x;
		this.y = source.y;
	}

	@Override
	public float getX()
	{
		return x;
	}

	@Override
	public float getY()
	{
		return y;
	}

	protected void setX(float x)
	{
		this.x = x;
	}

	protected void setY(float y)
	{
		this.y = y;
	}

	protected void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	protected void move(float xMove, float yMove)
	{
		this.x += xMove;
		this.y += yMove;
	}

	@Override
	public Vector2D asVector2D()
	{
		return new Vector2D(x, y);
	}

	@Override
	public String toString()
	{
		return String.format("(%s %s)", x, y);
	}
}
