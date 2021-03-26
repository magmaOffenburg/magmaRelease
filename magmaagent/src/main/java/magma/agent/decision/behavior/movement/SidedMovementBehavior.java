/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.movement;

import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

/**
 * A MovementBehavior which supports two versions - one for the left, one for
 * the right side.
 */
public class SidedMovementBehavior extends MovementBehavior
{
	public enum Side { LEFT, RIGHT }

	protected Side side;

	private String baseName;

	protected boolean flipSides;

	/**
	 * Creates a MovementBehavior that supports both a left and a right side.
	 * @param side which side
	 * @param baseName the name without "Left" or "Right"
	 * @param initialMovement the movement - assumed to the right version, will
	 *        be flipped if side == LEFT
	 */
	public SidedMovementBehavior(
			Side side, String baseName, IRoboCupThoughtModel thoughtModel, Movement initialMovement)
	{
		super(null, thoughtModel, initialMovement);
		this.side = side;
		this.baseName = baseName;

		name = baseName + (side == Side.LEFT ? "Left" : "Right");
		// the getLeftVersion() call is delayed so subclasses can still change
		// initialMovement
		flipSides = side == Side.LEFT;
	}

	@Override
	public void perform()
	{
		if (flipSides) {
			initialMovement = currentMovement = initialMovement.getLeftVersion();
			flipSides = false;
		}

		super.perform();
	}

	public String getBaseName()
	{
		return baseName;
	}
}
