/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import kdo.ebnDevKit.ebnAccess.IEbnPerception;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnProposition;

/**
 * table model for propositions
 * @author Thomas Rinklin
 *
 */
public class PropositionTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1476377120271430394L;

	private final List<IEbnProposition> propositions = new ArrayList<IEbnProposition>();

	private String[] columnNames;

	/**
	 * empty constructor
	 */
	public PropositionTableModel()
	{
	}

	/**
	 * constructor
	 * @param columnNames column names
	 */
	public PropositionTableModel(String[] columnNames)
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
	public PropositionTableModel(IEbnProposition[] data)
	{
		addPropositionData(data);
	}

	/**
	 * add proposition array
	 * @param data proposition array
	 */
	public void addPropositionData(IEbnProposition[] data)
	{
		int firstRow = propositions.size();
		int lastRow = firstRow - 1;
		for (IEbnProposition propositionData : data) {
			propositions.add(propositionData);
			lastRow++;
		}

		fireTableRowsInserted(firstRow, lastRow);
	}

	/**
	 * add proposition
	 * @param data proposition
	 */
	public void addPropositionData(IEbnProposition data)
	{
		propositions.add(data);
		fireTableRowsInserted(propositions.size() - 1, propositions.size() - 1);
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
			return Boolean.class;
		return super.getColumnClass(columnIndex);
	}

	@Override
	public int getRowCount()
	{
		return propositions.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		IEbnProposition data = propositions.get(rowIndex);
		if (columnIndex == 0)
			return data.getPerception().getName();
		else if (columnIndex == 1)
			return data.isNegated();
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
			if (aValue instanceof Boolean) {
				Boolean bVal = (Boolean) aValue;

				IEbnProposition oldProp = propositions.get(rowIndex);
				IEbnProposition newProp = oldProp.getPerception().createProposition(bVal);
				propositions.set(rowIndex, newProp);

				fireTableCellUpdated(rowIndex, columnIndex);
				return;
			}
		}
		super.setValueAt(aValue, rowIndex, columnIndex);
	}

	/**
	 * checks if this perception exists as a proposition
	 * @param perc perception
	 * @return true if it exists, false if not
	 */
	public boolean perceptionExists(IEbnPerception perc)
	{
		for (IEbnProposition rowElement : propositions) {
			if (rowElement.getPerception().equals(perc))
				return true;
		}
		return false;
	}

	/**
	 * do not change this list, if the corresponding table is visible
	 */
	public List<IEbnProposition> getPropositions()
	{
		return propositions;
	}

	/**
	 * removes rows
	 */
	public void removeRows(int[] selectedRows)
	{
		Arrays.sort(selectedRows);

		for (int i = selectedRows.length - 1; i >= 0; i--) {
			propositions.remove(selectedRows[i]);
			fireTableRowsDeleted(selectedRows[i], selectedRows[i]);
		}
	}
}
