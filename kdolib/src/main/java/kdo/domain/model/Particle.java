/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain.model;

import kdo.domain.IOptimizationProblem;
import kdo.domain.IPSOIndividuum;

public class Particle extends Individuum implements IPSOIndividuum
{
	protected float[] velocity;

	private float[] bestPostion;

	protected float bestValue;

	protected float[] bestNeighbor;

	protected float bestNeighborValue;

	protected float[][] area;

	public Particle(IOptimizationProblem problem, float[] state)
	{
		super(problem, state);
		area = problem.getArea();
	}

	@Override
	public float[] getVelocity()
	{
		return velocity;
	}

	@Override
	public void initializeVelocity()
	{
		velocity = new float[getState().length];

		for (int i = 0; i < velocity.length; i++) {
			velocity[i] = (float) (area[i][0] + (Math.random() * (area[i][1] - area[i][0]))) * 0.1f;
		}
	}

	@Override
	public float[] getCurrentPosition()
	{
		return getState();
	}

	@Override
	public float[] getBestPosition()
	{
		return bestPostion;
	}

	@Override
	public float getCurrentValue()
	{
		return utility;
	}

	@Override
	public float getBestValue()
	{
		return bestValue;
	}

	@Override
	public float[] getBestNeighborPosition()
	{
		return bestNeighbor;
	}

	@Override
	public float getBestNeighborValue()
	{
		return bestNeighborValue;
	}

	@Override
	public void updateBestNeighbor(float[] bestNeighbor, float bestValue)
	{
		if (this.bestNeighbor == null || isBetter(bestValue, this.bestNeighborValue)) {
			this.bestNeighborValue = bestValue;
			this.bestNeighbor = bestNeighbor.clone();
		}
	}

	@Override
	public void updateBest()
	{
		utility = calculateUtility();
		if (bestPostion == null || isBetter(utility, bestValue)) {
			bestValue = utility;
			bestPostion = getState().clone();
		}
	}

	private boolean isBetter(float first, float second)
	{
		return (isMaximize() && first > second) || (!isMaximize() && first < second);
	}

	private boolean isMaximize()
	{
		return ((IOptimizationProblem) problem).isMaximize();
	}

	@Override
	public void updateVelocity(int iteration, int maxIterations)
	{
		float discount = 1.0f - (((float) iteration) / maxIterations) * (1.0f - 0.0f);
		float adjust = 1f;

		for (int i = 0; i < velocity.length; i++) {
			velocity[i] = (float) (discount * velocity[i] + adjust * Math.random() * (bestPostion[i] - getState()[i]) +
								   adjust * Math.random() * (bestNeighbor[i] - getState()[i]));
		}
	}

	@Override
	public void updatePosition()
	{
		for (int i = 0; i < velocity.length; i++) {
			state[i] = state[i] + velocity[i];
			if (state[i] < area[i][0]) {
				state[i] = area[i][0];
			} else if (state[i] > area[i][1]) {
				state[i] = area[i][1];
			}
		}
	}

	@Override
	public String toString()
	{
		String result = String.format("(%5.2f)", utility);
		result += " state: " + addArray(state);
		if (velocity != null) {
			result += " vel: " + addArray(velocity);
		}
		if (bestPostion != null) {
			result += " bestOwn: " + addArray(bestPostion);
			result += " bestOther: " + addArray(bestNeighbor);
		}
		result += "\n";
		return result;
	}

	public String addArray(float[] value)
	{
		String result = "";
		for (int i = 0; i < value.length; i++) {
			result += String.format("%6.2f/", value[i]);
		}
		return result;
	}
}
