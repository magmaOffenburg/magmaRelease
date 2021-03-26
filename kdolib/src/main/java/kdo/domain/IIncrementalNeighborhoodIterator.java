/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */

package kdo.domain;

import java.util.Iterator;

/**
 * Iterators that allow for incremental neighborhoods. An incremental
 * neighborhood is one in which after all operators of one neighborhood have
 * been returned by next() it allows to change to a different neighborhood by
 * calling nextNeighborhood().
 */
public interface IIncrementalNeighborhoodIterator<E> extends Iterator<E> {
	boolean nextNeighborhood();
}
