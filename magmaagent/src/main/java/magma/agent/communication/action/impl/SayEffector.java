/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.impl.Effector;
import magma.agent.communication.action.ISayEffector;

/**
 * Implementation of the SimSpark "say" effector, used to communicate with other agents over a simulated voice
 * communication.
 *
 * @author Klaus Dorer
 */
public class SayEffector extends Effector implements ISayEffector
{
	/** The message to shout out to others. */
	private String message;

	public SayEffector()
	{
		this("");
	}

	public SayEffector(String message)
	{
		super("say");

		this.message = message;
	}

	/**
	 * Set the message to be transmitted
	 *
	 * @param message Message string
	 */
	public void setMessage(String message)
	{
		if (message != null) {
			this.message = message;
		}
	}

	public String getMessage()
	{
		return message;
	}
}
