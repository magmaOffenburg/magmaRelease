/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.channel;

import hso.autonomy.agent.communication.channel.IInputChannel;
import java.util.List;

/**
 *
 * @author kdorer
 */
public interface IRoboCupChannel extends IInputChannel {
	void init(List<String> initParams);
}
