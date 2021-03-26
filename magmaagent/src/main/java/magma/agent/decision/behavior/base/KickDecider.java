/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.base;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import magma.agent.decision.behavior.IKickDecider;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Kick ball while walking.
 *
 * @author Klaus Dorer
 */
public class KickDecider extends KickBaseDecider implements IKickDecider
{
	public final static float DEFAULT_MAX_OPP_DISTANCE = 10000;

	protected transient KickEstimator kickEstimator;

	/** The intended kick direction in the global system */
	protected Angle intendedKickDirection;

	/** The direction this kick will go to in the global system */
	private Angle kickDirection;

	/** the direction the ball will go in player relative system */
	private Angle relativeKickDirection;

	/** the distance we want to kick */
	private double intendedKickDistance;

	/** The kicking foot. */
	private final SupportFoot kickingFoot;

	/**
	 * The pose relative to the ball, to which we should navigate in order to be
	 * able to perform this kick.<br>
	 * (The pose is interpreted in the global system)
	 */
	private final Pose2D relativeRunToPose;

	/**
	 * the speed (local coordinates of the run to pose) we want to have at the
	 * kicking position
	 */
	private final Vector2D speedAtRunToPose;

	/** How far an opponent has to be so that we kick */
	private double opponentMinDistance;

	/** we do not consider a dribbling if opponent is too far */
	private double opponentMaxDistance;

	/** max speed (in m/cycle) the ball may have to perform the kick */
	private float ballMaxSpeed;

	/** min speed (in m/cycle) the player may have to perform the kick */
	private float ownMinSpeed;

	/** max speed (in m/cycle) the player may have to perform the kick */
	private float ownMaxSpeed;

	/** the global position we expect the ball to be for kick */
	private Vector3D expectedBallPosition;

	public KickDecider(KickEstimator kickWalkEstimator, KickDistribution distribution, SupportFoot kickingFoot,
			Pose2D relativeRunToPose, Vector2D speedAtRunToPose, Angle relativeKickDirection, Angle kickDirection,
			double maxKickDistance, double minKickDistance, float opponentMinDistance, float opponentMaxDistance,
			float ballMaxSpeed, float ownMinSpeed, float ownMaxSpeed, int ballHitCycles, boolean unstable,
			float priority)
	{
		super(distribution, maxKickDistance, minKickDistance, ballHitCycles, unstable, priority);
		this.kickEstimator = kickWalkEstimator;
		this.relativeKickDirection = relativeKickDirection;
		Pose2D kickDirPose = new Pose2D(0, 0, relativeKickDirection);
		this.kickingFoot = kickingFoot;
		this.relativeRunToPose = kickDirPose.applyInverseTo(relativeRunToPose);
		this.speedAtRunToPose = speedAtRunToPose;
		this.ballMaxSpeed = ballMaxSpeed;
		this.ownMinSpeed = ownMinSpeed;
		this.ownMaxSpeed = ownMaxSpeed;
		this.opponentMinDistance = opponentMinDistance;
		this.opponentMaxDistance = opponentMaxDistance;
		this.kickDirection = kickDirection;
		intendedKickDirection = Angle.ZERO;
		expectedBallPosition = Vector3D.ZERO;
	}

	protected IRoboCupThoughtModel getThoughtModel()
	{
		return getKickEstimator().getThoughtModel();
	}

	protected IRoboCupWorldModel getWorldModel()
	{
		return getThoughtModel().getWorldModel();
	}

	@Override
	public void setIntendedKickDirection(Angle intendedKickDirection)
	{
		this.intendedKickDirection = intendedKickDirection;
		setKickDirection(intendedKickDirection);
	}

	@Override
	public Angle getIntendedKickDirection()
	{
		return intendedKickDirection;
	}

	@Override
	public double getIntendedKickDistance()
	{
		return intendedKickDistance;
	}

	@Override
	public void setIntendedKickDistance(double intendedKickDistance)
	{
		this.intendedKickDistance = intendedKickDistance;
	}

	@Override
	public void setExpectedBallPosition(Vector3D pos)
	{
		expectedBallPosition = pos;
	}

	@Override
	public Vector3D getExpectedBallPosition()
	{
		return expectedBallPosition;
	}

	@Override
	public SupportFoot getKickingFoot()
	{
		return kickingFoot;
	}

	@Override
	public IPose2D getRelativeRunToPose()
	{
		return relativeRunToPose;
	}

	@Override
	public double getOpponentMinDistance()
	{
		return opponentMinDistance;
	}

	public void setOpponentMinDistance(double opponentMinDistance)
	{
		this.opponentMinDistance = opponentMinDistance;
	}

	@Override
	public float getBallMaxSpeed()
	{
		return ballMaxSpeed;
	}

	public void setBallMaxSpeed(float ballMaxSpeed)
	{
		this.ballMaxSpeed = ballMaxSpeed;
	}

	@Override
	public float getOwnMinSpeed()
	{
		return ownMinSpeed;
	}

	@Override
	public float getOwnMaxSpeed()
	{
		return ownMaxSpeed;
	}

	@Override
	public double getOpponentMaxDistance()
	{
		return opponentMaxDistance;
	}

	public void setOpponentMaxDistance(double opponentMaxDistance)
	{
		this.opponentMaxDistance = opponentMaxDistance;
	}

	@Override
	public Angle getKickDirection()
	{
		return kickDirection;
	}

	@Override
	public void setKickDirection(Angle kickDirection)
	{
		this.kickDirection = kickDirection;
	}

	@Override
	public Vector2D getSpeedAtRunToPose()
	{
		return speedAtRunToPose;
	}

	@Override
	public Angle getRelativeKickDirection()
	{
		return relativeKickDirection;
	}

	@Override
	public void setRelativeKickDirection(Angle relativeKickDirection)
	{
		this.relativeKickDirection = relativeKickDirection;
	}

	@Override
	public IPose2D getAbsoluteRunToPose()
	{
		return getKickEstimator().getAbsoluteRunToPose();
	}

	@Override
	public float getExecutability()
	{
		return getKickEstimator().getExecutability();
	}

	@Override
	public float getApplicability()
	{
		return getKickEstimator().getApplicability();
	}

	@Override
	public KickEstimator getKickEstimator()
	{
		return kickEstimator;
	}
}
