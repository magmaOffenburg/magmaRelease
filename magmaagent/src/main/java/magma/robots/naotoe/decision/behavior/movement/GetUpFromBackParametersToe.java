/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.naotoe.decision.behavior.movement;

import magma.robots.nao.decision.behavior.movement.getup.GetUpFromBackParameters;

public class GetUpFromBackParametersToe extends GetUpFromBackParameters
{
	@Override
	protected void setValues()
	{
		super.setValues();

		put(Param.TIME1, 0);
		put(Param.TIME2, 9.863538f);
		put(Param.TIME3, 16.11139f);
		put(Param.TIME4, 23.806534f);
		put(Param.HipYawPitch, -94.805695f);
		put(Param.HipPitch, 97.308495f);
		put(Param.HipRoll, 29.511276f);
		put(Param.KneePitch, -122.90438f);
		put(Param.FootPitch, -55.082344f);
		put(Param.HipPitchSpeed, 5.433256f);
		put(Param.FootPitchSpeed, 1.004598f);
		// Average utility: 2.798 averaged: 10 [ 2.798 -2.025 94.900 ]
	}
}
