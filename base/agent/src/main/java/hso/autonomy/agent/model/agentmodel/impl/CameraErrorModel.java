/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.model.agentmodel.ICameraErrorModel;

public abstract class CameraErrorModel implements ICameraErrorModel
{
	protected double maxDistance;

	public CameraErrorModel(double maxDistance)
	{
		this.maxDistance = maxDistance;
	}

	@Override
	public double getMaxDistance()
	{
		return maxDistance;
	}
}
