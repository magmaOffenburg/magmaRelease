/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.fullsearch.jointspacewalk;

import com.google.gson.reflect.TypeToken;
import hso.autonomy.util.misc.GsonUtil;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import magma.robots.nao.decision.behavior.movement.fullsearch.FullSearchMovementParameters;

@SuppressWarnings({"UnnecessaryLocalVariable", "unused"})
public class JointSpaceWalkParameters extends FullSearchMovementParameters
{
	public enum WalkDirection {
		FORWARD,
		BACKWARD,
		SIDEWARD,
		TURN,
		TURN_NO_MIRROR;

		public boolean isTurn()
		{
			return this == WalkDirection.TURN || this == WalkDirection.TURN_NO_MIRROR;
		}
	}

	public enum CustomParam { INITIAL_WALK, INITIAL_WALK_SPEED, DIRECTION }

	class JsonParameters
	{
		public String description;
		public double[] params;
	}

	public JointSpaceWalkParameters()
	{
		this(WALK_ALL_JOINTS_PENALIZE_MAX_Y2);
	}

	public JointSpaceWalkParameters(String name)
	{
		super(getParamArray(name), CustomParam.values());
	}

	public float get(CustomParam param)
	{
		return get(param.name());
	}

	public WalkDirection getDirection()
	{
		return WalkDirection.values()[(int) get(CustomParam.DIRECTION)];
	}

	public boolean hasInitialWalk()
	{
		return get(CustomParam.INITIAL_WALK) == 1;
	}

	/** 2.776 **/
	static final String TURN2 = "turn2";
	/** 5.957 **/
	static final String WALK_ALL_JOINTS = "walkAllJoints";
	/** 2.116 **/
	static final String WALK_BACK_WITH_ARMS = "walkBackWithArms";
	/** 3.898 **/
	static final String WALK128_TOE = "walk128Toe";
	/** 5.596 **/
	static final String WALK_ALL_JOINTS_PENALIZE_MAX_Y3 = "walkAllJointsPenalizeMaxY3";
	/** 5.477 **/
	static final String WALK_ALL_JOINTS_FROM_STANDING = "walkAllJointsFromStanding";
	/** 3.378 **/
	static final String WALK129 = "walk129";
	/** 8.057 **/
	static final String WALK_WITH_ARMS = "walkWithArms";
	/** 6.771 **/
	static final String WALK_ALL_JOINTS_FROM_RUNNING = "walkAllJointsFromRunning";
	/** 2.899 **/
	static final String WALK_PITCH_JOINTS_PENALIZE_MAX_Y = "walkPitchJointsPenalizeMaxY";
	/** 3.014 **/
	static final String WALK_ALL_JOINTS_NAO_ = "walkAllJointsNao_";
	/** 3.484 **/
	static final String WALK129_TOE = "walk129Toe";
	/** 3.405 **/
	static final String WALK_ALL_JOINTS_PENALIZE_MAX_Y = "walkAllJointsPenalizeMaxY";
	/** 4.883 **/
	static final String WALK128 = "walk128";
	/** 7.466 **/
	static final String WALK_ALL_JOINTS_NEW = "walkAllJointsNew";
	/** 7.639 **/
	static final String WALK_BACK_ALL_JOINTS = "walkBackAllJoints";
	/** 2.629 **/
	static final String WALK_SIDE_ALL_JOINTS_ = "walkSideAllJoints_";
	/** 0.350 **/
	static final String WALK_SIDE_ALL_JOINTS2 = "walkSideAllJoints2";
	/** 11.171 **/
	static final String TURN_NO_MIRROR = "turnNoMirror";
	/** 4.212 **/
	static final String TURN_NO_MIRROR_400 = "turnNoMirror400";
	/** 6.861 **/
	static final String WALK_ALL_JOINTS_NAO2 = "walkAllJointsNao2";
	/** 6.288 **/
	static final String WALK_ALL_JOINTS_PENALIZE_MAX_Y2 = "walkAllJointsPenalizeMaxY2";
	/** 11.677 **/
	static final String WALK_ALL_JOINTS_NEW_CMAES = "walkAllJointsNewCMAES";
	/** 2.708 **/
	static final String TURN = "turn";
	/** 4.017 **/
	static final String WALK_BACKWARD = "walkBackward";
	/** 6.094 **/
	static final String WALK_ALL_JOINTS_O_S20 = "walkAllJointsOS20";
	/** 4.786 **/
	static final String WALK_WITH_ARMS_PENALIZE_MAX_Y = "walkWithArmsPenalizeMaxY";
	/** 4.157 **/
	static final String WALK_FROM_STANDING = "walkFromStanding";
	/** 2.398 **/
	static final String WALK_PITCH_JOINTS_PENALIZE_MAX_Y2 = "walkPitchJointsPenalizeMaxY2";
	/** 7.303 **/
	static final String WALK_BACK_ALL_JOINTS_CMAES = "walkBackAllJointsCMAES";
	/** 6.145 **/
	static final String WALK_ALL_JOINTS_PENALIZE_MAX_Y400 = "walkAllJointsPenalizeMaxY400";
	/** 1.296 **/
	static final String WALK_BACK_ALL_JOINTS_PENALIZE_MAX_Y = "walkBackAllJointsPenalizeMaxY";

	static final String WALK_ALL_JOINTS_PENALIZE_MAX_Y400_0 = "walkAllJointsPenalizeMaxY400_0";
	static final String WALK_ALL_JOINTS_PENALIZE_MAX_Y400_50 = "walkAllJointsPenalizeMaxY400_50";
	static final String WALK_ALL_JOINTS_PENALIZE_MAX_Y400_100 = "walkAllJointsPenalizeMaxY400_100";
	static final String WALK_ALL_JOINTS_PENALIZE_MAX_Y400_150 = "walkAllJointsPenalizeMaxY400_150";
	static final String WALK_ALL_JOINTS_PENALIZE_MAX_Y400_200 = "walkAllJointsPenalizeMaxY400_200";
	static final String WALK_ALL_JOINTS_PENALIZE_MAX_Y400_250 = "walkAllJointsPenalizeMaxY400_250";
	static final String WALK_ALL_JOINTS_PENALIZE_MAX_Y400_300 = "walkAllJointsPenalizeMaxY400_300";

	private static double[] getParamArray(String name)
	{
		Type type = new TypeToken<Map<String, JsonParameters>>() {}.getType();
		String json =
				new Scanner(Objects.requireNonNull(JointSpaceWalkParameters.class.getClassLoader().getResourceAsStream(
									"behaviors/nao/JointSpaceWalk.json")),
						"UTF-8")
						.useDelimiter("\\A")
						.next();
		Map<String, JsonParameters> data = GsonUtil.fromJson(json, type);
		return Objects.requireNonNull(data).get(name).params;
	}
}
