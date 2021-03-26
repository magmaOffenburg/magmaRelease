/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement.balancing;

import hso.autonomy.util.geometry.Pose6D;
import magma.agent.decision.behavior.ikMovement.IIKMovement;
import magma.agent.decision.behavior.ikMovement.basic.IKCosineMovement;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Stefan Glaser
 */
public class IKBalanceOnLegMovement extends IKCosineMovement
{
	/** The target pose of the support foot */
	protected Pose6D supportFootTargetPose;

	/** The target pose of the free foot */
	protected Pose6D freeFootTargetPose;

	public IKBalanceOnLegMovement(IRoboCupThoughtModel thoughtModel)
	{
		this(thoughtModel, new Pose6D(), new Pose6D(), Vector3D.PLUS_K);
	}

	public IKBalanceOnLegMovement(IRoboCupThoughtModel thoughtModel, Pose6D supportFootTargetPose,
			Pose6D freeFootTargetPose, Vector3D targetLeaning)
	{
		this(thoughtModel, supportFootTargetPose, freeFootTargetPose, targetLeaning, 18, 0);
	}

	public IKBalanceOnLegMovement(IRoboCupThoughtModel thoughtModel, Pose6D supportFootTargetPose,
			Pose6D freeFootTargetPose, Vector3D targetLeaning, int movementCycles, int holdCycles)
	{
		super("BalanceOnLegMovement", thoughtModel, movementCycles, holdCycles);

		this.supportFootTargetPose = supportFootTargetPose;
		this.freeFootTargetPose = freeFootTargetPose;
		this.intendedLeaningVector = targetLeaning;

		isStatic = false;
	}

	@Override
	public void init(IIKMovement other)
	{
		if (other != null) {
			// If we are morphing from an existing movement, determine the next
			// support foot based on the previously performed movement. Otherwise
			// keep the current support foot (since it may have been set from
			// outside).
			if (other.getNextSupportFoot() == SupportFoot.LEFT) {
				supportFoot = SupportFoot.LEFT;
			} else {
				supportFoot = SupportFoot.RIGHT;
			}
		}

		// // HACK //////////////////////////////////////////////////
		// if (supportFoot == SupportFoot.LEFT) {
		// freeFootTargetPose = new Pose6D(0.075, -0.16, -0.17f, -70, 0, 0);
		// supportFootTargetPose = new Pose6D(-0.015, 0.02, -0.3);
		// intendedLeaningVector = new Rotation(Vector3D.PLUS_J,
		// Math.toRadians(-12)).applyTo(Vector3D.PLUS_K);
		// } else {
		// freeFootTargetPose = new Pose6D(-0.075, -0.16, -0.17f, -70, 0, 0);
		// supportFootTargetPose = new Pose6D(0.015, 0.02, -0.3);
		// intendedLeaningVector = new Rotation(Vector3D.PLUS_J,
		// Math.toRadians(12)).applyTo(Vector3D.PLUS_K);
		// }
		// // END HACK //////////////////////////////////////////////

		if (supportFoot == SupportFoot.LEFT) {
			leftFootTargetPose.set(supportFootTargetPose);
			rightFootTargetPose.set(freeFootTargetPose);
		} else {
			leftFootTargetPose.set(freeFootTargetPose);
			rightFootTargetPose.set(supportFootTargetPose);
		}

		super.init(other);
	}

	public void setTargets(Pose6D supportFootTargetPose, Pose6D freeFootTargetPose, Vector3D intendedLeaning)
	{
		this.supportFootTargetPose.set(supportFootTargetPose);
		this.freeFootTargetPose.set(freeFootTargetPose);
		this.intendedLeaningVector = intendedLeaning;
	}
}
