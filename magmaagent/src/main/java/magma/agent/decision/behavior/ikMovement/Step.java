/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import hso.autonomy.util.geometry.Angle;
import java.io.Serializable;

/**
 * @author Stefan Glaser
 */
public class Step implements Serializable
{
	/** amplitude of the side step function (how much we want to walk sideward) */
	public double sideward;

	/** amplitude of the forward function (how much we want to walk forward) */
	public double forward;

	/** amplitude of the up function (how much we want to lift the leg) */
	public double upward;

	/** how much we're currently turning */
	public Angle turn;

	public Step()
	{
		this.sideward = 0;
		this.forward = 0;
		this.upward = 0;
		this.turn = Angle.ZERO;
	}

	public Step(double sideward, double forward, double upward, Angle turn)
	{
		this.sideward = sideward;
		this.forward = forward;
		this.upward = upward;
		this.turn = turn;
	}

	/**
	 * @return a copy of this instance
	 */
	public Step copy()
	{
		return new Step(sideward, forward, upward, turn);
	}

	@Override
	public String toString()
	{
		return "f/b: " + String.format("%.4f", forward) + ", l/r: " + String.format("%.4f", sideward) +
				", height: " + String.format("%.4f", upward) + ", turn l/r: " + String.format("%.4f", turn.degrees());
	}
}
