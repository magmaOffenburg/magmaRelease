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
import kdo.ebn.PerceptionNode;

/**
 * XSteram converter to convert Perception objects
 * @author Thomas Rinklin
 *
 */
public class PerceptionConverter implements Converter
{
	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context)
	{
		PerceptionNode perceptionNode = (PerceptionNode) value;
		writer.startNode("BeliefName");
		context.convertAnother(perceptionNode.getBeliefLink());
		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
	{
		PerceptionNode perc = null;

		reader.moveDown();
		if ("BeliefName".equals(reader.getNodeName())) {
			IEBNPerception belief = (IEBNPerception) context.convertAnother(null, IEBNPerception.class);
			perc = new PerceptionNode(belief);
		}
		reader.moveUp();
		return perc;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz)
	{
		return clazz.equals(PerceptionNode.class);
	}
}
