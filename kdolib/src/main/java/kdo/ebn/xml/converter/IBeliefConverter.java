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
import kdo.ebn.IEBNPerception;

/**
 * XStream converter to convert objects implementing IBelief
 * @author Thomas Rinklin
 *
 */
public class IBeliefConverter implements Converter
{
	private final Map<String, IEBNPerception> belief;

	/**
	 * constructor with the maps of beliefs
	 * @param belief map of beliefs (and resources)
	 */
	public IBeliefConverter(Map<String, IEBNPerception> belief)
	{
		this.belief = belief;
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context)
	{
		IEBNPerception belief = (IEBNPerception) value;
		writer.setValue(belief.getName());
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
	{
		String beliefName = reader.getValue();
		return belief.get(beliefName);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz)
	{
		return IEBNPerception.class.isAssignableFrom(clazz);
	}
}
