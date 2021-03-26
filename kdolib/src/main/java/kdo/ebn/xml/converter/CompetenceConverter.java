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
import java.util.Iterator;
import kdo.ebn.Competence;
import kdo.ebn.Condition;
import kdo.ebn.Effect;
import kdo.ebn.IEBNAction;
import kdo.ebn.NetworkParams;
import kdo.ebn.Proposition;
import kdo.ebn.ResourceProposition;

/**
 * XSteram converter to convert Competence objects
 * @author Thomas Rinklin
 *
 */
public class CompetenceConverter implements Converter
{
	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context)
	{
		// Name
		Competence comp = (Competence) value;
		writer.startNode("Name");
		writer.setValue(comp.getName());
		writer.endNode();

		// Precondition
		Condition cond = comp.getPrecondition();
		if (cond != null) {
			writer.startNode("Precondition");
			context.convertAnother(cond);
			writer.endNode();
		}

		// Actions
		Iterator<IEBNAction> actions = comp.getActions();
		writer.startNode("Actions");
		while (actions.hasNext()) {
			IEBNAction behavior = actions.next();
			writer.startNode("Action");
			context.convertAnother(behavior);
			writer.endNode();
		}
		writer.endNode();

		// Effects
		Iterator<Effect> effects = comp.getEffects();
		writer.startNode("Effects");
		while (effects.hasNext()) {
			Effect effect = effects.next();
			writer.startNode("Effect");
			context.convertAnother(effect);
			writer.endNode();
		}
		writer.endNode();

		// Resources
		Iterator<ResourceProposition> resPropositions = comp.getResourcePropositions();
		writer.startNode("ResourcePropositions");
		while (resPropositions.hasNext()) {
			ResourceProposition resProposition = resPropositions.next();
			writer.startNode("ResourceProposition");
			context.convertAnother(resProposition);
			writer.endNode();
		}
		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
	{
		Competence comp = null;
		if (reader.hasMoreChildren()) {
			reader.moveDown();
			if (reader.getNodeName().equals("Name")) {
				String compName = reader.getValue();
				Integer goalCount = (Integer) context.get("GoalCount");
				if (goalCount == null) {
					throw new RuntimeException(
							"\"GoalCount\" is not set. Probably, the goals were not be parsed before.");
				}

				comp = new Competence(compName, goalCount, new NetworkParams());
			}
			reader.moveUp();
		}

		if (comp == null) {
			throw new RuntimeException("Did not found the Name as first Item in the Coponent of the XMl-File");
		}

		while (reader.hasMoreChildren()) {
			reader.moveDown();

			String nodeName = reader.getNodeName();

			if ("Actions".equals(nodeName)) {
				while (reader.hasMoreChildren()) {
					reader.moveDown();

					if ("Action".equals(reader.getNodeName())) {
						IEBNAction behavior = (IEBNAction) context.convertAnother(comp, IEBNAction.class);
						comp.addAction(behavior);
					}
					reader.moveUp();
				}
			} else if ("Effects".equals(nodeName)) {
				while (reader.hasMoreChildren()) {
					reader.moveDown();
					if (reader.getNodeName().equals("Effect")) {
						while (reader.hasMoreChildren()) {
							Effect eff = (Effect) context.convertAnother(comp, Effect.class);
							comp.addEffect(eff);
						}
					}
					reader.moveUp();
				}
			} else if ("Precondition".equals(nodeName)) {
				Condition cond = (Condition) context.convertAnother(comp, Condition.class);
				Iterator<Proposition> propositions = cond.getPropositions();

				while (propositions.hasNext()) {
					Proposition proposition = propositions.next();
					comp.addPrecondition(proposition);
				}

			} else if ("ResourcePropositions".equals(nodeName)) {
				while (reader.hasMoreChildren()) {
					reader.moveDown();
					if (reader.getNodeName().equals("ResourceProposition")) {
						while (reader.hasMoreChildren()) {
							ResourceProposition resProp =
									(ResourceProposition) context.convertAnother(comp, ResourceProposition.class);
							comp.addResource(resProp);
						}
					}
					reader.moveUp();
				}
			}
			reader.moveUp();
		}

		return comp;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class clazz)
	{
		return clazz.equals(Competence.class);
	}
}
