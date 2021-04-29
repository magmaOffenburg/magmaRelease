/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.misc;

/**
 *
 * @author kdorer
 */
public class ByteUtil
{
	public static float getFloatBigEndian(byte[] buffer, int index)
	{
		return Float.intBitsToFloat(
				getIntFromBytes(buffer[index + 3], buffer[index + 2], buffer[index + 1], buffer[index]));
	}

	public static int encodeFloatBigEndian(byte[] buffer, int index, float value)
	{
		return encodeIntBigEndian(buffer, index, Float.floatToRawIntBits(value));
	}

	public static int encodeFloatBigEndian(byte[] buffer, int index, double value)
	{
		return encodeFloatBigEndian(buffer, index, (float) value);
	}

	public static int getIntBigEndian(byte[] buffer, int index)
	{
		return getIntFromBytes(buffer[index + 3], buffer[index + 2], buffer[index + 1], buffer[index]);
	}

	public static int encodeIntBigEndian(byte[] buffer, int index, int value)
	{
		buffer[index++] = getLLByte(value);
		buffer[index++] = getLHByte(value);
		buffer[index++] = getHLByte(value);
		buffer[index++] = getHHByte(value);
		return index;
	}

	/**
	 * Converts the four bytes into a int
	 * @param hhByte the high-high byte
	 * @param hlByte the high-low byte
	 * @param lhByte the low-high byte
	 * @param llByte the low-low byte
	 * @return a int resembled from the four bytes passed
	 */
	public static int getIntFromBytes(byte hhByte, byte hlByte, byte lhByte, byte llByte)
	{
		return (hhByte & 0xFF) << 24 | (hlByte & 0xFF) << 16 | (lhByte & 0xFF) << 8 | (llByte & 0xFF);
	}

	/**
	 * @param value the value to return the high-high-byte from
	 * @return gets the high byte of the short passed
	 */
	public static byte getHHByte(int value)
	{
		return (byte) (value >>> 24);
	}

	/**
	 * @param value the value to return the high-low-byte from
	 * @return gets the high byte of the short passed
	 */
	public static byte getHLByte(int value)
	{
		return (byte) (value >>> 16);
	}

	/**
	 * @param value the value to return the low-high-byte from
	 * @return gets the high byte of the short passed
	 */
	public static byte getLHByte(int value)
	{
		return (byte) (value >>> 8);
	}

	/**
	 * @param value the value to return the low-low-byte from
	 * @return gets the low byte of the short passed
	 */
	public static byte getLLByte(int value)
	{
		return (byte) value;
	}

	/**
	 * Converts the two bytes into a short
	 * @param highByte the high byte
	 * @param lowByte the low byte
	 * @return a short resembled from the low and high byte passed
	 */
	public static short getShortFromBytes(byte highByte, byte lowByte)
	{
		return (short) ((highByte & 0xFF) << 8 | (lowByte & 0xFF));
	}

	/**
	 * @param value the value to return the high byte from
	 * @return gets the high byte of the short passed
	 */
	public static byte getHighByte(short value)
	{
		return (byte) (value >>> 8);
	}

	/**
	 * @param value the value to return the low byte from
	 * @return gets the low byte of the short passed
	 */
	public static byte getLowByte(short value)
	{
		return (byte) value;
	}

	/**
	 * @return true if the second byte array is found in the first byte array
	 */
	public static boolean findBytes(byte[] arrayToSearch, byte[] bytesToFind)
	{
		for (int i = 0; i <= arrayToSearch.length - bytesToFind.length; i++) {
			int j = 0;
			for (; j < bytesToFind.length; j++) {
				if (arrayToSearch[i + j] != bytesToFind[j]) {
					break;
				}
			}
			if (j == bytesToFind.length) {
				return true;
			}
		}
		return false;
	}

	public static void printBytes(byte[] data)
	{
		printBytes(data, 50);
	}

	public static void printBytes(byte[] data, int maxAmount)
	{
		for (int i = 0; i < data.length && i < maxAmount; i++) {
			System.out.print("" + data[i] + ",");
		}
		if (maxAmount < data.length) {
			System.out.println("...");
		} else {
			System.out.println();
		}
	}
}
