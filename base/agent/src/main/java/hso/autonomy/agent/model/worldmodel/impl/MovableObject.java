/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.impl;

import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.misc.FuzzyCompare;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Base class for all movable objects on the field
 *
 * @author Klaus Dorer
 */
public abstract class MovableObject extends VisibleObject implements IMoveableObject
{
	/** the speed calculated from previous and current positions in m/cycle */
	protected Vector3D speed;

	/** the speed as has been before last update */
	protected Vector3D oldSpeed;

	/** the position when last measured speed in the global coordinate system */
	protected Vector3D positionAtLastSpeedMeasurement;

	/** the time when last measured speed (in global time)*/
	protected float timeAtLastSpeedMeasurement;

	protected final float cycleTime;

	/** caching future positions */
	protected transient Vector3D[] futurePositions;

	/**
	 * @param name name of the object
	 * @param cycleTime the fixed time between two update cycles
	 */
	protected MovableObject(String name, float cycleTime)
	{
		super(name);
		this.cycleTime = cycleTime;
		speed = Vector3D.ZERO;
		oldSpeed = Vector3D.ZERO;
		positionAtLastSpeedMeasurement = position;
		timeAtLastSpeedMeasurement = lastSeenTime;
	}

	@Override
	public Vector3D[] getFuturePositions(int howMany)
	{
		if (futurePositions == null || futurePositions.length < howMany) {
			futurePositions = calculateFuturePositions(howMany);
		}

		return futurePositions;
	}

	@Override
	public Vector3D getFuturePosition(int when)
	{
		if (futurePositions == null || futurePositions.length < when) {
			futurePositions = calculateFuturePositions(when);
		}

		return futurePositions[when - 1];
	}

	protected Vector3D[] calculateFuturePositions(int howMany)
	{
		return Geometry.getFuturePositions(position, getSpeed(), howMany);
	}

	/**
	 * @return the speed of this object in m/cycle
	 */
	@Override
	public Vector3D getSpeed()
	{
		return speed;
	}

	/**
	 * @return the previous speed of this object
	 */
	@Override
	public Vector3D getOldSpeed()
	{
		return oldSpeed;
	}

	@Override
	public void updateFromVision(Vector3D seenPosition, Vector3D localPosition, Vector3D globalPosition, float time)
	{
		super.updateFromVision(seenPosition, localPosition, globalPosition, time);
		calculateSpeed(time);
		futurePositions = null;
	}

	@Override
	public void updateFromAudio(Vector3D localPosition, Vector3D globalPosition, float time)
	{
		super.updateFromAudio(localPosition, globalPosition, time);
		futurePositions = null;
	}

	/**
	 * Calculate the speed this object is moving at
	 *
	 * @param time Current global time
	 */
	void calculateSpeed(float time)
	{
		if (time - timeAtLastSpeedMeasurement < cycleTime - 0.00001) {
			//			System.out.println("Updating speed of object with no time passed: " + this +
			//							   " timeDelta: " + (time - timeAtLastSpeedMeasurement));
			return;
		}
		oldSpeed = speed;
		speed = calculateSpeed(getPosition(), time, positionAtLastSpeedMeasurement, timeAtLastSpeedMeasurement);
		timeAtLastSpeedMeasurement = time;
		positionAtLastSpeedMeasurement = getPosition();
	}

	/**
	 * Calculate the speed this object is moving at
	 */
	Vector3D calculateSpeed(Vector3D newPosition, float newTime, Vector3D oldPosition, float oldTime)
	{
		// need previousPosition, hence speed is calculated after update
		Vector3D checkSpeed = oldSpeed;
		if (oldPosition != null) {
			checkSpeed = newPosition.subtract(oldPosition);
			float deltaCycles = Math.round((newTime - oldTime) / cycleTime);
			if (deltaCycles > 0.01f) {
				checkSpeed = checkSpeed.scalarMultiply(1 / deltaCycles);
			}

			// we don't set speed for impossible values
			if (checkSpeed.getNorm() > getPossibleSpeed()) {
				return oldSpeed;
			}
		}
		return checkSpeed;
	}

	@Override
	public boolean isMoving()
	{
		return speed.getNorm() > 0.01;
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof MovableObject)) {
			return false;
		}
		MovableObject other = (MovableObject) o;
		if (!super.equals(other)) {
			return false;
		}
		return FuzzyCompare.eq(speed, other.speed, 0.00001f);
	}

	@Override
	public void updateNoVision(float globalTime)
	{
		super.updateNoVision(position.add(speed), globalTime);

		futurePositions = null;
	}
}