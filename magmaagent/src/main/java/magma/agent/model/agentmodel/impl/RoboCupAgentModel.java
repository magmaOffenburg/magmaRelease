/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.agentmodel.impl;

import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.ISensorConfiguration;
import hso.autonomy.agent.model.agentmodel.ISensorFactory;
import hso.autonomy.agent.model.agentmodel.impl.AgentModel;
import hso.autonomy.agent.model.agentmodel.impl.BodyModel;
import hso.autonomy.agent.model.agentmodel.impl.ForceResistance;
import hso.autonomy.agent.model.agentmodel.impl.ik.IAgentIKSolver;
import hso.autonomy.util.geometry.IPose2D;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import magma.agent.IHumanoidConstants;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.agentmodel.SupportFoot;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Implementation of the RoboCup specific part of AgentModel. Used to represent all the information the
 * agent has about itself.
 *
 * @author Klaus Dorer
 */
public class RoboCupAgentModel extends AgentModel implements IRoboCupAgentModel
{
	/** the static pivot point used in static movement definitions */
	protected final Vector3D staticPivotPoint;

	/**
	 * initializes all known Sensors like: HingeJoints, ForceResistances, GyroRates, etc.
	 */
	public RoboCupAgentModel(IRoboCupAgentMetaModel metaModel, ISensorFactory sensorFactory, IAgentIKSolver ikSolver)
	{
		super(metaModel, sensorFactory, ikSolver);
		staticPivotPoint = metaModel.getStaticPivotPoint();
	}

	@Override
	public void beamToPosition(IPose2D pose)
	{
		((RoboCupBodyModel) bodyModelFuture).beamToPosition(pose);
	}

	@Override
	public void sayMessage(String message)
	{
		((RoboCupBodyModel) bodyModelFuture).sayMessage(message);
	}

	@Override
	public void sendPassCommand()
	{
		((RoboCupBodyModel) bodyModelFuture).sendPassCommand();
	}

	@Override
	public Vector3D getStaticPivotPoint()
	{
		if (staticPivotPoint == null) {
			return Vector3D.ZERO;
		}

		return staticPivotPoint;
	}

	/**
	 * Factory method
	 * @param sourceModel the source from which to create the new body model
	 * @return the specific body model created
	 */
	@Override
	protected BodyModel createBodyModel(BodyModel sourceModel)
	{
		return new RoboCupBodyModel((RoboCupBodyModel) sourceModel);
	}

	/**
	 * Factory method
	 * @param metaModel the agent configuration meta model
	 * @param ikSolver an inverse kinematic solver if required
	 * @param sensorFactory factory for creating sensor instances
	 * @return the specific body model created
	 */
	@Override
	protected BodyModel createBodyModel(
			IAgentMetaModel metaModel, ISensorFactory sensorFactory, IAgentIKSolver ikSolver)
	{
		return new RoboCupBodyModel(metaModel, sensorFactory, ikSolver);
	}

	@Override
	protected IRoboCupAgentMetaModel getMetaModel()
	{
		return (IRoboCupAgentMetaModel) super.getMetaModel();
	}

	@Override
	public boolean hasFootForceSensors()
	{
		return getMetaModel().hasFootForceSensors();
	}

	@Override
	public SupportFoot getStepFoot(int previousCyclesWithoutForce, int cyclesWithForce)
	{
		if (!hasFootForceSensors()) {
			return SupportFoot.NONE;
		}

		boolean standingOnLeftFoot =
				hasNewForce(IHumanoidConstants.LFootForce, previousCyclesWithoutForce, cyclesWithForce);
		boolean standingOnRightFoot =
				hasNewForce(IHumanoidConstants.RFootForce, previousCyclesWithoutForce, cyclesWithForce);

		if (standingOnLeftFoot && standingOnRightFoot) {
			return SupportFoot.BOTH;
		} else if (standingOnLeftFoot) {
			return SupportFoot.LEFT;
		} else if (standingOnRightFoot) {
			return SupportFoot.RIGHT;
		} else {
			return SupportFoot.NONE;
		}
	}

	private boolean hasNewForce(String sensorName, int previousCyclesWithoutForce, int cyclesWithForce)
	{
		ForceResistance sensor = getForceResistance(sensorName);
		return sensor.getForce().getNorm() >= 0.01 &&
				sensor.getPreviousCyclesWithoutForce() >= previousCyclesWithoutForce &&
				sensor.getCyclesWithForce() <= cyclesWithForce;
	}

	@Override
	public float getSoccerPositionKneeAngle()
	{
		return getMetaModel().getSoccerPositionKneeAngle();
	}

	@Override
	public float getSoccerPositionHipAngle()
	{
		return getMetaModel().getSoccerPositionHipAngle();
	}

	@Override
	public float getCycleTime()
	{
		return getMetaModel().getCycleTime();
	}

	@Override
	public int getGoalPredictionTime()
	{
		return getMetaModel().getGoalPredictionTime();
	}

	@Override
	public float getHeight()
	{
		return getMetaModel().getHeight();
	}

	@Override
	public float getTorsoZUpright()
	{
		return getMetaModel().getTorsoZUpright();
	}

	@Override
	public String getModelName()
	{
		return getMetaModel().getName();
	}

	@Override
	public List<String> getJointNames()
	{
		return getMetaModel()
				.getBodyPartConfigurations()
				.stream()
				.map(IBodyPartConfiguration::getJointConfiguration)
				.filter(Objects::nonNull)
				.map(ISensorConfiguration::getName)
				.collect(Collectors.toList());
	}

	@Override
	public boolean isMirrorJoint(String jointName)
	{
		if (jointName.contains("Roll")) {
			return true;
		}

		if (jointName.contains("ShoulderYaw")) {
			return true;
		}

		if (jointName.contains("ArmYaw")) {
			return true;
		}

		return false;
	}
}
