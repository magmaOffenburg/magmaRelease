/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess;

import kdo.ebn.IResourceBelief;

/**
 * interface to a resource
 * @author Thomas Rinklin
 */
public interface IEbnResource {
	/**
	 * checks if this resource wraps the passed resource belief
	 * @param iResourceBelief resource belief to check
	 * @return true if the resource belief matches to this resource, false if not
	 */
	boolean isResource(IResourceBelief iResourceBelief);

	/**
	 * returns the name of the resource
	 * @return the name of the resource
	 */
	String getName();

	/**
	 * factory method to create a resource proposition
	 * @param amountUsed the the resource amount used by the proposition
	 * @return the created resource proposition
	 */
	IEbnResourceProposition creatProposition(int amountUsed);

	int getAmountAvailable();

	int getMaximalAmount();

	/**
	 * interface to a resource proposition
	 * @author Thomas Rinklin
	 */
	interface IEbnResourceProposition {
		/**
		 * checks if this resource proposition matches to a resource
		 * @param resource resource to check
		 * @return true if the resource matches to this resource proposition
		 */
		boolean isResource(IEbnResource resource);

		/**
		 * returns the underlying resource
		 * @return the underlying resource
		 */
		IEbnResource getResource();

		/**
		 * returns the amount of resource which this proposition uses
		 * @return the amount of resource which this proposition uses
		 */
		int getAmountUsed();
	}
}