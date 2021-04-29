/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import java.util.ArrayList;
import java.util.List;
import kdo.util.parameter.ParameterMap;
import magma.agent.IHumanoidConstants;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.complex.path.Path;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IThisPlayer;
import magma.util.benchmark.PathParameterWalkBenchmarkItem;

/**
 * @author Stefan Grossmann
 */
public class IKStepPlanBehavior extends IKWalkBehavior
{
	/** target for the last foot */
	private Pose2D footTarget;

	/** path for that feet has to plan */
	private Path currPath;

	/** support foot */
	private SupportFoot supportFoot;

	/** forward step plan */
	private List<Pose2D> steps;

	/** new step plan */
	private List<Pose2D> newSteps;

	// private Vector3D speedAtTarget;

	public IKStepPlanBehavior(IRoboCupThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors)
	{
		super(IBehaviorConstants.IK_STEP_PLAN, thoughtModel, params, behaviors);
	}

	/**
	 * set attributes by given parameters
	 * @param footTarget the target position and orientation for any leg
	 */
	public void setStepTarget(Path currPath, Pose2D footTarget, SupportFoot supportFoot)
	{
		this.footTarget = footTarget;
		this.supportFoot = supportFoot;
		this.currPath = currPath;
		if (intendedStep.sideward == 0 && intendedStep.forward == 0) {
			intendedStep = new Step(0, 0.08, 0.02, Angle.ZERO);
			// setSupportFoot(switchSupportFoot(walkMovement.supportFoot));
		}
		calculateStepPlan(intendedStep);
	}

	@Override
	public void perform()
	{
		perform(intendedStep);
	}

	/**
	 * calculate first forward stepplan with old walk parameters. then
	 * recalculate all feet that last foot reaches the foottarget
	 */
	protected void calculateStepPlan(Step intendedStep)
	{
		steps = new ArrayList<>();
		newSteps = new ArrayList<>();
		int countSteps = 0;
		Step lastIntendedStep = intendedStep.copy();

		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();
		SupportFoot supportFoot = walkMovement.supportFoot;
		String otherFootName = supportFoot == SupportFoot.LEFT ? IHumanoidConstants.LFoot : IHumanoidConstants.RFoot;
		Pose3D currentOtherFootPose = getAgentModel().getBodyPart(otherFootName).getPose();
		Pose2D dynFoot = thisPlayer.calculateGlobalBodyPose2D(currentOtherFootPose);

		while (!(Math.abs(footTarget.getAngleTo(dynFoot).degrees()) < 90 && this.supportFoot == supportFoot &&
				 footTarget.getDistanceTo(dynFoot) < 0.5)) {
			PathParameterWalkBenchmarkItem item;
			if (!currPath.stillOnPath(dynFoot)) {
				System.out.println("notonpath");
			}
			currPath.updateWithPose(dynFoot);
			item = currPath.get(0).item;
			setMovement(item.getForwardsBackwards(), item.getRadius(), item.getAngle());
			intendedStep = calculateStep(intendedStep, intendedWalk, intendedTurn, paramSetName);
			dynFoot = calculateFreeFootPose(dynFoot, supportFoot, intendedStep);
			steps.add(dynFoot);
			supportFoot = switchSupportFoot(supportFoot);
			countSteps++;
		}

		// last footPose and difference to footTarget
		Pose2D lastStep = steps.get(steps.size() - 1);
		Pose2D diffLast2Target = new Pose2D(
				lastStep.x - footTarget.x, lastStep.y - footTarget.y, lastStep.angle.subtract(footTarget.angle));

		// all of the new footPoses will be recalculated
		int count = 1;
		for (Pose2D old : steps) {
			Pose2D newOne = new Pose2D();
			newOne.x = old.x - (diffLast2Target.x * count / countSteps);
			newOne.y = old.y - (diffLast2Target.x * count / countSteps);
			newOne.angle = old.angle.subtract(Angle.deg(diffLast2Target.angle.degrees() * count / countSteps));
			count++;
			newSteps.add(newOne);
		}

		// needed intendedStep for first recalculated footPose, but how??!?!
		// TODO: !!
		intendedStep = lastIntendedStep.copy();
	}

	/**
	 * Switches supportFoot to the other one
	 * @return other supportFoot than current
	 */
	private SupportFoot switchSupportFoot(SupportFoot supportFoot)
	{
		return (supportFoot == SupportFoot.LEFT) ? SupportFoot.RIGHT : SupportFoot.LEFT;
	}

	public List<Pose2D> getStepPlan()
	{
		return steps;
	}

	public List<Pose2D> getNewStepPlan()
	{
		return newSteps;
	}
}
