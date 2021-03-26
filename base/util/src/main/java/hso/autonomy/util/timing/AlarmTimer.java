/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.timing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author kdorer
 */
public class AlarmTimer implements Runnable
{
	private static final ExecutorService pool = Executors.newCachedThreadPool(r -> {
		Thread t = Executors.defaultThreadFactory().newThread(r);
		t.setDaemon(true);
		return t;
	});

	private long timeout;

	private boolean wokeUp;

	private ITriggerReceiver sleeper;

	private String name;

	public AlarmTimer(String name, ITriggerReceiver sleeper, long timeout)
	{
		this.name = name;
		this.sleeper = sleeper;
		this.timeout = timeout;
		wokeUp = false;
		pool.submit(this);
	}

	public synchronized void stopAlarm()
	{
		wokeUp = true;
		notify();
	}

	@Override
	public synchronized void run()
	{
		boolean finished = false;

		do {
			final long alarmTime = System.currentTimeMillis() + timeout;
			long currentTime;
			while (!wokeUp && alarmTime > (currentTime = System.currentTimeMillis())) {
				try {
					wait(alarmTime - currentTime);
				} catch (InterruptedException e) {
				}
			}
			if (!wokeUp) {
				// inform the caller
				finished = sleeper.trigger(AlarmTimer.this.name);
			}
		} while (!wokeUp && !finished);
	}

	@Override
	public String toString()
	{
		return "Alarm: " + name + " timeout: " + timeout;
	}
}