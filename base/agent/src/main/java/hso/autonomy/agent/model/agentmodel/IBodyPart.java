/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

import hso.autonomy.agent.communication.action.IAction;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.util.geometry.Pose3D;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Generic BodyPart interface
 *
 * @author Ingo Schindler
 */
public interface IBodyPart {
	/**
	 * Retrieve the name of the body part.
	 * @return name of the body part
	 */
	String getName();

	/**
	 * Retrieve the parent body part.
	 *
	 * @return parent body part
	 */
	IBodyPart getParent();

	/**
	 * Retrieve the position of the center of this body part relative to the root body.
	 *
	 * @return Position of the center of this BodyPart relative to the root body
	 */
	Vector3D getPosition();

	/**
	 * The complete orientation matrix from root body to the last Joint in this row.<br>
	 * E.g. Torso -> Shoulder -> UpperArm -> Elbow -> LowerArm. Then this is the
	 * orientation of the LowerArm.
	 *
	 * @return orientation Matrix
	 */
	Rotation getOrientation();

	/**
	 * Return the pose (position and orientation) of the body part's center.
	 *
	 * @return the pose of the body part's center
	 */
	Pose3D getPose();

	/**
	 * Return the pose (position and orientation) of the specified local position
	 * relative to the body part's center.
	 *
	 * @return the pose of the specified local position relative to the body
	 *         part's center
	 */
	Pose3D getPose(Vector3D localPos);

	/**
	 * The complete transformation matrix from root body to the joint in this row.<br>
	 * E.g. Torso -> Shoulder -> UpperArm -> Elbow -> LowerArm. Then this is
	 * the position and orientation of the LowerArm-joint.
	 *
	 * @return Transformation Matrix
	 */
	Pose3D getJointTransformation();

	/**
	 * Retrieve the mass of this body part.
	 *
	 * @return mass of this body part
	 */
	float getMass();

	/**
	 * Retrieve the mass of this body part and all attached child body parts.
	 *
	 * @return the mass of this body part and all of its children (in kg)
	 */
	float getWholeMass();

	/**
	 * Get geometry of this BodyPart
	 *
	 * @return Geometry
	 */
	Vector3D getGeometry();

	/**
	 * Check if this body part is the root body part.
	 *
	 * @return true, if this body part is the root body part, false otherwise
	 */
	boolean isRootBody();

	/**
	 * Triggers the generation of actions based on the current internal state of the body model.
	 *
	 * @param actions the action component
	 */
	void generateActions(IAction action);

	/**
	 * Updates the joint values in the body model from perception
	 * @param perception the new perception we made
	 */
	void updateFromPerception(IPerception perception);

	/**
	 * Updates the joint values in the body model with no perception
	 */
	void updateNoPerception();

	/**
	 * @return the number of child body parts
	 */
	int getNoOfChildren();

	/**
	 * A collection of the child body parts to this body part.
	 *
	 * @return a collection of child body parts
	 */
	Collection<IBodyPart> getChildren();

	/**
	 * @param name name of the child body part
	 * @return the child body part, null if not existing
	 */
	IBodyPart getChild(String name);

	/**
	 * @return the joint which connects this body part to its parent body part.
	 */
	IHingeJointR getJoint();

	/**
	 * @param name the name of the part to retrieve
	 * @return the specified body part, null if not existing
	 */
	IBodyPart getBodyPart(String name);

	/**
	 * @return the center of mass of this body part and all parts connected to it
	 */
	Vector3D getCenterOfMass();

	/**
	 * Searches the body part hierarchy for a sensor with passed name
	 * @param name the name of the sensor
	 * @return the sensor with passed name, null if not existing
	 */
	ISensor getSensorDeep(String name);

	/**
	 * Returns the sensor instance of the requested type, if this body part
	 * contains such a sensor type.
	 *
	 * @param <T> - the type if the sensor
	 * @param type - the type of the sensor
	 * @return the sensor instance of the requested type, if existing
	 */
	<T extends ISensor> T getSensor(Class<T> type);

	/**
	 * Searches the body part hierarchy for an actuator with passed name
	 * @param name the name of the actuator
	 * @return the actuator with passed name, null if not existing
	 */
	IActuator getActuatorDeep(String name);

	/**
	 * Returns the actuator instance of the requested type, if this body part
	 * contains such an actuator type.
	 *
	 * @param <T> - the type if the actuator
	 * @param type - the type of the actuator
	 * @return the actuator instance of the requested type, if existing
	 */
	<T extends IActuator> T getActuator(Class<T> type);

	/**
	 * Deep update of joint's desired speed from the passed body part. The passed
	 * part has to match to this part and is usually created as copy from this
	 * sometimes before.
	 * @param part the body part from which to take the update values
	 */
	void updateJointsSpeed(IBodyPart part);

	/**
	 * @param globalOrientation matrix representing the global orientation of the
	 *        torso
	 * @return the pitch angle (deg) of this body part in the global coordinate
	 *         system
	 */
	double getGlobalPitch(Rotation globalOrientation);

	/**
	 * @param globalOrientation matrix representing the global orientation of the
	 *        torso
	 * @return the roll angle (deg) of this body part in the global coordinate
	 *         system
	 */
	double getGlobalRoll(Rotation globalOrientation);

	/**
	 * Get the position of the corners relative to the center of the BodyPart
	 *
	 * e.g. for a cube this will return eight vectors to the corners of it
	 *
	 * <pre>
	 *     6___________4
	 *    /|          /|
	 *   / |         / |
	 *  7___________5  |
	 *  |  |        |  |
	 *  |  3________|__1     z
	 *  | /         | /	  	 |   x
	 *  |/          |/	    | /
	 *  2___________0		    |/_____y
	 * </pre>
	 *
	 * TODO: change the method that the sequence makes more sense
	 *
	 * @return corners of the BodyPart relative to its center
	 */
	Vector3D[] getCorners();

	/**
	 * @param result map to add all sensors deep for caching
	 */
	void collectSensors(Map<String, ISensor> result);

	/**
	 * @param result map to add all actuators deep for caching
	 */
	void collectActuators(Map<String, IActuator> result);

	/**
	 * Retrieve the jacobian matrix, having this IBodyPart as the end of the
	 * chain. The jacobian is created with respect to the given endEffectorPos in
	 * the local system of this body.
	 *
	 * @param endEffectorPos - the position of the end effector of the
	 *        configuration in the local system of this IBodyPart
	 * @return the jacobian matrix
	 */
	double[][] getJacobian(Vector3D endEffectorPos, Vector3D targetAngles);

	/**
	 * Retrieve the translation from the center of the parent body part to the
	 * center of this body part with a zero joint angles.
	 *
	 * @return the translation from the parent body to this body with zero joint
	 *         angles
	 */
	Vector3D getTranslation();

	/**
	 * Retrieve the anchor of the joint in the local body system.
	 *
	 * @return the anchor of the joint in the local system
	 */
	Vector3D getAnchor();

	void getAllHingeJoints(List<IHingeJoint> result);

	/**
	 *
	 * @return the chain of hinge joints from this body part backward to root
	 */
	List<IHingeJoint> getBackwardHingeChain();
}
