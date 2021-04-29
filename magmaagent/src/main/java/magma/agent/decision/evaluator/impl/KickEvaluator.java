/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.evaluator.impl;

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.util.geometry.Geometry;
import magma.agent.IHumanoidConstants;
import magma.agent.decision.behavior.IKick;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IThisPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class KickEvaluator extends DecisionEvaluator
{
	enum DecisionState
	{
		NO_KICK,
		DECIDED_FOR_KICK,
		WAIT_FOR_KICK_END
	}

	private static boolean isFirstTime = true;

	private DecisionState state;
	private int cyclesLeft;
	private Vector3D maxBallSpeed;

	// decision state
	private Vector3D localBallPosition;
	private String behaviorName;
	private Vector2D localBallSpeed;
	private double[] upVector;
	private Vector3D playerSpeed;
	private Vector3D localRFootPosition;
	private Vector3D localLFootPosition;

	public KickEvaluator(IDecisionMaker decisionMaker, IRoboCupThoughtModel thoughtModel)
	{
		super(decisionMaker, thoughtModel);
		state = DecisionState.NO_KICK;
	}

	@Override
	public void evaluate()
	{
		IBehavior rootBehavior = decisionMaker.getCurrentBehavior().getRootBehavior();
		switch (state) {
		case NO_KICK:
			if (rootBehavior instanceof IKick) {
				cyclesLeft = ((IKick) rootBehavior).getKickDecider().getBallHitCycles() + 100;
				storeDecisionState(rootBehavior);
				state = DecisionState.DECIDED_FOR_KICK;
			}
			break;

		case DECIDED_FOR_KICK:
			cyclesLeft--;
			if (cyclesLeft == 0) {
				printResult();
				state = DecisionState.WAIT_FOR_KICK_END;
			} else {
				waitForKickResult();
			}
			break;

		case WAIT_FOR_KICK_END:
			if (!(rootBehavior instanceof IKick)) {
				state = DecisionState.NO_KICK;
			}
			break;
		}
	}

	/**
	 * Called to remember state information to be printed in case of event
	 */
	private void storeDecisionState(IBehavior rootBehavior)
	{
		IRoboCupWorldModel worldModel = thoughtModel.getWorldModel();
		IThisPlayer thisPlayer = worldModel.getThisPlayer();
		IMoveableObject ball = worldModel.getBall();
		behaviorName = rootBehavior.getName();
		localBallPosition = thisPlayer.calculateLocalPosition(ball.getPosition());
		localBallSpeed = Geometry.getLocalHorizontalSpeed(thisPlayer.getGlobalOrientation(), ball.getSpeed());
		upVector = thisPlayer.getOrientation().getMatrix()[2];
		playerSpeed = thisPlayer.getSpeed();
		localLFootPosition = thoughtModel.getAgentModel().getBodyPart(IHumanoidConstants.LFoot).getPosition();
		localRFootPosition = thoughtModel.getAgentModel().getBodyPart(IHumanoidConstants.RFoot).getPosition();

		maxBallSpeed = Vector3D.ZERO;
	}

	/**
	 * Called each cycle during which we wait for the kick result
	 */
	private void waitForKickResult()
	{
		IRoboCupWorldModel worldModel = thoughtModel.getWorldModel();
		Vector3D ballSpeed = worldModel.getBall().getSpeed();
		double speed = ballSpeed.getNorm();
		if (speed > maxBallSpeed.getNorm()) {
			if (speed < 0.2) {
				// plausible new highest speed
				maxBallSpeed = ballSpeed;
			}
		}
	}

	/**
	 * Called when the result of a kick should be finalized
	 */
	private void printResult()
	{
		StringBuffer buffer = new StringBuffer(200);

		// heading
		if (isFirstTime) {
			System.out.println(
					"evaluator;kickName;type;localBallPositionX;localBallPositionY;localBallSpeedX;localBallSpeedY;"
					+ "upVectorX;upVectorY;upVectorZ;playerSpeedX;playerSpeedY;"
					+ "localLFootPositionX;localLFootPositionY;localLFootPositionZ;"
					+ "localRFootPositionX;localRFootPositionY;localRFootPositionZ;"
					+ "evaluation");
			isFirstTime = false;
		}

		// the state
		buffer.append(this.getClass().getSimpleName());
		buffer.append(";" + behaviorName);
		buffer.append(";" + thoughtModel.getAgentModel().getModelName());
		buffer.append(String.format(";%4.2f;%4.2f", localBallPosition.getX(), localBallPosition.getY()));
		buffer.append(String.format(";%4.3f;%4.3f", localBallSpeed.getX(), localBallSpeed.getY()));
		buffer.append(String.format(";%4.2f;%4.2f;%4.2f", upVector[0], upVector[1], upVector[2]));
		buffer.append(String.format(";%4.3f;%4.3f", playerSpeed.getX(), playerSpeed.getY()));
		buffer.append(String.format(
				";%4.3f;%4.3f;%4.3f", localLFootPosition.getX(), localLFootPosition.getY(), localLFootPosition.getZ()));
		buffer.append(String.format(
				";%4.3f;%4.3f;%4.3f", localRFootPosition.getX(), localRFootPosition.getY(), localRFootPosition.getZ()));

		// the result
		buffer.append(String.format(";%4.3f", maxBallSpeed.getNorm()));

		System.out.println(buffer.toString());
	}
}
