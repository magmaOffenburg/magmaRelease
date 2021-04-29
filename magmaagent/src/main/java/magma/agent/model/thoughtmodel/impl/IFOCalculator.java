/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.impl;

import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import magma.agent.IMagmaConstants;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IThisPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Calculates indexical functional objects (Agre and Chapman) using filters and
 * comparators
 * @author Klaus Dorer
 */
public class IFOCalculator
{
	/** order criteria is distance to ball */
	private final Comparator<IPlayer> distanceToBallComparator;

	/** order criteria is distance to own goal */
	private final Comparator<IPlayer> distanceToOwnGoalComparator;

	/** order criteria is distance to this player */
	private final Comparator<IVisibleObject> distanceToMeComparator;

	/** accepts only teammates and not the goalie (+base checks) */
	private final Predicate<IPlayer> teammateNotGoalieFilter;

	/** accepts only teammates (+base checks) */
	private final Predicate<IPlayer> teammateFilter;

	/** accepts only opponents (+base checks) */
	private final Predicate<IPlayer> opponentFilter;

	private final Predicate<IPlayer> ownPlayersFilter;

	/** checking that player is not this player */
	private final Predicate<IPlayer> notThisPlayerFilter;

	/** list of the four goal posts for collision avoidance */
	private final List<IVisibleObject> goalPosts;

	/**
	 * Single instance created once at startup
	 */
	public IFOCalculator(IRoboCupWorldModel worldModel)
	{
		distanceToBallComparator =
				new DistanceToPositionComparator(worldModel, () -> worldModel.getBall().getPosition());
		distanceToOwnGoalComparator = new DistanceToPositionComparator(worldModel, worldModel::getOwnGoalPosition);
		distanceToMeComparator = new DistanceToMeComparator(worldModel);
		teammateNotGoalieFilter = new TeammateFilter(worldModel, new NotGoalieFilter(worldModel));
		ownPlayersFilter = new OwnPlayersFilter(worldModel);
		teammateFilter = new TeammateFilter(worldModel);
		opponentFilter = new OpponentFilter(worldModel);
		notThisPlayerFilter = new NotThisPlayerFilter(worldModel);
		goalPosts = worldModel.getGoalPostObstacles();
	}

	/**
	 * @param sourceList the list containing the players to sort
	 * @return a filtered list sorted ascending by the distance of the own team's
	 *         players not including goalie to the ball
	 */
	public List<IPlayer> getTeammatesAtBall(List<IPlayer> sourceList)
	{
		return getFilteredAndSortedList(sourceList, teammateNotGoalieFilter, distanceToBallComparator);
	}

	public List<IPlayer> getOwnPlayersAtOwnGoal(List<IPlayer> sourceList)
	{
		return getFilteredAndSortedList(sourceList, ownPlayersFilter, distanceToOwnGoalComparator);
	}

	public List<IPlayer> getTeammatesAtBallWithGoalie(List<IPlayer> sourceList)
	{
		return getFilteredAndSortedList(sourceList, teammateFilter, distanceToBallComparator);
	}

	/**
	 * @param sourceList the list containing the players to sort
	 * @return a filtered list sorted ascending by the distance of the opponent
	 *         team's players to the ball
	 */
	public List<IPlayer> getOpponentsAtBall(List<IPlayer> sourceList)
	{
		return getFilteredAndSortedList(sourceList, opponentFilter, distanceToBallComparator);
	}

	/**
	 * Filter all players currently near the ball out of the given list
	 *
	 * @param sourceList Source player list
	 * @return Filtered list
	 */
	public List<IPlayer> getPlayersAtBall(List<IPlayer> sourceList)
	{
		return getFilteredAndSortedList(sourceList, notThisPlayerFilter, distanceToBallComparator);
	}

	/**
	 * @param sourceList the list containing the players to sort
	 * @return a filtered list sorted ascending by the distance of the own team's
	 *         players including goalie to me
	 */
	public List<IPlayer> getPlayersAtMe(List<IPlayer> sourceList)
	{
		return getFilteredAndSortedList(sourceList, notThisPlayerFilter, distanceToMeComparator);
	}

	/**
	 * @param sourceList the list containing the players to sort
	 * @return a filtered list sorted ascending by the distance of the own team's
	 *         players including goalie to me
	 */
	public List<IPlayer> getTeammatesAtMe(List<IPlayer> sourceList)
	{
		return getFilteredAndSortedList(sourceList, teammateFilter, distanceToMeComparator);
	}

	/**
	 * @param sourceList the list containing the players to sort
	 * @return a filtered list sorted ascending by the distance of the other
	 *         team's players including goalie to me
	 */
	public List<IPlayer> getOpponentsAtMe(List<IPlayer> sourceList)
	{
		return getFilteredAndSortedList(sourceList, opponentFilter, distanceToMeComparator);
	}

	/**
	 * Retrieve a list of obstacles
	 *
	 * @param sourceList the list containing the players to sort
	 * @param ball Reference to ball object
	 * @return a filtered list sorted ascending by the distance of the other
	 *         team's players including goalie to me
	 */
	public List<IVisibleObject> getObstacles(List<IPlayer> sourceList, IMoveableObject ball)
	{
		List<IPlayer> result = filter(sourceList, notThisPlayerFilter);
		List<IVisibleObject> obstacles = new ArrayList<>(result);

		obstacles.addAll(goalPosts);
		if (!ball.isMoving()) {
			// ball is only obstacle if not moving
			obstacles.add(ball);
		}
		obstacles.sort(distanceToMeComparator);
		return obstacles;
	}

	/**
	 * Returns a new list containing only the elements of the source list that
	 * were accepted by the filter and are sorted using the passed comparator.
	 * Filter or comparator may be null, but it does not make sense to pass both
	 * as null since the list will then be unchanged.
	 * @param sourceList the list with the data to filter and sort
	 * @param filter the filter to use for each element of the source list, null
	 *        if no filter should be used
	 * @param comparator for sorting, null if order should not be changed
	 * @return the filtered and sorted list
	 */
	public List<IPlayer> getFilteredAndSortedList(
			List<IPlayer> sourceList, Predicate<IPlayer> filter, Comparator<? super IPlayer> comparator)
	{
		List<IPlayer> result = filter(sourceList, filter);
		if (comparator != null) {
			result.sort(comparator);
		}
		return result;
	}

	/**
	 * @param sourceList the list of players to filter
	 * @param filter the filter to use
	 * @return a new list that only contains the elements of the passed list that
	 *         passed the filter
	 */
	private List<IPlayer> filter(List<IPlayer> sourceList, Predicate<IPlayer> filter)
	{
		List<IPlayer> result = new ArrayList<>(sourceList.size());
		result.addAll(sourceList.stream()
							  .filter(player -> filter == null || filter.test(player))
							  .collect(Collectors.toList()));
		return result;
	}

	private class BaseFilter implements Predicate<IPlayer>
	{
		/** link to the world model */
		protected final IRoboCupWorldModel worldModel;

		/** decorated filter, null if none is decorated */
		protected final Predicate<IPlayer> decoratedFilter;

		public BaseFilter(IRoboCupWorldModel worldModel)
		{
			this.worldModel = worldModel;
			this.decoratedFilter = null;
		}

		public BaseFilter(IRoboCupWorldModel worldModel, Predicate<IPlayer> decoratee)
		{
			this.worldModel = worldModel;
			this.decoratedFilter = decoratee;
		}

		/**
		 * @param player the player to check
		 * @return true if the player is accepted by the filter, false if rejected
		 */
		@Override
		public boolean test(IPlayer player)
		{
			if (decoratedFilter != null) {
				boolean result = decoratedFilter.test(player);
				if (!result) {
					return false;
				}
			}

			if (player == null || player.getAge(worldModel.getGlobalTime()) > 4.0) {
				// only take into account recently seen players
				return false;
			}

			return localAccept(player);
		}

		/**
		 * Overwrite this method in subclasses to add filter criteria
		 * @param player the player to check for acceptance
		 * @return true if the player should remain in the list
		 */
		protected boolean localAccept(IPlayer player)
		{
			return true;
		}
	}

	private class NotThisPlayerFilter extends BaseFilter
	{
		public NotThisPlayerFilter(IRoboCupWorldModel worldModel)
		{
			super(worldModel);
		}

		public NotThisPlayerFilter(IRoboCupWorldModel worldModel, Predicate<IPlayer> decoratee)
		{
			super(worldModel, decoratee);
		}

		@Override
		public boolean localAccept(IPlayer player)
		{
			return !(player == null || (player.isOwnTeam() && player.getID() == worldModel.getThisPlayer().getID()));
		}
	}

	private class OwnPlayersFilter extends BaseFilter
	{
		public OwnPlayersFilter(IRoboCupWorldModel worldModel)
		{
			super(worldModel);
		}

		@Override
		public boolean localAccept(IPlayer player)
		{
			return super.localAccept(player) && !(player == null || !player.isOwnTeam());
		}
	}

	/**
	 * Accepts only players of own team
	 */
	private class TeammateFilter extends NotThisPlayerFilter
	{
		public TeammateFilter(IRoboCupWorldModel worldModel)
		{
			super(worldModel);
		}

		public TeammateFilter(IRoboCupWorldModel worldModel, Predicate<IPlayer> decoratee)
		{
			super(worldModel, decoratee);
		}

		@Override
		public boolean localAccept(IPlayer player)
		{
			return super.localAccept(player) && !(player == null || !player.isOwnTeam());
		}
	}

	/**
	 * Accepts only players of other team
	 */
	private class OpponentFilter extends BaseFilter
	{
		public OpponentFilter(IRoboCupWorldModel worldModel)
		{
			super(worldModel);
		}

		public OpponentFilter(IRoboCupWorldModel worldModel, Predicate<IPlayer> decoratee)
		{
			super(worldModel, decoratee);
		}

		@Override
		public boolean localAccept(IPlayer player)
		{
			return !(player == null || player.isOwnTeam());
		}
	}

	/**
	 * Accepts only players that are not goalies
	 */
	private class NotGoalieFilter extends NotThisPlayerFilter
	{
		public NotGoalieFilter(IRoboCupWorldModel worldModel)
		{
			super(worldModel);
		}

		public NotGoalieFilter(IRoboCupWorldModel worldModel, Predicate<IPlayer> decoratee)
		{
			super(worldModel, decoratee);
		}

		@Override
		public boolean localAccept(IPlayer player)
		{
			return super.localAccept(player) && !player.isGoalie();
		}
	}

	private abstract class IFOComparator<T> implements Comparator<T>
	{
		protected final IRoboCupWorldModel worldModel;

		public IFOComparator(IRoboCupWorldModel worldModel)
		{
			this.worldModel = worldModel;
		}
	}

	private class DistanceToPositionComparator extends IFOComparator<IPlayer>
	{
		private final Supplier<Vector3D> positionSupplier;

		public DistanceToPositionComparator(IRoboCupWorldModel worldModel, Supplier<Vector3D> positionSupplier)
		{
			super(worldModel);
			this.positionSupplier = positionSupplier;
		}

		@Override
		public int compare(IPlayer player1, IPlayer player2)
		{
			Vector3D position = positionSupplier.get();
			double distance1 = player1.getDistanceToXY(position);
			if (player1.isLying()) {
				distance1 += IMagmaConstants.DISTANCE_PENALTY_LYING;
			}
			double distance2 = player2.getDistanceToXY(position);
			if (player2.isLying()) {
				distance2 += IMagmaConstants.DISTANCE_PENALTY_LYING;
			}

			return Double.compare(distance1, distance2);
		}
	}

	/**
	 * Criteria is the distance to this player
	 */
	private class DistanceToMeComparator extends IFOComparator<IVisibleObject>
	{
		public DistanceToMeComparator(IRoboCupWorldModel worldModel)
		{
			super(worldModel);
		}

		@Override
		public int compare(IVisibleObject player1, IVisibleObject player2)
		{
			IThisPlayer thisPlayer = worldModel.getThisPlayer();
			double distance1 = thisPlayer.getDistanceToXY(player1.getPosition());
			double distance2 = thisPlayer.getDistanceToXY(player2.getPosition());

			return Double.compare(distance1, distance2);
		}
	}
}
