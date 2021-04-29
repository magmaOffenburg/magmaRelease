/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior;

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.util.geometry.Angle;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Interface for all underlying basic walk behaviors
 * @author kdorer
 */
public interface IBaseWalk extends IBehavior {
	/**
	 * Instrumentation for the walk
	 * @param forward percentage of forward backward speed (-100 is backward)
	 * @param sideward percentage of sideward speed (-100 is right)
	 * @param turn degrees to turn (-values turn right)
	 */
	void setMovement(double forward, double sideward, Angle turn);

	/**
	 * Instrumentation for the walk
	 * @param forward percentage of forward backward speed (-100 is backward)
	 * @param sideward percentage of sideward speed (-100 is right)
	 * @param turn degrees to turn (-values turn right)
	 * @param paramSetName the name of the walk parameter set to use
	 */
	void setMovement(double forward, double sideward, Angle turn, String paramSetName);

	/**
	 * @return the maximal angle to turn in one step
	 */
	Angle getMaxTurnAngle();

	/**
	 * @return the desired x-y walk speed (relative to max speed)
	 */
	Vector2D getIntendedWalk();

	/**
	 * @return the current x-y walk speed (in m/s)
	 */
	Vector2D getCurrentSpeed();

	/**
	 * @return the intended turn angle
	 */
	Angle getIntendedTurn();

	/**
	 * @return the real turn angle
	 */
	Angle getCurrentTurn();

	/**
	 * @return true if this is the start of a new step
	 */
	boolean isNewStep();
}
