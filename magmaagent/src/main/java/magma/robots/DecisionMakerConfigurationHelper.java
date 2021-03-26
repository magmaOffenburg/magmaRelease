/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import magma.agent.IMagmaConstants;
import magma.agent.decision.decisionmaker.impl.GoalieDecisionMaker;
import magma.agent.decision.decisionmaker.impl.SoccerDecisionMaker;
import magma.agent.decision.decisionmaker.impl.challenge.GazeboRunChallengeDecisionMaker;
import magma.agent.decision.decisionmaker.impl.challenge.KeepAwayChallengeDecisionMaker;
import magma.agent.decision.decisionmaker.impl.challenge.KickChallengeDecisionMaker;
import magma.agent.decision.decisionmaker.impl.challenge.PassingChallengeDecisionMaker;
import magma.agent.decision.decisionmaker.impl.challenge.RunChallengeDecisionMaker;
import magma.agent.decision.decisionmaker.impl.challenge.RunToBallDecisionMaker;
import magma.agent.decision.decisionmaker.impl.testing.DoNothingDecisionMaker;
import magma.agent.decision.decisionmaker.impl.testing.SimpleDecisionMaker;
import magma.agent.decision.decisionmaker.impl.testing.TrainingDecisionMaker;
import magma.agent.decision.decisionmaker.impl.testing.TrainingDecisionMaker2;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

public class DecisionMakerConfigurationHelper
{
	@FunctionalInterface
	public interface DecisionMakerConstructor {
		IDecisionMaker create(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel);
	}

	public static final Map<String, DecisionMakerConstructor> NAO_DECISION_MAKERS;

	public static final Map<String, DecisionMakerConstructor> SWEATY_DECISION_MAKERS;

	static
	{
		Map<String, DecisionMakerConstructor> nao = new LinkedHashMap<>();
		nao.put(IMagmaConstants.DEFAULT_DECISION_MAKER, null);
		nao.put("DoNothing", DoNothingDecisionMaker::new);
		nao.put("Goalie", GoalieDecisionMaker::new);
		nao.put("Soccer", SoccerDecisionMaker::new);
		nao.put("Simple", SimpleDecisionMaker::new);
		nao.put("Training", TrainingDecisionMaker::new);
		nao.put("Training2", TrainingDecisionMaker2::new);
		nao.put("RunChallenge", RunChallengeDecisionMaker::new);
		nao.put("GazeboRunChallenge", GazeboRunChallengeDecisionMaker::new);
		nao.put("KickChallenge", KickChallengeDecisionMaker::new);
		nao.put("KeepAwayChallenge", KeepAwayChallengeDecisionMaker::new);
		nao.put("RunToBall", RunToBallDecisionMaker::new);
		nao.put("PassingChallenge", PassingChallengeDecisionMaker::new);
		NAO_DECISION_MAKERS = Collections.unmodifiableMap(nao);

		Map<String, DecisionMakerConstructor> sweaty = new LinkedHashMap<>();
		sweaty.put(IMagmaConstants.DEFAULT_DECISION_MAKER, null);
		SWEATY_DECISION_MAKERS = Collections.unmodifiableMap(sweaty);
	}

	public static Collection<String> getDecisionMakerNames(String robotModel)
	{
		return NAO_DECISION_MAKERS.keySet();
	}
}
