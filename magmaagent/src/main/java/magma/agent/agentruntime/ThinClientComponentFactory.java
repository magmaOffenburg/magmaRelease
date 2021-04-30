package magma.agent.agentruntime;

import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.agentmodel.impl.RoboCupAgentModelThin;

public class ThinClientComponentFactory extends ComponentFactoryDecorator
{
	public ThinClientComponentFactory(ComponentFactory decoratee)
	{
		super(decoratee);
	}

	public IRoboCupAgentModel createAgentModel(IRoboCupAgentMetaModel metaModel)
	{
		return new RoboCupAgentModelThin(metaModel, createSensorFactory(), createAgentIKSolver());
	}
}
