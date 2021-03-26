/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn.observation;

/**
 * Abstract class with an empty implementation of the structureChanged() method,
 * so that only the valueChanged() method has to be implemented.
 * @author Thomas Rinklin
 *
 */
public abstract class EbnValueObserver implements IEbnObserver
{
	@Override
	public void structureChanged()
	{
	}
}
