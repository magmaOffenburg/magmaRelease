/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer.impl;

import hso.autonomy.agent.model.worldmodel.localizer.IWeightedParticle;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose3D;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class WeightedParticle implements IWeightedParticle
{
	private IPose3D pose;
	private double probability;
	protected double weight;

	public WeightedParticle(IPose3D pose, double probability)
	{
		this.pose = pose;
		this.probability = probability;
	}

	/**
	 * Copy constructor to reproduce the given particle
	 * @param particle particle to reproduce
	 */
	public WeightedParticle(IWeightedParticle particle)
	{
		this.pose = particle.getPose();
		this.probability = particle.getProbability();
	}

	@Override
	public IPose3D getPose()
	{
		return pose;
	}

	@Override
	public void updatePose(Vector3D odomTrans, Angle deltaZAngle, Rotation odomRotation)
	{
		double newZ = odomTrans.getZ();
		// add noisy delta z angle to current z orientation
		Angle horizontalAngle = Geometry.getHorizontalAngle(pose.getOrientation()).add(deltaZAngle);
		odomTrans = horizontalAngle.applyTo(odomTrans);
		double newX = pose.getX() + odomTrans.getX();
		double newY = pose.getY() + odomTrans.getY();

		Rotation zRotation = Geometry.createZRotation(horizontalAngle.radians());

		this.pose = new Pose3D(new Vector3D(newX, newY, newZ), zRotation.applyTo(odomRotation));
	}

	@Override
	public void updateRotation(Rotation newRotation)
	{
		this.pose = new Pose3D(pose.getPosition(), newRotation);
	}

	@Override
	public double getWeight()
	{
		return weight;
	}

	@Override
	public void setWeight(double newWeight)
	{
		this.weight = newWeight;
	}

	@Override
	public double getProbability()
	{
		return probability;
	}

	@Override
	public void setProbability(double newProbability)
	{
		this.probability = newProbability;
	}

	@Override
	public String toString()
	{
		return "Position= " + pose.getPosition() + " | Angle= " + pose.getHorizontalAngle() + " | Weight: " + weight +
				" | probability= " + probability;
	}
}
