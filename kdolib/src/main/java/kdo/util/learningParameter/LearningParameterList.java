/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.util.learningParameter;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import kdo.util.parameter.IParameterList;
import kdo.util.parameter.Parameter;

/**
 * Base class for sets of parameters used to modify Behaviors
 * @author Klaus Dorer
 */
@SuppressWarnings("serial")
public class LearningParameterList implements Serializable, ILearningParameterList
{
	/** the decorated parameter list */
	private IParameterList decoratee;

	private Map<String, ParameterDecorator> parameters;

	public LearningParameterList(IParameterList decoratee, Map<String, LearningParameter> learnInfo)
	{
		this.decoratee = decoratee;
		parameters = new LinkedHashMap<>();

		Map<String, Parameter> params = decoratee.getParameters();
		for (String key : params.keySet()) {
			LearningParameter range = learnInfo.get(key);
			if (range == null) {
				System.err.println("Missing learning parameter: " + key);
			}
			parameters.put(key, new ParameterDecorator(params.get(key), range));
		}
	}

	/**
	 * @return a new array containing all parameter values subject to learning
	 */
	@Override
	public float[] toChromosom()
	{
		int learnedParams = getNoOfLearnedParams();
		float[] result = new float[learnedParams];
		int j = 0;
		for (ParameterDecorator param : parameters.values()) {
			if (param.shouldBeLearned()) {
				result[j] = param.getValue();
				j++;
			}
		}
		return result;
	}

	@Override
	public float[] getStepSizes()
	{
		int learnedParams = getNoOfLearnedParams();
		float[] result = new float[learnedParams];
		int j = 0;
		for (ParameterDecorator param : parameters.values()) {
			if (param.shouldBeLearned()) {
				result[j] = param.getStepSize();
				j++;
			}
		}
		return result;
	}

	@Override
	public float[] getMinValues()
	{
		int learnedParams = getNoOfLearnedParams();
		float[] result = new float[learnedParams];
		int j = 0;
		for (ParameterDecorator param : parameters.values()) {
			if (param.shouldBeLearned()) {
				result[j] = param.getMinValue();
				j++;
			}
		}
		return result;
	}

	@Override
	public float[] getMaxValues()
	{
		int learnedParams = getNoOfLearnedParams();
		float[] result = new float[learnedParams];
		int j = 0;
		for (ParameterDecorator param : parameters.values()) {
			if (param.shouldBeLearned()) {
				result[j] = param.getMaxValue();
				j++;
			}
		}
		return result;
	}

	/**
	 * @return the number of parameters that are subject to learning
	 */
	public int getNoOfLearnedParams()
	{
		int result = 0;
		for (ParameterDecorator param : parameters.values()) {
			if (param.shouldBeLearned()) {
				result++;
			}
		}
		return result;
	}

	/**
	 * Allows to set all parameter values that are subject to learning. The order
	 * matters and should be typically retrieved by an earlier call to
	 * toChromosom.
	 * @param chromosom values for each parameter that is subject to learning
	 */
	@Override
	public void fromChromosom(float[] chromosom)
	{
		ParameterDecorator[] params = parameters.values().toArray(new ParameterDecorator[0]);
		int j = 0;
		for (int i = 0; i < chromosom.length; i++, j++) {
			while (!params[j].shouldBeLearned()) {
				j++;
			}
			params[j].setValue(chromosom[i]);
		}
	}

	@Override
	public Map<String, Parameter> getParameters()
	{
		return decoratee.getParameters();
	}

	@Override
	public float get(String key)
	{
		return parameters.get(key).getValue();
	}

	@Override
	public void put(String key, float value)
	{
		parameters.get(key).setValue(value);
	}

	@Override
	public String toString()
	{
		return decoratee.toString();
	}

	@Override
	public String getParamsString()
	{
		return decoratee.getParamsString();
	}

	@Override
	public IParameterList getDecoratedList()
	{
		return decoratee;
	}
}
