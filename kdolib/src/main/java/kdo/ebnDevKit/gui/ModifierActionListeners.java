/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import kdo.ebn.IEBNPerception;
import kdo.ebn.IResourceBelief;
import kdo.ebnDevKit.ebnAccess.IEbnAccess;
import kdo.ebnDevKit.ebnAccess.IEbnCompetence;
import kdo.ebnDevKit.ebnAccess.IEbnGoal;
import kdo.ebnDevKit.ebnAccess.IEbnPerception;
import kdo.ebnDevKit.ebnAccess.IEbnResource;
import kdo.ebnDevKit.gui.dialogs.CompetenceDialog;
import kdo.ebnDevKit.gui.dialogs.GoalDialog;

/**
 * This class provides action listeners to modify the model. the listeners are
 * nested classes which can be used for buttons or menu items. for modifying the
 * model they use the passed ModifierActionListeners constructor arguments.
 * because of that you must bind an action listener to an instance of this
 * class.
 * @author Thomas Rinklin
 *
 */
public class ModifierActionListeners
{
	private final IEbnAccess ebnAccess;

	private final JFrame parent;

	/**
	 * constructor
	 * @param ebnAccess defines, on which model the action listeners work
	 * @param parent used to show modal dialogs
	 */
	public ModifierActionListeners(IEbnAccess ebnAccess, JFrame parent)
	{
		this.ebnAccess = ebnAccess;
		this.parent = parent;
	}

	/**
	 * shows a component dialog to add or change a competence
	 * @param comp the competence to change or null to create a new competence
	 */
	private void showCompetenceDialog(IEbnCompetence comp)
	{
		CompetenceDialog dialog = new CompetenceDialog(parent, comp, ebnAccess);
		dialog.setVisible(true);
	}

	/**
	 * shows a dialog to remove the passed competence
	 * @param comp the competence to remove
	 */
	private void removeCompetence(IEbnCompetence comp)
	{
		int userSelection = JOptionPane.showConfirmDialog(parent, "Do you really want to delete this Competence?",
				"Delete Competence?", JOptionPane.OK_CANCEL_OPTION);

		if (userSelection == JOptionPane.OK_OPTION)
			ebnAccess.removeCompetence(comp);
	}

	/**
	 * shows a goal dialog to add or change a goal
	 * @param goal the goal to change or null to create a goal
	 */
	private void showGoalDialog(IEbnGoal goal)
	{
		GoalDialog dialog = new GoalDialog(parent, goal, ebnAccess);
		dialog.setVisible(true);
	}

	/**
	 * shows a dialog to remove a goal
	 * @param goal the goal to remove
	 */
	private void removeGoal(IEbnGoal goal)
	{
		int userSelection = JOptionPane.showConfirmDialog(
				parent, "Do you really want to delete this Goal?", "Delete Goal?", JOptionPane.OK_CANCEL_OPTION);

		if (userSelection == JOptionPane.OK_OPTION)
			ebnAccess.removeGoal(goal);
	}

	/**
	 * shows a dialog to add a Perception
	 */
	private void addPerception()
	{
		List<String> allBeliefs = createListOfUnusedBeliefs();

		String selectedBelief = (String) JOptionPane.showInputDialog(parent,
				"Select the Belief for the new Perception:", "New Perception", JOptionPane.PLAIN_MESSAGE, null,
				allBeliefs.toArray(), null);

		if ((selectedBelief != null) && (selectedBelief.length() > 0)) {
			ebnAccess.addPerception(selectedBelief);
		}
	}

	private List<String> createListOfUnusedBeliefs()
	{
		Collection<IEBNPerception> allBeliefs = ebnAccess.getAgent().getBeliefs().values();
		List<String> possiblePercs = new ArrayList<>();

		for (IEBNPerception iBelief : allBeliefs) {
			if (isBeliefUsed(iBelief))
				possiblePercs.add(iBelief.getName());
		}
		return possiblePercs;
	}

	private boolean isBeliefUsed(IEBNPerception iBelief)
	{
		Iterator<? extends IEbnPerception> itUsedBelief = ebnAccess.getPerceptions();
		while (itUsedBelief.hasNext()) {
			IEbnPerception usedPerception = itUsedBelief.next();
			if (usedPerception.isBelief(iBelief)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * shows a dialog to remove a perception
	 * @param perception the perception to remove
	 */
	private void removePerception(IEbnPerception perception)
	{
		int userSelection = JOptionPane.showConfirmDialog(parent, "Do you really want to delete this Perception?",
				"Delete Perception?", JOptionPane.OK_CANCEL_OPTION);

		if (userSelection == JOptionPane.OK_OPTION)
			ebnAccess.removePerception(perception);
	}

	/**
	 * shows a dialog to add a resource
	 */
	private void addResource()
	{
		List<String> possibleRes = createListOfUnusedResources();

		String selectedResource = (String) JOptionPane.showInputDialog(parent, "Select the Resource:", "New Resource",
				JOptionPane.PLAIN_MESSAGE, null, possibleRes.toArray(), null);

		if ((selectedResource != null) && (selectedResource.length() > 0)) {
			ebnAccess.addResource(selectedResource);
		}
	}

	/**
	 * creates a list of unused resources
	 * @return list of unused resources
	 */
	private List<String> createListOfUnusedResources()
	{
		Collection<IResourceBelief> allResources = ebnAccess.getAgent().getResources().values();
		List<String> possibleRes = new ArrayList<String>();

		for (IResourceBelief iResourceBelief : allResources) {
			if (isResourceUsed(iResourceBelief))
				possibleRes.add(iResourceBelief.getName());
		}
		return possibleRes;
	}

	/**
	 * checks if a resource belief is used
	 * @param iResourceBelief the belief to check
	 * @return true if the resource is used, false if not
	 */
	private boolean isResourceUsed(IResourceBelief iResourceBelief)
	{
		Iterator<? extends IEbnResource> itUsedRes = ebnAccess.getResources();
		while (itUsedRes.hasNext()) {
			IEbnResource usedResource = itUsedRes.next();
			if (usedResource.isResource(iResourceBelief)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * shows a dialog to remove a resource
	 * @param resource the resource to remove
	 */
	private void removeResource(IEbnResource resource)
	{
		int userSelection = JOptionPane.showConfirmDialog(parent, "Do you really want to delete this Resource?",
				"Delete Resource?", JOptionPane.OK_CANCEL_OPTION);

		if (userSelection == JOptionPane.OK_OPTION)
			ebnAccess.removeResource(resource);
	}

	public class AddCompetence implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			showCompetenceDialog(null);
		}
	}

	public class EditCompetence implements ActionListener
	{
		private final IEbnCompetence comp;

		public EditCompetence(IEbnCompetence comp)
		{
			this.comp = comp;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			showCompetenceDialog(comp);
		}
	}

	public class RemoveCompetence implements ActionListener
	{
		private final IEbnCompetence comp;

		public RemoveCompetence(IEbnCompetence comp)
		{
			this.comp = comp;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			removeCompetence(comp);
		}
	}

	public class AddGoal implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			showGoalDialog(null);
		}
	}

	public class EditGoal implements ActionListener
	{
		private final IEbnGoal goal;

		public EditGoal(IEbnGoal goal)
		{
			this.goal = goal;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			showGoalDialog(goal);
		}
	}

	public class RemoveGoal implements ActionListener
	{
		private final IEbnGoal goal;

		public RemoveGoal(IEbnGoal goal)
		{
			this.goal = goal;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			removeGoal(goal);
		}
	}

	public class AddPerception implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			addPerception();
		}
	}

	public class RemovePerception implements ActionListener
	{
		private final IEbnPerception perception;

		public RemovePerception(IEbnPerception perception)
		{
			this.perception = perception;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			removePerception(perception);
		}
	}

	public class AddResource implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			addResource();
		}
	}

	public class RemoveResource implements ActionListener
	{
		private final IEbnResource resource;

		public RemoveResource(IEbnResource resource)
		{
			this.resource = resource;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			removeResource(resource);
		}
	}

	@Deprecated
	public IEbnAccess getEbnAccess()
	{
		return ebnAccess;
	}
}
