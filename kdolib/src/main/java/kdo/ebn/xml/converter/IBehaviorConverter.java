/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn.xml.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.util.Map;
import kdo.ebn.IEBNAction;

/**
 * XSteram converter to convert objects implementing IBehavior
 * @author Thomas Rinklin
 *
 */
public class IBehaviorConverter implements Converter
{
	private final Map<String, IEBNAction> behaviors;

	/**
	 * constructor with the map of behaviors
	 * @param behaviors map of behaviors
	 */
	public IBehaviorConverter(Map<String, IEBNAction> behaviors)
	{
		this.behaviors = behaviors;
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context)
	{
		IEBNAction behavior = (IEBNAction) value;
		writer.setValue(behavior.getName());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
	{
		return behaviors.get(reader.getValue());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz)
	{
		return IEBNAction.class.isAssignableFrom(clazz);
	}
}
