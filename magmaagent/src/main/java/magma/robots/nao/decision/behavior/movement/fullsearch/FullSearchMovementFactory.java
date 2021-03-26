/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.fullsearch;

import hso.autonomy.agent.model.agentmodel.IHingeJointR;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import magma.agent.decision.behavior.movement.Movement;
import magma.agent.decision.behavior.movement.MovementPhase;
import magma.robots.nao.INaoConstants;
import magma.robots.nao.decision.behavior.movement.fullsearch.FullSearchMovementParameters.Joint;

/**
 * Creates Movement instances from FullSearchMovementParameters.
 */
public class FullSearchMovementFactory
{
	private static final String MOVEMENT_NAME = "fullSearchRight";

	public static Movement create(FullSearchMovementParameters params, IThoughtModel thoughtModel)
	{
		switch (params.getMode()) {
		case ANGLE:
			return createByAngle(params);
		case SPEED:
			return createBySpeed(params, thoughtModel);
		case ANGLE_AND_SPEED:
			return createByAngleAndSpeed(params);
		}
		return null;
	}

	private static Movement create(FullSearchMovementParameters params, BiFunction<Integer, Joint, Float> getAngle,
			BiFunction<Integer, Joint, Float> getSpeed)
	{
		Movement movement = new Movement(MOVEMENT_NAME);
		for (int i = 0; i < params.getPhases(); i++) {
			MovementPhase phase = new MovementPhase("phase" + i, (int) params.time(i));
			BiConsumer<Integer, Joint> add = (index, joint) ->
			{
				if (params.isActive(joint)) {
					phase.add(joint.jointName(), getAngle.apply(index, joint), getSpeed.apply(index, joint));
				}
			};

			add.accept(i, Joint.NY);
			add.accept(i, Joint.NP);

			add.accept(i, Joint.LSP);
			add.accept(i, Joint.LSY);
			add.accept(i, Joint.LAR);
			add.accept(i, Joint.LAY);

			add.accept(i, Joint.RSP);
			add.accept(i, Joint.RSY);
			add.accept(i, Joint.RAR);
			add.accept(i, Joint.RAY);

			add.accept(i, Joint.LHYP);
			add.accept(i, Joint.LHR);
			add.accept(i, Joint.LHP);
			add.accept(i, Joint.LKP);
			add.accept(i, Joint.LFP);
			add.accept(i, Joint.LFR);
			add.accept(i, Joint.LTP);

			add.accept(i, Joint.RHYP);
			add.accept(i, Joint.RHR);
			add.accept(i, Joint.RHP);
			add.accept(i, Joint.RKP);
			add.accept(i, Joint.RFP);
			add.accept(i, Joint.RFR);
			add.accept(i, Joint.RTP);

			movement.add(phase);
		}
		return movement;
	}

	public static Movement createByAngleAndSpeed(FullSearchMovementParameters p)
	{
		return create(p, p::angle, p::speed);
	}

	public static Movement createBySpeed(FullSearchMovementParameters p, IThoughtModel tm)
	{
		return create(p, (phase, joint) -> angle(p.speed(phase, joint), joint.jointName(), tm), p::speed);
	}

	public static Movement createByAngle(FullSearchMovementParameters p)
	{
		return create(p, p::angle, (phase, joint) -> speed(p, phase, joint));
	}

	public static float angle(double speed, String jointName, IThoughtModel thoughtModel)
	{
		IHingeJointR hj = thoughtModel.getAgentModel().getHJ(jointName);
		if (speed < 0) {
			return hj.getMinAngle();
		}
		return hj.getMaxAngle();
	}

	public static float speed(FullSearchMovementParameters p, int i, Joint joint)
	{
		// we round since a time of 0.3 means we run for 1 cycle
		int deltaT = (int) Math.ceil(p.time(i));
		if (i > 0) {
			float deltaAngle = p.angle(i, joint) - p.angle(i - 1, joint);
			return Math.min(Math.abs(deltaAngle / deltaT), INaoConstants.MAX_JOINT_SPEED);
		} else {
			// for the first phase we do not know the previous angle
			return INaoConstants.MAX_JOINT_SPEED;
		}
	}
}
