/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta.impl;

import magma.agent.model.worldmeta.ILineFeatureConfiguration;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Class for holding a line feature configuration.
 *
 * @author Stefan Glaser
 */
public class LineFeatureConfiguration extends GeometricFeatureConfiguration implements ILineFeatureConfiguration
{
	/** The first known position of the line feature. */
	private final Vector3D knownPosition1;

	/** The second known position of the line feature. */
	private final Vector3D knownPosition2;

	public LineFeatureConfiguration(String name, String type, Vector3D knownPosition1, Vector3D knownPosition2)
	{
		super(name, type);

		this.knownPosition1 = knownPosition1;
		this.knownPosition2 = knownPosition2;
	}

	@Override
	public Vector3D getKnownPosition1()
	{
		return knownPosition1;
	}

	@Override
	public Vector3D getKnownPosition2()
	{
		return knownPosition2;
	}
}
