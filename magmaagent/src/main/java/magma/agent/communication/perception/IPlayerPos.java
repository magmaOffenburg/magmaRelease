/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.perception;

import hso.autonomy.agent.communication.perception.IVisibleObjectPerceptor;
import java.util.List;
import java.util.Map;
import magma.agent.model.worldmodel.IPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * The Player Position Perceptor represents a single player as reported by the
 * visual sensor sub-system. Provides read-only access only.
 *
 * @author Simon Raffeiner
 */
public interface IPlayerPos extends IVisibleObjectPerceptor {
	int getId();

	String getTeamname();

	/**
	 * Retrieve a list of all visible body parts
	 *
	 * @return List of body parts
	 */
	Map<String, Vector3D> getAllBodyParts();

	/**
	 * Retrieve the 3-dimensional position of a given body part
	 *
	 * @param partname Body part name
	 * @return Body part position or null if no matching body part was found
	 */
	Vector3D getBodyPartPosition(String partname);

	void averagePosition(List<IPlayer> playersPos);
}