/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.logging;

import hso.autonomy.util.geometry.Arc2D;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.Circle2D;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Polygon;
import hso.autonomy.util.geometry.PositionedText;
import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class DrawingMap
{
	private Map<String, Drawing> drawings = new LinkedHashMap<>();

	private void put(String name, Color color, Object area)
	{
		drawings.put(name, new Drawing(area, color));
	}

	public void draw(String name, Color color, Area2D.Float area)
	{
		put(name, color, area);
	}

	public void draw(String name, Color color, Circle2D circle)
	{
		put(name, color, circle);
	}

	public void draw(String name, Color color, Arc2D arc)
	{
		put(name, color, arc);
	}

	public void draw(String name, Color color, Vector2D vector)
	{
		put(name, color, vector);
	}

	public void draw(String name, Color color, IPose3D pose)
	{
		put(name, color, pose);
	}

	public void draw(String name, Color color, Vector3D vector)
	{
		put(name, color, vector);
	}

	public void draw(String name, Color color, Vector3D[] vectors)
	{
		put(name, color, vectors);
	}

	public void draw(String name, Color color, Polygon polygon)
	{
		put(name, color, polygon);
	}

	public void draw(String name, Color color, SubLine line)
	{
		put(name, color, line);
	}

	public void draw(String name, Color color, String text, Vector2D position, float thickness)
	{
		put(name, color, new PositionedText(text, position, color, thickness));
	}

	public void remove(String name)
	{
		drawings.remove(name);
	}

	public Map<String, Drawing> getDrawingData()
	{
		return drawings;
	}

	public class Drawing
	{
		public final Object data;

		public final Color color;

		private Drawing(Object data, Color color)
		{
			this.data = data;
			this.color = color;
		}
	}
}
