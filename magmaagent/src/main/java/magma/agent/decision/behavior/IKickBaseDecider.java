/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior;

import magma.agent.decision.behavior.base.KickDistribution;

/**
 * The subset of the <code>IKick</code> interface which is needed for the kick
 * part of <code>StabilizedKick</code> behaviors.
 */
public interface IKickBaseDecider {
	/**
	 * @return the maximum kick distance this kick is able to kick
	 */
	double getMaxKickDistance();

	/**
	 * @return the minimum kick distance this kick is able to kick
	 */
	double getMinKickDistance();

	float getKickPower();

	/**
	 * @param kickPower the kickPower to set (factor 0.5 means half long)
	 */
	void setKickPower(float kickPower);

	/**
	 * @return whether the agent is expected fall down after the kick is complete
	 */
	boolean isUnstable();

	/**
	 * @return how many cycles it takes until the ball is hit.
	 */
	int getBallHitCycles();

	/**
	 * @return Probability distribution of this kick's distance / angle.
	 */
	KickDistribution getDistribution();

	float getPriority();
}
