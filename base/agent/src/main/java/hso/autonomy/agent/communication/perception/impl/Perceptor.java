/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.IPerceptor;
import java.io.Serializable;

/**
 * @author Simon Raffeiner
 */
public abstract class Perceptor implements IPerceptor, Serializable
{
	private final String name;

	public Perceptor()
	{
		this("default");
	}

	public Perceptor(String name)
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return name;
	}
}
