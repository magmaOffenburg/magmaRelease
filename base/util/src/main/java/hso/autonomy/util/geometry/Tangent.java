/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

/**
 *
 * @author Stefan Grossmann
 *
 */
public class Tangent
{
	/** startpoint */
	private Pose2D p1;

	/** endpoint */
	private Pose2D p2;

	public Tangent(Pose2D p1, Pose2D p2)
	{
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Tangent) {
			Tangent t = (Tangent) other;
			if ((this.p1.equals(t.p1) && this.p2.equals(t.p2)) || (this.p1.equals(t.p2) && this.p2.equals(t.p1)))
				return true;
		}
		return false;
	}

	// Getter and Setter
	public Pose2D getP1()
	{
		return p1;
	}

	public void setP1(Pose2D p1)
	{
		this.p1 = p1;
	}

	public Pose2D getP2()
	{
		return p2;
	}

	public void setP2(Pose2D p2)
	{
		this.p2 = p2;
	}
}
