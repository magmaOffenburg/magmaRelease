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
import kdo.ebn.IEBNPerception;
import kdo.ebn.IResourceBelief;
import kdo.ebn.Resource;

/**
 * XSteram converter to convert Resource objects
 * @author Thomas Rinklin
 *
 */
public class ResourceConverter implements Converter
{
	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context)
	{
		Resource res = (Resource) value;
		writer.startNode("BeliefName");
		context.convertAnother(res.getBeliefLink());
		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
	{
		Resource res = null;
		reader.moveDown();
		if ("BeliefName".equals(reader.getNodeName())) {
			IResourceBelief belief = (IResourceBelief) context.convertAnother(null, IEBNPerception.class);
			res = new Resource(belief);
		}
		reader.moveUp();

		return res;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz)
	{
		return Resource.class.equals(clazz);
	}
}
