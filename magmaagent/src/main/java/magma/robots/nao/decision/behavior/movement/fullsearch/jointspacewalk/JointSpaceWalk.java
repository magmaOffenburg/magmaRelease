/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.fullsearch.jointspacewalk;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Angle;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalk;
import magma.agent.decision.behavior.complex.RoboCupSingleComplexBehavior;
import magma.agent.decision.behavior.ikMovement.SwingArms;
import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;
import magma.agent.decision.behavior.movement.MovementBehavior;
import magma.agent.model.agentmodel.SupportFoot;
import magma.robots.nao.decision.behavior.movement.fullsearch.FullSearchMovementParameters.Joint;
import magma.robots.nao.decision.behavior.movement.fullsearch.jointspacewalk.JointSpaceWalkParameters.CustomParam;
import magma.robots.nao.decision.behavior.movement.fullsearch.jointspacewalk.JointSpaceWalkParameters.WalkDirection;

public class JointSpaceWalk extends RoboCupSingleComplexBehavior
{
	/** for how long the init phase should last */
	private static final int INIT_CYCLES = 80;

	/** for how long the joint space walk phase should last before switching back to the old walk engine */
	private static final int WALK_CYCLES = 300;

	private static final int MAX_SWITCH_WAIT_CYCLES = 70;

	private static final boolean SWING_ARMS = false;

	private enum Phase { INIT, WALK, MORPH, OLD_WALK }

	public enum WalkEngine { NEW, OLD, OLD_HECTIC }

	private final MovementBehavior walkLeft;

	private final MovementBehavior walkRight;

	private final IBehavior morph;

	private final SwingArms swingArms;

	private final JointSpaceWalkParameters params;

	private Phase phase;

	private boolean left;

	private int elapsedCycles;

	private WalkEngine walkEngine;

	private boolean morphingEnabled = true;

	public JointSpaceWalk(IThoughtModel thoughtModel, BehaviorMap behaviors, ParameterMap params)
	{
		super(IBehaviorConstants.JOINT_SPACE_WALK, thoughtModel, behaviors, IBehaviorConstants.WALK);
		walkLeft = (MovementBehavior) behaviors.get(IBehaviorConstants.JOINT_SPACE_WALK_MOVEMENT.LEFT);
		walkRight = (MovementBehavior) behaviors.get(IBehaviorConstants.JOINT_SPACE_WALK_MOVEMENT.RIGHT);
		morph = behaviors.get(IBehaviorConstants.JOINT_SPACE_WALK_MORPH);
		swingArms = (SwingArms) behaviors.get(IBehaviorConstants.SWING_ARMS);
		this.params = (JointSpaceWalkParameters) params.get(IBehaviorConstants.JOINT_SPACE_WALK_MOVEMENT.BASE_NAME);
		phase = this.params.hasInitialWalk() ? Phase.INIT : Phase.WALK;
		left = false;
		walkEngine = WalkEngine.NEW;
	}

	@Override
	public void init()
	{
		super.init();
		phase = Phase.INIT;
		left = false;
		elapsedCycles = 0;
	}

	@Override
	protected IBehavior decideNextBasicBehavior()
	{
		elapsedCycles++;
		getThoughtModel().getRoboVizDraw().drawAgentAnnotation(phase);

		if (walkEngine != WalkEngine.NEW) {
			return oldWalk();
		}

		switch (phase) {
		case INIT:
			if (elapsedCycles >= INIT_CYCLES) {
				boolean switchToWalk = getAgentModel().getStepFoot(5, 1) == SupportFoot.LEFT ||
									   elapsedCycles >= INIT_CYCLES + MAX_SWITCH_WAIT_CYCLES;
				if (switchToWalk) {
					getCurrentBehavior().abort();
					phase = Phase.WALK;
					return decideNextBasicBehavior();
				}
			}
			return oldWalk();

		case WALK:
			if (morphingEnabled && elapsedCycles >= WALK_CYCLES) {
				boolean shouldSwitch = getAgentModel().getStepFoot(5, 1) == SupportFoot.LEFT ||
									   elapsedCycles >= WALK_CYCLES + MAX_SWITCH_WAIT_CYCLES;
				if (shouldSwitch) {
					getCurrentBehavior().abort();
					phase = Phase.MORPH;
					return decideNextBasicBehavior();
				}
			}

			boolean hasLearnedArmMovement = params.isActive(Joint.LSP);
			if (!hasLearnedArmMovement && SWING_ARMS) {
				swingArms.perform(left ? SupportFoot.LEFT : SupportFoot.RIGHT, 7);
			}

			if (getCurrentBehavior().isFinished() && getDirection() != WalkDirection.TURN_NO_MIRROR) {
				left = !left;
			}
			return left ? walkLeft : walkRight;

		case MORPH:
			if (morph.isFinished()) {
				phase = Phase.OLD_WALK;
				return oldWalk();
			}
			return morph;

		case OLD_WALK:
			return oldWalk();
		}

		return null;
	}

	private IWalk oldWalk()
	{
		IWalk behavior = (IWalk) behaviors.get(IBehaviorConstants.WALK);
		double initialWalkSpeed = params.get(CustomParam.INITIAL_WALK_SPEED);
		double forwardsBackwards = 0;
		double stepLeftRight = 0;
		Angle turnLeftRight = Angle.ZERO;

		switch (params.getDirection()) {
		case FORWARD:
			forwardsBackwards = initialWalkSpeed;
			break;
		case BACKWARD:
			forwardsBackwards = -initialWalkSpeed;
			break;
		case SIDEWARD:
			stepLeftRight = initialWalkSpeed;
			break;
		case TURN:
		case TURN_NO_MIRROR:
			turnLeftRight = Angle.deg(initialWalkSpeed);
			break;
		}

		String params = IKDynamicWalkMovement.NAME_STABLE;
		behavior.walk(forwardsBackwards, stepLeftRight, turnLeftRight, params);
		return behavior;
	}

	public WalkDirection getDirection()
	{
		return params.getDirection();
	}

	public boolean hasFinishedInitialization()
	{
		return phase != Phase.INIT;
	}

	public void setWalkEngine(WalkEngine walkEngine)
	{
		this.walkEngine = walkEngine;
	}

	public void setMorphingEnabled(boolean morphingEnabled)
	{
		this.morphingEnabled = morphingEnabled;
	}

	public void skipInit()
	{
		if (phase == Phase.INIT) {
			phase = Phase.WALK;
		}
	}
}
