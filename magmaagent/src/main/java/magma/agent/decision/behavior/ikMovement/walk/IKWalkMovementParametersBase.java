/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement.walk;

import hso.autonomy.util.geometry.Angle;
import kdo.util.parameter.EnumParameterList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents the parameter set necessary for walking with walk movements
 *
 * @author Stefan Glaser
 */
@SuppressWarnings("Duplicates")
public class IKWalkMovementParametersBase extends EnumParameterList<IKWalkMovementParametersBase.Param>
{
	public enum Param {
		/** number of cycles per step */
		CYCLE_PER_STEP,
		/** walking height */
		WALK_HEIGHT,
		/** static x-offset (aka "John Wayne" parameter) */
		WALK_WIDTH,
		/** static y-offset (forward/backward shift of the movement center) */
		WALK_OFFSET,
		/** maximum forward (y) step size */
		MAX_STEP_LENGTH,
		/** maximum sideward (x) step size */
		MAX_STEP_WIDTH,
		/** maximum step height */
		MAX_STEP_HEIGHT,
		/** maximum turn angle per step */
		MAX_TURN_ANGLE,
		/**
		 * push down factor for the supporting foot (by which fraction of the step height should
		 * the target-height of the support foot be extended towards the end of a step)
		 */
		PUSHDOWN_FACTOR,
		/** the permanent slant (x) angle of the feet */
		FOOT_SLANT_ANGLE,
		/** maximum forward leaning (degrees) (+/-100% forward speed --> +/-100% forward leaning) */
		MAX_FORWARD_LEANING,
		/** maximum sidewards leaning (degrees) (+/-100% sidewards speed --> +/-100% sidewards leaning) */
		MAX_SIDEWARDS_LEANING,
		/** maximum step parameter acceleration per cycle */
		ACCELERATION,
		/** maximum step parameter deceleration per cycle */
		DECELERATION,
		/** maximum turn acceleration per cycle */
		TURN_ACCELERATION,
		/** maximum turn deceleration per cycle */
		TURN_DECELERATION,
		/** true if the agent uses arms for walking */
		SWING_ARMS,
		/** true if the agent will use dynamic balancing */
		DYNAMIC_WALK,
		/** the adjustment factor for the balancing engine in saggital direction */
		SAGGITAL_ADJUSTMENT_FACTOR,
		/** the maximum allowed absolute adjustment value in saggital direction */
		MAX_ABS_SAGGITAL_ADJUSTMENT,
		/** the adjustment factor for the balancing engine in coronal direction */
		CORONAL_ADJUSTMENT_FACTOR,
		/** the maximum allowed absolute adjustment value in coronal direction */
		MAX_ABS_CORONAL_ADJUSTMENT
	}

	public IKWalkMovementParametersBase()
	{
		this("");
	}

	public IKWalkMovementParametersBase(String name)
	{
		super(Param.class);
		setValues(name);
	}

	/**
	 * Set the parameters to a predefined parameter value set.
	 *
	 * @param name - the name of the predefined value set
	 */
	public void setValues(String name)
	{
		put(Param.CYCLE_PER_STEP, 14.0f);
		put(Param.WALK_HEIGHT, -0.25f);
		put(Param.WALK_WIDTH, 0.06f);
		put(Param.WALK_OFFSET, 0.0f);
		put(Param.MAX_STEP_LENGTH, 0.07f);
		put(Param.MAX_STEP_WIDTH, 0.07f);
		put(Param.MAX_STEP_HEIGHT, 0.025f);
		put(Param.MAX_TURN_ANGLE, 50.0f);
		put(Param.PUSHDOWN_FACTOR, 0.2f);
		put(Param.FOOT_SLANT_ANGLE, 0.0f);
		put(Param.MAX_FORWARD_LEANING, 0.0f);
		put(Param.MAX_SIDEWARDS_LEANING, 0.0f);
		put(Param.ACCELERATION, 0.0035f);
		put(Param.DECELERATION, 0.0035f);
		put(Param.TURN_ACCELERATION, 2f);
		put(Param.TURN_DECELERATION, 3f);
		put(Param.SAGGITAL_ADJUSTMENT_FACTOR, 0.6f);
		put(Param.CORONAL_ADJUSTMENT_FACTOR, 0.6f);
		put(Param.SWING_ARMS, 1.0f);
		put(Param.DYNAMIC_WALK, 1.0f);

		put(Param.MAX_ABS_SAGGITAL_ADJUSTMENT, 100.0f);
		put(Param.MAX_ABS_CORONAL_ADJUSTMENT, 100.0f);
	}

	/**
	 * @return number of cycles per step
	 */
	public double getCyclesPerStep()
	{
		return get(Param.CYCLE_PER_STEP);
	}

	/**
	 * @return walking height
	 */
	public double getWalkHeight()
	{
		return get(Param.WALK_HEIGHT);
	}

	/**
	 * @return static x-offset (aka "John Wayne" parameter)
	 */
	public double getWalkWidth()
	{
		return get(Param.WALK_WIDTH);
	}

	/**
	 * @return static y-offset (forward/backward shift of the movement center)
	 */
	public double getWalkOffset()
	{
		return get(Param.WALK_OFFSET);
	}

	/**
	 * maximum forward (y) step size
	 *
	 * @return maximum step length (step size in y direction)
	 */
	public double getMaxStepLength()
	{
		return get(Param.MAX_STEP_LENGTH);
	}

	/**
	 * maximum sideward (x) step size
	 *
	 * @return maximum step length (step size in x direction)
	 */
	public double getMaxStepWidth()
	{
		return get(Param.MAX_STEP_WIDTH);
	}

	/**
	 * @return maximum step height
	 */
	public double getMaxStepHeight()
	{
		return get(Param.MAX_STEP_HEIGHT);
	}

	/**
	 * @return maximum turn angle per step
	 */
	public Angle getMaxTurnAngle()
	{
		return Angle.deg(get(Param.MAX_TURN_ANGLE));
	}

	/**
	 * push down factor for the supporting foot (by which fraction of the step
	 * height should the target-height of the support foot be extended towards
	 * the end of a step)
	 *
	 * @return push down factor
	 */
	public double getPushDownFactor()
	{
		return get(Param.PUSHDOWN_FACTOR);
	}

	/**
	 * @return the permanent slant (x) angle of the feet
	 */
	public double getFootSlantAngle()
	{
		return get(Param.FOOT_SLANT_ANGLE);
	}

	/**
	 * maximum forward leaning (degrees) (+/-100% forward speed --> +/-100%
	 * forward leaning)
	 *
	 * @return maximum forward leaning (degrees)
	 */
	public double getMaxForwardLeaning()
	{
		return get(Param.MAX_FORWARD_LEANING);
	}

	/**
	 * maximum sidewards leaning (degrees) (+/-100% sidewards speed --> +/-100%
	 * sidewards leaning)
	 *
	 * @return maximum sidewards leaning (degrees)
	 */
	public double getMaxSidewardsLeaning()
	{
		return get(Param.MAX_SIDEWARDS_LEANING);
	}

	/**
	 * @return maximum step parameter acceleration per cycle
	 */
	public double getAcceleration()
	{
		return get(Param.ACCELERATION);
	}

	/**
	 * @return maximum step parameter deceleration per cycle
	 */
	public double getDeceleration()
	{
		return get(Param.DECELERATION);
	}

	/**
	 * @return maximum step parameter acceleration to side per cycle
	 */
	public double getSideAcceleration()
	{
		return getAcceleration() * 0.6;
	}

	/**
	 * @return maximum step parameter deceleration to side per cycle
	 */
	public double getSideDeceleration()
	{
		return getDeceleration() * 0.6;
	}

	/**
	 * @return the turn acceleration in degrees per cycle
	 */
	public double getTurnAcceleration()
	{
		return get(Param.TURN_ACCELERATION);
	}

	/**
	 * @return the turn acceleration in degrees per cycle
	 */
	public double getTurnDeceleration()
	{
		return get(Param.TURN_DECELERATION);
	}

	/**
	 * @return the turn acceleration in degrees per cycle
	 */
	public boolean getSwingArms()
	{
		return get(Param.SWING_ARMS) > 0.5f;
	}

	/**
	 * @return the turn acceleration in degrees per cycle
	 */
	public boolean getDynamicWalk()
	{
		return get(Param.DYNAMIC_WALK) > 0.5f;
	}

	/**
	 * @return the adjustment factor for the balancing engine in saggital
	 *         direction
	 */
	public float getSagittalAdjustmentFactor()
	{
		return get(Param.SAGGITAL_ADJUSTMENT_FACTOR);
	}

	/**
	 * @return the maximum allowed absolute adjustment value in saggital
	 *         direction
	 */
	public float getMaxAbsSagittalAdjustment()
	{
		return get(Param.MAX_ABS_SAGGITAL_ADJUSTMENT);
	}

	/**
	 * @return the adjustment factor for the balancing engine in coronal
	 *         direction
	 */
	public float getCoronalAdjustmentFactor()
	{
		return get(Param.CORONAL_ADJUSTMENT_FACTOR);
	}

	/**
	 * @return the maximum allowed absolute adjustment value in coronal direction
	 */
	public float getMaxAbsCoronalAdjustment()
	{
		return get(Param.MAX_ABS_CORONAL_ADJUSTMENT);
	}

	/**
	 * @return the adjustment factors for the balancing engine in {saggital,
	 *         coronal} direction
	 */
	public Vector2D getAdjustmentFactors()
	{
		return new Vector2D(getSagittalAdjustmentFactor(), getCoronalAdjustmentFactor());
	}
}