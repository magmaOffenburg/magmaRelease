/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

import hso.autonomy.agent.communication.action.IAction;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.observer.IObserver;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Provides read only access to the agent model including universal joints,
 * hinge joints, force resistance and gyro rate sensors
 *
 * @author Stefan Glaser, Klaus Dorer
 */
public interface IAgentModel {
	/**
	 * Called to trigger an update of the AgentModel based on the given
	 * perception object.
	 *
	 * @param perception the Perception
	 */
	boolean update(IPerception perception);

	/**
	 * This method is called from the AgentRuntime in order to reflect the
	 * target-state of the agent model into effector-actions, which can then be
	 * sent to the server.
	 */
	void reflectTargetStateToAction(IAction action);

	/**
	 * Returns read only access to a specified hinge joint in the sensed body
	 * model of the agent
	 * @param name the name of the joint
	 * @return read only version of the hinge joint specified by name
	 */
	IHingeJointR getHJ(String name);

	/**
	 * Returns read only access to a specified hinge joint in the expected body
	 * model of the agent
	 * @param name the name of the joint
	 * @return read only version of the hinge joint specified by name
	 */
	IHingeJointR getHJExpected(String name);

	/**
	 * Returns a specified hinge joint of the agent
	 * @param name the name of the joint
	 * @return read write version of the hinge joint specified by name
	 */
	IHingeJoint getWriteableHJ(String name);

	/**
	 * Returns a specified force resistance sensor of the agent
	 * @param name the name of the sensor
	 * @return read only version of the force sensor specified by name
	 */
	IForceResistance getForceResistance(String name);

	/**
	 * Retrieve accelerometer values
	 *
	 * @param name the name of the sensor
	 * @return Accelerations
	 */
	IAccelerometer getAccelerometer(String name);

	/**
	 * Retrieves the specified gyro sensor
	 *
	 * @param name the name of the sensor
	 * @return read only version of the gyro sensor specified by name
	 */
	IGyroRate getGyroRate(String name);

	/**
	 * Retrieves the specified IMU sensor
	 *
	 * @param name the name of the sensor
	 * @return read only version of the IMU sensor specified by name
	 */
	IIMU getIMU(String name);

	/**
	 * Retrieves the specified Camera sensor
	 *
	 * @param name the name of the sensor
	 * @return read only version of the Camera sensor specified by name
	 */
	ICamera getCamera(String name);

	/**
	 * Retrieves the specified light actuator
	 *
	 * @param name the name of the light
	 * @return the light actuator specified by name
	 */
	ILight getLight(String name);

	/**
	 * Retrieves the specified colored light actuator
	 *
	 * @param name the name of the light
	 * @return the light actuator specified by name
	 */
	IRGBLight getRGBLight(String name);

	/**
	 * Get the Center of mass
	 *
	 * @return Center of Mass
	 */
	Vector3D getCenterOfMass();

	/**
	 * @see hso.autonomy.util.observer.IPublishSubscribe#attach(hso.autonomy.util.observer.
	 *      IObserver)
	 *
	 * @param newObserver Observer object to add to the list
	 */
	void attach(IObserver<IAgentModel> newObserver);

	/**
	 * @see
	 * hso.autonomy.util.observer.IPublishSubscribe#detach(hso.autonomy.util.observer.IObserver)
	 *
	 * @param oldObserver Observer object to delete from the list
	 * @return True if successfully detached, false if not
	 */
	boolean detach(IObserver<IAgentModel> oldObserver);

	/**
	 * Returns the root body part of the sensed body model.
	 *
	 * @return the root body part
	 */
	IBodyPart getRootBodyPart();

	/**
	 * @param name the name of the body part to retrieve
	 * @return the body part with the specified name
	 */
	IBodyPart getBodyPart(String name);

	/**
	 * Returns the future body model, which is a copy of the sensed/expected body
	 * model, on which the actions for the current cycle are performed.
	 *
	 * @return the future body model
	 */
	IBodyModel getFutureBodyModel();

	/**
	 * Called to copy back joint speed values from the passed body model to this
	 * agents body model. The passed model is usually a copy that has been
	 * created earlier from this object so that the joint model matches.
	 * @param source the body model to take the values from
	 */
	void updateJointsSpeed(IBodyModel source);

	/**
	 * Returns the body part (of the sensed body model), which contains the
	 * camera.
	 *
	 * @return the body part containing the camera, or null, if no body part
	 *         contains a camera
	 */
	IBodyPart getBodyPartContainingCamera();

	/**
	 * Returns the offset of the camera relative to the system of the body part
	 * containing the camera.
	 *
	 * @return the offset of the camera in the camera body part
	 */
	IPose3D getCameraOffset();

	void setCameraOffset(Pose3D pose3d);

	/**
	 * Returns the current body-local pose of the camera (camera-body-part-pose
	 * combined with camera-offset)
	 *
	 * @return the current camera pose
	 */
	Pose3D getCameraPose();
}