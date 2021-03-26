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

import java.util.ArrayList;
import kdo.search.representation.Node;
import kdo.search.strategy.TreeSearchBaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author KDorer
 *
 * Tests the BreadthFirstSearch Implementation of the ISearchStrategy Interface
 */
public class IterativeDeepeningSearchTestCase extends TreeSearchBaseTestCase
{
	// the instance to use for testing
	IterativeDeepeningSearch search;

	/**
	 * Sets up the test fixture before every call to a test method.
	 */
	@Override
	@BeforeEach
	protected void setUp() throws Exception
	{
		super.setUp();

		// create a new instance each time
		search = new IterativeDeepeningSearch();
	}

	/**
	 * Test the method addNodes().
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test
	public void testAddNodesEmptyList() throws Exception
	{
		search.addNodes(new ArrayList<Node>());
		assertEquals(0, search.getNodes().size(), "List should be empty");

		search.addNodes(nodes);
		assertEquals(nodes.size(), search.getNodes().size(), "Wrong number of nodes");

		search.addNodes(new ArrayList<Node>());
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
		firstNode.setDepth(1);
		search.addNodes(nodes);
		assertEquals(size, search.getNodes().size(), "Not all nodes stored");

		// adding should not work
		firstNode.setDepth(2);
		search.addNodes(nodes);
		assertEquals(2 * size - 1, search.getNodes().size(), "Node 1 should not be stored");
	}

	/**
	 * Test the iteration addNodes().
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test
	public void testAddNodesDepthIteration() throws Exception
	{
		Node rootNode = nodes.remove(0);
		search.addRootNode(rootNode);

		int size = nodes.size();
		Node firstNode = nodes.get(0);

		// adding should work
		search.addNodes(nodes);
		assertEquals(size + 1, search.getNodes().size(), "Not all nodes stored");

		// adding should not work
		firstNode.setDepth(2);
		ArrayList<Node> newNodes = new ArrayList<Node>();
		newNodes.add(firstNode);
		search.addNodes(newNodes);
		assertEquals(size + 1, search.getNodes().size(), "Node 1 should not be stored");

		for (int i = 0; i < size; i++) {
			assertEquals(nodes.get(size - i - 1), search.getNextNode(), "Node retrieval in wrong order");
		}

		assertEquals(rootNode, search.getNextNode(), "Should get root node");
		assertEquals(rootNode, search.getNextNode(), "Should get root node");

		// adding should now work
		search.addNodes(newNodes);
		assertEquals(1, search.getNodes().size(), "Node 1 should now be stored");
	}
}
