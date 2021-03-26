/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.util.parameter;

@SuppressWarnings("serial")
public class EnumParameterList<T extends Enum<T>> extends ParameterList
{
	public EnumParameterList(Class<T> enumType)
	{
		super();
		for (T constant : enumType.getEnumConstants()) {
			put(constant, 0);
		}
		setValues();
	}

	protected void setValues()
	{
	}

	final public float get(T param)
	{
		return get(param.name());
	}

	public void put(T param, float value)
	{
		put(param.name(), value);
	}
}
