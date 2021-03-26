/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.supportPoint;

import hso.autonomy.agent.decision.behavior.basic.Behavior;
import hso.autonomy.agent.model.agentmodel.IHingeJoint;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.function.IFunction;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Behavior representing motor commands based on a mathematical function
 *
 * @author Klaus Dorer
 */
public class FunctionBehavior extends Behavior
{
	private boolean finished;

	protected float currentStep;

	/** motor manager for each motor involved in the behavior */
	protected transient final Map<String, MotorManager> motorManagers;

	/** the number of values after which the motor function repeats */
	protected float period;

	/** path to the file that has the underlying behavior definition */
	protected String filePath;

	/**
	 * Whether this behavior is the mirrored / left version of the original file.
	 */
	private boolean isMirrored;

	public FunctionBehavior(String name, IThoughtModel thoughtModel, FunctionBehaviorParameters params, String filePath,
			boolean isMirrored)
	{
		super(name, thoughtModel);
		this.currentStep = 0.0f;
		this.motorManagers = new HashMap<>();
		this.filePath = filePath;
		this.isMirrored = isMirrored;
		extractMotorManagers(params);
		period = params.getPeriod();
	}

	/**
	 * Extract motor managers from behavior parameters
	 */
	private void extractMotorManagers(FunctionBehaviorParameters parameters)
	{
		if (isMirrored) {
			parameters = parameters.getMirroredVersion();
		}

		Map<String, IFunction> joints = parameters.getJoints();
		motorManagers.clear();

		for (String jointName : joints.keySet()) {
			MotorManager manager = new MotorManager(getAgentModel(), jointName, joints.get(jointName));
			motorManagers.put(jointName, manager);
		}
	}

	public void init()
	{
		finished = false;
		currentStep = 0f;
	}

	@Override
	public boolean isFinished()
	{
		return finished;
	}

	@Override
	public void stayIn()
	{
		super.stayIn();
		if (isFinished()) {
			init();
		}
	}

	@Override
	public void perform()
	{
		super.perform();

		// loop through all joints affected
		for (MotorManager manager : motorManagers.values()) {
			IHingeJoint hingeJoint = manager.getHingeJoint();
			if (hingeJoint != null) {
				hingeJoint.setFutureValues(manager.getDesiredAngle(currentStep),
						manager.getSpeedAtDesiredAngle(currentStep),
						manager.getAccelerationAtDesiredAngle(currentStep));
			}
		}

		currentStep++;
		if (currentStep >= period) {
			finished = true;
		}
	}

	/**
	 * Retrieve the absolute path to the movement data file
	 *
	 * @return Absolute path
	 */
	public String getAbsoluteFilePath()
	{
		File file = new File(filePath);

		if (!file.isAbsolute()) {
			// workaround for being able to save files specified with relative
			// path
			file = new File(file.getAbsolutePath().replaceFirst("behaviors", "config/behaviors"));
		}

		return file.getAbsolutePath();
	}

	public void reload(String filePath)
	{
		FunctionBehaviorParameters parameters = null;
		try {
			parameters = FunctionBehaviorParameters.readBehaviorFile(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		extractMotorManagers(parameters);
		period = parameters.getPeriod();
	}
}
