/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.util.learningParameter;

import java.util.HashMap;
import java.util.Map;
import kdo.util.parameter.IParameterList;

public class LearningParameterListBuilder<T extends Enum<T>>
{
	@FunctionalInterface
	public interface LearningParameterListConstructor {
		LearningParameterList create(IParameterList decoratee, Map<String, LearningParameter> learnInfo);
	}

	private HashMap<String, LearningParameter> learnInfo = new HashMap<>();

	public void put(boolean shouldBeLearned, String param, float stepSize, float minValue, float maxValue)
	{
		learnInfo.put(param, new LearningParameter(shouldBeLearned, stepSize, minValue, maxValue));
	}

	public void put(boolean shouldBeLearned, T param, float stepSize, float minValue, float maxValue)
	{
		put(shouldBeLearned, param.name(), stepSize, minValue, maxValue);
	}

	public void put(T param, float stepSize, float minValue, float maxValue)
	{
		put(true, param, stepSize, minValue, maxValue);
	}

	public LearningParameterList create(
			LearningParameterListConstructor constructor, IParameterList decoratee, float[] chromosom)
	{
		LearningParameterList list = constructor.create(decoratee, learnInfo);
		if (chromosom != null) {
			list.fromChromosom(chromosom);
		}
		return list;
	}

	public LearningParameterList create(IParameterList decoratee, float[] chromosom)
	{
		return create(LearningParameterList::new, decoratee, chromosom);
	}
}
