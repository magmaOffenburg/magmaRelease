/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.roboviz;

import static magma.util.roboviz.RoboVizBufferUtil.concat;
import static magma.util.roboviz.RoboVizBufferUtil.newAgentAnnotation;
import static magma.util.roboviz.RoboVizBufferUtil.newAnnotation;
import static magma.util.roboviz.RoboVizBufferUtil.newBufferSwap;
import static magma.util.roboviz.RoboVizBufferUtil.newCircle;
import static magma.util.roboviz.RoboVizBufferUtil.newLine;
import static magma.util.roboviz.RoboVizBufferUtil.newPoint;
import static magma.util.roboviz.RoboVizBufferUtil.newPolygon;
import static magma.util.roboviz.RoboVizBufferUtil.newSelectAgent;
import static magma.util.roboviz.RoboVizBufferUtil.newSphere;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Polygon;
import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.function.Consumer;
import magma.common.spark.PlaySide;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Sends draw commands to RoboViz for visual debugging with shapes and text.
 * @see <a href="https://sites.google.com/site/umroboviz/drawing-api">Offical
 *      RoboViz docs</a>
 */
public class RoboVizDraw
{
	public static final String DEFAULT_HOST = "localhost";

	public static final int DEFAULT_PORT = 32769;

	private DatagramSocket socket;

	private InetAddress address;

	private boolean enabled = false;

	private String server = DEFAULT_HOST;

	private int port = DEFAULT_PORT;

	private PlaySide playSide = PlaySide.LEFT;

	private int agentNum = 1;

	private String customPrefix;

	private boolean inSingleDrawing = false;

	public RoboVizDraw()
	{
	}

	public RoboVizDraw(RoboVizParameters params)
	{
		if (params != null) {
			enabled = params.isEnabled();
			server = params.getServer();
			port = params.getPort();
			agentNum = params.getAgentNum();
		}
	}

	/**
	 * Sets a custom prefix to use for all drawings (by default it's assumed that
	 * drawings are made by an agent and initialize()'s params are used,
	 * resulting in a prefix like "agent8left.")
	 * @param prefix The custom prefix name, with a dot at the end.
	 */
	public void setCustomPrefix(String prefix)
	{
		customPrefix = prefix;
	}

	public void setPlaySide(PlaySide playSide)
	{
		this.playSide = playSide;
	}

	private void connect()
	{
		if (enabled && socket == null) {
			try {
				socket = new DatagramSocket();
				address = InetAddress.getByName(server);
			} catch (SocketException | UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

	/** Sends a packet including the buffer swap */
	private void sendAndSwap(byte[] buf, String group)
	{
		if (enabled) {
			byte[] command = buf;
			if (!inSingleDrawing) {
				command = concat(buf, newBufferSwap(group));
			}
			send(command);
		}
	}

	public void swapBuffer(String group)
	{
		send(newBufferSwap(group));
	}

	private void send(byte[] buf)
	{
		if (enabled) {
			connect();
			try {
				socket.send(new DatagramPacket(buf, buf.length, address, port));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void drawCircle(String group, Vector3D center, float radius, float thickness, Color color)
	{
		if (enabled) {
			drawCircle(group, (float) center.getX(), (float) center.getY(), radius, thickness, color);
		}
	}

	public void drawCircle(String group, float centerX, float centerY, float radius, float thickness, Color color)
	{
		if (enabled) {
			group = prefixGroup(group);
			sendAndSwap(newCircle(new float[] {centerX, centerY}, radius, thickness, color, group), group);
		}
	}

	public void drawLine(String group, Vector3D start, Vector3D end, float thickness, Color color)
	{
		if (enabled) {
			group = prefixGroup(group);
			sendAndSwap(newLine(start, end, thickness, color, group), group);
		}
	}

	public void drawLine(
			String group, float x1, float y1, float z1, float x2, float y2, float z2, float thickness, Color color)
	{
		if (enabled) {
			group = prefixGroup(group);
			sendAndSwap(newLine(new float[] {x1, y1, z1}, new float[] {x2, y2, z2}, thickness, color, group), group);
		}
	}

	public void drawPoint(String group, Vector3D point, float size, Color color)
	{
		if (enabled) {
			drawPoint(group, (float) point.getX(), (float) point.getY(), (float) point.getZ(), size, color);
		}
	}

	public void drawPoint(String group, float x, float y, float z, float size, Color color)
	{
		if (enabled) {
			group = prefixGroup(group);
			sendAndSwap(newPoint(new float[] {x, y, z}, size, color, group), group);
		}
	}

	public void drawPose(String group, IPose2D pose, float size, Color color)
	{
		if (enabled) {
			drawLine(group + "-l", pose.getPosition3D(), pose.applyTo(new Vector3D(0.5, 0, 0)), size * 0.5f, color);
			drawPoint(group + "-p", pose.getPosition3D(), size, color);
		}
	}

	public void drawSphere(String group, Vector3D center, float radius, Color color)
	{
		if (enabled) {
			drawSphere(group, (float) center.getX(), (float) center.getY(), (float) center.getZ(), radius, color);
		}
	}

	public void drawSphere(String group, float centerX, float centerY, float centerZ, float radius, Color color)
	{
		if (enabled) {
			group = prefixGroup(group);
			sendAndSwap(newSphere(new float[] {centerX, centerY, centerZ}, radius, color, group), group);
		}
	}

	public void drawPolygon(String group, Polygon poly, float thickness, Color color)
	{
		drawPolygon(group, poly.getPoints3D(), thickness, color);
	}

	public void drawPolygon(String group, List<Vector3D> vertices, float thickness, Color color)
	{
		if (enabled && vertices.size() > 1) {
			group = prefixGroup(group);

			byte[][] lines = new byte[vertices.size()][64];
			Vector3D a = vertices.get(0);
			for (int i = 1; i < vertices.size(); i++) {
				Vector3D b = vertices.get(i);
				lines[i - 1] = newLine(a, b, thickness, color, group);
				a = b;
			}
			lines[vertices.size() - 1] = newLine(a, vertices.get(0), thickness, color, group);
			sendAndSwap(concat(lines), group);
		}
	}

	public void drawFilledPolygon(String group, List<Vector3D> vertices, Color color)
	{
		if (enabled) {
			float[][] floatVertices = new float[vertices.size()][3];
			for (int i = 0; i < vertices.size(); i++) {
				Vector3D vertex = vertices.get(i);
				floatVertices[i] = new float[] {(float) vertex.getX(), (float) vertex.getY(), (float) vertex.getZ()};
			}
			group = prefixGroup(group);
			sendAndSwap(newPolygon(floatVertices, color, group), group);
		}
	}

	public void drawAnnotation(String group, Object text, Vector3D position, Color color)
	{
		if (enabled) {
			drawAnnotation(
					group, text, (float) position.getX(), (float) position.getY(), (float) position.getZ(), color);
		}
	}

	public void drawAnnotation(String group, Object text, float x, float y, float z, Color color)
	{
		if (enabled) {
			group = prefixGroup(group);
			String message = (text == null) ? null : text.toString();
			sendAndSwap(newAnnotation(message, new float[] {x, y, z}, color, group), group);
		}
	}

	/**
	 * Draws an annotation to the current agent. Requires a valid agentNum to be
	 * set via initialize().
	 */
	public void drawAgentAnnotation(Object text)
	{
		drawAgentAnnotation(text, Color.BLACK);
	}

	/**
	 * Draws an annotation to the current agent. Requires a valid agentNum to be
	 * set via initialize().
	 */
	public void drawAgentAnnotation(Object text, Color color)
	{
		if (enabled && agentNum > 0) {
			drawAgentAnnotation(text, playSide, agentNum, color);
		}
	}

	public void drawAgentAnnotation(Object text, PlaySide playSide, int agentNum, Color color)
	{
		if (enabled) {
			String message = null;
			if (text instanceof Float || text instanceof Double) {
				message = String.format("%.02f", text);
			} else if (text != null) {
				message = text.toString();
			}

			send(newAgentAnnotation(message, playSide == PlaySide.LEFT, agentNum, color));
		}
	}

	public void selectAgent()
	{
		send(newSelectAgent(playSide == PlaySide.LEFT, agentNum));
	}

	public void selectAgent(boolean leftTeam, int agentNum)
	{
		send(newSelectAgent(leftTeam, agentNum));
	}

	/**
	 * Prefixes a group string with a unique player ID (agent, agentNum,
	 * playSide) or a previously specified custom prefix.
	 */
	private String prefixGroup(String group)
	{
		if (customPrefix == null) {
			if (agentNum <= 0) {
				return group;
			}
			return "agent" + agentNum + playSide.getName() + "." + group;
		} else {
			return customPrefix + group;
		}
	}

	public void drawMeterMarkers(boolean left, Color color, int xOffset, Angle rotation, int meters)
	{
		String set = "meters" + (left ? "Left" : "Right");
		for (int i = 0; i < meters; i++) {
			int x = left ? i - meters : i + 1;
			Vector3D vector = new Vector3D(x + xOffset, 0, 0);
			Rotation rotation3d =
					new Rotation(new Vector3D(0, 0, 1), rotation.radians(), RotationConvention.VECTOR_OPERATOR);
			vector = rotation3d.applyTo(vector);

			String meter = Integer.toString(left ? meters - i : i + 1);
			send(newAnnotation(meter, new float[] {(float) vector.getX(), (float) vector.getY(), (float) vector.getZ()},
					color, set));
		}
		send(newBufferSwap(set));
	}

	public void drawMeterMarkers(boolean left, Color color, int xOffset, int meters)
	{
		drawMeterMarkers(left, color, xOffset, Angle.ZERO, meters);
	}

	public void drawArea(String group, Area2D.Float area, float thickness, Color color)
	{
		group = prefixGroup(group);

		float[] p1 = new float[] {area.getMinX(), area.getMinY(), 0};
		float[] p2 = new float[] {area.getMinX(), area.getMaxY(), 0};
		float[] p3 = new float[] {area.getMaxX(), area.getMaxY(), 0};
		float[] p4 = new float[] {area.getMaxX(), area.getMinY(), 0};
		drawArea(group, thickness, color, p1, p2, p3, p4);
	}

	private void drawArea(String group, float thickness, Color color, float[] p1, float[] p2, float[] p3, float[] p4)
	{
		byte[] line1 = newLine(p1, p2, thickness, color, group);
		byte[] line2 = newLine(p2, p3, thickness, color, group);
		byte[] line3 = newLine(p3, p4, thickness, color, group);
		byte[] line4 = newLine(p4, p1, thickness, color, group);

		sendAndSwap(concat(line1, line2, line3, line4), group);
	}

	/**
	 * Sends the buffer swap only after all draw commands issued in
	 * <code>draw</code> are done, so they appear as a single drawing in RoboViz'
	 * drawings panel.
	 */
	public void asSingleDrawing(String group, Consumer<RoboVizDraw> draw)
	{
		inSingleDrawing = true;

		draw.accept(this);

		inSingleDrawing = false;
		swapBuffer(prefixGroup(group));
	}
}
