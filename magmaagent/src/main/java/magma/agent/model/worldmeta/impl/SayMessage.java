/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta.impl;

import kdo.util.misc.ValueUtil;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class SayMessage
{
	private int teammateID;

	private int teammatePositionX;

	private int teammatePositionY;

	private int opponentPositionX;

	private int opponentPositionY;

	public SayMessage()
	{
	}

	public SayMessage(String message)
	{
		decode(message);
	}

	public String encode()
	{
		return String.format("%02d%+04d%+04d%+04d%+04d", teammateID, teammatePositionX, teammatePositionY,
				opponentPositionX, opponentPositionY);
	}

	private void decode(String message)
	{
		this.teammateID = Integer.valueOf(message.substring(0, 2));

		try {
			this.teammatePositionX = Integer.valueOf(message.substring(2, 6));
			this.teammatePositionY = Integer.valueOf(message.substring(6, 10));
		} catch (NumberFormatException e) {
			this.teammatePositionX = 999;
			this.teammatePositionY = 999;
		}

		try {
			this.opponentPositionX = Integer.valueOf(message.substring(10, 14));
			this.opponentPositionY = Integer.valueOf(message.substring(14, 18));
		} catch (NumberFormatException e) {
			this.opponentPositionX = 999;
			this.opponentPositionX = 999;
		}
	}

	public int getTeammateID()
	{
		return this.teammateID;
	}

	public void setTeammateID(int teammateID)
	{
		this.teammateID = ValueUtil.limitValue(teammateID, 1, 11);
	}

	public Vector3D getTeammatePosition()
	{
		if (teammatePositionX != 999 && teammatePositionY != 999) {
			return new Vector3D(teammatePositionX / 10.0, teammatePositionY / 10.0, 0.0);
		} else {
			return null;
		}
	}

	public void setTeammatePosition(Vector3D teammatePosition)
	{
		if (teammatePosition != null) {
			this.teammatePositionX = ValueUtil.limitValue((int) (teammatePosition.getX() * 10.0), -999, 999);
			this.teammatePositionY = ValueUtil.limitValue((int) (teammatePosition.getY() * 10.0), -999, 999);
		} else {
			this.teammatePositionX = 999;
			this.teammatePositionY = 999;
		}
	}

	public Vector3D getOpponentPosition()
	{
		if (opponentPositionX != 999 && opponentPositionY != 999) {
			return new Vector3D(opponentPositionX / 10.0, opponentPositionY / 10.0, 0.0);
		} else {
			return null;
		}
	}

	public void setOpponentPosition(Vector3D opponentPosition)
	{
		if (opponentPosition != null) {
			this.opponentPositionX = ValueUtil.limitValue((int) (opponentPosition.getX() * 10.0), -999, 999);
			this.opponentPositionY = ValueUtil.limitValue((int) (opponentPosition.getY() * 10.0), -999, 999);
		} else {
			this.opponentPositionX = 999;
			this.opponentPositionX = 999;
		}
	}
}
