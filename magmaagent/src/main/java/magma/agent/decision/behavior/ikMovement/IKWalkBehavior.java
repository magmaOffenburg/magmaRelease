/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import static hso.autonomy.util.misc.ValueUtil.adjustValue;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.VectorUtils;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBaseWalk;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalkBehaviorMarker;
import magma.agent.decision.behavior.complex.kick.StabilizedKick;
import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKStaticWalkMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author Stefan Glaser
 */
public class IKWalkBehavior extends IKMovementBehaviorBase implements IBaseWalk
{
	private final SwingArms swingArms;

	protected final IKStaticWalkMovement walkMovement;

	/** how much we intend to walk forward / sideward (factors from 0 to 1) */
	protected Vector2D intendedWalk = Vector2D.ZERO;

	/** how much we intend to turn */
	protected Angle intendedTurn;

	/** how much we currently actually turn */
	protected Angle currentTurn;

	protected Step intendedStep;

	protected String paramSetName;

	private Vector2D realSpeed;

	public IKWalkBehavior(IRoboCupThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors)
	{
		this(IBehaviorConstants.IK_WALK, thoughtModel, params, behaviors);
	}

	public IKWalkBehavior(String name, IRoboCupThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors)
	{
		super(name, thoughtModel);
		swingArms = (SwingArms) behaviors.get(IBehaviorConstants.SWING_ARMS);
		walkMovement = new IKDynamicWalkMovement(thoughtModel, (IKWalkMovementParametersBase) params.get(name));
		intendedStep = new Step();
		paramSetName = IKDynamicWalkMovement.NAME_STABLE;
	}

	@Override
	public void setMovement(double forward, double sideward, Angle turn)
	{
		setMovement(forward, sideward, turn, IKDynamicWalkMovement.NAME_STABLE);
	}

	@Override
	public void setMovement(double forward, double sideward, Angle turn, String paramSetName)
	{
		intendedWalk = new Vector2D(forward / 100, sideward / 100);
		this.paramSetName = paramSetName;
		this.intendedTurn = turn;
	}

	@Override
	public void perform()
	{
		perform(calculateStep(intendedStep, intendedWalk, intendedTurn, paramSetName));
	}

	protected void perform(Step step)
	{
		intendedStep = step;
		super.perform();
		if (walkMovement.getWalkParameters().getSwingArms()) {
			swingArms.perform(walkMovement.getSupportFoot(), 4);
		}

		// for cycles < 6 we do not achieve the desired step length
		int cycles = Math.max(6, currentMovement.getMovementCycles());
		realSpeed = new Vector2D(2 * step.forward / cycles, 2 * 0.569274541 * -step.sideward / cycles);
		currentTurn = step.turn;
		getWorldModel().getThisPlayer().setIntendedGlobalSpeed(VectorUtils.to3D(realSpeed));
	}

	/**
	 * Calculates the step based on the passed parameters, intended speed and turn angle.
	 *
	 * @param step the parameters on which to base calculations
	 * @param speed forward (x) and sideward (y) speed from 0 to 1 (in local coordinates)
	 * @param targetTurn the desired turn angle
	 * @return the new step parameters
	 */
	public Step calculateStep(Step step, Vector2D speed, Angle targetTurn, String paramSetName)
	{
		IKWalkMovementParametersBase p = walkMovement.getWalkParameters();
		// KDO: comment in case of learning (find a better way to switch this off during learning)
		p.setValues(paramSetName);

		if (speed.getNorm() > 1) {
			speed = speed.normalize();
		}

		double targetStepWidth = p.getMaxStepWidth() * -speed.getY();
		double targetStepLength = p.getMaxStepLength() * speed.getX();

		double sideward = adjustValue(step.sideward, targetStepWidth, p.getSideAcceleration(), p.getSideDeceleration());
		double forward = adjustValue(step.forward, targetStepLength, p.getAcceleration(), p.getDeceleration());
		double upward = p.getMaxStepHeight();
		Angle turn = Angle.deg(adjustValue(
				step.turn.degrees(), targetTurn.degrees(), p.getTurnAcceleration(), p.getTurnDeceleration()));
		return new Step(sideward, forward, upward, turn);
	}

	protected void adjustIntendedStepParameters()
	{
		Vector3D currentLeaning = getWorldModel().getThisPlayer().getOrientation().applyTo(Vector3D.PLUS_K);

		if (currentLeaning.getZ() < 0.7) {
			// Clear actual movement if current leaning is too high (around 45 degrees)
			intendedStep.sideward = 0;
			intendedStep.forward = 0;
			intendedStep.turn = Angle.ZERO;
		} else if (currentLeaning.getZ() < 0.9) {
			// Decelerate actual movement if current leaning is too high (around 25 degrees)
			if (currentLeaning.getY() > 0.1) {
				// no backward if leaning forward
				intendedStep.forward = Math.max(0, intendedStep.forward);
			} else if (currentLeaning.getY() < -0.1) {
				// no forward if leaning backward
				intendedStep.forward = Math.min(0, intendedStep.forward);
			}
		}
	}

	@Override
	protected IIKMovement decideNextMovement()
	{
		adjustIntendedStepParameters();
		return walkMovement.setNextStep(intendedStep);
	}

	@Override
	public void init()
	{
		super.init();
		currentMovement = null;
	}

	/**
	 * Calculates the 2D pose of the free foot resulting from the application of
	 * the given step parameters, if the robot stands on the given support foot
	 * and the support foot is the the specified pose.
	 *
	 * @param supportFootPose the global pose of the support foot
	 * @param supportFoot the support foot of the step
	 * @param step the planned step parameters
	 * @return the resulting global pose of the free foot
	 */
	public Pose2D calculateFreeFootPose(Pose2D supportFootPose, SupportFoot supportFoot, Step step)
	{
		Pose2D relativeFreeFootPose = walkMovement.calculateRelativeFreeFootPose(step, supportFoot);
		return supportFootPose.applyTo(relativeFreeFootPose);
	}

	@Override
	public boolean isFinished()
	{
		return currentMovement == null || currentMovement.isFinished();
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		intendedStep = new Step();

		IBehavior realBehavior = actualBehavior.getRootBehavior();

		// Switching case to ourself
		if (realBehavior == this) {
			return this;
		}

		if (realBehavior instanceof StabilizedKick) {
			if (realBehavior.isFinished()) {
				SupportFoot kickingFoot = ((StabilizedKick) realBehavior).getKickDecider().getKickingFoot();
				walkMovement.setSupportFoot(kickingFoot == SupportFoot.LEFT ? SupportFoot.RIGHT : SupportFoot.LEFT);
				actualBehavior.onLeavingBehavior(this);
				return this;
			} else {
				return actualBehavior;
			}
		}

		if (realBehavior instanceof IKKickBehavior) {
			if (realBehavior.isFinished()) {
				walkMovement.setSupportFoot(((IKKickBehavior) realBehavior).getCurrentMovement().getNextSupportFoot());
				actualBehavior.onLeavingBehavior(this);
				return this;
			} else {
				return actualBehavior;
			}
		}

		if (realBehavior instanceof IWalkBehaviorMarker) {
			walkMovement.setSupportFoot(SupportFoot.LEFT);
			IKWalkMovementParametersBase p = walkMovement.getWalkParameters();
			// don't accelerate, use full steps right away
			intendedStep = new Step(0, p.getMaxStepLength(), p.getMaxStepHeight(), Angle.ZERO);
			return this;
		}

		return super.switchFrom(actualBehavior);
	}

	public void setSupportFoot(SupportFoot supportFoot)
	{
		walkMovement.setSupportFoot(supportFoot);
	}

	public void setParameter(IKWalkMovementParametersBase.Param which, float value)
	{
		walkMovement.getWalkParameters().put(which, value);
	}

	@Override
	public Angle getMaxTurnAngle()
	{
		return walkMovement.getWalkParameters().getMaxTurnAngle();
	}

	@Override
	public Vector2D getIntendedWalk()
	{
		return intendedWalk;
	}

	@Override
	public Vector2D getCurrentSpeed()
	{
		return realSpeed;
	}

	@Override
	public Angle getIntendedTurn()
	{
		return intendedTurn;
	}

	@Override
	public Angle getCurrentTurn()
	{
		return currentTurn;
	}

	@Override
	public boolean isNewStep()
	{
		return newMovementStarted;
	}
}
