/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel.impl;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import java.util.HashMap;
import java.util.Map;
import magma.agent.model.worldmodel.IThisPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Container for this agent's information that is unique to the player this
 * agent is representing
 *
 * @author Klaus Dorer, Stefan Glaser
 */
public class ThisPlayer extends Player implements IThisPlayer
{
	/** manager for desired positions */
	private final PositionManager positionManager;

	/** the orientation of the player relative to the local coordinate system */
	private transient Rotation localOrientation;

	/** intended global speed in m/cycle */
	private Vector3D intendedGlobalSpeed;

	/** list of body parts in vision coordinate system*/
	protected final HashMap<String, Vector3D> bodyPartsVision;

	public ThisPlayer(String teamname, int id, float cycleTime, float torsoZUpright)
	{
		super(id, teamname, true, cycleTime);
		setIntendedGlobalSpeed(Vector3D.ZERO);
		position = new Vector3D(0, 0, torsoZUpright);
		previousPosition = position;
		positionManager = new PositionManager();
		this.bodyPartsVision = new HashMap<>();
	}

	/**
	 * Set visible body parts
	 *
	 * @param allBodyParts List of visible body parts
	 */
	public void setBodyPartsVision(Map<String, Vector3D> allBodyParts, Map<String, Vector3D> partsVision)
	{
		setBodyParts(allBodyParts);
		bodyPartsVision.clear();

		if (partsVision != null) {
			bodyPartsVision.putAll(partsVision);
		}
	}

	public Map<String, Vector3D> getBodyPartsVision()
	{
		return bodyPartsVision;
	}

	@Override
	public void setGlobalOrientation(Rotation orientation)
	{
		// Rotation noisyOrientation = addNoiseToGyro(orientation);
		// super.setGlobalOrientation(noisyOrientation);
		super.setGlobalOrientation(orientation);
		localOrientation = Geometry.zTransformRotation(globalOrientation, Math.PI / 2);

		// this method is called each cycle so we reset intended speed here
		// it will be set again by the walk behavior each cycle
		setIntendedGlobalSpeed(Vector3D.ZERO);
	}

	@Override
	public Rotation getOrientation()
	{
		// Handle not serialized localOrientation
		if (localOrientation == null) {
			return Geometry.zTransformRotation(globalOrientation, Math.PI / 2);
		}
		return localOrientation;
	}

	@Override
	public boolean isLying()
	{
		return getOrientation().getMatrix()[2][2] < 0.3;
	}

	@Override
	public boolean isLyingOnBack()
	{
		return getOrientation().getMatrix()[2][1] > 0.75;
	}

	@Override
	public boolean isLyingOnFront()
	{
		double[][] orientation = getOrientation().getMatrix();
		return orientation[2][1] < -0.75 || (orientation[2][1] < 0 && orientation[2][2] < -0.3);
	}

	@Override
	public boolean isLeaningToSide()
	{
		double[][] orientation = getOrientation().getMatrix();
		return orientation[2][0] > 0.7 || orientation[2][0] < -0.7;
	}

	@Override
	public boolean isInHandStand()
	{
		return getOrientation().getMatrix()[2][2] < -0.5;
	}

	@Override
	public double getUpVectorZ()
	{
		return getOrientation().getMatrix()[2][2];
	}

	@Override
	public Vector3D calculateGlobalPosition(Vector3D localPos)
	{
		return position.add(globalOrientation.applyTo(localPos));
	}

	@Override
	public Vector3D calculateGlobal2DPosition(Vector3D localPos)
	{
		return position.add(Geometry.createZRotation(getHorizontalAngle().radians()).applyTo(localPos));
	}

	@Override
	public Pose2D calculateGlobalBodyPose2D(Pose3D poseToTranslate)
	{
		return new Pose2D( //
				calculateGlobalPosition(new Vector3D(poseToTranslate.getY(), -poseToTranslate.getX(),
						poseToTranslate.getZ())), //
				Angle.rad(-Geometry.getTopViewZAngle(getOrientation().applyTo(poseToTranslate.getOrientation()))));
	}

	@Override
	public Pose3D calculateGlobalBodyPose(Pose3D poseToTranslate)
	{
		return getPose().applyTo(Geometry.bodyToWorld(poseToTranslate));
	}

	@Override
	public Vector3D calculateLocalPosition(Vector3D globalPosition)
	{
		Vector3D localPos = globalPosition.subtract(position);
		return globalOrientation.applyInverseTo(localPos);
	}

	/**
	 * Returns the relative angle (rad) this player's torso has to the specified
	 * position
	 * @param position the position to which to calculate the body angle
	 * @return the relative angle (rad) this player's torso has to the specified
	 *         position
	 */
	@Override
	public Angle getBodyDirectionTo(Vector3D position)
	{
		return getDirectionTo(position).subtract(getHorizontalAngle());
	}

	@Override
	public boolean isInsideArea(Vector3D absolutePosition, Area2D.Float area)
	{
		Vector3D localPosition = calculateLocalPosition(absolutePosition);
		return area.contains(localPosition);
	}

	/**
	 * @param absolutePosition the position to check
	 * @return true if the specified position is left of this player with respect
	 *         to the root body orientation
	 */
	@Override
	public boolean positionIsLeft(Vector3D absolutePosition)
	{
		return calculateLocalPosition(absolutePosition).getY() >= 0;
	}

	/**
	 * @param absolutePosition the position to check
	 * @return true if the specified position is right of this player with
	 *         respect to the root body orientation
	 */
	@Override
	public boolean positionIsRight(Vector3D absolutePosition)
	{
		return calculateLocalPosition(absolutePosition).getY() <= 0;
	}

	/**
	 * @param absolutePosition the position to check
	 * @return true if the specified position is behind this player with respect
	 *         to the root body orientation
	 */
	@Override
	public boolean positionIsBehind(Vector3D absolutePosition)
	{
		return calculateLocalPosition(absolutePosition).getX() <= 0;
	}

	@Override
	public PositionManager getPositionManager()
	{
		return positionManager;
	}

	@Override
	public double getMaxSpeed()
	{
		return 0.85;
	}

	public Pose3D getPose()
	{
		return new Pose3D(position, globalOrientation);
	}

	@Override
	public IPose2D getPose2D()
	{
		return new Pose2D(position, getHorizontalAngle());
	}

	@Override
	public void setGlobalPosition(Vector3D position, float time)
	{
		updateFromAudio(Vector3D.ZERO, position, time);
	}

	@Override
	public Vector3D getIntendedGlobalSpeed()
	{
		return intendedGlobalSpeed;
	}

	@Override
	public void setIntendedGlobalSpeed(Vector3D speed)
	{
		// convert from local to global coordinate frame
		this.intendedGlobalSpeed = Geometry.createZRotation(getHorizontalAngle().radians()).applyTo(speed);
	}

	protected void updateFromVision(IPose3D localizedPose, float time)
	{
		// directly set localized pose, as the features observations are transformed to the root body part
		super.updateFromVision(Vector3D.ZERO, Vector3D.ZERO, localizedPose.getPosition(), time);
		setGlobalOrientation(localizedPose.getOrientation());
	}

	public void updateFromOdometry(IPose3D localizedPose)
	{
		setPosition(localizedPose.getPosition());
		setGlobalOrientation(localizedPose.getOrientation());
	}
}
