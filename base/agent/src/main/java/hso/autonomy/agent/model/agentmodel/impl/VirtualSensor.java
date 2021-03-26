/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.util.geometry.Pose3D;

/**
 * This class is used to represent software-based information sources, e.g. map-based informations
 */
public abstract class VirtualSensor extends Sensor
{
	public VirtualSensor(String name)
	{
		super(name, name, new Pose3D());
	}
}
