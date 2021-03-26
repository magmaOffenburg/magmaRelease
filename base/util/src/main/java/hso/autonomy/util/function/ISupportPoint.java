/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

/**
 * Interface for SupportPoints
 *
 * @author Stefan Glaser
 */
public interface ISupportPoint extends IPoint {
	/**
	 * @return the tangential sub-support point before the actual support point
	 */
	IPoint getTangentPointBefore();

	/**
	 * @return the tangential sub-support point after the actual support point
	 */
	IPoint getTangentPointAfter();

	boolean equalsX(SupportPoint other);

	boolean equalsY(SupportPoint other);
}
