/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior;

/**
 * @author sturmflut
 */
public interface IBehaviorConstants {
	// General Behaviors
	String STOP = "Stop";

	String STOP_INSTANTLY = "StopInstantly";

	String BEAM_TO_POSITION = "BeamToPosition";

	String BEAM_HOME = "BeamHome";

	String GET_READY = "GetReady";

	String MOVE_ZERO = "MoveZero";

	// Say behaviors
	String SAY_POSITIONS = "SayPositions";

	String SAY_SOMETHING = "SaySomething";

	// Get Up behaviors
	String MOVE_ARM_TO_FALL_BACK = "MoveArmToFallBack";

	String GET_UP_BACK = "GetUpBack";

	String GET_UP_FRONT = "GetUpFront";

	String FALL_FORWARD = "FallForward";

	String FALL_BACKWARD = "FallBackward";

	String FALL_SIDE = "FallSide";

	// Keep behaviors
	String KEEP_CENTER = "KeepCenter";

	SidedBehaviorConstants KEEP_SIDE = new SidedBehaviorConstants("KeepSide");

	// Stabilized kicks
	StabilizedKickConstants KICK_8M = new StabilizedKickConstants("8m");

	StabilizedKickConstants KICK_11M = new StabilizedKickConstants("11m");

	// KickWalks
	SidedBehaviorConstants KICK_WALK = new SidedBehaviorConstants("KickWalk");

	SidedBehaviorConstants KICK_WALK_STANDING = new SidedBehaviorConstants("KickWalkStanding");

	SidedBehaviorConstants KICK_WALK_SIDE = new SidedBehaviorConstants("KickWalkSide");

	SidedBehaviorConstants KICK_WALK_DIAGONAL_MAX = new SidedBehaviorConstants("KickWalkDiagonalMax");

	SidedBehaviorConstants KICK_WALK_BACKWARD = new SidedBehaviorConstants("KickWalkBackward");

	SidedBehaviorConstants KICK_WALK_STRAIGHT_SIDE = new SidedBehaviorConstants("KickWalkStraightSide");

	// Dribble
	SidedBehaviorConstants DRIBBLE = new SidedBehaviorConstants("Dribble");

	// Celebrate/despair behaviors
	String CELEBRATE = "Celebrate";

	String GRIEVE = "Grieve";

	// Movement behaviors
	String WALK = "Walk";

	String WALK_TO_POSITION = "WalkToPosition";

	String WALK_PATH = "WalkPath";

	String RUN_PATH = "RunPath";

	String IK_WALK = "IKWalk";

	String SWING_ARMS = "SwingArms";

	String IK_WALK_STEP = "IKWalkStep";

	String IK_STEP_PLAN = "IKStepPlan";

	String IK_MOVEMENT = "IKMovement";

	SidedBehaviorConstants STABILIZE = new SidedBehaviorConstants("Stabilize");

	String JOINT_SPACE_WALK = "JointSpaceWalk";

	SidedBehaviorConstants JOINT_SPACE_WALK_MOVEMENT = new SidedBehaviorConstants("JointSpaceWalkMovement");

	String JOINT_SPACE_WALK_MORPH = "JointSpaceWalkMorph";

	// Positioning behaviors
	String ATTACK = "Attack";

	String KICK_CHALLENGE_ATTACK = "KickChallengeAttack";

	String PASSING_CHALLENGE_ATTACK = "PassingChallengeAttack";

	String PASSIVE_POSITIONING = "PassivePositioning";

	String GOALIE_POSITIONING = "GoaliePositioning";

	// Vision control behavior
	String TURN_HEAD = "TurnHead";

	String FOCUS_BALL = "FocusBall";

	String FOCUS_BALL_GOALIE = "FocusBallGoalie";

	String SEARCH_BALL = "SearchBall";

	String SEND_PASS_COMMAND = "SendPassCommand";

	class SidedBehaviorConstants
	{
		public final String LEFT;

		public final String RIGHT;

		/** The name without the "Left" / "Right" suffix */
		public final String BASE_NAME;

		public SidedBehaviorConstants(String baseName)
		{
			BASE_NAME = baseName;
			LEFT = baseName + "Left";
			RIGHT = baseName + "Right";
		}
	}

	class StabilizedKickConstants
	{
		/** The stabilization movement */
		public final SidedBehaviorConstants STABILIZE;

		/** The kick movement */
		public final SidedBehaviorConstants KICK;

		/** The full behavior (stabilization + kick) */
		public final SidedBehaviorConstants FULL;

		public StabilizedKickConstants(String suffix)
		{
			STABILIZE = new SidedBehaviorConstants("Stabilize" + suffix);
			KICK = new SidedBehaviorConstants("Kick" + suffix);
			FULL = new SidedBehaviorConstants("StabilizedKick" + suffix);
		}
	}
}
