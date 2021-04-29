/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.base;

import hso.autonomy.agent.model.worldmodel.InformationSource;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.PoseSpeed2D;
import hso.autonomy.util.geometry.VectorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import magma.agent.decision.behavior.IKick;
import magma.agent.decision.behavior.IKickDecider;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.GameState;
import magma.agent.model.worldmodel.IBall;
import magma.agent.model.worldmodel.IPassModeConstants;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author kdorer
 */
public class KickEstimator
{
	// set to true if the first negative check terminates further checking (default!)
	private static final boolean USE_SHORTCUT_CONDITIONS = true;

	private static boolean PRINTED_HEADER = false;

	protected IRoboCupThoughtModel thoughtModel;

	protected IRoboCupWorldModel worldModel;

	private transient IWalkEstimator walkEstimator;

	/** the kick to check */
	protected IKick kick;

	protected int[] executabilityConditionResults;

	protected int[] applicabilityConditionResults;

	protected transient List<Supplier<Float>> executabilityCheckSuppliers =
			new ArrayList<>(Arrays.asList(this::checkPlausibility, this::checkOwnSpeed, this::checkBallSpeed));

	protected String getExecutabilityCheckNames()
	{
		return "checkPlausibility;checkOwnSpeed;checkBallSpeed;";
	}

	protected transient List<Supplier<Float>> applicabilityCheckSuppliers =
			new ArrayList<>(Arrays.asList(this::checkOpponentDistanceFitting, this::checkKickDistanceFit,
					this::checkKickOutsideField, this::checkWalkability, this::checkOwnSpeed));

	protected String getAvailabilityCheckNames()
	{
		return "checkOpponentDistanceFitting;checkKickDistanceFit;checkKickOutsideField;checkWalkability;checkOwnSpeed;";
	}

	public KickEstimator(IRoboCupThoughtModel thoughtModel, IWalkEstimator walkEstimator, IKick kick)
	{
		super();
		this.thoughtModel = thoughtModel;
		this.kick = kick;
		this.worldModel = thoughtModel.getWorldModel();
		this.walkEstimator = walkEstimator;
		executabilityConditionResults = new int[executabilityCheckSuppliers.size()];
		applicabilityConditionResults = new int[applicabilityCheckSuppliers.size()];
	}

	public void addExecutabilityCheck(Supplier<Float> newCheck)
	{
		executabilityCheckSuppliers.add(newCheck);
		executabilityConditionResults = new int[executabilityCheckSuppliers.size()];
	}

	public void addApplicabilityCheck(Supplier<Float> newCheck)
	{
		applicabilityCheckSuppliers.add(newCheck);
		applicabilityConditionResults = new int[applicabilityCheckSuppliers.size()];
	}

	public float getExecutability()
	{
		float resultApplicability = checkConditions(applicabilityCheckSuppliers, applicabilityConditionResults);
		if (resultApplicability < 0) {
			if (USE_SHORTCUT_CONDITIONS) {
				return -1;
			}
		}

		float resultUtility = checkConditions(executabilityCheckSuppliers, executabilityConditionResults);

		float overallResult = resultApplicability + resultUtility;
		if (resultApplicability < 0 || resultUtility < 0) {
			overallResult = -1;
		}
		if (!USE_SHORTCUT_CONDITIONS) {
			printConditionResult(overallResult, kick);
		}
		return overallResult;
	}

	public float getApplicability()
	{
		return checkConditions(applicabilityCheckSuppliers, applicabilityConditionResults);
	}

	protected float checkConditions(List<Supplier<Float>> suppliers, int[] conditionResults)
	{
		boolean bad = false;
		float result = 0.0f;
		int i = 0;
		for (Supplier<Float> supplier : suppliers) {
			float value = supplier.get().floatValue();
			if (value < 0) {
				// use this as shortcut version
				if (USE_SHORTCUT_CONDITIONS) {
					return -1;
				}
				// use this to analyze and check all conditions
				bad = true;
				conditionResults[i] = 0;
			} else {
				conditionResults[i] = 1;
				result += value;
			}
			i++;
		}
		if (bad) {
			return -1;
		}
		return result;
	}

	private synchronized void printConditionResult(float result, IKick kick)
	{
		if (!PRINTED_HEADER) {
			// print header line only once
			PRINTED_HEADER = true;
			System.out.println(
					"ID;Time;Name;" + getAvailabilityCheckNames() + getExecutabilityCheckNames() + "sum;utility");
		}

		int id = worldModel.getThisPlayer().getID();
		float globalTime = worldModel.getGlobalTime();
		System.out.print(id + ";" + globalTime + ";" + kick.getName() + ";");
		int sum = printConditionResults(applicabilityConditionResults);
		sum += printConditionResults(executabilityConditionResults);
		System.out.println(sum + ";" + result);
	}

	private int printConditionResults(int[] conditionResults)
	{
		int sum = 0;
		for (int result : conditionResults) {
			System.out.print(result + ";");
			sum += result;
		}
		return sum;
	}

	public float checkPlausibility()
	{
		// prevent kicking balls that we only heard of
		if (worldModel.getBall().getInformationSource() == InformationSource.AUDIO ||
				worldModel.getBall().getAge(worldModel.getGlobalTime()) > 5) {
			return -1;
		}

		// fail if we are nowhere near the ball
		if (worldModel.getThisPlayer().getDistanceToXY(worldModel.getBall()) > 1) {
			return -1;
		}
		return 0;
	}

	public float checkOwnSpeed()
	{
		IKickDecider kickDecider = getKickDecider();
		// check own speed
		double speed = worldModel.getThisPlayer().getSpeed().getNorm();
		if (speed < kickDecider.getOwnMinSpeed()) {
			// System.out.println("We too slow: " + speed);
			return -1;
		}
		if (speed > kickDecider.getOwnMaxSpeed()) {
			// System.out.println("We too fast: " + speed);
			return -1;
		}
		return 0;
	}

	public float checkBallSpeed()
	{
		IKickDecider kickDecider = getKickDecider();
		// fail if ball is rolling
		if (worldModel.getBall().getSpeed().getNorm() > kickDecider.getBallMaxSpeed()) {
			//			System.out.println(kick.getName() + ": "
			//							   + "Ball too fast: " + worldModel.getBall().getSpeed().getNorm());
			return -1;
		}
		return 0;
	}

	public float checkOpponentDistanceFitting()
	{
		IPlayer opponentAtBall = thoughtModel.getOpponentAtBall();
		if (opponentAtBall == null) {
			// assume always applicable if there's no opponents (mainly for testing)
			return 0;
		}

		double myBallDistance = worldModel.getThisPlayer().getDistanceToXY(worldModel.getBall());
		double opponentBallDistance = opponentAtBall.getDistanceToXY(worldModel.getBall());
		if (worldModel.getGameState() == GameState.OWN_PASS) {
			float gameTime = worldModel.getGameTime();
			float difference = gameTime - worldModel.getEnteredPassModeTime();
			final float maxSpeed = 0.8f;
			float extraDistance = maxSpeed * (IPassModeConstants.DURATION - difference);

			opponentBallDistance += extraDistance;
		}

		// check max distance
		IKickDecider kickDecider = getKickDecider();
		if (opponentBallDistance >= myBallDistance + kickDecider.getOpponentMaxDistance()) {
			return -1;
		}

		// minDistance is only applicable if opponent can attack us
		if (worldModel.getGameState().isOwnKick()) {
			return 0;
		}

		// check min distance
		if (opponentBallDistance < myBallDistance + kickDecider.getOpponentMinDistance()) {
			return -1;
		}
		return 0;
	}

	public float checkKickDistanceFit()
	{
		IKickDecider kickDecider = getKickDecider();
		// HACK: do not use long kicks if short kick is intended
		if (kickDecider.getMaxKickDistance() > 12 && kickDecider.getIntendedKickDistance() < 8) {
			return -1;
		}

		// measure how well it fits the desired distance
		float distanceMalus = 0;
		if (kickDecider.getMinKickDistance() > kickDecider.getIntendedKickDistance()) {
			// The minimum kick distance is further than the intended kick distance
			distanceMalus = (float) (kickDecider.getMinKickDistance() - kickDecider.getIntendedKickDistance());
		} else if (kickDecider.getIntendedKickDistance() > kickDecider.getMaxKickDistance()) {
			distanceMalus = (float) (kickDecider.getIntendedKickDistance() - kickDecider.getMaxKickDistance());
		}
		return 34 - distanceMalus;
	}

	public float checkKickOutsideField()
	{
		// Do not kick outside the field
		IKickDecider kickDecider = getKickDecider();
		IBall ball = worldModel.getBall();
		Vector2D ballPosition2D = VectorUtils.to2D(ball.getPosition());
		Angle kickDirection = kickDecider.getKickDirection();
		Vector2D resultingPos = ballPosition2D.add(kickDirection.applyTo(kickDecider.getMinKickDistance(), 0));
		if (Math.abs(resultingPos.getX()) > worldModel.fieldHalfLength() ||
				Math.abs(resultingPos.getY()) > worldModel.fieldHalfWidth()) {
			// Allow kicking into the opponent's goal
			if (Math.abs(kickDirection.degrees()) > 95) {
				// it is certainly away from goal, so outside
				//				System.out.println(kick.getName() + ": would end up outside the field.");
				return -1;
			}
			if (Math.abs(ballPosition2D.getY() +
						 Math.tan(kickDirection.radians()) * (worldModel.fieldHalfLength() - ballPosition2D.getX())) >
					worldModel.goalHalfWidth()) {
				//				System.out.println(kick.getName() + ": would end up outside the field 2.");
				return -1;
			}
		}
		return 0;
	}

	public float checkWalkability()
	{
		// measure how hard it is to get there
		IPose2D target = kick.getKickDecider().getAbsoluteRunToPose();
		PoseSpeed2D targetSpeed = new PoseSpeed2D(target, Vector2D.ZERO);
		float walkTime = walkEstimator.getFastestWalkTime(
				worldModel.getThisPlayer().getPose2D(), Collections.singletonList(targetSpeed));
		return 33 - walkTime;
	}

	public float checkBlockOwnGoal()
	{
		// measure how much this position is blocking the own goal
		float positionMalus = 0;
		IPlayer opponentAtBall = thoughtModel.getOpponentAtBall();
		if (opponentAtBall != null) {
			// only check in own half
			if (worldModel.getBall().getPosition().getX() < 0) {
				double opponentBallDistance = opponentAtBall.getDistanceToXY(worldModel.getBall());
				if (opponentBallDistance < 2.0) {
					IPose2D target = kick.getKickDecider().getAbsoluteRunToPose();
					Vector2D point1 = VectorUtils.to2D(worldModel.getOwnGoalPosition());
					Vector2D point2 = VectorUtils.to2D(kick.getKickDecider().getExpectedBallPosition());
					Vector2D thePoint = target.getPosition();
					double distance = Geometry.getDistanceToLine(point1, point2, thePoint);
					positionMalus = (float) (distance * 10);
				}
			}
		}

		return 33 - positionMalus;
	}

	public IPose2D getAbsoluteRunToPose()
	{
		Angle intendedKickDirection = kick.getKickDecider().getIntendedKickDirection();
		IKickDecider kickDecider = getKickDecider();

		Vector3D relativeRunToPos = kickDecider.getRelativeRunToPose().getPosition3D();
		Vector3D ballDifference = new Vector3D(worldModel.getBallRadiusDifference(), 0, 0);
		relativeRunToPos = relativeRunToPos.subtract(ballDifference);
		Vector3D globalRelativeTargetPos = intendedKickDirection.applyTo(relativeRunToPos);

		Angle dir = intendedKickDirection.add(kickDecider.getRelativeRunToPose().getAngle());

		Angle ourDirection = worldModel.getThisPlayer().getHorizontalAngle();

		double delta = Math.abs(dir.subtract(ourDirection).degrees());

		if (delta > 20) {
			// to be able to run round the ball we have to stay away a bit
			float maxKeepAwayFactor = 0.5f;
			globalRelativeTargetPos = globalRelativeTargetPos.scalarMultiply(
					1 + (Geometry.getLinearFuzzyValue(20, 80, true, delta) * maxKeepAwayFactor));
		}

		Vector3D ballPos = kick.getKickDecider().getExpectedBallPosition();
		return new Pose2D(
				ballPos.getX() + globalRelativeTargetPos.getX(), ballPos.getY() + globalRelativeTargetPos.getY(), dir);
	}

	public Vector3D getBallPosAtKick()
	{
		int cycles = Math.min(getKickDecider().getBallHitCycles(), 150);
		return worldModel.getBall().getFuturePosition(cycles);
	}

	protected IKickDecider getKickDecider()
	{
		return kick.getKickDecider();
	}

	/**
	 * @return the thoughtModel
	 */
	public IRoboCupThoughtModel getThoughtModel()
	{
		return thoughtModel;
	}
}
