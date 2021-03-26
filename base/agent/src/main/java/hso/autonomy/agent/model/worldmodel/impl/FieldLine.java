/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.impl;

import static hso.autonomy.util.geometry.VectorUtils.mirror;

import hso.autonomy.agent.model.worldmodel.IFieldLine;
import hso.autonomy.agent.model.worldmodel.localizer.ILineFeature;
import hso.autonomy.util.geometry.VectorUtils.Mirror;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * This class represents a simple field line with a start and end point.
 *
 * @author Stefan Glaser
 */
public class FieldLine extends VisibleObject implements IFieldLine, ILineFeature
{
	/** The line feature type information. */
	private final String type;

	/** The first position of the line relative to our local root body system. */
	private Vector3D localPosition1;

	/** The second position of the line relative to our local root body system. */
	private Vector3D localPosition2;

	/** The first position of the line relative to the global system. */
	private Vector3D position1;

	/** The second position of the line relative to the global system. */
	private Vector3D position2;

	/** The first known position of the line feature. */
	private final Vector3D knownPosition1;

	/** The second known position of the line feature. */
	private final Vector3D knownPosition2;

	public FieldLine(String name, String type, Vector3D knownPosition1, Vector3D knownPosition2)
	{
		super(name);

		this.type = type;
		this.localPosition1 = Vector3D.ZERO;
		this.localPosition2 = Vector3D.ZERO;
		this.position1 = Vector3D.ZERO;
		this.position2 = Vector3D.ZERO;
		this.knownPosition1 = knownPosition1;
		this.knownPosition2 = knownPosition2;
	}

	/**
	 * Copy constructor.
	 *
	 * @param other the field line to copy
	 * @param mirror true, if the known positions should be mirrored on XY, false if not
	 */
	public FieldLine(IFieldLine other, boolean mirror)
	{
		this(other.getName(), other.getType(),
				mirror ? mirror(other.getKnownPosition1(), Mirror.XY) : other.getKnownPosition1(),
				mirror ? mirror(other.getKnownPosition2(), Mirror.XY) : other.getKnownPosition2());
	}

	public FieldLine(String name, String type, Vector3D[] knownPositions)
	{
		this(name, type, knownPositions[0], knownPositions[1]);
	}

	@Override
	public String getType()
	{
		return type;
	}

	@Override
	public Vector3D getPosition1()
	{
		return position1;
	}

	@Override
	public Vector3D getPosition2()
	{
		return position2;
	}

	@Override
	public Vector3D getLocalPosition1()
	{
		return localPosition1;
	}

	@Override
	public Vector3D getLocalPosition2()
	{
		return localPosition2;
	}

	@Override
	public Vector3D getKnownPosition1()
	{
		return knownPosition1;
	}

	@Override
	public Vector3D getKnownPosition2()
	{
		return knownPosition2;
	}

	/**
	 * Update the local and global positions of this field line.
	 *
	 * @param localPos1 the first local position
	 * @param localPos2 the second local position
	 * @param globalPos1 the first global position
	 * @param globalPos2 the second global position
	 * @param time the time of observation
	 */
	public void updatePositions(
			Vector3D localPos1, Vector3D localPos2, Vector3D globalPos1, Vector3D globalPos2, float time)
	{
		localPosition1 = localPos1;
		localPosition2 = localPos2;
		position1 = globalPos1;
		position2 = globalPos2;

		Vector3D newGlobalPos;
		Vector3D newLocalPos;
		if (globalPos1 != null && globalPos2 != null) {
			newLocalPos = localPos1.add(localPos2).scalarMultiply(.5);
			newGlobalPos = globalPos1.add(globalPos2).scalarMultiply(.5);
		} else if (globalPos1 != null) {
			newLocalPos = localPos1;
			newGlobalPos = globalPos1;
		} else {
			newLocalPos = localPos2;
			newGlobalPos = globalPos2;
		}

		super.updateFromVision(seenPosition, newLocalPos, newGlobalPos, time);
	}
}
