/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.benchmark;

import hso.autonomy.util.geometry.Angle;

/**
 *
 * @author Stefan Grossmann
 *
 */
public class PathParameterWalkBenchmarkItem
{
	private double itemQuality;

	private double forwardsBackwards;

	private double rightLeft;

	private double angle;

	private double utility;

	private double avgSpeed;

	private double radius;

	public PathParameterWalkBenchmarkItem(
			double forwardsBackwards, double rightLeft, double angle, double utility, double avgSpeed, double radius)
	{
		this.itemQuality = 1;
		this.forwardsBackwards = forwardsBackwards;
		this.rightLeft = rightLeft;
		this.angle = angle;
		this.utility = utility;
		this.avgSpeed = avgSpeed;
		this.radius = radius;
	}

	/**
	 * initialize constructor by another object
	 * @param other benchmark object
	 */
	public PathParameterWalkBenchmarkItem(PathParameterWalkBenchmarkItem other)
	{
		this.itemQuality = other.itemQuality;
		this.rightLeft = other.rightLeft;
		this.forwardsBackwards = other.forwardsBackwards;
		this.angle = other.angle;
		this.utility = other.utility;
		this.avgSpeed = other.avgSpeed;
		this.radius = other.radius;
	}

	/**
	 * updates a benchmark item by calculation new average
	 * @param item: to calculate
	 */
	public void update(PathParameterWalkBenchmarkItem item)
	{
		double newQuality = itemQuality + item.itemQuality;
		utility = (utility * itemQuality + item.utility * item.itemQuality) / newQuality;
		if (item.utility != 0) {
			avgSpeed = (avgSpeed * itemQuality + item.avgSpeed * item.itemQuality) / newQuality;
			radius = (radius * itemQuality + item.radius * item.itemQuality) / newQuality;
		}
		itemQuality = newQuality;
	}

	@Override
	public boolean equals(Object other)
	{
		PathParameterWalkBenchmarkItem item = (PathParameterWalkBenchmarkItem) other;
		return this.forwardsBackwards == item.forwardsBackwards && this.rightLeft == item.rightLeft &&
				this.angle == item.angle;
	}

	// Getter and Setter
	public double getForwardsBackwards()
	{
		return forwardsBackwards;
	}

	public void setForwardsBackwards(double forwardsBackwards)
	{
		this.forwardsBackwards = forwardsBackwards;
	}

	public double getRightLeft()
	{
		return rightLeft;
	}

	public void setRightLeft(double rightLeft)
	{
		this.rightLeft = rightLeft;
	}

	public Angle getAngle()
	{
		return Angle.deg(angle);
	}

	public void setAngle(double angle)
	{
		this.angle = angle;
	}

	public double getUtility()
	{
		return utility;
	}

	public void setUtility(double utility)
	{
		this.utility = utility;
	}

	public double getAvgSpeed()
	{
		return avgSpeed;
	}

	public void setAvgSpeed(double avgSpeed)
	{
		this.avgSpeed = avgSpeed;
	}

	public double getRadius()
	{
		return radius;
	}

	public void setRadius(double radius)
	{
		this.radius = radius;
	}
}
