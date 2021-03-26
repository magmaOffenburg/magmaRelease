/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.staubs.model;

import kdo.util.observer.IObserver;

/**
 *
 * @author Thomas Rinklin
 */
public interface IStaubs {
	/**
	 * returns the dirt
	 * @return level of dirt (between 0 and 1)
	 */
	double getDirt();

	/**
	 * returns the energy
	 * @return level of energy (between 0 and 1)
	 */
	double getEnergy();

	/**
	 * Sets or unsets action to clean
	 * @param clean true if the agent should clean, false if not
	 */
	void clean(boolean clean);

	/**
	 * Sets or unsets action to charge
	 * @param charge true if the agent should charge battery, false if not
	 */
	void charge(boolean charge);

	/**
	 * attaches an model observer
	 * @param newObserver object which wants to be informed
	 */
	void attachModel(IObserver<IStaubs> newObserver);

	/**
	 * detaches an model observer
	 * @param oldObserver object which not wants to be informed no more
	 * @return true if the object was in the list
	 */
	boolean detachModel(IObserver<IStaubs> oldObserver);

	/**
	 * attaches an decisionmaker
	 * @param newObserver object which wants to be informed when it's time to
	 *        decide
	 */
	void attachDecisionMaker(IObserver<IStaubs> newObserver);

	/**
	 * detaches an decisionmaker
	 * @param oldObserver object which not wants to be informed no more
	 * @return true if the object was in the list
	 */
	boolean detachDecisionMaker(IObserver<IStaubs> oldObserver);

	/**
	 * enable or disables the autotick
	 * @param autoTick true to enable, false to disable
	 */
	void setAutoTick(boolean autoTick);

	/**
	 * returns the autotick status
	 * @return true, if autotick is enabled, false if not
	 */
	boolean isAutoTick();

	/**
	 * returns the cleaning status
	 * @return true if the agent is cleaning, false if not
	 */
	boolean isCleaning();

	/**
	 * returns the charging status
	 * @return true if the agent id charging, false if not
	 */
	boolean isCharging();

	/**
	 * do a manual tick
	 */
	void tick();

	double getStress();

	void chill(boolean b);
}
