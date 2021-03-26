/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.path;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Circle2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Tangent;
import java.util.ArrayList;
import java.util.List;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalk;
import magma.agent.model.agentmodel.SupportFoot;
import magma.util.benchmark.PathParameterWalkBenchmark;
import magma.util.benchmark.PathParameterWalkBenchmarkItem;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Stefan Grossmann
 */
public class OptimalPathCalculation
{
	/** distance foot to foot */
	private static final float FOOT_DISTANCE = 0.1f;

	/** current fastest speed walked */
	private static final double maxSpeed = 0.841;

	/** distance of switch from path to step plan */
	private static final double distPathPlan = 1.0;

	/** start position */
	private Pose2D startPose;

	/** start speed */
	private double startSpeed;

	/** target of WalkToPosition, real target for the defined foot */
	private Pose2D footTargetPose;

	/** target for path calculation, cause body has to walk beside footTarget */
	private Pose2D bodyTargetPose;

	/** speed at target */
	private double speedThere;

	/** defined foot at target */
	private SupportFoot supportFoot;

	/** benchmark-file */
	private PathParameterWalkBenchmark benchmarkItems;

	/** best path */
	private Path optimalPath;

	/** all paths */
	private List<Path> calculatedPaths;

	/** OptPathVisualizer */
	// this is an invalid dependency to tools and has to be solved differently
	// private OptPathVisualizer vis;

	/***
	 * constructor initializes a visualizer for parameter true
	 * @param withVis true for parallel starting visualizer, else false
	 */
	public OptimalPathCalculation(boolean withVis)
	{
		if (withVis) {
			try {
				// vis = new OptPathVisualizer(this);
				// vis.setTitle("OptimalPath");
				// vis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				// vis.setSize(530, 680);
				// vis.setVisible(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * initialize OptimalPathCalculation with given parameter
	 * @param startPose: current position
	 * @param startSpeed: current speed
	 * @param targetPose: target position
	 * @param speedThere: target speed
	 * @param supportFoot: foot at target
	 */
	public OptimalPathCalculation(
			Pose2D startPose, double startSpeed, Pose2D targetPose, Vector3D speedThere, SupportFoot supportFoot)
	{
		init(startPose, startSpeed, targetPose, speedThere, supportFoot);
	}

	/**
	 * checks for a new game situation by given parameters
	 * @param startPose: current position
	 * @param startSpeed: current speed
	 * @param targetPose: target position
	 * @param speedThere: target speed
	 * @param supportFoot: foot at target
	 * @return true if its a new situation, else false
	 */
	public boolean newSituation(
			Pose2D startPose, double startSpeed, Pose2D targetPose, Vector3D speedThere, SupportFoot supportFoot)
	{
		if (this.startPose == null || this.startSpeed == 0 || this.footTargetPose == null || this.speedThere == 0) {
			init(startPose, startSpeed, targetPose, speedThere, supportFoot);
			calculatePath();
			return true;
		}
		if (isInTolerance(this.footTargetPose.x, targetPose.x) && isInTolerance(this.footTargetPose.y, targetPose.y) &&
				isInTolerance(this.footTargetPose.angle.degreesPositive(), targetPose.angle.degreesPositive()) &&
				isInTolerance(this.speedThere, speedThere.getX()) &&
				(supportFoot == null || supportFoot == SupportFoot.NONE || this.supportFoot == supportFoot)) {
			this.startSpeed = startSpeed;
			this.startPose = startPose;
			return false;
		} else {
			init(startPose, startSpeed, targetPose, speedThere, supportFoot);
			calculatePath();
			return true;
		}
	}

	/**
	 * checks if val2 is in tolerance of val
	 * @param val: first double param
	 * @param val2: second double param
	 * @return true if val2 is out of defined tolerance
	 */
	private boolean isInTolerance(double val, double val2)
	{
		double tolerance = 0.1; // tolerance of 10 percentage
		double diffVal = (val - val2) / val;
		if (diffVal < 0)
			diffVal *= -1;
		return diffVal <= tolerance;
	}

	/**
	 * initialize attributes of the class with given parameters
	 * @param startPose: current position
	 * @param startSpeed: current speed
	 * @param targetPose: target position
	 * @param speedThere: target speed
	 * @param supportFoot: foot at target
	 */
	private void init(
			Pose2D startPose, double startSpeed, Pose2D targetPose, Vector3D speedThere, SupportFoot supportFoot)
	{
		this.startPose = startPose;
		this.startSpeed = startSpeed;
		this.footTargetPose = this.bodyTargetPose = targetPose;
		this.speedThere = speedThere.getX();
		this.calculatedPaths = new ArrayList<>();
		this.benchmarkItems = new PathParameterWalkBenchmark();
		this.supportFoot = supportFoot;
		this.optimalPath = new Path();
		recalculateFootAndTarget();
	}

	/**
	 * checks which support foot is needed by a given situation
	 * @param start: start position
	 * @param target: target position
	 * @return the needed support foot
	 */
	private SupportFoot whichSupportFoot(Pose2D start, Pose2D target)
	{
		double angle = target.getAngleTo(start).degrees();
		return angle < 0 ? SupportFoot.LEFT : SupportFoot.RIGHT;
	}

	/**
	 * defines the needed support foot if its null and calculate the
	 * bodyTargetPose
	 */
	private void recalculateFootAndTarget()
	{
		if (supportFoot == null)
			supportFoot = this.whichSupportFoot(this.startPose, this.footTargetPose);

		if (supportFoot == SupportFoot.LEFT || supportFoot == SupportFoot.RIGHT) {
			Angle angle = supportFoot == SupportFoot.RIGHT ? Angle.ANGLE_90 : Angle.ANGLE_90.negate();
			this.bodyTargetPose = new Circle2D(footTargetPose.x, footTargetPose.y, FOOT_DISTANCE)
										  .getPointOnCircle(this.bodyTargetPose.angle.add(angle));
			this.bodyTargetPose.angle = this.footTargetPose.angle;
		}
	}

	/**
	 * calculate the optimalPath by current set attributes
	 * @return true if one path was found, else if not
	 */
	public boolean calculatePath()
	{
		calculatedPaths = new ArrayList<>();
		optimalPath = new Path();
		// if start pose got same angle to target as target itself
		if (startPose.getAngleTo(bodyTargetPose).degreesPositive() == 0 &&
				startPose.getAngle().degreesPositive() == bodyTargetPose.getAngle().degreesPositive()) {
			// just walk straight forward
			optimalPath.add(new PathStraight(startPose, bodyTargetPose));
			calculatedPaths.add(optimalPath);
		} else {
			double startSpeedTolerance = 0.0;
			double toleranceSteps = 0.1;
			while (optimalPath.size() == 0 && (startSpeedTolerance) < maxSpeed + 0.2) {
				startSpeedTolerance += toleranceSteps;
				for (PathParameterWalkBenchmarkItem targetItem : benchmarkItems.getSpeedAbout(
							 speedThere, startSpeedTolerance - toleranceSteps, startSpeedTolerance)) {
					PathCircle pathCircleTarget =
							this.putPathCircleOnPose(bodyTargetPose, startPose, targetItem, Angle.deg(90), false);

					Path initialPath = new Path();
					initialPath.add(pathCircleTarget);

					// start pose forward walk intersect target circle? so start
					// circle
					// at other side is needed
					Pose2D targetCirclePose =
							new Pose2D(pathCircleTarget.circle.getX(), pathCircleTarget.circle.getY());
					double dist = startPose.getDistanceTo(targetCirclePose);

					// distance to TargetCircle middle where the angle of startPose
					// passes
					double parallelDistToTangent = Math.sin(startPose.getAngleTo(targetCirclePose).radians()) * dist;
					if (parallelDistToTangent < 0)
						parallelDistToTangent *= -1;

					// check if straight forward = tangent on target circle -> so no
					// start circle needed
					if (parallelDistToTangent == pathCircleTarget.circle.getRadius()) {
						int tangentNo2 = 1;
						if (startPose.getAngleTo(targetCirclePose).radians() < 0) {
							tangentNo2 = 3;
						}
						PathStraight tangent2 = createTangent(
								new Circle2D(startPose.x, startPose.y, 0), pathCircleTarget.circle, tangentNo2);
						pathCircleTarget.setStartPoint(tangent2.getEndPoint());

						initialPath.add(pathCircleTarget);
						initialPath.add(tangent2);
						calculatedPaths.add(initialPath);
						this.validateCosts(initialPath);
					} else {
						// take different start circles for later path calculation
						for (PathParameterWalkBenchmarkItem startItem : benchmarkItems.getCirclesFaster(startSpeed)) {
							Path currPath = new Path();
							pathCircleTarget = new PathCircle(pathCircleTarget);
							currPath.add(pathCircleTarget);
							Angle startAngle = Angle.deg(90);

							// need to swap walk circle on other side if
							// startPose.angle
							// intersects target circle
							Angle start2Target = startPose.getAngleTo(bodyTargetPose);
							boolean swap = false;
							if (start2Target.degrees() > -90 && start2Target.degrees() < 90 &&
									parallelDistToTangent < pathCircleTarget.circle.getRadius()) {
								swap = true;
							}
							PathCircle pathCircleStart =
									this.putPathCircleOnPose(startPose, bodyTargetPose, startItem, startAngle, swap);

							if (pathCircleTarget.circle.checkOuterTouch(pathCircleStart.circle)) {
								// no tangent is needed; touch point calculation
								Angle angleToTouchPoint = startPose.getAngleTo(bodyTargetPose);
								Pose2D touchPoint = pathCircleTarget.circle.getPointOnCircle(angleToTouchPoint);
								pathCircleTarget.setStartPoint(touchPoint);
								pathCircleStart.setEndPoint(touchPoint);

								currPath.add(pathCircleStart);
								calculatedPaths.add(currPath);
								this.validateCosts(currPath);
							} else {
								// no inner circle or inner touch

								// decide which tangent is needed and calc it of both
								// circles
								int tangentNo = findTangent(pathCircleStart.item.getAngle().degrees(),
										pathCircleTarget.item.getAngle().degrees());
								// normal calculation if no intersect or intersection
								// but outer tangent
								if (!pathCircleTarget.circle.checkIntersect(pathCircleStart.circle) ||
										tangentNo % 2 == 1) {
									PathStraight tangent =
											createTangent(pathCircleStart.circle, pathCircleTarget.circle, tangentNo);
									pathCircleTarget.setStartPoint(tangent.getEndPoint());
									pathCircleStart.setEndPoint(tangent.getStartPoint());

									if (!pathCircleTarget.circle.checkInnerTouch(pathCircleStart.circle) &&
											!pathCircleTarget.circle.checkInnerCircle(pathCircleStart.circle) &&
											!pathCircleTarget.circle.checkPointOnCircleArea(
													pathCircleStart.startPoint) &&
											!pathCircleTarget.circle.checkPointOnCircleArea(pathCircleStart.endPoint) &&
											!pathCircleStart.circle.checkPointOnCircleArea(
													pathCircleTarget.startPoint) &&
											!pathCircleStart.circle.checkPointOnCircleArea(pathCircleTarget.endPoint)) {
										currPath.add(tangent);
										currPath.add(pathCircleStart);
										calculatedPaths.add(currPath);

										this.validateCosts(currPath);
									}
								}
							}
						}
					}
				}
			}

			if (optimalPath.size() == 0) {
				return false;
			}

			// if(vis != null)
			// vis.update(optimalPath,this.startPose);
		}
		return true;
	}

	/**
	 * decides which tangent number is needed for the given two angles
	 * @param startAngle double on startCircle
	 * @param targetAngle double on targetCircle
	 * @return the number for the needed tangent
	 */
	private int findTangent(double startAngle, double targetAngle)
	{
		if (startAngle > 0 && targetAngle > 0)
			return 1;
		if (startAngle < 0 && targetAngle < 0)
			return 3;
		if (startAngle > 0 && targetAngle < 0)
			return 2;
		if (startAngle < 0 && targetAngle > 0)
			return 4;
		return 0;
	}

	/**
	 * puts pathCircle on given position
	 * @param self: current position
	 * @param other: target position
	 * @param item: benchmarkItem which is needed
	 * @param angle: angle on the radius where the circle will be putted
	 * @return the calculated PathCircle
	 */
	private PathCircle putPathCircleOnPose(
			Pose2D self, Pose2D other, PathParameterWalkBenchmarkItem item, Angle angle, boolean swap)
	{
		item = new PathParameterWalkBenchmarkItem(item);
		if (self.getAngleTo(other).degrees() < 0 ^
				(swap && findTangent(self.getAngleTo(other).degrees(), other.getAngleTo(self).degrees()) % 2 == 1)) {
			item.setAngle(-item.getAngle().degrees());
			angle = angle.negate();
		}
		Circle2D circle = this.putCircleOnPose(self, other, item.getRadius(), angle);
		return new PathCircle(startPose, bodyTargetPose, item, circle);
	}

	/**
	 * create a tangent and returns it as a pathStraight
	 * @param circleStart: start circle
	 * @param circleTarget: end circle
	 * @param i: tangent number
	 * @return the tangent as pathStraight
	 */
	private PathStraight createTangent(Circle2D circleStart, Circle2D circleTarget, int i)
	{
		Pose2D start, end;

		Tangent t = circleStart.calculateTangent(circleTarget, i);
		start = t.getP1();
		end = t.getP2();

		Angle angle = start.getAngleTo(end);
		start.angle = angle;
		end.angle = angle;
		return new PathStraight(start, end);
	}

	/**
	 * entry for walk the path
	 * @param behaviors that are currently loaded
	 * @return behavior that has to be done
	 */
	public IBehavior walkIt(BehaviorMap behaviors)
	{
		// if optimalPath is null or agent is still on optimal path, path needed
		// to recalculate
		if (optimalPath == null || !this.stillOnOptimalPath()) {
			// optPath new Calculation
			if (!this.calculatePath()) {
				// TODO: find better solution
				IWalk walk = (IWalk) behaviors.get(IBehaviorConstants.WALK);
				walk.walk(100, 0, Angle.ZERO);
				return walk;
			}
		}

		// if(vis != null)
		// vis.update(optimalPath,this.startPose);

		optimalPath.updateWithPose(startPose);

		if (optimalPath.getDistanceLeft(startPose) < distPathPlan) {
			IBehavior stepBehavior =
					optimalPath.get(0).step(behaviors, startPose, new Path(optimalPath), footTargetPose, supportFoot);
			// if(vis != null)
			// {
			// vis.updateStep(((IKStepPlanBehavior)stepBehavior.getRootBehavior()).getStepPlan());
			// vis.updateNewStep(((IKStepPlanBehavior)stepBehavior.getRootBehavior()).getNewStepPlan());
			// }
			return stepBehavior;
		}
		return optimalPath.get(0).walk(behaviors);
	}

	/**
	 * checks if agent is still on the path
	 * @return true if still on path, else false
	 */
	private boolean stillOnOptimalPath()
	{
		// false if there doesn't exist an optimal path
		return !(optimalPath == null || optimalPath.size() == 0) && optimalPath.stillOnPath(startPose);
	}

	/**
	 * puts circle on given position
	 * @param self: current position
	 * @param other: target position
	 * @param radius: radius on what the circle has to be putted
	 * @param angle: angle on the radius where the circle will be putted
	 * @return the calculated Circle2D
	 */
	private Circle2D putCircleOnPose(Pose2D self, Pose2D other, double radius, Angle angle)
	{
		Circle2D circleAtPose = new Circle2D(self.x, self.y, radius);
		circleAtPose.relocate(circleAtPose.getPointOnCircle(self.angle.add(angle)));
		return circleAtPose;
	}

	/**
	 * checks if the given path is better than the current best
	 * @param path: that has to validate against the best
	 */
	private void validateCosts(Path path)
	{
		// check costs -> add to optimum if new min found
		if (path.getPathCost() < optimalPath.getPathCost() || optimalPath.getPathParts().size() == 0) {
			optimalPath = new Path(path);
		}
	}

	/**
	 * returning the best path of all calculated paths
	 */
	public Path getBestPath()
	{
		float minCost = 99999;
		Path minPath = null;
		for (Path path : calculatedPaths) {
			if (path.getPathCost() < minCost) {
				minPath = path;
				minCost = minPath.getPathCost();
			}
		}
		return minPath;
	}

	/**
	 * get all paths that are calculated
	 */
	public List<Path> getAllPaths()
	{
		return this.calculatedPaths;
	}

	// Getter and Setter Methods
	public PathParameterWalkBenchmark getBenchmarkItems()
	{
		return benchmarkItems;
	}

	public void setBenchmarkItems(PathParameterWalkBenchmark benchmarkItems)
	{
		this.benchmarkItems = benchmarkItems;
	}

	public Pose2D getStartPose()
	{
		return this.startPose;
	}

	public Pose2D getTargetPose()
	{
		return this.bodyTargetPose;
	}
}
