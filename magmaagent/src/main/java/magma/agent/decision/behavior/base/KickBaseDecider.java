/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.base;

import magma.agent.decision.behavior.IKickBaseDecider;

/**
 * Base class for making kick decisions
 *
 * @author Klaus Dorer
 */
public class KickBaseDecider implements IKickBaseDecider
{
	/** The maximum kick distance of the kick. */
	private final double maxKickDistance;

	/** The minimum kick distance of the kick. */
	private final double minKickDistance;

	/** the number of cycles this kick needs to hit the ball from starting */
	private int ballHitCycles;

	/** the relative priority compared to other kicks */
	private float priority;

	private boolean unstable;

	private KickDistribution distribution;

	private float kickPower;

	public KickBaseDecider(KickDistribution distribution, double maxKickDistance, double minKickDistance,
			int ballHitCycles, boolean unstable, float priority)
	{
		this.distribution = distribution;
		this.maxKickDistance = maxKickDistance;
		this.minKickDistance = minKickDistance;
		this.ballHitCycles = ballHitCycles;
		this.unstable = unstable;
		this.priority = priority;
		kickPower = 1;
	}

	@Override
	public double getMaxKickDistance()
	{
		return maxKickDistance;
	}

	@Override
	public double getMinKickDistance()
	{
		return minKickDistance;
	}

	@Override
	public float getPriority()
	{
		return priority;
	}

	public void setPriority(float priority)
	{
		this.priority = priority;
	}

	@Override
	public int getBallHitCycles()
	{
		return ballHitCycles;
	}

	public void setBallHitCycles(int ballHitCycles)
	{
		this.ballHitCycles = ballHitCycles;
	}

	@Override
	public boolean isUnstable()
	{
		return unstable;
	}

	@Override
	public float getKickPower()
	{
		return kickPower;
	}

	@Override
	public void setKickPower(float kickPower)
	{
		this.kickPower = kickPower;
	}

	@Override
	public KickDistribution getDistribution()
	{
		return distribution;
	}
}
