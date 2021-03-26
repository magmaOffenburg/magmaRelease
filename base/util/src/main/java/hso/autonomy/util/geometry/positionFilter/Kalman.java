/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry.positionFilter;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

public class Kalman extends BaseFilter
{
	private final RealMatrix dragMatrix;

	private final RealMatrix gMatrix;

	private final RealMatrix positionMatrix;

	private final RealMatrix bounceMatrix;

	private final double deltaT = 0.02 * 3;

	private final double drag = -0.4;

	private final double g = -9.81;

	// the state matrix
	private RealMatrix X;

	private RealMatrix A;

	// the state covariance matrix (error in the estimate X)
	private RealMatrix P;

	// the measurement covariance matrix (error in the measurement)
	private RealMatrix R;

	private RealMatrix Q;

	// private RealMatrix Z;

	private RealMatrix I;

	public Kalman()
	{
		init();

		A = new Array2DRowRealMatrix(new double[][] {
				{1, deltaT, 0, 0, 0, 0},
				{0, 1, 0, 0, 0, 0},
				{0, 0, 1, deltaT, 0, 0},
				{0, 0, 0, 1, 0, 0},
				{0, 0, 0, 0, 1, deltaT},
				{0, 0, 0, 0, 0, 1},
		});

		double positionError = 0.02;
		double velocityError = 0.01;
		R = new Array2DRowRealMatrix(new double[][] {
				{positionError * positionError, 0, 0, 0, 0, 0},
				{0, velocityError * velocityError, 0, 0, 0, 0},
				{0, 0, positionError * positionError, 0, 0, 0},
				{0, 0, 0, velocityError * velocityError, 0, 0},
				{0, 0, 0, 0, positionError * positionError, 0},
				{0, 0, 0, 0, 0, velocityError * velocityError},
		});

		double noise = 0.0001;
		Q = new Array2DRowRealMatrix(new double[][] {
				{noise, 0, 0, 0, 0, 0},
				{0, noise, 0, 0, 0, 0},
				{0, 0, noise, 0, 0, 0},
				{0, 0, 0, noise, 0, 0},
				{0, 0, 0, 0, noise, 0},
				{0, 0, 0, 0, 0, noise},
		});

		//		double noise2 = 0.00001;
		//		Z = new Array2DRowRealMatrix(new double[][] {
		//				{noise2},
		//				{noise2},
		//				{noise2},
		//				{noise2},
		//				{noise2},
		//				{noise2},
		//		});

		I = new Array2DRowRealMatrix(new double[][] {
				{1, 0, 0, 0, 0, 0},
				{0, 1, 0, 0, 0, 0},
				{0, 0, 1, 0, 0, 0},
				{0, 0, 0, 1, 0, 0},
				{0, 0, 0, 0, 1, 0},
				{0, 0, 0, 0, 0, 1},
		});

		dragMatrix = new Array2DRowRealMatrix(new double[][] {
				{0, 0, 0, 0, 0, 0},
				{0, drag * deltaT, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0},
				{0, 0, 0, drag * deltaT, 0, 0},
				{0, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, drag * deltaT},
		});

		gMatrix = new Array2DRowRealMatrix(new double[][] {
				{0},
				{0},
				{0},
				{0},
				{0},
				{g * deltaT},
		});

		positionMatrix = new Array2DRowRealMatrix(new double[][] {
				{1, deltaT, 0, 0, 0, 0},
				{0, 1, 0, 0, 0, 0},
				{0, 0, 1, deltaT, 0, 0},
				{0, 0, 0, 1, 0, 0},
				{0, 0, 0, 0, 1, deltaT},
				{0, 0, 0, 0, 0, 1},
		});

		bounceMatrix = new Array2DRowRealMatrix(new double[][] {
				{1, 0, 0, 0, 0, 0},
				{0, 0.75, 0, 0, 0, 0},
				{0, 0, 1, 0, 0, 0},
				{0, 0, 0, 0.75, 0, 0},
				{0, 0, 0, 0, 1, 0},
				{0, 0, 0, 0, 0, -0.67},
		});
	}

	@Override
	public Vector3D filterPosition(Vector3D newPosition, Vector3D oldPosition, Vector3D speed)
	{
		if (X == null) {
			System.out.println("init");

			// create initial state
			X = new Array2DRowRealMatrix(new double[][] {
					{newPosition.getX()},
					{speed.getX() * 1 / deltaT},
					{newPosition.getY()},
					{speed.getY() * 1 / deltaT},
					{newPosition.getZ()},
					{speed.getZ() * 1 / deltaT},
			});
		}

		// update state based on the simulations physics model
		// simulation seems to use a different equation model
		// so X = A*X + B*u didn't seem to work
		// instead using this one (similar to Geometry.getFuturePositions())
		X = X.add(dragMatrix.multiply(X).add(gMatrix));
		X = positionMatrix.multiply(X);
		// the ball bounces off the surface
		if (X.getEntry(4, 0) < 0) {
			X = bounceMatrix.multiply(X);
		}

		// update state covariance matrix
		P = A.multiply(P).multiply(A.transpose()).add(Q);

		// calculate Kalman gain
		RealMatrix K = P.multiply(new LUDecomposition(P.add(R)).getSolver().getInverse());

		// get observation
		RealMatrix Y = new Array2DRowRealMatrix(new double[][] {
				{newPosition.getX()},
				{speed.getX() * 1 / deltaT},
				{newPosition.getY()},
				{speed.getY() * 1 / deltaT},
				{newPosition.getZ()},
				{speed.getZ() * 1 / deltaT},
		});

		// Y.add(Z);

		// calculate current state
		X = X.add(K.multiply(Y.subtract(X)));

		// update P
		P = I.subtract(K).multiply(P);

		// return position vector
		return new Vector3D(X.getEntry(0, 0), X.getEntry(2, 0), X.getEntry(4, 0));
	}

	@Override
	public void reset()
	{
		X = null;
		init();
	}

	private void init()
	{
		double positionError = 1;
		double velocityError = 1;
		P = new Array2DRowRealMatrix(new double[][] {
				{positionError * positionError, 0, 0, 0, 0, 0},
				{0, velocityError * velocityError, 0, 0, 0, 0},
				{0, 0, positionError * positionError, 0, 0, 0},
				{0, 0, 0, velocityError * velocityError, 0, 0},
				{0, 0, 0, 0, positionError * positionError, 0},
				{0, 0, 0, 0, 0, velocityError * velocityError},
		});
	}
	@Override
	public Vector3D filterPosition(Vector3D newPosition, Vector3D oldPosition)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Vector3D filterPosition(Vector3D newPosition)
	{
		throw new UnsupportedOperationException();
	}
}