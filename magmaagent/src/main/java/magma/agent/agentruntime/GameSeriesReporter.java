/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.agentruntime;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.util.file.SerializationUtil;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import magma.common.spark.IConnectionConstants;
import magma.common.spark.PlaySide;
import magma.util.logging.AgentData;
import magma.util.logging.BehaviorData;

public class GameSeriesReporter
{
	private final BehaviorMap behaviors;

	private final PlaySide playSide;

	private final int totalCycles;

	public GameSeriesReporter(BehaviorMap behaviors, PlaySide playSide, int totalCycles)
	{
		this.behaviors = behaviors;
		this.playSide = playSide;
		this.totalCycles = totalCycles;
	}

	public void report()
	{
		try (DatagramSocket socket = new DatagramSocket()) {
			InetAddress address = InetAddress.getByName("localhost");
			byte[] bytes = SerializationUtil.convertToBytes(collectStats());
			socket.send(new DatagramPacket(bytes, bytes.length, address, IConnectionConstants.GAME_SERIES_PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private AgentData collectStats()
	{
		List<BehaviorData> stats = new ArrayList<>();
		for (IBehavior behavior : behaviors.getMap().values()) {
			if (behavior.getPerforms() == 0) {
				continue;
			}
			int cycles = behavior.getPerformedCycles();
			stats.add(
					new BehaviorData(behavior.getName(), behavior.getPerforms(), ((float) cycles / totalCycles) * 100));
		}
		return new AgentData(playSide, stats);
	}
}
