/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.search.representation;

import java.util.List;
import kdo.domain.IOperator;
import kdo.domain.IProblemState;

/**
 * Extends the base class by keeping the list of child nodes of this node
 *
 * @author Last modified by $Author: KDorer $
 * @version $Revision: 1.5 $
 */
public class TreeNode extends Node
{
	/** the child node of this nodes */
	protected List<Node> childNodes;

	/**
	 * Constructor creating a new node object.
	 * @param state the problem state this node is representing
	 * @param parent the parent node of this node, null if root node
	 * @param operator the operator that created this node
	 * @param depth the depth of this node in the tree
	 * @param cost the path cost of this node
	 */
	public TreeNode(IProblemState state, Node parent, IOperator operator, int depth, float cost)
	{
		super(state, parent, operator, depth, cost);
		childNodes = null;
	}

	/**
	 * Expands this node by generating all child nodes and storing them in this
	 * node
	 *
	 * @return a list containing the successor states of this node, an empty list
	 *         if there are none
	 */
	@Override
	public List<Node> expand()
	{
		childNodes = super.expand();
		return childNodes;
	}
}
