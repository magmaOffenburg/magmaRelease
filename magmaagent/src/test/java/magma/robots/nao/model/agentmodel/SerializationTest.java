/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.model.agentmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hso.autonomy.agent.model.agentmeta.impl.HingeJointConfiguration;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.agentmodel.impl.AgentModel;
import hso.autonomy.agent.model.agentmodel.impl.HingeJoint;
import hso.autonomy.util.file.SerializationUtil;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for Serialization of agent model
 * @author Klaus Dorer
 */
public class SerializationTest
{
	private HingeJoint testee1;

	@BeforeEach
	public void setUp() throws Exception
	{
		testee1 = new HingeJoint(
				new HingeJointConfiguration("test", "test", "test", new Vector3D(1, 0, 0), -120, 120, 7, false));
	}

	@Test
	public void testHingeJointSerialization() throws Exception
	{
		HingeJoint result = (HingeJoint) SerializationUtil.doubleSerialize(testee1);
		assertEquals(testee1, result);
	}

	@Test
	public void testAgentModelSerialization() throws Exception
	{
		IAgentModel model = new AgentModel(NaoAgentMetaModel.INSTANCE, null);
		IAgentModel result = (IAgentModel) SerializationUtil.doubleSerialize(model);
		assertEquals(result, model);
	}
}
