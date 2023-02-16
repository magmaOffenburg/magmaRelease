/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.general.agentruntime;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.worldmodel.IWorldModel;
import java.util.ArrayList;
import java.util.List;
import kdo.util.parameter.ParameterMap;
import magma.agent.UglyConstants;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.decision.behavior.complex.kick.StabilizedKick;
import magma.agent.decision.behavior.complex.misc.Attack;
import magma.agent.decision.behavior.ikMovement.IKKickBehavior;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.thoughtmodel.IKickPositionProfiler;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.impl.PenaltyKickPositionProfiler;
import magma.agent.model.thoughtmodel.impl.RoboCupThoughtModel;
import magma.agent.model.thoughtmodel.impl.RoboCupThoughtModelThin;
import magma.agent.model.thoughtmodel.strategy.IRoleManager;
import magma.agent.model.thoughtmodel.strategy.impl.RoleManager;
import magma.agent.model.thoughtmodel.strategy.impl.strategies.PenaltyKickerStrategy;
import magma.agent.model.worldmeta.IRoboCupWorldMetaModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.impl.RoboCupWorldModel;
import magma.robots.nao.decision.behavior.movement.kick.Kick11m;
import magma.util.roboviz.RoboVizDraw;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class PenaltyComponentFactory extends NaoComponentFactory
{
	public static final String NAME = "Penalty";

	@Override
	protected List<String> createDefaultAvailableKicks(BehaviorMap behaviors)
	{
		List<String> kicks = new ArrayList<>();
		addKick(behaviors, kicks, IBehaviorConstants.KICK_11M.FULL);
		return kicks;
	}

	@Override
	public IRoboCupWorldModel createWorldModel(
			IRoboCupAgentModel agentModel, IRoboCupWorldMetaModel worldMetaModel, String teamName, int playerNumber)
	{
		RoboCupWorldModel worldModel =
				new RoboCupWorldModel(agentModel, createLocalizer(agentModel), worldMetaModel, teamName, playerNumber) {
					@Override
					public boolean isInCriticalArea(Vector3D position)
					{
						return position.getX() < -fieldHalfLength() + penaltyWidth() - 0.3 &&
								Math.abs(position.getY()) < penaltyHalfLength() + goalHalfWidth() - 0.3;
					}
				};
		worldModel.setPenalty(true);
		return worldModel;
	}

	@Override
	public IRoboCupThoughtModel createThoughtModel(
			IAgentModel agentModel, IRoboCupWorldModel worldModel, RoboVizDraw roboVizDraw)
	{
		if (UglyConstants.thinClient) {
			return new RoboCupThoughtModelThin(agentModel, worldModel, roboVizDraw) {
				@Override
				protected void updateTeamStrategy()
				{
				}
			};
		} else {
			return new RoboCupThoughtModel(agentModel, worldModel, roboVizDraw) {
				@Override
				protected void updateTeamStrategy()
				{
				}
			};
		}
	}

	@Override
	public IRoleManager createRoleManager(
			IRoboCupThoughtModel thoughtModel, IWorldModel worldModel, String teamStrategyName)
	{
		return new RoleManager(worldModel, new PenaltyKickerStrategy(thoughtModel));
	}

	@Override
	public IKickPositionProfiler createKickPositionProfiler(IRoboCupThoughtModel thoughtModel)
	{
		return new PenaltyKickPositionProfiler(thoughtModel);
	}

	@Override
	protected void createSpecificBehaviors(
			IRoboCupThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors)
	{
		super.createSpecificBehaviors(thoughtModel, params, behaviors);

		IWalkEstimator walkEstimator = createWalkEstimator();

		behaviors.put(IKKickBehavior.getKickStabilizationLeft(KICK_11M.KICK.BASE_NAME, KICK_11M.STABILIZE, thoughtModel,
				params, Float.MIN_VALUE, Kick11m.MAX_KICK_DISTANCE, walkEstimator));
		behaviors.put(IKKickBehavior.getKickStabilizationRight(KICK_11M.KICK.BASE_NAME, KICK_11M.STABILIZE,
				thoughtModel, params, Float.MIN_VALUE, Kick11m.MAX_KICK_DISTANCE, walkEstimator));

		behaviors.put(new StabilizedKick(
				KICK_11M.FULL.LEFT, thoughtModel, params, behaviors, KICK_11M.STABILIZE.LEFT, KICK_11M.KICK.LEFT));
		behaviors.put(new StabilizedKick(
				KICK_11M.FULL.RIGHT, thoughtModel, params, behaviors, KICK_11M.STABILIZE.RIGHT, KICK_11M.KICK.RIGHT));
	}
}
