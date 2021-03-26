/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.impl.VisibleObjectPerceptor;
import hso.autonomy.util.geometry.VectorUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import magma.agent.communication.perception.IPlayerPos;
import magma.agent.model.worldmodel.IPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Represents an agent on the field (position, player number, team name, body parts if applicable)
 *
 * @author Simon Raffeiner
 */
public class PlayerPos extends VisibleObjectPerceptor implements IPlayerPos
{
	public static final String PLAYER_POS = "playerPos";

	private final int id;

	private final Map<String, Vector3D> bodyPartMap;

	private final String teamname;

	/**
	 * Assignment constructor
	 *
	 * @param type the type information
	 * @param position Player position
	 * @param id Player ID
	 * @param teamname Team name
	 * @param bodyPartMap List of visible body parts
	 * @param hasDepth true, if position contains depth information, false otherwise
	 * @param name the unique name of the player
	 */
	public PlayerPos(String type, Vector3D position, int id, String teamname, Map<String, Vector3D> bodyPartMap,
			boolean hasDepth, String name)
	{
		super(type, position, hasDepth, 1.0, name);
		this.id = id;
		this.teamname = teamname;
		this.bodyPartMap = bodyPartMap;
		if (bodyPartMap != null && bodyPartMap.size() > 1) {
			// when seeing multiple body parts we average the position
			Vector3D result = Vector3D.ZERO;
			for (Vector3D part : bodyPartMap.values()) {
				result = result.add(part);
			}
			result = new Vector3D(1.0d / bodyPartMap.size(), result);
			setPosition(result);
		}
	}

	@Override
	public int getId()
	{
		return id;
	}

	@Override
	public String getTeamname()
	{
		return teamname;
	}

	@Override
	public Map<String, Vector3D> getAllBodyParts()
	{
		return this.bodyPartMap;
	}

	/**
	 * Set the position of a given body part, add it to the list if it didn't
	 * exist before
	 *
	 * @param partName Body part name
	 * @param position New position
	 */
	public void setBodyPartPosition(String partName, Vector3D position)
	{
		this.bodyPartMap.put(partName, position);
	}

	@Override
	public Vector3D getBodyPartPosition(String partName)
	{
		return this.bodyPartMap.get(partName);
	}

	@Override
	public void averagePosition(List<IPlayer> playersPos)
	{
		List<Vector3D> positions = new ArrayList<>();
		positions.add(getPosition());
		for (IPlayer curPlayerPos : playersPos) {
			positions.add(curPlayerPos.getPosition());
		}

		setPosition(VectorUtils.average(positions));
	}
}
