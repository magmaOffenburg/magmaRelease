/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess;

import java.util.Iterator;
import kdo.ebn.IEBNAction;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnEffect;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnProposition;
import kdo.ebnDevKit.ebnAccess.IEbnResource.IEbnResourceProposition;

/**
 * interface to a competence
 * @author Thomas Rinklin
 */
public interface IEbnCompetence {
	/**
	 * returns the name of the competence
	 * @return the name of the competence
	 */
	String getName();

	double getActivation();

	double getExecutability();

	double getActivationAndExecutability();

	double getMainActivation();

	Iterator<? extends IEbnProposition> getPreconditions();

	Iterator<? extends IEbnEffect> getEffects();

	Iterator<? extends IEbnResourceProposition> getResources();

	/**
	 * checks if the perception is used from the competence
	 * @param perception perception to check
	 * @return true if it's used, false if not
	 */
	boolean perceptionIsUsed(IEbnPerception perception);

	/**
	 * checks if the resource is used from the competence
	 * @param resource resource to check
	 * @return true if it's used, false if not
	 */
	boolean isResourceUsed(IEbnResource resource);

	Iterator<IEBNAction> getActions();
}