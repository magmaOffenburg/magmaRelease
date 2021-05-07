package magma.agent.model.agentmodel.impl;

import hso.autonomy.agent.communication.action.IAction;
import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import hso.autonomy.agent.model.agentmodel.ISensorFactory;
import hso.autonomy.agent.model.agentmodel.impl.ik.IAgentIKSolver;
import magma.agent.communication.action.impl.PassEffector;
import magma.agent.communication.action.impl.ProxyEffector;

public class RoboCupBodyModelThin extends RoboCupBodyModel
{
	private float[] dashValues;

	private float[] kickValues;

	RoboCupBodyModelThin(IAgentMetaModel metaModel, ISensorFactory sensorFactory, IAgentIKSolver ikSolver)
	{
		super(metaModel, sensorFactory, ikSolver);
	}

	RoboCupBodyModelThin(RoboCupBodyModel source)
	{
		super(source);
	}

	protected void generateTargetStateToAction(IAction action)
	{
		super.generateTargetStateToAction(action);

		generateDashAction(action);

		generateKickAction(action);
	}

	void generatePassAction(IAction action)
	{
		if (sendPassCommand) {
			action.put(new PassEffector());
		}
	}

	void generateDashAction(IAction action)
	{
		if (dashValues != null) {
			action.put(new ProxyEffector("dash", dashValues));
		}
	}

	void sendDashCommand(float[] values)
	{
		dashValues = values;
	}

	void generateKickAction(IAction action)
	{
		if (kickValues != null) {
			action.put(new ProxyEffector("kick", kickValues));
		}
	}

	void sendKickCommand(float[] values)
	{
		kickValues = values;
	}
}
