/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.genericAgent;

import java.util.ArrayList;
import java.util.List;
import kdo.ebnDevKit.EbnDevKitStarter;

/**
 * Starts the EBNDevKit for generic agents. gets names of behaviors, beliefs and
 * resources from commandlins
 * @author Thomas Rinklin
 */
public class GenericStarter
{
	public static void main(String[] args)
	{
		List<String> behaviors = new ArrayList<String>();
		List<String> beliefs = new ArrayList<String>();
		List<String> resources = new ArrayList<String>();

		List<String> addingList = null;
		for (String string : args) {
			if (string.equals("-h")) {
				printHelp();
				return;
			} else if (string.equals("-a")) {
				addingList = behaviors;
			} else if (string.equals("-p")) {
				addingList = beliefs;
			} else if (string.equals("-r")) {
				addingList = resources;
			} else {
				if (addingList == null) {
					printHelp();
					return;
				}
				addingList.add(string);
			}
		}

		EbnDevKitStarter starter = new EbnDevKitStarter();
		starter.start(new GenericAgentCreator(behaviors, beliefs, resources));
	}

	private static void printHelp()
	{
		System.out.println("usage: -a behaviorA [behaviorB ...] -p beliefA [beliefB ...] -r resourceA [resourceB ...]");
	}
}
