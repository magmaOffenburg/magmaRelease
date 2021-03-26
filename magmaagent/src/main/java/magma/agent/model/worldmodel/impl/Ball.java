/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel.impl;

import hso.autonomy.agent.model.worldmodel.impl.MovableObject;
import hso.autonomy.util.geometry.Geometry;
import magma.agent.model.worldmodel.IBall;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Represents the ball of the game
 */
public class Ball extends MovableObject implements IBall
{
	/** The radius of the ball in m */
	private final float radius;

	/** The decay of the ball speed */
	private final float ballDecay;

	/** The time when the z position of the ball was "in the air" */
	public Float lastAirTime = null;

	// at which distance we collide with the ball
	private float collisionDistance;

	public Ball(float radius, float ballDecay, Vector3D initialPosition, float collisionDistance, float cycleTime)
	{
		super("Ball", cycleTime);
		this.radius = radius;
		this.ballDecay = ballDecay;
		this.collisionDistance = collisionDistance;
		position = initialPosition;
		previousPosition = initialPosition;
	}

	@Override
	protected Vector3D[] calculateFuturePositions(int howMany)
	{
		return Geometry.getFuturePositions(position, getSpeed(), ballDecay, radius, howMany, cycleTime);
	}

	@Override
	public double getPossibleSpeed()
	{
		return 6.0;
	}

	@Override
	public void updateFromVision(Vector3D seenPosition, Vector3D localPosition, Vector3D globalPosition, float time)
	{
		if (globalPosition.getZ() >= 0.7) {
			lastAirTime = time;
		}

		super.updateFromVision(seenPosition, localPosition, globalPosition, time);
	}

	@Override
	public double getCollisionDistance()
	{
		return collisionDistance;
	}

	@Override
	public void updateNoVision(float globalTime)
	{
		super.updateNoVision(globalTime);
		oldSpeed = speed;
		speed = speed.scalarMultiply(ballDecay);
	}

	@Override
	public float getRadius()
	{
		return radius;
	}

	@Override
	public void resetPosition(Vector3D position)
	{
		this.position = position;
		speed = oldSpeed = Vector3D.ZERO;
		futurePositions = null;
		lastAirTime = null;
	}

	@Override
	public boolean isBouncing(float time)
	{
		return lastAirTime != null && time - lastAirTime < 5;
	}

	@Override
	public void updateFromAudio(Vector3D localPosition, Vector3D globalPosition, Vector3D speed, float time)
	{
		oldSpeed = this.speed;
		this.speed = speed;
		super.updateFromAudio(localPosition, globalPosition, time);
	}

	@Override
	public Vector3D getPosition()
	{
		return super.getPosition();
	}
}