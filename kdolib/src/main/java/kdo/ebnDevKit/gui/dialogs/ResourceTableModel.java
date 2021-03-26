/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.dialogs;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import kdo.ebnDevKit.ebnAccess.IEbnResource.IEbnResourceProposition;

/**
 * table model for resource propositions
 * @author Thomas Rinklin
 */
public class ResourceTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1476377120271430394L;

	private final List<IEbnResourceProposition> resources = new ArrayList<IEbnResourceProposition>();

	private String[] columnNames;

	/**
	 * empty constructor
	 */
	public ResourceTableModel()
	{
	}

	/**
	 * constructor
	 * @param columnNames column names
	 */
	public ResourceTableModel(String[] columnNames)
	{
		this.columnNames = columnNames;
	}

	@Override
	public String getColumnName(int column)
	{
		if (columnNames != null)
			return columnNames[column];

		return super.getColumnName(column);
	}

	/**
	 * init constructor
	 * @param data init values
	 */
	public ResourceTableModel(IEbnResourceProposition[] data)
	{
		addResourceData(data);
	}

	/**
	 * add proposition array
	 * @param data proposition array
	 */
	public void addResourceData(IEbnResourceProposition[] data)
	{
		int firstRow = resources.size();
		int lastRow = firstRow - 1;
		for (IEbnResourceProposition resourceData : data) {
			resources.add(resourceData);
			lastRow++;
		}

		fireTableRowsInserted(firstRow, lastRow);
	}

	/**
	 * add proposition
	 * @param data proposition
	 */
	public void addResourceData(IEbnResourceProposition data)
	{
		resources.add(data);
		fireTableRowsInserted(resources.size() - 1, resources.size() - 1);
	}

	@Override
	public int getColumnCount()
	{
		return 2;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if (columnIndex == 1)
			return Integer.class;
		return super.getColumnClass(columnIndex);
	}

	@Override
	public int getRowCount()
	{
		return resources.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		IEbnResourceProposition resProp = resources.get(rowIndex);
		if (columnIndex == 0)
			return resProp.getResource().getName();
		else if (columnIndex == 1)
			return resProp.getAmountUsed();
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return columnIndex != 0;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		if (columnIndex == 1) {
			if (aValue instanceof Integer) {
				Integer iVal = (Integer) aValue;

				IEbnResourceProposition oldResProp = resources.get(rowIndex);
				IEbnResourceProposition newResProp = oldResProp.getResource().creatProposition(iVal);
				resources.set(rowIndex, newResProp);

				fireTableCellUpdated(rowIndex, columnIndex);
				return;
			}
		}
		super.setValueAt(aValue, rowIndex, columnIndex);
	}

	/**
	 * do not change this list, if the corresponding table is visible
	 */
	public List<IEbnResourceProposition> getResourcePropositions()
	{
		return resources;
	}
}
