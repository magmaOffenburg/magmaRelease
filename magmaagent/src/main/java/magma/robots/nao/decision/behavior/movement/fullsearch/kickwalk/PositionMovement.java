/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk;

import java.io.Serializable;
import magma.agent.decision.behavior.movement.Movement;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class PositionMovement implements Serializable
{
	private Vector3D ballPos;

	private Movement movement;

	public PositionMovement(Vector3D ballPos, Movement movement)
	{
		super();
		this.ballPos = ballPos;
		this.movement = movement;
	}

	public Vector3D getBallPos()
	{
		return ballPos;
	}

	public Movement getMovement()
	{
		return movement;
	}

	public void setMovement(Movement movement)
	{
		this.movement = movement;
	}

	public void setBallPosition(Vector3D position)
	{
		this.ballPos = position;
	}
}