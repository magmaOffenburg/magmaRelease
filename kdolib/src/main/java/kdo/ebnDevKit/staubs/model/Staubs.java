/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.staubs.model;

import kdo.util.observer.IObserver;
import kdo.util.observer.IPublishSubscribe;
import kdo.util.observer.Subject;

/**
 * Implementation of the staubs domain
 * @author Thomas Rinklin
 *
 */
public class Staubs implements IStaubs // , IObserver<IStaubs>
{
	/** dirt level */
	private double dirt;

	/** energy level */
	private double energy;

	/** stress level */
	private double stress;

	/** list of model observers */
	private final transient IPublishSubscribe<IStaubs> modelObserver;

	/** list of decide time observers */
	private final transient IPublishSubscribe<IStaubs> tickObserver;

	/** autotick tread */
	private AutoTicker autoTicker;

	/** autotick status */
	private boolean autoTick = false;

	/** cleaning status */
	private boolean cleaning = false;

	/** chargin status */
	private boolean charging = false;

	/** chargin status */
	private boolean chilling = false;

	/**
	 * Default constructor
	 */
	public Staubs()
	{
		autoTicker = new AutoTicker();
		modelObserver = new Subject<IStaubs>();
		tickObserver = new Subject<IStaubs>();
		dirt = 0.1;
		energy = 0;
	}

	@Override
	public void tick()
	{
		tickObserver.onStateChange(this);
		contaminate();
		clean();
		charge();
		chill();
		modelObserver.onStateChange(this);
	}

	/**
	 * lets the room getting more dirty
	 */
	private void contaminate()
	{
		dirt += (1.0 - dirt) * Math.random() * 0.02;
	}

	/** cleans the room */
	private void clean()
	{
		if (cleaning) {
			if (energy > 0.1) {
				if (dirt > 0) {
					dirt /= 2.0;
				}
				energy -= 0.1;
			}
		}
	}

	/** charges the battery */
	private void charge()
	{
		if (charging) {
			energy += 0.02;
			if (energy > 1.0)
				energy = 1.0;
		}
	}

	/** chill */
	private void chill()
	{
		if (chilling) {
			stress -= 0.02;
			if (stress < 0)
				stress = 0;

		} else {
			stress += 0.01;
			if (stress > 1)
				stress = 1;
		}
	}

	@Override
	public void clean(boolean clean)
	{
		if (clean == cleaning)
			return;

		cleaning = clean;

		if (cleaning) {
			charging = false;
			chilling = false;
		}

		modelObserver.onStateChange(this);
	}

	@Override
	public void charge(boolean charge)
	{
		if (charge == charging)
			return;
		charging = charge;

		if (charging) {
			cleaning = false;
			chilling = false;
		}

		modelObserver.onStateChange(this);
	}

	@Override
	public void chill(boolean chill)
	{
		if (chill == chilling)
			return;
		chilling = chill;

		if (chilling) {
			cleaning = false;
			charging = false;
		}

		modelObserver.onStateChange(this);
	}

	@Override
	public double getDirt()
	{
		return dirt;
	}

	@Override
	public double getEnergy()
	{
		return energy;
	}

	@Override
	public double getStress()
	{
		return stress;
	}

	@Override
	public void attachModel(IObserver<IStaubs> newObserver)
	{
		modelObserver.attach(newObserver);
	}

	@Override
	public boolean detachModel(IObserver<IStaubs> oldObserver)
	{
		return modelObserver.detach(oldObserver);
	}

	@Override
	public void setAutoTick(boolean autoTick)
	{
		if (this.autoTick == autoTick)
			return;
		this.autoTick = autoTick;
		if (autoTick) {
			autoTicker.start();
		} else {
			autoTicker.stopTicking();
			try {
				autoTicker.join();
				autoTicker = new AutoTicker();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isAutoTick()
	{
		return autoTick;
	}

	@Override
	public boolean isCleaning()
	{
		return cleaning;
	}

	@Override
	public boolean isCharging()
	{
		return charging;
	}

	/**
	 * autotick tread
	 * @author Thomas Rinklin
	 *
	 */
	private class AutoTicker extends Thread
	{
		int sleepTime = 0;

		boolean action;

		public AutoTicker()
		{
			this(300);
		}

		public AutoTicker(int sleepTime)
		{
			this.sleepTime = sleepTime;
		}

		@Override
		public void run()
		{
			action = true;
			while (action) {
				tick();
				try {
					sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void stopTicking()
		{
			action = false;
		}
	}

	@Override
	public void attachDecisionMaker(IObserver<IStaubs> newObserver)
	{
		tickObserver.attach(newObserver);
	}

	@Override
	public boolean detachDecisionMaker(IObserver<IStaubs> oldObserver)
	{
		return tickObserver.detach(oldObserver);
	}
}
