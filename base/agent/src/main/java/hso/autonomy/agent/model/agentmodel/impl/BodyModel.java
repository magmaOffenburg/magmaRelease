/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.communication.action.IAction;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import hso.autonomy.agent.model.agentmodel.IActuator;
import hso.autonomy.agent.model.agentmodel.IBodyModel;
import hso.autonomy.agent.model.agentmodel.IBodyPart;
import hso.autonomy.agent.model.agentmodel.IHingeJoint;
import hso.autonomy.agent.model.agentmodel.ISensor;
import hso.autonomy.agent.model.agentmodel.ISensorFactory;
import hso.autonomy.agent.model.agentmodel.impl.ik.IAgentIKSolver;
import hso.autonomy.util.function.FunctionUtil;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.geometry.Pose6D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Represents the body with its body parts and joints
 * @author dorer
 */
public class BodyModel implements Serializable, IBodyModel
{
	/** link to the root body part */
	protected final BodyPart rootBodyPart;

	/** a cache for all sensors including joints for faster access */
	private Map<String, ISensor> sensorCache;

	/** a cache for all actuators excluding joints for faster access */
	private Map<String, IActuator> actuatorCache;

	private boolean centerOfMassValid;

	private Vector3D centerOfMass;

	private transient final IAgentIKSolver ikSolver;

	/**
	 * Constructor to create the body model from a passed meta model
	 * @param metaModel the meta model containing body part information
	 * @param ikSolver the inverse kinematics solver
	 */
	protected BodyModel(IAgentMetaModel metaModel, IAgentIKSolver ikSolver)
	{
		this(metaModel, new DefaultSensorFactory(), ikSolver);
	}

	/**
	 * Constructor to create the body model from a passed meta model
	 * @param metaModel the meta model containing body part information
	 * @param sensorFactory factory for creating sensor instances
	 * @param ikSolver the inverse kinematics solver
	 */
	protected BodyModel(IAgentMetaModel metaModel, ISensorFactory sensorFactory, IAgentIKSolver ikSolver)
	{
		this.ikSolver = ikSolver;
		Map<String, IBodyPart> bodyParts = createBodyParts(metaModel, sensorFactory);
		rootBodyPart = connectBodyParts(metaModel, bodyParts);
		sensorCache = collectAllSensors();
		actuatorCache = collectAllActuators();
		centerOfMassValid = false;
	}

	/**
	 * Copy constructor
	 * @param source the source body model to copy
	 */
	protected BodyModel(BodyModel source)
	{
		this.ikSolver = source.ikSolver;
		rootBodyPart = new BodyPart((BodyPart) source.rootBodyPart, null);
		sensorCache = collectAllSensors();
		actuatorCache = collectAllActuators();
	}

	private Map<String, IBodyPart> createBodyParts(IAgentMetaModel metaModel, ISensorFactory sensorFactory)
	{
		Map<String, IBodyPart> bodyParts = new HashMap<>();

		for (IBodyPartConfiguration config : metaModel.getBodyPartConfigurations()) {
			BodyPart newPart = new BodyPart(config, sensorFactory);
			bodyParts.put(config.getName(), newPart);
		}
		return bodyParts;
	}

	private BodyPart connectBodyParts(IAgentMetaModel metaModel, Map<String, IBodyPart> bodyParts)
	{
		BodyPart result = null;
		for (IBodyPartConfiguration config : metaModel.getBodyPartConfigurations()) {
			BodyPart parent = (BodyPart) bodyParts.get(config.getParent());
			BodyPart currentPart = (BodyPart) bodyParts.get(config.getName());
			currentPart.setParent(parent);
			if (parent == null) {
				// we have what we call torso
				result = currentPart;
			}
		}
		return result;
	}

	private Map<String, ISensor> collectAllSensors()
	{
		Map<String, ISensor> result = new HashMap<>(50);
		rootBodyPart.collectSensors(result);
		return result;
	}

	private Map<String, IActuator> collectAllActuators()
	{
		Map<String, IActuator> result = new HashMap<>(50);
		rootBodyPart.collectActuators(result);
		return result;
	}

	/**
	 * Creates the action content for sending to the server
	 */
	protected void generateTargetStateToAction(IAction action)
	{
		generateBodyActions(action);
	}

	/**
	 * Triggers the generation of actions based on the current internal state of the body model.
	 *
	 * @param actions the action component
	 */
	void generateBodyActions(IAction action)
	{
		rootBodyPart.generateActions(action);
	}

	/**
	 * Updates the joint values in the body model from perception
	 * @param perception the new perception we made
	 */
	protected void updateFromPerception(IPerception perception)
	{
		rootBodyPart.updateFromPerception(perception);
		centerOfMassValid = false;
	}

	/**
	 * Updates the joint values in the body model if not connected to perception
	 */
	protected void updateNoPerception()
	{
		rootBodyPart.updateNoPerception();
		centerOfMassValid = false;
	}

	/**
	 * Called to copy back joint speed values from the passed model to this
	 * model. The passed model is usually a copy that has been created earlier
	 * from this object so that the joint model matches.
	 * @param source the body model to take the values from
	 */
	public void updateJointsSpeed(IBodyModel source)
	{
		rootBodyPart.updateJointsSpeed(source.getRootBodyPart());
	}

	@Override
	public ISensor getSensor(String name)
	{
		return sensorCache.get(name);
		// return torso.getSensorDeep(name);
	}

	@Override
	public IActuator getActuator(String name)
	{
		return actuatorCache.get(name);
	}

	@Override
	public HingeJoint getJoint(String name)
	{
		return (HingeJoint) getSensor(name);
	}

	@Override
	public BodyPart getRootBodyPart()
	{
		return rootBodyPart;
	}

	@Override
	public IBodyPart getBodyPart(String name)
	{
		return rootBodyPart.getBodyPart(name);
	}

	@Override
	public Vector3D getCenterOfMass()
	{
		if (!centerOfMassValid) {
			centerOfMass = rootBodyPart.getCenterOfMass();
		}
		return centerOfMass;
	}

	@Override
	public void setHingeJointPosition(String name, double value)
	{
		getJoint(name).performAxisPosition(value);
	}

	@Override
	public void adjustHingeJointPosition(String name, double delta)
	{
		getJoint(name).adjustAxisPosition(delta);
	}

	@Override
	public void performInitialPose()
	{
		rootBodyPart.performInitialPose();
	}

	@Override
	public void resetAllMovements()
	{
		rootBodyPart.resetAllMovements();
	}

	@Override
	public Vector3D[] getCorners(IBodyPart part)
	{
		Pose3D pose = part.getPose();

		Vector3D[] edges = part.getCorners();
		Vector3D[] edgesReturn = new Vector3D[edges.length];

		for (int i = 0; i < edges.length; i++) {
			edgesReturn[i] = pose.getPosition().add(pose.getOrientation().applyTo(edges[i]));
		}

		return edgesReturn;
	}

	@Override
	public boolean moveBodyPartToPose(String targetBodyName, Pose6D targetPose)
	{
		return moveBodyPartToPose(targetBodyName, targetPose.getPosition(), targetPose.getAngles());
	}

	@Override
	public boolean moveBodyPartToPose(String targetBodyName, Vector3D targetPosition, Vector3D targetAngles)
	{
		IBodyPart targetBody = getBodyPart(targetBodyName);

		if (targetBody != null) {
			return ikSolver.solve(targetBody, targetPosition, targetAngles);
		}

		return false;
	}

	/**
	 * Use this method to do inverse kinematics and calculate the joint angles,
	 * speed and acceleration of the target
	 * @param targetBodyName name of the body part for which to solve
	 * @param targetPose an array of three 6D target poses in the time difference
	 *        FunctionUtil.DELTA_T
	 * @return false if the body part is not existing or is not applicable to
	 *         multiple target position calculation.
	 */
	@Override
	public boolean moveBodyPartToPose(String targetBodyName, Pose6D[] targetPose)
	{
		if (targetPose.length != 3) {
			return false;
		}
		IBodyPart targetBody = getBodyPart(targetBodyName);
		if (targetBody == null) {
			return false;
		}
		Vector3D[] targetPosition = new Vector3D[targetPose.length];
		Vector3D[] targetAngles = new Vector3D[targetPose.length];
		for (int i = 0; i < targetPose.length; i++) {
			targetPosition[i] = targetPose[i].getPosition();
			targetAngles[i] = targetPose[i].getAngles();
		}
		double[] deltas0 = ikSolver.calculateDeltaAngles(targetBody, targetPosition[0], targetAngles[0]);
		if (deltas0 == null) {
			return false;
		}
		double[] deltas1 = ikSolver.calculateDeltaAngles(targetBody, targetPosition[1], targetAngles[1]);
		if (deltas1 == null) {
			return false;
		}
		double[] deltas2 = ikSolver.calculateDeltaAngles(targetBody, targetPosition[2], targetAngles[2]);
		if (deltas2 == null) {
			return false;
		}

		// perform the change
		List<IHingeJoint> hingeJoints = targetBody.getBackwardHingeChain();
		Collections.reverse(hingeJoints);
		for (int i = deltas1.length - 1; i >= 0; i--) {
			IHingeJoint joint = hingeJoints.get(i);
			double angle0 = joint.getAngle() + deltas0[i];
			double angle1 = joint.getAngle() + deltas1[i];
			double angle2 = joint.getAngle() + deltas2[i];
			double speedAtDesiredAngle = FunctionUtil.derivative1(angle0, angle2, 2 * FunctionUtil.DELTA_T);
			double accelerationAtDesiredAngle = FunctionUtil.derivative2(angle0, angle1, angle2, FunctionUtil.DELTA_T);
			joint.setFutureValues((float) angle1, (float) speedAtDesiredAngle, (float) accelerationAtDesiredAngle);
		}

		return true;
	}

	public List<IHingeJoint> getAllHingeJoints()
	{
		List<IHingeJoint> result = new ArrayList<>();
		rootBodyPart.getAllHingeJoints(result);
		return result;
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof BodyModel)) {
			return false;
		}
		BodyModel other = (BodyModel) o;
		return rootBodyPart.equals(other.rootBodyPart);
	}
}
