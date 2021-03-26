/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta;

import magma.agent.model.worldmeta.impl.RCServerMetaModelV62;
import magma.agent.model.worldmeta.impl.RCServerMetaModelV63;
import magma.agent.model.worldmeta.impl.RCServerMetaModelV64;
import magma.agent.model.worldmeta.impl.RCServerMetaModelV66;

public class RCServerConfigurationHelper
{
	public static IRoboCupWorldMetaModel getRCServerMetalModel(int serverVersion)
	{
		if (serverVersion < 62) {
			serverVersion = 62;
		}

		switch (serverVersion) {
		case 62:
			return RCServerMetaModelV62.INSTANCE;
		case 63:
			return RCServerMetaModelV63.INSTANCE;
		case 64:
		case 65:
			return RCServerMetaModelV64.INSTANCE;
		case 66:
			return RCServerMetaModelV66.INSTANCE;
		default:
			return RCServerMetaModelV66.INSTANCE;
		}
	}
}
