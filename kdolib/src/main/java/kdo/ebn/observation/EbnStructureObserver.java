/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn.observation;

/**
 * Abstract class with an empty implementation of the valueChanged() method, so
 * that only the structureChanged() method has to be implemented.
 * @author Thomas Rinklin
 *
 */
public abstract class EbnStructureObserver implements IEbnObserver
{
	@Override
	public void valuesChanged()
	{
	}
}
