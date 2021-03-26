/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta;

import magma.agent.model.worldmeta.impl.*;

public class RCFieldConfigurationHelper
{
	public static IRoboCupWorldMetaModel getRCFieldMetalModel(int fieldVersion)
	{
		if (fieldVersion < 2014) {
			fieldVersion = 2014;
		}

		switch (fieldVersion) {
		case 2014:
		case 2015:
		case 2016:
			return HumanoidMetaModelV2014.INSTANCE;
		case 2017:
		case 2018:
			return HumanoidMetaModelV2017.INSTANCE;
		case 2019:
		default:
			return HumanoidMetaModelV2019.INSTANCE;
		}
	}

	public static int[] getAllFieldVersions()
	{
		return new int[] {2019, 2017, 2014};
	}
}
