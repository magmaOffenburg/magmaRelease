/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.fullsearch;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import kdo.util.parameter.Parameter;
import kdo.util.parameter.ParameterList;
import magma.robots.nao.INaoJoints;

/**
 * @author Klaus Dorer
 */
public class FullSearchMovementParameters extends ParameterList
{
	public enum Mode { ANGLE_AND_SPEED, SPEED, ANGLE }

	/** general parameters */
	public enum Param { PHASES, MODE }

	/** parameters that are repeated for each phase */
	public enum Joint {
		TIME,

		NY,
		NP,

		LSP,
		LSY,
		LAR,
		LAY,

		RSP,
		RSY,
		RAR,
		RAY,

		LHYP,
		LHR,
		LHP,
		LKP,
		LFP,
		LFR,
		LTP,

		RHYP,
		RHR,
		RHP,
		RKP,
		RFP,
		RFR,
		RTP;

		public String time(int phase)
		{
			return TIME.name() + phase;
		}

		public String angle(int phase)
		{
			return name() + phase;
		}

		public String speed(int phase)
		{
			return name() + "S" + phase;
		}

		public String jointName()
		{
			switch (this) {
			case NY:
				return INaoJoints.NeckYaw;
			case NP:
				return INaoJoints.NeckPitch;

			case LSP:
				return INaoJoints.LShoulderPitch;
			case LSY:
				return INaoJoints.LShoulderYaw;
			case LAR:
				return INaoJoints.LArmRoll;
			case LAY:
				return INaoJoints.LArmYaw;

			case RSP:
				return INaoJoints.RShoulderPitch;
			case RSY:
				return INaoJoints.RShoulderYaw;
			case RAR:
				return INaoJoints.RArmRoll;
			case RAY:
				return INaoJoints.RArmYaw;

			case LHYP:
				return INaoJoints.LHipYawPitch;
			case LHR:
				return INaoJoints.LHipRoll;
			case LHP:
				return INaoJoints.LHipPitch;
			case LKP:
				return INaoJoints.LKneePitch;
			case LFP:
				return INaoJoints.LFootPitch;
			case LFR:
				return INaoJoints.LFootRoll;
			case LTP:
				return INaoJoints.LToePitch;

			case RHYP:
				return INaoJoints.RHipYawPitch;
			case RHR:
				return INaoJoints.RHipRoll;
			case RHP:
				return INaoJoints.RHipPitch;
			case RKP:
				return INaoJoints.RKneePitch;
			case RFP:
				return INaoJoints.RFootPitch;
			case RFR:
				return INaoJoints.RFootRoll;
			case RTP:
				return INaoJoints.RToePitch;

			default:
				return null;
			}
		}
	}

	private enum Dummy {}

	/** flag to allow printing names only once during a run */
	private static boolean printedNames = false;

	private final List<String> customParams;

	private List<Joint> activeJoints;

	public FullSearchMovementParameters(double[] params)
	{
		this(params, Dummy.values());
	}

	protected <T extends Enum<T>> FullSearchMovementParameters(double[] params, T[] customParams)
	{
		this.customParams = Arrays.stream(customParams).map(Enum::name).collect(Collectors.toList());

		for (int i = 0; i < params.length; i++) {
			put(getParamKey(i), (float) params[i]);
		}
	}

	/**
	 * The names are assumed to be like NON-PHASE parameters Phase parameters
	 * angles left, right if existing phase parameters speed left, right
	 *
	 * @param index the index of the parameter to get
	 * @return the name of the i-th parameter
	 */
	protected String getParamKey(int index)
	{
		// non phase params
		int nonPhaseParams = Param.values().length;
		if (index < nonPhaseParams) {
			return Param.values()[index].name();
		}
		index -= nonPhaseParams;

		// custom params
		if (index < customParams.size()) {
			return customParams.get(index);
		}
		index -= customParams.size();

		// activation params
		int activationParams = Joint.values().length - 1; // all except time
		if (index < activationParams) {
			return Joint.values()[index + 1].name();
		}
		index -= activationParams;

		if (activeJoints == null) {
			activeJoints = Arrays.stream(Joint.values())
								   .filter(joint -> joint == Joint.TIME || isActive(joint))
								   .collect(Collectors.toList());
		}

		// phase params
		int phaseParamNames = activeJoints.size();
		int phaseParams = phaseParamNames;
		Mode mode = getMode();
		if (mode == Mode.ANGLE_AND_SPEED) {
			// all params but time are duplicated
			phaseParams = (2 * phaseParams) - 1;
		}

		int phase = index / phaseParams;
		int paramIndex = index % phaseParams;
		String suffix = "";

		if (mode == Mode.SPEED && paramIndex != Joint.TIME.ordinal()) {
			suffix = "S";
		} else if (mode == Mode.ANGLE_AND_SPEED && paramIndex >= phaseParamNames) {
			suffix = "S";
			paramIndex = (paramIndex - phaseParamNames) + 1;
		}

		return activeJoints.get(paramIndex).name() + suffix + phase;
	}

	@Override
	protected String getParamCode(String key)
	{
		if (key.equals(Param.PHASES.name()) || key.equals(Param.MODE.name()) || customParams.contains(key)) {
			return super.getParamCode(key);
		}

		int phase = Integer.parseInt(Character.toString(key.charAt(key.length() - 1)));
		String name = key.substring(0, key.length() - 1);
		String function = "angle";
		if (name.equals(Joint.TIME.name())) {
			function = "time";
		} else if (name.endsWith("S")) {
			function = "speed";
			name = name.substring(0, name.length() - 1);
		}

		return String.format("Param.%s.%s(%d)", name, function, phase);
	}

	public float get(Param param)
	{
		return get(param.name());
	}

	public void put(Param param, float value)
	{
		put(param.name(), value);
	}

	public float time(int phase)
	{
		return get(Joint.TIME.name() + phase);
	}

	public boolean isActive(Joint joint)
	{
		return get(joint.name()) == 1;
	}

	public float angle(int phase, Joint param)
	{
		return get(param.name() + phase);
	}

	public float speed(int phase, Joint param)
	{
		return get(param.name() + "S" + phase);
	}

	public int getPhases()
	{
		return (int) get(Param.PHASES);
	}

	public Mode getMode()
	{
		return Mode.values()[(int) get(Param.MODE)];
	}

	@Override
	public String getParamsString()
	{
		StringBuilder values = new StringBuilder(1000);
		StringBuilder names = new StringBuilder(1000);
		values.append("\ndouble[] p = { ");
		names.append("\n");
		for (String key : parameters.keySet()) {
			names.append(key).append(",");
			Parameter param = parameters.get(key);
			float value = param.getValue();
			if (value == Math.round(value)) {
				values.append(Math.round(value));
			} else {
				values.append(String.format(Locale.US, "%6.4f", value));
			}
			values.append(",");
		}
		values.deleteCharAt(values.length() - 1);
		values.append(" };");
		if (!printedNames) {
			// print names only once
			System.out.println("Parameter Names: " + names.toString());
			printedNames = true;
		}
		return values.toString();
	}
}