/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.supportPoint;

import hso.autonomy.util.function.FunctionLoader;
import hso.autonomy.util.function.IFunction;
import hso.autonomy.util.function.PiecewiseLinearFunction;
import hso.autonomy.util.function.SupportPoint;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Klaus Dorer, Stefan Glaser
 */
public class FunctionBehaviorParameters implements Serializable
{
	public static final String BEHAVIOR_PATH = "config/behaviors/nao/";

	public static final String EXTENSION = ".mfb";

	/** map containing an entry for all joints involved in the behavior */
	private final Map<String, IFunction> joints;

	/** the name of the corresponding behavior */
	private String name;

	/** the number of cycles the behavior takes */
	private final float period;

	/**
	 * whether a mirrored version of this behavior should be generated when
	 * loading it
	 */
	private final boolean mirror;

	private final String metaModel;

	public static FunctionBehaviorParameters fromJointSupportPoints(String name, float duration, boolean mirror,
			String metaModel, Map<String, ArrayList<SupportPoint>> jointPoints)
	{
		Map<String, IFunction> jointFunctions = new HashMap<>();
		for (String jointName : jointPoints.keySet()) {
			PiecewiseLinearFunction function = new PiecewiseLinearFunction(jointPoints.get(jointName), 1);
			jointFunctions.put(jointName, function.simplify());
		}
		return new FunctionBehaviorParameters(name, duration, mirror, metaModel, jointFunctions);
	}

	public FunctionBehaviorParameters(
			String name, float duration, boolean mirror, String metaModel, Map<String, IFunction> jointFunctions)
	{
		this.name = name;
		this.period = duration;
		this.mirror = mirror;
		this.joints = jointFunctions;
		this.metaModel = metaModel;
	}

	/**
	 * Writes a function motor file
	 * @param filename the name to use as filename
	 */
	public void writeToFile(String filename)
	{
		try {
			writeBehaviorFile(new File(filename + EXTENSION), period, mirror, metaModel, joints);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the function motor file to FunctionBehaviorParameter.BEHAVIOR_PATH.
	 */
	public void writeToFile()
	{
		writeToFile(BEHAVIOR_PATH + name);
	}

	/**
	 * @return a map of functions for each joint that is involved in the behavior
	 */
	public Map<String, IFunction> getJoints()
	{
		return joints;
	}

	public String getName()
	{
		return name;
	}

	public float getPeriod()
	{
		return period;
	}

	public boolean getMirror()
	{
		return mirror;
	}

	public String getMetaModel()
	{
		return metaModel;
	}

	public FunctionBehaviorParameters getMirroredVersion()
	{
		Map<String, IFunction> newJoints = new HashMap<>();
		Map<String, IFunction> oldJoints = getJoints();

		for (String oldName : oldJoints.keySet()) {
			IFunction oldFunction = oldJoints.get(oldName);
			// we have to create a copy of the function in order not to change the
			// original
			IFunction newFunction = oldFunction.copy();

			if (oldName.startsWith("R")) {
				mirror("L", oldName, newFunction, newJoints);

			} else if (oldName.startsWith("L")) {
				mirror("R", oldName, newFunction, newJoints);

			} else {
				// joint without side, no mirror
				newJoints.put(oldName, newFunction);
			}
		}

		return new FunctionBehaviorParameters(name, getPeriod(), false, metaModel, newJoints);
	}

	private void mirror(String replacement, String oldName, IFunction oldFunction, Map<String, IFunction> newJoints)
	{
		String newName = replacement + oldName.substring(1, oldName.length());
		IFunction newFunction = oldFunction;
		if ((oldName.contains("Roll") || oldName.contains("Yaw")) && !oldName.contains("ArmRoll")) {
			// roll support points have to be negated in y values
			// exception for arm roll which is misnamed and is a pitch joint!
			newFunction.mirrorYFunction();
		}
		newJoints.put(newName, newFunction);
	}

	/**
	 * Create a new {@link FunctionBehaviorParameters} instance from the given
	 * behavior file.
	 *
	 * @param fileName - the file to read
	 * @return a new {@link FunctionBehaviorParameters} instance
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static FunctionBehaviorParameters readBehaviorFile(String fileName) throws FileNotFoundException, IOException
	{
		ClassLoader cl = FunctionBehaviorParameters.class.getClassLoader();
		InputStream is = cl.getResourceAsStream(fileName);
		if (is == null) {
			is = new FileInputStream(fileName);
		}
		BufferedReader in2 = new BufferedReader(new InputStreamReader(is));

		return readBehaviorFile(in2, new File(fileName));
	}

	/**
	 * Create a new {@link FunctionBehaviorParameters} instance from the given
	 * behavior file <b>without throwing Exceptions</b>.
	 *
	 * @param file - the file to read
	 * @param silent - whether to print stack traces
	 * @return a new {@link FunctionBehaviorParameters} instance
	 */
	public static FunctionBehaviorParameters readBehaviorFileNoEx(File file, boolean silent)
	{
		try {
			return readBehaviorFile(new BufferedReader(new FileReader(file)), file);
		} catch (IOException e) {
			if (!silent) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * Create a new {@link FunctionBehaviorParameters} instance from the given
	 * behavior file.
	 *
	 * @param file - the file to read
	 * @return a new {@link FunctionBehaviorParameters} instance
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static FunctionBehaviorParameters readBehaviorFile(File file) throws FileNotFoundException, IOException
	{
		return readBehaviorFile(new BufferedReader(new FileReader(file)), file);
	}

	/**
	 * Create a new {@link FunctionBehaviorParameters} instance from the given
	 * input reader.
	 *
	 * @param in - an buffered reader instance
	 * @return a new {@link FunctionBehaviorParameters} instance
	 * @throws IOException
	 */
	public static FunctionBehaviorParameters readBehaviorFile(BufferedReader in, File file) throws IOException
	{
		String name = file.getName();
		float duration;
		boolean mirror = false;
		String metaModel;
		Map<String, IFunction> jointFunctions = new HashMap<>();

		if (name.endsWith(EXTENSION)) {
			name = file.getName().replace(EXTENSION, "");
		} else {
			return null;
		}

		try {
			String line;
			String[] csvLineContents;

			line = in.readLine();
			csvLineContents = line.split(",");

			// read the date from the first line
			duration = Float.valueOf(csvLineContents[0]);
			mirror = Boolean.valueOf(csvLineContents[1]);
			metaModel = csvLineContents[2];

			// read all data for each joint
			while ((line = in.readLine()) != null) {
				int sepIndex = line.indexOf(",");
				String jointName = line.substring(0, sepIndex);
				String functionString = line.substring(sepIndex + 1, line.length());
				try {
					IFunction jointFunction = FunctionLoader.INSTANCE.fromCSVString(functionString);

					jointFunctions.put(jointName, jointFunction);
				} catch (RuntimeException rte) {
					System.err.println("Error loading function: \"" + functionString + "\" for Joint: \"" + jointName +
									   "\" of behavior \"" + name + "\". Skipping function!");
				}
			}
		} catch (IOException e) {
			in.close();
			throw e;
		}

		in.close();

		return new FunctionBehaviorParameters(name, duration, mirror, metaModel, jointFunctions);
	}

	/**
	 * Write a function-behavior-file with the given
	 * {@link FunctionBehaviorParameters}.
	 *
	 * @param file - the file to write to
	 * @param parameter - the parameter which should be written to the file
	 * @throws IOException
	 */
	public static void writeBehaviorFile(File file, FunctionBehaviorParameters parameter) throws IOException
	{
		writeBehaviorFile(file, parameter.period, parameter.mirror, parameter.metaModel, parameter.joints);
	}

	/**
	 * Write a function-behavior-file with the given information.
	 *
	 * @param file - the file to write to
	 * @param duration - the duration of the behavior
	 * @param jointFunctions - the joint functions of this behaviors
	 * @throws IOException
	 */
	public static void writeBehaviorFile(File file, float duration, boolean mirror, String metaModel,
			Map<String, IFunction> jointFunctions) throws IOException
	{
		PrintWriter out = new PrintWriter(new FileWriter(file));
		out.println(String.join(",", Float.toString(duration), Boolean.toString(mirror), metaModel));

		// write all data for each joint
		IFunction jointFunction;
		for (String key : jointFunctions.keySet()) {
			jointFunction = jointFunctions.get(key);
			out.println(key + "," + FunctionLoader.INSTANCE.toCSVString(jointFunction));
		}

		out.close();
	}
}
