/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.connection.impl.serial;

/**
 * This interface is used between jssc lower serial methods and SerialConnection
 * class methods. I don't want to mess whole project with jssc library methods.
 * @author Mahdi Sadeghi
 *
 */
@FunctionalInterface
public interface ISerialEventHandler {
	/**
	 * This method will be called when new bytes are available on serial channel.
	 */
	void handleReceivedBytes(byte buffer[]);
}
