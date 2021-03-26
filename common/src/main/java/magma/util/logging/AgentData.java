/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.logging;

import java.io.Serializable;
import java.util.List;
import magma.common.spark.PlaySide;

public class AgentData implements Serializable
{
	public static final long serialVersionUID = 4290507372474569249L;

	public final PlaySide playSide;

	public final List<BehaviorData> behaviors;

	public AgentData(PlaySide playSide, List<BehaviorData> behaviors)
	{
		this.playSide = playSide;
		this.behaviors = behaviors;
	}
}
