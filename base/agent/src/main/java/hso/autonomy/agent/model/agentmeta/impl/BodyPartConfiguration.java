/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmeta.impl;

import hso.autonomy.agent.model.agentmeta.IActuatorConfiguration;
import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.ICameraConfiguration;
import hso.autonomy.agent.model.agentmeta.IHingeJointConfiguration;
import hso.autonomy.agent.model.agentmeta.ISensorConfiguration;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Class for representing a configuration of a body part of our agent.
 *
 * @author Stefan Glaser
 */
public class BodyPartConfiguration implements IBodyPartConfiguration
{
	/** The name of the body part. */
	private final String name;

	/** The name of the parent body part or null, if this body is the root body part. */
	private final String parent;

	/** The translation from the parent body part to this body part with zero joint angle. */
	private Vector3D translation;

	/** The mass of this body part. */
	private float mass;

	/** The geometry (bounding box) of this body part. */
	private Vector3D geometry;

	/**
	 * The configuration of the hinge joint connecting this body part to its parent body part, or null in case this body
	 * part is the root body part.
	 */
	private final IHingeJointConfiguration jointConfig;

	/** The anchor of the joint (its location relative to this body part). */
	private Vector3D jointAnchor;

	/**
	 * The configuration of the gyro-rate sensor attached to this body part or null, if no such sensor is attached to
	 * this body part.
	 */
	private final ISensorConfiguration gyroRateConfig;

	/**
	 * The configuration of the accelerometer sensor attached to this body part or null, if no such sensor is attached
	 * to this body part.
	 */
	private final ISensorConfiguration accelerometerConfig;

	/**
	 * The configuration of the force-resistance sensor attached to this body part or null, if no such sensor is
	 * attached to this body part.
	 */
	private final ISensorConfiguration forceResistanceConfig;

	/**
	 * The configuration of the IMU sensor attached to this body part or null, if no such sensor is attached to
	 * this body part.
	 */
	private final ISensorConfiguration imuConfig;

	/**
	 * The configuration of the compass sensor attached to this body part or null, if no such sensor is attached to
	 * this body part.
	 */
	private final ISensorConfiguration compassConfig;

	/**
	 * The configuration of the camera sensor attached to this body part or null, if no such sensor is attached to
	 * this body part.
	 */
	private final ICameraConfiguration cameraConfig;

	/**
	 * The list of configurations of light actuators attached to this body part or null, if no such actuators are
	 * attached to this body part.
	 */
	private final List<IActuatorConfiguration> lightConfigs;

	public BodyPartConfiguration(String name, String parent, Vector3D translation, float mass, Vector3D geometry,
			IHingeJointConfiguration jointConfig, Vector3D jointAnchor, ISensorConfiguration gyroRateConfig,
			ISensorConfiguration accelerometerConfig, ISensorConfiguration forceResistanceConfig,
			ISensorConfiguration imuConfig, ISensorConfiguration compassConfig, ICameraConfiguration cameraConfig,
			List<IActuatorConfiguration> lightConfigs)
	{
		this.name = name;
		this.parent = parent;
		this.translation = translation;
		this.mass = mass;
		this.geometry = geometry;
		this.jointConfig = jointConfig;
		this.jointAnchor = jointAnchor;
		this.gyroRateConfig = gyroRateConfig;
		this.accelerometerConfig = accelerometerConfig;
		this.forceResistanceConfig = forceResistanceConfig;
		this.imuConfig = imuConfig;
		this.compassConfig = compassConfig;
		this.cameraConfig = cameraConfig;
		this.lightConfigs = lightConfigs;
	}

	public BodyPartConfiguration(String name, String parent, Vector3D translation, float mass, Vector3D geometry,
			IHingeJointConfiguration jointConfig, Vector3D jointAnchor)
	{
		this(name, parent, translation, mass, geometry, jointConfig, jointAnchor, null, null, null, null, null, null,
				null);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getParent()
	{
		return parent;
	}

	@Override
	public boolean isRootBody()
	{
		return parent == null || jointConfig == null;
	}

	@Override
	public Vector3D getTranslation()
	{
		return translation;
	}

	@Override
	public float getMass()
	{
		return mass;
	}

	@Override
	public Vector3D getGeometry()
	{
		return geometry;
	}

	@Override
	public IHingeJointConfiguration getJointConfiguration()
	{
		return jointConfig;
	}

	@Override
	public Vector3D getJointAnchor()
	{
		return jointAnchor;
	}

	@Override
	public ISensorConfiguration getGyroRateConfiguration()
	{
		return gyroRateConfig;
	}

	@Override
	public ISensorConfiguration getAccelerometerConfiguration()
	{
		return accelerometerConfig;
	}

	@Override
	public ISensorConfiguration getForceResistanceConfiguration()
	{
		return forceResistanceConfig;
	}

	@Override
	public ISensorConfiguration getIMUConfiguration()
	{
		return imuConfig;
	}

	@Override
	public ISensorConfiguration getCompassConfig()
	{
		return compassConfig;
	}

	@Override
	public ICameraConfiguration getCameraConfig()
	{
		return cameraConfig;
	}

	public BodyPartConfiguration setTranslation(Vector3D translation)
	{
		this.translation = translation;
		return this;
	}

	public BodyPartConfiguration setMass(float mass)
	{
		this.mass = mass;
		return this;
	}

	public BodyPartConfiguration setGeometry(Vector3D geometry)
	{
		this.geometry = geometry;
		return this;
	}

	public BodyPartConfiguration setJointAnchor(Vector3D jointAnchor)
	{
		this.jointAnchor = jointAnchor;
		return this;
	}

	@Override
	public List<IActuatorConfiguration> getLightConfigs()
	{
		return lightConfigs;
	}
}
