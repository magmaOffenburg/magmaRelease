/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.connection.impl.serial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class SLIPTest
{
	@Test
	public void testEncodeDecode()
	{
		// Original message
		String message = "Not yet implemented";

		// Encode the message
		ArrayList<Byte> encoded = SLIP.encodeToSLIP(message.getBytes());

		byte[] encodedBytes = new byte[encoded.size()];

		for (int i = 0; i < encodedBytes.length; i++) {
			encodedBytes[i] = encoded.get(i);
		}

		try {
			// Decode the message from encoded bytes
			ArrayList<Byte> decoded = SLIP.decodeFromSLIP(encodedBytes);

			byte[] decodedBytes = new byte[decoded.size()];
			for (int i = 0; i < decodedBytes.length; i++) {
				decodedBytes[i] = decoded.get(i);
			}

			String decodedString = new String(decodedBytes);

			// The decoded value should be the same as encoded value
			assertEquals(message, decodedString);

		} catch (SLIPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Failed with exception" + e.toString());
		}
	}

	@Test
	public void testEncodingWithByteStuffing()
	{
		byte[] message = new byte[5];
		message[0] = "a".getBytes()[0];
		message[1] = (byte) 0300; // Slip end as part of the message
		message[2] = "z".getBytes()[0];
		message[3] = (byte) 0333; // Slip esc as part of the message
		message[4] = "d".getBytes()[0];

		// Encode the message
		ArrayList<Byte> encoded = SLIP.encodeToSLIP(message);

		assertEquals(encoded.get(0).byteValue(), (byte) 0300);
		assertEquals(encoded.get(encoded.size() - 1).byteValue(), (byte) 0300);

		// Two escape characters plus two starting and ending characters being
		// added to message
		assertEquals(encoded.size(), 9);
	}

	@Test
	public void testDecodingWithByteStuffing()
	{
		// Making a sample SLIP message with escape characters
		byte[] message = new byte[9];

		// END in the beginning
		message[0] = (byte) 0300; // Slip end as part of the message

		// Normal data
		message[1] = "a".getBytes()[0];

		// END as part of the message
		message[2] = (byte) 0333; // Slip esc as part of the message
		message[3] = (byte) 0334; // Slip EscEnd as part of the message

		// Another normal data
		message[4] = "z".getBytes()[0];

		// ESC as part of the message
		message[5] = (byte) 0333; // Slip esc as part of the message
		message[6] = (byte) 0335; // Slip EscEsc as part of the message

		// Last normal data
		message[7] = "d".getBytes()[0];

		// END at the end
		message[8] = (byte) 0300; // Slip end as part of the message

		// Making a sample SLIP message with escape characters
		byte[] expectedMessage = new byte[5];

		// Normal data
		expectedMessage[0] = "a".getBytes()[0];

		// END as part of the message
		expectedMessage[1] = (byte) 0300; // Slip end as part of the message

		// Another normal data
		expectedMessage[2] = "z".getBytes()[0];

		// ESC as part of the message
		expectedMessage[3] = (byte) 0333; // Slip esc as part of the message

		// Last normal data
		expectedMessage[4] = "d".getBytes()[0];

		try {
			// Decode the message from encoded bytes
			ArrayList<Byte> decoded = SLIP.decodeFromSLIP(message);

			// First of all the length should be 9 - 4 = 5, because of removing two
			// END and two ESCs
			assertEquals(decoded.size(), 5);

			byte[] decodedBytes = new byte[decoded.size()];
			for (int i = 0; i < decodedBytes.length; i++) {
				decodedBytes[i] = decoded.get(i);
			}

			// The decoded value should be the same as encoded value
			for (int i = 0; i < decodedBytes.length; i++) {
				assertEquals(decodedBytes[i], expectedMessage[i]);
			}

		} catch (SLIPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Failed with exception" + e.toString());
		}
	}
}
