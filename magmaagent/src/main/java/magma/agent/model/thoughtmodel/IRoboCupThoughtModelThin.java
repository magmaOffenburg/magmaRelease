package magma.agent.model.thoughtmodel;

public interface IRoboCupThoughtModelThin {
	/**
	 * Sets the values for the dash command.
	 * @param x the x parameter for the dash
	 * @param y the y parameter for the dash
	 * @param desiredTurn the desired turn for the dash
	 */
	void setDash(float x, float y, float desiredTurn);

	/**
	 * Sets the values for the kick command.
	 * @param power [0..100] desired kick distance in m
	 * @param horizontalAngle [-180..180] horizontal direction (minus is right). Not all angles might be supported by
	 * the proxy agent.
	 * @param verticalAngle [0..90] the vertical angle of the desired ball start trajectory. Not all angles might be
	 * supported by the proxy agent
	 */
	void setKick(float power, float horizontalAngle, float verticalAngle);
}
