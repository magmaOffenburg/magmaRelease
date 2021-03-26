/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.dynamic;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.worldmodel.ILandmark;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.misc.FuzzyCompare;
import java.util.Collection;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.basic.RoboCupBehavior;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.nao.INaoJoints;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Implements a behavior which lets the robot head focus on the ball
 *
 * @author Ingo Schindler
 */
public class FocusBall extends RoboCupBehavior
{
	private static final float TIME_TO_LOOK_AROUND = 2;

	private static final float LOOK_AROUND_DELAY = 8;

	private final IBehavior turnHead;

	private float timeToTurnHead;

	public FocusBall(IRoboCupThoughtModel thoughtModel, BehaviorMap behaviors)
	{
		super(IBehaviorConstants.FOCUS_BALL, thoughtModel);
		turnHead = behaviors.get(IBehaviorConstants.TURN_HEAD);
		timeToTurnHead = getWorldModel().getGlobalTime();
	}

	@Override
	public void perform()
	{
		super.perform();

		boolean seeLandMark = false;
		Collection<ILandmark> landmarks = getWorldModel().getLandmarks();
		for (ILandmark landmark : landmarks) {
			float time = landmark.getAge(getWorldModel().getGlobalTime());
			if (time < 0.1) {
				seeLandMark = true;
				break;
			}
		}

		Vector3D ballPos = getWorldModel().getBall().getLocalPosition();

		float timeDelta = getWorldModel().getGlobalTime() - timeToTurnHead;

		if (getWorldModel().getBall().getAge(getWorldModel().getGlobalTime()) > 0.1 || !seeLandMark ||
				(ballPos.getNorm() > 1.5 &&
						FuzzyCompare.gte(timeDelta, LOOK_AROUND_DELAY + TIME_TO_LOOK_AROUND, TIME_TO_LOOK_AROUND))) {
			turnHead.perform();
			if (timeDelta > LOOK_AROUND_DELAY + TIME_TO_LOOK_AROUND) {
				timeToTurnHead = getWorldModel().getGlobalTime();
			}
			// we have to init this behavior manually since we return null here to allow to perform this behavior in
			// parallel
			turnHead.stayIn();

		} else {
			focusBall(getAgentModel(), ballPos);
		}
	}

	static void focusBall(IRoboCupAgentModel agentModel, Vector3D ballPos)
	{
		// Calculate position of ball relative to camera body
		Pose3D headPose = agentModel.getCameraPose();

		Vector3D ballVec = new Vector3D(-ballPos.getY(), ballPos.getX(), ballPos.getZ());

		ballVec = headPose.getPosition().negate().add(headPose.getOrientation().applyInverseTo(ballVec));
		ballPos = new Vector3D(ballVec.getY(), -ballVec.getX(), ballVec.getZ());

		// Adjust head joints to focus the ball
		agentModel.getWriteableHJ(INaoJoints.NeckYaw).adjustAxisPosition(Math.toDegrees(ballPos.getAlpha() / 3));
		agentModel.getWriteableHJ(INaoJoints.NeckPitch).adjustAxisPosition(Math.toDegrees(ballPos.getDelta() / 3));
	}
}
