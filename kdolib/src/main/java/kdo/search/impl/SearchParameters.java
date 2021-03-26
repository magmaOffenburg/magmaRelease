/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.impl;

import java.util.HashMap;
import java.util.Map;
import kdo.domain.IProblem;
import kdo.util.IRandomSource;

/**
 * @author klaus
 *
 */
public class SearchParameters
{
	private int repeats;

	private String strategy;

	private Map<String, Double> strategyParameter;

	private long maxRuntime;

	private IRandomSource randomSource;

	private IProblem domain;

	public SearchParameters(IRandomSource random, String strategy)
	{
		repeats = 1;
		this.strategy = strategy;
		maxRuntime = 60000;
		randomSource = random;
		domain = null;
		strategyParameter = new HashMap<>();
	}

	public void setRandomSource(IRandomSource randomSource)
	{
		this.randomSource = randomSource;
	}

	public IRandomSource getRandomSource()
	{
		return randomSource;
	}

	/**
	 * @param repeats the repeats to set
	 */
	public void setRepeats(int repeats)
	{
		this.repeats = repeats;
	}

	/**
	 * @return the repeats
	 */
	public int getRepeats()
	{
		return repeats;
	}

	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategy(String strategy)
	{
		this.strategy = strategy;
	}

	/**
	 * @return the strategy
	 */
	public String getStrategy()
	{
		return strategy;
	}

	/**
	 * @param name the name of the parameter
	 * @param value the value to use
	 */
	public void setStrategyParameter(String name, double value)
	{
		strategyParameter.put(name, value);
	}

	/**
	 * @return the strategy parameter specified, null if not existing
	 */
	public Double getStrategyParameter(String name)
	{
		return strategyParameter.get(name);
	}

	/**
	 * @return the strategy parameter specified, defaultValue if not existing
	 */
	public double getStrategyParameter(String name, double defaultValue)
	{
		Double result = strategyParameter.get(name);
		if (result == null) {
			return defaultValue;
		}
		return result.doubleValue();
	}

	/**
	 * @param maxRuntime the maxRuntime to set
	 */
	public void setMaxRuntime(long maxRuntime)
	{
		this.maxRuntime = maxRuntime;
	}

	/**
	 * @return the maxRuntime
	 */
	public long getMaxRuntime()
	{
		return maxRuntime;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(IProblem domain)
	{
		this.domain = domain;
	}

	/**
	 * @return the domain
	 */
	public IProblem getDomain()
	{
		return domain;
	}

	@Override
	public String toString()
	{
		return "SearchParameters [repeats=" + repeats + ", strategy=" + strategy + ", maxRuntime=" + maxRuntime + "]";
	}
}