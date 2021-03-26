/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.controller;

public class PTController
{
	private float lastOut;

	private float inputFactor;
	private float tau;
	private float tSample;

	public PTController(float inputFactor, float tau, float tSample)
	{
		this.inputFactor = inputFactor;
		this.tau = tau;
		this.tSample = tSample;
		lastOut = 0;
	}

	public void init()
	{
		lastOut = 0f;
	}

	public void setInputFactor(float factor)
	{
		inputFactor = factor;
	}

	/**
	 *
	 *********************************
	 *
	 * PT 1 discrete algorithm
	 *
	 *               Tau
	 *       In +    ---  * LastOut
	 *             Tsample
	 * Out = ---------------------
	 *               Tau
	 *       1 +     ---
	 *             Tsample
	 *
	 *                           Tau
	 * here with     Factor =    ---
	 *                         Tsample
	 *
	 *******************************************
	 */
	public float getOutput(float delta)
	{
		float gain = tau / tSample;
		float scaledError = delta * inputFactor;
		float out = (scaledError + gain * lastOut) / (1 + gain);
		lastOut = out;
		return out;
	}
}
