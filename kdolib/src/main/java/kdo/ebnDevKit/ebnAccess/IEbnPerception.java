/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess;

import kdo.ebn.IEBNPerception;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnEffect;

/**
 * interface to a perception
 * @author Thomas Rinklin
 */
public interface IEbnPerception {
	/**
	 * returns the name of the perception
	 * @return the name of the perception
	 */
	String getName();

	/**
	 * returns the activation of the perception
	 * @return the activation of the perception
	 */
	double getActivation();

	/**
	 * returns the truth value of the perception
	 * @return the truth value of the perception
	 */
	double getTruthValue();

	/**
	 * creates proposition upon the perception
	 * @param bVal true if the proposition is negated, false if not
	 * @return created proposition
	 */
	IEbnProposition createProposition(boolean bVal);

	/**
	 * factory method to create an effect with this proposition
	 * @param negated if the proposition is negated
	 * @param probability the probability
	 * @return the proposition
	 */
	EbnEffect createEffect(boolean negated, double probability);

	/**
	 * interface to a proposition
	 * @author Thomas Rinklin
	 */
	interface IEbnProposition {
		/**
		 * returns the underlying perception
		 * @return the underlying perception
		 */
		IEbnPerception getPerception();

		/**
		 * @return true if the proposition is negated, false if not
		 */
		boolean isNegated();
	}

	/**
	 * interface to an effect
	 * @author Thomas Rinklin
	 */
	interface IEbnEffect extends IEbnProposition {
		/**
		 * returns the probability
		 * @return the probability
		 */
		double getProbability();
	}

	/**
	 * returns if the passed belief is the underlying belief
	 * @param iBelief belief to check
	 * @return true if the passed belief is the underlying belief, false if not
	 */
	boolean isBelief(IEBNPerception iBelief);
}