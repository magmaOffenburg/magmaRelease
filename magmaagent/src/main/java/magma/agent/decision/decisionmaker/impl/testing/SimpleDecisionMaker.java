/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.decisionmaker.impl.testing;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.decisionmaker.impl.DecisionMakerBase;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalk;
import magma.agent.decision.behavior.ikMovement.IKStabilizeOnLegBehavior;

/**
 * Basic decision making class used for testing mainly
 *
 * @author Klaus Dorer
 */
public class SimpleDecisionMaker extends DecisionMakerBase
{
	public SimpleDecisionMaker(BehaviorMap behaviors, IThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
	}

	@Override
	public String decideNextBehavior()
	{
		if (numberOfDecisions < 50) {
			return IBehaviorConstants.GET_READY;
		} else if (numberOfDecisions < 200) {
			IWalk walk = (IWalk) behaviors.get(IBehaviorConstants.WALK);
			walk.walk(60, 0, Angle.ZERO);
			return walk.getName();
		} else {
			boolean left = true;
			String stabilizeName = "Stabilize" + (left ? "Left" : "Right"); // "StabilizeRight"
			int sideFactor = left ? -1 : 1;

			IKStabilizeOnLegBehavior stabilizeBehavior = (IKStabilizeOnLegBehavior) behaviors.get(stabilizeName);
			stabilizeBehavior.setFreeFootTargetPose(
					new Pose2D(sideFactor * 0.18, -0.02, Angle.deg(-1 * sideFactor * 90)));

			return stabilizeName;
		}
	}
}
