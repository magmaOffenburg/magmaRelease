/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.connection.impl;

import hso.autonomy.util.connection.ConnectionException;
import hso.autonomy.util.connection.impl.ConnectionBase;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbServices;

/**
 *
 * @author kdorer
 */
public class USBConnection extends ConnectionBase
{
	/**
	 * Dumps the name of the specified device to stdout.
	 *
	 * @param device The USB device.
	 * @throws UnsupportedEncodingException When string descriptor could not be
	 *         parsed.
	 * @throws UsbException When string descriptor could not be read.
	 */
	private static void dumpName(final UsbDevice device) throws UnsupportedEncodingException, UsbException
	{
		// Read the string descriptor indices from the device descriptor.
		// If they are missing then ignore the device.
		final UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
		final byte iManufacturer = desc.iManufacturer();
		final byte iProduct = desc.iProduct();
		if (iManufacturer == 0 || iProduct == 0)
			return;

		// Dump the device name
		// System.out.println(device.getString(iManufacturer) + " "
		// + device.getString(iProduct));
		System.out.format("%04x:%04x%n", desc.idVendor() & 0xffff, desc.idProduct() & 0xffff);
	}

	/**
	 * Processes the specified USB device.
	 *
	 * @param device The USB device to process.
	 */
	@SuppressWarnings("unchecked")
	private static void processDevice(final UsbDevice device)
	{
		// When device is a hub then process all child devices
		if (device.isUsbHub()) {
			final UsbHub hub = (UsbHub) device;
			((List<UsbDevice>) hub.getAttachedUsbDevices()).forEach(USBConnection::processDevice);
		}

		// When device is not a hub then dump its name.
		else {
			try {
				dumpName(device);
			} catch (Exception e) {
				// On Linux this can fail because user has no write permission
				// on the USB device file. On Windows it can fail because
				// no libusb device driver is installed for the device
				System.err.println("Ignoring problematic device: " + e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Main method.
	 *
	 * @param args Command-line arguments (Ignored)
	 * @throws UsbException When an USB error was reported which wasn't handled
	 *         by this program itself.
	 */
	public static void main(final String[] args) throws UsbException
	{
		// Get the USB services and dump information about them
		final UsbServices services = UsbHostManager.getUsbServices();

		// Dump the root USB hub
		processDevice(services.getRootUsbHub());
	}

	// private static void readConfigurationNumber(UsbDevice device)
	// throws IllegalArgumentException, UsbDisconnectedException,
	// UsbException
	// {
	// UsbControlIrp irp = device
	// .createUsbControlIrp(
	// (byte) (UsbConst.REQUESTTYPE_DIRECTION_IN
	// | UsbConst.REQUESTTYPE_TYPE_STANDARD |
	// UsbConst.REQUESTTYPE_RECIPIENT_DEVICE),
	// UsbConst.REQUEST_GET_CONFIGURATION, (short) 0, (short) 0);
	// irp.setData(new byte[1]);
	// device.syncSubmit(irp);
	// System.out.println(irp.getData()[0]);
	// }

	@Override
	public void establishConnection() throws ConnectionException
	{
		throw new ConnectionException("Do not call this method in USBConnection");
	}

	@Override
	public void sendMessage(byte[] msg) throws ConnectionException
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void startReceiveLoop() throws ConnectionException
	{
	}
}
