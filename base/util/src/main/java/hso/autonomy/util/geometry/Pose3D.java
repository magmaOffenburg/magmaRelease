/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import java.text.DecimalFormat;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Stores a 3D position and orientation
 *
 * @author Klaus Dorer
 */
public class Pose3D implements IPose3D
{
	/** The 3D position. */
	public Vector3D position;

	/** The 3D orientation. */
	public Rotation orientation;

	/**
	 * Instantiate a new Pose3D object. The position is initialized with the
	 * {@link Vector3D#ZERO} vector and the {@link Rotation#IDENTITY} rotation.
	 */
	public Pose3D()
	{
		this(Vector3D.ZERO, Rotation.IDENTITY);
	}

	/**
	 * Instantiate a new Pose3D object. The position is initialized with the
	 * passed parameter and the {@link Rotation#IDENTITY} rotation.
	 * @param position the position
	 */
	public Pose3D(Vector3D position)
	{
		this(position, Rotation.IDENTITY);
	}

	/**
	 * Instantiate a new Pose3D object. The position is initialized with the
	 * passed coordinates, z = 0 and the {@link Rotation#IDENTITY} rotation.
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 */
	public Pose3D(double x, double y)
	{
		this(new Vector3D(x, y, 0), Rotation.IDENTITY);
	}

	/**
	 * Instantiate a new Pose3D object. The position is initialized with the
	 * passed coordinates and the {@link Rotation#IDENTITY} rotation.
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 */
	public Pose3D(double x, double y, double z)
	{
		this(new Vector3D(x, y, z), Rotation.IDENTITY);
	}

	/**
	 * Instantiate a new Pose3D object and initialize position and orientation.
	 *
	 * @param position: the position
	 * @param orientation: the orientation
	 */
	public Pose3D(Vector3D position, Rotation orientation)
	{
		this.position = position;
		this.orientation = orientation;
	}

	/**
	 * Instantiate a new Pose3D object from a 2D position (z=0) and a horizontal angle.
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param horizontalAngle the horizontal angle at which this object is turned around z
	 */
	public Pose3D(double x, double y, Angle horizontalAngle)
	{
		this(new Vector3D(x, y, 0),
				new Rotation(Vector3D.PLUS_K, horizontalAngle.radians(), RotationConvention.VECTOR_OPERATOR));
	}

	/**
	 * Instantiate a new Pose3D object from a 2D pose with (z=0) and the specified horizontal angle.
	 *
	 * @param pose2D the 2D pose from which to create a 3D pose
	 */
	public Pose3D(IPose2D pose2D)
	{
		this(pose2D.getX(), pose2D.getY(), pose2D.getAngle());
	}

	/**
	 * Instantiate a new Pose3D object from a position and a horizontal angle.
	 *
	 * @param position: the position
	 * @param horizontalAngle the horizontal angle at which this object is turned around z
	 */
	public Pose3D(Vector3D position, Angle horizontalAngle)
	{
		this(position, new Rotation(Vector3D.PLUS_K, horizontalAngle.radians(), RotationConvention.VECTOR_OPERATOR));
	}

	/**
	 * Copy constructor.
	 *
	 * @param other the other pose
	 */
	public Pose3D(IPose3D other)
	{
		this(other.getPosition(), other.getOrientation());
	}

	@Override
	public double getX()
	{
		return position.getX();
	}

	@Override
	public double getY()
	{
		return position.getY();
	}

	@Override
	public double getZ()
	{
		return position.getZ();
	}

	@Override
	public Vector3D getPosition()
	{
		return position;
	}

	@Override
	public Rotation getOrientation()
	{
		return orientation;
	}

	@Override
	public Pose3D applyTo(IPose3D other)
	{
		return new Pose3D(												//
				position.add(orientation.applyTo(other.getPosition())), //
				orientation.applyTo(other.getOrientation()));
	}

	@Override
	public Pose3D applyInverseTo(IPose3D other)
	{
		return new Pose3D(															//
				orientation.applyInverseTo(other.getPosition().subtract(position)), //
				orientation.applyInverseTo(other.getOrientation()));
	}

	@Override
	public Vector3D applyTo(Vector3D pos)
	{
		return position.add(orientation.applyTo(pos));
	}

	@Override
	public Vector3D applyInverseTo(Vector3D pos)
	{
		return orientation.applyInverseTo(pos.subtract(position));
	}

	@Override
	public Pose3D revert()
	{
		Vector3D pos = orientation.applyInverseTo(position).negate();
		Rotation rot = orientation.revert();

		return new Pose3D(pos, rot);
	}

	@Override
	public IPose2D get2DPose()
	{
		return new Pose2D(getX(), getY(), Geometry.getHorizontalAngle(orientation));
	}

	@Override
	public Angle getHorizontalAngle()
	{
		return Geometry.getHorizontalAngle(orientation);
	}

	@Override
	public Angle getDeltaHorizontalAngle(IPose3D other)
	{
		return other.getHorizontalAngle().subtract(getHorizontalAngle());
	}

	@Override
	public Angle getHorizontalAngleTo(IPose3D other)
	{
		Pose3D relative = applyInverseTo(other);
		return Angle.rad(relative.getPosition().getAlpha());
	}

	@Override
	public double getDistanceTo(IPose3D other)
	{
		return Vector3D.distance(position, other.getPosition());
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof IPose3D) {
			IPose3D other = (IPose3D) obj;
			return position.equals(other.getPosition()) &&
					Rotation.distance(orientation, other.getOrientation()) < 0.0001;
		}

		return super.equals(obj);
	}

	@Override
	public String toString()
	{
		double[][] mat = orientation.getMatrix();
		double[] angles = orientation.getAngles(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR);

		return position.toString(new DecimalFormat("#.##")) + "\n" +
				String.format(								//
						"\t%2.3f %2.3f %2.3f\n"				//
								+ "rm\t%2.3f %2.3f %2.3f\n" //
								+ "\t%2.3f %2.3f %2.3f\n"	//
								+ "deg\t%2.3f %2.3f %2.3f", //
						mat[0][0], mat[0][1], mat[0][2],	//
						mat[1][0], mat[1][1], mat[1][2],	//
						mat[2][0], mat[2][1], mat[2][2],	//
						Math.toDegrees(angles[0]), Math.toDegrees(angles[1]), Math.toDegrees(angles[2]));
	}
}
