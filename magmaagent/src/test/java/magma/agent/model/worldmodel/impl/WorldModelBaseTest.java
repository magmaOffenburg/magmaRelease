/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel.impl;

import static org.mockito.Mockito.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hso.autonomy.agent.model.worldmodel.ILandmark;
import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import java.util.ArrayList;
import magma.agent.IMagmaConstants;
import magma.agent.model.worldmodel.IBall;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IThisPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Klaus Dorer
 */
public class WorldModelBaseTest
{
	protected IRoboCupWorldModel worldModelMock;

	protected IThisPlayer thisPlayerMock;

	protected IBall ballMock;

	protected IPlayer playerMock1;

	protected IPlayer playerMock2;

	protected IPlayer playerMock3;

	protected IPlayer playerMock4;

	@BeforeEach
	public void setUp()
	{
		playerMock1 = createPlayerMock(playerMock1, 1);
		playerMock2 = createPlayerMock(playerMock2, 2);
		playerMock3 = createPlayerMock(playerMock3, 3);
		playerMock4 = createPlayerMock(playerMock4, 4);
		ballMock = mock(IBall.class);
		thisPlayerMock = mock(IThisPlayer.class);
		when(thisPlayerMock.getID()).thenReturn(3);
		worldModelMock = mock(IRoboCupWorldModel.class);
		when(worldModelMock.getBall()).thenReturn(ballMock);
		when(worldModelMock.getThisPlayer()).thenReturn(thisPlayerMock);
		when(worldModelMock.getGlobalTime()).thenReturn(0.0f);
		when(worldModelMock.getServerVersion()).thenReturn(IMagmaConstants.DEFAULT_SERVER_VERSION);
		when(worldModelMock.getLandmarks()).thenReturn(new ArrayList<ILandmark>());
		when(worldModelMock.getGoalPostObstacles()).thenReturn(new ArrayList<IVisibleObject>());
	}

	/**
	 * Train a player mock (for unit testing only!)
	 *
	 * @param playerMock Player mock
	 * @param id Player id
	 * @return Trained mock
	 */
	IPlayer createPlayerMock(IPlayer playerMock, int id)
	{
		playerMock = mock(IPlayer.class);
		when(playerMock.isOwnTeam()).thenReturn(true);
		when(playerMock.getID()).thenReturn(id);
		when(playerMock.getAge(anyFloat())).thenReturn(0.0f);

		if (id == 1) {
			when(playerMock.isGoalie()).thenReturn(true);
		} else {
			when(playerMock.isGoalie()).thenReturn(false);
		}

		return playerMock;
	}

	@Test
	public void emptyDummyTestToKeepJenkinsHappy() throws Exception
	{
	}
}
