package magma.agent.decision.behavior.complex.misc;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.geometry.VectorUtils;
import java.util.List;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModelThin;
import magma.common.spark.PlaySide;
import magma.robots.nao.INaoConstants;

public class AttackThin extends Attack
{
	/** how far a ball might be to kick it. Is 0.7 in 2D league */
	public static final double KICKABLE_MARGIN = 0.44;

	/** the maximum distance we should be able to kick */
	public static final double MAX_KICK_DISTANCE = 15;

	public AttackThin(IThoughtModel thoughtModel, BehaviorMap behaviors, List<String> availableKicks)
	{
		super(thoughtModel, behaviors, availableKicks);
	}

	public AttackThin(String name, IThoughtModel thoughtModel, BehaviorMap behaviors, List<String> availableKicks)
	{
		super(name, thoughtModel, behaviors, availableKicks);
	}

	@Override
	protected List<IBehavior> checkKicks()
	{
		double distanceBall = getWorldModel()
									  .getThisPlayer()
									  .getPose2D()
									  .getPosition3D()
									  .subtract(getWorldModel().getBall().getPosition())
									  .getNorm();
		if (distanceBall < KICKABLE_MARGIN) {
			double kickPower = getIntendedKickDistance();
			Angle globalHorizontalAngle = getIntendedKickDirection();
			float verticalAngle = 45;
			if (getWorldModel().getBall().getDistanceToXY(getWorldModel().getOtherGoalPosition()) < 10) {
				verticalAngle = 25;
			}
			Angle relativeHorizontalAngle =
					globalHorizontalAngle.subtract(getWorldModel().getThisPlayer().getHorizontalAngle());

			// adjust kick power
			IPose3D torso = getAgentModel().getBodyPart(INaoConstants.Torso).getPose();
			IPose3D ball = new Pose3D(getWorldModel().getBall().getPosition());
			double deltaAngle = Math.abs(torso.getHorizontalAngleTo(ball).degrees()) / 180;
			// kick model from proxy
			double factor = 1 - 0.25 * deltaAngle - 0.25 * distanceBall / KICKABLE_MARGIN;
			kickPower = (float) Math.max(0.4, Math.min(MAX_KICK_DISTANCE, (kickPower / factor)));

			((IRoboCupThoughtModelThin) getThoughtModel())
					.setKick((float) kickPower, (float) relativeHorizontalAngle.degrees(), verticalAngle);
		}

		return null;
	}
}
