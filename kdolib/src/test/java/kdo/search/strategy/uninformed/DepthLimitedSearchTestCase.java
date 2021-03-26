/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Created on 18.09.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package kdo.search.strategy.uninformed;

import static org.junit.jupiter.api.Assertions.assertEquals;

import kdo.search.representation.Node;
import kdo.search.strategy.TreeSearchBaseTestCase;
import kdo.search.strategy.uninformed.DepthLimitedSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author KDorer
 *
 * Tests the depth limited search Implementation of the ISearchStrategy
 * Interface
 */
public class DepthLimitedSearchTestCase extends TreeSearchBaseTestCase
{
	// the instance to use for testing
	DepthLimitedSearch search;

	/**
	 * Sets up the test fixture before every call to a test method.
	 */
	@Override
	@BeforeEach
	protected void setUp() throws Exception
	{
		super.setUp();

		// create a new instance each time
		search = new DepthLimitedSearch(5);
	}

	/**
	 * Test the method addNodes().
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test
	public void testAddNodes() throws Exception
	{
		search.addNodes(nodes);
		assertEquals(nodes.size(), search.getNodes().size(), "Wrong number of nodes");
	}

	/**
	 * Test the method addNodes().
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test
	public void testAddNodesAndGetNextNode() throws Exception
	{
		int size = nodes.size();
		search.addNodes(nodes);

		assertEquals(size, search.getNodes().size(), "Not all nodes stored");
		for (int i = 0; i < nodes.size(); i++) {
			assertEquals(nodes.get(size - i - 1), search.getNextNode(), "Node retrieval in wrong order");
		}
		assertEquals(null, search.getNextNode(), "Did not get null when there are no nodes");
		assertEquals(size, search.getMaxNumberOfNodesStored(), "Did not get proper max nodes");

		// test also when called twice
		search.addNodes(nodes.subList(size - 5, size));
		search.addNodes(nodes.subList(0, size - 5));

		assertEquals(size, search.getNodes().size(), "Not all nodes stored");
		assertEquals(nodes.get(size - 6), search.getNextNode(), "Node retrieval in wrong order");
	}

	/**
	 * Test the method addNodes().
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test
	public void testAddNodesDepthLimit() throws Exception
	{
		int size = nodes.size();
		Node firstNode = nodes.get(0);

		// adding should work
		firstNode.setDepth(4);
		search.addNodes(nodes);
		assertEquals(size, search.getNodes().size(), "Not all nodes stored");

		// adding should work
		firstNode.setDepth(5);
		search.addNodes(nodes);
		assertEquals(2 * size, search.getNodes().size(), "Not all nodes stored");

		// adding should not work
		firstNode.setDepth(6);
		search.addNodes(nodes);
		assertEquals(2 * size, search.getNodes().size(), "Not all nodes stored");
	}
}
