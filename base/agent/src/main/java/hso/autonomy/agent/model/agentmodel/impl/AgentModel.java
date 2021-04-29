/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.communication.action.IAction;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.agentmodel.IBodyModel;
import hso.autonomy.agent.model.agentmodel.IBodyPart;
import hso.autonomy.agent.model.agentmodel.ICamera;
import hso.autonomy.agent.model.agentmodel.IIMU;
import hso.autonomy.agent.model.agentmodel.ILight;
import hso.autonomy.agent.model.agentmodel.IRGBLight;
import hso.autonomy.agent.model.agentmodel.ISensorFactory;
import hso.autonomy.agent.model.agentmodel.impl.ik.IAgentIKSolver;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.observer.IObserver;
import hso.autonomy.util.observer.IPublishSubscribe;
import hso.autonomy.util.observer.Subject;
import java.io.Serializable;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Implementation of the AgentModel. Used to represent all the information the agent has about itself.
 *
 * @author Stefan Glaser, Ingo Schindler
 */
public class AgentModel implements IAgentModel, Serializable
{
	/** observers of agent model */
	protected final transient IPublishSubscribe<IAgentModel> observer;

	private final IAgentMetaModel metaModel;

	/** the body part model of this robot as sensed by last perception */
	protected BodyModel bodyModelSensed;

	/** the body part model of this robot as we expect it after next sense */
	protected BodyModel bodyModelExpected;

	/** the body part model of this robot to define the future */
	protected BodyModel bodyModelFuture;

	/** the name of the camera body part containing the camera */
	protected final String nameOfCameraBodyPart;

	/** the camera offset in the camera body part system */
	protected IPose3D cameraOffset;

	/**
	 * initializes all known Sensors like: HingeJoints, ForceResistances, GyroRates, etc.
	 */
	public AgentModel(IAgentMetaModel metaModel, IAgentIKSolver ikSolver)
	{
		this(metaModel, new DefaultSensorFactory(), ikSolver);
	}

	/**
	 * initializes all known Sensors like: HingeJoints, ForceResistances, GyroRates, etc.
	 */
	public AgentModel(IAgentMetaModel metaModel, ISensorFactory sensorFactory, IAgentIKSolver ikSolver)
	{
		this.metaModel = metaModel;

		bodyModelSensed = createBodyModel(metaModel, sensorFactory, ikSolver);
		bodyModelExpected = bodyModelSensed;
		bodyModelFuture = createBodyModel(bodyModelSensed);
		nameOfCameraBodyPart = metaModel.getNameOfCameraBodyPart();
		cameraOffset = metaModel.getCameraOffset();
		observer = new Subject<>();
	}

	/**
	 * Factory method
	 * @param sourceModel the source from which to create the new body model
	 * @return the specific body model created
	 */
	protected BodyModel createBodyModel(BodyModel sourceModel)
	{
		return new BodyModel(sourceModel);
	}

	/**
	 * Factory method
	 * @param metaModel the agent configuration meta model
	 * @param sensorFactory factory for creating sensor instances
	 * @param ikSolver an inverse kinematic solver if required
	 * @return the specific body model created
	 */
	protected BodyModel createBodyModel(
			IAgentMetaModel metaModel, ISensorFactory sensorFactory, IAgentIKSolver ikSolver)
	{
		return new BodyModel(metaModel, sensorFactory, ikSolver);
	}

	@Override
	public boolean update(IPerception perception)
	{
		boolean result = updateWithFuture(perception);

		// now inform observers about changes
		observer.onStateChange(this);
		return result;
	}

	protected boolean updateWithFuture(IPerception perception)
	{
		// update all Sensors
		bodyModelSensed.updateFromPerception(perception);
		bodyModelExpected = bodyModelFuture;
		bodyModelExpected.updateFromPerception(perception);
		bodyModelFuture = createBodyModel(bodyModelExpected);
		return perception.containsMotion();
	}

	protected boolean updateNoFuture(IPerception perception)
	{
		// we only have two bodyModels effectively
		bodyModelSensed.updateFromPerception(perception);
		bodyModelExpected = bodyModelSensed;
		bodyModelFuture.updateNoPerception();
		return perception.containsMotion();
	}

	@Override
	public void attach(IObserver<IAgentModel> newObserver)
	{
		observer.attach(newObserver);
	}

	@Override
	public boolean detach(IObserver<IAgentModel> oldObserver)
	{
		return observer.detach(oldObserver);
	}

	@Override
	public HingeJoint getHJ(String name)
	{
		return bodyModelSensed.getJoint(name);
	}

	@Override
	public HingeJoint getHJExpected(String name)
	{
		return bodyModelExpected.getJoint(name);
	}

	@Override
	public HingeJoint getWriteableHJ(String name)
	{
		// TODO: kdo here we can now switch to a separate instance
		return bodyModelFuture.getJoint(name);
	}

	@Override
	public IBodyPart getBodyPart(String name)
	{
		// TODO: check which model to use
		// Better would be I think to move this method to BodyModel and out of
		// here
		// so that the user decides.
		return bodyModelSensed.getBodyPart(name);
	}

	@Override
	public BodyPart getRootBodyPart()
	{
		return bodyModelSensed.getRootBodyPart();
	}

	@Override
	public Vector3D getCenterOfMass()
	{
		// TODO: check which model to use
		// Better would be I think to move this method to BodyModel and out of
		// here
		// so that the user decides.
		return bodyModelSensed.getCenterOfMass();
	}

	@Override
	public ForceResistance getForceResistance(String name)
	{
		// TODO: check which model to use
		// Better would be I think to move this method to BodyModel and out of
		// here
		// so that the user decides.
		return (ForceResistance) bodyModelSensed.getSensor(name);
	}

	@Override
	public GyroRate getGyroRate(String name)
	{
		// TODO: check which model to use
		// Better would be I think to move this method to BodyModel and out of here so that the user decides.
		return (GyroRate) bodyModelSensed.getSensor(name);
	}

	@Override
	public Accelerometer getAccelerometer(String name)
	{
		// TODO: check which model to use
		// Better would be I think to move this method to BodyModel and out of here so that the user decides.
		return (Accelerometer) bodyModelSensed.getSensor(name);
	}

	@Override
	public IIMU getIMU(String name)
	{
		// TODO: check which model to use
		// Better would be I think to move this method to BodyModel and out of here so that the user decides.
		return (IMU) bodyModelSensed.getSensor(name);
	}

	@Override
	public ICamera getCamera(String name)
	{
		// TODO: check which model to use
		// Better would be I think to move this method to BodyModel and out of here so that the user decides.
		return (Camera) bodyModelSensed.getSensor(name);
	}

	@Override
	public ILight getLight(String name)
	{
		return (ILight) bodyModelFuture.getActuator(name);
	}

	@Override
	public IRGBLight getRGBLight(String name)
	{
		return (IRGBLight) bodyModelFuture.getActuator(name);
	}

	@Override
	public void reflectTargetStateToAction(IAction action)
	{
		bodyModelFuture.generateTargetStateToAction(action);
	}

	@Override
	public void updateJointsSpeed(IBodyModel source)
	{
		bodyModelFuture.updateJointsSpeed(source);
	}

	@Override
	public IBodyModel getFutureBodyModel()
	{
		return bodyModelFuture;
	}

	@Override
	public IBodyPart getBodyPartContainingCamera()
	{
		if (nameOfCameraBodyPart != null) {
			return getBodyPart(nameOfCameraBodyPart);
		}

		return null;
	}

	@Override
	public IPose3D getCameraOffset()
	{
		return this.cameraOffset;
	}

	@Override
	public Pose3D getCameraPose()
	{
		return getBodyPartContainingCamera().getPose().applyTo(this.cameraOffset);
	}

	protected IAgentMetaModel getMetaModel()
	{
		return metaModel;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof AgentModel)) {
			return false;
		}
		return bodyModelSensed.equals(((AgentModel) obj).bodyModelSensed);
	}

	@Override
	public int hashCode()
	{
		return bodyModelSensed.hashCode();
	}

	@Override
	public String toString()
	{
		return bodyModelSensed.toString();
	}

	@Override
	public void setCameraOffset(Pose3D pose3d)
	{
		this.cameraOffset = pose3d;
	}
}
