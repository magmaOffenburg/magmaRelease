/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer.impl;

import hso.autonomy.agent.model.worldmodel.localizer.IPointCorrespondence;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Simple immutable class, combining a local position and a known position.
 *
 * @author Stefan Glaser
 */
public class PointCorrespondence implements IPointCorrespondence
{
	/** The position observed in our local frame. */
	private final Vector3D observedPosition;

	/** The known position in the map. */
	private final Vector3D knownPosition;

	/**
	 * Construct a new instance from the given observed and known positions.
	 *
	 * @param observedPosition the observed position
	 * @param knownPosition the known position in the map
	 */
	public PointCorrespondence(Vector3D observedPosition, Vector3D knownPosition)
	{
		this.observedPosition = observedPosition;
		this.knownPosition = knownPosition;
	}

	@Override
	public Vector3D getKnownPosition()
	{
		return knownPosition;
	}

	@Override
	public Vector3D getObservedPosition()
	{
		return observedPosition;
	}
}
