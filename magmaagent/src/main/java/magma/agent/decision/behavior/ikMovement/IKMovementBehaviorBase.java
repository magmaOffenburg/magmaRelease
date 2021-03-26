/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import hso.autonomy.agent.model.agentmodel.IBodyModel;
import hso.autonomy.util.geometry.Pose6D;
import magma.agent.IHumanoidConstants;
import magma.agent.decision.behavior.basic.RoboCupBehavior;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

/**
 * @author Stefan Glaser
 */
public abstract class IKMovementBehaviorBase extends RoboCupBehavior
{
	protected IIKMovement currentMovement;

	protected transient BalancingEngine balancingEngine;

	protected boolean newMovementStarted;

	public IKMovementBehaviorBase(String name, IRoboCupThoughtModel thoughtModel)
	{
		super(name, thoughtModel);
		balancingEngine = new BalancingEngine();
	}

	public IIKMovement getCurrentMovement()
	{
		return currentMovement;
	}

	@Override
	public void perform()
	{
		super.perform();

		IRoboCupAgentModel agentModel = getAgentModel();

		newMovementStarted = false;
		// if the current movement is finished, decide for the next one and
		// initialize it
		if (currentMovement == null || currentMovement.isFinished()) {
			IIKMovement previousMovement = currentMovement;
			currentMovement = decideNextMovement();
			currentMovement.init(previousMovement);
			newMovementStarted = true;
		}

		// if the current movement indicates an abort, decide for the next
		// movement, initialize and update it
		if (!currentMovement.update()) {
			IIKMovement previousMovement = currentMovement;
			currentMovement = decideNextMovement();
			currentMovement.init(previousMovement);
			currentMovement.update();
			newMovementStarted = true;
		}

		// transform foot poses via the balancing engine
		Pose6D[] adjustedPoses = BalancingEngine.adjustTargetPoses(getWorldModel().getThisPlayer().getOrientation(),
				currentMovement, currentMovement.getLeftFootPose(), currentMovement.getRightFootPose());

		/*Vector3D pos = getThoughtModel().getWorldModel().getThisPlayer().getPose2D().getPosition3D();
		getThoughtModel().getRoboVizDraw().drawPoint(
				"leftFoot", pos.add(new Vector3D(adjustedPoses[0].y, adjustedPoses[0].x, 0)), 10, Color.RED);
		getThoughtModel().getRoboVizDraw().drawPoint(
				"rightFoot", pos.add(new Vector3D(adjustedPoses[1].y, adjustedPoses[1].x, 0)), 10, Color.ORANGE);*/

		// perform movement poses
		IBodyModel bodyModel = agentModel.getFutureBodyModel();
		bodyModel.moveBodyPartToPose(IHumanoidConstants.LFoot, adjustedPoses[0]);
		bodyModel.moveBodyPartToPose(IHumanoidConstants.RFoot, adjustedPoses[1]);
	}

	/**
	 * Decide for the next movement to perform.
	 *
	 * @return the next movement to perform
	 */
	protected abstract IIKMovement decideNextMovement();

	@Override
	public void abort()
	{
		super.abort();
		currentMovement = null;
	}
}