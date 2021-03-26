/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.IVisibleObjectPerceptor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Visible Object
 *
 * @author Simon Raffeiner, Stefan Glaser
 */
public class VisibleObjectPerceptor extends Perceptor implements IVisibleObjectPerceptor
{
	/** The type information associated with this visual object perception. */
	private final String type;

	/** Object position in the camera coordinate system. */
	private Vector3D position;

	/** true if this perceptor has depth information in the position, false otherwise. */
	private final boolean hasDepth;

	/** How confident we are in the perception [0..1]. */
	private final double confidence;

	public VisibleObjectPerceptor(String type, Vector3D position, boolean hasDepth, double confidence)
	{
		this(type, position, hasDepth, confidence, null);
	}

	public VisibleObjectPerceptor(String type, Vector3D position, boolean hasDepth, double confidence, String name)
	{
		super(name);

		this.type = type;
		this.position = position;
		this.hasDepth = hasDepth;
		this.confidence = confidence;
	}

	@Override
	public String getType()
	{
		return type;
	}

	@Override
	public Vector3D getPosition()
	{
		return position;
	}

	@Override
	public void setPosition(Vector3D position)
	{
		this.position = position;
	}

	@Override
	public double getDistance()
	{
		return position.getNorm();
	}

	@Override
	public double getHorizontalAngle()
	{
		return position.getAlpha();
	}

	@Override
	public double getHorizontalAngleDeg()
	{
		return Math.toDegrees(position.getAlpha());
	}

	@Override
	public double getLatitudeAngle()
	{
		return position.getDelta();
	}

	@Override
	public double getLatitudeAngleDeg()
	{
		return Math.toDegrees(position.getDelta());
	}

	@Override
	public boolean hasDepth()
	{
		return hasDepth;
	}

	public double getConfidence()
	{
		return confidence;
	}
}
