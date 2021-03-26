/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
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
import kdo.ebn.PerceptionNode;
import kdo.ebn.Proposition;
import kdo.ebn.Resource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests for serializing EBNs
 * @author Klaus Dorer
 */
public class NetworkSerializerTest
{
	NetworkSerializer testee;

	static final IEBNPerception belief1 = new TestBelief("Belief1");

	static final IEBNPerception belief2 = new TestBelief("Belief2");

	static final IResourceBelief belief3 = new TestResourceBelief("Belief3");

	static final Map<String, IEBNPerception> beliefs = new HashMap<>();

	static final IEBNAction behavior1 = new TestBehavior("Behavior1");

	static final IEBNAction behavior2 = new TestBehavior("Behavior2");

	static final Map<String, IEBNAction> behaviors = new HashMap<>();

	@BeforeAll
	public static void setUpClass()
	{
		beliefs.put("Belief1", belief1);
		beliefs.put("Belief2", belief2);
		beliefs.put("Belief3", belief3);
		behaviors.put("Behavior1", behavior1);
		behaviors.put("Behavior2", behavior2);
		beliefs.put("true1", new BeliefTrue("true1"));
		beliefs.put("true2", new BeliefTrue("true2"));
		beliefs.put("false1", new BeliefFalse("false1"));
		beliefs.put("false2", new BeliefFalse("false2"));
		beliefs.put("resource0", new ResourceBelief("resource0", 0));
		beliefs.put("resource1", new ResourceBelief("resource1", 1));
		beliefs.put("resource2", new ResourceBelief("resource2", 2));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		testee = new NetworkSerializer(beliefs, behaviors);
	}

	@Test
	public void testStoreNetwork() throws Exception
	{
		ExtendedBehaviorNetwork ebn = new ExtendedBehaviorNetwork("Test", behaviors);

		ebn.getNetworkParams().setBeta(0.4);

		PerceptionNode perc1 = new PerceptionNode(belief1);
		PerceptionNode perc2 = new PerceptionNode(belief2);
		ebn.addPerception(perc1);
		ebn.addPerception(perc2);

		Resource res = new Resource(belief3);
		ebn.addResource(res);

		Competence comp1 = new Competence("test1");
		comp1.addAction(behavior1);

		Proposition degree = new Proposition(comp1, perc1, false);
		Effect eff1 = new Effect(comp1, new PerceptionNode(belief1), false, 0.5, degree);
		comp1.addEffect(eff1);

		Proposition precondition1 = new Proposition(comp1, perc2, true);
		Proposition precondition2 = new Proposition(comp1, perc1, true);

		comp1.addPrecondition(precondition1);
		comp1.addPrecondition(precondition2);

		Competence comp2 = new Competence("test2");
		comp2.addAction(behavior1);
		comp2.addAction(behavior2);

		ebn.addCompetenceModule(comp1);
		ebn.addCompetenceModule(comp2);

		Goal goal1 = new Goal("goal1");
		Proposition goalCondition = new Proposition(goal1, perc1, false);
		goal1.setGoalCondition(goalCondition);
		goal1.setImportance(0.23);
		Condition relevanceCondition = new Condition(new Proposition(goal1, perc2, false));
		goal1.setRelevanceCondition(relevanceCondition);
		ebn.addGoal(goal1);

		String xml = testee.storeNetwork(ebn);

		assertEquals(getNetworkString(), xml);
	}

	/**
	 * Test method for
	 * {@link kdo.ebn.xml.NetworkSerializer#storeNetwork(kdo.ebn.xml.NetworkXML)}
	 * .
	 */
	@Test
	@Disabled
	public void testLoadAndStoreNetwork()
	{
		String xml = getNetworkString();
		ExtendedBehaviorNetwork ebn = testee.loadNetwork(xml);
		String result = testee.storeNetwork(ebn);
		assertEquals(xml, result);
	}

	public static String getNetworkString()
	{
		StringBuffer xml = new StringBuffer();
		xml.append("<EBN>\n");
		xml.append("  <NetworkParams>\n");
		xml.append("    <concurrentActions>true</concurrentActions>\n");
		xml.append("    <goalTracking>true</goalTracking>\n");
		xml.append("    <inboxProcessing>false</inboxProcessing>\n");
		xml.append("    <transferFunction>true</transferFunction>\n");
		xml.append("    <beta>0.4</beta>\n");
		xml.append("    <delta>0.7</delta>\n");
		xml.append("    <gain>5.0</gain>\n");
		xml.append("    <gamma>0.8</gamma>\n");
		xml.append("    <sigma>0.55</sigma>\n");
		xml.append("    <theta>0.8</theta>\n");
		xml.append("    <thetaReduction>0.1</thetaReduction>\n");
		xml.append("  </NetworkParams>\n");
		xml.append("  <Perceptions>\n");
		xml.append("    <PerceptionNode>\n");
		xml.append("      <BeliefName>Belief1</BeliefName>\n");
		xml.append("    </PerceptionNode>\n");
		xml.append("    <PerceptionNode>\n");
		xml.append("      <BeliefName>Belief2</BeliefName>\n");
		xml.append("    </PerceptionNode>\n");
		xml.append("  </Perceptions>\n");
		xml.append("  <Resources>\n");
		xml.append("    <Resource>\n");
		xml.append("      <BeliefName>Belief3</BeliefName>\n");
		xml.append("    </Resource>\n");
		xml.append("  </Resources>\n");
		xml.append("  <Goals>\n");
		xml.append("    <Goal>\n");
		xml.append("      <name>Belief1</name>\n");
		xml.append("      <relevanceCondition>\n");
		xml.append("        <propositions>\n");
		xml.append("          <Proposition>\n");
		xml.append("            <perceptionNode reference=\"../../../../../../Perceptions/PerceptionNode[2]\"/>\n");
		xml.append("            <isNegated>false</isNegated>\n");
		xml.append("            <containingNode class=\"kdo.ebn.Goal\" reference=\"../../../..\"/>\n");
		xml.append("          </Proposition>\n");
		xml.append("        </propositions>\n");
		xml.append("      </relevanceCondition>\n");
		xml.append("      <goalCondition>\n");
		xml.append("        <perceptionNode reference=\"../../../../Perceptions/PerceptionNode\"/>\n");
		xml.append("        <isNegated>false</isNegated>\n");
		xml.append("        <containingNode class=\"kdo.ebn.Goal\" reference=\"../..\"/>\n");
		xml.append("      </goalCondition>\n");
		xml.append("      <importance>0.23</importance>\n");
		xml.append("      <index>0</index>\n");
		xml.append("    </Goal>\n");
		xml.append("  </Goals>\n");
		xml.append("  <Competences>\n");
		xml.append("    <Competence>\n");
		xml.append("      <Name>test1</Name>\n");
		xml.append("      <Precondition>\n");
		xml.append("        <propositions>\n");
		xml.append("          <Proposition>\n");
		xml.append("            <perceptionNode reference=\"../../../../../../Perceptions/PerceptionNode[2]\"/>\n");
		xml.append("            <isNegated>true</isNegated>\n");
		xml.append("            <containingNode class=\"Competence\" reference=\"../../../..\"/>\n");
		xml.append("          </Proposition>\n");
		xml.append("          <Proposition>\n");
		xml.append("            <perceptionNode reference=\"../../../../../../Perceptions/PerceptionNode\"/>\n");
		xml.append("            <isNegated>true</isNegated>\n");
		xml.append("            <containingNode class=\"Competence\" reference=\"../../../..\"/>\n");
		xml.append("          </Proposition>\n");
		xml.append("        </propositions>\n");
		xml.append("      </Precondition>\n");
		xml.append("      <Actions>\n");
		xml.append("        <Action>Behavior1</Action>\n");
		xml.append("      </Actions>\n");
		xml.append("      <Effects>\n");
		xml.append("        <Effect>\n");
		xml.append("          <perceptionNode>\n");
		xml.append("            <BeliefName reference=\"../../../../../../Perceptions/PerceptionNode/BeliefName\"/>\n");
		xml.append("          </perceptionNode>\n");
		xml.append("          <isNegated>false</isNegated>\n");
		xml.append("          <containingNode class=\"Competence\" reference=\"../../..\"/>\n");
		xml.append("          <degree>\n");
		xml.append("            <perceptionNode reference=\"../../../../../../Perceptions/PerceptionNode\"/>\n");
		xml.append("            <isNegated>false</isNegated>\n");
		xml.append("            <containingNode class=\"Competence\" reference=\"../../../..\"/>\n");
		xml.append("          </degree>\n");
		xml.append("          <probability>0.5</probability>\n");
		xml.append("        </Effect>\n");
		xml.append("      </Effects>\n");
		xml.append("      <ResourcePropositions/>\n");
		xml.append("    </Competence>\n");
		xml.append("    <Competence>\n");
		xml.append("      <Name>test2</Name>\n");
		xml.append("      <Precondition>\n");
		xml.append("        <propositions/>\n");
		xml.append("      </Precondition>\n");
		xml.append("      <Actions>\n");
		xml.append("        <Action reference=\"../../../Competence/Actions/Action\"/>\n");
		xml.append("        <Action>Behavior2</Action>\n");
		xml.append("      </Actions>\n");
		xml.append("      <Effects/>\n");
		xml.append("      <ResourcePropositions/>\n");
		xml.append("    </Competence>\n");
		xml.append("  </Competences>\n");
		xml.append("</EBN>");
		return xml.toString();
	}

	/**
	 * Test for {@link NetworkSerializer#loadNetworkFromFile(File)}
	 *
	 * @throws NetworkConfigurationException In case of fatal error
	 */
	@Test
	@Disabled
	public void testCreateEBN() throws Exception
	{
		NetworkSerializer ns = new NetworkSerializer(beliefs, behaviors);

		ExtendedBehaviorNetwork ebn = ns.loadNetworkFromFile(new File("build/test/kdo/ebn/xml/ebnTest.xml"));
		assertNotNull(ebn);

		// test goals
		assertEquals(1, ebn.getGoalsCount());
		Goal goal = ebn.getGoals().next();
		assertEquals("true1", goal.getName());
		assertEquals(0.23, goal.getImportance(), 0.0001);
		assertEquals("true1", goal.getGoalCondition().getName());
		assertFalse(goal.getGoalCondition().isNegated());
		Proposition relevance = goal.getRelevanceCondition().getPropositions().next();
		assertEquals("true2", relevance.getName());
		assertFalse(relevance.isNegated());

		// test competence
		assertEquals(2, ebn.getCompetenceModulesCount());
		Competence competence = ebn.getCompetenceModules().next();
		assertEquals("true2", competence.getPrecondition().getPropositions().next().getName());
		Effect effect = competence.getEffects().next();
		assertEquals("true1", effect.getName());
		assertEquals(0.5, effect.getProbability(), 0.0001);
	}
}
