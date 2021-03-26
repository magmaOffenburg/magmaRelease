/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.scenegraph;

public interface IModelFiles {
	@Deprecated
	String DIR = "models/";

	@Deprecated
	String ENDING = ".obj";

	@Deprecated
	String NUMBERED = "[0-9]*[.]obj";

	String LEFT_GOAL = DIR + "leftgoal" + ENDING;

	String RIGHT_GOAL = DIR + "rightgoal" + ENDING;

	String NAO_SOCCER_FIELD = DIR + "naosoccerfield" + ENDING;

	String NEW_FIELD = DIR + "newfield" + ENDING;

	String SKYBOX = DIR + "skybox" + ENDING;

	String SOCCER_BALL = DIR + "soccerball" + ENDING;

	String NAO_HEAD = DIR + "naohead" + ENDING;

	String NAO_BODY = DIR + "naobody" + NUMBERED;

	String LEFT_UPPER_ARM = DIR + "lupperarm" + NUMBERED;

	String RIGHT_UPPER_ARM = DIR + "rupperarm" + NUMBERED;

	String LEFT_LOWER_ARM = DIR + "llowerarm" + ENDING;

	String RIGHT_LOWER_ARM = DIR + "rlowerarm" + ENDING;

	String LEFT_THIGH = DIR + "lthigh" + ENDING;

	String RIGHT_THIGH = DIR + "rthigh" + ENDING;

	String LEFT_SHANK = DIR + "lshank" + NUMBERED;

	String RIGHT_SHANK = DIR + "rshank" + NUMBERED;

	String LEFT_FOOT = DIR + "lfoot" + ENDING;

	String RIGHT_FOOT = DIR + "rfoot" + ENDING;
}
