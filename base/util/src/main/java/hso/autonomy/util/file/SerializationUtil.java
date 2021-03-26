/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Utils for serialization tests
 * @author Klaus Dorer
 */
public class SerializationUtil
{
	/**
	 * Serializes the passed object to a byte array and deserializes it again
	 * @param testee the object to serialize
	 * @return the double serialized object
	 * @throws IOException should never happen
	 * @throws ClassNotFoundException if a class may not be deserialized
	 */
	public static Object doubleSerialize(Object testee) throws IOException, ClassNotFoundException
	{
		// write object to byte array (memory)
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(byteOut);
		oos.writeObject(testee);

		// read object from byte array (memory)
		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(byteIn);
		return ois.readObject();
	}

	public static byte[] convertToBytes(Object object) throws IOException
	{
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
			out.writeObject(object);
			return bos.toByteArray();
		}
	}

	public static Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException
	{
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInput in = new ObjectInputStream(bis)) {
			return in.readObject();
		}
	}
}
