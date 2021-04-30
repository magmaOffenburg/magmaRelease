/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.general.agentruntime;

import hso.autonomy.agent.communication.channel.IChannelManager;
import hso.autonomy.agent.communication.channel.IOutputChannel;
import hso.autonomy.agent.communication.channel.impl.ChannelManager;
import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import hso.autonomy.agent.model.agentmodel.impl.ik.IAgentIKSolver;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.properties.PropertyManager;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import kdo.util.parameter.ParameterMap;
import magma.agent.IMagmaConstants;
import magma.agent.UglyConstants;
import magma.agent.agentruntime.ComponentFactory;
import magma.agent.communication.channel.IRoboCupChannel;
import magma.agent.communication.channel.impl.ChannelParameters;
import magma.agent.communication.channel.impl.SimsparkChannel;
import magma.agent.decision.behavior.IBaseWalk;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.decision.behavior.base.WalkEstimator;
import magma.agent.decision.behavior.basic.SendPassCommand;
import magma.agent.decision.behavior.complex.goalie.GoaliePositioning;
import magma.agent.decision.behavior.complex.kick.StabilizedKick;
import magma.agent.decision.behavior.complex.misc.Attack;
import magma.agent.decision.behavior.complex.misc.AttackThin;
import magma.agent.decision.behavior.complex.misc.KickChallengeAttack;
import magma.agent.decision.behavior.complex.misc.PassingChallengeAttack;
import magma.agent.decision.behavior.complex.path.WalkPath;
import magma.agent.decision.behavior.complex.walk.Dribbling;
import magma.agent.decision.behavior.complex.walk.PassivePositioning;
import magma.agent.decision.behavior.complex.walk.SearchBall;
import magma.agent.decision.behavior.complex.walk.Walk;
import magma.agent.decision.behavior.complex.walk.WalkToPosition;
import magma.agent.decision.behavior.ikMovement.IKKickBehavior;
import magma.agent.decision.behavior.ikMovement.IKStabilizeOnLegBehavior;
import magma.agent.decision.behavior.ikMovement.IKStepPlanBehavior;
import magma.agent.decision.behavior.ikMovement.IKStepWalkBehavior;
import magma.agent.decision.behavior.ikMovement.IKWalkBehavior;
import magma.agent.decision.behavior.ikMovement.SimpleIKMovementBehavior;
import magma.agent.decision.behavior.ikMovement.SwingArms;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;
import magma.agent.decision.behavior.movement.SidedMovementBehavior.Side;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.DecisionMakerConfigurationHelper;
import magma.robots.nao.decision.behavior.dynamic.FocusBall;
import magma.robots.nao.decision.behavior.dynamic.FocusBallGoalie;
import magma.robots.nao.decision.behavior.movement.GetReady;
import magma.robots.nao.decision.behavior.movement.MoveZero;
import magma.robots.nao.decision.behavior.movement.fall.FallBack;
import magma.robots.nao.decision.behavior.movement.fall.FallForward;
import magma.robots.nao.decision.behavior.movement.fall.FallSide;
import magma.robots.nao.decision.behavior.movement.fall.MoveArmsToFall;
import magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk.KickWalk;
import magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk.KickWalkBackward;
import magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk.KickWalkBackwardParameters;
import magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk.KickWalkParameters;
import magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk.KickWalkSide;
import magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk.KickWalkSideParameters;
import magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk.KickWalkStanding;
import magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk.KickWalkStandingParameters;
import magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk.ParameterListComposite;
import magma.robots.nao.decision.behavior.movement.getup.GetUpFromBack;
import magma.robots.nao.decision.behavior.movement.getup.GetUpFromBackParameters;
import magma.robots.nao.decision.behavior.movement.getup.GetUpFromFront;
import magma.robots.nao.decision.behavior.movement.getup.GetUpFromFrontParameters;
import magma.robots.nao.decision.behavior.movement.keep.KeepCenter;
import magma.robots.nao.decision.behavior.movement.keep.KeepSide;
import magma.robots.nao.decision.behavior.movement.kick.Kick11m;
import magma.robots.nao.decision.behavior.movement.kick.Kick11mParameters;
import magma.robots.nao.decision.behavior.movement.kick.Kick8m;
import magma.robots.nao.decision.behavior.movement.kick.Kick8mParameters;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;
import magma.robots.nao.model.agentmodel.ik.impl.NaoLegCalculator;

public class NaoComponentFactory extends ComponentFactory
{
	@Override
	public IRoboCupAgentMetaModel getAgentMetaModel()
	{
		return NaoAgentMetaModel.INSTANCE;
	}

	protected List<String> createDefaultAvailableKicks(BehaviorMap behaviors)
	{
		List<String> kicks = new ArrayList<>();

		addKick(behaviors, kicks, IBehaviorConstants.KICK_8M.FULL);
		addKick(behaviors, kicks, IBehaviorConstants.KICK_11M.FULL);

		addKick(behaviors, kicks, IBehaviorConstants.DRIBBLE);

		for (int i = 30; i <= 120; i += 30) {
			kicks.add(IBehaviorConstants.DRIBBLE.LEFT + i);
			kicks.add(IBehaviorConstants.DRIBBLE.RIGHT + i);

			kicks.add(IBehaviorConstants.DRIBBLE.LEFT + (-i));
			kicks.add(IBehaviorConstants.DRIBBLE.RIGHT + (-i));
		}

		addKick(behaviors, kicks, IBehaviorConstants.KICK_WALK_STANDING);
		// addKick(behaviors, kicks, IBehaviorConstants.KICK_WALK_BACKWARD);
		// addKick(behaviors, kicks, IBehaviorConstants.KICK_WALK_SIDE);

		return kicks;
	}

	protected void addKick(BehaviorMap behaviors, List<String> kicks, SidedBehaviorConstants constants)
	{
		addKick(behaviors, kicks, constants.LEFT);
		addKick(behaviors, kicks, constants.RIGHT);
	}

	private void addKick(BehaviorMap behaviors, List<String> kicks, String name)
	{
		if (behaviors.get(name) != null) {
			kicks.add(name);
		} else {
			System.err.println("Behavior not existing: " + name);
		}
	}

	@Override
	public IChannelManager createChannelManager(ChannelParameters info)
	{
		IChannelManager channelManager = new ChannelManager();
		IRoboCupChannel channel = new SimsparkChannel(channelManager, info);
		List<String> initParams = new ArrayList<>();
		initParams.add(getAgentMetaModel().getSceneString());
		channel.init(initParams);
		channelManager.addInputChannel(channel, true);
		channelManager.addOutputChannel((IOutputChannel) channel);
		return channelManager;
	}

	@Override
	public IAgentIKSolver createAgentIKSolver()
	{
		return new NaoLegCalculator();
	}

	@Override
	public IDecisionMaker createDecisionMaker(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel,
			int playerNumber, String decisionMakerName, ParameterMap learningParam)
	{
		if (decisionMakerName.equals(IMagmaConstants.DEFAULT_DECISION_MAKER)) {
			return super.createDecisionMaker(behaviors, thoughtModel, playerNumber, decisionMakerName, learningParam);
		}

		return DecisionMakerConfigurationHelper.NAO_DECISION_MAKERS.get(decisionMakerName)
				.create(behaviors, thoughtModel);
	}

	@Override
	protected String getBehaviorFilesBasePath()
	{
		return "behaviors/nao/";
	}

	@Override
	protected void createSpecificBehaviors(
			IRoboCupThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors)
	{
		// General behaviors
		behaviors.put(new GetReady(thoughtModel));
		behaviors.put(new MoveZero(thoughtModel));
		behaviors.put(new SwingArms(thoughtModel));
		behaviors.put(new SearchBall(thoughtModel, behaviors));
		behaviors.put(new FocusBall(thoughtModel, behaviors));
		behaviors.put(new FocusBallGoalie(thoughtModel));
		behaviors.put(new SendPassCommand(thoughtModel));

		// GetUp behaviors
		behaviors.put(new GetUpFromBack(thoughtModel, params));
		behaviors.put(new GetUpFromFront(thoughtModel, params));
		behaviors.put(new MoveArmsToFall(thoughtModel));
		behaviors.put(new FallBack(thoughtModel));
		behaviors.put(new FallForward(thoughtModel));
		behaviors.put(new FallSide(thoughtModel));

		// Movement behaviors
		IBaseWalk walkBehavior = createWalk(thoughtModel, params, behaviors);
		behaviors.put(new Walk(thoughtModel, behaviors, walkBehavior));

		behaviors.put(new WalkToPosition(thoughtModel, behaviors, createWalkEstimator()));
		behaviors.put(new IKStepWalkBehavior(thoughtModel, params, behaviors));

		IBaseWalk walkPathBehavior = new IKStepPlanBehavior(thoughtModel, params, behaviors);
		behaviors.put(walkPathBehavior);
		behaviors.put(new WalkPath(thoughtModel, params, behaviors, walkPathBehavior));

		// Positioning behaviors
		behaviors.put(new PassivePositioning(thoughtModel, behaviors));
		behaviors.put(new GoaliePositioning(thoughtModel, behaviors));

		// Kick behaviors
		IWalkEstimator walkEstimator = createWalkEstimator();
		final float opponentDistanceFar = 2.2f;
		float opponentDistanceMedium = 1.5f;

		behaviors.put(IKKickBehavior.getKickStabilizationLeft(KICK_8M.KICK.BASE_NAME, KICK_8M.STABILIZE, thoughtModel,
				params, opponentDistanceMedium, Kick8m.MAX_KICK_DISTANCE, walkEstimator));
		behaviors.put(IKKickBehavior.getKickStabilizationRight(KICK_8M.KICK.BASE_NAME, KICK_8M.STABILIZE, thoughtModel,
				params, opponentDistanceMedium, Kick8m.MAX_KICK_DISTANCE, walkEstimator));

		behaviors.put(IKKickBehavior.getKickStabilizationLeft(KICK_11M.KICK.BASE_NAME, KICK_11M.STABILIZE, thoughtModel,
				params, opponentDistanceFar, Kick11m.MAX_KICK_DISTANCE, walkEstimator));
		behaviors.put(IKKickBehavior.getKickStabilizationRight(KICK_11M.KICK.BASE_NAME, KICK_11M.STABILIZE,
				thoughtModel, params, opponentDistanceFar, Kick11m.MAX_KICK_DISTANCE, walkEstimator));

		behaviors.put(new Kick8m(Side.LEFT, thoughtModel, params));
		behaviors.put(new Kick8m(Side.RIGHT, thoughtModel, params));
		behaviors.put(new Kick11m(Side.LEFT, thoughtModel, params));
		behaviors.put(new Kick11m(Side.RIGHT, thoughtModel, params));

		behaviors.put(KickWalk.instance(Side.LEFT, thoughtModel, params, walkEstimator));
		behaviors.put(KickWalk.instance(Side.RIGHT, thoughtModel, params, walkEstimator));
		behaviors.put(
				KickWalkStanding.instance(Side.LEFT, thoughtModel, params, opponentDistanceMedium, walkEstimator));
		behaviors.put(
				KickWalkStanding.instance(Side.RIGHT, thoughtModel, params, opponentDistanceMedium, walkEstimator));
		behaviors.put(KickWalkSide.instance(Side.LEFT, thoughtModel, params, opponentDistanceMedium, walkEstimator));
		behaviors.put(KickWalkSide.instance(Side.RIGHT, thoughtModel, params, opponentDistanceMedium, walkEstimator));
		behaviors.put(
				KickWalkBackward.instance(Side.LEFT, thoughtModel, params, opponentDistanceMedium, walkEstimator));
		behaviors.put(
				KickWalkBackward.instance(Side.RIGHT, thoughtModel, params, opponentDistanceMedium, walkEstimator));

		opponentDistanceMedium -= 1;

		// Keep behaviors
		behaviors.put(new KeepSide(Side.LEFT, thoughtModel));
		behaviors.put(new KeepSide(Side.RIGHT, thoughtModel));
		behaviors.put(new KeepCenter(thoughtModel));

		// Other behaviors
		behaviors.put(Dribbling.getLeftVersion(
				DRIBBLE.LEFT, thoughtModel, behaviors, opponentDistanceMedium, Angle.ZERO, walkEstimator));
		behaviors.put(Dribbling.getRightVersion(
				DRIBBLE.RIGHT, thoughtModel, behaviors, opponentDistanceMedium, Angle.ZERO, walkEstimator));

		for (int i = 30; i <= 120; i += 30) {
			behaviors.put(Dribbling.getLeftVersion(
					DRIBBLE.LEFT + i, thoughtModel, behaviors, opponentDistanceMedium, Angle.deg(i), walkEstimator));
			behaviors.put(Dribbling.getRightVersion(
					DRIBBLE.RIGHT + i, thoughtModel, behaviors, opponentDistanceMedium, Angle.deg(i), walkEstimator));
			behaviors.put(Dribbling.getLeftVersion(DRIBBLE.LEFT + (-i), thoughtModel, behaviors, opponentDistanceMedium,
					Angle.deg(-i), walkEstimator));
			behaviors.put(Dribbling.getRightVersion(DRIBBLE.RIGHT + (-i), thoughtModel, behaviors,
					opponentDistanceMedium, Angle.deg(-i), walkEstimator));
		}

		Consumer<StabilizedKickConstants> createStabilizedKick = constants ->
		{
			behaviors.put(new StabilizedKick(constants.FULL.LEFT, thoughtModel, params, behaviors,
					constants.STABILIZE.LEFT, constants.KICK.LEFT));
			behaviors.put(new StabilizedKick(constants.FULL.RIGHT, thoughtModel, params, behaviors,
					constants.STABILIZE.RIGHT, constants.KICK.RIGHT));
		};
		createStabilizedKick.accept(KICK_8M);
		createStabilizedKick.accept(KICK_11M);
		List<String> defaultAvailableKicks = createDefaultAvailableKicks(behaviors);
		if (UglyConstants.thinClient) {
			behaviors.put(new AttackThin(thoughtModel, behaviors, defaultAvailableKicks));
		} else {
			behaviors.put(new Attack(thoughtModel, behaviors, defaultAvailableKicks));
		}

		behaviors.put(new KickChallengeAttack(thoughtModel, behaviors, defaultAvailableKicks));

		List<String> passingKicks = new ArrayList<>();
		addKick(behaviors, passingKicks, IBehaviorConstants.KICK_8M.FULL);
		behaviors.put(new PassingChallengeAttack(thoughtModel, behaviors, passingKicks));

		// Experimental behaviors
		behaviors.put(new SimpleIKMovementBehavior(thoughtModel, params));
		behaviors.put(IKStabilizeOnLegBehavior.getStabilizeOnLeftLeg(thoughtModel, params));
		behaviors.put(IKStabilizeOnLegBehavior.getStabilizeOnRightLeg(thoughtModel, params));
	}

	protected IBaseWalk createWalk(IRoboCupThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors)
	{
		return (IBaseWalk) behaviors.put(new IKWalkBehavior(thoughtModel, params, behaviors));
	}

	@Override
	protected ParameterMap createSpecificParameters()
	{
		ParameterMap result = new ParameterMap();

		result.put(IK_WALK_STEP, new IKWalkMovementParametersBase());
		result.put(IK_WALK, new IKWalkMovementParametersBase());
		result.put(IK_STEP_PLAN, new IKWalkMovementParametersBase());
		result.put(IK_MOVEMENT, new IKWalkMovementParametersBase());

		result.put(STABILIZE.BASE_NAME, new IKWalkMovementParametersBase());

		result.put(KICK_8M.KICK.BASE_NAME, new Kick8mParameters());
		result.put(KICK_8M.STABILIZE.BASE_NAME, new IKWalkMovementParametersBase());

		result.put(KICK_11M.KICK.BASE_NAME, new Kick11mParameters());
		result.put(KICK_11M.STABILIZE.BASE_NAME, new IKWalkMovementParametersBase());

		result.put(KICK_WALK.BASE_NAME, ParameterListComposite.fromSingle(KickWalkParameters.instance()));
		result.put(
				KICK_WALK_STANDING.BASE_NAME, ParameterListComposite.fromSingle(KickWalkStandingParameters.instance()));

		// result.put(KICK_WALK_STANDING.BASE_NAME, new KickWalkStandingGridParameters());
		// result.put(KICK_WALK.BASE_NAME, new KickWalkStandingGridParameters());

		result.put(KICK_WALK_SIDE.BASE_NAME, ParameterListComposite.fromSingle(KickWalkSideParameters.instance()));
		result.put(
				KICK_WALK_BACKWARD.BASE_NAME, ParameterListComposite.fromSingle(KickWalkBackwardParameters.instance()));

		result.put(GET_UP_BACK, new GetUpFromBackParameters());
		result.put(GET_UP_FRONT, new GetUpFromFrontParameters());

		return result;
	}

	@Override
	public IWalkEstimator createWalkEstimator()
	{
		float[] speeds = {0.8f, 0.7f, 0.5f, 0.5f, 0.65f, 0.65f, 0.65f, 0.65f, 180};
		return new WalkEstimator(speeds);
	}

	@Override
	public void loadProperties(int playerNumber)
	{
		PropertyManager.load("/properties/common.properties", "/properties/nao/nao.properties");
	}
}
