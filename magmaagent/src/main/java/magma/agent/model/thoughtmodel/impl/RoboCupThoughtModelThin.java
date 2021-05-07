package magma.agent.model.thoughtmodel.impl;

import hso.autonomy.agent.communication.action.IAction;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.agentmodel.IRoboCupAgentModelThin;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModelThin;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.util.roboviz.RoboVizDraw;

public class RoboCupThoughtModelThin extends RoboCupThoughtModel implements IRoboCupThoughtModelThin
{
	/**
	 * dash parameters to send to the fat proxy
	 */
	private float[] dashParameters;

	/**
	 * flag to indicate if a new dash command needs to be executed/sent
	 */
	private boolean dashExecuted;

	/** kick parameters to send to the fat proxy */
	private float[] kickParameters;

	/** flag to indicate if a new kick command needs to be executed/sent */
	private boolean kickExecuted;

	public RoboCupThoughtModelThin(IAgentModel agentModel, IRoboCupWorldModel worldModel, RoboVizDraw roboVizDraw)
	{
		super(agentModel, worldModel, roboVizDraw);

		dashExecuted = false;
		dashParameters = new float[3];
		kickExecuted = false;
		kickParameters = new float[3];
	}

	@Override
	public void mapStateToAction(IAction action, boolean remoteControlled)
	{
		IRoboCupAgentModelThin agentModel = (IRoboCupAgentModelThin) getAgentModel();
		if (dashExecuted) {
			agentModel.sendDashCommand(dashParameters);
			dashExecuted = false;
		}
		if (kickExecuted) {
			agentModel.sendKickCommand(kickParameters);
			kickExecuted = false;
		}
		getAgentModel().reflectTargetStateToAction(action);
	}

	@Override
	public void setDash(float x, float y, float desiredTurn)
	{
		dashParameters[0] = x;
		dashParameters[1] = y;
		dashParameters[2] = desiredTurn;
		dashExecuted = true;
	}

	@Override
	public void setKick(float power, float hAngle, float vAngle)
	{
		kickParameters[0] = power;
		kickParameters[1] = hAngle;
		kickParameters[2] = vAngle;
		kickExecuted = true;
	}
}
