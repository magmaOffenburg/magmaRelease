/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.kick;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose2D;
import magma.agent.decision.behavior.IKickBaseDecider;
import magma.agent.decision.behavior.IKickDecider;
import magma.agent.decision.behavior.base.KickDistribution;
import magma.agent.decision.behavior.base.KickEstimator;
import magma.agent.model.agentmodel.SupportFoot;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author kdorer
 *
 */
public class StabilizedKickDecider implements IKickDecider
{
	private IKickDecider stabilizeDecider;
	private IKickBaseDecider kickDecider;
	private int stabilizeCycles;

	public StabilizedKickDecider(IKickDecider stabilizeDecider, int stabilizeCycles, IKickBaseDecider kickDecider)
	{
		this.stabilizeDecider = stabilizeDecider;
		this.stabilizeCycles = stabilizeCycles;
		this.kickDecider = kickDecider;
	}

	@Override
	public void setIntendedKickDirection(Angle intendedKickDirection)
	{
		stabilizeDecider.setIntendedKickDirection(intendedKickDirection);
	}

	@Override
	public Angle getIntendedKickDirection()
	{
		return stabilizeDecider.getIntendedKickDirection();
	}

	@Override
	public void setIntendedKickDistance(double intendedKickDistance)
	{
		stabilizeDecider.setIntendedKickDistance(intendedKickDistance);
	}

	@Override
	public SupportFoot getKickingFoot()
	{
		return stabilizeDecider.getKickingFoot();
	}

	@Override
	public float getExecutability()
	{
		return stabilizeDecider.getExecutability();
	}

	@Override
	public float getApplicability()
	{
		return stabilizeDecider.getApplicability();
	}

	@Override
	public int getBallHitCycles()
	{
		return stabilizeCycles + kickDecider.getBallHitCycles();
	}

	@Override
	public KickDistribution getDistribution()
	{
		return kickDecider.getDistribution();
	}

	@Override
	public IPose2D getRelativeRunToPose()
	{
		return stabilizeDecider.getRelativeRunToPose();
	}

	@Override
	public Vector2D getSpeedAtRunToPose()
	{
		return stabilizeDecider.getSpeedAtRunToPose();
	}

	@Override
	public IPose2D getAbsoluteRunToPose()
	{
		return stabilizeDecider.getAbsoluteRunToPose();
	}

	@Override
	public double getMaxKickDistance()
	{
		return kickDecider.getMaxKickDistance();
	}

	@Override
	public double getMinKickDistance()
	{
		return kickDecider.getMinKickDistance();
	}

	@Override
	public void setKickPower(float kickPower)
	{
		kickDecider.setKickPower(kickPower);
	}

	@Override
	public boolean isUnstable()
	{
		return kickDecider.isUnstable();
	}

	@Override
	public void setExpectedBallPosition(Vector3D pos)
	{
		stabilizeDecider.setExpectedBallPosition(pos);
	}

	@Override
	public Vector3D getExpectedBallPosition()
	{
		return stabilizeDecider.getExpectedBallPosition();
	}

	@Override
	public float getKickPower()
	{
		return stabilizeDecider.getKickPower();
	}

	@Override
	public float getPriority()
	{
		return stabilizeDecider.getPriority();
	}

	@Override
	public double getIntendedKickDistance()
	{
		return stabilizeDecider.getIntendedKickDistance();
	}

	@Override
	public double getOpponentMinDistance()
	{
		return stabilizeDecider.getOpponentMinDistance();
	}

	@Override
	public double getOpponentMaxDistance()
	{
		return stabilizeDecider.getOpponentMaxDistance();
	}

	@Override
	public float getBallMaxSpeed()
	{
		return stabilizeDecider.getBallMaxSpeed();
	}

	@Override
	public float getOwnMaxSpeed()
	{
		return stabilizeDecider.getOwnMaxSpeed();
	}

	@Override
	public float getOwnMinSpeed()
	{
		return stabilizeDecider.getOwnMinSpeed();
	}

	@Override
	public Angle getKickDirection()
	{
		return stabilizeDecider.getKickDirection();
	}

	@Override
	public void setKickDirection(Angle kickDirection)
	{
		stabilizeDecider.setKickDirection(kickDirection);
	}

	@Override
	public Angle getRelativeKickDirection()
	{
		return stabilizeDecider.getRelativeKickDirection();
	}

	@Override
	public KickEstimator getKickEstimator()
	{
		return stabilizeDecider.getKickEstimator();
	}

	@Override
	public void setRelativeKickDirection(Angle relativeKickDirection)
	{
		stabilizeDecider.setRelativeKickDirection(relativeKickDirection);
	}
}
