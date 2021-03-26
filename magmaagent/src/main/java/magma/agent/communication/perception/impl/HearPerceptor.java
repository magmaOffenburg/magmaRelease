/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.impl.Perceptor;
import magma.agent.communication.perception.IHearPerceptor;

/**
 * Hear Perceptor
 *
 * @author Ingo Schindler
 * @author Simon Raffeiner
 *
 */
class HearPerceptor extends Perceptor implements IHearPerceptor
{
	// Time the message was heard
	private float time;

	// Target the message is directed to
	private String target;

	// The actual message
	private String message;

	private String team;

	/**
	 * Default constructor, nothing heard
	 */
	public HearPerceptor()
	{
		this(0.00f, "", "", "");
	}

	public HearPerceptor(float time, String team, String target, String message)
	{
		super("hear" + time + target);

		this.time = time;
		this.team = team;
		this.target = target;
		this.message = message;
	}

	@Override
	public String getTeam()
	{
		return this.team;
	}

	@Override
	public float getTime()
	{
		return this.time;
	}

	@Override
	public String getTarget()
	{
		return this.target;
	}

	@Override
	public String getMessage()
	{
		return this.message;
	}

	/**
	 * Sets back message to be empty
	 */
	public void init()
	{
		message = "";
	}
}
