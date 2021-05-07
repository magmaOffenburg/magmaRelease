package magma.agent.model.agentmodel;

import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

public interface IRoboCupAgentModelThin {
	void sendDashCommand(float[] values);

	void sendKickCommand(float[] values);
}
