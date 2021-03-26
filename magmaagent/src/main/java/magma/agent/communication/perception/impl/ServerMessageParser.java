/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.IMessageParser;
import hso.autonomy.agent.communication.perception.IPerceptor;
import hso.autonomy.agent.communication.perception.IPerceptorMap;
import hso.autonomy.agent.communication.perception.PerceptorConversionException;
import hso.autonomy.agent.communication.perception.impl.AccelerometerPerceptor;
import hso.autonomy.agent.communication.perception.impl.ForceResistancePerceptor;
import hso.autonomy.agent.communication.perception.impl.GyroPerceptor;
import hso.autonomy.agent.communication.perception.impl.HingeJointPerceptor;
import hso.autonomy.agent.communication.perception.impl.LinePerceptor;
import hso.autonomy.agent.communication.perception.impl.PerceptorMap;
import hso.autonomy.agent.communication.perception.impl.TimePerceptor;
import hso.autonomy.agent.communication.perception.impl.TouchPerceptor;
import hso.autonomy.agent.communication.perception.impl.VisibleObjectPerceptor;
import hso.autonomy.util.symboltreeparser.IllegalSymbolInputException;
import hso.autonomy.util.symboltreeparser.SymbolNode;
import hso.autonomy.util.symboltreeparser.SymbolTreeParser;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import magma.agent.IMagmaConstants;
import magma.common.spark.TeamColor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Parses incoming server messages, converts them into single Perceptor messages
 * and delivers them to all observers
 *
 * @author Simon Raffeiner
 */
public class ServerMessageParser implements IMessageParser
{
	private final SymbolTreeParser treeParser;

	public ServerMessageParser()
	{
		treeParser = new SymbolTreeParser();
	}

	/**
	 * Parse a string into a list of Perceptor objects
	 *
	 * @param message Message string
	 * @return List of parsed Perceptor objects
	 * @throws PerceptorConversionException If the input string contains illegal
	 *         data which cannot be converted
	 */
	@Override
	public IPerceptorMap parseMessage(byte[] message) throws PerceptorConversionException
	{
		SymbolNode root;

		// Generate the symbol tree
		try {
			String msg = new String(message, 0, message.length, "UTF-8");
			root = treeParser.parse(msg);

		} catch (IllegalSymbolInputException e) {
			throw new PerceptorConversionException("Parsing error: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new PerceptorConversionException("Parsing error: unsupported encoding " + e.getMessage());
		}

		IPerceptorMap map = new PerceptorMap();

		// Parse all top-level nodes into messages
		for (int i = 0; i < root.children.size(); i++) {
			Object node = root.children.get(i);

			/*
			 * The tree may contain top-level leaves, ATM they are not specified
			 * and therefore not parsed
			 */
			if (node instanceof SymbolNode) {
				parseNode((SymbolNode) node, map);
			}
		}

		return map;
	}

	private float parseFloat(Object value)
	{
		try {
			return Float.parseFloat((String) value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Parse a symbol tree node into a Perceptor object (if possible)
	 *
	 * @param node Symbol tree node
	 * @throws PerceptorConversionException If the input string contains illegal
	 *         data which cannot be converted
	 */
	private void parseNode(SymbolNode node, Map<String, IPerceptor> map) throws PerceptorConversionException
	{
		/* Check message type */
		Object child = node.children.get(0);

		if (!(child instanceof String))
			throw new PerceptorConversionException("Malformed node: " + node.toString());

		String type = (String) child;
		IPerceptor perceptor = null;
		switch (type) {
		case "HJ":
			perceptor = parseHingeJoint(node);
			break;
		case "time":
			perceptor = parseTime(node);
			break;
		case "GS":
			perceptor = parseGameState(node);
			break;
		case "GYR":
			perceptor = parseGyro(node);
			break;
		case "ACC":
			perceptor = parseAccelerometer(node);
			break;
		case "See":
			parseVision(node, map);
			break;
		case "TCH":
			perceptor = parseTouch(node);
			break;
		case "AgentState":
			perceptor = parseAgentState(node);
			break;
		case "hear":
			perceptor = parseHear(node);
			break;
		case "FRP":
			perceptor = parseForceResistance(node);
			break;
		default:
			/* Unknown node? */
			break;
		}
		if (perceptor != null) {
			map.put(perceptor.getName(), perceptor);
		}
	}

	/**
	 * Parse a symbol tree node into a Gyro Perceptor object
	 *
	 * @param node Symbol tree node
	 * @return Gyro Perceptor object
	 * @throws PerceptorConversionException If the input string contains illegal
	 *         data which cannot be converted
	 */
	private GyroPerceptor parseGyro(SymbolNode node) throws PerceptorConversionException
	{
		if (!(node.children.get(1) instanceof SymbolNode) || !(node.children.get(2) instanceof SymbolNode))
			throw new PerceptorConversionException("Malformed Message: " + node.toString());

		try {
			/* Check content */
			SymbolNode nameNode = (SymbolNode) node.children.get(1);
			SymbolNode rotationNode = (SymbolNode) node.children.get(2);

			if (!nameNode.children.get(0).equals("n"))
				throw new PerceptorConversionException("name expected: " + node.toString());

			if (!rotationNode.children.get(0).equals("rt"))
				throw new PerceptorConversionException("rotation expected: " + node.toString());

			return new GyroPerceptor(nameNode.children.get(1) + "Gyro", parseFloat(rotationNode.children.get(1)),
					parseFloat(rotationNode.children.get(2)), parseFloat(rotationNode.children.get(3)));
		} catch (IndexOutOfBoundsException e) {
			throw new PerceptorConversionException("Malformed node: " + node.toString());
		}
	}

	/**
	 * Parse a symbol tree node into a Gyro perceptor object
	 *
	 * @param node Symbol tree node
	 * @return Gyro perceptor object
	 * @throws PerceptorConversionException If the input string contains illegal
	 *         data which cannot be converted
	 */
	private AccelerometerPerceptor parseAccelerometer(SymbolNode node) throws PerceptorConversionException
	{
		if (!(node.children.get(1) instanceof SymbolNode) || !(node.children.get(2) instanceof SymbolNode))
			throw new PerceptorConversionException("Malformed Message: " + node.toString());

		try {
			/* Check content */
			SymbolNode nameNode = (SymbolNode) node.children.get(1);
			SymbolNode accelerationNode = (SymbolNode) node.children.get(2);

			if (!nameNode.children.get(0).equals("n"))
				throw new PerceptorConversionException("name expected: " + node.toString());

			if (!accelerationNode.children.get(0).equals("a"))
				throw new PerceptorConversionException("rotation expected: " + node.toString());

			return new AccelerometerPerceptor(nameNode.children.get(1) + "Accel",
					parseFloat(accelerationNode.children.get(1)), parseFloat(accelerationNode.children.get(2)),
					parseFloat(accelerationNode.children.get(3)));

		} catch (NumberFormatException e) {
			// seems that the server sometimes sends NAN, in which case we ignore
			// the reading for accelerometer
			return null;
		} catch (IndexOutOfBoundsException e) {
			throw new PerceptorConversionException("Malformed node: " + node.toString());
		}
	}

	/**
	 * Parse a symbol tree node into a Hinge Joint Perceptor object
	 *
	 * @param node Symbol tree node
	 * @return Hinge Joint Perceptor object
	 * @throws PerceptorConversionException If the input string contains illegal
	 *         data which cannot be converted
	 */
	private HingeJointPerceptor parseHingeJoint(SymbolNode node) throws PerceptorConversionException
	{
		try {
			// if (!(node.children.get(1) instanceof SymbolNode)
			// || !(node.children.get(2) instanceof SymbolNode))
			// throw new PerceptorConversionException("Malformed Message: "
			// + node.toString());

			/* Check content */
			SymbolNode nameNode = (SymbolNode) node.children.get(1);
			SymbolNode rotationNode = (SymbolNode) node.children.get(2);

			// if (!((String) nameNode.children.get(0)).equals("n"))
			// throw new PerceptorConversionException("Malformed Message: "
			// + node.toString() + ": name expected");
			//
			// if (!((String) rotationNode.children.get(0)).equals("ax"))
			// throw new PerceptorConversionException("Malformed Message: "
			// + node.toString() + ": axis expected");

			return new HingeJointPerceptor((String) nameNode.children.get(1), parseFloat(rotationNode.children.get(1)));
		} catch (IndexOutOfBoundsException e) {
			throw new PerceptorConversionException("Malformed node: " + node.toString());
		}
	}

	/**
	 * Parse a symbol tree node into a Force Resistance Perceptor object
	 *
	 * @param node Symbol tree node
	 * @return Force Resistance Perceptor object
	 * @throws PerceptorConversionException If the input string contains illegal
	 *         data which cannot be converted
	 */
	private ForceResistancePerceptor parseForceResistance(SymbolNode node) throws PerceptorConversionException
	{
		try {
			// Sanity checks
			if (!(node.children.get(1) instanceof SymbolNode) ||
					!(node.children.get(2) instanceof SymbolNode) | !(node.children.get(3) instanceof SymbolNode))
				throw new PerceptorConversionException("Malformed Message: " + node.toString());

			/* Check content */
			SymbolNode nameNode = (SymbolNode) node.children.get(1);
			SymbolNode originNode = (SymbolNode) node.children.get(2);
			SymbolNode forceNode = (SymbolNode) node.children.get(3);

			if (!nameNode.children.get(0).equals("n"))
				throw new PerceptorConversionException("name expected: " + node.toString());
			if (!originNode.children.get(0).equals("c"))
				throw new PerceptorConversionException("origin expected: " + node.toString());
			if (!forceNode.children.get(0).equals("f"))
				throw new PerceptorConversionException("force expected: " + node.toString());

			return new ForceResistancePerceptor((String) nameNode.children.get(1),
					parseFloat(originNode.children.get(1)), parseFloat(originNode.children.get(2)),
					parseFloat(originNode.children.get(3)), parseFloat(forceNode.children.get(1)),
					parseFloat(forceNode.children.get(2)), parseFloat(forceNode.children.get(3)));
		} catch (IndexOutOfBoundsException e) {
			throw new PerceptorConversionException("Malformed node: " + node.toString());
		}
	}

	/**
	 * Parse a symbol tree node into a Touch Perceptor object
	 *
	 * @param node Symbol tree node
	 * @return Touch Perceptor object
	 * @throws PerceptorConversionException If the input string contains illegal
	 *         data which cannot be converted
	 */
	private TouchPerceptor parseTouch(SymbolNode node) throws PerceptorConversionException
	{
		if (node.children.size() != 5 || !node.children.get(1).equals("n") || !node.children.get(3).equals("val"))
			throw new PerceptorConversionException("Malformed node: " + node.toString());

		try {
			String name = (String) node.children.get(2);

			boolean value = false;
			if (node.children.get(4).equals("1"))
				value = true;

			return new TouchPerceptor(name, value);
		} catch (IndexOutOfBoundsException e) {
			throw new PerceptorConversionException("Malformed node: " + node.toString());
		}
	}

	/**
	 * Parse a symbol tree vision node into a list of Perceptor objects
	 *
	 * @param node Symbol tree vision node
	 * @throws PerceptorConversionException If the input string contains illegal
	 *         data which cannot be converted
	 */
	private void parseVision(SymbolNode node, Map<String, IPerceptor> map) throws PerceptorConversionException
	{
		// Sanity checks
		if (node.children.size() == 0)
			throw new PerceptorConversionException("Malformed Node: " + node.toString());

		try {
			// Parse visible objects
			int lineID = 0;
			for (int i = 1; i < node.children.size(); i++) {
				Object subItem = node.children.get(i);

				if (subItem instanceof SymbolNode) {
					SymbolNode subnode = (SymbolNode) subItem;

					// Player object?
					if (!(subnode.children.get(0) instanceof String))
						throw new PerceptorConversionException("Malformed Node, empty name: " + node.toString());
					else if (subnode.children.get(0).equals("P"))
						parsePlayer(subnode, map);

					else if (subnode.children.get(0).equals("L"))
						parseLine(subnode, map, lineID++);

					else
						parseVisibleObject(subnode, map);
				}
			}

		} catch (IndexOutOfBoundsException e) {
			throw new PerceptorConversionException("Malformed node: " + node.toString());
		}
	}

	private void parseLine(SymbolNode node, Map<String, IPerceptor> map, int lineID) throws PerceptorConversionException
	{
		try {
			assert node.children.size() == 3 : "Malformed node";
			assert node.children.get(1) instanceof SymbolNode : "Malformed node: " + node.toString();
			assert node.children.get(2) instanceof SymbolNode : "Malformed node: " + node.toString();

			Vector3D pol1 = parsePol((SymbolNode) node.children.get(1));
			Vector3D pol2 = parsePol((SymbolNode) node.children.get(2));

			LinePerceptor perceptor = new LinePerceptor("FieldLine", pol1, pol2, true, "L" + lineID);
			map.put(perceptor.getName(), perceptor);

		} catch (IndexOutOfBoundsException e) {
			throw new PerceptorConversionException("Malformed node: " + node.toString());
		}
	}

	/**
	 * Parse a symbol tree node into a Player object
	 *
	 * @param node Symbol tree node
	 * @throws PerceptorConversionException If the input string contains illegal
	 *         data which cannot be converted
	 */
	private void parsePlayer(SymbolNode node, Map<String, IPerceptor> map) throws PerceptorConversionException
	{
		try {
			/* Parse content */
			String teamName = IMagmaConstants.UNKNOWN_PLAYER_TEAMNAME;
			int id = IMagmaConstants.UNKNOWN_PLAYER_NUMBER;
			Vector3D pol = Vector3D.ZERO;
			String bodyPartIdentifier = "";
			Map<String, Vector3D> bodyPartMap = new HashMap<>();

			for (int i = 1; i < node.children.size(); i++) {
				if (!(node.children.get(i) instanceof SymbolNode)) {
					throw new PerceptorConversionException("Malformed node: " + node.toString());
				}

				SymbolNode param = (SymbolNode) node.children.get(i);

				if (param.children.get(0).equals("team")) {
					teamName = (String) param.children.get(1);
				} else if (param.children.get(0).equals("id")) {
					id = Integer.parseInt((String) param.children.get(1));
				} else {
					// in case of seeing parts of opponent, we have to look into them
					if (!param.children.get(0).equals("pol")) {
						bodyPartIdentifier = (String) param.children.get(0);
						param = (SymbolNode) param.children.get(1);
					}

					if (param.children.get(0).equals("pol")) {
						pol = parsePol(param);
						bodyPartMap.put(bodyPartIdentifier, pol);
					}
				}
			}

			String name = "P" + teamName + id;
			if (id == IMagmaConstants.UNKNOWN_PLAYER_NUMBER) {
				name = "P" + teamName + pol.toString();
			}
			PlayerPos perceptor = new PlayerPos("Player", pol, id, teamName, bodyPartMap, true, name);
			// we ignore visible players that have a position 0 since they are usually exploded players
			if (perceptor.getPosition().getNorm() > 0.00001) {
				map.put(perceptor.getName(), perceptor);
			}
		} catch (IndexOutOfBoundsException e) {
			throw new PerceptorConversionException("Malformed node: " + node.toString());
		}
	}

	/**
	 * Parse a symbol tree node into a Player object
	 *
	 * @param node Symbol tree node
	 * @throws PerceptorConversionException If the input string contains illegal
	 *         data which cannot be converted
	 */
	private void parseVisibleObject(SymbolNode node, Map<String, IPerceptor> map) throws PerceptorConversionException
	{
		try {
			assert node.children.size() == 2 : "Malformed node";
			assert node.children.get(1) instanceof SymbolNode : "Malformed node: " + node.toString();

			String name = (String) node.children.get(0);
			Vector3D pol = parsePol((SymbolNode) node.children.get(1));

			String type = name.startsWith("G") ? "Goalpost" : "Flag";
			VisibleObjectPerceptor perceptor = new VisibleObjectPerceptor(type, pol, true, 1.0, name);
			map.put(perceptor.getName(), perceptor);
		} catch (IndexOutOfBoundsException e) {
			throw new PerceptorConversionException("Malformed node: " + node.toString());
		}
	}

	/**
	 * Parse object coordinates
	 *
	 * @param node SymbolNode to parse
	 * @return Coordinates
	 * @throws PerceptorConversionException If the input string contains illegal
	 *         data which cannot be converted
	 */
	private Vector3D parsePol(SymbolNode node) throws PerceptorConversionException
	{
		try {
			if (node.children.size() != 4)
				throw new PerceptorConversionException("Malformed node: " + node.toString());
			if (!node.children.get(0).equals("pol"))
				throw new PerceptorConversionException("Expecting a pol object: " + node.toString());

			float val1 = parseFloat(node.children.get(1));
			float val2 = parseFloat(node.children.get(2));
			float val3 = parseFloat(node.children.get(3));

			return new Vector3D(val1, new Vector3D(Math.toRadians(val2), Math.toRadians(val3)));
		} catch (IndexOutOfBoundsException e) {
			throw new PerceptorConversionException("Malformed node: " + node.toString());

		} catch (NumberFormatException e) {
			// seems that the server sometimes sends NAN, in which case we ignore
			// the reading for position
		}
		return null;
	}

	/**
	 * Parse a symbol tree node into a Game State Perceptor object
	 *
	 * @param node Symbol tree node
	 * @return Game State Perceptor object
	 * @throws PerceptorConversionException If the input string contains illegal
	 *         data which cannot be converted
	 */
	private GameStatePerceptor parseGameState(SymbolNode node) throws PerceptorConversionException
	{
		float time = 0.0f;
		String playmode = "", team = "";
		int unum = 0;
		int scoreLeft = 0, scoreRight = 0;

		SymbolNode child = null;

		// Evaluate content
		for (int i = 1; i < node.children.size(); i++) {
			try {
				child = (SymbolNode) node.children.get(i);

				String type = (String) child.children.get(0);

				// Check sub-node type
				switch (type) {
				case "t":
					time = parseFloat(child.children.get(1));
					break;
				case "pm":
					playmode = (String) child.children.get(1);
					break;
				case "unum":
					unum = Integer.parseInt((String) child.children.get(1));
					break;
				case "team":
					team = (String) child.children.get(1);
					break;
				case "sl":
					scoreLeft = Integer.parseInt((String) child.children.get(1));
					break;
				case "sr":
					scoreRight = Integer.parseInt((String) child.children.get(1));
					break;
				default:
					throw new PerceptorConversionException(
							"Malformed GameState node, unknown sub-node: " + child.toString());
				}
			} catch (Exception e) {
				if (child != null)
					throw new PerceptorConversionException(
							"Malformed GameState node, conversion error: " + child.toString());

				throw new PerceptorConversionException("Malformed GameState node: child was null!");
			}
		}
		// we do not have sufficient information for team color, so team color
		// information is missing in Simspark!
		TeamColor color = TeamColor.UNKNOWN;
		return new GameStatePerceptor(time, playmode, team, color, unum, scoreLeft, scoreRight);
	}

	/**
	 * Parse a symbol tree node into an Agent State Perceptor object
	 *
	 * @param node Symbol tree node
	 * @return Agent State Perceptor object
	 * @throws PerceptorConversionException If the input string contains illegal
	 *         data which cannot be converted
	 */
	private AgentStatePerceptor parseAgentState(SymbolNode node) throws PerceptorConversionException
	{
		try {
			// Sanity checks
			if (!(node.children.get(1) instanceof SymbolNode))
				throw new PerceptorConversionException("Malformed Message: " + node.toString());
			if (!(node.children.get(2) instanceof SymbolNode))
				throw new PerceptorConversionException("Malformed Message: " + node.toString());

			/* Check content */
			SymbolNode temperatureNode = (SymbolNode) node.children.get(1);
			SymbolNode batteryNode = (SymbolNode) node.children.get(2);

			if (!temperatureNode.children.get(0).equals("temp"))
				throw new PerceptorConversionException("temperature expected: " + node.toString());
			if (!batteryNode.children.get(0).equals("battery"))
				throw new PerceptorConversionException("battery level expected: " + node.toString());

			return new AgentStatePerceptor(Integer.parseInt((String) temperatureNode.children.get(1)),
					Integer.parseInt((String) batteryNode.children.get(1)));
		} catch (IndexOutOfBoundsException e) {
			throw new PerceptorConversionException("Malformed node: " + node.toString());
		}
	}

	/**
	 * Parse a symbol tree node into a Hear Perceptor object
	 *
	 * @param node Symbol tree node
	 * @return Hear Perceptor object
	 * @throws PerceptorConversionException If the input string contains illegal
	 *         data which cannot be converted
	 */
	private HearPerceptor parseHear(SymbolNode node) throws PerceptorConversionException
	{
		float time;
		String message = "";
		String target;
		String team;

		// Sanity checks
		if (node.children.size() < 4)
			throw new PerceptorConversionException("Malformed hear node: " + node.toString());

		try {
			team = (String) node.children.get(1);
			time = parseFloat(node.children.get(2));
			target = (String) node.children.get(3);
		} catch (Exception e) {
			throw new PerceptorConversionException("Malformed hear node, conversion error: " + node.toString());
		}

		// Concatenate following nodes
		int messageStartID = 4;
		for (int i = messageStartID; i < node.children.size(); i++) {
			if (i > messageStartID)
				message += " " + node.children.get(i);
			else
				message += node.children.get(i);
		}

		return new HearPerceptor(time, team, target, message);
	}

	/**
	 * Parse a symbol tree node into a Time Perceptor object
	 *
	 * @param node Symbol tree node
	 * @return Time Perceptor object
	 * @throws PerceptorConversionException If the input string contains illegal
	 *         data which cannot be converted
	 */
	private TimePerceptor parseTime(SymbolNode node) throws PerceptorConversionException
	{
		// Sanity check
		if (node.children.size() != 2 || !(node.children.get(1) instanceof SymbolNode))
			throw new PerceptorConversionException("Malformed time node: " + node.toString());

		// Sub-node sanity check
		SymbolNode timeNode = (SymbolNode) node.children.get(1);
		if (timeNode.children.size() != 2 || timeNode.children.get(1).equals("now"))
			throw new PerceptorConversionException("Malformed time sub-node: " + timeNode.toString());

		try {
			// Sanity checks
			float time = parseFloat(timeNode.children.get(1));

			return new TimePerceptor(time);
		} catch (Exception e) {
			throw new PerceptorConversionException("Malformed time node, conversion error: " + node.toString());
		}
	}

	@Override
	public String getErrorString(byte[] message)
	{
		return new String(message);
	}
}
