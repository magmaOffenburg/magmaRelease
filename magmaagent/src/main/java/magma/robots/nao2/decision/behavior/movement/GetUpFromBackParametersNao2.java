/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao2.decision.behavior.movement;

import magma.robots.nao.decision.behavior.movement.getup.GetUpFromBackParameters;

public class GetUpFromBackParametersNao2 extends GetUpFromBackParameters
{
	@Override
	protected void setValues()
	{
		super.setValues();

		put(Param.TIME1, 12);
		put(Param.TIME2, 11.133841f);
		put(Param.TIME3, 16.155367f);
		put(Param.TIME4, 24.848993f);
		put(Param.HipYawPitch, -102.07887f);
		put(Param.HipPitch, 97.28174f);
		put(Param.HipRoll, 25.376493f);
		put(Param.KneePitch, -127.013535f);
		put(Param.FootPitch, -56.13343f);
		put(Param.HipPitchSpeed, 4.6851935f);
		put(Param.FootPitchSpeed, 3.3894556f);
		// Average utility: 2.733 averaged: 10 [ 2.733 -0.734 91.000 ]
	}
}
