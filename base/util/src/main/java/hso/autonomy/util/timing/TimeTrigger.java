/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.timing;

/**
 *
 * @author kdorer
 */
public class TimeTrigger implements Runnable
{
	private long timeout;

	private ITriggerReceiver receiver;

	private String name;

	private boolean stopped;

	/**
	 * @param timeout the timeout in ms
	 */
	public TimeTrigger(String name, ITriggerReceiver receiver, long timeout)
	{
		this.name = name;
		this.receiver = receiver;
		this.timeout = timeout;
		Thread t = new Thread(this, name);
		t.setDaemon(true);
		t.start();
	}

	/** stops the thread with the next cycle */
	public void stop()
	{
		stopped = true;
	}

	@Override
	public void run()
	{
		start();
	}

	/**
	 * Like with a thread you can only start this once.
	 */
	public void start()
	{
		while (!stopped) {
			try {
				// the current implementation does not account for how long the
				// trigger() needs
				Thread.sleep(timeout);
				if (!stopped) {
					// inform the caller
					receiver.trigger(name);
				}
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public String toString()
	{
		return "Timer: " + name + " timeout: " + timeout;
	}
}