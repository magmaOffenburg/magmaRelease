/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.staubs;

import kdo.ebnDevKit.EbnDevKitStarter;
import kdo.ebnDevKit.staubs.agent.StaubsAgentCreator;

/**
 * Starter of the EbnDevKit for the staubs agent
 * @author Thomas Rinklin
 *
 */
public class StaubsStarter
{
	public static void main(String[] args)
	{
		new EbnDevKitStarter().start(new StaubsAgentCreator());
	}
}
