/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.representation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import kdo.domain.IOperator;
import kdo.domain.IProblemState;

/**
 * Represents a node in a search tree.
 */
public class Node implements Comparable<Node>
{
	/** the number of node objects generated */
	private static long createdNodes = 0;

	/** the number of node objects explored */
	private static long exploredNodes = 0;

	/** id of this node */
	private final int nodeID;

	/** state this node is representing */
	private IProblemState state;

	/** parent node of this node */
	private Node parent;

	/** operator that generated this node */
	private final IOperator operator;

	/** depth of this node in the search tree */
	private int depth;

	/** path cost of this node */
	private final float pathCost;

	/** the (estimated) utility of this node */
	private float utility;

	/**
	 * Constructor creating a new node object.
	 * @param state the problem state this node is representing
	 */
	public Node(IProblemState state)
	{
		this(state, null, null, 0, 0.0f);
	}

	/**
	 * Constructor creating a new node object.
	 * @param state the problem state this node is representing
	 * @param parent the parent node of this node, null if root node
	 * @param operator the operator that created this node
	 * @param depth the depth of this node in the tree
	 * @param cost the path cost of this node
	 */
	public Node(IProblemState state, Node parent, IOperator operator, int depth, float cost)
	{
		this.state = state;
		this.parent = parent;
		this.operator = operator;
		this.depth = depth;
		this.pathCost = cost;
		nodeID = (int) createdNodes++;
	}

	/**
	 * Returns the number of node objects created
	 * @return the number of node objects created
	 */
	public static long getNumberOfNodes()
	{
		return createdNodes;
	}

	/**
	 * Returns the number of node objects explored
	 * @return the number of node objects explored
	 */
	public static long getExploredNodes()
	{
		return exploredNodes;
	}

	/**
	 * Returns the state this node represents
	 * @return the state this node represents
	 */
	public IProblemState getState()
	{
		return state;
	}

	/**
	 * Returns the depth of this node in the search tree
	 * @return the depth of this node in the search tree
	 */
	public int getDepth()
	{
		return depth;
	}

	/**
	 * Sets the depth of this node in the search tree
	 * @param depth the depth of this node in the search tree
	 */
	public void setDepth(int depth)
	{
		this.depth = depth;
	}

	/**
	 * Returns the operator that produced this node
	 * @return the operator that produced this node
	 */
	public IOperator getOperator()
	{
		return operator;
	}

	/**
	 * Returns the (estimated) utility of this node
	 * @return the (estimated) utility of this node
	 */
	public float getUtility()
	{
		return utility;
	}

	/**
	 * Sets the (estimated) utility of this node
	 * @param utility the (estimated) utility of this node
	 */
	public void setUtility(float utility)
	{
		this.utility = utility;
	}

	/**
	 * Returns the cost of the solution path from the root node to this node.
	 * @return the cost of the solution path from the root node to this node.
	 */
	public float getPathCost()
	{
		return pathCost;
	}

	/**
	 * Returns the parent node of this node
	 * @return the parent node of this node
	 */
	public Node getParent()
	{
		return parent;
	}

	/**
	 * Sets the parent node of this node
	 * @param parent the parent node of this node
	 */
	public void setParent(Node parent)
	{
		this.parent = parent;
	}

	/**
	 * Sets the state this node is representing
	 * @param state the new problem state
	 */
	public void setState(IProblemState state)
	{
		this.state = state;
	}

	/**
	 * Expands this node by generating all child nodes
	 * @return a list containing the successor states of this node, an empty list
	 *         if there are none
	 */
	public List<Node> expand()
	{
		List<Node> successorNodes = createNodeList();
		List<IOperator> operators = state.getOperators();
		Iterator<IOperator> operatorIterator = operators.iterator();

		while (operatorIterator.hasNext()) {
			IOperator operator = operatorIterator.next();
			Node currentNode = new Node(
					operator.getSuccessorState(state), this, operator, depth + 1, pathCost + operator.getCost());
			successorNodes.add(currentNode);
		}
		exploredNodes++;
		return successorNodes;
	}

	/**
	 * Creates a list of nodes representing the path from the root node to this
	 * node. Recursive.
	 * @param path the list to which to add this node
	 */
	public List<Node> getSolutionPath()
	{
		List<Node> path = new LinkedList<Node>();
		Node currentNode = this;
		while (currentNode != null) {
			// add node at the beginning
			path.add(0, currentNode);
			currentNode = currentNode.getParent();
		}
		return path;
	}

	/**
	 * Prints the path from the root node to this node to stdout. Recursive.
	 */
	public void printSolutionPath()
	{
		// call recursively for parent node
		if (parent != null) {
			parent.printSolutionPath();
		}
		// now also print this node
		System.out.println(this);
	}

	/**
	 * Factory Method to create a list represenation for nodes. The default
	 * representation returns an ArrayList.
	 * @return a list representation to contain nodes
	 */
	public List<Node> createNodeList()
	{
		return new ArrayList<Node>();
	}

	/**
	 * Returns a string representation of this object
	 * @return a string representation of this object
	 */
	@Override
	public String toString()
	{
		StringBuffer result = new StringBuffer(100);
		result.append("operator: " + operator);
		result.append(" depth: " + (depth + 1));
		result.append(" state: " + state);
		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return nodeID == ((Node) obj).nodeID;
	}

	/*
	 * Nodes are compared by their utility (heuristic) value. If it is equal, the
	 * nodeIDs are compared.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Node node2)
	{
		if (utility < node2.getUtility()) {
			return -1;
		}
		if (utility > node2.getUtility()) {
			return 1;
		}

		if (nodeID < node2.nodeID) {
			return -1;
		}
		if (nodeID > node2.nodeID) {
			return 1;
		}
		return 0;
	}

	/**
	 * Calculates the estimated cost to the goal (heuristic value) of the node's
	 * state
	 * @return the estimated cost calculated
	 */
	public float calculateEstimatedCost()
	{
		return state.getHeuristicsValue();
	}

	public static class NodeUtilityComparator implements Comparator<Node>
	{
		/*
		 * (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Node node1, Node node2)
		{
			if (node1.getUtility() < node2.getUtility()) {
				return -1;
			} else if (node1.getUtility() > node2.getUtility()) {
				return 1;
			}
			return 0;
		}
	}

	public static class NodeUtilityIDComparator implements Comparator<Node>
	{
		/*
		 * (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Node node1, Node node2)
		{
			if (node1.getUtility() < node2.getUtility()) {
				return -1;
			}
			if (node1.getUtility() > node2.getUtility()) {
				return 1;
			}

			if (node1.nodeID < node2.nodeID) {
				return -1;
			}
			if (node1.nodeID > node2.nodeID) {
				return 1;
			}
			return 0;
		}
	}

	/**
	 * @return the nodeID
	 */
	public int getNodeID()
	{
		return nodeID;
	}
}
