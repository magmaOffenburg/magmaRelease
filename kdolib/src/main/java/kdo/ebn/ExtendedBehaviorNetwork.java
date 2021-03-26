/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import kdo.ebn.observation.EbnSubject;
import kdo.ebn.observation.ISubscribeUnsubscribe;

/**
 * Represents extended behavior networks.
 */
public class ExtendedBehaviorNetwork implements INetworkConstants
{
	/** number of tries to execute an ebn */
	public static final int EXECUTION_TRIES = 10;

	/** network parameters define the runtime behavior of the network */
	protected final NetworkParams networkParams;

	/** list of all perceptions the agent has */
	protected final List<PerceptionNode> perceptionNodes;

	/** the resource nodes of the network */
	protected final List<Resource> resources;

	/** list of all competence modules (behavior rules) of the network */
	protected final List<Competence> competenceModules;

	/** list of all goals of the network */
	protected final List<Goal> goals;

	/** contains logic to provide observation functionality */
	protected final EbnSubject observation;

	/** the actions available to this decision maker */
	private Map<String, IEBNAction> behaviors;

	/**
	 * The default constructor of the control. The logic will be set by default
	 * to not multi threaded. This means only one logic could be executed at one
	 * time.
	 *
	 * @param controlName The name of the control representing the logic like a
	 *        workflow
	 * @param behaviors List of available behaviors
	 */
	public ExtendedBehaviorNetwork(String controlName, Map<String, IEBNAction> behaviors)
	{
		this.behaviors = behaviors;
		networkParams = new NetworkParams();
		goals = new ArrayList<Goal>();
		competenceModules = new ArrayList<Competence>();
		perceptionNodes = new ArrayList<PerceptionNode>();
		resources = new ArrayList<Resource>();
		observation = new EbnSubject();
	}

	/**
	 * Loops through the childControls and call the perform method on them.
	 */
	public boolean decide()
	{
		boolean ruleExecuted = false;

		updatePerceptions();
		observation.onValueChange();

		for (int i = 0; i < EXECUTION_TRIES && !ruleExecuted; i++) {
			spreadActivation();
			ruleExecuted = performActions();
			observation.onValueChange();
		}
		return ruleExecuted;
	}

	/**
	 * Called to update all truth values of perceptions. Extends the superclass
	 * implementation by also recalculating the relevance condition's truth
	 * values
	 */
	void updatePerceptions()
	{
		NetworkNode control = null;

		for (PerceptionNode perceptionNode : perceptionNodes) {
			control = perceptionNode;
			control.perform();
		}
	}

	/**
	 * Calculates the new input to the network and the spreading of activation
	 * within the network.
	 */
	void spreadActivation()
	{
		for (Competence competenceModule2 : competenceModules) {
			competenceModule2.calculateExecutability();
		}

		for (Competence competenceModule1 : competenceModules) {
			competenceModule1.calculateActivation();
		}

		calculateSpreading();

		for (Competence competenceModule : competenceModules) {
			competenceModule.setToNewActivation();
		}
	}

	/**
	 * Checks all modules if executable and executes them. Overwrites the default
	 * implementation in order to allow multiple modules to be executed.
	 *
	 * @return true if at least one rule/module was executed.
	 */
	boolean performActions()
	{
		TreeSet<Competence> orderedModules = getSortedModules();
		resetResourceUsage();
		boolean ruleExecuted = performModules(orderedModules);
		reduceThreshold();
		return ruleExecuted;
	}

	/**
	 * @return an ordered set containing the competence modules sorted by
	 *         ModuleDescendingComparator
	 */
	private TreeSet<Competence> getSortedModules()
	{
		TreeSet<Competence> orderedModules = new TreeSet<Competence>(new ModuleDescendingComparator());

		// get a sorted collection of competence modules
		for (Competence currentRule : competenceModules) {
			if (currentRule.getActions().hasNext()) {
				orderedModules.add(currentRule);
			}
		}
		return orderedModules;
	}

	/**
	 * Resets the amount of available resource units for all resource nodes
	 */
	private void resetResourceUsage()
	{
		for (Resource nextResource : resources) {
			nextResource.resetResource();
		}
	}

	/**
	 * @param orderedModules the ordered set of competence modules
	 * @return true if at least one module was executed
	 */
	boolean performModules(TreeSet<Competence> orderedModules)
	{
		boolean ruleExecuted = false;
		try {
			Iterator<Competence> orderedModulesIterator = orderedModules.iterator();
			Competence competenceModule = null;

			while (orderedModulesIterator.hasNext()) {
				competenceModule = orderedModulesIterator.next();
				competenceModule.perform();
				ruleExecuted |= competenceModule.isExecuted();

				if (!networkParams.getConcurrentActions()) {
					break;
				}
			}
		} catch (Exception ex) {
			System.out.println("Error when performing action: " + ex);
		}
		return ruleExecuted;
	}

	/**
	 * Reduces the activation threshold of all resource nodes
	 */
	void reduceThreshold()
	{
		for (Resource nextResource : resources) {
			nextResource.reduceActivationLevel(networkParams.getThetaReduction());
		}
	}

	/**
	 * @return an iterator for the list of goals
	 */
	public Iterator<Goal> getGoals()
	{
		return goals.iterator();
	}

	/**
	 * @return an iterator for the list of competence modules
	 */
	public Iterator<Competence> getCompetenceModules()
	{
		return competenceModules.iterator();
	}

	/**
	 * @return an iterator for the list of perceptions
	 */
	public Iterator<PerceptionNode> getPerceptions()
	{
		return perceptionNodes.iterator();
	}

	/**
	 * @return the network parameters
	 */
	public NetworkParams getNetworkParams()
	{
		return networkParams;
	}

	/**
	 * @return the number of goals
	 */
	public int getGoalsCount()
	{
		return goals.size();
	}

	/**
	 * @return the number of competence modules
	 */
	public int getCompetenceModulesCount()
	{
		return competenceModules.size();
	}

	/**
	 * @return the number of perceptions
	 */
	public int getPerceptionsCount()
	{
		return perceptionNodes.size();
	}

	/**
	 * Called to perform activation spreading. The default implementation does
	 * nothing
	 */
	protected void calculateSpreading()
	{
		// empty
	}

	/**
	 * Adds a new perception of an agent to the list of perceptions. It is not
	 * checked right now, if the perception is already existing.
	 *
	 * @param perceptionNode - new perception to be added
	 */
	public void addPerception(PerceptionNode perceptionNode)
	{
		perceptionNodes.add(perceptionNode);
		observation.onStructureChange();
	}

	/**
	 * @param name the name of the behavior to get
	 * @return the behavior with the passed name if existing
	 * @throws NetworkConfigurationException if no behavior with the passed name
	 *         is existing
	 */
	public IEBNAction getBehaviorByName(String name) throws NetworkConfigurationException
	{
		IEBNAction behavior = behaviors.get(name);
		if (behavior == null) {
			throw new NetworkConfigurationException("Behavior not existing: " + name);
		}
		return behavior;
	}

	/**
	 * @param name the name of the perception to get
	 * @return the perception with passed name, null if not found
	 */
	public PerceptionNode getPerception(String name)
	{
		for (PerceptionNode perceptionNode : perceptionNodes) {
			if (perceptionNode.getName().equals(name)) {
				return perceptionNode;
			}
		}
		return null;
	}

	/**
	 * Returns the perception node specified by name. If not yet existing creates
	 * one.
	 * @param name the name of the belief
	 * @return perception network node
	 * @throws NetworkConfigurationException if no belief exists with that name
	 */
	public PerceptionNode getOrCreatePerception(String name) throws NetworkConfigurationException
	{
		PerceptionNode result = getPerception(name);
		if (result == null) {
			/*
			 * IBelief beliefs = this.beliefs.get(name); if (beliefs == null) {
			 * throw new NetworkConfigurationException("Belief not existing: " +
			 * name); } result = new PerceptionNode(beliefs);
			 * addPerception(result);
			 */
		}
		return result;
	}

	/**
	 * @param name the name of the resource node to get
	 * @return the resource node with passed name, null if not found
	 */
	public Resource getResource(String name)
	{
		for (Resource resource : resources) {
			if (resource.getName().equals(name)) {
				return resource;
			}
		}
		return null;
	}

	/**
	 * Returns the resource node specified by name. If not yet existing creates
	 * one.
	 * @param name the name of the belief
	 * @return resource network node
	 * @throws NetworkConfigurationException if no belief exists with that name
	 */
	public NetworkNode getOrCreateResource(String name) throws NetworkConfigurationException
	{
		Resource result = getResource(name);
		if (result == null) {
			try {
				/*
				 * IResourceBelief belief = (IResourceBelief) beliefs.get(name); if
				 * (belief == null) { throw new
				 * NetworkConfigurationException("Belief not existing: " + name); }
				 * result = new Resource(belief); addResource(result);
				 */
			} catch (ClassCastException e) {
				throw new NetworkConfigurationException("Resource has name of non resource belief: " + name);
			}
		}
		return result;
	}

	/**
	 * Factory method to create a new competence module
	 *
	 * @param name Competence module name
	 * @return The newly created competence module
	 */
	protected Competence createNewCompetence(String name)
	{
		return new Competence(name, goals.size(), networkParams);
	}

	/**
	 * Factory method to create a new goal
	 * @param goalName The name / identifier of the goal.
	 * @param index the index this goal has within all goals of the network
	 * @return the newly created goal
	 */
	protected Goal createNewGoal(String goalName, int index)
	{
		return new Goal(goalName, index);
	}

	/**
	 * Factory method to create a new resource
	 *
	 * @param belief Resource belief to assign to the created Resource
	 * @return The newly created resource
	 */
	protected Resource createNewResource(IResourceBelief belief)
	{
		return new Resource(belief);
	}

	/**
	 * Adds a resource to the list of resources
	 * @param resource a resource node to be added
	 */
	public void addResource(Resource resource)
	{
		resource.setActivation(networkParams.getTheta());
		resource.setInitialActivation(networkParams.getTheta());
		resources.add(resource);
	}

	/**
	 * @return an iterator for the list of resources
	 */
	public Iterator<Resource> getResources()
	{
		return resources.iterator();
	}

	/**
	 * Adds a new competence module (behavior rule) to the network. The module is
	 * connected to the existing modules. This is done before adding it in order
	 * to avoid that the module is connected to itself. It is not checked right
	 * now, if the module is already existing.
	 *
	 * @param module - new competence to be added to the EBN
	 * @throws NetworkConfigurationException If the competence module could not
	 *         be added to the network
	 */
	public void addCompetenceModule(Competence module) throws NetworkConfigurationException
	{
		module.setNetworkParams(networkParams);
		module.connectCompetence(getCompetenceModules(), getGoals(), getResources());
		competenceModules.add(module);
		// observation.onStructureChange();
	}

	/**
	 * Adds a new goal of the agent to the list of goals. It is not checked right
	 * now, if the goal is already existing.
	 *
	 * @param goal - new goal to be added to the EBN
	 */
	public void addGoal(Goal goal)
	{
		goals.add(goal);
		// observation.onStructureChange();
	}

	/**
	 * Removes goal from list of goals. don't use this method, if you want to use
	 * this instance of the ebn network afterwards, because maybe there are still
	 * some references to this goal existing inside of the ebn-structure. you can
	 * use it, if you serialize and deserialize this eben network.
	 * @param goal goal to remove
	 */
	public void removeGoal(Goal goal)
	{
		goals.remove(goal);
		// observation.onStructureChange();
	}

	/**
	 * Removes perception from list of perceptions. don't use this method, if you
	 * want to use this instance of the ebn network afterwards, because maybe
	 * there are still some references to this perception existing inside of the
	 * ebn-structure. you can use it, if you serialize and deserialize this eben
	 * network.
	 * @param perceptionNode perception to remove
	 */
	public void removePerception(PerceptionNode perceptionNode)
	{
		perceptionNodes.remove(perceptionNode);
		// observation.onStructureChange();
	}

	/**
	 * Removes resource from list of resources. don't use this method, if you
	 * want to use this instance of the ebn network afterwards, because maybe
	 * there are still some references to this resource existing inside of the
	 * ebn-structure. you can use it, if you serialize and deserialize this eben
	 * network.
	 * @param resource resource to remove
	 */
	public void removeResource(Resource resource)
	{
		resources.remove(resource);
		// observation.onStructureChange();
	}

	/**
	 * Removes competence from list of competence. don't use this method, if you
	 * want to use this instance of the ebn network afterwards, because maybe
	 * there are still some references to this competence existing inside of the
	 * ebn-structure. you can use it, if you serialize and deserialize this eben
	 * network.
	 * @param comp competence to remove
	 */
	public void removeCompetence(Competence comp)
	{
		competenceModules.remove(comp);
		// observation.onStructureChange();
	}

	/**
	 * Returns the information about network.
	 *
	 * @param tabs - the number of tabs used to make indentation in the output.
	 * @return the information about this control.
	 */
	public String toString(int tabs)
	{
		StringBuffer info = new StringBuffer(200);

		info.append(PrintUtil.addTabs(tabs)).append("extended behavior network").append(":");

		StringBuffer tempBuffer = new StringBuffer(200);

		for (Goal goal : goals) {
			tempBuffer.append(goal.toString(tabs + 1));
		}

		if (tempBuffer.length() > 0) {
			info.append(PrintUtil.addTabs(tabs)).append("EBN contains following goals:").append(tempBuffer.toString());
			tempBuffer = new StringBuffer(100);
		}
		for (Resource resource : resources) {
			tempBuffer.append(resource.toString(tabs + 1));
		}

		if (tempBuffer.length() > 0) {
			info.append(PrintUtil.addTabs(tabs))
					.append("EBN contains following resources:")
					.append(tempBuffer.toString());
			tempBuffer = new StringBuffer(100);
		}

		info.append(PrintUtil.addTabs(tabs)).append("EBN parameters are:").append(networkParams.toString(tabs + 1));

		for (Competence competenceModule : competenceModules) {
			tempBuffer.append(PrintUtil.addTabs(tabs)).append(competenceModule.toString(tabs + 1));
		}

		if (tempBuffer.length() > 0) {
			info.append(PrintUtil.addTabs(tabs))
					.append("EBN contains following competence modules:")
					.append(tempBuffer.toString());
		}

		return info.append("\r\n").toString();
	}

	/**
	 * Returns interface that provides observation functionality
	 * @return interface that provides observation functionality
	 */
	public ISubscribeUnsubscribe observe()
	{
		return observation;
	}

	/**
	 * Comparator used to order competences in descending mode
	 * @author Last modified by $Author: psi $
	 * @version $Revision: 72088 $
	 */
	private class ModuleDescendingComparator implements Comparator<Competence>, Serializable
	{
		/** id for serialization */
		private static final long serialVersionUID = 1L;

		/**
		 * Compares two competences.
		 * @param firstModule - first competence to be compared
		 * @param secondModule - second competence to be compared
		 * @return -1 if o1 is bigger then o2, 1 - if o2 is bigger or equal to o1.
		 */
		@Override
		public int compare(Competence firstModule, Competence secondModule)
		{
			double value1 = firstModule.getActivationAndExecutability();
			double value2 = secondModule.getActivationAndExecutability();

			if (value1 > value2) {
				return -1;

			} else if (value1 <= value2) {
				return 1;
			} else {
				return 0;
			}
		}
	}
}
