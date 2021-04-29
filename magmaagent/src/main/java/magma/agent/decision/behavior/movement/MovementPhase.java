/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.movement;

import hso.autonomy.agent.model.agentmodel.IAgentModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One phase of movement containing the information of which single joints to
 * move as MovementSingles
 * @author dorer
 */
public class MovementPhase implements Serializable
{
	/** the list of movements */
	private List<MovementSingle> movementSingles;

	/** the number of cycles this phase should run */
	private final int cycles;

	/** the number of cycles this phase is running */
	private int cyclesPerformed;

	/** name of the phase */
	private final String name;

	/** true if the behavior can be stopped when all joints have reached angle */
	private final boolean skipWhenFinished;

	/** the relative speed this phase is performed in */
	private float relativeSpeed;

	/**
	 * @param cycles the number of cycles this phase is running
	 */
	public MovementPhase(String name, int cycles)
	{
		this(name, cycles, false);
	}

	/**
	 * @param name the name of this phase
	 * @param cycles the number of cycles this phase is running
	 * @param skipWhenFinished true if this phase is stopped when all joints have
	 *        reached the destination
	 */
	public MovementPhase(String name, int cycles, boolean skipWhenFinished)
	{
		this.name = name;
		this.cycles = cycles;
		this.skipWhenFinished = skipWhenFinished;
		cyclesPerformed = 0;
		movementSingles = new ArrayList<>();
		relativeSpeed = 1.0f;
	}

	public MovementPhase add(MovementSingle move)
	{
		movementSingles.add(move);
		return this;
	}

	public MovementPhase add(String jointName, double jointAngle)
	{
		// the speed will be set later
		MovementSingle move = new MovementSingle(jointName, jointAngle, Float.MAX_VALUE);
		movementSingles.add(move);
		return this;
	}

	public MovementPhase add(String jointName, double jointAngle, float speed)
	{
		MovementSingle move = new MovementSingle(jointName, jointAngle, speed);
		movementSingles.add(move);
		return this;
	}

	/**
	 * Sets the speeds of all movements according to the speed required to get
	 * from the previous phases joint position to the current within the
	 * specified cycle times. It uses default values if no previous phase is
	 * existing, if in the previous phase there is no single movement for this
	 * joint or if this phase has no cycles.
	 * @param previousPhase reference to the previous phase of this movement
	 */
	protected void setSpeeds(MovementPhase previousPhase)
	{
		for (MovementSingle movement : movementSingles) {
			if (movement.getSpeed() == Float.MAX_VALUE) {
				// we have to calculate the speed
				double previousAngle = 0;
				if (previousPhase != null) {
					MovementSingle prevMovement = previousPhase.find(movement.getJointName());
					if (prevMovement != null) {
						previousAngle = prevMovement.getJointAngle();
					}
				}
				float speed = 1.0f;
				if (cycles > 0) {
					speed = (float) ((movement.getJointAngle() - previousAngle) / cycles);
					// it seems as if speed is used as a positive max speed
					speed = Math.abs(speed);
				}
				movement.setSpeed(speed);
			}
		}
	}

	public MovementSingle find(String name)
	{
		for (MovementSingle move : movementSingles) {
			if (move.getJointName().equals(name)) {
				return move;
			}
		}
		return null;
	}

	/**
	 * Resets the state information
	 */
	public void init()
	{
		cyclesPerformed = 0;
	}

	/**
	 * Calls move for all single movements
	 * @param agent reference to the agent model
	 * @return true if the phase should continue
	 */
	public boolean perform(IAgentModel agent)
	{
		boolean reached = true;
		for (MovementSingle current : movementSingles) {
			reached = current.move(agent, relativeSpeed) && reached;
		}

		if (skipWhenFinished && reached) {
			// we have reached the destination, so stop the movement if dynamic
			// System.out.println("stop: " + cyclesPerformed + " cycles: " +
			// cycles);
			cyclesPerformed = (int) (cycles * relativeSpeed);
		}

		cyclesPerformed++;
		if (cyclesPerformed >= (int) (cycles * relativeSpeed)) {
			cyclesPerformed = 0;
			return false;
		}
		return true;
	}

	public int getCyclesPerformed()
	{
		return cyclesPerformed;
	}

	public int getCycles()
	{
		return cycles;
	}

	public String getName()
	{
		return name;
	}

	public void setRelativeSpeed(float speed)
	{
		if (speed >= 1) {
			relativeSpeed = speed;
		} else {
			relativeSpeed = 1;
		}
	}

	public boolean isSkipWhenFinished()
	{
		return skipWhenFinished;
	}

	public List<MovementSingle> getMovementSingles()
	{
		return Collections.unmodifiableList(movementSingles);
	}

	public MovementPhase getLeftVersion()
	{
		String newName = name.replace("Right", "Left");
		MovementPhase result = new MovementPhase(newName, cycles, skipWhenFinished);
		for (MovementSingle current : movementSingles) {
			MovementSingle newMovement = current.getLeftVersion();
			result.add(newMovement);
		}
		return result;
	}

	@Override
	public String toString()
	{
		return name + " cycles: " + cyclesPerformed;
	}
}
