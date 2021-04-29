/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.movement;

import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.agentmodel.IHingeJoint;
import java.io.Serializable;
import magma.agent.IHumanoidJoints;

/**
 * A single joint movement specification
 * @author dorer
 */
public class MovementSingle implements Serializable
{
	/** name of the joint to move */
	private String jointName;

	/** the angle to which to move (in degrees) */
	private double jointAngle;

	/** the speed with which to move (in degrees / cycle) */
	private float speed;

	/**
	 * @param jointName name of the joint to move
	 * @param jointAngle the angle to which to move (in degrees)
	 * @param speed the speed with which to move (in degrees / cycle)
	 */
	public MovementSingle(String jointName, double jointAngle, float speed)
	{
		this.jointName = jointName;
		this.jointAngle = jointAngle;
		this.speed = speed;
	}

	/**
	 * @param agentModel the model with the joints
	 * @return true if the joint has reached its desired angle
	 */
	public boolean move(IAgentModel agentModel, float relativeSpeed)
	{
		IHingeJoint writeableHJ = agentModel.getWriteableHJ(jointName);
		if (writeableHJ == null) {
			// allow toe pitch to make it easier to support both
			if (!jointName.equals(IHumanoidJoints.LToePitch) && !jointName.equals(IHumanoidJoints.RToePitch)) {
				System.err.println("No such joint: " + jointName);
			}
			return false;
		}

		float speedUsed = writeableHJ.performAxisPosition(jointAngle, speed * 1.0f / relativeSpeed);
		return Math.abs(speedUsed) <= 0.01;
	}

	/**
	 * @return a left handed version of this single movement
	 */
	public MovementSingle getLeftVersion()
	{
		String newName = jointName;
		if (newName.startsWith("R")) {
			newName = "L" + newName.substring(1);
		} else if (newName.startsWith("L")) {
			newName = "R" + newName.substring(1);
		}

		double newAngle = jointAngle;
		if (newName.contains("Roll")) {
			newAngle = -newAngle;
		}
		return new MovementSingle(newName, newAngle, speed);
	}

	public String getJointName()
	{
		return jointName;
	}

	public double getJointAngle()
	{
		return jointAngle;
	}

	public float getSpeed()
	{
		return speed;
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	@Override
	public String toString()
	{
		return jointName + " angle: " + jointAngle + " speed: " + speed;
	}
}
