/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import kdo.ebn.IEBNAction;
import kdo.ebnDevKit.ebnAccess.IEbnAccess;
import kdo.ebnDevKit.ebnAccess.IEbnCompetence;
import kdo.ebnDevKit.ebnAccess.IEbnPerception;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnEffect;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnProposition;
import kdo.ebnDevKit.ebnAccess.IEbnResource;
import kdo.ebnDevKit.ebnAccess.IEbnResource.IEbnResourceProposition;

/**
 * Dialog for editing or creating a Competence
 * @author Thomas Rinklin
 */
public class CompetenceDialog extends JDialog
{
	private static final long serialVersionUID = -3895543032389475124L;

	private final IEbnCompetence ebnComp;

	private final IEbnAccess ebnAccess;

	private JPanel jContentPane;

	private JPanel actionPanel;

	private JLabel actionLabel;

	private JPanel buttonPanel;

	private DefaultListModel<String> actionsModel;

	private JList<String> actionsList;

	private JLabel resourceLabel;

	private ResourceTableModel resTabModel;

	private JPanel preAndPostConditionPanel;

	private PropositionTableModel preCondModel;

	private EffectTableModel effectModel;

	private JScrollPane resScrollPane;

	private JButton addEffectButton;

	private JButton addPreconditionButton;

	private JButton removePreconditionButton;

	private JTable preconditionTable;

	private JTable effectTable;

	private JButton removeEffectButton;

	/**
	 * constructor
	 * @param frame parent frame
	 * @param comp the component to edit or null to create a new component
	 * @param agent reference to the agent
	 */
	public CompetenceDialog(JFrame frame, IEbnCompetence comp, IEbnAccess ebnAccess)
	{
		super(frame, "Competence Dialog", true);

		this.setSize(400, 400);

		this.ebnComp = comp;

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
			jContentPane.add(getActionPanel(), BorderLayout.NORTH);
			jContentPane.add(getPreAndPostConditionPanel(), BorderLayout.CENTER);
			jContentPane.add(getButtonPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	private Component getActionPanel()
	{
		if (actionPanel == null) {
			// Panel
			actionPanel = new JPanel();
			actionPanel.setLayout(new GridBagLayout());
			actionPanel.setBorder(new TitledBorder("Actions and Resources"));

			// Actions
			actionLabel = new JLabel("Performed Actions:");
			createActionModel();
			actionsList = new JList<String>(actionsModel);

			preselectActionValues();

			// Resources
			resourceLabel = new JLabel("Used Resources:");

			createResourceTableModel();
			JTable resTable = new JTable(resTabModel);
			resScrollPane = new JScrollPane(resTable);
			TableHelper.setTableSize(resTable, resScrollPane, 3);

			placeActionComponents();
		}

		return actionPanel;
	}

	private void placeActionComponents()
	{
		// Place the components
		GridBagConstraints cText = new GridBagConstraints();
		cText.anchor = GridBagConstraints.NORTH;

		GridBagConstraints cOther = new GridBagConstraints();
		cOther.fill = GridBagConstraints.BOTH;
		cOther.weightx = 1;

		actionPanel.add(actionLabel, cText);
		actionPanel.add(new JScrollPane(actionsList), cOther);
		actionPanel.add(resourceLabel, cText);
		actionPanel.add(resScrollPane, cOther);
	}

	private void createActionModel()
	{
		actionsModel = new DefaultListModel<String>();
		for (IEBNAction behavior : ebnAccess.getAgent().getBehaviors().values()) {
			String behaviorName = behavior.getName();
			actionsModel.addElement(behaviorName);
		}
	}

	private void createResourceTableModel()
	{
		String[] columnNamse = {"Resource Name", "Amount"};
		resTabModel = new ResourceTableModel(columnNamse);
		Iterator<? extends IEbnResource> itEbnRes = ebnAccess.getResources();
		while (itEbnRes.hasNext()) {
			IEbnResource ebnResource = itEbnRes.next();

			IEbnResourceProposition resProp = null;
			if (ebnComp != null)
				resProp = getResourceProp(ebnResource);

			if (resProp == null)
				resProp = ebnResource.creatProposition(0);

			resTabModel.addResourceData(resProp);
		}
	}

	private void preselectActionValues()
	{
		// load Action Values
		if (ebnComp != null) {
			// get selected Actions and select them on the list
			List<Integer> selectedActions = new ArrayList<Integer>();
			for (int index = 0; index < actionsModel.getSize(); index++) {
				String curActionName = actionsModel.get(index);

				Iterator<IEBNAction> actions = ebnComp.getActions();
				while (actions.hasNext()) {
					IEBNAction iBehavior = actions.next();
					if (iBehavior.getName().equals(curActionName))
						selectedActions.add(index);
				}
			}
			int[] selectionActionsArray = new int[selectedActions.size()];
			int i = 0;
			for (Integer integer : selectedActions) {
				selectionActionsArray[i] = integer;
				i++;
			}
			actionsList.setSelectedIndices(selectionActionsArray);
		}
	}

	private IEbnResourceProposition getResourceProp(IEbnResource ebnResource)
	{
		Iterator<? extends IEbnResourceProposition> itResIterator = ebnComp.getResources();
		while (itResIterator.hasNext()) {
			IEbnResourceProposition iEbnResourceProposition = itResIterator.next();
			if (iEbnResourceProposition.isResource(ebnResource))
				return iEbnResourceProposition;
		}
		return null;
	}

	private JPanel getPreAndPostConditionPanel()
	{
		if (preAndPostConditionPanel == null) {
			preAndPostConditionPanel = new JPanel();
			preAndPostConditionPanel.setLayout(new BorderLayout());
			preAndPostConditionPanel.setBorder(new TitledBorder("Preconditions and Effects"));

			// Precondition
			createPreconditionModel();
			preconditionTable = new JTable(preCondModel);

			addPreconditionButton = new JButton("Add Precondition");
			addPreconditionButton.addActionListener(new AddPerconditionAction());
			removePreconditionButton = new JButton("Remove Precondition");
			removePreconditionButton.addActionListener(new RemovePerconditionAction());

			// Effects
			createEffectModel();
			effectTable = new JTable(effectModel);

			addEffectButton = new JButton("Add Effect");
			addEffectButton.addActionListener(new AddEffectAction());
			removeEffectButton = new JButton("Remove Effect");
			removeEffectButton.addActionListener(new RemoveEffectAction());

			placePreAndPostconditionComponents(preconditionTable, effectTable);
		}
		return preAndPostConditionPanel;
	}

	private void placePreAndPostconditionComponents(JTable relevanceConditionTable, JTable effectTable)
	{
		JPanel relevanceConditionButtonPanel = new JPanel();
		relevanceConditionButtonPanel.add(addPreconditionButton);
		relevanceConditionButtonPanel.add(removePreconditionButton);
		relevanceConditionButtonPanel.add(addEffectButton);
		relevanceConditionButtonPanel.add(removeEffectButton);

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.weightx = 1;
		c.weighty = 1;

		JScrollPane tableScrollPane = new JScrollPane(relevanceConditionTable);
		TableHelper.setTableSize(relevanceConditionTable, tableScrollPane, 3);
		centerPanel.add(tableScrollPane, c);

		JScrollPane tableScrollPane2 = new JScrollPane(effectTable);
		TableHelper.setTableSize(effectTable, tableScrollPane2, 3);
		centerPanel.add(tableScrollPane2, c);

		preAndPostConditionPanel.add(centerPanel, BorderLayout.CENTER);
		preAndPostConditionPanel.add(relevanceConditionButtonPanel, BorderLayout.SOUTH);
	}

	private void createEffectModel()
	{
		String[] columnNames = {"Effect Name", "negated", "probability"};
		effectModel = new EffectTableModel(columnNames);
		if (ebnComp != null) {
			Iterator<? extends IEbnEffect> itEffects = ebnComp.getEffects();
			while (itEffects.hasNext()) {
				IEbnEffect ebnEffect = itEffects.next();
				effectModel.addEffectData(ebnEffect);
			}
		}
	}

	private void createPreconditionModel()
	{
		String[] columnNames = {"Precondition Name", "negated"};
		preCondModel = new PropositionTableModel(columnNames);
		if (ebnComp != null) {
			Iterator<? extends IEbnProposition> itPreCon = ebnComp.getPreconditions();
			while (itPreCon.hasNext()) {
				IEbnProposition ebnProposition = itPreCon.next();
				preCondModel.addPropositionData(ebnProposition);
			}
		}
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
					if (ebnComp == null)
						ebnAccess.addCompetence(getActions(), getResources(), getPreconditions(), getEffects());
					else {
						ebnAccess.changeCompetence(
								ebnComp, getActions(), getResources(), getPreconditions(), getEffects());
					}
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

	private void addPreconditionAction()
	{
		Map<String, IEbnPerception> availablePerceptions = new HashMap<String, IEbnPerception>();

		Iterator<? extends IEbnPerception> itPerceptions = ebnAccess.getPerceptions();
		while (itPerceptions.hasNext()) {
			IEbnPerception ebnPerception = itPerceptions.next();

			if (!preCondModel.perceptionExists(ebnPerception)) {
				availablePerceptions.put(ebnPerception.getName(), ebnPerception);
			}
		}

		Object[] availablePerceptionsArray = availablePerceptions.keySet().toArray();
		String selectedPerception = (String) JOptionPane.showInputDialog(CompetenceDialog.this,
				"Select the Perception to add as Precondition:", "New Precondition", JOptionPane.PLAIN_MESSAGE, null,
				availablePerceptionsArray, null);

		if ((selectedPerception != null) && (selectedPerception.length() > 0)) {
			preCondModel.addPropositionData(availablePerceptions.get(selectedPerception).createProposition(false));
		}
	}

	private void removeSelectedPrecondition()
	{
		preCondModel.removeRows(preconditionTable.getSelectedRows());
	}

	private void addEffectAction()
	{
		Map<String, IEbnPerception> availablePerceptions = new HashMap<String, IEbnPerception>();

		Iterator<? extends IEbnPerception> itPerceptions = ebnAccess.getPerceptions();
		while (itPerceptions.hasNext()) {
			IEbnPerception ebnPerception = itPerceptions.next();

			if (!effectModel.perceptionExists(ebnPerception)) {
				availablePerceptions.put(ebnPerception.getName(), ebnPerception);
			}
		}

		Object[] availablePerceptionsArray = availablePerceptions.keySet().toArray();
		String selectedPerception = (String) JOptionPane.showInputDialog(CompetenceDialog.this,
				"Select the Perception to add as Effect:", "New Effect", JOptionPane.PLAIN_MESSAGE, null,
				availablePerceptionsArray, null);

		if ((selectedPerception != null) && (selectedPerception.length() > 0)) {
			effectModel.addEffectData(availablePerceptions.get(selectedPerception).createEffect(false, 1));
		}
	}

	private void removeSelectedEffect()
	{
		effectModel.removeRows(effectTable.getSelectedRows());
	}

	/**
	 * returns the list of actions
	 * @return the list of actions
	 */
	public List<IEBNAction> getActions()
	{
		Map<String, IEBNAction> behaviorMap = ebnAccess.getAgent().getBehaviors();

		List<IEBNAction> behaviorList = new ArrayList<IEBNAction>();
		List<String> selValues = actionsList.getSelectedValuesList();
		for (String selValue : selValues) {
			IEBNAction behavior = behaviorMap.get(selValue);
			behaviorList.add(behavior);
		}
		return behaviorList;
	}

	/**
	 * returns the resource list.
	 * @return the resource list
	 */
	public List<IEbnResourceProposition> getResources()
	{
		ArrayList<IEbnResourceProposition> retList = new ArrayList<IEbnResourceProposition>();

		for (IEbnResourceProposition iEbnResourceProposition : resTabModel.getResourcePropositions()) {
			if (iEbnResourceProposition.getAmountUsed() > 0)
				retList.add(iEbnResourceProposition);
		}

		return retList;
	}

	/**
	 * returns the precondition list. do not change this list, if the dialog is
	 * visible!
	 * @return the precondition list
	 */
	public List<IEbnProposition> getPreconditions()
	{
		return preCondModel.getPropositions();
	}

	/**
	 * returns the effect list. do not change this list, if the dialog is
	 * visible!
	 * @return the effect list
	 */
	public List<IEbnEffect> getEffects()
	{
		return effectModel.getEffects();
	}

	private final class AddEffectAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			addEffectAction();
		}
	}

	private final class RemoveEffectAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			removeSelectedEffect();
		}
	}

	private final class AddPerconditionAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			addPreconditionAction();
		}
	}

	private final class RemovePerconditionAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			removeSelectedPrecondition();
		}
	}
}
