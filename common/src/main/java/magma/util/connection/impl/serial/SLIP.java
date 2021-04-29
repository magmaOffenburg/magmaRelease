/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.connection.impl.serial;

import java.util.ArrayList;

/**
 * SLIP protocol implementation
 * @author Mahdi Sadeghi
 *
 */
public class SLIP
{
	private static final byte SLIP_END = (byte) 0300;

	private static final byte SLIP_ESC = (byte) 0333;

	private static final byte SLIP_ESC_END = (byte) 0334;

	private static final byte SLIP_ESC_ESC = (byte) 0335;

	private static final byte DEBUG_MAKER = (byte) 0015;

	// private final int MAX_MTU = 200;

	/**
	 * Encode the bytes according to SLIP protocol
	 */
	public static ArrayList<Byte> encodeToSLIP(byte buffer[])
	{
		ArrayList<Byte> temp = new ArrayList<>();
		// System.out.println("Appending start");
		temp.add(SLIP_END);
		for (byte b : buffer) {
			if (b == SLIP_END) {
				temp.add(SLIP_ESC);
				temp.add(SLIP_ESC_END);
			} else if (b == SLIP_ESC) {
				temp.add(SLIP_ESC);
				temp.add(SLIP_ESC_ESC);
			} else {
				temp.add(b);
			}
		}
		temp.add(SLIP_END);
		return temp;
	}

	/**
	 * Decode the give bytes into normal byte stream.
	 * @throws SLIPException
	 */
	public static ArrayList<Byte> decodeFromSLIP(byte buffer[]) throws SLIPException
	{
		ArrayList<Byte> decoded = new ArrayList<>();

		boolean esc = false;

		for (byte b : buffer) {
			// First check if we should check next byte of an escape byte stuffing
			if (esc) {
				if (b == SLIP_ESC_END) {
					decoded.add(SLIP_END);
				} else if (b == SLIP_ESC_ESC) {
					decoded.add(SLIP_ESC);
				} else if (b == DEBUG_MAKER) {
					decoded.add(DEBUG_MAKER);
				} else {
					// If none of above happens we have a protocol error,
					// because there is no other case.
					throw new SLIPException("Protocol error. Expected byte after SLIP_ESC");
				}

				// Reset the flag at the end and continue the loop for next byte
				esc = false;
				continue;

			} else if (b == SLIP_END) {
				// If we have reached the end flag return the bytes
				if (decoded.size() > 0) {
					return decoded;
				}
			} else if (b == SLIP_ESC) {
				// If we have reached to an escape byte set the flag and continue
				esc = true;
				continue;
			} else {
				// If this byte is not byte stuffing and just a normal one, just add
				// it.
				decoded.add(b);
			}
		}

		// This means that the bytes are finished while still expecting next byte
		if (esc) {
			throw new SLIPException("Protocol error. Expected byte after SLIP_ESC");
		}

		return decoded;
	}

	/**
	 * Method for decoding SLIP encoded messages from a stream of bytes.
	 */
	public static byte[] decodeFromStream(ArrayList<Byte> decoded, byte[] buffer)
	{
		boolean esc = false;
		boolean startedDecoding = false;
		byte b;

		for (int i = 0; i < buffer.length; i++) {
			b = buffer[i];

			// First check if we should check next byte of an escape byte stuffing
			if (esc) {
				if (b == SLIP_ESC_END) {
					decoded.add(SLIP_END);
				} else if (b == SLIP_ESC_ESC) {
					decoded.add(SLIP_ESC);
				} else if (b == DEBUG_MAKER) {
					decoded.add(DEBUG_MAKER);
				} else {
					// If none of above happens we have a protocol error,
					// because there is no other case.
					System.out.println("WARINGGG!!!!");
				}

				// Reset the flag at the end and continue the loop for next byte
				esc = false;
				continue;

			} else if (b == SLIP_END) {
				// If we have reached the end flag return the bytes
				if (startedDecoding && decoded.size() > 0) {
					// SLIP_END is END of message
					int size = buffer.length - i - 1;
					byte[] ret = new byte[size];
					System.arraycopy(buffer, i + 1, ret, 0, size);
					return ret;
				} else {
					// SLIP_END is START of message
					startedDecoding = true;
				}
			} else if (b == SLIP_ESC) {
				// If we have reached to an escape byte set the flag and continue
				esc = true;
				continue;
			} else {
				// If this byte is not byte stuffing and just a normal one, just add
				// it.
				if (startedDecoding) {
					decoded.add(b);
				}
			}
		}

		if (startedDecoding) {
			decoded.clear();
		}

		return buffer;
	}
}