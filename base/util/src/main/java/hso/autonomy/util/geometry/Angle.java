/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import java.io.Serializable;
import java.util.Locale;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents an angle. Objects are immutable.
 *
 * @author Klaus Dorer
 */
public class Angle implements Serializable
{
	/** Constant for -180 degrees angle */
	public static final Angle ANGLE_180;

	/** Constant for +90 degrees angle */
	public static final Angle ANGLE_90;

	/** Constant for 360 degrees in rad */
	private final static double RAD360D;

	/** Constant for 180 degrees in rad */
	private final static double RAD180D;

	/** Constant for 90 degrees in rad */
	private final static double RAD90D;

	/** Constant for 0 angle */
	public static final Angle ZERO;

	/** the angle value (rad) this object represents */
	private double angle;

	static
	{
		/* Initialize constants */
		RAD360D = Math.toRadians(360);

		RAD180D = Math.toRadians(180);

		RAD90D = Math.toRadians(90);

		ANGLE_180 = Angle.deg(-180);

		ANGLE_90 = Angle.deg(90);

		ZERO = Angle.rad(0);
	}

	/**
	 * Creates a normalized angle [-RAD180 to RAD180[
	 *
	 * @param value Angle in rad
	 * @return Angle object
	 */
	public static Angle rad(double value)
	{
		return new Angle(value);
	}

	/**
	 * Creates a normalized angle [-RAD180 to RAD180[
	 *
	 * @param value Angle in degrees
	 * @return Angle object
	 */
	public static Angle deg(double value)
	{
		return new Angle(Math.toRadians(value));
	}

	private Angle(double angle)
	{
		this.angle = angle;
		normalize();
	}

	/**
	 * Retrieve the angle value in rad
	 *
	 * @return angle in radians
	 */
	public double radians()
	{
		return angle;
	}

	/**
	 * Retrieve the angle value in non normalized 0..2*PI area
	 *
	 * @return positive angle in radians
	 */
	public double radiansPositive()
	{
		if (angle < 0) {
			return 2 * Math.PI + angle;
		}
		return angle;
	}

	/**
	 * Retrieve the angle value in degrees
	 *
	 * @return Angle value
	 */
	public double degrees()
	{
		return Math.toDegrees(angle);
	}

	/**
	 * Retrieve the angle value in non normalized 0...360 degrees value area
	 *
	 * @return Angle value
	 */
	public double degreesPositive()
	{
		if (angle < 0) {
			return 360 + Math.toDegrees(angle);
		}
		return Math.toDegrees(angle);
	}

	/**
	 * Adds another angle object to this angle
	 *
	 * @param other angle object
	 * @return a new Angle object representing the sum of this and the other
	 *         angle
	 */
	public Angle add(Angle other)
	{
		return new Angle(angle + other.angle);
	}

	/**
	 * Adds a constant to this angle
	 *
	 * @param value Angle (in rad)
	 * @return a new Angle object representing the sum of this angle and the
	 *         constant
	 */
	public Angle add(double value)
	{
		return new Angle(angle + value);
	}

	/**
	 * Subtracts another angle object from this angle
	 *
	 * @param other Angle object to subtract
	 * @return a new Angle object representing the difference between this and
	 *         the other angle
	 */
	public Angle subtract(Angle other)
	{
		return new Angle(angle - other.angle);
	}

	/**
	 * Subtracts a constant angle from this angle
	 *
	 * @param value Angle in rad
	 * @return a new Angle object representing the difference between this angle
	 *         and the constant
	 */
	public Angle subtract(double value)
	{
		return new Angle(angle - value);
	}

	/**
	 * Normalize this angle
	 */
	private void normalize()
	{
		if (angle >= 0) {
			angle += RAD180D;
			angle = angle % RAD360D;
			angle -= RAD180D;
		} else {
			angle = -angle + RAD180D;
			angle = angle % RAD360D;
			angle = -(angle - RAD180D);
			if (angle >= RAD180D) {
				angle = -angle;
			}
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Angle && ((Angle) obj).angle == this.angle;
	}

	@Override
	public String toString()
	{
		return String.format(Locale.US, "%.2f\u00b0", degrees());
	}

	/**
	 * An angle is "left" of another if it is bigger, but by less than 180
	 * degrees.
	 *
	 * @param angle2 second angle object
	 * @return true if first angle is left of second
	 */
	public boolean isLeftOf(Angle angle2)
	{
		if (angle > RAD90D && angle2.angle < -RAD90D) {
			// value on -180 degree border
			return false;
		} else if (angle < -RAD90D && angle2.angle > RAD90D) {
			// value on +180 degree border
			return true;
		}

		double delta = angle2.subtract(angle).radians();
		return delta < 0 && delta > -RAD180D;
	}

	/**
	 * An angle is "right" of another if it is not "left" of it
	 *
	 * @param angle2 second angle object
	 * @return true if first angle is right of second
	 */
	public boolean isRightOf(Angle angle2)
	{
		return !isLeftOf(angle2);
	}

	/**
	 * Checks if this angle is between the passed angles. We can not use isLeft() isRight() methods here, because they
	 * consider only 180 degrees slices at most
	 * @param startAngle the start angle to check against
	 * @param endAngle the end angle to check
	 * @return true if the angle is bigger than startAngle and less than endAngle.
	 */
	public boolean isBetween(Angle startAngle, Angle endAngle)
	{
		if (startAngle.angle <= endAngle.angle) {
			return angle >= startAngle.angle && angle < endAngle.angle;
		}
		// arc contains 180 degrees border
		if (angle < endAngle.angle) {
			return true;
		}
		return angle >= startAngle.angle;
	}

	/**
	 * Calculate the average of two Angles
	 *
	 * @param angle1 first angle to use
	 * @param angle2 second angle to use
	 * @return the average of the two passed angles
	 */
	public static Angle average(Angle angle1, Angle angle2)
	{
		Angle[] angles = {angle1, angle2};
		return average(angles);
	}

	/**
	 * Calculate the average of a number of Angles
	 *
	 * @param angles Angles array
	 * @return Average
	 */
	public static Angle averageArcTan(Angle[] angles)
	{
		double sumSin = 0.0;
		double sumCos = 0.0;
		for (Angle angle : angles) {
			double angleToUse = angle.radians();
			sumSin += Math.sin(angleToUse);
			sumCos += Math.cos(angleToUse);
		}
		if (Math.abs(sumCos) < 0.0001) {
			return Angle.ZERO;
		}

		return Angle.rad(Math.atan2(sumSin, sumCos));
	}

	/**
	 * Calculate the average of a number of Angles
	 *
	 * @param angles Angles array
	 * @return Average
	 */
	public static Angle averageVectors(Angle[] angles)
	{
		Vector3D sum = Vector3D.ZERO;
		for (Angle angle : angles) {
			double angleToUse = angle.radians();
			Vector3D newVec = new Vector3D(angleToUse, 0);
			sum = sum.add(newVec);
		}
		if (sum.getNorm() > 0.0001) {
			return Angle.rad(sum.getAlpha());
		}
		return Angle.ZERO;
	}

	/**
	 * Calculate the average of a number of Angles
	 *
	 * @param angles Angles array
	 * @return Average
	 */
	public static Angle average(Angle[] angles)
	{
		double average = angles[0].radians();

		for (int i = 1; i < angles.length; i++) {
			double angleToUse = angles[i].radians();

			if (average > RAD90D && angleToUse < -RAD90D) {
				// value on -180 degree border
				angleToUse += RAD360D;
			} else if (average < -RAD90D && angleToUse > RAD90D) {
				// value on +180 degree border
				angleToUse -= RAD360D;
			}
			average = (i * average + angleToUse) / (i + 1);
		}

		return Angle.rad(average);
	}

	/**
	 * Rotates a given position by this angle (2D rotation around the z-axis).
	 *
	 * @param position the position to rotate
	 * @return the rotated position
	 */
	public Vector3D applyTo(Vector3D position)
	{
		return VectorUtils.to3D(rotate(position.getX(), position.getY(), angle));
	}

	/**
	 * Rotates a given position by this angle.
	 *
	 * @param position the position to rotate
	 * @return the rotated position
	 */
	public Vector2D applyTo(Vector2D position)
	{
		return rotate(position.getX(), position.getY(), angle);
	}

	/**
	 * Rotates a given position inversely by this angle (2D rotation around the
	 * z-axis).
	 *
	 * @param position the position to rotate
	 * @return the inversely rotated position
	 */
	public Vector3D applyInverseTo(Vector3D position)
	{
		return VectorUtils.to3D(rotate(position.getX(), position.getY(), -angle));
	}

	/**
	 * Rotates a given position inversely by this angle (2D rotation around the
	 * z-axis).
	 *
	 * @param position the position to rotate
	 * @return the inversely rotated position
	 */
	public Vector2D applyInverseTo(Vector2D position)
	{
		return rotate(position.getX(), position.getY(), -angle);
	}

	/**
	 * Rotates a given position by this angle (2D rotation around the z-axis).
	 *
	 * @param x the x position value
	 * @param y the y position value
	 * @return the rotated position
	 */
	public Vector2D applyTo(double x, double y)
	{
		return rotate(x, y, angle);
	}

	/**
	 * Rotates a given position inversely by this angle (2D rotation around the
	 * z-axis).
	 *
	 * @param x the x position value
	 * @param y the y position value
	 * @return the inversely rotated position
	 */
	public Vector2D applyInverseTo(double x, double y)
	{
		return rotate(x, y, -angle);
	}

	/**
	 * Rotate the given position by the given angle around the z-axis.
	 *
	 * @param x the x position value
	 * @param y the y position value
	 * @param angleRad the rotation angle
	 * @return the rotated position
	 */
	private Vector2D rotate(double x, double y, double angleRad)
	{
		double cosAngle = Math.cos(angleRad);
		double sinAngle = Math.sin(angleRad);

		// Calculate new position
		double newX = cosAngle * x - sinAngle * y;
		double newY = sinAngle * x + cosAngle * y;

		return new Vector2D(newX, newY);
	}

	/**
	 * @return the angle that sums up to 180 degrees with this angle if this
	 *         angle is positive or to -180 degrees if this angle is negative.
	 */
	public Angle getAdjacencyAngle()
	{
		return Angle.ANGLE_180.subtract(this);
	}

	/**
	 * @return the angle with same absolute value but different sign
	 */
	public Angle negate()
	{
		return Angle.rad(-angle);
	}

	/**
	 * Limits the passed angle to the range passed. The returned angle will be
	 * in the interval [min, max]. If min > max then the angle value is left unchanged.
	 * @param min the smallest angle possible
	 * @param max the highest angle possible
	 * @return the passed angle limited by the passed limits
	 */
	public Angle limit(Angle min, Angle max)
	{
		if (min.radians() > max.radians()) {
			return this;
		} else if (angle < min.radians()) {
			return min;
		} else if (angle > max.radians()) {
			return max;
		} else {
			return this;
		}
	}

	public static Angle to(Vector2D point)
	{
		if (point.getX() == 0 && point.getY() == 0) {
			return Angle.ZERO;
		}

		return new Angle(Math.atan2(point.getY(), point.getX()));
	}

	public static Angle to(Vector3D point)
	{
		return to(VectorUtils.to2D(point));
	}

	public static Angle average(Angle a1, Angle a2, int weight1, int weight2)
	{
		return new Angle(
				((weight1 * (a1.radians() + Math.PI) + weight2 * (a2.radians() + Math.PI)) / (weight1 + weight2)) -
				Math.PI);
	}
}
