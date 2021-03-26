/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.impl.Effector;
import magma.agent.communication.action.IPassEffector;

public class PassEffector extends Effector implements IPassEffector
{
	public PassEffector()
	{
		super("sendPassCommand");
	}
}
