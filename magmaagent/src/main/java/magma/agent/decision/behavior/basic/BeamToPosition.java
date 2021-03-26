/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.basic;

import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose3D;
import magma.agent.decision.behavior.IBeam;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * This Behavior is to beam the agent somewhere to the field
 *
 * @author Ingo Schindler
 */
public class BeamToPosition extends RoboCupBehavior implements IBeam
{
	private IPose2D target;

	protected BeamToPosition(String name, IRoboCupThoughtModel thoughtModel)
	{
		super(name, thoughtModel);
	}

	public BeamToPosition(IRoboCupThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.BEAM_TO_POSITION, thoughtModel);
	}

	@Override
	public void perform()
	{
		super.perform();

		IRoboCupWorldModel worldModel = getWorldModel();
		double previousZ = worldModel.getThisPlayer().getPosition().getZ();
		getAgentModel().beamToPosition(target);
		worldModel.resetLocalizer(new Pose3D(new Vector3D(target.getX(), target.getY(), previousZ),
				Geometry.createZRotation(target.getAngle().radians())));
	}

	@Override
	public void setPose(IPose2D target)
	{
		this.target = target;
	}

	@Override
	public IPose2D getPose()
	{
		return target;
	}
}
