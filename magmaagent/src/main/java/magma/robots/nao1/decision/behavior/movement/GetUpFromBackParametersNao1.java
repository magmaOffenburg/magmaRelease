/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao1.decision.behavior.movement;

import magma.robots.nao.decision.behavior.movement.getup.GetUpFromBackParameters;

public class GetUpFromBackParametersNao1 extends GetUpFromBackParameters
{
	@Override
	protected void setValues()
	{
		super.setValues();

		put(Param.HipYawPitch, -94.974976f);
		put(Param.HipPitch, 101.52334f);
		put(Param.HipRoll, 28.040258f);
		put(Param.KneePitch, -129.80742f);
		put(Param.FootPitch, -53.971676f);
		put(Param.HipPitchSpeed, 4.121893f);
		put(Param.FootPitchSpeed, 6.704934f);
		// Average utility: 2.459 averaged: 10 [ 2.459 -2.856 50.000 ]
	}
}
