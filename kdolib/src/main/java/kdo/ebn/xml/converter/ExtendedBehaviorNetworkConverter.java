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
import java.util.Map;
import kdo.ebn.Competence;
import kdo.ebn.ExtendedBehaviorNetwork;
import kdo.ebn.Goal;
import kdo.ebn.IEBNAction;
import kdo.ebn.IEBNPerception;
import kdo.ebn.NetworkConfigurationException;
import kdo.ebn.NetworkParams;
import kdo.ebn.PerceptionNode;
import kdo.ebn.Resource;

/**
 * XStream converter to convert ExtendedBehaviorNetwork objects
 * @author Thomas Rinklin
 */
public class ExtendedBehaviorNetworkConverter implements Converter
{
	@SuppressWarnings("unused")
	private final Map<String, IEBNPerception> beliefs;

	private final Map<String, IEBNAction> behaviors;

	/**
	 * constructor with the maps of beliefs and behaviors
	 * @param beliefs map of beliefs (and resources)
	 * @param behaviors map of behaviors
	 */
	public ExtendedBehaviorNetworkConverter(Map<String, IEBNPerception> beliefs, Map<String, IEBNAction> behaviors)
	{
		this.behaviors = behaviors;
		this.beliefs = beliefs;
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context)
	{
		ExtendedBehaviorNetwork ebn = (ExtendedBehaviorNetwork) value;

		// NetworkParams
		writer.startNode("NetworkParams");
		context.convertAnother(ebn.getNetworkParams());
		writer.endNode();

		// Perceptions
		writer.startNode("Perceptions");
		Iterator<PerceptionNode> percIter = ebn.getPerceptions();
		while (percIter.hasNext()) {
			PerceptionNode perceptionNode = percIter.next();
			writer.startNode("PerceptionNode");
			context.convertAnother(perceptionNode);
			writer.endNode();
		}
		writer.endNode();

		// Ressources
		writer.startNode("Resources");
		Iterator<Resource> resIter = ebn.getResources();
		while (resIter.hasNext()) {
			Resource resource = resIter.next();
			writer.startNode("Resource");
			context.convertAnother(resource);
			writer.endNode();
		}
		writer.endNode();

		// Goals
		writer.startNode("Goals");
		Iterator<Goal> goalIter = ebn.getGoals();

		while (goalIter.hasNext()) {
			Goal goal = goalIter.next();
			writer.startNode("Goal");
			context.convertAnother(goal);
			writer.endNode();
		}
		writer.endNode();

		// Note, that Competences MUST be added AFTER the goals
		// Competences
		writer.startNode("Competences");
		Iterator<Competence> compIter = ebn.getCompetenceModules();
		while (compIter.hasNext()) {
			Competence competence = compIter.next();

			writer.startNode("Competence");
			context.convertAnother(competence);
			writer.endNode();
		}
		writer.endNode();
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
	{
		ExtendedBehaviorNetwork ebn = new ExtendedBehaviorNetwork("", behaviors);

		while (reader.hasMoreChildren()) {
			reader.moveDown();

			String nodeName = reader.getNodeName();

			if ("NetworkParams".equals(nodeName)) {
				NetworkParams newNetParams = (NetworkParams) context.convertAnother(ebn, NetworkParams.class);
				ebn.getNetworkParams().setNetworkParams(newNetParams);

			} else if ("Perceptions".equals(nodeName)) {
				while (reader.hasMoreChildren()) {
					reader.moveDown();
					if ("PerceptionNode".equalsIgnoreCase(reader.getNodeName())) {
						PerceptionNode perc = (PerceptionNode) context.convertAnother(ebn, PerceptionNode.class);
						ebn.addPerception(perc);
					}
					reader.moveUp();
				}

			} else if ("Resources".equals(nodeName)) {
				while (reader.hasMoreChildren()) {
					reader.moveDown();
					if ("Resource".equals(reader.getNodeName())) {
						Resource res = (Resource) context.convertAnother(ebn, Resource.class);
						ebn.addResource(res);
					}
					reader.moveUp();
				}

			} else if ("Goals".equals(nodeName)) {
				int goalCount = 0;
				while (reader.hasMoreChildren()) {
					reader.moveDown();
					if ("Goal".equals(reader.getNodeName())) {
						Goal goal = (Goal) context.convertAnother(ebn, Goal.class);
						ebn.addGoal(goal);
						goalCount++;
					}
					reader.moveUp();
				}
				context.put("GoalCount", goalCount);

			} else if ("Competences".equals(nodeName)) {
				while (reader.hasMoreChildren()) {
					reader.moveDown();
					if ("Competence".equals(reader.getNodeName())) {
						Competence comp = (Competence) context.convertAnother(ebn, Competence.class);
						try {
							ebn.addCompetenceModule(comp);
						} catch (NetworkConfigurationException e) {
							e.printStackTrace();
						}
					}
					reader.moveUp();
				}
			}
			reader.moveUp();
		}

		return ebn;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class arg0)
	{
		return arg0.equals(ExtendedBehaviorNetwork.class);
	}
}
