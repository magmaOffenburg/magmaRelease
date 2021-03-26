/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import magma.agent.model.worldmeta.impl.SayMessage;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.Test;

public class SayMessageTest
{
	@Test
	public void simple()
	{
		int ownID = 1;
		Vector3D ownPos = new Vector3D(1.0, 2.0, 0.0);
		Vector3D opponentPos = new Vector3D(-5.0, -8.0, 0.0);

		executeTest(ownID, ownPos, opponentPos);
	}

	@Test
	public void decimals()
	{
		int ownID = 7;
		Vector3D ownPos = new Vector3D(14.12345, 6.789, 0.0);
		Vector3D opponentPos = new Vector3D(-2.3456789, 4.56789, 0.0);

		executeTest(ownID, ownPos, opponentPos);
	}

	@Test
	public void zeroPositions()
	{
		int ownID = 11;
		Vector3D ownPos = new Vector3D(0.0, 0.0, 0.0);
		Vector3D opponentPos = new Vector3D(0.0, 0.0, 0.0);

		executeTest(ownID, ownPos, opponentPos);
	}

	@Test
	public void nullPositions()
	{
		SayMessage message = new SayMessage();
		message.setTeammateID(9);
		message.setTeammatePosition(null);
		message.setOpponentPosition(null);
		String encodedMessage = message.encode();

		SayMessage decodedMessage = new SayMessage(encodedMessage);

		assertEquals(9, decodedMessage.getTeammateID());
		assertNull(decodedMessage.getTeammatePosition());
		assertNull(decodedMessage.getOpponentPosition());
	}

	@Test
	public void random()
	{
		for (int i = 0; i < 10; i++) {
			int ownID = (int) (Math.random() * (11 - 1) + 1);
			Vector3D ownPos = new Vector3D((Math.random() - 0.5) * 30, (Math.random() - 0.5) * 20, 0);
			Vector3D opponentPos = new Vector3D((Math.random() - 0.5) * 30, (Math.random() - 0.5) * 20, 0);

			executeTest(ownID, ownPos, opponentPos);
		}
	}

	private void executeTest(int ownID, Vector3D ownPos, Vector3D opponentPos)
	{
		SayMessage message = new SayMessage();
		message.setTeammateID(ownID);
		message.setTeammatePosition(ownPos);
		message.setOpponentPosition(opponentPos);
		String encodedMessage = message.encode();

		SayMessage decodedMessage = new SayMessage(encodedMessage);

		int decOwnID = decodedMessage.getTeammateID();
		Vector3D decPlayerPos = decodedMessage.getTeammatePosition();
		Vector3D decOpponentPos = decodedMessage.getOpponentPosition();

		assertEquals(ownID, decOwnID);
		assertEquals(0, ownPos.distance(decPlayerPos), 0.2);
		assertEquals(0, opponentPos.distance(decOpponentPos), 0.2);
	}
}
