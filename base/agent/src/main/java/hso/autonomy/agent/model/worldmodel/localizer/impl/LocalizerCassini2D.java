/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer.impl;

import hso.autonomy.agent.model.worldmodel.localizer.IFeatureMap;
import hso.autonomy.agent.model.worldmodel.localizer.ILineFeatureObservation;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeatureObservation;
import hso.autonomy.util.geometry.IPose3D;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;

/**
 * Implements a 2-dimensional localizer based on the Cassini algorithm
 *
 * @author Stefan Glaser
 */
public class LocalizerCassini2D extends SimpleLocalizerBase
{
	@Override
	public IPose3D localize(IFeatureMap map, List<IPointFeatureObservation> pointObservations,
			List<ILineFeatureObservation> lineObservations, Rotation orientationEstimation)
	{
		return null;

		// if (flags.size() < 3) {
		// // there are not 3 visible flags
		// logger.log(Level.FINER,
		// "Localization not possible, only have {0} flags", flags.size());
		// return null;
		//
		// }
		// // sort the flags according to their angle
		// List<ILocalizationFlag> sortedFlags = new ArrayList<ILocalizationFlag>(
		// flags.values());
		// Collections.sort(sortedFlags);
		//
		// double x1, x2, x3; // Flag-Positions
		// double y1, y2, y3; // Flag Positions
		// double x, y; // Localized Position
		// double xH1, yH1; // Support-Points
		// double xH2, yH2; // Support-Points
		// double n, N; // q = 1 / p
		// double alpha, beta;
		// double distance;
		// double ratio;
		// double gamma;
		// double iFlag1Direction;
		// double iFlag2Direction;
		// double iFlag3Direction;
		// double dAbsDirection;
		// // double dAbsDir = 0.0;
		// ILocalizationFlag flag1;
		// ILocalizationFlag flag2;
		// ILocalizationFlag flag3;
		// Vector3D position;
		// // Vector3D AbsPos;
		// // Vector3D pos1;
		// // Vector3D pos2;
		// // Vector3D pos3;
		//
		// // do some averaging
		// // int loops = sortedFlags.size() / 3;
		// // pos1 = sortedFlags.get(0);
		// // pos2 = sortedFlags.get(sortedFlags.size() / 3);
		// // pos3 = sortedFlags.get(sortedFlags.size() / 3 * 2);
		// // for (int i = 0; i < loops; i++)
		// // {
		// // left flag
		// flag1 = sortedFlags.get(0);
		// // mid flag
		// flag2 = sortedFlags.get(1);
		// // right flag
		// flag3 = sortedFlags.get(2);
		//
		// // set parameters
		// // Vector3D flag1Vector = new Vector3D(flag1.getDistance(), new
		// Vector3D(
		// // flag1.getHorizontalDirection(), flag1.getLatitudalDirection()));
		// // Vector3D flag2Vector = new Vector3D(flag2.getDistance(), new
		// Vector3D(
		// // flag2.getHorizontalDirection(), flag2.getLatitudalDirection()));
		// // Vector3D flag3Vector = new Vector3D(flag3.getDistance(), new
		// Vector3D(
		// // flag3.getHorizontalDirection(), flag3.getLatitudalDirection()));
		//
		// x1 = flag1.getKnownPosition().getX();
		// x2 = flag2.getKnownPosition().getX();
		// x3 = flag3.getKnownPosition().getX();
		// y1 = flag1.getKnownPosition().getY();
		// y2 = flag2.getKnownPosition().getY();
		// y3 = flag3.getKnownPosition().getY();
		//
		// iFlag1Direction = flag1.getHorizontalDirection();
		// iFlag2Direction = flag2.getHorizontalDirection();
		// iFlag3Direction = flag3.getHorizontalDirection();
		//
		// alpha = (iFlag2Direction - iFlag1Direction);
		// beta = (iFlag3Direction - iFlag2Direction);
		//
		// if (alpha < 0) {
		// alpha *= -1;
		// }
		//
		// if (beta < 0) {
		// beta *= -1;
		// }
		//
		// // System.out.println("---------------");
		// // System.out.println("a: " + Math.toDegrees(alpha));
		// // System.out.println("b: " + Math.toDegrees(beta));
		// //
		// // if (alpha < 0) {
		// // alpha *= -1;
		// // }
		// //
		// // if (beta < 0) {
		// // beta *= -1;
		// // }
		// //
		// // System.out.println("a: " + Math.toDegrees(alpha));
		// // System.out.println("b: " + Math.toDegrees(beta));
		//
		// // do calculations
		// xH1 = x1 + ((y2 - y1) / Math.tan(alpha));
		// yH1 = y1 + ((x1 - x2) / Math.tan(alpha));
		// xH2 = x3 + ((y3 - y2) / Math.tan(beta));
		// yH2 = y3 + ((x2 - x3) / Math.tan(beta));
		//
		// // ASSERT ((nenner < D_ZERO_N) || (nenner > D_ZERO_P));
		// n = (xH2 - xH1) / (yH2 - yH1);
		// N = n + (1 / n);
		//
		// // calculate coordinates
		// // ASSERT ((nenner < D_ZERO_N) || (nenner > D_ZERO_P));
		// y = ((n * yH1) + ((1 / n) * y2) + x2 - xH1) / N;
		// x = ((n * x2) + ((1 / n) * xH1) + y2 - yH1) / N;
		//
		// position = new Vector3D(x, y, 0);
		//
		// // calculate absoulte direction
		// distance = flag1.getDistance();
		// ratio = (y1 - y) / distance;
		// gamma = Math.asin(ratio);
		// if (x > x1)
		// if (y > y1)
		// dAbsDirection = -Math.PI - gamma - iFlag1Direction;
		// else
		// dAbsDirection = Math.PI - gamma - iFlag1Direction;
		// else
		// dAbsDirection = gamma - iFlag1Direction;
		//
		// // do some filtering
		// // AbsPos += position;
		// // dAbsDir += dAbsDirection;
		// // }
		//
		// // there are i points to measure
		// // if (i > 1)
		// // {
		// // AbsPos *= 1.0 / (double) i;
		// // dAbsDir /= (double) i;
		// // }
		//
		// // set this players coordinates
		// // m_pThisPlayer->SetPlayerAbsCoordinates(AbsPos, (int) dAbsDir,
		// // m_iHeadAngle,
		// // m_dSpeed, m_iViewAngle, m_pAgent->IsDashingBackward()
		// // , (m_iSeeTime > m_iSenseTime), m_pAgent->HaveTurned());
		//
		// // m_pAgent->DisplayPosition(m_pThisPlayer->GetPosition(),
		// // m_pThisPlayer->GetAbsBodyDir()
		// // , m_pThisPlayer->GetAbsDir(), TRUE);
		//
		// PositionOrientation3D myPos = new PositionOrientation3D(position, Angle
		// .rad(dAbsDirection));
		//
		// return myPos;
	}

	// @Override
	// public PositionOrientation localize(
	// HashMap<String, ILocalizationFlag> flags, float neckYawAngle,
	// float neckPitchAngle, IGyroRate gyro)
	// {
	// if (flags.size() < 3) {
	// // there are not 3 visible flags
	// logger.log(Level.FINER,
	// "Localization not possible, only have {0} flags", flags.size());
	// return null;
	// }
	//
	// // sort the flags according to their angle
	// List<ILocalizationFlag> sortedFlags = new ArrayList<ILocalizationFlag>(
	// flags.values());
	// Collections.sort(sortedFlags);
	//
	// double x1, x2, x3; // Flag-Positions
	// double y1, y2, y3; // Flag Positions
	// double x, y; // Localized Position
	// double xP, yP; // Support-Points
	// double xQ, yQ; // Support-Points
	// double m1, m2; // q = 1 / p
	// double b1, b2;
	// double tant45;
	// double alpha, beta;
	// double nenner;
	// double distance;
	// double ratio;
	// double gamma;
	// double iFlag1Direction;
	// double iFlag2Direction;
	// double iFlag3Direction;
	// double dAbsDirection;
	// // double dAbsDir = 0.0;
	// ILocalizationFlag flag1;
	// ILocalizationFlag flag2;
	// ILocalizationFlag flag3;
	// Vector3D position;
	// // Vector3D AbsPos;
	// // Vector3D pos1;
	// // Vector3D pos2;
	// // Vector3D pos3;
	//
	// // do some averaging
	// // int loops = sortedFlags.size() / 3;
	// // pos1 = sortedFlags.get(0);
	// // pos2 = sortedFlags.get(sortedFlags.size() / 3);
	// // pos3 = sortedFlags.get(sortedFlags.size() / 3 * 2);
	// // for (int i = 0; i < loops; i++)
	// // {
	// // left flag
	// flag1 = sortedFlags.get(0);
	// // mid flag
	// flag2 = sortedFlags.get(1);
	// // right flag
	// flag3 = sortedFlags.get(2);
	//
	// // set parameters
	// // Vector3D flag1Vector = new Vector3D(flag1.getDistance(), new Vector3D(
	// // flag1.getHorizontalDirection(), flag1.getLatitudalDirection()));
	// // Vector3D flag2Vector = new Vector3D(flag2.getDistance(), new Vector3D(
	// // flag2.getHorizontalDirection(), flag2.getLatitudalDirection()));
	// // Vector3D flag3Vector = new Vector3D(flag3.getDistance(), new Vector3D(
	// // flag3.getHorizontalDirection(), flag3.getLatitudalDirection()));
	//
	// x1 = flag1.getKnownPosition().getX();
	// x2 = flag2.getKnownPosition().getX();
	// x3 = flag3.getKnownPosition().getX();
	// y1 = flag1.getKnownPosition().getY();
	// y2 = flag2.getKnownPosition().getY();
	// y3 = flag3.getKnownPosition().getY();
	//
	// iFlag1Direction = flag1.getHorizontalDirection();
	// iFlag2Direction = flag2.getHorizontalDirection();
	// iFlag3Direction = flag3.getHorizontalDirection();
	//
	// alpha = (iFlag2Direction - iFlag1Direction);
	// beta = (iFlag3Direction - iFlag2Direction);
	//
	// // System.out.println("---------------");
	// // System.out.println("a: " + Math.toDegrees(alpha));
	// // System.out.println("b: " + Math.toDegrees(beta));
	// //
	// // if (alpha < 0) {
	// // alpha *= -1;
	// // }
	// //
	// // if (beta < 0) {
	// // beta *= -1;
	// // }
	// //
	// // System.out.println("a: " + Math.toDegrees(alpha));
	// // System.out.println("b: " + Math.toDegrees(beta));
	//
	// // do calculations
	// yP = y1 + ((x2 - x1) / Math.tan(alpha));
	// xP = x1 - ((y2 - y1) / Math.tan(alpha));
	// yQ = y3 - ((x2 - x3) / Math.tan(beta));
	// xQ = x3 + ((y2 - y3) / Math.tan(beta));
	//
	// // ASSERT ((nenner < D_ZERO_N) || (nenner > D_ZERO_P));
	// m1 = (yQ - yP) / (xQ - xP);
	// m2 = -1 / m1;
	//
	// b1 = yP - m1 * xP;
	// b2 = y2 - m2 * x2;
	//
	// // calculate coordinates
	// // ASSERT ((nenner < D_ZERO_N) || (nenner > D_ZERO_P));
	// x = (b2 - b1) / (m1 - m2);
	// y = (m1 * b2 - m2 * b1) / (m1 - m2);
	//
	// position = new Vector3D(x, y, 0);
	//
	// // calculate absoulte direction
	// distance = flag1.getDistance();
	// ratio = (y1 - y) / distance;
	// gamma = Math.asin(ratio);
	// if (x > x1)
	// if (y > y1)
	// dAbsDirection = -Math.PI - gamma - iFlag1Direction;
	// else
	// dAbsDirection = Math.PI - gamma - iFlag1Direction;
	// else
	// dAbsDirection = gamma - iFlag1Direction;
	//
	// // do some filtering
	// // AbsPos += position;
	// // dAbsDir += dAbsDirection;
	// // }
	//
	// // there are i points to measure
	// // if (i > 1)
	// // {
	// // AbsPos *= 1.0 / (double) i;
	// // dAbsDir /= (double) i;
	// // }
	//
	// // set this players coordinates
	// // m_pThisPlayer->SetPlayerAbsCoordinates(AbsPos, (int) dAbsDir,
	// // m_iHeadAngle,
	// // m_dSpeed, m_iViewAngle, m_pAgent->IsDashingBackward()
	// // , (m_iSeeTime > m_iSenseTime), m_pAgent->HaveTurned());
	//
	// // m_pAgent->DisplayPosition(m_pThisPlayer->GetPosition(),
	// // m_pThisPlayer->GetAbsBodyDir()
	// // , m_pThisPlayer->GetAbsDir(), TRUE);
	//
	// PositionOrientation myPos = new PositionOrientation(position, Angle
	// .rad(dAbsDirection));
	//
	// return myPos;
	// }

	// @Override
	// public PositionOrientation localize(
	// HashMap<String, ILocalizationFlag> flags, float neckYawAngle,
	// float neckPitchAngle, IGyroRate gyro)
	// {
	// if (flags.size() < 3) {
	// // there are not 3 visible flags
	// logger.log(Level.FINER,
	// "Localization not possible, only have {0} flags", flags.size());
	// return null;
	// }
	//
	// // sort the flags according to their angle
	// List<ILocalizationFlag> sortedFlags = new ArrayList<ILocalizationFlag>(
	// flags.values());
	// Collections.sort(sortedFlags);
	//
	// double x1, x2, x3; // Flag-Positions
	// double y1, y2, y3; // Flag Positions
	// double x, y; // Localized Position
	// double xC, yC; // Support-Points
	// double xD, yD; // Support-Points
	// double p, q; // q = 1 / p
	// double tant45;
	// double alpha, beta;
	// double nenner;
	// double distance;
	// double ratio;
	// double gamma;
	// double iFlag1Direction;
	// double iFlag2Direction;
	// double iFlag3Direction;
	// double dAbsDirection;
	// // double dAbsDir = 0.0;
	// ILocalizationFlag flag1;
	// ILocalizationFlag flag2;
	// ILocalizationFlag flag3;
	// Vector3D position;
	// // Vector3D AbsPos;
	// // Vector3D pos1;
	// // Vector3D pos2;
	// // Vector3D pos3;
	//
	// // do some averaging
	// // int loops = sortedFlags.size() / 3;
	// // pos1 = sortedFlags.get(0);
	// // pos2 = sortedFlags.get(sortedFlags.size() / 3);
	// // pos3 = sortedFlags.get(sortedFlags.size() / 3 * 2);
	// // for (int i = 0; i < loops; i++)
	// // {
	// // left flag
	// flag1 = sortedFlags.get(0);
	// // mid flag
	// flag2 = sortedFlags.get(1);
	// // right flag
	// flag3 = sortedFlags.get(2);
	//
	// // set parameters
	// // Vector3D flag1Vector = new Vector3D(flag1.getDistance(), new Vector3D(
	// // flag1.getHorizontalDirection(), flag1.getLatitudalDirection()));
	// // Vector3D flag2Vector = new Vector3D(flag2.getDistance(), new Vector3D(
	// // flag2.getHorizontalDirection(), flag2.getLatitudalDirection()));
	// // Vector3D flag3Vector = new Vector3D(flag3.getDistance(), new Vector3D(
	// // flag3.getHorizontalDirection(), flag3.getLatitudalDirection()));
	//
	// x1 = flag1.getKnownPosition().getX();
	// x2 = flag2.getKnownPosition().getX();
	// x3 = flag3.getKnownPosition().getX();
	// y1 = flag1.getKnownPosition().getY();
	// y2 = flag2.getKnownPosition().getY();
	// y3 = flag3.getKnownPosition().getY();
	//
	// iFlag1Direction = flag1.getHorizontalDirection();
	// iFlag2Direction = flag2.getHorizontalDirection();
	// iFlag3Direction = flag3.getHorizontalDirection();
	//
	// alpha = (iFlag2Direction - iFlag1Direction);
	// beta = (iFlag3Direction - iFlag2Direction);
	//
	// // do calculations
	// xC = x1 + ((y2 - y1) / Math.tan(alpha));
	// yC = y1 - ((x2 - x1) / Math.tan(alpha));
	// xD = x3 + ((y3 - y2) / Math.tan(beta));
	// yD = y3 - ((x3 - x2) / Math.tan(beta));
	//
	// nenner = (yD - yC);
	// // ASSERT ((nenner < D_ZERO_N) || (nenner > D_ZERO_P));
	// p = (xD - xC) / nenner;
	// q = 1 / p;
	//
	// // calculate coordinates
	// // ASSERT ((nenner < D_ZERO_N) || (nenner > D_ZERO_P));
	// y = yC + ((x2 - xC + (q * (y2 - yC))) / (p + q));
	//
	// if (p < q) {
	// x = xC + (p * (y - yC));
	// } else {
	// x = x2 - (q * (y - y2));
	// }
	//
	// position = new Vector3D(x, y, 0);
	//
	// // calculate absoulte direction
	// distance = flag1.getDistance();
	// ratio = (y1 - y) / distance;
	// gamma = Math.asin(ratio);
	// if (x > x1)
	// if (y > y1)
	// dAbsDirection = -Math.PI - gamma - iFlag1Direction;
	// else
	// dAbsDirection = Math.PI - gamma - iFlag1Direction;
	// else
	// dAbsDirection = gamma - iFlag1Direction;
	//
	// // do some filtering
	// // AbsPos += position;
	// // dAbsDir += dAbsDirection;
	// // }
	//
	// // there are i points to measure
	// // if (i > 1)
	// // {
	// // AbsPos *= 1.0 / (double) i;
	// // dAbsDir /= (double) i;
	// // }
	//
	// // set this players coordinates
	// // m_pThisPlayer->SetPlayerAbsCoordinates(AbsPos, (int) dAbsDir,
	// // m_iHeadAngle,
	// // m_dSpeed, m_iViewAngle, m_pAgent->IsDashingBackward()
	// // , (m_iSeeTime > m_iSenseTime), m_pAgent->HaveTurned());
	//
	// // m_pAgent->DisplayPosition(m_pThisPlayer->GetPosition(),
	// // m_pThisPlayer->GetAbsBodyDir()
	// // , m_pThisPlayer->GetAbsDir(), TRUE);
	//
	// PositionOrientation myPos = new PositionOrientation(position, Angle
	// .rad(dAbsDirection));
	//
	// return myPos;
	// }

	// @Override
	// public PositionOrientation localize(
	// HashMap<String, ILocalizationFlag> flags, float neckYawAngle,
	// float neckPitchAngle, IGyroRate gyro)
	// {
	// if (flags.size() < 3) {
	// // there are not 3 visible flags
	// logger.log(Level.FINER,
	// "Localization not possible, only have {0} flags", flags.size());
	// return null;
	// }
	//
	// // sort the flags according to their angle
	// List<ILocalizationFlag> sortedFlags = new ArrayList<ILocalizationFlag>(
	// flags.values());
	// Collections.sort(sortedFlags);
	//
	// double x1, x2, x3;
	// double y1, y2, y3;
	// double x, y;
	// double y5y2, x5x2;
	// double y4y2, x4x2;
	// double tant45;
	// double alpha, beta;
	// double nenner;
	// double distance;
	// double ratio;
	// double gamma;
	// double iFlag1Direction;
	// double iFlag2Direction;
	// double iFlag3Direction;
	// double dAbsDirection;
	// // double dAbsDir = 0.0;
	// ILocalizationFlag flag1;
	// ILocalizationFlag flag2;
	// ILocalizationFlag flag3;
	// Vector3D position;
	// // Vector3D AbsPos;
	// // Vector3D pos1;
	// // Vector3D pos2;
	// // Vector3D pos3;
	//
	// // do some averaging
	// // int loops = sortedFlags.size() / 3;
	// // pos1 = sortedFlags.get(0);
	// // pos2 = sortedFlags.get(sortedFlags.size() / 3);
	// // pos3 = sortedFlags.get(sortedFlags.size() / 3 * 2);
	// // for (int i = 0; i < loops; i++)
	// // {
	// // left flag
	// flag1 = sortedFlags.get(0);
	// // mid flag
	// flag2 = sortedFlags.get(1);
	// // right flag
	// flag3 = sortedFlags.get(2);
	//
	// // set parameters
	// // Vector3D flag1Vector = new Vector3D(flag1.getDistance(), new Vector3D(
	// // flag1.getHorizontalDirection(), flag1.getLatitudalDirection()));
	// // Vector3D flag2Vector = new Vector3D(flag2.getDistance(), new Vector3D(
	// // flag2.getHorizontalDirection(), flag2.getLatitudalDirection()));
	// // Vector3D flag3Vector = new Vector3D(flag3.getDistance(), new Vector3D(
	// // flag3.getHorizontalDirection(), flag3.getLatitudalDirection()));
	//
	// x1 = flag1.getKnownPosition().getX();
	// x2 = flag2.getKnownPosition().getX();
	// x3 = flag3.getKnownPosition().getX();
	// y1 = flag1.getKnownPosition().getY();
	// y2 = flag2.getKnownPosition().getY();
	// y3 = flag3.getKnownPosition().getY();
	//
	// iFlag1Direction = flag1.getHorizontalDirection();
	// iFlag2Direction = flag2.getHorizontalDirection();
	// iFlag3Direction = flag3.getHorizontalDirection();
	//
	// alpha = iFlag3Direction - iFlag2Direction;
	// beta = (Math.PI * 2) - (iFlag2Direction - iFlag1Direction);
	//
	// // do calculations
	// x5x2 = (x3 - x2) - (y3 - y2) / Math.tan(alpha);
	// y5y2 = (y3 - y2) + (x3 - x2) / Math.tan(alpha);
	// x4x2 = (x1 - x2) - (y1 - y2) / Math.tan(beta);
	// y4y2 = (y1 - y2) + (x1 - x2) / Math.tan(beta);
	//
	// nenner = (x4x2 - x5x2);
	// // ASSERT ((nenner < D_ZERO_N) || (nenner > D_ZERO_P));
	// tant45 = (y4y2 - y5y2) / nenner;
	//
	// // calculate coordinates
	// nenner = (1 + tant45 * tant45);
	// // ASSERT ((nenner < D_ZERO_N) || (nenner > D_ZERO_P));
	// y = (y5y2 - x5x2 * tant45) / nenner + y2;
	// x = -(y - y2) * tant45 + x2;
	// position = new Vector3D(x, y, 0);
	//
	// // calculate absoulte direction
	// distance = flag1.getDistance();
	// ratio = (y1 - y) / distance;
	// gamma = Math.asin(ratio);
	// if (x > x1)
	// if (y > y1)
	// dAbsDirection = -Math.PI - gamma - iFlag1Direction;
	// else
	// dAbsDirection = Math.PI - gamma - iFlag1Direction;
	// else
	// dAbsDirection = gamma - iFlag1Direction;
	//
	// // do some filtering
	// // AbsPos += position;
	// // dAbsDir += dAbsDirection;
	// // }
	//
	// // there are i points to measure
	// // if (i > 1)
	// // {
	// // AbsPos *= 1.0 / (double) i;
	// // dAbsDir /= (double) i;
	// // }
	//
	// // set this players coordinates
	// // m_pThisPlayer->SetPlayerAbsCoordinates(AbsPos, (int) dAbsDir,
	// // m_iHeadAngle,
	// // m_dSpeed, m_iViewAngle, m_pAgent->IsDashingBackward()
	// // , (m_iSeeTime > m_iSenseTime), m_pAgent->HaveTurned());
	//
	// // m_pAgent->DisplayPosition(m_pThisPlayer->GetPosition(),
	// // m_pThisPlayer->GetAbsBodyDir()
	// // , m_pThisPlayer->GetAbsDir(), TRUE);
	//
	// PositionOrientation myPos = new PositionOrientation(position, Angle
	// .rad(dAbsDirection));
	//
	// return myPos;
	// }
}
