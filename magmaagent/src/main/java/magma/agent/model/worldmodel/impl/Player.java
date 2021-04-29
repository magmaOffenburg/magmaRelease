/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel.impl;

import hso.autonomy.agent.model.worldmodel.impl.MovableObject;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.positionFilter.IPositionFilter;
import hso.autonomy.util.geometry.positionFilter.PositionFilter;
import hso.autonomy.util.properties.PropertyManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import magma.agent.IMagmaConstants;
import magma.agent.model.worldmodel.IPlayer;
import magma.common.humanoid.HLPenalty;
import magma.common.humanoid.HLRole;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Represents a player agent on the field
 */
public class Player extends MovableObject implements IPlayer
{
	private static final String OPPONENT_COLLISION_DISTANCE = "model.player.opponentCollisionDistance";
	private static final String TEAM_COLLISION_DISTANCE = "model.player.teamCollisionDistance";
	private static final String LYING_COLLISION_DISTANCE = "model.player.lyingCollisionDistance";

	private static final int INITIAL_TTL = 300;

	/** the player number */
	private final int id;

	/** the team name of this player */
	private final String teamname;

	/** true if this is a player of our own team */
	private final boolean ownTeam;

	/** whether the player is lying */
	private boolean isLying;

	/** a timestamp for when we were last lying on the ground */
	private float lyingTimestamp;

	/** maximum speed the player can achieve running forward (m/s) */
	protected float forwardSpeed;

	/** maximum speed the player can turn (deg/s) */
	protected float turnSpeed;

	/** maximum speed the player can step sidewise */
	protected float sideStepSpeed;

	/** maximum speed the player can step backwards */
	private final float backwardSpeed;

	/** the orientation of the player relative to the global coordinate system */
	protected Rotation globalOrientation;

	/** time of last horizontal angle measurement */
	private float lastOrientationMeasurement;

	/** list of body parts */
	protected final HashMap<String, Vector3D> bodyParts;

	/** a filter for the x-axis in orientation determination */
	private transient IPositionFilter xAxisFilter;

	/** a filter for the y-axis in orientation determination */
	private transient IPositionFilter yAxisFilter;

	/** The player ttl. */
	private int ttl;

	/** The player penalty. */
	private HLPenalty penalty;

	/** The time we expect the player to remain penalized. */
	private float timeRemainingPenalized;

	/** The current role of the player. */
	private HLRole role;

	/**
	 * Constructor
	 *
	 * @param id The number of the player on the field
	 * @param teamname The name of the team this player plays in
	 * @param ownTeam True if this is a player of our own team
	 */
	public Player(int id, String teamname, boolean ownTeam, float cycleTime)
	{
		super(teamname + id, cycleTime);
		this.id = id;
		this.teamname = teamname;
		this.ownTeam = ownTeam;
		this.isLying = false;
		this.lyingTimestamp = 0.0f;
		this.bodyParts = new HashMap<>();
		globalOrientation = Rotation.IDENTITY;
		lastOrientationMeasurement = 0.0f;

		// TODO: measure for own players and observe for other players
		forwardSpeed = 0.3f;
		turnSpeed = 20.0f;
		sideStepSpeed = 0.01f;
		backwardSpeed = 0.05f;

		xAxisFilter = new PositionFilter();
		yAxisFilter = new PositionFilter();

		ttl = INITIAL_TTL;
		penalty = HLPenalty.NONE;
		timeRemainingPenalized = 0;
		role = null;
	}

	public Player(int id, String teamname, boolean ownTeam, float cycleTime, Vector3D initialPos)
	{
		this(id, teamname, ownTeam, cycleTime);
		position = initialPos;
		previousPosition = initialPos;
	}

	@Override
	public int getID()
	{
		return id;
	}

	@Override
	public String getTeamname()
	{
		return teamname;
	}

	@Override
	public double getPossibleSpeed()
	{
		return 0.5;
	}

	/**
	 * Returns the relative angle (rad) this player's torso has to the specified
	 * position. As long as we do not know other player's heading this method
	 * returns 0.
	 * @param position the position to which to calculate the body angle
	 * @return the relative angle (rad) this player's torso has to the specified
	 *         position
	 */
	public Angle getBodyDirectionTo(Vector3D position)
	{
		if (lastOrientationMeasurement > 0) {
			return getDirectionTo(position).subtract(getHorizontalAngle());
		}

		// if we do not know heading, we assume player is faced to direction
		return Angle.ZERO;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof Player))
			return false;
		if (!super.equals(o))
			return false;
		Player player = (Player) o;
		return id == player.id && ownTeam == player.ownTeam && Objects.equals(teamname, player.teamname);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), id, teamname, ownTeam);
	}

	@Override
	public boolean isOwnTeam()
	{
		return ownTeam;
	}

	@Override
	public boolean isGoalie()
	{
		return id == 1;
	}

	@Override
	public Angle getHorizontalAngle()
	{
		return Geometry.getHorizontalAngle(globalOrientation);
	}

	/**
	 * Set player's global orientation in 3-dimensional space.
	 *
	 * @param orientation - new orientation
	 */
	public void setGlobalOrientation(Rotation orientation)
	{
		this.globalOrientation = orientation;
		lastOrientationMeasurement = getLastSeenTime();
	}

	@Override
	public Rotation getGlobalOrientation()
	{
		return this.globalOrientation;
	}

	/**
	 * Calculates the time a player is expected to take to get from its current
	 * position to the passed position heading in the passed direction running
	 * forward
	 * @param position the destination position (global)
	 * @param directionAtTarget desired body direction (global, rad) at the
	 *        destination position
	 * @return time in seconds we estimate is needed to get to the specified
	 *         position with the specified direction
	 */
	@Override
	public float getTimeToTurnAndRun(Vector3D position, Angle directionAtTarget)
	{
		return getTimeForMoving(position, directionAtTarget, forwardSpeed, 0);
	}

	/**
	 * Calculates the time a player is expected to take to get from its current
	 * position to the passed position heading in the passed direction running
	 * forward
	 * @param position the destination position (global)
	 * @param directionAtTarget desired body direction (global, rad) at the
	 *        destination position
	 * @return time in seconds we estimate is needed to get to the specified
	 *         position with the specified direction
	 */
	@Override
	public float getTimeForBackStep(Vector3D position, Angle directionAtTarget)
	{
		return getTimeForMoving(position, directionAtTarget, backwardSpeed, -180);
	}

	/**
	 * Calculates the time a player is expected to take to get from its current
	 * position to the passed position heading in the passed direction by side
	 * stepping
	 * @param position the destination position (global)
	 * @param directionAtTarget desired body direction (global, rad) at the
	 *        destination position
	 * @return time in seconds we estimate is needed to get to the specified
	 *         position with the specified direction
	 */
	@Override
	public float getTimeForSideStep(Vector3D position, Angle directionAtTarget, boolean left)
	{
		if (left) {
			return getTimeForMoving(position, directionAtTarget, sideStepSpeed, 90);
		}

		return getTimeForMoving(position, directionAtTarget, sideStepSpeed, -90);
	}

	/**
	 * Calculates the time a player is expected to take to get from its current
	 * position to the passed position heading in the passed direction
	 * @param position the destination position (global)
	 * @param directionAtTarget desired body direction (global, rad) at the
	 *        destination position
	 * @param speed the average speed we can achieve moving
	 * @param heading the body direction in which to move (forward 0, left 90,
	 *        right -90, backward -180)
	 * @return time in seconds we estimate is needed to get to the specified
	 *         position with the specified direction
	 */
	private float getTimeForMoving(Vector3D position, Angle directionAtTarget, float speed, float heading)
	{
		// time for turning into side step direction
		Angle turnAngleNow = Angle.deg(Math.abs(getBodyDirectionTo(position).degrees()) - heading);
		float time = getTimeForTurn(turnAngleNow);

		// time for stepping there
		float distanceToTarget = (float) getDistanceToXY(position);
		time += distanceToTarget / speed;

		// time for turning into desired direction
		Angle turnAngleAtTarget = directionAtTarget.subtract(Angle.deg(getDirectionTo(position).degrees() - heading));
		time += getTimeForTurn(turnAngleAtTarget);

		return time;
	}

	/**
	 * @param angleToTurn the angle to turn
	 * @return time it needs (in s) to turn the specified angle
	 */
	private float getTimeForTurn(Angle angleToTurn)
	{
		// TODO: may be we should use a stepwise linear function
		float angle = (float) Math.abs(angleToTurn.degrees());
		return angle / turnSpeed;
	}

	@Override
	public boolean isLying()
	{
		return isLying;
	}

	public void setLying(boolean lying)
	{
		isLying = lying;
	}

	/**
	 * Sets whether we're lying or not
	 */
	protected void updateLying(float time)
	{
		boolean newIsLying;

		if (bodyParts.size() < 3) {
			isLying = false;
			lyingTimestamp = time;
			return;
		}

		double minZ = Double.MAX_VALUE;
		double maxZ = Double.MIN_VALUE;

		for (Vector3D pos : bodyParts.values()) {
			if (pos.getZ() < minZ) {
				minZ = pos.getZ();
			}
			if (pos.getZ() > maxZ) {
				maxZ = pos.getZ();
			}
		}

		newIsLying = maxZ - minZ < 0.25;

		if (!newIsLying && isLying) {
			if ((time - lyingTimestamp) < IMagmaConstants.TIME_DELAY_LYING) {
				newIsLying = true;
			}
		} else if (newIsLying && isLying) {
			lyingTimestamp = time;
		}

		isLying = newIsLying;
	}

	/**
	 * Get position of bodyPart
	 */
	public Vector3D getBodyPart(String name)
	{
		return bodyParts.get(name);
	}

	@Override
	public Map<String, Vector3D> getBodyParts()
	{
		return bodyParts;
	}

	/**
	 * Set visible body parts
	 *
	 * @param allBodyParts List of visible body parts
	 */
	public void setBodyParts(Map<String, Vector3D> allBodyParts)
	{
		bodyParts.clear();

		if (allBodyParts != null) {
			bodyParts.putAll(allBodyParts);
		}
	}

	private void determineOrientation()
	{
		if (bodyParts.size() < 5) {
			return;
		}

		// If we haven't seen enough body parts to determine the orientation for
		// more than a second, we reinitialize the position-filters since the
		// vectors they contain are obsolete.
		if (lastOrientationMeasurement + 1 < lastSeenTime) {
			xAxisFilter.reset();
			yAxisFilter.reset();
		}

		String leftPart;
		String rightPart;
		Vector3D pos1;
		Vector3D pos2;
		Vector3D headPos = bodyParts.get("head");

		Vector3D headFoot1 = null;
		Vector3D headFoot2 = null;

		if (headPos == null) {
			return;
		}

		Vector3D xAxis = null;
		Vector3D yAxis = null;

		for (String part : bodyParts.keySet()) {
			if (part.startsWith("l")) {
				leftPart = part;
				pos1 = bodyParts.get(leftPart);
				rightPart = "r" + leftPart.substring(1);
				pos2 = bodyParts.get(rightPart);

				// If there is a second position, we found a left and corresponding
				// right body part, so calculate x-axis to it
				if (pos2 != null) {
					if (yAxis == null) {
						yAxis = pos1.subtract(pos2);
					} else {
						yAxis = yAxis.add(pos1.subtract(pos2));
					}

					if (part.contains("foot")) {
						headFoot1 = headPos.subtract(pos1);
						headFoot2 = headPos.subtract(pos2);
					}
				}
			}
		}

		// If we were not able to match left and right body parts, or we are not
		// able to find vector from head to foots, we have no chance to determine
		// a proper orientation
		if (yAxis == null || headFoot1 == null || headFoot2 == null) {
			return;
		}

		xAxis = Vector3D.crossProduct(headFoot2, headFoot1);
		if (xAxis.getNorm() < 0.01 || yAxis.getNorm() < 0.01) {
			// avoid that normalize may fail
			return;
		}

		yAxis = yAxis.normalize();
		xAxis = xAxis.normalize();

		// Set orientation to fit the calculated axes
		setGlobalOrientation(new Rotation(Vector3D.PLUS_I, Vector3D.PLUS_J, xAxisFilter.filterPosition(xAxis),
				yAxisFilter.filterPosition(yAxis)));
	}

	@Override
	public double getCollisionDistance()
	{
		if (isLying()) {
			return PropertyManager.getDouble(LYING_COLLISION_DISTANCE);
		}

		if (isOwnTeam()) {
			return PropertyManager.getDouble(TEAM_COLLISION_DISTANCE);
		}

		return PropertyManager.getDouble(OPPONENT_COLLISION_DISTANCE);
	}

	@Override
	public void updateFromAudio(Vector3D localPosition, Vector3D globalPosition, float time)
	{
		super.updateFromAudio(localPosition, globalPosition, time);
		setBodyParts(null);
		updateLying(time);
	}

	@Override
	public void updateFromVision(Vector3D seenPosition, Vector3D localPosition, Vector3D globalPosition, float time)
	{
		update(seenPosition, localPosition, globalPosition, null, time);
	}

	public void update(Vector3D seenPosition, Vector3D localPosition, Vector3D globalPosition,
			Map<String, Vector3D> bodyParts, float time)
	{
		// Update position and body part map
		super.updateFromVision(seenPosition, localPosition, globalPosition, time);
		setBodyParts(bodyParts);

		// Determine new orientation
		determineOrientation();

		// Check if the player is lying around
		updateLying(time);

		// Reset player ttl
		resetTTL();
	}

	public void updatePenalty(HLPenalty penalty, float timeRemainingPenalized)
	{
		this.penalty = penalty;
		this.timeRemainingPenalized = timeRemainingPenalized;
	}

	public void updateRole(HLRole role)
	{
		this.role = role;
	}

	@Override
	public double getMaxSpeed()
	{
		return 0.7;
	}

	@Override
	public String toString()
	{
		return teamname + " " + id;
	}

	public void setLastSeenTime(float time)
	{
		lastSeenTime = time;
	}

	@Override
	public void resetTTL()
	{
		ttl = INITIAL_TTL;
	}

	@Override
	public boolean decreaseTTL()
	{
		ttl--;

		return ttl <= 0;
	}

	@Override
	public HLPenalty getPenalty()
	{
		return penalty;
	}

	@Override
	public boolean isPenalized()
	{
		return penalty == HLPenalty.SOME;
	}

	@Override
	public boolean isSubstitute()
	{
		return penalty == HLPenalty.SUBSTITUTE;
	}

	@Override
	public float getTimeRemainingPenalized()
	{
		return timeRemainingPenalized;
	}

	@Override
	public HLRole getRole()
	{
		return role;
	}
}