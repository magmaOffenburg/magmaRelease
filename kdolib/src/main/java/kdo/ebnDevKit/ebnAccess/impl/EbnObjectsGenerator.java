/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kdo.ebn.Competence;
import kdo.ebn.Condition;
import kdo.ebn.Effect;
import kdo.ebn.ExtendedBehaviorNetwork;
import kdo.ebn.Goal;
import kdo.ebn.PerceptionNode;
import kdo.ebn.Proposition;
import kdo.ebn.Resource;
import kdo.ebn.ResourceProposition;
import kdo.ebnDevKit.ebnAccess.IEbnPerception;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnProposition;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnCompetence;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnCompetenceActivationFlow;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnEffect;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnGoal;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnGoalActivationFlow;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnPerception;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnProposition;
import kdo.ebnDevKit.ebnAccess.impl.elements.EbnResource;

/**
 * this class regenerates the ebn objects structure and holds the references to
 * the created objects
 * @author Thomas Rinklin
 */
public class EbnObjectsGenerator
{
	/** map of the generated EbnPerceptions */
	private final Map<PerceptionNode, EbnPerception> perceptionNodes;

	/** map of the generated EbnResources */
	private final Map<Resource, EbnResource> resources;

	/** map of the generated EbnCompetences */
	private final Map<Competence, EbnCompetence> competences;

	/** map of the generated EbnGoals */
	private final Map<Goal, EbnGoal> goals;

	/** generated EbnActivationFlow objects */
	private final List<EbnGoalActivationFlow> goalActivationFlow;

	/** generated EbnCompetenceActivationFlow objects */
	private final List<EbnCompetenceActivationFlow> competenceActivationFlow;

	/** reference to the ebn */
	private ExtendedBehaviorNetwork network;

	/**
	 * constructor
	 * @param network the ebn
	 */
	public EbnObjectsGenerator(ExtendedBehaviorNetwork network)
	{
		this.network = network;

		perceptionNodes = new HashMap<PerceptionNode, EbnPerception>();
		resources = new HashMap<Resource, EbnResource>();
		goals = new HashMap<Goal, EbnGoal>();
		competences = new HashMap<Competence, EbnCompetence>();
		goalActivationFlow = new ArrayList<EbnGoalActivationFlow>();
		competenceActivationFlow = new ArrayList<EbnCompetenceActivationFlow>();
	}

	/**
	 * regenerates the Ebn* objects and fires calls observers
	 */
	public void regenerateEbnObjects()
	{
		goals.clear();
		competences.clear();
		perceptionNodes.clear();
		resources.clear();
		goalActivationFlow.clear();
		competenceActivationFlow.clear();

		createPerceptions();
		createResources();
		createGoals();
		createCompetencesAndAddResources();
		createPreconditionsAndEffects();
		createActivationFlow();
	}

	private void createActivationFlow()
	{
		// create activation flows
		for (EbnCompetence ebnComp : competences.values()) {
			Iterator<Effect> itPostCond = ebnComp.getCompetence().getEffects();
			while (itPostCond.hasNext()) {
				Effect effect = itPostCond.next();

				createCompetenceActivationFlow(ebnComp, effect);

				createGoalActivationFlow(ebnComp, effect);

				// TODO extract degree dingens -> effect.getDegreeProposition();
			}
		}
	}

	private void createGoalActivationFlow(EbnCompetence ebnComp, Effect effect)
	{
		// creat goal activation flow to the competences
		for (EbnGoal sourceEbnGoal : goals.values()) {
			Goal goal = sourceEbnGoal.getGoal();
			if (effect.isIdentic(goal.getGoalCondition())) {
				goalActivationFlow.add(new EbnGoalActivationFlow(sourceEbnGoal, ebnComp, true)); // excitations
			} else if (effect.isInvers(goal.getGoalCondition())) {
				goalActivationFlow.add(new EbnGoalActivationFlow(sourceEbnGoal, ebnComp, false)); // inhibition
			}
		}
	}

	private void createCompetenceActivationFlow(EbnCompetence ebnComp, Effect effect)
	{
		// create competence activation flow to other competences
		for (EbnCompetence targetEbnComp : competences.values()) {
			if (ebnComp != targetEbnComp) {
				Iterator<Proposition> itPreCond = targetEbnComp.getCompetence().getPrecondition().getPropositions();
				while (itPreCond.hasNext()) {
					Proposition proposition = itPreCond.next();

					if (effect.isIdentic(proposition)) {
						competenceActivationFlow.add(
								new EbnCompetenceActivationFlow(ebnComp, targetEbnComp, true)); // excitation

					} else if (effect.isInvers(proposition)) {
						competenceActivationFlow.add(
								new EbnCompetenceActivationFlow(ebnComp, targetEbnComp, true)); // inhibition
					}
				}
			}
		}
	}

	private void createPreconditionsAndEffects()
	{
		// create preconditions and effect
		for (EbnCompetence ebnComp : competences.values()) {
			Iterator<Proposition> itPreCond = ebnComp.getCompetence().getPrecondition().getPropositions();
			while (itPreCond.hasNext()) {
				Proposition proposition = itPreCond.next();
				IEbnPerception ebnPerception = perceptionNodes.get(proposition.getPerception());
				IEbnProposition ebnProposition = ebnPerception.createProposition(proposition.isNegated());
				ebnComp.addPrecondition((EbnProposition) ebnProposition);
			}

			Iterator<Effect> itPostCond = ebnComp.getCompetence().getEffects();
			while (itPostCond.hasNext()) {
				Effect effect = itPostCond.next();
				IEbnPerception ebnPerception = perceptionNodes.get(effect.getPerception());
				EbnEffect ebnEffect = ebnPerception.createEffect(effect.isNegated(), effect.getProbability());
				ebnComp.addEffect(ebnEffect);
			}
		}
	}

	private void createCompetencesAndAddResources()
	{
		// create competences and add resources
		Iterator<Competence> itComp = network.getCompetenceModules();
		while (itComp.hasNext()) {
			Competence comp = itComp.next();
			EbnCompetence ebnComp = new EbnCompetence(comp);
			competences.put(comp, ebnComp);

			Iterator<ResourceProposition> itResProp = comp.getResourcePropositions();
			while (itResProp.hasNext()) {
				ResourceProposition resourceProposition = itResProp.next();

				PerceptionNode resPerc = resourceProposition.getPerception();
				ebnComp.addResource(resources.get(resPerc).creatProposition(resourceProposition.getAmountUsed()));
			}
		}
	}

	private void createGoals()
	{
		// create goals
		Iterator<Goal> itGoals = network.getGoals();
		while (itGoals.hasNext()) {
			Goal goal = itGoals.next();
			EbnPerception ebnPerc = perceptionNodes.get(goal.getGoalCondition().getPerception());
			boolean isNegated = goal.getGoalCondition().isNegated();

			EbnGoal ebnGoal = new EbnGoal(goal, new EbnProposition(ebnPerc, isNegated));

			Condition relCond = goal.getRelevanceCondition();

			if (relCond != null) {
				Iterator<Proposition> itRelConds = relCond.getPropositions();
				while (itRelConds.hasNext()) {
					Proposition proposition = itRelConds.next();
					EbnProposition ebnProp =
							perceptionNodes.get(proposition.getPerception()).createProposition(proposition.isNegated());
					ebnGoal.addProposition(ebnProp);
				}
			}
			goals.put(goal, ebnGoal);
		}
	}

	private void createResources()
	{
		// create resources
		Iterator<Resource> itRes = network.getResources();
		while (itRes.hasNext()) {
			Resource resource = itRes.next();
			resources.put(resource, new EbnResource(resource));
		}
	}

	private void createPerceptions()
	{
		// create perceptions
		Iterator<PerceptionNode> itPerc = network.getPerceptions();
		while (itPerc.hasNext()) {
			PerceptionNode perceptionNode = itPerc.next();
			perceptionNodes.put(perceptionNode, new EbnPerception(perceptionNode));
		}
	}

	public Map<PerceptionNode, EbnPerception> getPerceptions()
	{
		return perceptionNodes;
	}

	public Map<Resource, EbnResource> getResources()
	{
		return resources;
	}

	public Map<Competence, EbnCompetence> getCompetences()
	{
		return competences;
	}

	public Map<Goal, EbnGoal> getGoals()
	{
		return goals;
	}

	public List<EbnGoalActivationFlow> getGoalActivationFlow()
	{
		return goalActivationFlow;
	}

	public List<EbnCompetenceActivationFlow> getCompetenceActivationFlow()
	{
		return competenceActivationFlow;
	}

	public void setNetwork(ExtendedBehaviorNetwork network)
	{
		this.network = network;
	}
}
