/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.ICompositeJointPerceptor;
import hso.autonomy.agent.communication.perception.IHingeJointPerceptor;

/**
 * Universal Joint perceptor
 *
 * @author Simon Raffeiner
 */
public class CompositeJointPerceptor extends Perceptor implements ICompositeJointPerceptor
{
	private IHingeJointPerceptor[] joints;

	public CompositeJointPerceptor(String name, float[] axis)
	{
		super(name);
		for (int i = 0; i < axis.length; i++) {
			joints[i] = new HingeJointPerceptor(name, axis[i]);
		}
	}

	public CompositeJointPerceptor(String name, IHingeJointPerceptor[] perceptors)
	{
		super(name);
		joints = perceptors;
	}

	@Override
	public IHingeJointPerceptor[] getJoints()
	{
		return joints;
	}
}
