/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package magma.agent.decision.behavior.complex.kick;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.decision.behavior.complex.SingleComplexBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IKick;
import magma.agent.decision.behavior.IKickDecider;
import magma.agent.decision.behavior.IKickMovement;
import magma.agent.decision.behavior.ikMovement.IKKickBehavior;

/**
 * Implements a behavior which performs kicking by dynamically balancing on one
 * leg and then statically kicking
 *
 * @author Klaus Dorer
 */
public class StabilizedKick extends SingleComplexBehavior implements IKick
{
	private final String stabilizeName;

	private final String kickName;

	private boolean isFinished;

	private transient IKickDecider kickDecider;

	public StabilizedKick(String name, IThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors,
			String stabilizeName, String kickName)
	{
		super(name, thoughtModel, behaviors, stabilizeName);
		this.stabilizeName = stabilizeName;
		this.kickName = kickName;
		isFinished = false;
		kickDecider = new StabilizedKickDecider(getStabilizeBehavior().getKickDecider(),
				getStabilizeBehavior().getStabilizeCycles(), getKickBehavior().getKickDecider());
	}

	@Override
	public IKickDecider getKickDecider()
	{
		return kickDecider;
	}

	private IKKickBehavior getStabilizeBehavior()
	{
		return (IKKickBehavior) behaviors.get(stabilizeName);
	}

	public IKickMovement getKickBehavior()
	{
		return (IKickMovement) behaviors.get(kickName);
	}

	@Override
	public IBehavior decideNextBasicBehavior()
	{
		IBehavior current = getCurrentBehavior();
		if (current == getStabilizeBehavior()) {
			if (current.isFinished()) {
				current = getKickBehavior();
			}
		} else {
			if (current.isFinished()) {
				current = getStabilizeBehavior();
			}
		}
		return current;
	}

	@Override
	public void init()
	{
		isFinished = false;
		super.init();
	}

	@Override
	public boolean isFinished()
	{
		IBehavior currentBehavior = getCurrentBehavior();
		return (currentBehavior == getKickBehavior() && currentBehavior.isFinished()) || isFinished;
	}

	/**
	 * To prevent switching from lower behaviors, we return <code>this</code>
	 * behavior here.
	 */
	@Override
	public IBehavior getRootBehavior()
	{
		return this;
	}
}