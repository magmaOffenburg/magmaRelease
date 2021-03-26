/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.IReferencePointPerceptor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * The Reference Point Perceptor represents a single point with it's local seen
 * position referenced to it's global known position
 *
 * @author Rico Schillings
 */
public class ReferencePointPerceptor extends Perceptor implements IReferencePointPerceptor
{
	private String label;

	private Vector3D seenPosition;

	public ReferencePointPerceptor(String name, Vector3D seenPosition, String label)
	{
		super(name);
		this.label = label;
		this.seenPosition = seenPosition;
	}

	@Override
	public String getLabel()
	{
		return label;
	}

	@Override
	public void setLabel(String label)
	{
		this.label = label;
	}

	@Override
	public Vector3D getSeenPosition()
	{
		return seenPosition;
	}

	@Override
	public void setSeenPosition(Vector3D seenPosition)
	{
		this.seenPosition = seenPosition;
	}
}
