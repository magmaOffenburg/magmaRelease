/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.roles;

import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;

/**
 * @author Stephan Kammerer
 */
public class DummyRole extends Role
{
	public static final DummyRole INSTANCE = new DummyRole();

	public DummyRole()
	{
		super(null, "Dummy", 0.0f, -100, 100);
	}

	@Override
	public void update()
	{
		// do nothing
	}

	@Override
	protected IPose2D determinePosition()
	{
		return new Pose2D();
	}
}
