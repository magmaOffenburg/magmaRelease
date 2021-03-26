/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception;

/**
 * The Universal Joint Perceptor measures the angles of a two-axis joint.
 *
 * @author Simon Raffeiner
 */
public interface ICompositeJointPerceptor extends IPerceptor {
	/**
	 * @return the first hinge joint perceptor
	 */
	IHingeJointPerceptor[] getJoints();
}