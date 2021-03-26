/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.impl;

import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.util.geometry.Angle;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import magma.agent.model.thoughtmodel.IKickPositionProfiler;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.KickPositionEstimation;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public abstract class KickPositionProfilerBase implements IKickPositionProfiler
{
	protected final IRoboCupThoughtModel thoughtModel;

	protected final IRoboCupWorldModel worldModel;

	private boolean refreshProfile;

	private SortedSet<KickPositionEstimation> evaluatedPositions;

	private Vector3D intendedKickPosition;

	public KickPositionProfilerBase(IRoboCupThoughtModel thoughtModel)
	{
		this.thoughtModel = thoughtModel;
		worldModel = thoughtModel.getWorldModel();
		evaluatedPositions = Collections.emptySortedSet();
		intendedKickPosition = Vector3D.ZERO;
	}

	private void evaluatePositionsIfNecessary()
	{
		if (refreshProfile) {
			evaluatedPositions = evaluatePositions();
			refreshProfile = false;
			intendedKickPosition = evaluatedPositions.first().position;
		}
	}

	protected abstract SortedSet<KickPositionEstimation> evaluatePositions();

	@Override
	public SortedSet<KickPositionEstimation> getEvaluatedPositions()
	{
		evaluatePositionsIfNecessary();
		return Collections.unmodifiableSortedSet(new TreeSet<>(evaluatedPositions));
	}

	@Override
	public Angle getIntendedKickDirection()
	{
		evaluatePositionsIfNecessary();

		IMoveableObject ball = thoughtModel.getWorldModel().getBall();
		return ball.getDirectionTo(intendedKickPosition);
	}

	@Override
	public double getIntendedKickDistance()
	{
		evaluatePositionsIfNecessary();

		IMoveableObject ball = thoughtModel.getWorldModel().getBall();
		return ball.getDistanceToXY(intendedKickPosition);
	}

	@Override
	public Vector3D getIntendedKickPosition()
	{
		evaluatePositionsIfNecessary();

		return intendedKickPosition;
	}

	@Override
	public void resetProfile()
	{
		refreshProfile = true;
	}
}