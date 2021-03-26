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
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnEffect;

/**
 * table model for effects
 * @author Thomas Rinklin
 */
public class EffectTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = -4108012546365678520L;

	private final List<IEbnEffect> effects = new ArrayList<IEbnEffect>();

	private String[] columnNames;

	public String[] getColumnNames()
	{
		return columnNames;
	}

	/**
	 * empty constructor
	 */
	public EffectTableModel()
	{
	}

	/**
	 * constructor
	 * @param columnNames column names
	 */
	public EffectTableModel(String[] columnNames)
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
	public EffectTableModel(IEbnEffect[] data)
	{
		addEffectData(data);
	}

	/**
	 * add effect array
	 * @param data effect array
	 */
	public void addEffectData(IEbnEffect[] data)
	{
		int firstRow = effects.size();
		int lastRow = firstRow - 1;
		for (IEbnEffect propositionData : data) {
			effects.add(propositionData);
			lastRow++;
		}

		fireTableRowsInserted(firstRow, lastRow);
	}

	/**
	 * add proposition
	 * @param data proposition
	 */
	public void addEffectData(IEbnEffect data)
	{
		effects.add(data);
		fireTableRowsInserted(effects.size() - 1, effects.size() - 1);
	}

	@Override
	public int getColumnCount()
	{
		return 3;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if (columnIndex == 1)
			return Boolean.class;
		if (columnIndex == 2)
			return Double.class;
		return super.getColumnClass(columnIndex);
	}

	@Override
	public int getRowCount()
	{
		return effects.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		IEbnEffect data = effects.get(rowIndex);
		if (columnIndex == 0)
			return data.getPerception().getName();
		else if (columnIndex == 1)
			return data.isNegated();
		else if (columnIndex == 2)
			return data.getProbability();
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

				IEbnEffect oldProp = effects.get(rowIndex);
				IEbnEffect newProp = oldProp.getPerception().createEffect(bVal, oldProp.getProbability());
				effects.set(rowIndex, newProp);

				fireTableCellUpdated(rowIndex, columnIndex);
				return;
			}
		} else if (columnIndex == 2) {
			if (aValue instanceof Double) {
				Double dVal = (Double) aValue;

				IEbnEffect oldProp = effects.get(rowIndex);
				IEbnEffect newProp = oldProp.getPerception().createEffect(oldProp.isNegated(), dVal);
				effects.set(rowIndex, newProp);

				fireTableCellUpdated(rowIndex, columnIndex);
				return;
			}
		}
		super.setValueAt(aValue, rowIndex, columnIndex);
	}

	/**
	 * checks if this perception exists as a proposition
	 * @param ebnPerception perception
	 * @return true if it exists, false if not
	 */
	public boolean perceptionExists(IEbnPerception ebnPerception)
	{
		for (IEbnEffect rowElement : effects) {
			if (rowElement.getPerception().equals(ebnPerception))
				return true;
		}
		return false;
	}

	/**
	 * do not change this list, if the corresponding table is visible
	 */
	public List<IEbnEffect> getEffects()
	{
		return effects;
	}

	public void removeRows(int[] selectedRows)
	{
		Arrays.sort(selectedRows);

		for (int i = selectedRows.length - 1; i >= 0; i--) {
			effects.remove(selectedRows[i]);
			fireTableRowsDeleted(selectedRows[i], selectedRows[i]);
		}
	}
}
