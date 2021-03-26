/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception;

/**
 * Thrown if a piece of a server message could not be converted into a perceptor
 *
 * @author Simon Raffeiner
 */
public class PerceptorConversionException extends Exception
{
	/**
	 * Constructor
	 *
	 * @param msg Error message
	 */
	public PerceptorConversionException(String msg)
	{
		super(msg);
	}
}
