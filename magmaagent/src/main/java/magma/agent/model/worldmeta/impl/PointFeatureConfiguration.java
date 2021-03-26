/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta.impl;

import magma.agent.model.worldmeta.IPointFeatureConfiguration;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Class for holding a point feature configuration.
 *
 * @author Stefan Glaser
 */
public class PointFeatureConfiguration extends GeometricFeatureConfiguration implements IPointFeatureConfiguration
{
	/** The known position of this point feature. */
	private final Vector3D knownPosition;

	public PointFeatureConfiguration(String name, String type, Vector3D knownPosition)
	{
		super(name, type);

		this.knownPosition = knownPosition;
	}

	@Override
	public Vector3D getKnownPosition()
	{
		return knownPosition;
	}
}
