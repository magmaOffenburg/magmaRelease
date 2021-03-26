/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel;

import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public interface IBall extends IMoveableObject {
	float COLLISION_DISTANCE = 0.28f;

	float getRadius();

	void resetPosition(Vector3D position);

	boolean isBouncing(float globalTime);

	void updateFromAudio(Vector3D localPosition, Vector3D globalPosition, Vector3D speed, float time);
}
