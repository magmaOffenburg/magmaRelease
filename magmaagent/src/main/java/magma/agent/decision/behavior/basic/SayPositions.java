/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.basic;

import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import hso.autonomy.agent.model.worldmodel.InformationSource;
import magma.agent.IMagmaConstants;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmeta.impl.SayMessage;
import magma.agent.model.worldmodel.IRoboCupWorldModel;

/**
 * Used for communication with the server
 *
 * @author Klaus Dorer
 */
public class SayPositions extends RoboCupBehavior
{
	public SayPositions(IRoboCupThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.SAY_POSITIONS, thoughtModel);
	}

	@Override
	public void perform()
	{
		super.perform();

		IRoboCupWorldModel worldModel = getWorldModel();
		int count = (int) (worldModel.getGlobalTime() * 100.f);
		// we may only say when it is our turn and only every 3 cycles and one
		// cycle is 20 ms
		if (count % 2 > 0) {
			// avoids a problem that server's global time sometimes is 30ms more
			count--;
		}
		int turn = ((count / 6) % IMagmaConstants.NUMBER_OF_PLAYERS_PER_TEAM) + 1;
		int ourID = worldModel.getThisPlayer().getID();
		if (turn != ourID || count % 6 != 0) {
			return;
		}

		SayMessage message = new SayMessage();
		message.setTeammateID(ourID);
		message.setTeammatePosition(worldModel.getThisPlayer().getPosition());

		// communicate opponent position (same id)
		IVisibleObject opponent = worldModel.getVisiblePlayer(ourID, false);
		if (opponent != null && opponent.getAge(worldModel.getGlobalTime()) < 0.1 &&
				opponent.getInformationSource() == InformationSource.VISION) {
			message.setOpponentPosition(opponent.getPosition());
		} else {
			message.setOpponentPosition(null);
		}

		getAgentModel().sayMessage(message.encode());
	}
}
