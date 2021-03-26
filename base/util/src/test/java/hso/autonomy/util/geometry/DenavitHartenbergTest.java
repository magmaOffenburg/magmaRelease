/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.Test;

/**
 * @author Simon Raffeiner
 *
 */
public class DenavitHartenbergTest
{
	/**
	 * Test method for
	 * {@link hso.autonomy.util.geometry.DenavitHartenberg#transform(Vector3D,
	 * DenavitHartenbergParameters)}
	 */
	@Test
	public void testTransform()
	{
		Vector3D origin = new Vector3D(2.0, 1.0, 3.0);
		DenavitHartenbergParameters p = new DenavitHartenbergParameters(0, 0, 0, new Vector3D(-0.12, -0.005, 0));

		Vector3D result = DenavitHartenberg.transform(origin, p);
		GeometryTestUtil.compareVector3D(1.88, 0.995, 3.0, result);

		// Change theta value and try again
		p.setTheta(32.0);
		result = DenavitHartenberg.transform(origin, p);
		GeometryTestUtil.compareVector3D(1.04617, 1.90288, 3.0, result);
	}
}
