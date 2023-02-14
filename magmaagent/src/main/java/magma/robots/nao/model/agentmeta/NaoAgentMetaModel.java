/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.model.agentmeta;

import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.impl.BodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.impl.HingeJointConfiguration;
import hso.autonomy.agent.model.agentmeta.impl.SensorConfiguration;
import hso.autonomy.util.geometry.Pose3D;
import java.util.ArrayList;
import java.util.List;
import magma.agent.communication.perception.ISimsparkEffectorNames;
import magma.agent.communication.perception.ISimsparkPerceptorNames;
import magma.agent.model.agentmeta.impl.RoboCupAgentMetaModel;
import magma.robots.nao.INaoConstants;
import magma.robots.nao.INaoJoints;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class NaoAgentMetaModel extends RoboCupAgentMetaModel
		implements INaoConstants, INaoJoints, ISimsparkEffectorNames, ISimsparkPerceptorNames
{
	public static final String NAME = "Nao";

	public static final NaoAgentMetaModel INSTANCE = new NaoAgentMetaModel();

	public NaoAgentMetaModel()
	{
		this(NAME, ACTION_SCENE);
	}

	public NaoAgentMetaModel(String modelName, String sceneString)
	{
		super(modelName, sceneString, STATIC_PIVOT_POINT, Head, new Pose3D(), -60, Float.NEGATIVE_INFINITY, 0.6531f,
				300, 0.02f, 0.04f, TORSO_Z_UPRIGHT);
	}

	@Override
	protected List<IBodyPartConfiguration> createBodyPartConfigs()
	{
		List<IBodyPartConfiguration> configs = new ArrayList<>();

		IBodyPartConfiguration currentConfig;

		currentConfig = new BodyPartConfiguration(Torso, null, Vector3D.ZERO, 1.2171f, new Vector3D(0.1, 0.1, 0.18),
				null, Vector3D.ZERO, new SensorConfiguration("TorsoGyro", "torsoGyro"),
				new SensorConfiguration("TorsoAccel", "torsoAccel"), null, null, null, null, null);
		configs.add(currentConfig);

		currentConfig =
				new BodyPartConfiguration(Neck, Torso, new Vector3D(0, 0, 0.09), 0.05f, new Vector3D(0.03, 0.03, 0.08),
						new HingeJointConfiguration(
								NeckYaw, NeckYawP, NeckYawE, new Vector3D(0, 0, 1), -120, 120, MAX_JOINT_SPEED, false),
						Vector3D.ZERO);
		configs.add(currentConfig);

		currentConfig =
				new BodyPartConfiguration(Head, Neck, new Vector3D(0, 0, 0.065), 0.35f, new Vector3D(0.13, 0.13, 0.13),
						new HingeJointConfiguration(NeckPitch, NeckPitchP, NeckPitchE, new Vector3D(1, 0, 0), -45, 45,
								MAX_JOINT_SPEED, false),
						new Vector3D(0, 0, -0.005));
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(RShoulder, Torso, new Vector3D(0.098, 0, 0.075), 0.07f,
				new Vector3D(0.01, 0.01, 0.01),
				new HingeJointConfiguration(RShoulderPitch, RShoulderPitchP, RShoulderPitchE, Vector3D.PLUS_I, -120,
						120, MAX_JOINT_SPEED, false),
				Vector3D.ZERO);
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(RUpperArm, RShoulder, new Vector3D(0.01, 0.02, 0), 0.15f,
				new Vector3D(0.07, 0.08, 0.06),
				new HingeJointConfiguration(
						RShoulderYaw, RShoulderYawP, RShoulderYawE, Vector3D.PLUS_K, -95, 1, MAX_JOINT_SPEED, false),
				new Vector3D(-0.01, -0.02, 0));
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(RElbow, RUpperArm, new Vector3D(-0.01, 0.07, 0.009), 0.035f,
				new Vector3D(0.01, 0.01, 0.01),
				new HingeJointConfiguration(
						RArmRoll, RShoulderRollP, RShoulderRollE, Vector3D.PLUS_J, -120, 120, MAX_JOINT_SPEED, false),
				Vector3D.ZERO);
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(RLowerArm, RElbow, new Vector3D(0, 0.05, 0.0), 0.2f,
				new Vector3D(0.05, 0.11, 0.05),
				new HingeJointConfiguration(
						RArmYaw, RArmYawP, RArmYawE, new Vector3D(0, 0, 1), -1, 90, MAX_JOINT_SPEED, false),
				new Vector3D(0, -0.05, 0));
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(RHip1, Torso, new Vector3D(0.055, -0.01, -0.115), 0.09f,
				new Vector3D(0.01, 0.01, 0.01),
				new HingeJointConfiguration(RHipYawPitch, RHipYawP, RHipYawE, new Vector3D(-0.7071, 0, 0.7071), -90, 1,
						MAX_JOINT_SPEED, false),
				Vector3D.ZERO);
		configs.add(currentConfig);

		currentConfig =
				new BodyPartConfiguration(RHip2, RHip1, new Vector3D(0, 0, 0), 0.125f, new Vector3D(0.01, 0.01, 0.01),
						new HingeJointConfiguration(
								RHipRoll, RHipRollP, RHipRollE, new Vector3D(0, 1, 0), -45, 25, MAX_JOINT_SPEED, false),
						Vector3D.ZERO);
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(RThight, RHip2, new Vector3D(0, 0.01, -0.04), 0.275f,
				new Vector3D(0.07, 0.07, 0.14),
				new HingeJointConfiguration(
						RHipPitch, RHipPitchP, RHipPitchE, Vector3D.PLUS_I, -25, 100, MAX_JOINT_SPEED, false),
				new Vector3D(0, -0.01, 0.04));
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(RShank, RThight, new Vector3D(0, 0.005, -0.125), 0.225f,
				new Vector3D(0.08, 0.07, 0.11),
				new HingeJointConfiguration(
						RKneePitch, RKneePitchP, RKneePitchE, Vector3D.PLUS_I, -130, 1, MAX_JOINT_SPEED, false),
				new Vector3D(0, -0.01, 0.045));
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(RAnkle, RShank, new Vector3D(0, -0.01, -0.055), 0.125f,
				new Vector3D(0.01, 0.01, 0.01),
				new HingeJointConfiguration(
						RFootPitch, RFootPitchP, RFootPitchE, Vector3D.PLUS_I, -45, 75, MAX_JOINT_SPEED, false),
				Vector3D.ZERO);
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(RFoot, RAnkle, new Vector3D(0, 0.03, -0.035), 0.2f,
				new Vector3D(0.08, 0.16, 0.02),
				new HingeJointConfiguration(
						RFootRoll, RFootRollP, RFootRollE, Vector3D.PLUS_J, -25, 45, MAX_JOINT_SPEED, false),
				new Vector3D(0, -0.03, 0.035), null, null, new SensorConfiguration("RFootForce", "rf"), null, null,
				null, null);
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(LShoulder, Torso, new Vector3D(-0.098, 0, 0.075), 0.07f,
				new Vector3D(0.01, 0.01, 0.01),
				new HingeJointConfiguration(LShoulderPitch, LShoulderPitchP, LShoulderPitchE, Vector3D.PLUS_I, -120,
						120, MAX_JOINT_SPEED, false),
				Vector3D.ZERO);
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(LUpperArm, LShoulder, new Vector3D(-0.01, 0.02, 0), 0.15f,
				new Vector3D(0.07, 0.08, 0.06),
				new HingeJointConfiguration(
						LShoulderYaw, LShoulderYawP, LShoulderYawE, Vector3D.PLUS_K, -1, 95, MAX_JOINT_SPEED, false),
				new Vector3D(0.01, -0.02, 0));
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(LElbow, LUpperArm, new Vector3D(0.01, 0.07, 0.009), 0.035f,
				new Vector3D(0.01, 0.01, 0.01),
				new HingeJointConfiguration(
						LArmRoll, LShoulderRollP, LShoulderRollE, Vector3D.PLUS_J, -120, 120, MAX_JOINT_SPEED, false),
				Vector3D.ZERO);
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(LLowerArm, LElbow, new Vector3D(0, 0.05, 0.0), 0.2f,
				new Vector3D(0.05, 0.11, 0.05),
				new HingeJointConfiguration(
						LArmYaw, LArmYawP, LArmYawE, new Vector3D(0, 0, 1), -90, 1, MAX_JOINT_SPEED, false),
				new Vector3D(0, -0.05, 0));
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(LHip1, Torso, new Vector3D(-0.055, -0.01, -0.115), 0.09f,
				new Vector3D(0.01, 0.01, 0.01),
				new HingeJointConfiguration(LHipYawPitch, LHipYawP, LHipYawE, new Vector3D(-0.7071, 0, -0.7071), -90, 1,
						MAX_JOINT_SPEED, false),
				Vector3D.ZERO);
		configs.add(currentConfig);

		currentConfig =
				new BodyPartConfiguration(LHip2, LHip1, new Vector3D(0, 0, 0), 0.125f, new Vector3D(0.01, 0.01, 0.01),
						new HingeJointConfiguration(
								LHipRoll, LHipRollP, LHipRollE, new Vector3D(0, 1, 0), -25, 45, MAX_JOINT_SPEED, false),
						Vector3D.ZERO);
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(LThight, LHip2, new Vector3D(0, 0.01, -0.04), 0.275f,
				new Vector3D(0.07, 0.07, 0.14),
				new HingeJointConfiguration(
						LHipPitch, LHipPitchP, LHipPitchE, Vector3D.PLUS_I, -25, 100, MAX_JOINT_SPEED, false),
				new Vector3D(0, -0.01, 0.04));
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(LShank, LThight, new Vector3D(0, 0.005, -0.125), 0.225f,
				new Vector3D(0.08, 0.07, 0.11),
				new HingeJointConfiguration(
						LKneePitch, LKneePitchP, LKneePitchE, Vector3D.PLUS_I, -130, 1, MAX_JOINT_SPEED, false),
				new Vector3D(0, -0.01, 0.045));
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(LAnkle, LShank, new Vector3D(0, -0.01, -0.055), 0.125f,
				new Vector3D(0.01, 0.01, 0.01),
				new HingeJointConfiguration(
						LFootPitch, LFootPitchP, LFootPitchE, Vector3D.PLUS_I, -45, 75, MAX_JOINT_SPEED, false),
				Vector3D.ZERO);
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration(LFoot, LAnkle, new Vector3D(0, 0.03, -0.035), 0.2f,
				new Vector3D(0.08, 0.16, 0.02),
				new HingeJointConfiguration(
						LFootRoll, LFootRollP, LFootRollE, Vector3D.PLUS_J, -45, 25, MAX_JOINT_SPEED, false),
				new Vector3D(0, -0.03, 0.035), null, null, new SensorConfiguration("LFootForce", "lf"), null, null,
				null, null);
		configs.add(currentConfig);

		return configs;
	}

	protected BodyPartConfiguration getBodyPart(List<IBodyPartConfiguration> bodyPartConfigs, String name)
	{
		for (IBodyPartConfiguration config : bodyPartConfigs) {
			if (config.getName().equals(name)) {
				return (BodyPartConfiguration) config;
			}
		}
		return null;
	}

	@Override
	public boolean hasFootForceSensors()
	{
		return true;
	}
}
