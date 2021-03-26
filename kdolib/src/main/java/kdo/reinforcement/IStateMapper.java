/*
 * Copyright (c) 2012 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.reinforcement;

import kdo.domain.IProblemState;

/**
 * @author kdorer
 *
 */
public interface IStateMapper {
	/**
	 * Maps the passed state to a generalized state
	 * @param stateToMap the real state to map
	 * @return a state that is used for learning
	 */
	IProblemState map(IProblemState stateToMap);
}
