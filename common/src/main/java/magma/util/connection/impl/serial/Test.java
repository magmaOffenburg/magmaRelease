/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.connection.impl.serial;

import hso.autonomy.util.connection.ConnectionException;
import hso.autonomy.util.observer.IObserver;
import jssc.SerialPortList;

/**
 * Test drive for SLIP communication over serial port.
 * @author mehdix
 *
 */
public class Test
{
	static SerialConnection comm = null;

	public static void main(String[] args)
	{
		String[] portNames = SerialPortList.getPortNames();
		for (int i = 0; i < portNames.length; i++) {
			System.out.println("Getting list of serial ports");
			System.out.println(portNames[i]);
		}

		// System.out.println("Byte test...");
		// byte end = (byte)0300;
		// System.out.printf("0300 is %d\n", (int)end);//-64
		// System.out.printf("0300&0xFF is %s\n", end&0xFF);//192
		// System.out.printf("%c\n", (char)(end&0xFF));//A with dash on top
		// System.out.printf("%s\n", Character.toString((char)(192&0xFF)));//A
		// with dash on top
		// System.out.printf("%s\n",
		// Integer.toBinaryString(end&0xFF));//11000000
		// System.out.println("***************\n");

		comm = new SerialConnection("/dev/ttyACM0");

		try {
			comm.establishConnection();
			comm.startReceiveLoop();
			sendStartMessage();

		} catch (ConnectionException e) {
			e.printStackTrace();
			System.err.println(e.getCause());
		}

		comm.attach(new Test.EchoObserver());
	}

	protected static void sendStartMessage() throws ConnectionException
	{
		// TODO: temporary, for the current implementation of the controller
		byte ignite[] = new byte[256];
		ignite[0] = (byte) 255;
		ignite[1] = (byte) 255;
		comm.sendMessage(ignite);
	}

	static class EchoObserver implements IObserver<byte[]>
	{
		@Override
		public void update(byte[] content)
		{
			try {
				comm.sendMessage(content);
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
		}
	}
}