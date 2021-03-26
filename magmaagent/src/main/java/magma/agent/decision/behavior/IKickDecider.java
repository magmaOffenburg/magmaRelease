/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose2D;
import magma.agent.decision.behavior.base.KickEstimator;
import magma.agent.model.agentmodel.SupportFoot;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public interface IKickDecider extends IKickBaseDecider {
	/**
	 * Set the intended global kick direction.
	 *
	 * @param intendedKickDirection - the intended kick direction in the global
	 *        system
	 */
	void setIntendedKickDirection(Angle intendedKickDirection);

	Angle getIntendedKickDirection();

	void setIntendedKickDistance(double intendedKickDistance);

	double getIntendedKickDistance();

	/**
	 * Sets the global expected position of the ball at kick
	 * @param pos the global expected position of the ball at kick
	 */
	void setExpectedBallPosition(Vector3D pos);

	/**
	 * @return the global expected position of the ball at kick
	 */
	Vector3D getExpectedBallPosition();

	/**
	 * Retrieve the foot with which this kick kicks.
	 *
	 * @return the kicking foot
	 */
	SupportFoot getKickingFoot();

	/**
	 * Retrieve the pose relative to the ball and intended kick direction, to
	 * which we should navigate in order to be able to perform this kick.
	 *
	 * @return the target pose relative to the ball
	 */
	IPose2D getRelativeRunToPose();

	double getOpponentMinDistance();

	float getBallMaxSpeed();

	float getOwnMaxSpeed();

	float getOwnMinSpeed();

	double getOpponentMaxDistance();

	Angle getKickDirection();

	void setKickDirection(Angle kickDirection);

	Angle getRelativeKickDirection();

	void setRelativeKickDirection(Angle relativeKickDirection);

	/**
	 * @return the speed (local coordinates of the run to pose) we want to have
	 *         at the kicking position
	 */
	Vector2D getSpeedAtRunToPose();

	/**
	 * Retrieve the absolute pose and intended kick direction, to which we should navigate in order to be able to
	 * perform this kick. Make sure that intended kick direction and expected ball position has been set to the kick
	 * before.
	 * @return the target pose relative to the ball
	 */
	IPose2D getAbsoluteRunToPose();

	/**
	 * Check if the ball is kickable with this kick into the intended kick
	 * direction set via {@link #setIntendedKickDirection(Angle)}.<br>
	 * This method works like a kind of measure how well we are currently able to
	 * perform this kick in a certain direction. A negative value indicates that
	 * the kick will certainly fail and should not be performed. Values grater
	 * than zero represent the deviation from the current to the optimal kick
	 * situation. With old kicks, this deviation is represented by the deviation
	 * from the kick direction. New kicks return a size measure of the last step
	 * that has to be performed before the kick. This way, old and new kicks can
	 * be prioritized by the same system. If someone wants to mix old and new kicks,
	 * he has to ensure a proper weighting.
	 *
	 * @return < 0 if the ball is not kickable<br>
	 *         >= 0 the deviation of the optimal situation
	 */
	float getExecutability();

	/**
	 * @return how applicable the kick is in the current situation<br>
	 *         < 0 if not applicable<br>
	 *         >= 0 the deviation of the optimal situation
	 */
	float getApplicability();

	KickEstimator getKickEstimator();
}
