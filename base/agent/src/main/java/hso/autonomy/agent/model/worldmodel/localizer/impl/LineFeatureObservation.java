/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer.impl;

import hso.autonomy.agent.model.worldmodel.localizer.ILineFeature;
import hso.autonomy.agent.model.worldmodel.localizer.ILineFeatureObservation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Class for holding a (possibly unlabeled) line feature observation, which can be used for localization.
 *
 * @author Stefan Glaser
 */
public class LineFeatureObservation extends FeatureObservationBase implements ILineFeatureObservation
{
	/** The first observed position on the line. */
	private Vector3D position1;

	/** The second observed position of the line. */
	private Vector3D position2;

	/**
	 * Creates a new instance with the two positions, type "line" and no name.
	 *
	 * @param time the time of this observation
	 * @param type the observed feature type information
	 * @param position1 - the first observed position of the line
	 * @param position2 - the second observed position of the line
	 * @param hasDepth true, if the position contains full 3D information (including depth), false if it only contains
	 * directional information
	 */
	public LineFeatureObservation(double time, String type, Vector3D position1, Vector3D position2, boolean hasDepth)
	{
		this(time, type, position1, position2, hasDepth, null);
	}

	/**
	 * Creates a new instance for the given name, with the two positions and type "line".
	 *
	 * @param time the time of this observation
	 * @param type the observed feature type information
	 * @param position1 - the first observed position of the line
	 * @param position2 - the second observed position of the line
	 * @param hasDepth true, if the position contains full 3D information (including depth), false if it only contains
	 * directional information
	 * @param name the unique name of the line observation, corresponding to a known {@link ILineFeature}.
	 */
	public LineFeatureObservation(
			double time, String type, Vector3D position1, Vector3D position2, boolean hasDepth, String name)
	{
		super(time, type, hasDepth, name);

		this.position1 = position1;
		this.position2 = position2;
	}

	@Override
	public Vector3D getObservedPosition1()
	{
		return position1;
	}

	@Override
	public Vector3D getObservedPosition2()
	{
		return position2;
	}

	@Override
	public void assignDepthInfo(Vector3D position1, Vector3D position2)
	{
		this.position1 = position1;
		this.position2 = position2;
		this.hasDepth = true;
	}

	@Override
	public void assign(String name, boolean swapEnds)
	{
		super.assign(name);

		if (swapEnds) {
			Vector3D tmp = position1;
			position1 = position2;
			position2 = tmp;
		}
	}
}
