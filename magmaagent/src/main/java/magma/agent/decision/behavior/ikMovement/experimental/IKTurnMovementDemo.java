/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement.experimental;

import hso.autonomy.util.geometry.Angle;
import magma.agent.decision.behavior.ikMovement.walk.IKStaticWalkMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase.Param;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class IKTurnMovementDemo extends IKStaticWalkMovement
{
	public IKTurnMovementDemo(IRoboCupThoughtModel thoughtModel, IKWalkMovementParametersBase params)
	{
		super("TurnDemo", thoughtModel, params);

		setMovementCycles(10);

		params.put(Param.WALK_WIDTH, 0.065f);
		params.put(Param.WALK_HEIGHT, -0.255f);
		params.put(Param.WALK_OFFSET, 0.045f);
		params.put(Param.PUSHDOWN_FACTOR, 0.5f);
		params.put(Param.FOOT_SLANT_ANGLE, 0);

		currentStep.upward = 0.03;
		currentStep.turn = Angle.deg(60);
	}

	@Override
	public Vector3D getIntendedLeaningVector()
	{
		return new Rotation(Vector3D.PLUS_I, Math.toRadians(-5), RotationConvention.VECTOR_OPERATOR)
				.applyTo(Vector3D.PLUS_K);
	}
}
