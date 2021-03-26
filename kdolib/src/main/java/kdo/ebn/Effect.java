/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Class representing an Effect within a extended behavior network.
 */
public class Effect extends Proposition
{
	/** for degree in which the effect will come true */
	private Proposition degree;

	/** the probability in which the effect will come true */
	private double probability;

	/**
	 * Default constructor. Sets probability to value 1.0 and degree to null
	 */
	public Effect()
	{
		probability = 1.0;
		degree = null;
	}

	/**
	 * Constructor which sets the internal attributes of this effect object.
	 *
	 * @param parent the containing network node of this proposition
	 * @param perceptionNode perception used to calculate effect value
	 * @param isNegated negation value for the effect
	 * @param probability the probability with which an effect comes true
	 * @param degree to which an effect comes true
	 */
	public Effect(NetworkNode parent, PerceptionNode perceptionNode, boolean isNegated, double probability,
			Proposition degree)
	{
		super(parent, perceptionNode, isNegated);
		this.probability = probability;
		this.degree = degree;
	}

	/**
	 * Sets the internal attributes of this effect object.
	 *
	 * @param perceptionNode perception used to calculate effect value
	 * @param isNegated negation value for the effect
	 * @param probability the probability with which an effect comes true
	 * @param degree the degree to which an effect comes true
	 */
	public void setEffect(PerceptionNode perceptionNode, boolean isNegated, double probability, Proposition degree)
	{
		setProposition(perceptionNode, isNegated);
		this.probability = probability;
		this.degree = degree;
	}

	/**
	 * @return the probability with which an effect comes true
	 */
	public double getProbability()
	{
		return probability;
	}

	/**
	 * @return the degree to which an effect comes true
	 */
	double getDegree()
	{
		return (degree == null) ? 1.0 : degree.getTruthValue();
	}

	/**
	 * @return degree proposition
	 */
	public Proposition getDegreeProposition()
	{
		return degree;
	}

	/**
	 * @return the influence an effect has on activation spreading as product of
	 *         degree and probability
	 */
	public double getInfluence()
	{
		return getProbability() * getDegree();
	}
}
