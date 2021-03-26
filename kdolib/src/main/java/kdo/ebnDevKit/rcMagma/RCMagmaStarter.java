/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.rcMagma;

import kdo.ebnDevKit.EbnDevKitStarter;

/**
 * starter of the ebnDevKit to use it with magma robocup agents
 * @author Thomas Rinklin
 *
 */
public class RCMagmaStarter
{
	public static void main(String[] args)
	{
		EbnDevKitStarter starter = new EbnDevKitStarter();
		starter.start(new RCMagmaAgentCreator());
	}
}
