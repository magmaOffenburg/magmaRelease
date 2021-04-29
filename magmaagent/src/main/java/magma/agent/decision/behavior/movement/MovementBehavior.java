/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.movement;

import magma.agent.decision.behavior.basic.RoboCupBehavior;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

/**
 * @author dorer
 */
public abstract class MovementBehavior extends RoboCupBehavior
{
	protected Movement initialMovement;

	/** true if this behavior is over */
	protected boolean isFinished;

	/** true if this behavior is performed the first time */
	protected boolean justStarted;

	/** the current movement of this behavior */
	protected Movement currentMovement;

	/** the relative speed this movement is performed in */
	private float relativeSpeed;

	public MovementBehavior(String name, IRoboCupThoughtModel thoughtModel, Movement initialMovement)
	{
		super(name, thoughtModel);
		this.initialMovement = initialMovement;

		isFinished = false;
		justStarted = true;
		currentMovement = initialMovement;
		relativeSpeed = 1;
	}

	protected void switchTo(Movement newMovement)
	{
		currentMovement = newMovement;
		currentMovement.init();
	}

	@Override
	public void perform()
	{
		super.perform();

		isFinished = false;
		justStarted = false;
		currentMovement.setRelativeSpeed(relativeSpeed);
		currentMovement.perform(getAgentModel());
		if (currentMovement.isFinished()) {
			Movement nextMovement = getNextMovement();
			if (nextMovement == null) {
				isFinished = true;
				nextMovement = initialMovement;
			}
			switchTo(nextMovement);
		}
	}

	protected Movement getNextMovement()
	{
		return null;
	}

	@Override
	public boolean isFinished()
	{
		return isFinished;
	}

	public boolean justStarted()
	{
		return justStarted;
	}

	@Override
	public void init()
	{
		super.init();
		isFinished = false;
		justStarted = true;
		switchTo(initialMovement);
	}

	public Movement getInitialMovement()
	{
		return initialMovement;
	}

	public void setRelativeSpeed(float speed)
	{
		this.relativeSpeed = speed;
	}
}
