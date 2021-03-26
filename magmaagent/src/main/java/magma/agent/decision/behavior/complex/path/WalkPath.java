/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package magma.agent.decision.behavior.complex.path;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.decision.behavior.complex.SingleComplexBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Pose2D;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBaseWalk;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.ikMovement.IKStepPlanBehavior;
import magma.agent.model.agentmodel.SupportFoot;

/**
 * Implements a behavior which performs bi-pedal walking based on the "Balance"
 * behavior.
 *
 * @author Stefan Grossmann
 */
public class WalkPath extends SingleComplexBehavior
{
	public WalkPath(
			String name, IThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors, IBaseWalk baseWalk)
	{
		super(name, thoughtModel, behaviors, baseWalk.getName());
	}

	public WalkPath(IThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors, IBaseWalk baseWalk)
	{
		this(IBehaviorConstants.WALK_PATH, thoughtModel, params, behaviors, baseWalk);
	}

	/**
	 * set the parameter for the IKStepPlanBehavior
	 */
	public void step(Path currPath, Pose2D footTarget, SupportFoot supportFoot)
	{
		IKStepPlanBehavior base = (IKStepPlanBehavior) getCurrentBehavior();
		base.setStepTarget(currPath, footTarget, supportFoot);
	}

	@Override
	public IBehavior decideNextBasicBehavior()
	{
		return getCurrentBehavior();
	}
}