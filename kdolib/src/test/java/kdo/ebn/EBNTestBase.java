/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;
import kdo.ebn.xml.BeliefFalse;
import kdo.ebn.xml.BeliefTrue;
import kdo.ebn.xml.ResourceBelief;
import org.junit.jupiter.api.BeforeEach;

/**
 * Utility class to create EBN elements
 *
 */
public class EBNTestBase
{
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

	/** all beliefs available for unit tests */
	Map<String, IEBNPerception> beliefs;

	/** all behaviors available for unit tests */
	Map<String, IEBNAction> behaviors;

	PerceptionNode perceptionTrue1;

	PerceptionNode perceptionTrue2;

	PerceptionNode perceptionFalse1;

	PerceptionNode perceptionFalse2;

	Goal goalTrue1;

	Goal goalNotTrue1;

	NetworkParams params;

	IEBNAction actionMock1;

	IEBNAction actionMock2;

	Competence emptyCompetence;

	Competence competence1;

	Competence competence2;

	Resource resource0;

	Resource resource1;

	Resource resource2;

	@BeforeEach
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
		beliefs = new HashMap<>();
		beliefs.put("true1", new BeliefTrue("true1"));
		beliefs.put("true2", new BeliefTrue("true2"));
		beliefs.put("false1", new BeliefFalse("false1"));
		beliefs.put("false2", new BeliefFalse("false2"));
		beliefs.put("resource0", new ResourceBelief("resource0", 0));
		beliefs.put("resource1", new ResourceBelief("resource1", 1));
		beliefs.put("resource2", new ResourceBelief("resource2", 2));
	}

	private void createBehaviors()
	{
		actionMock1 = mock(IEBNAction.class);
		behaviors = new HashMap<>();
		behaviors.put("action1", actionMock1);

		actionMock2 = mock(IEBNAction.class);
		behaviors.put("action2", actionMock2);
	}

	private void createPerceptions()
	{
		perceptionTrue1 = new PerceptionNode(beliefs.get("true1"));
		perceptionTrue2 = new PerceptionNode(beliefs.get("true2"));
		perceptionFalse1 = new PerceptionNode(beliefs.get("false1"));
		perceptionFalse2 = new PerceptionNode(beliefs.get("false2"));
	}

	private void createGoals()
	{
		goalTrue1 = new Goal("goalTrue1", 0);
		goalTrue1.setGoalCondition(new Proposition(goalTrue1, perceptionTrue1, false));
		goalTrue1.setImportance(0.8);

		goalNotTrue1 = new Goal("goalNotTrue1", 1);
		goalNotTrue1.setGoalCondition(new Proposition(goalNotTrue1, perceptionTrue1, true));
		goalNotTrue1.setImportance(0.3);
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
		emptyCompetence = new Competence("testCompetence", 2, params);
		competence1 = new Competence("competence1", 2, params);
		competence1.addPrecondition(new Proposition(competence1, perceptionTrue1, false));
		competence1.addPrecondition(new Proposition(competence1, perceptionFalse1, true));
		competence1.addEffect(new Effect(competence2, perceptionTrue1, false, 1.0, null));
		competence1.addEffect(new Effect(competence2, perceptionFalse1, false, 1.0, null));
		competence1.addAction(actionMock1);

		competence2 = new Competence("competence2", 2, params);
		competence2.addPrecondition(new Proposition(competence2, perceptionTrue1, false));
		competence2.addPrecondition(new Proposition(competence2, perceptionFalse1, true));
		competence2.addEffect(new Effect(competence2, perceptionTrue1, false, 1.0, null));
		competence2.addEffect(new Effect(competence2, perceptionFalse1, false, 1.0, null));
		competence2.addAction(actionMock2);

		competence2.addResource(new ResourceProposition(competence2, resource1, 1));
	}

	private void createResources()
	{
		resource0 = new Resource((IResourceBelief) beliefs.get("resource0"));
		resource1 = new Resource((IResourceBelief) beliefs.get("resource1"));
		resource1.setActivation(1.0);
		resource2 = new Resource((IResourceBelief) beliefs.get("resource2"));
	}
}
