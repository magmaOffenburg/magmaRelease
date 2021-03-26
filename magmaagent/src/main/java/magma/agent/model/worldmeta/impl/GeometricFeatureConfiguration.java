/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta.impl;

import magma.agent.model.worldmeta.IGeometricFeatureConfiguration;

/**
 * Base class for {@link IGeometricFeatureConfiguration}s.
 *
 * @author Stefan Glaser
 */
public class GeometricFeatureConfiguration implements IGeometricFeatureConfiguration
{
	/** The unique name of the feature. */
	protected final String name;

	/** The type information. */
	protected final String type;

	public GeometricFeatureConfiguration(String name, String type)
	{
		this.name = name;
		this.type = type;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getType()
	{
		return type;
	}
}
