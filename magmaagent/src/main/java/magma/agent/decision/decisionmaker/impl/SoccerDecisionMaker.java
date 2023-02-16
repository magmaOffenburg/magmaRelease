/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.decisionmaker.impl;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose3D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.GameState;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IThisPlayer;
import magma.robots.nao.INaoConstants;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Simple decision maker for primitive field player
 *
 * @author Klaus Dorer
 */
public class SoccerDecisionMaker extends RoboCupDecisionMakerBase
{
	/**
	 * Methods that are called to determine the next behavior to be performed, sorted by priority.
	 */
	protected transient List<Supplier<String>> behaviorSuppliers =
			new ArrayList<>(Arrays.asList(this::calibrateCamera, this::performSay, this::beamHome,
					this::sendPassCommand, this::getUp, this::reactToGameEnd, this::performFocusBall, this::getReady,
					this::waitForGameStart, this::waitForOpponentActions, this::searchBall, this::move));
	private boolean calibrated;

	public SoccerDecisionMaker(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
		calibrated = false;
	}

	@Override
	public String decideNextBehavior()
	{
		for (Supplier<String> supplier : behaviorSuppliers) {
			String behavior = supplier.get();
			if (behavior != null) {
				// System.out.println("Desired Behavior: " + behavior + " currentBehavior: " +
				// currentBehavior.getName());
				return behavior;
			}
		}

		// stop any running motors and do nothing
		return IBehavior.NONE;
	}

	/**
	 * Tries to keep the ball in the visible area.
	 */
	protected String performFocusBall()
	{
		IBehavior focusBall = behaviors.get(IBehaviorConstants.FOCUS_BALL);
		focusBall.perform();
		return null;
	}

	/**
	 * Say something.
	 */
	protected String performSay()
	{
		behaviors.get(IBehaviorConstants.SAY_POSITIONS).perform();
		return null;
	}

	protected String calibrateCamera()
	{
		if (numberOfDecisions > 10) {
			// we do not want to wait forever
			calibrated = true;
		}
		if (!calibrated) {
			IRoboCupAgentModel agentModel = getAgentModel();
			Vector3D deltaLeft = getCameraDelta(INaoConstants.LLowerArm);
			Vector3D deltaRight = getCameraDelta(INaoConstants.RLowerArm);
			Vector3D delta = deltaLeft;
			if (delta == null) {
				delta = deltaRight;
			}

			if (delta != null) {
				if (deltaLeft != null && deltaRight != null) {
					delta = deltaLeft.add(deltaRight).scalarMultiply(0.5);
					// System.out.println("both arms: " + deltaLeft + " right: " + deltaRight);
				}

				IPose3D cameraOffset = agentModel.getCameraOffset();
				Pose3D newCameraOffset =
						new Pose3D(cameraOffset.getPosition().subtract(delta), cameraOffset.getOrientation());
				agentModel.setCameraOffset(newCameraOffset);
				calibrated = true;
			}
		}
		if (calibrated) {
			return null;
		}
		return IBehavior.NONE;
	}

	public Vector3D getCameraDelta(String bodyPart)
	{
		IRoboCupAgentModel agentModel = getAgentModel();
		Map<String, Vector3D> bodyParts = getWorldModel().getThisPlayer().getBodyPartsVision();
		Vector3D seenPosition = bodyParts.get(bodyPart);
		if (seenPosition == null) {
			return null;
		}
		Vector3D armPosition = agentModel.getBodyPart(bodyPart).getPosition();
		Pose3D headPose = agentModel.getBodyPartContainingCamera().getPose();
		Vector3D armInHeadSystem = headPose.applyInverseTo(armPosition);
		armInHeadSystem = new Vector3D(armInHeadSystem.getY(), -armInHeadSystem.getX(), armInHeadSystem.getZ());

		Pose3D cameraPose = agentModel.getCameraPose();
		Vector3D armInCameraSystem = headPose.applyInverseTo(cameraPose.applyTo(seenPosition));
		// System.out.println("armInHead: " + armInHeadSystem + " armInCamera: " + armInCameraSystem);
		return armInCameraSystem.subtract(armInHeadSystem);
	}

	protected String sendPassCommand()
	{
		if (getThoughtModel().shouldActivatePassMode()) {
			behaviors.get(IBehaviorConstants.SEND_PASS_COMMAND).perform();
		}
		return null;
	}

	/**
	 * Called to decide if we should beam us to our home position
	 * @return the behavior to execute, null if this part of decision does not
	 *         trigger a behavior currently
	 */
	protected String beamHome()
	{
		if (getThoughtModel().shouldBeam()) {
			return IBehaviorConstants.BEAM_HOME;
		}
		return null;
	}

	protected String reactToGameEnd()
	{
		IRoboCupWorldModel worldModel = getWorldModel();
		if (worldModel.getGameState() == GameState.OWN_GOAL) {
			return IBehaviorConstants.CELEBRATE;
		}

		if (worldModel.getGameState() == GameState.OPPONENT_GOAL) {
			return IBehaviorConstants.GRIEVE;
		}

		if (worldModel.getGameState() == GameState.GAME_OVER) {
			int ourGoals = worldModel.getGoalsWeScored();
			int opponentGoals = worldModel.getGoalsTheyScored();

			if (ourGoals > opponentGoals) {
				return IBehaviorConstants.CELEBRATE;
			} else if (opponentGoals > ourGoals) {
				return IBehaviorConstants.GRIEVE;
			}
		}

		return null;
	}

	/**
	 * Called to decide if we should wait for game start or not. Default
	 * implementation waits.
	 * @return the behavior to execute, null if this part of decision does not
	 *         trigger a behavior currently
	 */
	protected String waitForGameStart()
	{
		if (!getWorldModel().getGameState().isGameRunning() ||
				(getWorldModel().isPenalty() && !isMyTurnInPenalties())) {
			return IBehaviorConstants.GET_READY;
		}
		return null;
	}

	/**
	 * Called to decide if we should wait for opponent actions or not. Default
	 * implementation waits only on OpponentKickOff.
	 * @return the behavior to execute, null if this part of decision does not
	 *         trigger a behavior currently
	 */
	protected String waitForOpponentActions()
	{
		if (getWorldModel().getGameState() == GameState.OPPONENT_KICK_OFF) {
			return IBehaviorConstants.GET_READY;
		}
		return null;
	}

	/**
	 * Called to decide if we should stand up from lying on ground
	 * @return the behavior to execute, null if this part of decision does not
	 *         trigger a behavior currently
	 */
	protected String getUp()
	{
		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();

		boolean tryGetUpBack = thisPlayer.isLyingOnBack();
		boolean tryGetUpFront = thisPlayer.isLyingOnFront();

		int consecutivePerformsFront = getBehavior(IBehaviorConstants.GET_UP_FRONT).getConsecutivePerforms();
		if (tryGetUpFront && consecutivePerformsFront > 0 && consecutivePerformsFront % 3 == 0) {
			// if GET_UP_FRONT isn't working, our orientation might simply be wrong
			// - just try GET_UP_BACK instead (see #112)
			tryGetUpBack = true;
			tryGetUpFront = false;
		}

		if (tryGetUpBack) {
			return IBehaviorConstants.GET_UP_BACK;
		}
		if (tryGetUpFront) {
			return IBehaviorConstants.GET_UP_FRONT;
		}

		if (thisPlayer.isLeaningToSide()) {
			if (thisPlayer.isLying()) {
				// straighten legs
				getBehavior(IBehaviorConstants.GET_READY).perform();
			}
			return IBehaviorConstants.MOVE_ARM_TO_FALL_BACK;
		}

		if (thisPlayer.isInHandStand()) {
			// if the robot is in a handstand, move the arms as he could otherwise start walking
			return IBehaviorConstants.MOVE_ARM_TO_FALL_BACK;
		}

		return null;
	}

	/**
	 * Called to decide if we should get into ready position
	 * @return the behavior to execute, null if this part of decision does not
	 *         trigger a behavior currently
	 */
	protected String getReady()
	{
		if (getThoughtModel().isInSoccerPosition()) {
			return null;
		}
		return IBehaviorConstants.GET_READY;
	}

	protected String searchBall()
	{
		if (getThoughtModel().isBallVisible()) {
			return null;
		}
		return IBehaviorConstants.SEARCH_BALL;
	}

	/**
	 * Called to decide if movement is necessary
	 * @return walk behavior if no team mate is closer to ball
	 */
	protected String move()
	{
		if (getThoughtModel().isClosestToBall()) {
			return IBehaviorConstants.ATTACK;
		}

		if (getWorldModel().getGameState() == GameState.OWN_KICK_OFF) {
			// do not position until kick off
			return IBehaviorConstants.GET_READY;
		}

		return IBehaviorConstants.PASSIVE_POSITIONING;
	}
	protected boolean isMyTurnInPenalties()
	{
		return getWorldModel().getPenaltyState() == IRoboCupWorldModel.PenaltyState.KICK;
	}
}
