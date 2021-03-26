/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.channel.impl;

import hso.autonomy.agent.communication.action.IEffectorMap;
import hso.autonomy.agent.communication.channel.IChannel;
import hso.autonomy.agent.communication.channel.IChannelManager;
import hso.autonomy.agent.communication.channel.IInputChannel;
import hso.autonomy.agent.communication.channel.IOutputChannel;
import hso.autonomy.agent.communication.perception.IPerceptionLogger;
import hso.autonomy.agent.communication.perception.IPerceptorMap;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kdorer
 */
public class ChannelManager implements IChannelManager
{
	/** the container for incoming perceptions */
	private List<IPerceptorMap> perceptors;

	/** the single main channel */
	private IInputChannel mainChannel;

	/** channels for receiving sensor information */
	protected List<IInputChannel> inChannels;

	/** channels for sending sensor information */
	protected List<IOutputChannel> outChannels;

	private ChannelManagerStatus status;

	private boolean hadConnection;

	/** logs all perceptions into a logfile */
	protected IPerceptionLogger perceptionLogger;

	public ChannelManager()
	{
		perceptors = new ArrayList<>();
		this.inChannels = new ArrayList<>();
		this.outChannels = new ArrayList<>();
		status = ChannelManagerStatus.CREATED;
	}

	@Override
	public void addPerceptors(IPerceptorMap newPerceptors)
	{
		if (newPerceptors == null) {
			return;
		}

		synchronized (perceptors)
		{
			perceptors.add(newPerceptors);
		}

		// outside the synchronized block we trigger the updates
		synchronized (this)
		{
			notify();
		}
	}

	@Override
	public IPerceptorMap getNextPerceptorMap()
	{
		synchronized (perceptors)
		{
			if (perceptors.isEmpty()) {
				return null;
			}
			return perceptors.remove(0);
		}
	}

	@Override
	public void performAction(IEffectorMap effectors)
	{
		for (IOutputChannel outChannel : outChannels) {
			outChannel.sendMessage(effectors);
		}
	}

	@Override
	public void addInputChannel(IInputChannel channel, boolean isMainChannel)
	{
		inChannels.add(channel);
		if (isMainChannel) {
			mainChannel = channel;
		}
	}

	@Override
	public void addOutputChannel(IOutputChannel channel)
	{
		outChannels.add(channel);
	}

	@Override
	public boolean start()
	{
		boolean result = true;
		for (IInputChannel in : inChannels) {
			boolean connected = in.startChannel();
			if (!connected && in == mainChannel) {
				stop();
				// start is only unsuccessful if the main channel doesn't work
				return false;
			}
		}
		for (IOutputChannel out : outChannels) {
			result &= out.startChannel();
		}
		status = ChannelManagerStatus.STARTED;
		hadConnection = true;
		return result;
	}

	@Override
	public void stop()
	{
		inChannels.forEach(IChannel::stopChannel);
		outChannels.forEach(IChannel::stopChannel);

		synchronized (this)
		{
			notify();
		}

		status = ChannelManagerStatus.STOPPED;
	}

	@Override
	public boolean isConnected()
	{
		return mainChannel != null && mainChannel.isConnected();
	}

	@Override
	public void lostConnection(IInputChannel channel)
	{
		if (mainChannel == channel) {
			// we have to inform that a main connection is lost
			status = ChannelManagerStatus.LOST_MAIN_CONNECTION;
			synchronized (this)
			{
				notify();
			}
			stop();
			status = ChannelManagerStatus.LOST_MAIN_CONNECTION;
		}
	}

	@Override
	public ChannelManagerStatus getStatus()
	{
		return status;
	}

	@Override
	public String toString()
	{
		if (mainChannel != null) {
			return mainChannel.getConnectionState().toString();
		}
		return status.toString();
	}

	@Override
	public boolean hadConnection()
	{
		return hadConnection;
	}

	@Override
	public IInputChannel getMainChannel()
	{
		return mainChannel;
	}

	@Override
	public IPerceptionLogger getPerceptionLogger()
	{
		return perceptionLogger;
	}
}
