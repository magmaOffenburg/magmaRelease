/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Defines the parameters that control the runtime dynamics of the network. The
 * parameters defined are: For all of these parameters there are get and set
 * methods
 */
public class NetworkParams implements INetworkConstants
{
	/** specifies whether multiple actions can be executed during each step */
	private boolean concurrentActions;

	/** specifies the way for goal tracking */
	private boolean goalTracking;

	/**
	 * specifies whether inbox should be processed automaticaly in every EBN
	 * execution loop
	 */
	private boolean inboxProcessing;

	/** specifies whethe transfer function should be used */
	private boolean transferFunction;

	/** inertia of activation and therefore inertia of behavior changes */
	private double beta;

	/** relative influence of inhibition [0..1] */
	private double delta;

	/** steepness of the sigmoidal transfer function */
	private double gain;

	/** relative influence of activation [0..1] */
	private double gamma;

	/** breadth of the sigmoidal transfer function */
	private double sigma;

	/** initial activation threshold */
	private double theta;

	/** the decay of theta */
	private double thetaReduction;

	/**
	 * Default constructor. Sets all the parameters with values from
	 * INetworkConstants
	 */
	public NetworkParams()
	{
		setDefaultParameters();
		setInboxProcessing(DEFAULT_INBOX_PROCESSING);
		setTransferFunction(DEFAULT_TRANSFER_FUNCTION);
		setGoalTracking(DEFAULT_GOAL_TRACKING);
		setConcurrentActions(DEFAULT_CONCURRENT_ACTIONS);
	}

	/**
	 * The EBN default parameter settings
	 */
	void setDefaultParameters()
	{
		setGamma(DEFAULT_GAMMA);
		setDelta(DEFAULT_DELTA);
		setBeta(DEFAULT_BETA);
		setTheta(DEFAULT_THETA);
		setThetaReduction(DEFAULT_THRESHOLD_REDUCTION);
		setSigma(DEFAULT_SIGMA);
		setGain(DEFAULT_GAIN);
	}

	/**
	 * Copy constructor. Sets all the parameters with values from params.
	 * @param params - instance of NetworkParams from which all the values for
	 *        parameters are taken
	 */
	public NetworkParams(NetworkParams params)
	{
		setNetworkParams(params);
	}

	/**
	 * Sets all the parameters with values from params.
	 * @param params - instance of NetworkParams from which all the values for
	 *        parameters are taken
	 */
	public void setNetworkParams(NetworkParams params)
	{
		setGamma(params.getGamma());
		setDelta(params.getDelta());
		setSigma(params.getSigma());
		setGain(params.getGain());
		setBeta(params.getBeta());
		setTheta(params.getTheta());
		setThetaReduction(params.getThetaReduction());
		setInboxProcessing(params.getInboxProcessing());
		setConcurrentActions(params.getConcurrentActions());
		setGoalTracking(params.getGoalTracking());
		setTransferFunction(params.getTransferFunction());
	}

	/**
	 * Returns the gamma value of the parameters.
	 *
	 * @return double The gamma value of the parameter.
	 */
	public double getGamma()
	{
		return gamma;
	}

	/**
	 * Returns the delta value of the parameters.
	 *
	 * @return double The delta value of the parameter.
	 */
	public double getDelta()
	{
		return delta;
	}

	/**
	 * Returns the beta value of the parameters.
	 *
	 * @return double The beta value of the parameter.
	 */
	public double getNetworkPi()
	{
		return beta;
	}

	/**
	 * Returns the theta value of the parameters.
	 *
	 * @return double The theta value of the parameter.
	 */
	public double getTheta()
	{
		return theta;
	}

	/**
	 * Returns the theta reduction value of the parameters.
	 *
	 * @return double The theta reduction value of the parameter.
	 */
	public double getThetaReduction()
	{
		return thetaReduction;
	}

	/**
	 * Returns the sigma value of the parameters.
	 *
	 * @return double The sigma value of the parameter.
	 */
	public double getSigma()
	{
		return sigma;
	}

	/**
	 * Returns the gain value of the parameters.
	 *
	 * @return double The gain value of the parameter.
	 */
	public double getGain()
	{
		return gain;
	}

	/**
	 * Returns the beta value of the parameters.
	 *
	 * @return double The beta value of the parameter.
	 */
	public double getBeta()
	{
		return beta;
	}

	/**
	 * Returns the concurrent actions value of the parameters.
	 *
	 * @return double The concurrent actions value of the parameter.
	 */
	public boolean getConcurrentActions()
	{
		return concurrentActions;
	}

	/**
	 * Returns the goal tracking value of the parameters.
	 *
	 * @return double The goal tracking value of the parameter.
	 */
	public boolean getGoalTracking()
	{
		return goalTracking;
	}

	/**
	 * Sets new value for inboxProcessing field
	 *
	 * @param inboxProcessing - new value for the field
	 */
	public void setInboxProcessing(boolean inboxProcessing)
	{
		this.inboxProcessing = inboxProcessing;
	}

	/**
	 * Returns the inbox processing state.
	 *
	 * @return boolean The inbox processing state.
	 */
	boolean getInboxProcessing()
	{
		return inboxProcessing;
	}

	/**
	 * Returns the transfer function state.
	 *
	 * @return boolean The transfer function state.
	 */
	public boolean getTransferFunction()
	{
		return transferFunction;
	}

	/**
	 * Sets gamma parameter
	 * @param val - new value for the parameter
	 */
	public void setGamma(double val)
	{
		gamma = val;
	}

	/**
	 * Sets delta parameter
	 * @param val - new value for the parameter
	 */
	public void setDelta(double val)
	{
		delta = val;
	}

	/**
	 * Sets theta parameter
	 * @param val - new value for the parameter
	 */
	public void setTheta(double val)
	{
		theta = val;
	}

	/**
	 * Sets thetaReduction parameter
	 * @param val - new value for the parameter
	 */
	public void setThetaReduction(double val)
	{
		thetaReduction = val;
	}

	/**
	 * Sets sigma parameter
	 * @param val - new value for the parameter
	 */
	public void setSigma(double val)
	{
		sigma = val;
	}

	/**
	 * Sets gain parameter
	 * @param val - new value for the parameter
	 */
	public void setGain(double val)
	{
		gain = val;
	}

	/**
	 * Sets beta parameter
	 * @param val - new value for the parameter
	 */
	public void setBeta(double val)
	{
		beta = val;
	}

	/**
	 * Sets goalTracking parameter
	 * @param val - new value for the parameter
	 */
	public void setGoalTracking(boolean val)
	{
		goalTracking = val;
	}

	/**
	 * Sets concurrentActions parameter
	 * @param val - new value for the parameter
	 */
	public void setConcurrentActions(boolean val)
	{
		concurrentActions = val;
	}

	/**
	 * Sets transferFunction parameter
	 * @param val - new value for the parameter
	 */
	public void setTransferFunction(boolean val)
	{
		transferFunction = val;
	}

	/**
	 * Returns the information about current values of network parameters.
	 *
	 * @param tabs - the number of tabs used to make indentation in the output.
	 * @return the information about current values of network parameters.
	 */
	public String toString(int tabs)
	{
		StringBuffer info = new StringBuffer(100);

		info.append(PrintUtil.addTabs(tabs))
				.append("beta: ")
				.append(beta)
				.append(PrintUtil.addTabs(tabs))
				.append("gamma: ")
				.append(gamma)
				.append(PrintUtil.addTabs(tabs))
				.append("delta: ")
				.append(delta)
				.append(PrintUtil.addTabs(tabs))
				.append("theta: ")
				.append(theta)
				.append(PrintUtil.addTabs(tabs))
				.append("theta reduction: ")
				.append(thetaReduction);

		if (inboxProcessing) {
			info.append("automatic inbox processing");
		}

		return info.toString();
	}
}
