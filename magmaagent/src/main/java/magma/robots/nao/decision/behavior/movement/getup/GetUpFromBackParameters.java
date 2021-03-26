/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.getup;

import kdo.util.parameter.EnumParameterList;

/**
 * @author Klaus Dorer
 */
public class GetUpFromBackParameters extends EnumParameterList<GetUpFromBackParameters.Param>
{
	public enum Param {
		TIME1,
		TIME2,
		TIME3,
		TIME4, //
		HipYawPitch,
		HipPitch,
		HipRoll,
		KneePitch,
		FootPitch, //
		HipPitchSpeed,
		FootPitchSpeed
	}

	public GetUpFromBackParameters()
	{
		super(Param.class);
	}

	@Override
	protected void setValues()
	{
		put(Param.TIME1, 12);
		put(Param.TIME2, 10.291558f);
		put(Param.TIME3, 14.841928f);
		put(Param.TIME4, 22.867283f);
		put(Param.HipYawPitch, -99.13001f);
		put(Param.HipPitch, 104.3575f);
		put(Param.HipRoll, 32.10262f);
		put(Param.KneePitch, -123.23603f);
		put(Param.FootPitch, -56.03583f);
		put(Param.HipPitchSpeed, 5.3564677f);
		put(Param.FootPitchSpeed, 5.3803496f);
		// Average utility: 2.860 averaged: 10 [ 2.860 -1.311 79.700 ]
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

	final public int getTime4()
	{
		return (int) get(Param.TIME4);
	}

	final public float getHipPitch()
	{
		return get(Param.HipPitch);
	}

	final public float getHipYawPitch()
	{
		return get(Param.HipYawPitch);
	}

	final public float getHipRoll()
	{
		return get(Param.HipRoll);
	}

	final public float getKneePitch()
	{
		return get(Param.KneePitch);
	}

	final public float getFootPitch()
	{
		return get(Param.FootPitch);
	}

	final public float getHipPitchSpeed()
	{
		return get(Param.HipPitchSpeed);
	}

	final public float getFootPitchSpeed()
	{
		return get(Param.FootPitchSpeed);
	}
}
