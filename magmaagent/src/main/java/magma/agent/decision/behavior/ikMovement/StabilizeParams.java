/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import java.io.Serializable;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Parameter class for stabilize on leg model
 * @author kdorer
 */
public class StabilizeParams implements Serializable
{
	protected Vector3D stabilizationLegTargetPosition;

	protected double stabilizationLegTargetAngle;

	public final Vector3D supportFootStabilizationPosition;

	public final Vector3D freeFootStabilizationPosition;

	public final Vector3D freeFootTargetPosition;

	public final Vector3D freeFootTargetAngles;

	public final double intendedTargetLeaningSidewards;

	public final double intendedTargetLeaningForwards;

	protected final double walkHeight;

	private static double stabilizationHeight = -0.3f;

	public StabilizeParams(Vector3D stabilizationLegTargetPosition, double stabilizationLegTargetAngle,
			Vector3D supportFootStabilizationPosition, Vector3D freeFootStabilizationPosition,
			Vector3D freeFootTargetPosition, Vector3D freeFootTargetAngles, double intendedTargetLeaningSidewards,
			double intendedTargetLeaningForwards, double walkHeight)
	{
		this.stabilizationLegTargetPosition = stabilizationLegTargetPosition;
		this.stabilizationLegTargetAngle = stabilizationLegTargetAngle;
		this.supportFootStabilizationPosition = supportFootStabilizationPosition;
		this.freeFootStabilizationPosition = freeFootStabilizationPosition;
		this.freeFootTargetPosition = freeFootTargetPosition;
		this.freeFootTargetAngles = freeFootTargetAngles;
		this.intendedTargetLeaningSidewards = intendedTargetLeaningSidewards;
		this.intendedTargetLeaningForwards = intendedTargetLeaningForwards;
		this.walkHeight = walkHeight;
	}

	public static StabilizeParams getLeftKickStraightParams()
	{
		double walkHeight = -0.255f;
		return new StabilizeParams(new Vector3D(0.13, 0, 0), 0, new Vector3D(0.015, 0.02, stabilizationHeight),
				new Vector3D(-0.075, 0, walkHeight), new Vector3D(-0.075, -0.16, -0.17f), new Vector3D(-70, 0, 0), 12,
				0, walkHeight);
	}

	public static StabilizeParams getRightKickStraightParams()
	{
		double walkHeight = -0.255f;
		return new StabilizeParams(new Vector3D(-0.13, 0, 0), 0, new Vector3D(-0.015, 0.02, stabilizationHeight),
				new Vector3D(0.075, 0, walkHeight), new Vector3D(0.075, -0.16, -0.17f), new Vector3D(-70, 0, 0), -12, 0,
				walkHeight);
	}
}
