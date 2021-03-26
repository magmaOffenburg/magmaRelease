/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.strategy.informed;

import kdo.search.strategy.TreeSearchBaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author KDorer
 *
 * Tests the AStarSearch Implementation of the ISearchStrategy Interface
 */
public class AStarSearchTestCase extends TreeSearchBaseTestCase
{
	// the instance to use for testing
	AStarSearch search;

	/**
	 * Sets up the test fixture before every call to a test method.
	 */
	@Override
	@BeforeEach
	protected void setUp() throws Exception
	{
		super.setUp();

		// create a new instance each time
		search = new AStarSearch();
	}

	@Test
	public void testEmpty() throws Exception
	{
		// to avoid warnings
	}

	// /**
	// * Test the method addNodes().
	// *
	// * @throws Exception if an exception occurs
	// */
	// public void testAddNodesAndGetNextNode() throws Exception
	// {
	// int size = nodes.size();
	// search.addNodes(nodes);
	//
	// assertEquals("Not all nodes stored", size, search.getNodes().size());
	//
	// // check storage/retrieval order
	// Node previousNode = search.getNextNode();
	// Node currentNode;
	// // we want to allow negative values for utility or positive
	// boolean negativeUtility = false;
	// if (previousNode.getUtility() < 0) {
	// negativeUtility = true;
	// }
	// for (int i = 1; i < nodes.size(); i++) {
	// currentNode = search.getNextNode();
	// if (negativeUtility) {
	// assertEquals("Wrong order of nodes", true, previousNode.getUtility() >=
	// currentNode.getUtility());
	// } else {
	// assertEquals("Wrong order of nodes", true, previousNode.getUtility() <=
	// currentNode.getUtility());
	// }
	// previousNode = currentNode;
	// }
	// assertEquals("Did not get null when there are no nodes", null,
	// search.getNextNode());
	// assertEquals("Did not get proper max nodes", size,
	// search.getMaxNumberOfNodesStored());
	// }
	//
	// /**
	// * Test the method addNodes() when called two times
	// *
	// * @throws Exception if an exception occurs
	// */
	// public void testAddNodesAndGetNextNodeWhenCalledTwice() throws Exception
	// {
	// int size = nodes.size();
	//
	// search.addNodes(nodes.subList(size - 5, size));
	// search.addNodes(nodes.subList(0, size - 5));
	//
	// assertEquals("Not all nodes stored", size, search.getNodes().size());
	//
	// // check storage/retrieval order
	// Node previousNode = search.getNextNode();
	// Node currentNode;
	// // we want to allow negative values for utility or positive
	// boolean negativeUtility = false;
	// if (previousNode.getUtility() < 0) {
	// negativeUtility = true;
	// }
	// for (int i = 1; i < nodes.size(); i++) {
	// currentNode = search.getNextNode();
	// if (negativeUtility) {
	// assertEquals("Wrong order of nodes", true, previousNode.getUtility() >=
	// currentNode.getUtility());
	// } else {
	// assertEquals("Wrong order of nodes", true, previousNode.getUtility() <=
	// currentNode.getUtility());
	// }
	// previousNode = currentNode;
	// }
	// assertEquals("Did not get null when there are no nodes", null,
	// search.getNextNode());
	// assertEquals("Did not get proper max nodes", size,
	// search.getMaxNumberOfNodesStored());
	// }
	// /**
	// * Test the method addNodes() if it calculates proper utility
	// *
	// * @throws Exception if an exception occurs
	// */
	// public void testAddNodesAbsoluteUtility() throws Exception
	// {
	// int size = nodes.size();
	//
	// search.addNodes(nodes);
	//
	// assertEquals("Not all nodes stored", size, search.getNodes().size());
	// assertEquals("Utility is not correct", 2.0,
	// Math.abs(search.getNextNode().getUtility()),0.0001);
	// }
}
