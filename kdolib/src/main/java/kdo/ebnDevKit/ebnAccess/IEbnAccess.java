/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import kdo.ebn.IEBNAction;
import kdo.ebn.observation.ISubscribeUnsubscribe;
import kdo.ebnDevKit.agent.IEbnAgent;
import kdo.ebnDevKit.ebnAccess.IEbnActivationFlow.IEbnCompetenceActivationFlow;
import kdo.ebnDevKit.ebnAccess.IEbnActivationFlow.IEbnGoalActivationFlow;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnEffect;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnProposition;
import kdo.ebnDevKit.ebnAccess.IEbnResource.IEbnResourceProposition;

/**
 * interface to the model. it provides access to the agent and to the ebn
 * @author Thomas Rinklin
 *
 */
public interface IEbnAccess {
	/**
	 * returns the ebnAccess-observer
	 * @return the ebnAccess-observer
	 */
	ISubscribeUnsubscribe observe();

	/**
	 * add a perception to the ebn and reload
	 * @param beliefName name of the new belief
	 */
	void addPerception(String beliefName);

	/**
	 * removes a perception, if it is not used, and reloads
	 * @param perception perception to remove
	 */
	void removePerception(IEbnPerception perception);

	/**
	 * checks if a perception is used from a goal or a competence
	 * @param perception the perception to check
	 * @return true if it is used, false if not
	 */
	boolean isPerceptionUsed(IEbnPerception perception);

	/**
	 * add a resource to the ebn and reload
	 * @param resourceName name of the new resource
	 */
	void addResource(String resourceName);

	/**
	 * removes a resource, if it is not used, and reloads
	 * @param resource resource to remove
	 */
	void removeResource(IEbnResource resource);

	/**
	 * checks if a resource is used from a competence
	 * @param resource resource to check
	 * @return true if it is used, false if not
	 */
	boolean isResourceUsed(IEbnResource resource);

	/**
	 * adds a competence to the ebn and reloads
	 * @param behaviorName name of the behavior
	 */
	void addCompetence(List<IEBNAction> actions, List<IEbnResourceProposition> resources,
			List<IEbnProposition> preconditions, List<IEbnEffect> effects);

	/**
	 * changes a competence and reloads
	 * @param comp competence to change
	 * @param actions new list of actions
	 * @param resources new list of resource propositions
	 * @param preconditions new list of preconditions
	 * @param effects new list of effects
	 */
	void changeCompetence(IEbnCompetence comp, List<IEBNAction> actions, List<IEbnResourceProposition> resources,
			List<IEbnProposition> preconditions, List<IEbnEffect> effects);

	/**
	 * removes a competence and reloads
	 * @param comp competence to remove
	 */
	void removeCompetence(IEbnCompetence comp);

	/**
	 * adds a goal to the ebn and reloads
	 * @param perception goal condition
	 * @param isNegated is goal condition negated
	 * @param importance situation independent importance
	 * @param relevanceConditions list of relevance propositions
	 */
	void addGoal(IEbnPerception perception, boolean isNegated, double importance,
			List<? extends IEbnProposition> relevanceConditions);

	/**
	 * replaces a goal and reloads
	 * @param goal goal to replace
	 * @param isNegated is goal condition negated
	 * @param importance situation independent importance
	 * @param relevanceConditions list of relevance propositions
	 */
	void changeGoal(
			IEbnGoal goal, boolean isNegated, double importance, List<? extends IEbnProposition> relevanceConditions);

	/**
	 * removes a goal an reloads
	 * @param goal the goal to remove
	 */
	void removeGoal(IEbnGoal goal);

	/**
	 * returns an iterator of the goals
	 * @return an iterator of the goals
	 */
	Iterator<? extends IEbnGoal> getGoals();

	/**
	 * returns an iterator of the perceptions
	 * @return an iterator of the perceptions
	 */
	Iterator<? extends IEbnPerception> getPerceptions();

	/**
	 * returns an iterator of the resources
	 * @return an iterator of the resources
	 */
	Iterator<? extends IEbnResource> getResources();

	/**
	 * returns an iterator of the competences
	 * @return an iterator of the competences
	 */
	Iterator<? extends IEbnCompetence> getCompetenceModules();

	/**
	 * returns an iterator of the activation flow from goal to competence
	 * @return an iterator of the activation flow from goal to competence
	 */
	Iterator<? extends IEbnGoalActivationFlow> getGoalActivationFlow();

	/**
	 * returns an iterator of the activation flow from competence to competence
	 * @return an iterator of the activation flow from competence to competence
	 */
	Iterator<? extends IEbnCompetenceActivationFlow> getCompetenceActivationFlow();

	/**
	 * returns interface to the agent
	 * @return interface to the agent
	 */
	IEbnAgent getAgent();

	/**
	 * saves ebn structure to file
	 */
	void save(File f);
}