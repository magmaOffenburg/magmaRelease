/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnTreePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import kdo.ebnDevKit.ebnAccess.IEbnAccess;
import kdo.ebnDevKit.ebnAccess.IEbnCompetence;
import kdo.ebnDevKit.ebnAccess.IEbnGoal;
import kdo.ebnDevKit.ebnAccess.IEbnPerception;
import kdo.ebnDevKit.gui.DefaultEbnPanel;

/**
 * ebn view component which display a ebn as a tree
 * @author Thomas Rinklin
 *
 */
public class EbnTreePanel extends DefaultEbnPanel
{
	private static final long serialVersionUID = 4045117115053351229L;

	private JTree tree;

	private DefaultMutableTreeNode root;

	private DefaultMutableTreeNode goals;

	private DefaultMutableTreeNode perceptions;

	private DefaultMutableTreeNode competences;

	private DefaultTreeModel model;

	public EbnTreePanel(JFrame parent, IEbnAccess ebnAccess)
	{
		super(parent, ebnAccess);
		this.setLayout(new BorderLayout());

		addTree();
	}

	private void addTree()
	{
		root = new DefaultMutableTreeNode("EBN");
		model = new DefaultTreeModel(root);
		tree = new JTree(model);

		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setPreferredSize(new Dimension(400, 800));
		add(scrollPane, 0);

		goals = new DefaultMutableTreeNode("Goals");
		root.add(goals);

		perceptions = new DefaultMutableTreeNode("Perceptrons");
		root.add(perceptions);

		competences = new DefaultMutableTreeNode("Competences");
		root.add(competences);

		model.nodeStructureChanged(root);
	}

	private void updateTree()
	{
		root.add(new DefaultMutableTreeNode("Test"));

		updateGoals();

		updatePerceptions();

		updateCompetences();

		model.nodeStructureChanged(goals);
		model.nodeStructureChanged(perceptions);
		model.nodeStructureChanged(competences);

		for (int i = 0; i < goals.getChildCount(); i++) {
			tree.expandPath(new TreePath(model.getPathToRoot(goals.getChildAt(i))));
		}

		for (int i = 0; i < perceptions.getChildCount(); i++) {
			tree.expandPath(new TreePath(model.getPathToRoot(perceptions.getChildAt(i))));
		}

		for (int i = 0; i < competences.getChildCount(); i++) {
			tree.expandPath(new TreePath(model.getPathToRoot(competences.getChildAt(i))));
		}
	}

	private void updateCompetences()
	{
		competences.removeAllChildren();
		Iterator<? extends IEbnCompetence> itCompetence = ebnAccess.getCompetenceModules();

		while (itCompetence.hasNext()) {
			IEbnCompetence competence = itCompetence.next();
			DefaultMutableTreeNode competenceNode = new DefaultMutableTreeNode(competence.getName());
			competences.add(competenceNode);

			competenceNode.add(new DefaultMutableTreeNode("Activation: " + competence.getActivation()));
			competenceNode.add(new DefaultMutableTreeNode("Executability: " + competence.getExecutability()));
			competenceNode.add(new DefaultMutableTreeNode(
					"Activation + Executability: " + competence.getActivationAndExecutability()));
			competenceNode.add(new DefaultMutableTreeNode("Main Activation: " + competence.getMainActivation()));
		}
	}

	private void updatePerceptions()
	{
		perceptions.removeAllChildren();
		Iterator<? extends IEbnPerception> itPerceptron = ebnAccess.getPerceptions();

		while (itPerceptron.hasNext()) {
			IEbnPerception perception = itPerceptron.next();
			DefaultMutableTreeNode preceptronNode = new DefaultMutableTreeNode(perception.getName());
			perceptions.add(preceptronNode);

			preceptronNode.add(new DefaultMutableTreeNode("Activation: " + perception.getActivation()));

			preceptronNode.add(new DefaultMutableTreeNode("TruthValue: " + perception.getTruthValue()));
		}
	}

	private void updateGoals()
	{
		goals.removeAllChildren();
		Iterator<? extends IEbnGoal> itGoal = ebnAccess.getGoals();
		while (itGoal.hasNext()) {
			IEbnGoal goal = itGoal.next();
			DefaultMutableTreeNode goalNode = new DefaultMutableTreeNode(goal.getName());
			goals.add(goalNode);

			goalNode.add(new DefaultMutableTreeNode("Index: " + goal.getIndex()));

			goalNode.add(new DefaultMutableTreeNode("Activation: " + goal.getActivation()));
			goalNode.add(new DefaultMutableTreeNode("Importance: " + goal.getImportance()));
			goalNode.add(new DefaultMutableTreeNode("Relevance: " + goal.getRelevance()));
			goalNode.add(new DefaultMutableTreeNode(
					"GoalCondition: " + (goal.getGoalCondition().isNegated() ? "not " : "") +
					goal.getGoalCondition().getPerception().getName() + " -> " + (goal.getGoalConditionTruthValue())));
		}
	}

	@Override
	protected void ebnValuesChanged()
	{
		updateTree();
	}

	@Override
	protected void ebnStructureChanged()
	{
		updateTree();
	}

	@Override
	public String getName()
	{
		return super.getName() + " (Tree View)";
	}
}
