/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmeta.ICameraConfiguration;
import hso.autonomy.agent.model.agentmodel.ICamera;
import hso.autonomy.agent.model.agentmodel.ICameraErrorModel;
import hso.autonomy.agent.model.agentmodel.IFieldOfView;
import hso.autonomy.agent.model.agentmodel.ISensor;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose3D;

/**
 * Implementation for a camera sensor detecting geometric features.
 *
 * @author Stefan Glaser
 */
public class Camera extends Sensor implements ICamera
{
	/** The field of view of the camera. */
	private final FieldOfView fov;

	/** The feature detection error model. */
	private ICameraErrorModel errorModel;

	public Camera(String name, String perceptorName, IPose3D pose, Angle horizontalFoV, Angle verticalFoV)
	{
		this(name, perceptorName, pose, horizontalFoV, verticalFoV, null);
	}

	public Camera(ICameraConfiguration config)
	{
		this(config.getName(), config.getPerceptorName(), config.getPose(), config.getHorizontalFoVRange(),
				config.getVerticalFoVRange(), null);
	}

	public Camera(String name, String perceptorName, IPose3D pose, Angle horizontalFoV, Angle verticalFoV,
			ICameraErrorModel errorModel)
	{
		super(name, perceptorName, pose);

		this.fov = new FieldOfView(horizontalFoV, verticalFoV);
		this.errorModel = errorModel;
	}

	public Camera(Camera source)
	{
		super(source);

		this.fov = source.fov;
		this.errorModel = source.errorModel;
	}

	@Override
	public IFieldOfView getFoV()
	{
		return fov;
	}

	@Override
	public ICameraErrorModel getErrorModel()
	{
		return errorModel;
	}

	@Override
	public void setErrorModel(ICameraErrorModel errorModel)
	{
		this.errorModel = errorModel;
	}

	@Override
	public void updateFromPerception(IPerception perception)
	{
		// TODO: update this sensor with perception
	}

	@Override
	public ISensor copy()
	{
		return new Camera(this);
	}
}
