/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.supportPoint;

import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.agentmodel.IHingeJoint;
import hso.autonomy.util.function.IFunction;
import hso.autonomy.util.function.PiecewiseLinearFunction;
import hso.autonomy.util.function.SupportPointFunction;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Single motor controller for a motor involved in a behavior
 *
 * @author Klaus Dorer
 */
public class MotorManager
{
	/** link to the corresponding agentModel */
	private final IAgentModel agentModel;

	/** name of the corresponding joint */
	private final String jointName;

	/** information about the angles over time the joints should have */
	protected IFunction motorFunction;

	/** function to store actual values of the motor if switched on */
	protected PiecewiseLinearFunction logFunction;

	public MotorManager(IAgentModel agentModel, String jointName, IFunction motorFunction)
	{
		this.agentModel = agentModel;
		this.jointName = jointName;
		this.motorFunction = motorFunction;
		logFunction = null;
	}

	/**
	 * Returns the joint angle (in degrees) where we want to have the joint with
	 * passed name at the passed time step
	 * @param step the time step at which to calculate the angle
	 * @return the joint angle (in degrees) where we want to have the joint with
	 */
	protected float getDesiredAngle(float step)
	{
		return (float) motorFunction.value(step);
	}

	protected float getSpeedAtDesiredAngle(float step)
	{
		return (float) motorFunction.derivative(step);
	}

	protected float getAccelerationAtDesiredAngle(float step)
	{
		return (float) motorFunction.derivative2(step);
	}

	/**
	 * @return the hinge joint to which this manager belongs
	 */
	public IHingeJoint getHingeJoint()
	{
		return agentModel.getWriteableHJ(jointName);
	}

	/**
	 * @return the name of the joint involved
	 */
	public String getName()
	{
		return jointName;
	}

	public IFunction getMotorFunction()
	{
		return motorFunction;
	}

	/**
	 * Adds a log point to the function
	 */
	public void setLogPoint(float x, float y)
	{
		if (logFunction == null) {
			logFunction = new PiecewiseLinearFunction();
		}

		logFunction.addSupportPoint(x, y);
	}

	/**
	 * Changes the coordinates of a support point. Does not allow to be moved
	 * left or right of neighboring support points.
	 * @param index the index of the point to change.
	 * @param x the new x coordinate
	 * @param y the new y coordinate
	 * @return the old coordinates of the point
	 */
	public Vector2D changePointCoordinates(int index, double x, double y)
	{
		IHingeJoint joint = getHingeJoint();
		if (y > joint.getMaxAngle()) {
			y = joint.getMaxAngle();
		} else if (y < joint.getMinAngle()) {
			y = joint.getMinAngle();
		}
		return motorFunction.moveSupportPointTo(index, (float) x, (float) y);
	}

	/**
	 * Changes the coordinates of a support point. Does not allow to be moved
	 * left or right of neighboring support points.
	 *
	 * @param index The index of the point to change.
	 * @param deltaX The change as a delta
	 * @return the old coordinates of the point. Index 0 is the old x coordinate,
	 *         index 1 the old y coordinate
	 */
	public Vector2D shiftPointCoordinates(int index, double deltaX)
	{
		SupportPointFunction function = (SupportPointFunction) motorFunction;

		float x = function.getSupportPoint(index).getX() + (float) deltaX;
		float y = function.getSupportPoint(index).getY();

		return function.moveSupportPointTo(index, x, y);
	}

	/**
	 * Checks if the function has a support point in the specified area
	 * @param right border (joint coordinates)
	 * @param left border (joint coordinates)
	 * @return true if at least one support point is inside the specified x area
	 */
	public boolean hasSupportPointInArea(double left, double right)
	{
		SupportPointFunction function = (SupportPointFunction) motorFunction;
		return function.hasSupportPointInArea(left, right);
	}

	@Override
	public String toString()
	{
		return motorFunction.toCSVString();
	}
}
