/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer.impl;

import hso.autonomy.agent.model.worldmodel.localizer.IFeatureObservation;
import hso.autonomy.agent.model.worldmodel.localizer.IGeometricFeature;

/**
 * Base class for observations of {@link IGeometricFeature}s.
 *
 * @author Stefan Glaser
 */
public abstract class FeatureObservationBase implements IFeatureObservation
{
	/** The time this observation was made. */
	protected double time;

	/** The type of the feature. */
	protected String type;

	/** Indicator for if this observation contains depth information. */
	protected boolean hasDepth;

	/** The unique name of the corresponding {@link IGeometricFeature}. */
	protected String name;

	/**
	 * Construct a new instance with the given type information.<br>
	 * By default, the observation will have no depth information and no name assigned to it.
	 *
	 * @param time the time of this observation
	 * @param type the observed type information
	 */
	public FeatureObservationBase(double time, String type)
	{
		this(time, type, false);
	}

	/**
	 * Construct a new instance with the given type information and hasDepth indicator.<br>
	 * By default, the observation will have no name assigned to it.
	 *
	 * @param time the time of this observation
	 * @param type the observed type information
	 * @param hasDepth true, if the observation contains depth information, false otherwise
	 */
	public FeatureObservationBase(double time, String type, boolean hasDepth)
	{
		this(time, type, hasDepth, null);
	}

	/**
	 * Construct a new instance with the given type, hasDepth indicator and feature name.
	 *
	 * @param time the time of this observation
	 * @param type the observed type information
	 * @param hasDepth true, if the observation contains depth information, false otherwise
	 * @param name the unique name of the corresponding {@link IGeometricFeature}
	 */
	public FeatureObservationBase(double time, String type, boolean hasDepth, String name)
	{
		this.time = time;
		this.type = type;
		this.hasDepth = hasDepth;
		this.name = name;
	}

	@Override
	public double getTime()
	{
		return time;
	}

	@Override
	public String getType()
	{
		return type;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean hasDepth()
	{
		return hasDepth;
	}

	@Override
	public void assign(String name)
	{
		this.name = name;
	}

	@Override
	public boolean isAssigned()
	{
		return name != null;
	}

	@Override
	public String toString()
	{
		return type + " " + name;
	}
}
