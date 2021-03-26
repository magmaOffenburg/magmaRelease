/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kdo.ebn.ExtendedBehaviorNetwork;
import kdo.ebn.IEBNAction;
import kdo.ebn.IEBNPerception;
import kdo.ebn.observation.EbnSubject;
import kdo.ebn.observation.IEbnObserver;
import kdo.ebn.observation.ISubscribeUnsubscribe;
import kdo.ebn.xml.NetworkSerializer;
import kdo.ebnDevKit.agent.IEbnAgent;
import kdo.ebnDevKit.ebnAccess.IEbnAccess;
import kdo.ebnDevKit.ebnAccess.IEbnCompetence;
import kdo.ebnDevKit.ebnAccess.IEbnGoal;
import kdo.ebnDevKit.ebnAccess.IEbnPerception;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnEffect;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnProposition;
import kdo.ebnDevKit.ebnAccess.IEbnResource;
import kdo.ebnDevKit.ebnAccess.IEbnResource.IEbnResourceProposition;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnCompetence;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnCompetenceActivationFlow;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnGoal;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnGoalActivationFlow;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnPerception;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnResource;

/**
 * class to control the access to the ebn
 * @author Thomas Rinklin
 *
 */
public class EbnAccess implements IEbnAccess
{
	/** reference to the current ebn */
	private ExtendedBehaviorNetwork network;

	/** contains the logic to subscribe and unsubscribe */
	private EbnSubject observation;

	/** reference to the agent */
	private final IEbnAgent agent;

	/** observer object to observe the ebn */
	private IEbnObserver observer;

	/** rebuild the ebn objects and hold the references to them */
	private EbnObjectsGenerator ebnObjGen;

	/** modifies the ebn */
	private EbnModifier ebnModifier;

	/**
	 * constructor to create an empty ebn
	 * @param agent reference to the agent
	 */
	public EbnAccess(IEbnAgent agent)
	{
		this.agent = agent;

		this.network = new ExtendedBehaviorNetwork(agent.getName(), generateBehaviors());

		init();
	}

	/**
	 * constructor to create an ebn from file
	 * @param agent reference to an agent
	 * @param f reference to the ebn file
	 */
	public EbnAccess(IEbnAgent agent, File f)
	{
		this.agent = agent;

		this.network = load(f);

		init();
	}

	/**
	 * inits the array, maps and observer objects
	 */
	private void init()
	{
		ebnObjGen = new EbnObjectsGenerator(network);
		ebnModifier = new EbnModifier(network, agent);

		observation = new EbnSubject();

		observer = new IEbnObserver() {
			@Override
			public void valuesChanged()
			{
				observation.onValueChange();
			}

			@Override
			public void structureChanged()
			{
				ebnObjGen.regenerateEbnObjects();
				observation.onStructureChange();
			}
		};

		attachNetwork();
	}

	@Override
	public ISubscribeUnsubscribe observe()
	{
		return observation;
	}

	@Override
	public void addPerception(String beliefName)
	{
		ebnModifier.addPerceptionStep(beliefName);
		restart();
	}

	@Override
	public void removePerception(IEbnPerception perception)
	{
		if (!isPerceptionUsed(perception)) {
			ebnModifier.removePerceptionStep((EbnPerception) perception);
			restart();
		}
	}

	@Override
	public boolean isPerceptionUsed(IEbnPerception perception)
	{
		for (IEbnGoal goal : ebnObjGen.getGoals().values()) {
			if (goal.isPerceptionUsed(perception))
				return true;
		}

		for (IEbnCompetence ebnComp : ebnObjGen.getCompetences().values()) {
			if (ebnComp.perceptionIsUsed(perception))
				return true;
		}

		return false;
	}

	@Override
	public void addResource(String resourceName)
	{
		ebnModifier.addResourceStep(resourceName);
		restart();
	}

	@Override
	public void removeResource(IEbnResource resource)
	{
		if (!isResourceUsed(resource)) {
			ebnModifier.removeResourceStep((EbnResource) resource);
			restart();
		}
	}

	@Override
	public boolean isResourceUsed(IEbnResource resource)
	{
		for (IEbnCompetence ebnComp : ebnObjGen.getCompetences().values()) {
			if (ebnComp.isResourceUsed(resource))
				return true;
		}
		return false;
	}

	@Override
	public void addCompetence(List<IEBNAction> actions, List<IEbnResourceProposition> resources,
			List<IEbnProposition> preconditions, List<IEbnEffect> effects)
	{
		ebnModifier.addCompetenceStep(actions, resources, preconditions, effects);
		restart();
	}

	@Override
	public void changeCompetence(IEbnCompetence comp, List<IEBNAction> actions, List<IEbnResourceProposition> resources,
			List<IEbnProposition> preconditions, List<IEbnEffect> effects)
	{
		EbnCompetence ebnComp = (EbnCompetence) comp;
		ebnModifier.removeCompetenceStep(ebnComp.getCompetence());
		ebnModifier.addCompetenceStep(actions, resources, preconditions, effects);
		restart();
	}

	@Override
	public void removeCompetence(IEbnCompetence comp)
	{
		EbnCompetence ebnComp = (EbnCompetence) comp;
		ebnModifier.removeCompetenceStep(ebnComp.getCompetence());
		restart();
	}

	@Override
	public void addGoal(IEbnPerception perception, boolean isNegated, double importance,
			List<? extends IEbnProposition> relevanceConditions)
	{
		ebnModifier.addGoalStep((EbnPerception) perception, isNegated, importance, relevanceConditions);
		restart();
	}

	@Override
	public void changeGoal(
			IEbnGoal goal, boolean isNegated, double importance, List<? extends IEbnProposition> relevanceConditions)
	{
		EbnPerception ebnPerc = (EbnPerception) goal.getGoalCondition().getPerception();

		ebnModifier.removeGoalStep((EbnGoal) goal);
		ebnModifier.addGoalStep(ebnPerc, isNegated, importance, relevanceConditions);

		restart();
	}

	@Override
	public void removeGoal(IEbnGoal goal)
	{
		detachNetwork();

		ebnModifier.removeGoalStep((EbnGoal) goal);

		rebuildNetwork();
		attachNetwork();
	}

	@Override
	public Iterator<EbnGoal> getGoals()
	{
		return ebnObjGen.getGoals().values().iterator();
	}

	@Override
	public Iterator<EbnPerception> getPerceptions()
	{
		return ebnObjGen.getPerceptions().values().iterator();
	}

	@Override
	public Iterator<EbnResource> getResources()
	{
		return ebnObjGen.getResources().values().iterator();
	}

	@Override
	public Iterator<EbnCompetence> getCompetenceModules()
	{
		return ebnObjGen.getCompetences().values().iterator();
	}

	@Override
	public Iterator<EbnGoalActivationFlow> getGoalActivationFlow()
	{
		return ebnObjGen.getGoalActivationFlow().iterator();
	}

	@Override
	public Iterator<EbnCompetenceActivationFlow> getCompetenceActivationFlow()
	{
		return ebnObjGen.getCompetenceActivationFlow().iterator();
	}

	@Override
	public IEbnAgent getAgent()
	{
		return agent;
	}

	/**
	 * completely restarts the current network (detach, rebuild, attatch)
	 */
	private void restart()
	{
		detachNetwork();  // detatch from old network
		rebuildNetwork(); // replaceNetwork
		attachNetwork();  // attach to the new network
	}

	/**
	 * detaches the observer from the network and the network from the agent
	 */
	private void detachNetwork()
	{
		network.observe().detach(observer); // detach from ebn
		agent.connectEbn(null);				// detach agent from ebn
	}

	/**
	 * attaches the observer to the network, the network to the agent, rebuilds
	 * the generated structure and calls the listeners
	 */
	private void attachNetwork()
	{
		agent.connectEbn(network);			// attach agent to ebn
		network.observe().attach(observer); // attach to ebn
		ebnObjGen.regenerateEbnObjects();	// build new structure
		observation.onStructureChange();	// inform observes
	}

	/**
	 * rebuilds the network by serializing and deserializing
	 */
	private void rebuildNetwork()
	{
		NetworkSerializer ns = new NetworkSerializer(generateMergedBeliefs(), generateBehaviors());
		String xml = ns.storeNetwork(network); // ebn->xml
		network = ns.loadNetwork(xml);		   // xml->ebn
		ebnObjGen.setNetwork(network);
		ebnModifier.setNetwork(network);
	}

	@Override
	public void save(File f)
	{
		NetworkSerializer ns = new NetworkSerializer(generateMergedBeliefs(), generateBehaviors());
		ns.storeNetworkToFile(f, network);
	}

	/**
	 * Loads an Extended Behavior Network from file
	 *
	 * @param f File object
	 * @return Generated ExtendedBehaviorNetwork object
	 */
	private ExtendedBehaviorNetwork load(File f)
	{
		NetworkSerializer ns = new NetworkSerializer(generateMergedBeliefs(), generateBehaviors());
		return ns.loadNetworkFromFile(f);
	}

	/**
	 * Generate a new map with the behaviors from the agent
	 * @return Generated map
	 */
	private Map<String, IEBNAction> generateBehaviors()
	{
		Map<String, IEBNAction> behaviors = new HashMap<String, IEBNAction>();
		behaviors.putAll(agent.getBehaviors());
		return behaviors;
	}

	/**
	 * Generates a new map with the merged beliefs and resources
	 * @return Generated map
	 */
	private Map<String, IEBNPerception> generateMergedBeliefs()
	{
		Map<String, IEBNPerception> mergedBeliefs = new HashMap<String, IEBNPerception>();
		mergedBeliefs.putAll(agent.getBeliefs());
		mergedBeliefs.putAll(agent.getResources());
		return mergedBeliefs;
	}
}