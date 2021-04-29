/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.connection.impl.serial;

import hso.autonomy.util.connection.ConnectionException;
import hso.autonomy.util.connection.impl.ConnectionBase;
import java.util.ArrayList;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 * Communicates with give serial port to send/receive messages to the Sweety
 * board according the the SLIP protocol.
 * @author Mahdi Sadeghi
 *
 */
public class SerialConnection extends ConnectionBase
{
	/** The instance of serial port. */
	protected SerialPort serialPort;

	private String portName;

	public SerialConnection(String portName)
	{
		super();
		this.portName = portName;
		serialPort = null;
	}

	/*
	 * In this class must implement the method serialEvent, through it we learn
	 * about events that happened to our port. But we will not report on all
	 * events but only those that we put in the mask. In this case the arrival of
	 * the data and change the status lines CTS and DSR
	 *
	 * Reference:
	 * https://code.google.com/p/java-simple-serial-connector/wiki/jSSC_examples
	 */
	private class SerialPortReader implements SerialPortEventListener
	{
		// To keep the up stream handler class
		private ISerialEventHandler handler = null;

		// Timing instance, only for monitoring
		private final TimingMonitor timingMonitor = new TimingMonitor(true);

		SerialPortReader(ISerialEventHandler handler)
		{
			super();
			this.handler = handler;
		}

		@Override
		public void serialEvent(SerialPortEvent event)
		{
			int eventValue = event.getEventValue();

			timingMonitor.doBeforeHandling(eventValue);

			byte buffer[] = null;

			// If data is available read available data from the port
			if (event.isRXCHAR()) {
				try {
					buffer = serialPort.readBytes(eventValue);

					this.handler.handleReceivedBytes(buffer);

				} catch (SerialPortException ex) {
					System.err.println(ex);
				}
			} else if (event.isCTS()) { // If CTS line has changed state
				if (eventValue == 1) {	// If line is ON
					System.err.println("CTS - ON");
				} else {
					System.err.println("CTS - OFF");
				}
			} else if (event.isDSR()) { // /If DSR line has changed state
				if (eventValue == 1) {	// If line is ON
					System.err.println("DSR - ON");
				} else {
					System.err.println("DSR - OFF");
				}
			}

			// Call timing after handling
			this.timingMonitor.doAfterHandling();
		}
	}

	/**
	 * A nested class to use as serial input data handler
	 * @author Mahdi Sadeghi
	 *
	 */
	class SerialEventHandler implements ISerialEventHandler
	{
		private byte[] remaining = null;

		@Override
		public void handleReceivedBytes(byte[] buffer)
		{
			// try {
			// // Decode SLIP message
			// ArrayList<Byte> decoded = SLIP.decodeFromSLIP(buffer);

			byte[] fullBuffer;
			if (remaining != null && remaining.length > 0) {
				fullBuffer = new byte[remaining.length + buffer.length];
				System.arraycopy(remaining, 0, fullBuffer, 0, remaining.length);
				System.arraycopy(buffer, 0, fullBuffer, remaining.length, buffer.length);
			} else {
				fullBuffer = buffer;
			}

			remaining = fullBuffer;

			ArrayList<Byte> decoded = new ArrayList<>();
			do {
				decoded.clear();

				remaining = SLIP.decodeFromStream(decoded, remaining);

				if (decoded.size() > 0) {
					// Convert to byte array
					byte[] decodedBuffer = new byte[decoded.size()];
					for (int i = 0; i < decodedBuffer.length; i++) {
						decodedBuffer[i] = decoded.get(i).byteValue();
					}

					// System.out.println(Arrays.toString(decodedBuffer));

					// Inform all about received data, pass over the decoded data
					observer.onStateChange(decodedBuffer);
				}
			} while (decoded.size() > 0);

			// } catch (SLIPException e) {
			// // TODO Auto-generated catch block
			//
			// System.out.printf("We got an exception %s", e.toString());
			// e.printStackTrace();
			// }
		}
	}

	@Override
	public void establishConnection() throws ConnectionException
	{
		try {
			// Initialize the serial port instance
			serialPort = new SerialPort(portName);

			// Open port
			serialPort.openPort();

			/*
			 * Setting parameters for serial port. The baudrade value should be
			 * reconsidered according to whatever is being used in the other end.
			 * Still unclear for myself.
			 *
			 * In the old C client code the rate is 1000000 which sounds weird to
			 * me.
			 */
			serialPort.setParams(SerialPort.BAUDRATE_115200, 8, 1, 0);

			// Prepare mask
			int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;

			// Set mask
			serialPort.setEventsMask(mask);

			// Add SerialPortEventListener
			serialPort.addEventListener(new SerialPortReader(new SerialEventHandler()));

			// Now we are connected to the port
			this.connected = true;

		} catch (SerialPortException e) {
			e.printStackTrace();
			throw new ConnectionException(e.getMessage(), e);
		}
	}

	@Override
	/**
	 * This method writes the given bytes to the serial port.
	 * @param buffer: the input buffer
	 * @throws SerialPortException
	 */
	public void sendMessage(byte[] msg) throws ConnectionException
	{
		// System.out.println("sending: " + Arrays.toString(msg));
		ArrayList<Byte> encoded = SLIP.encodeToSLIP(msg);

		byte[] sendBuffer = new byte[encoded.size()];

		for (int i = 0; i < sendBuffer.length; i++) {
			sendBuffer[i] = encoded.get(i).byteValue();
		}

		try {
			serialPort.writeBytes(sendBuffer);
		} catch (SerialPortException e) {
			e.printStackTrace();
			throw new ConnectionException(e.getMessage(), e);
		}
	}

	@Override
	public void startReceiveLoop() throws ConnectionException
	{
		// nothing to do
	}

	/**
	 * Stops the loop of receiving messages and notifying observers after
	 * receiving the next message
	 */
	@Override
	public void stopReceiveLoop()
	{
		super.stopReceiveLoop();
		try {
			if (serialPort != null) {
				serialPort.closePort();
				connected = false;
				serialPort = null;
			}
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}
}
