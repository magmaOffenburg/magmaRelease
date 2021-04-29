/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import kdo.ebn.Competence;
import kdo.ebn.ExtendedBehaviorNetwork;
import kdo.ebn.IEBNAction;
import kdo.ebn.IEBNPerception;
import kdo.ebn.Proposition;
import kdo.ebn.xml.converter.CompetenceConverter;
import kdo.ebn.xml.converter.ExtendedBehaviorNetworkConverter;
import kdo.ebn.xml.converter.IBehaviorConverter;
import kdo.ebn.xml.converter.IBeliefConverter;
import kdo.ebn.xml.converter.PerceptionConverter;
import kdo.ebn.xml.converter.ResourceConverter;

/**
 * Main class to serialize and deserialize networks to and from xml
 * @author Klaus Dorer
 */
public class NetworkSerializer
{
	private final XStream xStream;

	// TODO exception management??

	/**
	 * @param beliefs reference to the beliefs and resource beliefs
	 * @param behaviors reference to the behaviors
	 */
	public NetworkSerializer(Map<String, IEBNPerception> beliefs, Map<String, IEBNAction> behaviors)
	{
		xStream = new XStream(new DomDriver());
		xStream.registerConverter(new ExtendedBehaviorNetworkConverter(beliefs, behaviors));
		xStream.registerConverter(new IBeliefConverter(beliefs));
		xStream.registerConverter(new IBehaviorConverter(behaviors));
		xStream.registerConverter(new PerceptionConverter());
		xStream.registerConverter(new ResourceConverter());
		xStream.registerConverter(new CompetenceConverter());
		xStream.alias("Competence", Competence.class);
		xStream.alias("Proposition", Proposition.class);
		xStream.alias("EBN", ExtendedBehaviorNetwork.class);
	}

	/**
	 * Creates an ebn from the passed xml file
	 * @param filename the file containing an xml representation of an EBN
	 * @param beliefs the available beliefs to which to connect the perception
	 *        nodes
	 * @param behaviors the available behaviors to which to connect the actions
	 * @return a fully connected Extended Behavior network
	 */
	public static ExtendedBehaviorNetwork createEBN(
			String filename, Map<String, IEBNPerception> beliefs, Map<String, IEBNAction> behaviors)
	{
		NetworkSerializer serializer = new NetworkSerializer(beliefs, behaviors);
		return serializer.loadNetworkFromFile(new File(filename));
	}

	/**
	 * stores an ebn in a xml string
	 * @param ebn ebn to store
	 * @return xml-representation of the ebn
	 */
	public String storeNetwork(ExtendedBehaviorNetwork ebn)
	{
		return xStream.toXML(ebn);
	}

	/**
	 * stores an ebn in a file
	 * @param f target file
	 * @param ebn ebn to store
	 */
	public void storeNetworkToFile(File f, ExtendedBehaviorNetwork ebn)
	{
		String xml = storeNetwork(ebn);

		byte[] buffer = xml.getBytes();
		FileOutputStream fs;
		try {
			fs = new FileOutputStream(f);
			fs.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * loads an ebn from a xml string
	 * @param xml xml-representation of the ebn
	 * @return the ebn
	 */
	public ExtendedBehaviorNetwork loadNetwork(String xml)
	{
		return (ExtendedBehaviorNetwork) xStream.fromXML(xml);
	}

	/**
	 * loads an ebn from a file
	 * @param file xml-file
	 * @return the ebn
	 */
	public ExtendedBehaviorNetwork loadNetworkFromFile(File file)
	{
		byte[] buffer = new byte[(int) file.length()];
		FileInputStream fs;
		try {
			fs = new FileInputStream(file);
			fs.read(buffer);
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String xml = new String(buffer);

		return loadNetwork(xml);
	}
}
