/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.communication.action.IEffector;
import hso.autonomy.agent.communication.action.impl.HingeEffector;
import hso.autonomy.agent.communication.perception.IHingeJointPerceptor;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmeta.IHingeJointConfiguration;
import hso.autonomy.agent.model.agentmodel.IHingeJoint;
import hso.autonomy.agent.model.agentmodel.IHingeJointR;
import hso.autonomy.agent.model.agentmodel.ISensor;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.misc.FuzzyCompare;
import hso.autonomy.util.misc.ValueUtil;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * "HingeJoint" sensor implementation
 *
 * @author Stefan Glaser, Klaus Dorer
 *
 */
public class HingeJoint extends Sensor implements IHingeJoint
{
	/** The name of the effector corresponding to this hinge joint. */
	private String effectorName;

	// from sensors
	/** current angle of the joint */
	private float angle;

	// self calculated
	/** the original angle of the source body model */
	private float originalAngle;

	/** the last speed value of the joint */
	private float lastSpeed;

	/** a vector describing the rotation axis of the joint */
	private final Vector3D jointAxis;

	/** minimum axis position */
	private final float minAngle;

	/** maximum axis position */
	private final float maxAngle;

	/** maximum axis speed */
	private final float maxSpeed;

	/** the maximal amount of speed change (in degrees per cycle) */
	private float maxAcceleration;

	/** whether to move to initial position automatically if unused this cycle */
	private final boolean defaultToInitialPos;

	/** the rotation to this joint */
	private transient Rotation rotation;

	/** -1 if this joint is turning negated with respect to our standard model */
	private final float negateRotation;

	/** the acceleration in deg/cycle//cycle */
	private float acceleration;

	/** the angular speed we want to have at target angle (in deg / cycle) */
	private float speedAtDesiredAngle;

	/**
	 * the angular acceleration we want to have at target angle (in deg /
	 * cycle^2)
	 */
	private float accelerationAtDesiredAngle;

	/** whether the joint was moved this cycle */
	private boolean performed;

	/**
	 * Instantiates a new HingeJoint sensor
	 *
	 * @param name Sensor name
	 * @param effectorName Effector name linked to this sensor
	 * @param minAngle Minimum sensor value
	 * @param maxAngle Maximum sensor value
	 */
	HingeJoint(String name, String perceptorName, String effectorName, IPose3D pose, float minAngle, float maxAngle,
			float maxSpeed, float maxAcceleration, boolean defaultToInitialPos)
	{
		super(name, perceptorName, pose);
		this.effectorName = effectorName;
		this.angle = 0;
		this.originalAngle = 0;
		this.lastSpeed = 0;
		this.jointAxis = pose.getOrientation().applyTo(Vector3D.PLUS_K);
		this.minAngle = minAngle;
		this.maxAngle = maxAngle;
		this.maxSpeed = maxSpeed;
		this.maxAcceleration = maxAcceleration;
		this.defaultToInitialPos = defaultToInitialPos;
		acceleration = 0;
		performed = false;

		// HACK: to get Webots and 3Dsim to work
		double sum = jointAxis.getX() + jointAxis.getY() + jointAxis.getZ();
		if (sum < -0.9 && sum > -1.1) {
			negateRotation = -1;
		} else {
			negateRotation = 1;
		}
	}

	HingeJoint(String name, String perceptorName, String effectorName, Vector3D rotationAxis, float minAngle,
			float maxAngle, float maxSpeed, float maxAcceleration, boolean defaultToInitialPos)
	{
		this(name, perceptorName, effectorName, new Pose3D(Vector3D.ZERO, new Rotation(Vector3D.PLUS_K, rotationAxis)),
				minAngle, maxAngle, maxSpeed, maxAcceleration, defaultToInitialPos);
	}

	public HingeJoint(IHingeJointConfiguration jointConfig)
	{
		this(jointConfig.getName(), jointConfig.getPerceptorName(), jointConfig.getEffectorName(),
				jointConfig.getPose(), jointConfig.getMinAngle(), jointConfig.getMaxAngle(), jointConfig.getMaxSpeed(),
				jointConfig.getMaxAcceleration(), jointConfig.getDefaultToInitialPos());
	}

	/**
	 * Copy constructor
	 * @param source the object to copy from
	 */
	HingeJoint(HingeJoint source)
	{
		super(source);
		this.effectorName = source.effectorName;
		this.angle = source.angle;
		// we do not copy the original value but use current as new original
		this.originalAngle = angle;
		this.lastSpeed = source.lastSpeed;
		this.acceleration = source.acceleration;
		this.jointAxis = source.jointAxis;
		this.minAngle = source.minAngle;
		this.maxAngle = source.maxAngle;
		this.maxSpeed = source.maxSpeed;
		this.maxAcceleration = source.maxAcceleration;
		this.negateRotation = source.negateRotation;
		this.defaultToInitialPos = source.defaultToInitialPos;
	}

	@Override
	public float getAngle()
	{
		return angle;
	}

	@Override
	public float getMinAngle()
	{
		return minAngle;
	}

	@Override
	public float getMaxAngle()
	{
		return maxAngle;
	}

	/**
	 * Called to act by setting an angular speed
	 * @param speed the angular speed (in degrees per cycle)
	 */
	@Override
	public void performAxisSpeed(float speed)
	{
		performAxisSpeed(speed, maxSpeed);
	}

	@Override
	public void performAxisSpeed(float speed, float maxSpeed)
	{
		acceleration = speed - lastSpeed;
		if (Math.abs(acceleration) > maxAcceleration) {
			acceleration = ValueUtil.limitAbs(acceleration, maxAcceleration);
			speed = lastSpeed + acceleration;
		}

		float usedSpeed = ValueUtil.limitAbs(speed, maxSpeed);
		float newAx = originalAngle + usedSpeed;
		angle = ValueUtil.limitValue(newAx, minAngle, maxAngle);

		performed = true;

		// Reset rotation matrix
		rotation = null;
	}

	@Override
	public void adjustAxisPosition(double delta)
	{
		performAxisPosition(angle + delta, maxSpeed);
	}

	@Override
	public float performAxisPosition(double position)
	{
		return performAxisPosition(position, maxSpeed);
	}

	@Override
	public float performAxisPosition(double position, float maxSpeed)
	{
		float axisSpeed = (float) (position - originalAngle);

		performAxisSpeed(axisSpeed, maxSpeed);
		speedAtDesiredAngle = 0;
		accelerationAtDesiredAngle = 0;

		return axisSpeed;
	}

	@Override
	public void performInitialPosition()
	{
		performAxisPosition(0);
	}

	@Override
	public void resetMovement()
	{
		performAxisSpeed(0);
	}

	@Override
	public float getNextAxisSpeed()
	{
		return angle - originalAngle;
	}

	@Override
	public Rotation getRotation()
	{
		if (rotation == null) {
			rotation = new Rotation(jointAxis, Math.toRadians(angle), RotationConvention.VECTOR_OPERATOR);
		}
		return rotation;
	}

	@Override
	public Vector3D getTranslation()
	{
		return Vector3D.ZERO;
	}

	@Override
	public Vector3D getJointAxis()
	{
		return jointAxis;
	}

	@Override
	public IEffector generateJointAction()
	{
		if (defaultToInitialPos && !performed) {
			performInitialPosition();
		}

		HingeEffector effector = new HingeEffector(effectorName);

		float nextAxisSpeed = getNextAxisSpeed();
		effector.set(nextAxisSpeed, angle, speedAtDesiredAngle, accelerationAtDesiredAngle);

		lastSpeed = nextAxisSpeed;
		performed = false;

		return effector;
	}

	@Override
	public void updateFromPerception(IPerception perception)
	{
		IHingeJointPerceptor hingeJointPerceptor = perception.getHingeJointPerceptor(getPerceptorName());
		if (hingeJointPerceptor != null) {
			updateFromPerception(hingeJointPerceptor);
		}
	}

	void updateFromPerception(IHingeJointPerceptor hingeJointPerceptor)
	{
		// update hinge joint state
		float newAxis = hingeJointPerceptor.getAxis();
		if (Double.isNaN(newAxis)) {
			System.err.println("Received Nan value for joint: " + getName() + " skipping it...");
			return;
		}

		float axis = negateRotation * newAxis;
		float delta = angle - originalAngle;
		originalAngle = axis;
		angle = originalAngle + delta;

		// Reset rotation matrix
		rotation = null;
	}

	@Override
	public void updateNoPerception()
	{
		// in the case where we are connected to the perception this is done in
		// the copy constructor
		originalAngle = angle;
		super.updateNoPerception();
	}

	@Override
	public void updateJointPositionFromJoint(IHingeJointR joint)
	{
		angle = ((HingeJoint) joint).angle;

		// Reset rotation matrix
		rotation = null;
	}

	@Override
	public float getMaxSpeed()
	{
		return maxSpeed;
	}

	@Override
	public float getAcceleration()
	{
		return acceleration;
	}

	@Override
	public void setFutureValues(float desiredAngle, float speedAtDesiredAngle, float accelerationAtDesiredAngle)
	{
		performAxisPosition(desiredAngle);
		this.speedAtDesiredAngle = speedAtDesiredAngle;
		this.accelerationAtDesiredAngle = accelerationAtDesiredAngle;
	}

	public float getSpeedAtDesiredAngle()
	{
		return speedAtDesiredAngle;
	}

	public float getAccelerationAtDesiredAngle()
	{
		return accelerationAtDesiredAngle;
	}

	@Override
	public float getLastSpeed()
	{
		return lastSpeed;
	}

	@Override
	public ISensor copy()
	{
		return new HingeJoint(this);
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof HingeJoint)) {
			return false;
		}
		HingeJoint other = (HingeJoint) o;
		if (!super.equals(other)) {
			return false;
		}
		if (!FuzzyCompare.eq(angle, other.angle, 0.00001f)) {
			return false;
		}
		if (!FuzzyCompare.eq(minAngle, other.minAngle, 0.00001f)) {
			return false;
		}
		return FuzzyCompare.eq(maxAngle, other.maxAngle, 0.00001f);
	}

	@Override
	public String toString()
	{
		return super.toString() + "[axis=" + angle + "]";
	}
}
