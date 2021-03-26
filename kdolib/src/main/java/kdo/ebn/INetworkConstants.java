/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Interface defining all global constants for behavior networks
 */
public interface INetworkConstants {
	/** inbox processing mode value to check inbox in every EBN execution loop */
	boolean AUTO_INBOX_PROCESSING = true;

	/** allows to limit network to execute only one action during each step */
	boolean DEFAULT_CONCURRENT_ACTIONS = true;

	/** used during competence initialization to specify connections to the goals */
	boolean DEFAULT_GOAL_TRACKING = true;

	/** allows to switch off transfer function usage */
	boolean DEFAULT_TRANSFER_FUNCTION = true;

	/** inbox processing mode value to check inbox with predefined goal and rule */
	boolean GOAL_INBOX_PROCESSING = false;

	/** default way for process inbox mode */
	boolean DEFAULT_INBOX_PROCESSING = GOAL_INBOX_PROCESSING;

	/** default value for the beta network parameter */
	double DEFAULT_BETA = 0.5;

	/** default value for the delta network parameter */
	double DEFAULT_DELTA = 0.7;

	/** default value for the gain network parameter */
	double DEFAULT_GAIN = 5.0;

	/** default value for the gamma network parameter */
	double DEFAULT_GAMMA = 0.8;

	/** default value for the sigma network parameter */
	double DEFAULT_SIGMA = 0.55;

	/** default value for the theta network parameter */
	double DEFAULT_THETA = 0.8;

	/** default value for the threshold_reduction network parameter */
	double DEFAULT_THRESHOLD_REDUCTION = 0.1;

	/** Number of milliseconds used to receive message from inbox */
	long RECEIVE_TIMEOUT = 5000;
}
