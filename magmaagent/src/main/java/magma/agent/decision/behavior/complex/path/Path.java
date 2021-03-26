/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.path;

import hso.autonomy.util.geometry.Pose2D;
import java.util.ArrayList;
import java.util.List;

/**
 * represents a calculated geometric path
 *
 * @author Stefan Grossmann
 */
public class Path
{
	/** path represented by list of parts (PathBase) */
	public List<PathBase> pathParts;

	public Path()
	{
		pathParts = new ArrayList<>();
	}

	/**
	 * copy constructor
	 * @param copy: the path that has to be copied
	 */
	public Path(Path copy)
	{
		pathParts = new ArrayList<>();
		pathParts.addAll(copy.pathParts);
	}

	/**
	 * get method for a specified path part i
	 * @param i: the part that wants to be returned i=0 is the first(current)
	 *        path part
	 * @return the wanted path part
	 */
	public PathBase get(int i)
	{
		if (pathParts.size() > i) {
			return pathParts.get(pathParts.size() - (i + 1));
		} else {
			return null;
		}
	}

	/**
	 * calculated path costs over all path parts
	 * @return sum of each path part cost
	 */
	public float getPathCost()
	{
		float costs = 0;
		for (PathBase part : pathParts) {
			costs += part.getCost();
		}
		return costs;
	}

	/**
	 * adds a path part to the list pathParts
	 * @param part that wants to be added
	 */
	public void add(PathBase part)
	{
		pathParts.add(part);
	}

	/**
	 * returns all parts of the path
	 * @return list of all path parts
	 */
	public List<PathBase> getPathParts()
	{
		return pathParts;
	}

	/**
	 * count of the path parts
	 * @return size of the list pathParts
	 */
	public int size()
	{
		return pathParts.size();
	}

	/**
	 * removes a specified path of the list given by an index i
	 * @param i: index of the part that has to be deleted
	 */
	public void remove(int i)
	{
		try {
			pathParts.remove(pathParts.size() - (i + 1));
		} catch (Exception e) {
			System.err.println("Path.remove(" + i + ") failed! Size of pathParts is " + pathParts.size());
			e.printStackTrace();
		}
	}

	/**
	 * calculates the distance that left to the end of the path
	 * @param currPose of the agent
	 * @return the distance from currPose to end of path
	 */
	public double getDistanceLeft(Pose2D currPose)
	{
		double dist = this.get(0).getDistanceLeft(currPose);
		for (PathBase pathPart : pathParts) {
			if (!pathPart.equals(this.get(0))) {
				dist += pathPart.getPathDistance();
			}
		}
		return dist;
	}

	/**
	 * updates all path parts by a given pose
	 * @param currPose of the agent
	 */
	public void updateWithPose(Pose2D currPose)
	{
		PathBase part = pathParts.size() > 1 ? this.get(1) : null;
		this.get(0).updateWithPose(currPose, part);
	}

	/**
	 * checks if the agent is still on path, and removes current path part if
	 * agent reached the next part
	 * @param startPose: current pose of agent
	 * @return true: if agent is on path else: if out of path range
	 */
	public boolean stillOnPath(Pose2D startPose)
	{
		// checks if Agent is nearer to current PathPart or to next PathPart
		if (nextPathPartReached(startPose)) {
			// if next part reached, current will be deleted
			this.remove(0);
		}

		// checks if Agent is still on path (within of tolerance)
		return this.get(0).stillOnPath(startPose);
	}

	/**
	 * checks if a next path part is reached
	 * @param startPose: current pose of agent
	 * @return true: if pose is on next path part false: if still on current path
	 *         part
	 */
	private boolean nextPathPartReached(Pose2D startPose)
	{
		return this.size() >= 2 && this.get(0).endOfPathPartReached(startPose);
	}
}
