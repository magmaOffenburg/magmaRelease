/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.action;

/**
 * Interface for the protocol encoding layer
 * @author kdorer
 */
public interface IMessageEncoder {
	byte[] encodeMessage(IEffectorMap effectors);
}
