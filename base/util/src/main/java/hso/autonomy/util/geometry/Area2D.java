/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import java.io.Serializable;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * An Area in 2D space. Usually used to define a rectangular area.
 *
 * @author Stefan Glaser
 */
public class Area2D
{
	public static class Int implements Serializable
	{
		private int minX;

		private int maxX;

		private int minY;

		private int maxY;

		public Int(int minX, int maxX, int minY, int maxY)
		{
			if (minX > maxX) {
				int tmp = maxX;
				maxX = minX;
				minX = tmp;
			}

			if (minY > maxY) {
				int tmp = maxY;
				maxY = minY;
				minY = tmp;
			}

			this.minX = minX;
			this.maxX = maxX;
			this.minY = minY;
			this.maxY = maxY;
		}

		public Int applyBorder(int borderX, int borderY)
		{
			return new Int(minX + borderX, maxX - borderX, minY + borderY, maxX - borderY);
		}

		public Int applyBorder(int border)
		{
			return applyBorder(border, border);
		}

		public int getMinX()
		{
			return minX;
		}

		public int getMaxX()
		{
			return maxX;
		}

		public int getMinY()
		{
			return minY;
		}

		public int getMaxY()
		{
			return maxY;
		}

		public int getWidth()
		{
			return maxX - minX;
		}

		public int getHeight()
		{
			return maxY - minY;
		}

		public Vector2D getTopLeft()
		{
			return new Vector2D(minX, minY);
		}

		public Vector2D getTopRight()
		{
			return new Vector2D(maxX, minY);
		}

		public Vector2D getBottomLeft()
		{
			return new Vector2D(minX, maxY);
		}

		public Vector2D getBottomRight()
		{
			return new Vector2D(maxX, maxY);
		}

		public Vector2D getCenter()
		{
			return new Vector2D((float) (minX + getWidth() / 2.0), (float) (minY + getHeight() / 2.0));
		}

		public Float getLeftHalf()
		{
			return new Float(minX, maxX - getWidth() / 2, minY, maxY);
		}

		public Float getRightHalf()
		{
			return new Float(minX + getWidth() / 2, maxX, minY, maxY);
		}

		public boolean contains(Vector3D v)
		{
			return contains((int) v.getX(), (int) v.getY());
		}

		public boolean contains(int x, int y)
		{
			return x >= minX && x < maxX && y >= minY && y < maxY;
		}

		public boolean containsX(int x)
		{
			return x >= minX && x < maxX;
		}

		public boolean containsY(int y)
		{
			return y >= minY && y < maxY;
		}

		public boolean equals(int minX, int maxX, int minY, int maxY)
		{
			return this.minX == minX && this.maxX == maxX && this.minY == minY && this.maxY == maxY;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (super.equals(obj)) {
				return true;
			}

			if (obj instanceof Area2D.Int) {
				Area2D.Int other = (Area2D.Int) obj;
				return other.minX == minX && other.maxX == maxX && other.minY == minY && other.maxY == maxY;
			}

			return false;
		}

		@Override
		public String toString()
		{
			return "[minX=" + minX + ",maxX=" + maxX + ",minY=" + minY + ",maxY=" + maxY + "]";
		}
	}

	public static class Float implements Serializable
	{
		private float minX;

		private float maxX;

		private float minY;

		private float maxY;

		public Float(float minX, float maxX, float minY, float maxY)
		{
			if (minX > maxX) {
				float tmp = maxX;
				maxX = minX;
				minX = tmp;
			}

			if (minY > maxY) {
				float tmp = maxY;
				maxY = minY;
				minY = tmp;
			}

			this.minX = minX;
			this.maxX = maxX;
			this.minY = minY;
			this.maxY = maxY;
		}

		public Float(double minX, double maxX, double minY, double maxY)
		{
			this((float) minX, (float) maxX, (float) minY, (float) maxY);
		}

		public Float applyBorder(float borderX, float borderY)
		{
			return new Float(minX + borderX, maxX - borderX, minY + borderY, maxY - borderY);
		}

		public Float applyBorder(float border)
		{
			return applyBorder(border, border);
		}

		public float getMaxX()
		{
			return maxX;
		}

		public float getMinX()
		{
			return minX;
		}

		public float getMinY()
		{
			return minY;
		}

		public float getMaxY()
		{
			return maxY;
		}

		public float getWidth()
		{
			return maxX - minX;
		}

		public float getHeight()
		{
			return maxY - minY;
		}

		public Vector2D getTopLeft()
		{
			return new Vector2D(minX, minY);
		}

		public Vector2D getTopRight()
		{
			return new Vector2D(maxX, minY);
		}

		public Vector2D getBottomLeft()
		{
			return new Vector2D(minX, maxY);
		}

		public Vector2D getBottomRight()
		{
			return new Vector2D(maxX, maxY);
		}

		public Vector2D getCenter()
		{
			return new Vector2D((float) (minX + getWidth() / 2.0), (float) (minY + getHeight() / 2.0));
		}

		public Float getLeftHalf()
		{
			return new Float(minX, maxX - getWidth() / 2, minY, maxY);
		}

		public Float getRightHalf()
		{
			return new Float(minX + getWidth() / 2, maxX, minY, maxY);
		}

		public boolean contains(Vector2D v)
		{
			return contains((float) v.getX(), (float) v.getY());
		}

		public boolean contains(Vector3D v)
		{
			return contains((float) v.getX(), (float) v.getY());
		}

		public boolean contains(float x, float y)
		{
			return x >= minX && x < maxX && y >= minY && y < maxY;
		}

		public boolean containsX(float x)
		{
			return x >= minX && x < maxX;
		}

		public boolean containsY(float y)
		{
			return y >= minY && y < maxY;
		}

		public boolean equals(float minX, float maxX, float minY, float maxY)
		{
			return this.minX == minX && this.maxX == maxX && this.minY == minY && this.maxY == maxY;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (super.equals(obj)) {
				return true;
			}

			if (obj instanceof Area2D.Float) {
				Area2D.Float other = (Area2D.Float) obj;
				return other.minX == minX && other.maxX == maxX && other.minY == minY && other.maxY == maxY;
			}

			return false;
		}

		@Override
		public String toString()
		{
			return "[minX=" + minX + ",maxX=" + maxX + ",minY=" + minY + ",maxY=" + maxY + "]";
		}
	}
}
