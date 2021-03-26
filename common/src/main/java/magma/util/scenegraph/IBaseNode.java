/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.scenegraph;

import java.util.ArrayList;

public interface IBaseNode extends Cloneable {
	NodeType getNodeType();

	void setParent(IBaseNode parent);

	IBaseNode getParent();

	ArrayList<IBaseNode> getChildren();

	IBaseNode clone();

	boolean structurallyEquals(IBaseNode other);

	void update(IBaseNode other);

	/**
	 * Fetch the first node in the child structure by a depth first search that
	 * corresponds to the given NodeType and fulfills the property constraint. A
	 * node fulfills the property constraint, if the specified property has or
	 * contains the given value.
	 *
	 * @param <T> - the intended node type
	 * @param nodeType - the intended node type
	 * @param property - a property of the node that should be checked or null if
	 *        no check should be performed
	 * @param value - the value which the specified property should contain
	 * @return the first node of type nodeType with the given property having the
	 *         given value or null if no such node was found
	 */
	<T extends IBaseNode> T getNode(Class<T> nodeType, String property, String value);
}
