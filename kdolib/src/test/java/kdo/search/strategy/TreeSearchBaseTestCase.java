/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.strategy;

import java.util.ArrayList;
import kdo.search.representation.Node;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author KDorer
 *
 *         Tests the BreadthFirstSearch Implementation of the ISearchStrategy
 *         Interface
 */
public abstract class TreeSearchBaseTestCase
{
	// the search nodes to test with
	protected ArrayList<Node> nodes;

	/**
	 * Sets up the test fixture before every call to a test method.
	 */
	@BeforeEach
	protected void setUp() throws Exception
	{
		final int NODE_COUNT = 20;
		nodes = new ArrayList<Node>(NODE_COUNT + 2);

		// create some search nodes
		nodes.add(new Node(null, null, null, 0, 1.0f));

		nodes.add(new Node(null, null, null, 0, 1.0f));

		for (int i = 0; i < NODE_COUNT; i++) {
			nodes.add(new Node(null, null, null, 0, 1.0f));
		}
	}
}
