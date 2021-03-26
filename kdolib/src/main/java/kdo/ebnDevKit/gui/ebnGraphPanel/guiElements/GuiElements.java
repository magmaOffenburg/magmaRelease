/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel.guiElements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import kdo.ebnDevKit.ebnAccess.IEbnAccess;
import kdo.ebnDevKit.ebnAccess.IEbnActivationFlow.IEbnCompetenceActivationFlow;
import kdo.ebnDevKit.ebnAccess.IEbnActivationFlow.IEbnGoalActivationFlow;
import kdo.ebnDevKit.ebnAccess.IEbnCompetence;
import kdo.ebnDevKit.ebnAccess.IEbnGoal;
import kdo.ebnDevKit.ebnAccess.IEbnPerception;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnProposition;
import kdo.ebnDevKit.ebnAccess.IEbnResource;
import kdo.ebnDevKit.ebnAccess.IEbnResource.IEbnResourceProposition;
import kdo.ebnDevKit.gui.PopupMenuBuilder;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.links.CompActivationConnection;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.links.ExecutabilityConnection;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.links.GoalActivationConnection;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.links.GuiConnection;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.links.ResourceConnection;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiCompetence;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiGoal;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiNode;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiPerception;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.nodes.GuiResource;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.util.IIterWrapper;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.util.IterListWrapper;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.util.IterMapWrapper;
import kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.util.ValueScaler;

/**
 * model for the EbnGraphPanel. creates and updates the model for the gui
 * elements
 * @author Thomas Rinklin
 *
 */
public class GuiElements
{
	/** list of gui goal elements */
	private final Map<IEbnGoal, GuiGoal> goals;

	/** list of gui competence elements */
	private final Map<IEbnCompetence, GuiCompetence> competences;

	/** list of gui perception elements */
	private final Map<IEbnPerception, GuiPerception> perceptions;

	/** list of gui resources elements */
	private final Map<IEbnResource, GuiResource> resources;

	/** list of all refreshable elements */
	private final ArrayList<IIterWrapper<IRefreshable>> refreshable;

	/** list of all gui connection elements */
	private final List<GuiConnection<? extends GuiNode, ? extends GuiNode>> connections;

	private final List<ExecutabilityConnection> execConnections;

	private final PopupMenuBuilder popupMenuBuilder;

	private final ArrayList<ResourceConnection> resConnections;

	private ValueScaler actScaler;

	public GuiElements(PopupMenuBuilder popupMenuBuilder)
	{
		this.popupMenuBuilder = popupMenuBuilder;
		goals = new HashMap<IEbnGoal, GuiGoal>();
		competences = new HashMap<IEbnCompetence, GuiCompetence>();
		perceptions = new HashMap<IEbnPerception, GuiPerception>();
		resources = new HashMap<IEbnResource, GuiResource>();
		connections = new ArrayList<GuiConnection<? extends GuiNode, ? extends GuiNode>>();
		execConnections = new ArrayList<ExecutabilityConnection>();
		resConnections = new ArrayList<ResourceConnection>();

		refreshable = new ArrayList<IIterWrapper<IRefreshable>>();
		refreshable.add(new IterMapWrapper<IRefreshable>(goals));
		refreshable.add(new IterMapWrapper<IRefreshable>(competences));
		refreshable.add(new IterMapWrapper<IRefreshable>(perceptions));
		refreshable.add(new IterMapWrapper<IRefreshable>(resources));
		refreshable.add(new IterListWrapper<IRefreshable>(execConnections));
	}

	/**
	 * updates the structure of the gui elements
	 * @param ebnAccess extended behavior network
	 */
	public void update(IEbnAccess ebnAccess)
	{
		goals.clear();
		competences.clear();
		perceptions.clear();
		resources.clear();
		connections.clear();
		execConnections.clear();
		resConnections.clear();

		updatePerceptions(ebnAccess);
		updateResources(ebnAccess);
		updateGoals(ebnAccess);
		updateCompetences(ebnAccess);
		updateGoalActivationFlow(ebnAccess);
		updateCompetenceActivationFlow(ebnAccess);
		// TODO draw degree dingens -> effect.getDegreeProposition();
	}

	private void updateCompetenceActivationFlow(IEbnAccess ebnAccess)
	{
		Iterator<? extends IEbnCompetenceActivationFlow> itCompActivationFlow = ebnAccess.getCompetenceActivationFlow();
		while (itCompActivationFlow.hasNext()) {
			IEbnCompetenceActivationFlow ebnCompActivationFlow = itCompActivationFlow.next();
			GuiCompetence source = competences.get(ebnCompActivationFlow.getSourceCompetence());
			GuiCompetence target = competences.get(ebnCompActivationFlow.getTargetCompetence());

			connections.add(new CompActivationConnection(source, target, ebnCompActivationFlow));
		}
	}

	private void updateGoalActivationFlow(IEbnAccess ebnAccess)
	{
		Iterator<? extends IEbnGoalActivationFlow> itGoalActivationFlow = ebnAccess.getGoalActivationFlow();
		while (itGoalActivationFlow.hasNext()) {
			IEbnGoalActivationFlow ebnGoalActivationFlow = itGoalActivationFlow.next();
			GuiGoal source = goals.get(ebnGoalActivationFlow.getSourceGoal());
			GuiCompetence target = competences.get(ebnGoalActivationFlow.getTargetCompetence());
			connections.add(new GoalActivationConnection(source, target, ebnGoalActivationFlow));
		}
	}

	private void updateCompetences(IEbnAccess ebnAccess)
	{
		actScaler = new ValueScaler();

		Iterator<? extends IEbnCompetence> itComp = ebnAccess.getCompetenceModules();
		while (itComp.hasNext()) {
			IEbnCompetence comp = itComp.next();

			GuiCompetence guiComp = new GuiCompetence(comp, actScaler, popupMenuBuilder);
			competences.put(comp, guiComp);

			updateExecutabilityConnection(guiComp);
			updateResourceConnection(guiComp);
		}
	}

	private void updateResourceConnection(GuiCompetence guiComp)
	{
		Iterator<? extends IEbnResourceProposition> itResurce = guiComp.getCompetence().getResources();
		while (itResurce.hasNext()) {
			IEbnResourceProposition ebnResourceProp = itResurce.next();

			ResourceConnection resConn = new ResourceConnection(guiComp, resources.get(ebnResourceProp.getResource()));
			resConnections.add(resConn);
		}
	}

	private void updateExecutabilityConnection(GuiCompetence guiComp)
	{
		Iterator<? extends IEbnProposition> itPreconditions = guiComp.getCompetence().getPreconditions();
		while (itPreconditions.hasNext()) {
			IEbnProposition ebnProposition = itPreconditions.next();

			ExecutabilityConnection execConn = new ExecutabilityConnection(
					guiComp, perceptions.get(ebnProposition.getPerception()), ebnProposition);
			execConnections.add(execConn);
		}
	}

	private void updateGoals(IEbnAccess ebnAccess)
	{
		Iterator<? extends IEbnGoal> itGoals = ebnAccess.getGoals();
		while (itGoals.hasNext()) {
			IEbnGoal goal = itGoals.next();
			goals.put(goal, new GuiGoal(goal, popupMenuBuilder));
		}
	}

	private void updateResources(IEbnAccess ebnAccess)
	{
		Iterator<? extends IEbnResource> itRes = ebnAccess.getResources();
		while (itRes.hasNext()) {
			IEbnResource resource = itRes.next();
			resources.put(resource, new GuiResource(resource, popupMenuBuilder));
		}
	}

	private void updatePerceptions(IEbnAccess ebnAccess)
	{
		Iterator<? extends IEbnPerception> itPerc = ebnAccess.getPerceptions();
		while (itPerc.hasNext()) {
			IEbnPerception perception = itPerc.next();
			perceptions.put(perception, new GuiPerception(perception, popupMenuBuilder));
		}
	}

	/**
	 * refresh all values
	 */
	public void refreshAllValues()
	{
		for (IIterWrapper<IRefreshable> refrIterWrapper : refreshable) {
			for (IRefreshable refrEl : refrIterWrapper.getIterable()) {
				refrEl.refreshValues();
			}
		}
		actScaler.markRound();
	}

	/**
	 * returns all gui goals
	 * @return all gui goals
	 */
	public Iterator<GuiGoal> getGoals()
	{
		List<GuiGoal> sorted = new LinkedList<GuiGoal>(goals.values());
		Collections.sort(sorted);
		return sorted.iterator();
	}

	/**
	 * returns all gui competences
	 * @return all gui competences
	 */
	public Iterator<GuiCompetence> getCompetences()
	{
		List<GuiCompetence> sorted = new LinkedList<GuiCompetence>(competences.values());
		Collections.sort(sorted);
		return sorted.iterator();
	}

	/**
	 * returns all gui perceptions
	 * @return all gui perceptions
	 */
	public Iterator<GuiPerception> getPercetions()
	{
		List<GuiPerception> sorted = new LinkedList<GuiPerception>(perceptions.values());
		Collections.sort(sorted);
		return sorted.iterator();
	}

	/**
	 * returns all gui connections
	 * @return all gui connections
	 */
	public Iterator<GuiConnection<? extends GuiNode, ? extends GuiNode>> getConnections()
	{
		List<GuiConnection<? extends GuiNode, ? extends GuiNode>> retList =
				new ArrayList<GuiConnection<? extends GuiNode, ? extends GuiNode>>();
		retList.addAll(connections);
		retList.addAll(execConnections);
		retList.addAll(resConnections);

		return retList.iterator();
	}

	/**
	 * returns all gui resources
	 * @return all gui resources
	 */
	public Iterator<GuiResource> getResources()
	{
		List<GuiResource> sorted = new LinkedList<GuiResource>(resources.values());
		Collections.sort(sorted);
		return sorted.iterator();
	}
}
