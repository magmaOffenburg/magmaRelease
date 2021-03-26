/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao3.decision.behavior.movement;

import magma.robots.nao.decision.behavior.movement.getup.GetUpFromBackParameters;

public class GetUpFromBackParametersNao3 extends GetUpFromBackParameters
{
	@Override
	protected void setValues()
	{
		super.setValues();

		put(Param.HipYawPitch, -97.96448f);
		put(Param.HipPitch, 99.048546f);
		put(Param.HipRoll, 25.762905f);
		put(Param.KneePitch, -126.554405f);
		put(Param.FootPitch, -56.232098f);
		put(Param.HipPitchSpeed, 6.409752f);
		put(Param.FootPitchSpeed, 1.0555235f);
		// Average utility: 2.356 averaged: 10 [ 2.356 -0.983 51.200 ]
	}
}
