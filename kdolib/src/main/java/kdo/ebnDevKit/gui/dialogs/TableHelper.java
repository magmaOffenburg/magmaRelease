/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.dialogs;

import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JTable;

class TableHelper
{
	public static void setTableSize(JTable relevanceConditionTable, JScrollPane tableScrollPane, final int rowsVisible)
	{
		Dimension newTableSize = relevanceConditionTable.getPreferredSize();
		Dimension headerPrefSize = relevanceConditionTable.getTableHeader().getPreferredSize();
		newTableSize.height = (rowsVisible * relevanceConditionTable.getRowHeight()) + headerPrefSize.height;
		tableScrollPane.getViewport().setPreferredSize(newTableSize);
		tableScrollPane.setMinimumSize(new Dimension(newTableSize));
	}
}
