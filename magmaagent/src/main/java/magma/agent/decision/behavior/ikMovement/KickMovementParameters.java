/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import kdo.util.parameter.EnumParameterList;
import magma.agent.decision.behavior.base.KickDistribution;

/**
 * @author Klaus Dorer
 */
public abstract class KickMovementParameters extends EnumParameterList<KickMovementParameters.Param>
{
	protected KickDistribution distribution;

	public enum Param {
		TIME0,
		TIME1,
		TIME2,
		TIME3,
		LHP1,
		RHP1,
		RKP1,
		RFP1,
		RTP1,
		LHP2,
		LHP3,
		LHYP1,
		LHYP2,
		LHYP3,
		LHYPS1,
		LHYPS2,
		RHYP2,
		RHYP3,
		RHP2,
		RHP3,
		RKP2,
		RKP3,
		RFP2,
		RFP3,
		RTP2,
		RKPS1,
		RHPS2,
		RHYPS2,
		RFPS2,
		RHR2,
		RHR3,
		LHR1,
		LHR2,
		LHR3,
		LKP2,
		LKP3,
		LFP2,
		LFP3,
		POS_X,
		POS_Y,
		KICK_ANGLE,
		MIN_X_OFFSET,
		RUN_TO_X,
		RUN_TO_Y,
		CANCEL_DISTANCE,
		STABILIZE_TIME
	}

	public KickMovementParameters()
	{
		super(Param.class);
	}

	@Override
	protected abstract void setValues();

	final public int getTime0()
	{
		return (int) get(Param.TIME0);
	}

	final public int getTime1()
	{
		return (int) get(Param.TIME1);
	}

	final public int getTime2()
	{
		return (int) get(Param.TIME2);
	}

	final public int getTime3()
	{
		return (int) get(Param.TIME3);
	}

	final public float getLHP1()
	{
		return get(Param.LHP1);
	}

	final public float getRHP1()
	{
		return get(Param.RHP1);
	}

	final public float getRKP1()
	{
		return get(Param.RKP1);
	}

	final public float getRFP1()
	{
		return get(Param.RFP1);
	}

	final public float getRTP1()
	{
		return get(Param.RTP1);
	}

	final public float getLHP2()
	{
		return get(Param.LHP2);
	}

	final public float getLHP3()
	{
		return get(Param.LHP3);
	}

	final public float getLHYP1()
	{
		return get(Param.LHYP1);
	}

	final public float getLHYP2()
	{
		return get(Param.LHYP2);
	}

	final public float getLHYP3()
	{
		return get(Param.LHYP3);
	}

	final public float getLHYPS1()
	{
		return get(Param.LHYPS1);
	}

	final public float getLHYPS2()
	{
		return get(Param.LHYPS2);
	}

	final public float getRHYP2()
	{
		return get(Param.RHYP2);
	}

	final public float getRHYP3()
	{
		return get(Param.RHYP3);
	}

	final public float getRHP2()
	{
		return get(Param.RHP2);
	}

	final public float getRHP3()
	{
		return get(Param.RHP3);
	}

	final public float getRKP2()
	{
		return get(Param.RKP2);
	}

	final public float getRKP3()
	{
		return get(Param.RKP3);
	}

	final public float getRFP2()
	{
		return get(Param.RFP2);
	}

	final public float getRFP3()
	{
		return get(Param.RFP3);
	}

	final public float getRTP2()
	{
		return get(Param.RTP2);
	}

	final public float getRKPS1()
	{
		return get(Param.RKPS1);
	}

	final public float getRHPS2()
	{
		return get(Param.RHPS2);
	}

	final public float getRHYPS2()
	{
		return get(Param.RHYPS2);
	}

	final public float getRFPS2()
	{
		return get(Param.RFPS2);
	}

	final public float getRHR2()
	{
		return get(Param.RHR2);
	}

	final public float getRHR3()
	{
		return get(Param.RHR3);
	}

	final public float getLHR1()
	{
		return get(Param.LHR1);
	}

	final public float getLHR2()
	{
		return get(Param.LHR2);
	}

	final public float getLHR3()
	{
		return get(Param.LHR3);
	}

	final public float getPosX()
	{
		return get(Param.POS_X);
	}

	final public float getPosY()
	{
		return get(Param.POS_Y);
	}

	final public float getKickAngle()
	{
		return get(Param.KICK_ANGLE);
	}

	final public float getLKP2()
	{
		return get(Param.LKP2);
	}

	final public float getLKP3()
	{
		return get(Param.LKP3);
	}

	final public float getLFP2()
	{
		return get(Param.LFP2);
	}

	final public float getLFP3()
	{
		return get(Param.LFP3);
	}

	public KickDistribution getDistribution()
	{
		return distribution;
	}
}
