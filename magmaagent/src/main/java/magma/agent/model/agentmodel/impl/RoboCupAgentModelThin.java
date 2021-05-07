package magma.agent.model.agentmodel.impl;

import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import hso.autonomy.agent.model.agentmodel.ISensorFactory;
import hso.autonomy.agent.model.agentmodel.impl.BodyModel;
import hso.autonomy.agent.model.agentmodel.impl.ik.IAgentIKSolver;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.agent.model.agentmodel.IRoboCupAgentModelThin;

public class RoboCupAgentModelThin extends RoboCupAgentModel implements IRoboCupAgentModelThin
{
	/**
	 * initializes all known Sensors like: HingeJoints, ForceResistances, GyroRates, etc.
	 *
	 * @param metaModel
	 * @param sensorFactory
	 * @param ikSolver
	 */
	public RoboCupAgentModelThin(
			IRoboCupAgentMetaModel metaModel, ISensorFactory sensorFactory, IAgentIKSolver ikSolver)
	{
		super(metaModel, sensorFactory, ikSolver);
	}

	@Override
	protected BodyModel createBodyModel(BodyModel sourceModel)
	{
		return new RoboCupBodyModelThin((RoboCupBodyModel) sourceModel);
	}

	@Override
	protected BodyModel createBodyModel(
			IAgentMetaModel metaModel, ISensorFactory sensorFactory, IAgentIKSolver ikSolver)
	{
		return new RoboCupBodyModelThin(metaModel, sensorFactory, ikSolver);
	}

	@Override
	public void sendDashCommand(float[] values)
	{
		((RoboCupBodyModelThin) bodyModelFuture).sendDashCommand(values);
	}

	@Override
	public void sendKickCommand(float[] values)
	{
		((RoboCupBodyModelThin) bodyModelFuture).sendKickCommand(values);
	}
}
