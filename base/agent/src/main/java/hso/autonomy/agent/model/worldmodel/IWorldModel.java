/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel;

import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.worldmodel.localizer.IFeatureLocalizer;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.observer.IObserver;
import hso.autonomy.util.observer.ISubscribe;
import java.util.Collection;
import java.util.List;

/**
 * Represents all environment data available to the agent
 */
public interface IWorldModel extends ISubscribe<IWorldModel> {
	/**
	 * Called to trigger an update of the AgentModel based on the given
	 * perception object.
	 *
	 * @param perception the Perception
	 */
	boolean update(IPerception perception);

	/**
	 * Get a list of all visible landmarks
	 *
	 * @return An unmodifiable collection of the landmarks
	 */
	Collection<ILandmark> getLandmarks();

	/**
	 * @param name the name of the landmark
	 * @return the landmark specified by name
	 */
	ILandmark getLandmark(String name);

	/**
	 * Calculate the average error between known and calculated position
	 *
	 * @return the average error of known position and calculated position,
	 *         Double.MAX_VALUE if we do not see any flags
	 */
	double getLandmarkError();

	/**
	 * Get a list of all visible field lines
	 *
	 * @return An unmodifiable collection of the field lines
	 */
	Collection<IFieldLine> getFieldLines();

	/**
	 * @param name the name of the field line
	 * @return the field line specified by name
	 */
	IFieldLine getFieldLine(String name);

	/**
	 * @return the obstacles to avoid
	 */
	List<IVisibleObject> getObstacles();

	/**
	 * Get the current global time
	 *
	 * @return the time on the global clock
	 */
	float getGlobalTime();

	/**
	 * Detaches the passed observer to this connection
	 *
	 * @param observer the observer that is no longer interested in new messages
	 * @return true if the observer was in the list and detached
	 */
	boolean detach(IObserver<IWorldModel> observer);

	/**
	 * Call to reset the used position filter
	 *
	 * @param initialPose the expected initial pose
	 */
	void resetLocalizer(IPose3D initialPose);

	/**
	 * Retrieves the used localizer
	 *
	 * @return the localizer
	 */
	IFeatureLocalizer getLocalizer();
}