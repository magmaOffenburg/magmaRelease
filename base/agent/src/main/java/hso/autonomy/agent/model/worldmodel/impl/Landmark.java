/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.impl;

import static hso.autonomy.util.geometry.VectorUtils.mirror;

import hso.autonomy.agent.model.worldmodel.ILandmark;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeature;
import hso.autonomy.util.geometry.VectorUtils.Mirror;
import hso.autonomy.util.misc.FuzzyCompare;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Non movable orientation point on the field.
 *
 * @author Klaus Dorer, Stefan Glaser
 */
public class Landmark extends VisibleObject implements ILandmark, IPointFeature
{
	/** The type information associated to this landmark. */
	private final String type;

	/** the known position of the landmark (global coordinates) */
	private final Vector3D knownPosition;

	/**
	 * Constructor
	 *
	 * @param name the unique name of the Landmark
	 * @param type the type information associated to this feature
	 * @param knownPosition the known position (global coordinates)
	 */
	public Landmark(String name, String type, Vector3D knownPosition)
	{
		super(name);

		this.type = type;
		this.knownPosition = knownPosition;
	}

	/**
	 * Copy constructor.
	 *
	 * @param other the landmark to copy
	 * @param mirror true, if the known position should be mirrored on XY, false if not
	 */
	public Landmark(ILandmark other, boolean mirror)
	{
		this(other.getName(), other.getType(),
				mirror ? mirror(other.getKnownPosition(), Mirror.XY) : other.getKnownPosition());
	}

	@Override
	public String getType()
	{
		return type;
	}

	@Override
	public Vector3D getKnownPosition()
	{
		return knownPosition;
	}

	/**
	 * @param localPosition the position as observed in the root body system
	 */
	public void updateLocalPosition(Vector3D localPosition)
	{
		this.localPosition = localPosition;
		setVisible(true);
	}

	@Override
	public Vector3D getPosition()
	{
		// TODO: a delegate to getKnownPosition is not necessarily good here (was
		// introduced because of IFOs). We avoid obstacles, even if we see them
		// somewhere else - in case of coming from the outside to a goal when the
		// ball lies somewhere very close to a goal post, this could lead to
		// unnecessary obstacle avoidance, even if the ball is between us and the
		// goal post. maybe:
		// if(isVisible()){ return position; } else {
		return getKnownPosition();
		// }
	}

	public Vector3D getGlobalPosition()
	{
		return super.getPosition();
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Landmark)) {
			return false;
		}
		Landmark other = (Landmark) o;
		if (!super.equals(other)) {
			return false;
		}
		return FuzzyCompare.eq(knownPosition, other.knownPosition, 0.00001f);
	}

	@Override
	public String toString()
	{
		return "Landmark [knownPosition=" + knownPosition + " " + super.toString() + "]";
	}
}