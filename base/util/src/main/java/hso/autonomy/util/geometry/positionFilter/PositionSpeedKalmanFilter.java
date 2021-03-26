/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry.positionFilter;

import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author kdorer
 */
public class PositionSpeedKalmanFilter extends BaseFilter
{
	private KalmanFilter filter;

	public PositionSpeedKalmanFilter()
	{
		// discrete time interval
		double dt = 0.06;
		// position measurement noise (meter)
		double mnoise = 0.2;
		// speed decay (due to friction)
		double decay = 0.94;

		// A = state transition matrix for state {x, vx, y, vy}
		RealMatrix A = new Array2DRowRealMatrix(
				new double[][] {{1, dt, 0, 0}, {0, decay, 0, 0}, {0, 0, 1, dt}, {0, 0, 0, decay}});
		// B = control input matrix (none so far)
		RealMatrix B = new Array2DRowRealMatrix(new double[][] {{0}, {0}, {0}, {0}});

		// H = measurement matrix
		RealMatrix H =
				new Array2DRowRealMatrix(new double[][] {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}});
		// x = initial guess
		RealVector x = new ArrayRealVector(new double[] {0, 0, 0, 0});

		// Q = estimate of process error
		RealMatrix Q =
				new Array2DRowRealMatrix(new double[][] {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}});

		// P0 = initial guess of covariance matrix
		RealMatrix P0 =
				new Array2DRowRealMatrix(new double[][] {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}});
		// R = estimate of measurement noise
		RealMatrix R = new Array2DRowRealMatrix(
				new double[][] {{mnoise, 0, 0, 0}, {0, mnoise, 0, 0}, {0, 0, mnoise, 0}, {0, 0, 0, mnoise}});

		ProcessModel pm = new DefaultProcessModel(A, B, Q, x, P0);
		MeasurementModel mm = new DefaultMeasurementModel(H, R);
		filter = new KalmanFilter(pm, mm);
	}

	public void predict()
	{
		// here we could add our actions with a vector u
		filter.predict();
	}

	@Override
	public Vector3D filterPosition(Vector3D position, Vector3D oldPosition, Vector3D speed)
	{
		// here we could add our actions with a vector u
		filter.predict();

		RealVector z = new ArrayRealVector(new double[] {position.getX(), speed.getX(), position.getY(), speed.getY()});

		filter.correct(z);

		Vector3D result = new Vector3D(filter.getStateEstimation()[0], filter.getStateEstimation()[2], position.getZ());

		// System.out.println(filter.toString());
		System.out.println("Observed Speed: " + speed.getNorm() + "observedx: " + speed.getX() +
						   " speedx: " + filter.getStateEstimation()[1]);
		return result;
	}

	@Override
	public void reset()
	{
	}
}
