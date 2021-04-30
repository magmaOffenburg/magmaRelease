/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package magma.agent.decision.behavior.complex.walk;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.misc.ValueUtil;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import magma.agent.decision.behavior.IBaseWalk;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalk;
import magma.agent.decision.behavior.complex.RoboCupSingleComplexBehavior;
import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModelThin;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Walk extends RoboCupSingleComplexBehavior implements IWalk
{
	private List<Double> lastUpValues;

	public Walk(String name, IThoughtModel thoughtModel, BehaviorMap behaviors, IBaseWalk baseWalk)
	{
		super(name, thoughtModel, behaviors, baseWalk.getName());
		Double value = 0.0;
		lastUpValues = new LinkedList<>();
		lastUpValues.addAll(Arrays.asList(value, value, value, value, value, value));
	}

	public Walk(IThoughtModel thoughtModel, BehaviorMap behaviors, IBaseWalk baseWalk)
	{
		this(IBehaviorConstants.WALK, thoughtModel, behaviors, baseWalk);
	}

	/**
	 * Set parameters for Walk. It is possible to combine all different
	 * parameters, e.g. forwards and sidesteps.
	 *
	 * @param forwardsBackwards positive = forwards; negative = backwards (-100 .. 100)
	 * @param stepLeftRight positive = right; negative = left (-100 .. 100)
	 * @param turnLeftRight positive = right; negative = left
	 */
	@Override
	public void walk(double forwardsBackwards, double stepLeftRight, Angle turnLeftRight)
	{
		walk(forwardsBackwards, stepLeftRight, turnLeftRight, IKDynamicWalkMovement.NAME_STABLE);
	}
	/**
	 * Set parameters for Walk. It is possible to combine all different
	 * parameters, e.g. forwards and sidesteps.
	 *
	 * @param forwardsBackwards positive = forwards; negative = backwards (-100 .. 100)
	 * @param stepLeftRight positive = right; negative = left (-100 .. 100)
	 * @param turnLeftRight positive = right; negative = left
	 */
	@Override
	public void walk(double forwardsBackwards, double stepLeftRight, Angle turnLeftRight, String paramSetName)
	{
		// Limit turn angle by the current speed
		IBaseWalk base = (IBaseWalk) getCurrentBehavior();
		double turnLimit = base.getMaxTurnAngle().degrees() * 2;
		double actualSpeed = base.getIntendedWalk().getNorm() * 100;
		turnLimit = Math.min(turnLimit, 100 - actualSpeed + 7);
		turnLimit = (actualSpeed > 60) ? 9 : turnLimit;

		// limit turn if we are hanging
		//		double up = getWorldModel().getThisPlayer().getOrientation().getMatrix()[2][2];
		//		double upLimit = Geometry.getLinearFuzzyValue(0.92, 0.99, true, up) * turnLimit;
		//		turnLimit = Math.min(turnLimit, upLimit);

		Angle turnAmount = Angle.deg(ValueUtil.limitAbs(turnLeftRight.degrees(), turnLimit) / 2);
		base.setMovement(forwardsBackwards, stepLeftRight, turnAmount, paramSetName);
	}

	@Override
	public void globalWalk(Pose2D walkTo, Vector3D speedThere, double distanceToFinal, double slowDownDistance,
			double maxSpeedLimit, String paramSetName)
	{
		Vector3D fieldVector = walkTo.getPosition3D();
		Angle horizontalDirection = getWorldModel().getThisPlayer().getHorizontalAngle();

		// rotate into local coordinate system
		Vector3D playerVector = Geometry.createZRotation(-(float) horizontalDirection.radians()).applyTo(fieldVector);

		double desiredTurn = walkTo.angle.degrees();

		// pretend we have done the wanted turn
		Vector3D virtualPosition = Geometry.createZRotation((float) Math.toRadians(-desiredTurn)).applyTo(playerVector);

		// account for that we are slower sideward
		Vector2D realSpeed = new Vector2D(virtualPosition.getX(), virtualPosition.getY() * 1.5);
		double absSpeed = realSpeed.getNorm();

		if (absSpeed < 0.001) {
			// avoid vector normalization exception, assume walk forward
			realSpeed = new Vector2D(1, 0);
		} else {
			realSpeed = realSpeed.normalize();
		}
		absSpeed = 1.0;

		// limit speed if diagonal
		double reduction = Math.toDegrees(Math.abs(Math.atan2(realSpeed.getY(), realSpeed.getX())));
		if (reduction > 90) {
			reduction = 180 - reduction;
		}
		reduction = Geometry.getLinearFuzzyValue(0, 20, false, Math.abs(45 - reduction)) * 0.3;
		double maxSpeed = Math.min(1.0 - reduction, maxSpeedLimit / 100.0);

		// limit speed, if we have to turn
		double turnFactor = Geometry.getLinearFuzzyValue(10, 60, true, Math.abs(desiredTurn));
		turnFactor = 1 - turnFactor; // * turnFactor;
		maxSpeed = Math.min(maxSpeed, turnFactor);

		if (absSpeed > maxSpeed) {
			realSpeed = realSpeed.normalize().scalarMultiply(maxSpeed);
		}

		// limit speed if close to destination (separate for x and y)
		double finalSpeedX = speedThere.getX();
		finalSpeedX += Geometry.getLinearFuzzyValue(0, slowDownDistance, true, virtualPosition.getNorm());
		double finalSpeedY = speedThere.getY();
		finalSpeedY += Geometry.getLinearFuzzyValue(0, slowDownDistance, true, virtualPosition.getNorm());
		double finalX = ValueUtil.limitAbs(realSpeed.getX(), finalSpeedX);
		double finalY = ValueUtil.limitAbs(realSpeed.getY(), finalSpeedY);
		realSpeed = new Vector2D(finalX, finalY);

		// translate to percent
		realSpeed = realSpeed.scalarMultiply(100);

		IRoboCupThoughtModel thoughtModel = getThoughtModel();
		if (thoughtModel instanceof IRoboCupThoughtModelThin) {
			((IRoboCupThoughtModelThin) thoughtModel)
					.setDash((float) realSpeed.getX(), (float) realSpeed.getY(), (float) desiredTurn);
		}
		walk(realSpeed.getX(), realSpeed.getY(), Angle.deg(desiredTurn), paramSetName);
	}

	@Override
	public IBehavior decideNextBasicBehavior()
	{
		return getCurrentBehavior();
	}
}