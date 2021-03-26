/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Interface defining tags used in XML network definition
 */
public interface IXMLConstants {
	/** tag describing rule action */
	String ACTION = "action";

	/**
	 * value used in network configuration to set automatic inbox processing in
	 * every EBN execution loop
	 */
	String AUTO_INBOX_PROCESSING_TAG = "auto";

	/** tag describing available resource */
	String AVAILABLE_RESOURCE = "available-resource";

	/** tag defining parameter name */
	String BETA = "beta";

	/** tag keeping the configuration data for the engine */
	String CONFIGURATION = "configuration";

	/** attribute specifying method for control */
	String CONTROL_METHOD = "method";

	/** attribute specifying control name */
	String CONTROL_NAME = "name";

	/** tag defining parameter name */
	String DELTA = "delta";

	/** tag describing perception */
	String EBN_PERCEPTION = "ebn-perception";

	/** tag describing rule effect */
	String EFFECT = "effect";

	/** tag describing rule effect degree */
	String EFFECT_DEGREE = "degree";

	/** tag describing effect name */
	String EFFECT_NAME = "name";

	/** tag describing effect probability */
	String EFFECT_PROBABILITY = "probability";

	/** tag defining parameter name */
	String GAIN = "gain";

	/** tag defining parameter name */
	String GAMMA = "gamma";

	/** tag describing network goal */
	String GOAL = "goal";

	/** tag describing goal condition */
	String GOAL_CONDITION = "goal-condition";

	/** value used in network configuration to set gole-based inbox processing */
	String GOAL_INBOX_PROCESSING_TAG = "goal";

	/** tag describing goal importance */
	String IMPORTANCE = "importance";

	/** tag defining parameter name */
	String INBOX_PROCESSING = "inbox-processing";

	/** tag specifying interval between logic execution steps */
	String INTERVAL = "interval";

	/** attribute specifying that proposition is negated */
	String NEGATED = "negated";

	/** tag describing network parameters */
	String PARAMETERS = "parameters";

	/** tag describing rule precondition */
	String PRECONDITION = "precondition";

	/** tag describing goal relevance */
	String RELEVANCE = "relevance-condition";

	/** tag describing rule resource */
	String RESOURCE = "resource";

	/** tag describing available resource amount */
	String RESOURCE_AMOUNT = "amount";

	/** tag describing available resource name */
	String RESOURCE_NAME = "name";

	/** tag describing network rule */
	String RULE = "rule";

	/** tag defining parameter name */
	String SIGMA = "sigma";

	/** tag defining parameter name */
	String THETA = "theta";

	/** tag defining parameter name */
	String THETA_REDUCTION = "theta-reduction";
}
