/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.walk;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import magma.agent.decision.behavior.IBaseWalk;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IKick;
import magma.agent.decision.behavior.IKickDecider;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.decision.behavior.base.KickDistribution;
import magma.agent.decision.behavior.base.KickEstimator;
import magma.agent.decision.behavior.complex.RoboCupSingleComplexBehavior;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Dribbling extends RoboCupSingleComplexBehavior implements IKick
{
	/** length of the dribble area */
	private static final float MAX_X_DRIBBLE = 0.35f;

	/** width of the dribble area */
	private static final float MAX_Y_DRIBBLE = 0.10f;

	public static final Area2D.Float DRIBBLEABLE_AREA =
			new Area2D.Float(0, MAX_X_DRIBBLE, -MAX_Y_DRIBBLE, MAX_Y_DRIBBLE);

	protected transient IKickDecider kickDecider;

	private IBaseWalk walk;

	public Dribbling(String name, IRoboCupThoughtModel thoughtModel, BehaviorMap behaviors, SupportFoot kickingFoot,
			IPose2D relativeRunToPose, float opponentMaxDistance, Angle intendedKickOffset,
			IWalkEstimator walkEstimator)
	{
		super(name, thoughtModel, behaviors);

		this.walk = (IBaseWalk) behaviors.get(IBehaviorConstants.IK_WALK);
		KickEstimator kickEstimator = new KickEstimator(thoughtModel, walkEstimator, this);
		KickDistribution distribution = createKickDistribution(intendedKickOffset);
		kickDecider = new DribblingKickDecider(kickEstimator, distribution, intendedKickOffset, kickingFoot,
				(Pose2D) relativeRunToPose, new Vector2D(0.2, 0), Angle.ZERO, intendedKickOffset, 3, 3, -10,
				opponentMaxDistance, 0.05f, 0, 100, 1, false, 50);
	}

	private KickDistribution createKickDistribution(Angle intendedKickOffset)
	{
		// values are guessed, but should ideally be measured!
		double[] distanceDistribution = new double[] {0, 0, 0, 0, 0.05, 0.2, 0.5, 0.2, 0.05};

		int maximum = (int) Math.round(Math.abs(intendedKickOffset.degrees()) / KickDistribution.ANGLE_INTERVAL);
		int distributionLength = maximum + 3;
		double[] angleDistribution = new double[distributionLength];
		for (int i = 0; i < distributionLength; i++) {
			int offset = Math.abs(maximum - i);
			double value = 0;
			switch (offset) {
			case 0:
				value = 0.5;
				break;
			case 1:
				value = 0.2;
				break;
			case 2:
				value = 0.05;
				break;
			}
			angleDistribution[i] = value;
		}

		return new KickDistribution(distanceDistribution, angleDistribution);
	}

	@Override
	public IKickDecider getKickDecider()
	{
		return kickDecider;
	}

	@Override
	protected IBehavior decideNextBasicBehavior()
	{
		walk.setMovement(100, 0, Angle.ZERO);
		// walk.setMovement(100, 0, 0, IKDynamicWalkMovement.NAME_HECTIC);
		// System.out.println(getWorldModel().getThisPlayer().getTeamname() + " Dribbling ...");
		return walk;
	}

	public static Dribbling getLeftVersion(String name, IRoboCupThoughtModel thoughtModel, BehaviorMap behaviors,
			float opponentMaxDistance, Angle intendedKickOffset, IWalkEstimator walkEstimator)
	{
		return new Dribbling(name, thoughtModel, behaviors, SupportFoot.LEFT, new Pose2D(-0.12, -0.055),
				opponentMaxDistance, intendedKickOffset, walkEstimator);
	}

	public static Dribbling getRightVersion(String name, IRoboCupThoughtModel thoughtModel, BehaviorMap behaviors,
			float opponentMaxDistance, Angle intendedKickOffset, IWalkEstimator walkEstimator)
	{
		return new Dribbling(name, thoughtModel, behaviors, SupportFoot.RIGHT, new Pose2D(-0.12, 0.055),
				opponentMaxDistance, intendedKickOffset, walkEstimator);
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		if (getCurrentBehavior().isFinished()) {
			return super.switchFrom(actualBehavior);
		} else {
			return this;
		}
	}
}