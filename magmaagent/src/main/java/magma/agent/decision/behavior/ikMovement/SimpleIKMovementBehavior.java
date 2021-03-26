/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import hso.autonomy.agent.decision.behavior.IBehavior;
import java.util.ArrayList;
import java.util.List;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.ikMovement.balancing.IKGetReadyMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

public class SimpleIKMovementBehavior extends IKMovementBehaviorBase
{
	private List<IIKMovement> movements;

	private int movementIndex;

	public SimpleIKMovementBehavior(IRoboCupThoughtModel thoughtModel, ParameterMap params)
	{
		super(IBehaviorConstants.IK_MOVEMENT, thoughtModel);

		movementIndex = 0;
		this.movements = new ArrayList<>();

		IIKMovement newMovement;
		// newMovement = new IKTappleMovementDemo();
		// movements.add(newMovement);
		// movements.add(newMovement);

		IKWalkMovementParametersBase param = (IKWalkMovementParametersBase) params.get(name);
		if (param == null) {
			param = new IKWalkMovementParametersBase();
		}
		// newMovement = new IKForwardMovementDemo(param);
		// movements.add(newMovement);
		// movements.add(newMovement);

		// newMovement = new IKBalancedForwardMovementDemo();
		// movements.add(newMovement);
		// movements.add(newMovement);

		// newMovement = new IKBalancedForwardTurnMovementDemo();
		// movements.add(newMovement);
		// movements.add(newMovement);

		// newMovement = new IKSidewardsMovementDemo();
		// movements.add(newMovement);
		// movements.add(newMovement);

		// newMovement = new IKLegRotationMovementDemo();
		// movements.add(newMovement);
		// movements.add(newMovement);

		// newMovement = new IKTurnMovementDemo();
		// movements.add(newMovement);
		// movements.add(newMovement);

		// newMovement = new IKCoMShiftingStepMovement();
		// movements.add(newMovement);
		//
		// newMovement = new IKGetOnLegStepMovement();
		// movements.add(newMovement);
		//
		// newMovement = new IKBalanceOnLegMovement();
		// movements.add(newMovement);
		//
		// newMovement = new IKGetOnLegStepMovement();
		// movements.add(newMovement);

		newMovement = new IKGetReadyMovement(thoughtModel);
		movements.add(newMovement);
	}

	@Override
	public void init()
	{
		super.init();
		movementIndex = 0;
		// currentMovement.init(worldModel, agentModel, null);
	}

	@Override
	protected IIKMovement decideNextMovement()
	{
		IIKMovement newMovement = movements.get(movementIndex);

		// Proceed Movements
		movementIndex++;
		if (movementIndex >= movements.size()) {
			movementIndex = 0;
		}

		return newMovement;
	}

	@Override
	public boolean isFinished()
	{
		return movementIndex == 0 && (currentMovement != null && currentMovement.isFinished());
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		IBehavior realBehavior = actualBehavior.getRootBehavior();

		if (realBehavior == this) {
			return this;
		}

		return super.switchFrom(actualBehavior);
	}
}
