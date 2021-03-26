/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.getup;

import kdo.util.parameter.EnumParameterList;

/**
 * @author Klaus Dorer
 */
public class GetUpFromFrontParameters extends EnumParameterList<GetUpFromFrontParameters.Param>
{
	public enum Param {
		TIME1,
		TIME2,
		TIME3,
		TIME4,
		TIME5,
		TIME6,
		TIME7,
		TIME8,
		TIME9, //
		HipYawPitch,
		HipPitch,
		HipRoll,
		KneePitch,
		FootPitch, //
		FootRoll,  //
	}

	public GetUpFromFrontParameters()
	{
		super(Param.class);
	}

	@Override
	protected void setValues()
	{
		put(Param.TIME1, 1.0f);
		put(Param.TIME2, 9.1f);
		put(Param.TIME3, 2.5f);
		put(Param.TIME4, 7.8f);
		put(Param.TIME5, 5.6f);
		put(Param.TIME6, 3.1f);
		put(Param.TIME7, 4.6f);
		put(Param.TIME8, 7.9f);
		put(Param.TIME9, 27.8f);
		put(Param.HipYawPitch, -60f);
		put(Param.HipPitch, 100f);
		put(Param.HipRoll, 45f);
		put(Param.KneePitch, -130f);
		put(Param.FootPitch, 45f);
		put(Param.FootRoll, -45f);

		//		put(Param.TIME1, 5);
		//		put(Param.TIME2, 5);
		//		put(Param.TIME3, 5);
		//		put(Param.TIME4, 5);
		//		put(Param.TIME5, 5);
		//		put(Param.TIME6, 5);
		//		put(Param.TIME7, 5);
		//		put(Param.TIME8, 15);
		//		put(Param.TIME9, 25);
		//		put(Param.HipYawPitch, -60f);
		//		put(Param.HipPitch, 100f);
		//		put(Param.HipRoll, 45f);
		//		put(Param.KneePitch, -130f);
		//		put(Param.FootPitch, 45f);
		//		put(Param.FootRoll, -45f);
	}

	public int getTime1()
	{
		return (int) get(Param.TIME1);
	}

	public int getTime2()
	{
		return (int) get(Param.TIME2);
	}

	public int getTime3()
	{
		return (int) get(Param.TIME3);
	}

	public int getTime4()
	{
		return (int) get(Param.TIME4);
	}

	public int getTime5()
	{
		return (int) get(Param.TIME5);
	}

	public int getTime6()
	{
		return (int) get(Param.TIME6);
	}

	public int getTime7()
	{
		return (int) get(Param.TIME7);
	}

	public int getTime8()
	{
		return (int) get(Param.TIME8);
	}

	public int getTime9()
	{
		return (int) get(Param.TIME9);
	}

	public float getHipPitch()
	{
		return get(Param.HipPitch);
	}

	public float getHipYawPitch()
	{
		return get(Param.HipYawPitch);
	}

	public float getHipRoll()
	{
		return get(Param.HipRoll);
	}

	public float getKneePitch()
	{
		return get(Param.KneePitch);
	}

	public float getFootPitch()
	{
		return get(Param.FootPitch);
	}

	public float getFootRoll()
	{
		return get(Param.FootRoll);
	}
}
