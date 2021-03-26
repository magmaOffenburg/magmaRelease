/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import kdo.ebnDevKit.ebnAccess.IEbnAccess;
import kdo.ebnDevKit.ebnAccess.IEbnGoal;
import kdo.ebnDevKit.ebnAccess.IEbnPerception;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnProposition;

/**
 * dialog for creating and editing goals
 * @author Thomas Rinklin
 *
 */
public class GoalDialog extends JDialog
{
	private JPanel jContentPane = null;

	private JPanel buttonPanel = null;

	private JPanel mainPanel = null;

	private JComboBox<String> goalConditionComboBox = null;

	private JPanel goalConditionPanel = null;

	private JPanel relevanceConditionPanel;

	private JPanel importancePanel;

	private final IEbnGoal ebnGoal;

	private final IEbnAccess ebnAccess;

	private JSlider importanceSlider;

	private JTextField importanceField;

	private JCheckBox isNegatedCheckBox;

	private static final long serialVersionUID = -3810022733663944982L;

	private PropositionTableModel relCondModel;

	private JTable relevanceConditionTable;

	/**
	 * constructor for the goal dialog
	 * @param frame parent frame
	 * @param ebnGoal reference to the goal or null for creating a new one
	 * @param ebnAccess reference to the ebnAccess object
	 */
	public GoalDialog(JFrame frame, IEbnGoal ebnGoal, IEbnAccess ebnAccess)
	{
		super(frame, "Goal Dialog", true);

		this.setSize(400, 400);

		this.ebnGoal = ebnGoal;
		this.ebnAccess = ebnAccess;

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				super.windowClosing(e);
			}
		});

		initialize();
	}

	private void initialize()
	{
		this.setSize(550, 450);
		this.setContentPane(getJContentPane());
	}

	private JPanel getJContentPane()
	{
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getMainPanel(), BorderLayout.NORTH);
			jContentPane.add(getRelevanceConditionPanel(), BorderLayout.CENTER);
			jContentPane.add(getButtonPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	private JPanel getButtonPanel()
	{
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());

			JButton buttonOk = new JButton("OK");
			buttonOk.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if (ebnGoal == null)
						ebnAccess.addGoal(
								getGoalCondition(), getIsNegated(), getImportance(), getRelevanceConditions());
					else
						ebnAccess.changeGoal(ebnGoal, getIsNegated(), getImportance(), getRelevanceConditions());
					setVisible(false);
				}
			});

			JButton buttonCancle = new JButton("Cancle");
			buttonCancle.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e)
				{
					setVisible(false);
				}
			});

			buttonPanel.add(buttonOk, new GridBagConstraints());
			buttonPanel.add(buttonCancle, new GridBagConstraints());
		}
		return buttonPanel;
	}

	private JPanel getMainPanel()
	{
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.anchor = GridBagConstraints.NORTH;
			gbc.weightx = 1;

			gbc.gridy = 0;
			mainPanel.add(getGoalConditionPanel(), gbc);
			gbc.gridy = 1;
			mainPanel.add(getImportancePanel(), gbc);
		}
		return mainPanel;
	}

	private JPanel getRelevanceConditionPanel()
	{
		if (relevanceConditionPanel == null) {
			relevanceConditionPanel = new JPanel();
			relevanceConditionPanel.setLayout(new BorderLayout());
			relevanceConditionPanel.setBorder(new TitledBorder("Relevance Condition"));

			String[] columnNames = {"Relevance Condition Name", "negated"};
			relCondModel = new PropositionTableModel(columnNames);

			fillPreselected();

			relevanceConditionTable = new JTable(relCondModel);
			JScrollPane tableScrollPane = new JScrollPane(relevanceConditionTable);
			JPanel relevanceConditionButtonPanel = new JPanel();

			TableHelper.setTableSize(relevanceConditionTable, tableScrollPane, 3);

			JButton addButton = new JButton("Add");
			relevanceConditionButtonPanel.add(addButton);
			addButton.addActionListener(new AddRelevanceConditionAction());

			JButton removeButton = new JButton("Remove");
			relevanceConditionButtonPanel.add(removeButton);
			removeButton.addActionListener(new RemoveRelevanceConditionAction());

			relevanceConditionPanel.add(tableScrollPane, BorderLayout.CENTER);
			relevanceConditionPanel.add(relevanceConditionButtonPanel, BorderLayout.SOUTH);
		}

		return relevanceConditionPanel;
	}

	private void fillPreselected()
	{
		if (ebnGoal != null) {
			Iterator<? extends IEbnProposition> itRelCon = ebnGoal.getRelevanceConditions();
			while (itRelCon.hasNext()) {
				IEbnProposition ebnProposition = itRelCon.next();
				relCondModel.addPropositionData(ebnProposition);
			}
		}
	}

	private JPanel getImportancePanel()
	{
		if (importancePanel == null) {
			importancePanel = new JPanel();
			importancePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			importancePanel.setBorder(new TitledBorder("Importance"));

			importanceSlider = new JSlider(0, 100, 100);
			importanceSlider.setPaintTicks(true);
			importanceSlider.setMinorTickSpacing(10);

			importanceSlider.setLabelTable(null);

			importanceField = new JTextField(5);

			importanceSlider.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e)
				{
					onImportanceSliderChange();
				}
			});

			importanceField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e)
				{
					onImportanceFieldChange();
				}
			});

			if (ebnGoal == null) {
				importanceSlider.setValue(100);
			} else {
				int newVal = (int) (ebnGoal.getImportance() * 100);
				importanceSlider.setValue(newVal);
			}
			onImportanceSliderChange();

			importancePanel.add(new JLabel("Importance: "));
			importancePanel.add(importanceSlider);
			importancePanel.add(importanceField);
		}
		return importancePanel;
	}

	private JPanel getGoalConditionPanel()
	{
		if (goalConditionPanel == null) {
			goalConditionPanel = new JPanel();
			goalConditionPanel.setLayout(new GridBagLayout());
			goalConditionPanel.setBorder(new TitledBorder("Goal Condition"));
			JLabel goalConditionLabel = new JLabel("Goal Condition:");

			goalConditionComboBox = new JComboBox<String>();
			isNegatedCheckBox = new JCheckBox("negate");

			if (ebnGoal == null) {
				createSelectableGoalCondComboBox();
			} else {
				createPreselectedGoalCondComboBox();
			}

			goalConditionPanel.add(goalConditionLabel);
			goalConditionPanel.add(goalConditionComboBox);
			goalConditionPanel.add(isNegatedCheckBox);
		}
		return goalConditionPanel;
	}

	private void createPreselectedGoalCondComboBox()
	{
		goalConditionComboBox.addItem(ebnGoal.getGoalCondition().getPerception().getName());
		goalConditionComboBox.setEnabled(false);

		isNegatedCheckBox.getModel().setSelected(ebnGoal.getGoalCondition().isNegated());
	}

	private void createSelectableGoalCondComboBox()
	{
		Iterator<? extends IEbnPerception> itPerceptions = ebnAccess.getPerceptions();
		while (itPerceptions.hasNext()) {
			IEbnPerception ebnPerception = itPerceptions.next();
			Iterator<? extends IEbnGoal> itGoals = ebnAccess.getGoals();
			boolean alreadyExistsAsGoal = false;

			while (itGoals.hasNext()) {
				IEbnGoal ebnGoal = itGoals.next();
				if (ebnGoal.getGoalCondition().getPerception().getName().equals(ebnPerception.getName()))
					alreadyExistsAsGoal = true;
			}

			if (!alreadyExistsAsGoal) {
				goalConditionComboBox.addItem(ebnPerception.getName());
			}
		}
	}

	/**
	 * returns the importance
	 * @return the importance
	 */
	public double getImportance()
	{
		if (importanceSlider == null)
			return 0.0;
		return ((double) importanceSlider.getValue()) / 100;
	}

	/**
	 * returns the goal condition
	 * @return the goal condition
	 */
	public IEbnPerception getGoalCondition()
	{
		if (goalConditionComboBox == null)
			return null;
		Iterator<? extends IEbnPerception> itPercs = ebnAccess.getPerceptions();
		while (itPercs.hasNext()) {
			IEbnPerception ebnPerception = itPercs.next();
			if (ebnPerception.getName().equals(goalConditionComboBox.getSelectedItem()))
				return ebnPerception;
		}

		return null;
	}

	/**
	 * returns the relevance condition list. do not change this list, if the
	 * dialog is visible!
	 * @return the relevance condition list
	 */
	public List<IEbnProposition> getRelevanceConditions()
	{
		return relCondModel.getPropositions();
	}

	/**
	 * returns if the goal condition is negated
	 * @return if the goal condition is negated
	 */
	public boolean getIsNegated()
	{
		if (isNegatedCheckBox == null)
			return false;
		return isNegatedCheckBox.getModel().isSelected();
	}

	private void onImportanceSliderChange()
	{
		double realValue = (double) importanceSlider.getValue() / 100;

		String realText = "" + realValue;
		if (!importanceField.getText().equals(realText)) {
			importanceField.setText(realText);
		}
	}

	private void onImportanceFieldChange()
	{
		try {
			Double realValue = Double.valueOf(importanceField.getText());

			int realInt = (int) (realValue * 100);

			if (importanceSlider.getValue() != realInt) {
				importanceSlider.setValue(realInt);
			}
		} catch (NumberFormatException nfe) {
		}
	}

	private final class AddRelevanceConditionAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			Map<String, IEbnPerception> availablePerceptions = new HashMap<String, IEbnPerception>();

			Iterator<? extends IEbnPerception> itPerceptions = ebnAccess.getPerceptions();
			while (itPerceptions.hasNext()) {
				IEbnPerception ebnPerception = itPerceptions.next();

				if (!relCondModel.perceptionExists(ebnPerception)) {
					availablePerceptions.put(ebnPerception.getName(), ebnPerception);
				}
			}

			Object[] availablePerceptionsArray = availablePerceptions.keySet().toArray();
			String selectedPerception = (String) JOptionPane.showInputDialog(GoalDialog.this,
					"Select the Perception to add as Relevance Proposition:", "New Relevance Proposition",
					JOptionPane.PLAIN_MESSAGE, null, availablePerceptionsArray, null);

			if ((selectedPerception != null) && (selectedPerception.length() > 0)) {
				relCondModel.addPropositionData(availablePerceptions.get(selectedPerception).createProposition(false));
			}
		}
	}

	public class RemoveRelevanceConditionAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			relCondModel.removeRows(relevanceConditionTable.getSelectedRows());
		}
	}
}
