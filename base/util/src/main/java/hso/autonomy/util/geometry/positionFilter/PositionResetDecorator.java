/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry.positionFilter;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author kdorer
 *
 */
public class PositionResetDecorator extends BaseFilter
{
	IPositionFilter decoratee;

	/** distance at which to reset the filter */
	private double resetDistance;

	public PositionResetDecorator(IPositionFilter decoratee, double resetDistance)
	{
		super();
		this.decoratee = decoratee;
		this.resetDistance = resetDistance;
	}

	@Override
	public Vector3D filterPosition(Vector3D newPosition, Vector3D oldPosition, Vector3D speed)
	{
		if (Vector3D.distance(newPosition, oldPosition) > resetDistance) {
			decoratee.reset();
		}

		return decoratee.filterPosition(newPosition, oldPosition, speed);
	}
}
