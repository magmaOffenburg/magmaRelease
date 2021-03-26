/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess.impl;

import java.util.List;
import kdo.ebn.Competence;
import kdo.ebn.Condition;
import kdo.ebn.Effect;
import kdo.ebn.ExtendedBehaviorNetwork;
import kdo.ebn.Goal;
import kdo.ebn.IEBNAction;
import kdo.ebn.IEBNPerception;
import kdo.ebn.IResourceBelief;
import kdo.ebn.NetworkConfigurationException;
import kdo.ebn.PerceptionNode;
import kdo.ebn.Proposition;
import kdo.ebn.Resource;
import kdo.ebn.ResourceProposition;
import kdo.ebnDevKit.agent.IEbnAgent;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnEffect;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnProposition;
import kdo.ebnDevKit.ebnAccess.IEbnResource.IEbnResourceProposition;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnGoal;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnPerception;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnResource;

/**
 * this class is responisble for modifying the ebn. note that it leafs the ebn
 * in an inconsistent state. after the change steps you must restart the ebn
 * @author Thomas Rinklin
 *
 */
public class EbnModifier
{
	private ExtendedBehaviorNetwork network;

	private final IEbnAgent agent;

	public EbnModifier(ExtendedBehaviorNetwork network, IEbnAgent agent)
	{
		this.network = network;
		this.agent = agent;
	}

	/**
	 * adds a goal to the ebn
	 */
	public void addGoalStep(EbnPerception perception, boolean isNegated, double importance,
			List<? extends IEbnProposition> relevanceConditions)
	{
		Goal goal = new Goal(perception.getName(), network.getGoalsCount());

		Proposition prop = new Proposition(goal, perception.getPerception(), isNegated);
		goal.setGoalCondition(prop);
		goal.setImportance(importance);

		Condition relCond = new Condition();

		for (IEbnProposition ebnProposition : relevanceConditions) {
			EbnPerception perc = (EbnPerception) ebnProposition.getPerception();
			relCond.addProposition(new Proposition(goal, perc.getPerception(), ebnProposition.isNegated()));
		}

		goal.setRelevanceCondition(relCond);
		network.addGoal(goal);
	}

	/**
	 * removes a goal from the ebn
	 */
	public void removeGoalStep(EbnGoal goal)
	{
		network.removeGoal(goal.getGoal());
	}

	/**
	 * adds a competence to the ebn
	 */
	public void addCompetenceStep(List<IEBNAction> actions, List<IEbnResourceProposition> resources,
			List<IEbnProposition> preconditions, List<IEbnEffect> effects)
	{
		String name;
		if (actions.size() > 0)
			name = actions.get(0).getName();
		else
			name = "noAction";

		Competence comp = new Competence(name, network.getGoalsCount(), network.getNetworkParams());

		for (IEBNAction behavior : actions) {
			comp.addAction(behavior);
		}

		for (IEbnResourceProposition resourceProp : resources) {
			EbnResource resource = (EbnResource) resourceProp.getResource();
			comp.addResource(new ResourceProposition(comp, resource.getResource(), resourceProp.getAmountUsed()));
		}

		for (IEbnProposition precondition : preconditions) {
			EbnPerception perception = (EbnPerception) precondition.getPerception();
			comp.addPrecondition(new Proposition(comp, perception.getPerception(), precondition.isNegated()));
		}

		for (IEbnEffect effect : effects) {
			EbnPerception perception = (EbnPerception) effect.getPerception();
			comp.addEffect(
					new Effect(comp, perception.getPerception(), effect.isNegated(), effect.getProbability(), null));
		}

		try {
			network.addCompetenceModule(comp);
		} catch (NetworkConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * removes a competence from the ebn
	 */
	public void removeCompetenceStep(Competence comp)
	{
		network.removeCompetence(comp);
	}

	/**
	 * adds a perception to the ebn
	 */
	public void addPerceptionStep(String beliefName)
	{
		IEBNPerception belief = agent.getBeliefs().get(beliefName);
		PerceptionNode perc = new PerceptionNode(belief);
		network.addPerception(perc);
	}

	/**
	 * removes a perception from the ebn
	 */
	public void removePerceptionStep(EbnPerception perception)
	{
		network.removePerception(perception.getPerception());
	}

	/**
	 * adds a resource to the ebn
	 */
	public void addResourceStep(String resourceName)
	{
		IResourceBelief resBelief = agent.getResources().get(resourceName);
		Resource res = new Resource(resBelief);
		network.addResource(res);
	}

	/**
	 * removes a resource from the ebn
	 */
	public void removeResourceStep(EbnResource resource)
	{
		network.removeResource(resource.getResource());
	}

	public void setNetwork(ExtendedBehaviorNetwork network)
	{
		this.network = network;
	}
}
