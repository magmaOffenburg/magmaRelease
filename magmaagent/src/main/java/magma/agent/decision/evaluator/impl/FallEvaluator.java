/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.evaluator.impl;

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import hso.autonomy.util.geometry.Angle;
import java.util.LinkedList;
import java.util.List;
import magma.agent.decision.behavior.IBaseWalk;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IKick;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IThisPlayer;
import magma.robots.nao.decision.behavior.movement.getup.GetUpFromBack;
import magma.robots.nao.decision.behavior.movement.getup.GetUpFromFront;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class FallEvaluator extends DecisionEvaluator
{
	enum DecisionState
	{
		NO_FALL,
		FALLEN
	}

	private static final int MAX_STEPS_TO_EVALUATE = 20;

	private static boolean isFirstTime = true;

	private DecisionState decisionState;
	private int resultCount;
	private List<EvaluatorState> evaluatorStates;

	public FallEvaluator(IDecisionMaker decisionMaker, IRoboCupThoughtModel thoughtModel)
	{
		super(decisionMaker, thoughtModel);
		decisionState = DecisionState.NO_FALL;
		resultCount = 0;
		evaluatorStates = new LinkedList<>();
	}

	@Override
	public void evaluate()
	{
		IBehavior rootBehavior = decisionMaker.getCurrentBehavior().getRootBehavior();
		switch (decisionState) {
		case NO_FALL:
			if (isGetupBehavior(rootBehavior)) {
				printResult();
				decisionState = DecisionState.FALLEN;
			} else {
				storeState(rootBehavior);
			}
			break;

		case FALLEN:
			if (!isGetupBehavior(rootBehavior)) {
				decisionState = DecisionState.NO_FALL;
			}
			break;
		}
	}

	private boolean isGetupBehavior(IBehavior rootBehavior)
	{
		return (rootBehavior instanceof GetUpFromBack) || (rootBehavior instanceof GetUpFromFront);
	}

	/**
	 * Called to remember state information to be printed in case of event
	 * @param rootBehavior
	 */
	private void storeState(IBehavior rootBehavior)
	{
		if (rootBehavior.getName().equals(IBehaviorConstants.MOVE_ARM_TO_FALL_BACK)) {
			// do not store state information during this behavior
			return;
		}

		if (rootBehavior instanceof IKick) {
			// currently not interested in falls from kicking
			evaluatorStates.clear();
			return;
		}

		IThisPlayer thisPlayer = thoughtModel.getWorldModel().getThisPlayer();
		EvaluatorState eval = new EvaluatorState();
		eval.behaviorName = rootBehavior.getName();
		double opponentDistance = 100;
		if (!thoughtModel.getOpponentsAtMeList().isEmpty()) {
			IPlayer closestOpponent = thoughtModel.getOpponentsAtMeList().get(0);
			opponentDistance = thisPlayer.getDistanceToXY(closestOpponent);
		}
		if (opponentDistance < 0.4) {
			// currently we are not interested in falls due to collision
			evaluatorStates.clear();
			return;
		}

		if (rootBehavior instanceof IBaseWalk) {
			IBaseWalk walk = (IBaseWalk) rootBehavior;
			if (!walk.isNewStep()) {
				// for now we only log state on step change
				return;
			}

			eval.desiredSpeed = walk.getIntendedWalk();
			eval.realSpeed = walk.getCurrentSpeed();
			eval.desiredTurn = walk.getIntendedTurn();
			eval.realTurn = walk.getCurrentTurn();
			eval.upVector = thisPlayer.getOrientation().getMatrix()[2];
			eval.horizontalAngle = thisPlayer.getHorizontalAngle().degrees();
			eval.opponentDistance = opponentDistance;
		}

		evaluatorStates.add(eval);
		if (evaluatorStates.size() > MAX_STEPS_TO_EVALUATE) {
			evaluatorStates.remove(0);
		}
	}

	/**
	 * Called when the event happened
	 */
	private void printResult()
	{
		resultCount++;
		StringBuilder buffer = new StringBuilder(200);

		// heading
		if (isFirstTime) {
			System.out.println(
					"evaluator;count;teamName;playerId;type;behaviorName;time;desiredWalkForward;desiredWalkSideward;realForward;realSideward;"
					+ "desiredTurn;realTurn;upX;upY;horizontalAngle;opponentDistance");
			isFirstTime = false;
		}

		// the state
		for (EvaluatorState eval : evaluatorStates) {
			buffer.append(this.getClass().getSimpleName());
			buffer.append(";").append(resultCount);
			buffer.append(";").append(thoughtModel.getWorldModel().getThisPlayer().getName());
			buffer.append(";").append(thoughtModel.getWorldModel().getThisPlayer().getID());
			buffer.append(";").append(thoughtModel.getAgentModel().getModelName());
			buffer.append(";").append(eval.toString());
			buffer.append(System.getProperty("line.separator"));
		}
		System.out.print(buffer.toString());
	}

	private class EvaluatorState
	{
		String behaviorName;
		float time;
		Vector2D desiredSpeed;
		Vector2D realSpeed;
		Angle desiredTurn;
		Angle realTurn;
		double[] upVector;
		double horizontalAngle;
		double opponentDistance;

		public EvaluatorState()
		{
			time = thoughtModel.getWorldModel().getGlobalTime();
			desiredSpeed = Vector2D.ZERO;
			realSpeed = Vector2D.ZERO;
			upVector = new double[2];
		}

		@Override
		public String toString()
		{
			StringBuilder buffer = new StringBuilder(100);
			buffer.append(behaviorName);
			buffer.append(String.format(";%4.2f", time));
			buffer.append(String.format(";%4.3f", desiredSpeed.getX()));
			buffer.append(String.format(";%4.3f", desiredSpeed.getY()));
			buffer.append(String.format(";%4.3f", realSpeed.getX()));
			buffer.append(String.format(";%4.3f", realSpeed.getY()));
			buffer.append(String.format(";%4.3f", desiredTurn.degrees()));
			buffer.append(String.format(";%4.3f", realTurn.degrees()));
			buffer.append(String.format(";%4.3f;%4.3f", upVector[0], upVector[1]));
			buffer.append(String.format(";%.1f", horizontalAngle));
			buffer.append(String.format(";%.2f", opponentDistance));
			return buffer.toString();
		}
	}
}
