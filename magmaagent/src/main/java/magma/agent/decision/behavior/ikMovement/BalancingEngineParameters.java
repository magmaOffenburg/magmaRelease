/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * This class represents a parameter set for the {@link BalancingEngine}. It
 * encapsulates all relevant information needed by the {@link BalancingEngine}
 * to perform its adjustment.
 *
 * @author Stefan Glaser
 */
public class BalancingEngineParameters implements IBalancingEngineParameters
{
	/**
	 * The intended leaning vector the balancing engine is adjusting to.
	 */
	protected Vector3D intendedLeaningVector;

	/**
	 * The pivot point of the adjustment. This position is relative to the torso
	 * system and describes around which local point the target poses are rotated
	 * by the adjustment of the {@link BalancingEngine}.<br>
	 * In the general case, this position is defined as the current estimation of
	 * the Center of Mass of the robot. By using the current CoM estimation, the
	 * target poses are also automatically adjusted to CoM shifts, forcing the
	 * origin of the motion system to match the local CoM position.<br>
	 * However, by supplying a static local pivot point, e.g. in the pelvis of
	 * the robot, a behavior can command the {@link BalancingEngine} to produce a
	 * static adjustment.
	 */
	protected Vector3D pivotPoint;

	/**
	 * The adjustment factor for the sagittal (x-) axis, in a range from 0 to 1
	 * for 0% to 100% adjustment to the intended leaning in forward/backward
	 * direction.
	 */
	protected float saggitalAdjustmentFactor;

	/**
	 * The maximum absolute allowed adjustment angle for the sagittal (x-) axis
	 * in degrees.
	 */
	protected float maxAbsSagittalAdjustment;

	/**
	 * The adjustment factor for the coronal (y-) axis, in a range from 0 to 1
	 * for 0% to 100% adjustment to the intended leaning in sideways (left/right)
	 * direction.
	 */
	protected float coronalAdjustmentFactor;

	/**
	 * The maximum absolute allowed adjustment angle for the coronal (y-) axis in
	 * degrees.
	 */
	protected float maxAbsCoronalAdjustment;

	/**
	 * The default constructor defines a full static adjustment with a zero
	 * vector as pivot point. This parameter-set will cancel out the effects of
	 * the {@link BalancingEngine} completely.
	 */
	protected BalancingEngineParameters()
	{
		this(Vector3D.PLUS_K, Vector3D.ZERO, 1, 360, 1, 360);
	}

	public BalancingEngineParameters(Vector3D intendedLeaningVector, Vector3D pivotPoint,
			float sagittalAdjustmentFactor, float maxAbsSagittalAdjustment, float coronalAdjustmentFactor,
			float maxAbsCoronalAdjustment)
	{
		this.intendedLeaningVector = intendedLeaningVector;
		this.pivotPoint = pivotPoint;
		this.saggitalAdjustmentFactor = sagittalAdjustmentFactor;
		this.maxAbsSagittalAdjustment = maxAbsSagittalAdjustment;
		this.coronalAdjustmentFactor = coronalAdjustmentFactor;
		this.maxAbsCoronalAdjustment = maxAbsCoronalAdjustment;
	}

	@Override
	public Vector3D getIntendedLeaningVector()
	{
		return intendedLeaningVector;
	}

	@Override
	public Vector3D getPivotPoint()
	{
		return pivotPoint;
	}

	@Override
	public float getSaggitalAdjustmentFactor()
	{
		return saggitalAdjustmentFactor;
	}

	@Override
	public float getMaxAbsSaggitalAdjustment()
	{
		return maxAbsSagittalAdjustment;
	}

	@Override
	public float getCoronalAdjustmentFactor()
	{
		return coronalAdjustmentFactor;
	}

	@Override
	public float getMaxAbsCoronalAdjustment()
	{
		return maxAbsCoronalAdjustment;
	}
}