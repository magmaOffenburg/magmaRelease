/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior;

import hso.autonomy.agent.decision.behavior.IBehavior;

public interface IKick extends IBehavior {
	IKickDecider getKickDecider();
}
