/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.symboltreeparser;

import java.util.List;

/**
 * Represents a Symbol Nodes, containing other child nodes (or leafs).
 *
 * @author Simon Raffeiner
 */
public class SymbolNode
{
	public final List<Object> children;

	public SymbolNode()
	{
		children = null;
	}

	public SymbolNode(List<Object> children)
	{
		this.children = children;
	}

	/**
	 * Returns a textual representation of the Node. If your Symbol Tree was
	 * parsed from an input string of a proper S-expression,
	 * this output should always be identical to your input.
	 */
	@Override
	public String toString()
	{
		String ret = "";

		if (children == null || children.isEmpty())
			return "";

		for (int i = 0; i < children.size(); i++) {
			Object child = children.get(i);

			if (i > 0)
				ret += " ";

			if (child instanceof SymbolNode)
				ret += "(" + child.toString() + ")";
			else
				ret += child.toString();
		}

		return ret;
	}
}
