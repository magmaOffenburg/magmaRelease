/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.connection.impl;

import hso.autonomy.util.misc.ByteUtil;

/**
 *
 * @author kdorer
 */
public class TCPIPTestMessageCreator
{
	public static void printMessage(byte[] body, boolean verbose)
	{
		int value = ByteUtil.getIntBigEndian(body, 4);
		float floatValue = ByteUtil.getFloatBigEndian(body, 16);
		if (verbose) {
			System.out.println("Message: " + value + " with Number: " + floatValue);
		}
	}

	public static byte[] createAnyMessage(int howManyInts)
	{
		int counter = 0;
		int length = howManyInts * 4;
		byte[] body = new byte[length];
		for (int i = 0; i < howManyInts; i++) {
			counter = addInt32(body, counter, length);
		}
		return body;
	}

	public static byte[] createServerMessage(long count)
	{
		int counter = 0;
		float value = 3.1415f;
		int length = 1348;
		byte[] body = new byte[length];
		counter = addInt32(body, counter, length);
		counter = addInt32(body, counter, (int) count);
		counter = addInt32(body, counter, length);

		// Motors
		for (int i = 0; i < 28; i++) {
			counter = addInt32(body, counter, length);
			for (int j = 0; j < 10; j++) {
				counter = addFloat32(body, counter, value);
			}
		}

		// Sensor
		counter = addInt32(body, counter, length);
		for (int j = 0; j < 25; j++) {
			counter = addFloat32(body, counter, value);
		}

		return body;
	}

	public static byte[] createClientMessage(int count)
	{
		int counter = 0;
		float value = 3.1415f;
		int length = 472;
		byte[] body = new byte[length];
		counter = addInt32(body, counter, length);
		counter = addInt32(body, counter, count);
		counter = addInt32(body, counter, 0);
		counter = addInt32(body, counter, 0);
		counter = addInt32(body, counter, 0);
		counter = addInt32(body, counter, 0);

		// Motors
		for (int i = 0; i < 28; i++) {
			for (int j = 0; j < 4; j++) {
				counter = addFloat32(body, counter, value);
			}
		}

		return body;
	}

	private static int addInt32(byte[] body, int counter, int value)
	{
		ByteUtil.encodeIntBigEndian(body, counter, value);
		return counter + 4;
	}

	private static int addFloat32(byte[] body, int counter, float value)
	{
		ByteUtil.encodeFloatBigEndian(body, counter, value);
		return counter + 4;
	}
}
