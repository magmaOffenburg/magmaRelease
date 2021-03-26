/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.staubs.decision;

import java.util.HashMap;
import java.util.Map;
import kdo.ebn.Competence;
import kdo.ebn.Condition;
import kdo.ebn.Effect;
import kdo.ebn.ExtendedBehaviorNetwork;
import kdo.ebn.Goal;
import kdo.ebn.IEBNAction;
import kdo.ebn.IEBNPerception;
import kdo.ebn.IResourceBelief;
import kdo.ebn.NetworkConfigurationException;
import kdo.ebn.NetworkParams;
import kdo.ebn.PerceptionNode;
import kdo.ebn.Proposition;
import kdo.ebn.Resource;
import kdo.ebnDevKit.staubs.decision.behavior.Charge;
import kdo.ebnDevKit.staubs.decision.behavior.Chill;
import kdo.ebnDevKit.staubs.decision.behavior.Clean;
import kdo.ebnDevKit.staubs.decision.behavior.Jump;
import kdo.ebnDevKit.staubs.decision.beliefs.Battery;
import kdo.ebnDevKit.staubs.decision.beliefs.Diff;
import kdo.ebnDevKit.staubs.decision.beliefs.Dirty;
import kdo.ebnDevKit.staubs.decision.beliefs.ResourceBelief;
import kdo.ebnDevKit.staubs.decision.beliefs.Stress;
import kdo.ebnDevKit.staubs.model.IStaubs;

/**
 * Helper class to create a ebn
 * @author Thomas Rinklin
 *
 */
public class EBNCreator
{
	private final IStaubs model;

	public EBNCreator(IStaubs model)
	{
		this.model = model;

		setUp();
	}

	/** default value for the beta network parameter */
	private final double TEST_DEFAULT_BETA = 0.5;

	/** default value for the delta network parameter */
	private final double TEST_DEFAULT_DELTA = 0.7;

	/** default value for the gain network parameter */
	private final double TEST_DEFAULT_GAIN = 5.0;

	/** default value for the gamma network parameter */
	private final double TEST_DEFAULT_GAMMA = 0.8;

	/** default value for the sigma network parameter */
	private final double TEST_DEFAULT_SIGMA = 0.55;

	/** default value for the theta network parameter */
	private final double TEST_DEFAULT_THETA = 0.8;

	/** default value for the threshold_reduction network parameter */
	private final double TEST_DEFAULT_THRESHOLD_REDUCTION = 0.1;

	/** allows to limit network to execute only one action during each step */
	private final boolean TEST_DEFAULT_CONCURRENT_ACTIONS = true;

	/** used during competence initialization to specify connections to the goals */
	private final boolean TEST_DEFAULT_GOAL_TRACKING = true;

	/** allows to switch off transfer function usage */
	private final boolean TEST_DEFAULT_TRANSFER_FUNCTION = true;

	/** default way for process inbox mode */
	private final boolean TEST_DEFAULT_INBOX_PROCESSING = false;

	private Map<String, IEBNPerception> beliefs;

	private Map<String, IResourceBelief> resources;

	private Map<String, IEBNAction> behaviors;

	private Goal goalClean;

	private Goal goalCharged;

	private NetworkParams params;

	private Competence competenceClean;

	private Competence competenceCharge;

	Resource resource1;

	private PerceptionNode perceptionDirty;

	private PerceptionNode perceptionBattery;

	public void setUp()
	{
		createBeliefs();
		createBehaviors();
		createPerceptions();
		createResources();
		createGoals();
		createNetworkParams();
		createCompetences();
	}

	private void createBeliefs()
	{
		beliefs = new HashMap<String, IEBNPerception>();
		beliefs.put("dirty", new Dirty("dirty", model));
		beliefs.put("battery", new Battery("battery", model));
		beliefs.put("diff", new Diff("diff", model));
		beliefs.put("stress", new Stress("stress", model));
	}

	private void createBehaviors()
	{
		behaviors = new HashMap<String, IEBNAction>();

		behaviors.put("clean", new Clean("clean", model));
		behaviors.put("charge", new Charge("charge", model));
		behaviors.put("chill", new Chill("chill", model));
		behaviors.put("jump", new Jump("jump", model));
	}

	private void createPerceptions()
	{
		perceptionDirty = new PerceptionNode(beliefs.get("dirty"));
		perceptionBattery = new PerceptionNode(beliefs.get("battery"));
	}

	private void createResources()
	{
		resources = new HashMap<String, IResourceBelief>();
		resources.put("head", new ResourceBelief("head", 1));
	}

	private void createGoals()
	{
		goalClean = new Goal("goalClean", 0);
		Proposition propClean = new Proposition(goalClean, perceptionDirty, true);
		goalClean.setGoalCondition(propClean);
		goalClean.setImportance(0.8);

		goalCharged = new Goal("goalCharged", 1);
		Proposition propCharge = new Proposition(goalCharged, perceptionBattery, false);

		Condition cond = new Condition();
		cond.addProposition(new Proposition(goalCharged, perceptionDirty, true));

		goalCharged.setRelevanceCondition(cond);
		goalCharged.setGoalCondition(propCharge);
		goalCharged.setImportance(1);
	}

	/**
	 * Default Network Parameters for testing
	 */
	private void createNetworkParams()
	{
		params = new NetworkParams();
		params.setGamma(TEST_DEFAULT_GAMMA);
		params.setDelta(TEST_DEFAULT_DELTA);
		params.setBeta(TEST_DEFAULT_BETA);
		params.setTheta(TEST_DEFAULT_THETA);
		params.setThetaReduction(TEST_DEFAULT_THRESHOLD_REDUCTION);
		params.setSigma(TEST_DEFAULT_SIGMA);
		params.setGain(TEST_DEFAULT_GAIN);
		params.setInboxProcessing(TEST_DEFAULT_INBOX_PROCESSING);
		params.setTransferFunction(TEST_DEFAULT_TRANSFER_FUNCTION);
		params.setGoalTracking(TEST_DEFAULT_GOAL_TRACKING);
		params.setConcurrentActions(TEST_DEFAULT_CONCURRENT_ACTIONS);
	}

	private void createCompetences()
	{
		competenceClean = new Competence("clean", 2, params);
		competenceClean.addPrecondition(new Proposition(competenceClean, perceptionBattery, false));
		competenceClean.addPrecondition(new Proposition(competenceClean, perceptionDirty, false));
		competenceClean.addEffect(new Effect(competenceClean, perceptionBattery, true, 1.0, null));
		competenceClean.addEffect(new Effect(competenceClean, perceptionDirty, true, 1.0, null));
		competenceClean.addAction(behaviors.get("clean"));

		competenceCharge = new Competence("charge", 2, params);
		competenceCharge.addPrecondition(new Proposition(competenceCharge, perceptionBattery, true));
		competenceCharge.addEffect(new Effect(competenceCharge, perceptionBattery, false, 1.0, null));
		competenceCharge.addAction(behaviors.get("charge"));
	}

	public ExtendedBehaviorNetwork createSampleEBN()
	{
		ExtendedBehaviorNetwork staubsNetwork;

		Map<String, IEBNPerception> mergedBelives = new HashMap<String, IEBNPerception>();

		mergedBelives.putAll(beliefs);
		mergedBelives.putAll(resources);

		staubsNetwork = new ExtendedBehaviorNetwork("staubs", behaviors);

		staubsNetwork.addPerception(perceptionDirty);
		staubsNetwork.addPerception(perceptionBattery);
		staubsNetwork.addGoal(goalCharged);
		staubsNetwork.addGoal(goalClean);
		try {
			staubsNetwork.addCompetenceModule(competenceCharge);
			staubsNetwork.addCompetenceModule(competenceClean);
		} catch (NetworkConfigurationException e) {
			e.printStackTrace();
		}

		return staubsNetwork;
	}

	public Map<String, IEBNPerception> getBeliefs()
	{
		return beliefs;
	}

	public Map<String, IResourceBelief> getResources()
	{
		return resources;
	}

	public Map<String, IEBNAction> getBehaviors()
	{
		return behaviors;
	}
}
