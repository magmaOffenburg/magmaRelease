/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import java.util.Locale;
import org.apache.commons.math3.geometry.euclidean.threed.CardanEulerSingularityException;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Stores pose in 3D. The pose is represented by 6 double values, three for
 * position- and three angle-values.
 *
 * @author Stefan Glaser
 */
public class Pose6D implements IPose3D
{
	/** the x-value of the position */
	public double x;

	/** the y-value of the position */
	public double y;

	/** the z-value of the position */
	public double z;

	/** the x-angle in degrees */
	public double xAngle;

	/** the y-angle in degrees */
	public double yAngle;

	/** the z-angle in degrees */
	public double zAngle;

	/** the rotation order */
	public transient RotationOrder rotationOrder;

	/**
	 * Instantiate a new Pose6D object and initialize position and orientation.
	 *
	 * @param x: the x-position value
	 * @param y: the y-position value
	 * @param z: the z-position value
	 * @param xAngle: the x-angle
	 * @param yAngle: the y-angle
	 * @param zAngle: the z-angle
	 * @param rotationOrder: the rotation order
	 */
	public Pose6D(
			double x, double y, double z, double xAngle, double yAngle, double zAngle, RotationOrder rotationOrder)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.xAngle = xAngle;
		this.yAngle = yAngle;
		this.zAngle = zAngle;
		this.rotationOrder = rotationOrder;
	}

	/**
	 * Instantiate a new Pose6D object and initialize position and orientation to
	 * zero.
	 */
	public Pose6D()
	{
		this(0, 0, 0, 0, 0, 0, RotationOrder.XYZ);
	}

	/**
	 * Instantiate a new Pose6D object and initialize position and orientation
	 * values to match the other pose.
	 *
	 * @param other: the other pose
	 */
	public Pose6D(Pose6D other)
	{
		this(other.x, other.y, other.z, other.xAngle, other.yAngle, other.zAngle, other.rotationOrder);
	}

	/**
	 * Instantiate a new Pose6D object and initialize position with the given x-,
	 * y-, and z-values. The angles are initialized with zero.
	 *
	 * @param x: the x-position value
	 * @param y: the y-position value
	 * @param z: the z-position value
	 */
	public Pose6D(double x, double y, double z)
	{
		this(x, y, z, 0, 0, 0, RotationOrder.XYZ);
	}

	/**
	 * Instantiate a new Pose6D object and initialize position and orientation.
	 * <br>
	 * The rotation order is set to the default rotation order of
	 * {@link RotationOrder#XYZ}.
	 *
	 * @param x: the x-position value
	 * @param y: the y-position value
	 * @param z: the z-position value
	 * @param xAngle: the x-angle
	 * @param yAngle: the y-angle
	 * @param zAngle: the z-angle
	 */
	public Pose6D(double x, double y, double z, double xAngle, double yAngle, double zAngle)
	{
		this(x, y, z, xAngle, yAngle, zAngle, RotationOrder.XYZ);
	}

	/**
	 * Instantiate a new Pose6D object and initialize position with the given
	 * position. The angles are initialized with zero.
	 *
	 * @param position: the position vector
	 */
	public Pose6D(Vector3D position)
	{
		this(position, Vector3D.ZERO);
	}

	/**
	 * Instantiate a new Pose6D object and initialize position and orientation.
	 * <br>
	 * The rotation order is set to the default rotation order of
	 * {@link RotationOrder#XYZ}.
	 *
	 * @param position: the position vector
	 * @param angles: the angles vector
	 */
	public Pose6D(Vector3D position, Vector3D angles)
	{
		this(position.getX(), position.getY(), position.getZ(), //
				angles.getX(), angles.getY(), angles.getZ(), RotationOrder.XYZ);
	}

	/**
	 * Instantiate a new Pose6D object and initialize position and orientation.
	 * <br>
	 * The rotation order is set to the default rotation order of
	 * {@link RotationOrder#XYZ}.
	 *
	 * @param position the position vector
	 * @param angles the angles vector
	 * @param rotationOrder the rotation order
	 */
	public Pose6D(Vector3D position, Vector3D angles, RotationOrder rotationOrder)
	{
		this(position.getX(), position.getY(), position.getZ(), //
				angles.getX(), angles.getY(), angles.getZ(), rotationOrder);
	}

	/**
	 * Instantiate a new Pose6D object and initialize position and orientation.
	 * <br>
	 * The rotation order is used to calculate the angles.
	 *
	 * @param position the position vector
	 * @param orientation the orientation
	 * @param rotOrder the rotation order used to determine the angles
	 */
	public Pose6D(Vector3D position, Rotation orientation, RotationOrder rotOrder)
	{
		this(position, toAnglesVector(orientation, rotOrder), rotOrder);
	}

	@Override
	public double getX()
	{
		return x;
	}

	@Override
	public double getY()
	{
		return y;
	}

	@Override
	public double getZ()
	{
		return z;
	}

	@Override
	public Vector3D getPosition()
	{
		return new Vector3D(x, y, z);
	}

	public Vector3D getAngles()
	{
		return new Vector3D(xAngle, yAngle, zAngle);
	}

	@Override
	public Rotation getOrientation()
	{
		return new Rotation(rotationOrder, RotationConvention.VECTOR_OPERATOR, Math.toRadians(xAngle),
				Math.toRadians(yAngle), Math.toRadians(zAngle));
	}

	/**
	 * Copy all values from the other pose into this pose.
	 *
	 * @param other: the pose to copy
	 * @return this
	 */
	public Pose6D set(Pose6D other)
	{
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		this.xAngle = other.xAngle;
		this.yAngle = other.yAngle;
		this.zAngle = other.zAngle;
		this.rotationOrder = other.rotationOrder;

		return this;
	}

	/**
	 * Set the position.
	 *
	 * @param x: the x-position value
	 * @param y: the y-position value
	 * @param z: the z-position value
	 */
	public void setPosition(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Set the position.
	 *
	 * @param position: the position vector
	 */
	public void setPosition(Vector3D position)
	{
		this.x = position.getX();
		this.y = position.getY();
		this.z = position.getZ();
	}

	/**
	 * Set the angles.
	 *
	 * @param xAngle: the x-angle
	 * @param yAngle: the y-angle
	 * @param zAngle: the z-angle
	 */
	public void setAngles(double xAngle, double yAngle, double zAngle)
	{
		this.xAngle = xAngle;
		this.yAngle = yAngle;
		this.zAngle = zAngle;
	}

	/**
	 * Set the angles.
	 *
	 * @param angles: the angles vector
	 */
	public void setAngles(Vector3D angles)
	{
		this.xAngle = angles.getX();
		this.yAngle = angles.getY();
		this.zAngle = angles.getZ();
	}

	public void reset()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.xAngle = 0;
		this.yAngle = 0;
		this.zAngle = 0;
	}

	@Override
	public Pose6D applyTo(IPose3D other)
	{
		Rotation orientation = getOrientation();
		Vector3D rotOtherPos = orientation.applyTo(other.getPosition());
		Vector3D angles = toAnglesVector(orientation.applyTo(other.getOrientation()), rotationOrder);

		return new Pose6D(x + rotOtherPos.getX(), y + rotOtherPos.getY(), z + rotOtherPos.getZ(), angles.getX(),
				angles.getY(), angles.getZ(), rotationOrder);
	}

	@Override
	public Pose6D applyInverseTo(IPose3D other)
	{
		Rotation orientation = getOrientation();

		return new Pose6D(																 //
				orientation.applyInverseTo(other.getPosition().subtract(getPosition())), //
				orientation.applyInverseTo(other.getOrientation()), rotationOrder);
	}

	@Override
	public Vector3D applyTo(Vector3D position)
	{
		Rotation orientation = getOrientation();
		Vector3D rotPos = orientation.applyTo(position);

		return new Vector3D(x + rotPos.getX(), y + rotPos.getY(), z + rotPos.getZ());
	}

	@Override
	public Vector3D applyInverseTo(Vector3D position)
	{
		Rotation orientation = getOrientation();

		return orientation.applyInverseTo(position.subtract(getPosition()));
	}

	@Override
	public IPose3D revert()
	{
		Rotation rot = new Rotation(rotationOrder, RotationConvention.VECTOR_OPERATOR, xAngle, yAngle, zAngle);
		Vector3D vec = rot.applyInverseTo(new Vector3D(x, y, z)).negate();

		return new Pose6D(vec, rot.revert(), rotationOrder);
	}

	@Override
	public IPose2D get2DPose()
	{
		return new Pose2D(getX(), getY(), Angle.deg(zAngle));
	}

	public IPose3D get3DPose()
	{
		return new Pose3D(getPosition(), getOrientation());
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Pose6D) {
			Pose6D other = (Pose6D) obj;
			return x == other.x && y == other.y && z == other.z && xAngle == other.xAngle && yAngle == other.yAngle &&
					zAngle == other.zAngle && rotationOrder == other.rotationOrder;
		} else if (obj instanceof IPose3D) {
			IPose3D other = (IPose3D) obj;
			return getPosition().equals(other.getPosition()) &&
					Rotation.distance(getOrientation(), other.getOrientation()) < 0.0001;
		}

		return super.equals(obj);
	}

	private static Vector3D toAnglesVector(Rotation orientation, RotationOrder rotationOrder)
	{
		Vector3D ret = Vector3D.ZERO;

		try {
			double[] angles = orientation.getAngles(rotationOrder, RotationConvention.VECTOR_OPERATOR);
			ret = new Vector3D(angles[0], angles[1], angles[2]);
		} catch (CardanEulerSingularityException e) {
			e.printStackTrace();
		}

		return ret;
	}

	@Override
	public String toString()
	{
		return String.format(
				Locale.US, "pos(%.3f; %.3f; %.3f), angles(%.3f; %.3f; %.3f)", x, y, z, xAngle, yAngle, zAngle);
	}

	@Override
	public Angle getHorizontalAngle()
	{
		return Geometry.getHorizontalAngle(getOrientation());
	}

	@Override
	public double getDistanceTo(IPose3D other)
	{
		return Vector3D.distance(getPosition(), other.getPosition());
	}

	@Override
	public Angle getDeltaHorizontalAngle(IPose3D other)
	{
		return get3DPose().getDeltaHorizontalAngle(other);
	}

	@Override
	public Angle getHorizontalAngleTo(IPose3D other)
	{
		return get3DPose().getHorizontalAngleTo(other);
	}
}
