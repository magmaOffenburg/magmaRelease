/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Base class for all connections within a behavior network
 */
public abstract class Connection
{
	/** Parameters used during activation calculation */
	NetworkParams networkParams;

	/** Destination proposition to be connected */
	Proposition destinationProposition;

	/** Source proposition to be connected */
	Proposition sourceProposition;

	/** Activation that is passed through this connection */
	double activation;

	/**
	 * Default constructor. Sets activation to initial value.
	 */
	Connection()
	{
		activation = 0.0;
	}

	/**
	 * Constructor creating a connection by specifying the network parameters
	 * used to calculate the activation passed by this connection and the source
	 * and destination propositions of the connection.
	 * @param params the network parameters controlling runtime dynamics
	 * @param source the source proposition
	 * @param destination the destination proposition
	 */
	Connection(NetworkParams params, Proposition source, Proposition destination)
	{
		if (source == null) {
			throw new RuntimeException("Invalid source when creating a connection: null");
		}

		if (destination == null) {
			throw new RuntimeException("Invalid destination when creating a connection: null");
		}

		sourceProposition = source;
		destinationProposition = destination;
		activation = 0.0;
		networkParams = params;
	}

	/**
	 * @return the activation that is passed through this connection
	 */
	public double getActivation()
	{
		return activation;
	}

	/**
	 * @return the module that is the source of activation
	 */
	public NetworkNode getSourceModule()
	{
		return sourceProposition.getContainingNode();
	}

	/**
	 * @return the module that receives the activation
	 */
	public NetworkNode getDestinationModule()
	{
		return destinationProposition.getContainingNode();
	}

	/**
	 * @return the source proposition of the source module
	 */
	public Proposition getSourceProposition()
	{
		return sourceProposition;
	}

	/**
	 * @return the destination proposition of the destination module
	 */
	public Proposition getDestinationProposition()
	{
		return destinationProposition;
	}

	/**
	 * Sets new value for the networkParams attribute.
	 * @param params - the network parameters defining the runtime dynamics
	 */
	public void setNetworkParams(NetworkParams params)
	{
		networkParams = params;
	}

	/**
	 * Returns a string representation of this object
	 * @return a string representation of this object
	 */
	@Override
	public String toString()
	{
		return toString(0);
	}

	/**
	 * Returns a string representation of this object with indentation
	 *
	 * @param tabs - the number of tabs used to make indentation in the output.
	 * @return a string representation of this object with indentation
	 */
	String toString(int tabs)
	{
		StringBuffer info = new StringBuffer(100);

		info.append("source: ")
				.append(sourceProposition.toString())
				.append(PrintUtil.addTabs(tabs))
				.append("destination: ")
				.append(destinationProposition.toString())
				.append(PrintUtil.addTabs(tabs))
				.append("type: ")
				.append(getClass().getName());

		return info.toString();
	}
}
