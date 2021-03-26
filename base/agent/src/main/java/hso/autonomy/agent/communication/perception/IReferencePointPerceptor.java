/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public interface IReferencePointPerceptor extends IPerceptor {
	public String getLabel();

	public void setLabel(String label);

	public Vector3D getSeenPosition();

	public void setSeenPosition(Vector3D seenPosition);
}
