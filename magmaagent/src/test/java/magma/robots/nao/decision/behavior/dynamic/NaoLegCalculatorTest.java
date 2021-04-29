/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.dynamic;

import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.agentmodel.impl.AgentModel;
import java.util.Arrays;
import magma.robots.nao.INaoConstants;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;
import magma.robots.nao.model.agentmodel.ik.impl.NaoLegCalculator;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author kdorer
 */
public class NaoLegCalculatorTest
{
	@BeforeEach
	public void setUp() throws Exception
	{
	}

	@Test
	@Disabled
	public void testCalculateJointAngles()
	{
		// left pos:
		// (0.06374230377623169,0.047648535001667665,-0.3183174420184781)
		// orientation: (0.0,0.0,1.7415060758031728)
		// left pos:
		// (0.0703952908396075,0.045629192636641294,-0.32336746148929707)
		// orientation: (0.0,0.0,0.7965962087084204)
		// left pos: (0.07398964880994535,0.0442142822382936,-0.32946155235102914)
		// orientation: (0.0,0.0,0.2026006359999002)

		// left pos:
		// (0.07429667348148239,0.04337967415049646,-0.3363432657871804)
		// orientation: (0.0,0.0,0.0)
		// left pos:
		// (0.05739536897386307,0.04514400786757308,-0.33779395362736536)
		// orientation: (0.0,0.0,0.2451784408864146)

		// left pos:
		// (0.04018267393680784,0.047239212368965125,-0.3392439019075342)
		// orientation: (0.0,0.0,0.4903568817728292)
		// left pos: (0.02281771880600201,0.04838913536436627,-0.339183922696635)
		// orientation: (0.0,0.0,0.7355353409264609)
		// left pos:
		// (0.0032270117073146774,0.050633364805121164,-0.34079938470728327)
		// orientation: (0.0,0.0,0.9807137635456584)
		// left pos:
		// (-0.014611842627405872,0.05290437473883744,-0.3419917087888282)
		// orientation: (0.0,0.0,1.225892186164856)

		IAgentMetaModel metaModel = NaoAgentMetaModel.INSTANCE;
		IAgentModel agentModel = new AgentModel(metaModel, null);

		Vector3D[] targetPos = new Vector3D[] {//
				new Vector3D(-0.045629192636641294, 0.0703952908396075, -0.32336746148929707),
				new Vector3D(-0.0442142822382936, 0.07398964880994535, -0.32946155235102914),
				new Vector3D(-0.04337967415049646, 0.07429667348148239, -0.3363432657871804),
				new Vector3D(-0.04514400786757308, 0.05739536897386307, -0.33779395362736536),
				new Vector3D(-0.047239212368965125, 0.04018267393680784, -0.3392439019075342),
				new Vector3D(-0.04838913536436627, 0.02281771880600201, -0.339183922696635),
				new Vector3D(-0.050633364805121164, 0.0032270117073146774, -0.34079938470728327),
				new Vector3D(-0.05290437473883744, -0.014611842627405872, -0.3419917087888282)};

		Vector3D[] targetAngles =
				new Vector3D[] {new Vector3D(0.0, 0.0, 0.7965962087084204), new Vector3D(0.0, 0.0, 0.2026006359999002),
						new Vector3D(0.0, 0.0, 0.0), new Vector3D(0.0, 0.0, 0.2451784408864146),
						new Vector3D(0.0, 0.0, 0.4903568817728292), new Vector3D(0.0, 0.0, 0.7355353409264609),
						new Vector3D(0.0, 0.0, 0.9807137635456584), new Vector3D(0.0, 0.0, 1.225892186164856)};

		for (int i = 0; i < targetAngles.length; i++) {
			double[] angles1 = NaoLegCalculator.calculateJointAngles(
					agentModel.getFutureBodyModel().getBodyPart(INaoConstants.LFoot), targetPos[i], targetAngles[i]);
			System.out.println(Arrays.toString(angles1));
		}
	}
}
